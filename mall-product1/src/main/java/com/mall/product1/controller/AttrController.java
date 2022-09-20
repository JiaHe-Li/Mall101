package com.mall.product1.controller;

import java.util.Arrays;
import java.util.Map;


import com.mall.product1.vo.AttrGroupRelationVo;
import com.mall.product1.vo.AttrRespVo;
import com.mall.product1.vo.AttrVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.mall.product1.entity.AttrEntity;
import com.mall.product1.service.AttrService;
import com.mall.common.utils.PageUtils;
import com.mall.common.utils.R;



/**
 * 商品属性
 *
 * @author jiahe
 * @email lijiahe888888@gmail.com
 * @date 2022-09-04 21:42:17
 */
@Slf4j
@RestController
@RequestMapping("product1/attr")
public class AttrController {
    @Autowired
    private AttrService attrService;
    // product1/atr/base/list/{catelogId}
    // 商品和销售属性用的是同一个封装，在这里用{attrType}来确定最终用的是哪个
    //1是基本属性，其他是销售属性
    @GetMapping("/{attrType}/list/{catelogId}")
    public R baseAttrList(@RequestParam Map<String, Object> params,@PathVariable("catelogId") Long catelogId,@PathVariable("attrType") String type){
        PageUtils page = attrService.querBaseAttrPage(params,catelogId,type);
        log.info("返回值内容"+page);
        return R.ok().put("page", page);
    }
    /**
     * 列表
     */
    @RequestMapping("/list")
    //@RequiresPermissions("product1:attr:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = attrService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{attrId}")
    //@RequiresPermissions("product1:attr:info")
    public R info(@PathVariable("attrId") Long attrId){
		// AttrEntity attr = attrService.getById(attrId);
        AttrRespVo attrRespVo = attrService.getAttrInfo(attrId);
        return R.ok().put("attr", attrRespVo);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    //@RequiresPermissions("product1:attr:save")
    public R save(@RequestBody AttrVo attr){
		// attrService.save(attr);
        attrService.saveAttr(attr);
        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    //@RequiresPermissions("product1:attr:update")
    public R update(@RequestBody AttrVo attr){
		//attrService.updateById(attr);
        attrService.updateAttr(attr);
        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
   // @RequiresPermissions("product1:attr:delete")
    public R delete(@RequestBody Long[] attrIds){
		attrService.removeByIds(Arrays.asList(attrIds));

        return R.ok();
    }


}
