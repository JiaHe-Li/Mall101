package com.mall.order.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mall.common.utils.PageUtils;
import com.mall.order.entity.MqMessageEntity;

import java.util.Map;

/**
 * 
 *
 * @author jiahe
 * @email lijiahe888888@gmail.com
 * @date 2022-09-04 23:07:24
 */
public interface MqMessageService extends IService<MqMessageEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

