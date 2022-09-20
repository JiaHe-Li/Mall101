package com.mall.ware2.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mall.common.utils.PageUtils;
import com.mall.ware2.entity.WareSkuEntity;

import java.util.Map;

/**
 * 商品库存
 *
 * @author jiahe
 * @email lijiahe888888@gmail.com
 * @date 2022-09-04 23:32:51
 */
public interface WareSkuService extends IService<WareSkuEntity> {

    PageUtils queryPage(Map<String, Object> params);

    void addStock(Long skuId, Long wareId, Integer skuNum);
}

