package com.mall.ware2.service.impl;

import com.mall.common.constant.WareConstant;
import com.mall.ware2.entity.PurchaseDetailEntity;
import com.mall.ware2.service.PurchaseDetailService;
import com.mall.ware2.service.WareSkuService;
import com.mall.ware2.vo.DoneReqVo;
import com.mall.ware2.vo.MergeReqVo;
import com.mall.ware2.vo.item;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mall.common.utils.PageUtils;
import com.mall.common.utils.Query;

import com.mall.ware2.dao.PurchaseDao;
import com.mall.ware2.entity.PurchaseEntity;
import com.mall.ware2.service.PurchaseService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Slf4j
@Service("purchaseService")
public class PurchaseServiceImpl extends ServiceImpl<PurchaseDao, PurchaseEntity> implements PurchaseService {
    @Autowired
    private PurchaseDetailService purchaseDetailService;

    @Autowired
    private WareSkuService wareSkuService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<PurchaseEntity> page = this.page(
                new Query<PurchaseEntity>().getPage(params),
                new QueryWrapper<PurchaseEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public PageUtils getUnclaimedPurchase(Map<String, Object> params) {
        QueryWrapper<PurchaseEntity> wrapper = new QueryWrapper<>();
        String key = (String) params.get("key");
        if (!StringUtils.isEmpty(key)) {
            wrapper.and(w -> {
                w.eq("id", key).or().like("assignee_name", key);
            });
        }
        IPage<PurchaseEntity> page = this.page(
                new Query<PurchaseEntity>().getPage(params),
                wrapper.eq("status", 0).or().eq("status", 1)
        );
        return new PageUtils(page);
    }

    /**
     * 合并采购单
     *
     * @param mergeReqVo
     */
    @Transactional
    @Override
    public void mergePurchase(MergeReqVo mergeReqVo) {
        Long purchaseId = mergeReqVo.getPurchaseId();
        if (purchaseId == null) {
            // 新建的采购单
            PurchaseEntity purchaseEntity = new PurchaseEntity();
            purchaseEntity.setStatus(WareConstant.PurchaseStatusEnum.CREATE.getCode());
            baseMapper.insert(purchaseEntity);
            purchaseId = purchaseEntity.getId();
        } else {
            // 确认采购单状态为0或者1
            log.info("采购单状态为" + purchaseId);
            PurchaseEntity purchaseEntity = baseMapper.selectById(purchaseId);
            boolean aBoolean = purchaseEntity.getStatus().intValue() != WareConstant.PurchaseStatusEnum.CREATE.getCode();
            boolean bBoolean = purchaseEntity.getStatus().intValue() != WareConstant.PurchaseStatusEnum.ASSIGNED.getCode();
            log.info("采购单状态为" + aBoolean);

            if (aBoolean) {
                log.info("采购单状态为" + purchaseEntity.getStatus());
                log.info("mubiao"+ WareConstant.PurchaseStatusEnum.CREATE.getCode());
                log.error("采购单状态不对，无法合并");
                return;
            } else if (bBoolean) {
                log.info("采购单状态为" + purchaseEntity.getStatus());
                log.info("mubiao"+ WareConstant.PurchaseStatusEnum.CREATE.getCode());
                log.error("采购单状态不对，无法合并");
                return;
            }
        }
        List<Long> items = mergeReqVo.getItems();
        Long finalPurchaseId = purchaseId;
        List<PurchaseDetailEntity> purchaseDetailEntityList = items.stream().map(item -> {
            PurchaseDetailEntity purchaseDetailEntity = new PurchaseDetailEntity();
            purchaseDetailEntity.setId(item);
            purchaseDetailEntity.setPurchaseId(finalPurchaseId);
            purchaseDetailEntity.setStatus(WareConstant.PurchaseDetailStatusEnum.ASSIGNED.getCode());
            return purchaseDetailEntity;
        }).collect(Collectors.toList());
        purchaseDetailService.saveBatch(purchaseDetailEntityList);
    }

    /**
     * 领取采购单
     *
     * @param ids
     */
    @Override
    public void receivePurchase(List<Long> ids) {
        // 查询未分配或新建的采购单
        List<PurchaseEntity> purchaseEntityList = ids.stream().map(id -> {
            PurchaseEntity purchaseEntity = baseMapper.selectById(id);
            return purchaseEntity;
        }).filter(item -> {
            return (item.getStatus() == WareConstant.PurchaseStatusEnum.CREATE.getCode() || item.getStatus() == WareConstant.PurchaseStatusEnum.ASSIGNED.getCode());
        }).map(item -> {
            item.setStatus(WareConstant.PurchaseStatusEnum.RECEIVE.getCode());
            return item;
        }).collect(Collectors.toList());
        // 修改
        this.updateBatchById(purchaseEntityList);
        // 改变采购项状态
        purchaseEntityList.forEach(item -> {
            List<PurchaseDetailEntity> purchaseDetailEntities = purchaseDetailService.getBypurchaseId(item.getId());
            List<PurchaseDetailEntity> detailEntityList = purchaseDetailEntities.stream().map(entity -> {
                PurchaseDetailEntity purchaseDetailEntity = new PurchaseDetailEntity();
                purchaseDetailEntity.setId(entity.getId());
                purchaseDetailEntity.setStatus(WareConstant.PurchaseDetailStatusEnum.BUYING.getCode());
                return purchaseDetailEntity;
            }).collect(Collectors.toList());
            purchaseDetailService.updateBatchById(detailEntityList);
        });
    }

    /**
     * 完成采购
     *
     * @param doneReqVo
     */
    @Override
    public void done(DoneReqVo doneReqVo) {
        // 1、改变采购项的状态
        List<item> items = doneReqVo.getItems();
        Boolean flag = true;
        List<PurchaseDetailEntity> updates = new ArrayList<>(10);
        for (item item : items) {
            PurchaseDetailEntity purchaseDetailEntity = new PurchaseDetailEntity();
            if (item.getStatus() == WareConstant.PurchaseDetailStatusEnum.HASERROR.getCode()) {
                // 采购失败
                log.error("采购单已被采购，采购失败");
                flag = false;
                purchaseDetailEntity.setStatus(WareConstant.PurchaseDetailStatusEnum.HASERROR.getCode());
            } else {
                purchaseDetailEntity.setStatus(WareConstant.PurchaseDetailStatusEnum.FINISH.getCode());
                // 3、将成功采购的入库
                PurchaseDetailEntity detailEntity = purchaseDetailService.getById(item.getItemId());
                wareSkuService.addStock(detailEntity.getSkuId(), detailEntity.getWareId(), detailEntity.getSkuNum());
            }
            purchaseDetailEntity.setId(item.getItemId());
            updates.add(purchaseDetailEntity);
        }
        purchaseDetailService.updateBatchById(updates);
        // 2、改变采购状态
        PurchaseEntity purchaseEntity = baseMapper.selectById(doneReqVo.getId());
        purchaseEntity.setStatus(flag ? WareConstant.PurchaseStatusEnum.FINISH.getCode() : WareConstant.PurchaseStatusEnum.HASERROR.getCode());
        purchaseEntity.setId(doneReqVo.getId());
        baseMapper.updateById(purchaseEntity);
    }

}