package com.mall.product1.dao;

import com.mall.product1.entity.CommentReplayEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 商品评价回复关系
 * 
 * @author jiahe
 * @email lijiahe888888@gmail.com
 * @date 2022-09-04 21:28:57
 */
@Mapper
public interface CommentReplayDao extends BaseMapper<CommentReplayEntity> {
	
}
