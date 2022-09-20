package com.mall.product1.controller;

import java.util.Arrays;
import java.util.Map;


import com.mall.product1.vo.spvsavevo.SpuSaveVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mall.product1.entity.SpuInfoEntity;
import com.mall.product1.service.SpuInfoService;
import com.mall.common.utils.PageUtils;
import com.mall.common.utils.R;



/**
 * spu信息
 *
 * @author jiahe
 * @email lijiahe888888@gmail.com
 * @date 2022-09-04 21:42:15
 */
@RestController
@RequestMapping("product1/spuinfo")
public class SpuInfoController {
    @Autowired
    private SpuInfoService spuInfoService;

    /**
     * 列表
     */
//    @RequestMapping("/list")
//    //@RequiresPermissions("product1:spuinfo:list")
//    public R list(@RequestParam Map<String, Object> params){
//        PageUtils page = spuInfoService.queryPage(params);
//
//        return R.ok().put("page", page);
//    }

    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params) {
        PageUtils page = spuInfoService.queryByCondition(params);
        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    //@RequiresPermissions("product1:spuinfo:info")
    public R info(@PathVariable("id") Long id){
		SpuInfoEntity spuInfo = spuInfoService.getById(id);

        return R.ok().put("spuInfo", spuInfo);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    //@RequiresPermissions("product1:spuinfo:save")
    public R save(@RequestBody SpuSaveVo spuSaveVo){
		//spuInfoService.save(spuInfo);
        spuInfoService.saveSpuInfo(spuSaveVo);
        return R.ok();

    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    //@RequiresPermissions("product1:spuinfo:update")
    public R update(@RequestBody SpuInfoEntity spuInfo){
		spuInfoService.updateById(spuInfo);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
   // @RequiresPermissions("product1:spuinfo:delete")
    public R delete(@RequestBody Long[] ids){
		spuInfoService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
