package com.mall.ware2.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mall.common.utils.PageUtils;
import com.mall.ware2.entity.PurchaseEntity;
import com.mall.ware2.vo.DoneReqVo;
import com.mall.ware2.vo.MergeReqVo;

import java.util.List;
import java.util.Map;

/**
 * 采购信息
 *
 * @author jiahe
 * @email lijiahe888888@gmail.com
 * @date 2022-09-04 23:32:56
 */
public interface PurchaseService extends IService<PurchaseEntity> {

    PageUtils queryPage(Map<String, Object> params);

    PageUtils getUnclaimedPurchase(Map<String, Object> params);

    void mergePurchase(MergeReqVo mergeReqVo);

    void receivePurchase(List<Long> ids);

    void done(DoneReqVo doneReqVo);

}

