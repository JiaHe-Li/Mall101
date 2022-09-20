package com.mall.order.dao;

import com.mall.order.entity.OrderEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 订单
 * 
 * @author jiahe
 * @email lijiahe888888@gmail.com
 * @date 2022-09-04 23:07:24
 */
@Mapper
public interface OrderDao extends BaseMapper<OrderEntity> {
	
}
