package com.mall.product1.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mall.common.utils.PageUtils;
import com.mall.product1.entity.CategoryEntity;

import java.util.List;
import java.util.Map;

/**
 * 商品三级分类
 *
 * @author jiahe
 * @email lijiahe888888@gmail.com
 * @date 2022-09-04 21:28:59
 */
public interface CategoryService extends IService<CategoryEntity> {

    PageUtils queryPage(Map<String, Object> params);

    //返回所有分类信息
    List<CategoryEntity> listWithTree();

    void removeMenuByIds(List<Long> asList);
    //找到catelogId的完整路径[父/子/孙]
    Long[] findCatelogPath(Long catelogId);

    void updateCascade(CategoryEntity category);

    Long[] findCategoryPath(Long catelogId);

}

