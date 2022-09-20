package com.mall.ware2.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mall.common.utils.PageUtils;
import com.mall.ware2.entity.PurchaseDetailEntity;

import java.util.List;
import java.util.Map;

/**
 * 
 *
 * @author jiahe
 * @email lijiahe888888@gmail.com
 * @date 2022-09-04 23:32:55
 */
public interface PurchaseDetailService extends IService<PurchaseDetailEntity> {

    PageUtils queryPage(Map<String, Object> params);
    List<PurchaseDetailEntity> getBypurchaseId(Long id);
}

