package com.mall.ware2.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mall.common.utils.PageUtils;
import com.mall.common.utils.Query;

import com.mall.ware2.dao.WareSkuDao;
import com.mall.ware2.entity.WareSkuEntity;
import com.mall.ware2.service.WareSkuService;
import org.springframework.util.StringUtils;


@Service("wareSkuService")
public class WareSkuServiceImpl extends ServiceImpl<WareSkuDao, WareSkuEntity> implements WareSkuService {

    @Autowired
    //private ProductFeignService productFeignService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
//        IPage<WareSkuEntity> page = this.page(
//                new Query<WareSkuEntity>().getPage(params),
//                new QueryWrapper<WareSkuEntity>()
//        );
//
//        return new PageUtils(page);
        QueryWrapper<WareSkuEntity> wrapper = new QueryWrapper<>();
        String skuId = (String) params.get("skuId");
        if (!StringUtils.isEmpty(skuId)) {
            wrapper.eq("sku_id", skuId);
        }
        String wareId = (String) params.get("wareId");
        if (!StringUtils.isEmpty(wareId)) {
            wrapper.eq("ware_id", wareId);
        }
        IPage<WareSkuEntity> page = this.page(
                new Query<WareSkuEntity>().getPage(params),
                wrapper
        );
        return new PageUtils(page);

    }

    @Override
    public void addStock(Long skuId, Long wareId, Integer skuNum) {
        List<WareSkuEntity> wareSkuEntities = baseMapper.selectList(new QueryWrapper<WareSkuEntity>().eq("sku_id", skuId).eq("ware_id", wareId));
        if (wareSkuEntities != null && wareSkuEntities.size() > 0) {
            // 如果不是空的,添加库存
            baseMapper.addStock(skuId, wareId, skuNum);
        } else {
            // 保存新的
            WareSkuEntity wareSkuEntity = new WareSkuEntity();
            wareSkuEntity.setSkuId(skuId);
            wareSkuEntity.setWareId(wareId);
            wareSkuEntity.setStock(skuNum);
            wareSkuEntity.setStockLocked(0);
            // 远程查询sku名字
//            String skuName = productFeignService.getSkuName(skuId);
//            wareSkuEntity.setSkuName(skuName);
//            baseMapper.insert(wareSkuEntity);
        }
    }

}