package com.mall.product1.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;


import com.mall.product1.entity.AttrEntity;
import com.mall.product1.service.AttrAttrgroupRelationService;
import com.mall.product1.service.AttrService;
import com.mall.product1.service.CategoryService;
import com.mall.product1.vo.AttrGroupRelationVo;
import com.mall.product1.vo.AttrGroupWithAttrsVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.mall.product1.entity.AttrGroupEntity;
import com.mall.product1.service.AttrGroupService;
import com.mall.common.utils.PageUtils;
import com.mall.common.utils.R;



/**
 * 属性分组
 *
 * @author jiahe
 * @email lijiahe888888@gmail.com
 * @date 2022-09-04 21:42:17
 */
@Slf4j
@RestController
@RequestMapping("product1/attrgroup")
public class AttrGroupController {
    @Autowired
    private AttrGroupService attrGroupService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private AttrService attrService;

    @Autowired
    private AttrAttrgroupRelationService attrAttrgroupRelationService;

    //product/attrgroup/{attrgroupId}/attr/relation
    @GetMapping("/{attrgroupId}/attr/relation")
    public R attrRelation(@PathVariable("attrgroupId") Long attrgroupId){
        List<AttrEntity> entities = attrService.getRelationAttr(attrgroupId);
        return  R.ok().put("data",entities);
    }
    /**
     * 列表
     */
    @RequestMapping("/list/{catelogId}")
    //@RequiresPermissions("product1:attrgroup:list")
    public R list(@RequestParam Map<String, Object> params,@PathVariable("catelogId") Long catelogId){
        //PageUtils page = attrGroupService.queryPage(params);
        PageUtils pageUtils = attrGroupService.queryPage(params, catelogId);
        return R.ok().put("page", pageUtils);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{attrGroupId}")
    //@RequiresPermissions("product1:attrgroup:info")
    public R info(@PathVariable("attrGroupId") Long attrGroupId){
		AttrGroupEntity attrGroup = attrGroupService.getById(attrGroupId);
        Long catelogId = attrGroup.getCatelogId();
        Long[] path = categoryService.findCatelogPath(catelogId);
        attrGroup.setCatelogPath(path);
        return R.ok().put("attrGroup", attrGroup);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    //@RequiresPermissions("product1:attrgroup:save")
    public R save(@RequestBody AttrGroupEntity attrGroup){
		attrGroupService.save(attrGroup);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    //@RequiresPermissions("product1:attrgroup:update")
    public R update(@RequestBody AttrGroupEntity attrGroup){
		attrGroupService.updateById(attrGroup);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
   // @RequiresPermissions("product1:attrgroup:delete")
    public R delete(@RequestBody Long[] attrGroupIds){
		attrGroupService.removeByIds(Arrays.asList(attrGroupIds));

        return R.ok();
    }
    //移除属性关联
    @PostMapping("/attr/relation/delete")
    public R deleteRelation(@RequestBody AttrGroupRelationVo[] vos){
        log.info("执行前" + vos);
        attrGroupService.deleteRelation(vos);
        log.info("执行后");
        return R.ok();
    }

    /**
     * 获取属性分组没有关联的其他属性
     */
    @GetMapping("/{attrgroupId}/noattr/relation")
    public R getNoRelation(@PathVariable("attrgroupId") Long attrgroupId,
                           @RequestParam Map<String, Object> params) {
        PageUtils pageUtils = attrGroupService.getNoRelation(attrgroupId, params);
        return R.ok().put("page", pageUtils);
    }

    /**
     * 添加属性与分组关联关系
     */
    @PostMapping("/attr/relation")
    public R saveRelation(@RequestBody List<AttrGroupRelationVo> attrGroupRelationVos) {
        attrAttrgroupRelationService.saveGroupCateRelation(attrGroupRelationVos);
        return R.ok();
    }

    /**
     * 获取分类下所有分组&关联属性
     */
    @GetMapping("/{catelogId}/withattr")
    public R getGroupAndAttr(@PathVariable("catelogId") Long catelogId) {
        List<AttrGroupWithAttrsVo> attrGroupAndAttrVos = attrGroupService.getGroupAndAttr(catelogId);
        return R.ok().put("data", attrGroupAndAttrVos);
    }

}
