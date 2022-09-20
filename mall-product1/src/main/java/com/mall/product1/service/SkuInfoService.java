package com.mall.product1.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mall.common.utils.PageUtils;
import com.mall.product1.entity.SkuInfoEntity;

import java.util.Map;

/**
 * sku信息
 *
 * @author jiahe
 * @email lijiahe888888@gmail.com
 * @date 2022-09-04 21:28:55
 */
public interface SkuInfoService extends IService<SkuInfoEntity> {

    PageUtils queryPage(Map<String, Object> params);

    PageUtils queryPageByCondition(Map<String, Object> params);

}

