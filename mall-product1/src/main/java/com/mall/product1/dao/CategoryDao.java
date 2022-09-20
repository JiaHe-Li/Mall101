package com.mall.product1.dao;

import com.mall.product1.entity.CategoryEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 商品三级分类
 * 
 * @author jiahe
 * @email lijiahe888888@gmail.com
 * @date 2022-09-04 21:28:59
 */
@Mapper
public interface CategoryDao extends BaseMapper<CategoryEntity> {
	
}
