package com.mall.product1.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Map;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mall.product1.entity.CategoryEntity;
import com.mall.product1.service.CategoryService;
import com.mall.common.utils.PageUtils;
import com.mall.common.utils.R;



/**
 * 商品三级分类
 *
 * @author jiahe
 * @email lijiahe888888@gmail.com
 * @date 2022-09-04 21:42:16
 */
@RestController
//@RequestMapping("category")
@RequestMapping("product1/category")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    /**
     * 查询出所有分类及其子分类，再以树形结构进行封装
     */
    @RequestMapping("/list/tree")
    //@RequiresPermissions("product1:category:list")
    public List<CategoryEntity> list(){
        List<CategoryEntity> entities =  categoryService.listWithTree();

        return entities;
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{catId}")
    //@RequiresPermissions("product1:category:info")
    public R info(@PathVariable("catId") Long catId){
		CategoryEntity category = categoryService.getById(catId);

        return R.ok().put("category", category);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    //@RequiresPermissions("product1:category:save")
    public R save(@RequestBody CategoryEntity category){
		categoryService.save(category);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    //@RequiresPermissions("product1:category:update")
    public R update(@RequestBody CategoryEntity category){
		// categoryService.updateById(category);
        categoryService.updateCascade(category);
        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
   // @RequiresPermissions("product1:category:delete")
    public R delete(@RequestBody Long[] catIds){
        //检查带你钱要删除的分类有没有引用
		//categoryService.removeByIds(Arrays.asList(catIds));
        categoryService.removeMenuByIds(Arrays.asList(catIds));
        return R.ok();
    }

}
