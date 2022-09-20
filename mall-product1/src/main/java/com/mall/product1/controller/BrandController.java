package com.mall.product1.controller;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;


import com.mall.common.valid.AddGroup;
import com.mall.common.valid.UpdateGroup;
import com.mall.common.valid.UpdateStatusGroup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mall.product1.entity.BrandEntity;
import com.mall.product1.service.BrandService;
import com.mall.common.utils.PageUtils;
import com.mall.common.utils.R;

import javax.validation.Valid;


/**
 * 品牌
 *
 * @author jiahe
 * @email lijiahe888888@gmail.com
 * @date 2022-09-04 21:42:16
 */
@RestController
@RequestMapping("product1/brand")
public class BrandController {
    @Autowired
    private BrandService brandService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    //@RequiresPermissions("product1:brand:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = brandService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{brandId}")
    //@RequiresPermissions("product1:brand:info")
    public R info(@PathVariable("brandId") Long brandId){
		BrandEntity brand = brandService.getById(brandId);

        return R.ok().put("brand", brand);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    //@RequiresPermissions("product1:brand:save")
    public R save(@Validated({AddGroup.class}) @RequestBody BrandEntity brand){
        // 参数也删了一个，不在这里接收异常了
//        if(result.hasErrors()){
//            Map<String,String> map = new HashMap<>();
//            //1.获取校验错误的结果
//            result.getFieldErrors().forEach((item) -> {
//                //FieldError,这个方法没配就获取自带的错误集，配了就用自己的
//                String message = item.getDefaultMessage();
//                //获取错误属性名字
//                String field = item.getField();
//                map.put(field,message);
//            });
//            return R.error(400,"提交的数据不合法").put("data",map);
//        }else{
//
//        }
        brandService.save(brand);
        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    //@RequiresPermissions("product1:brand:update")
    public R update(@Validated({UpdateGroup.class}) @RequestBody BrandEntity brand){
		//brandService.updateById(brand);
        brandService.updateDetail(brand);
        return R.ok();
    }
    //这里只修改状态
    @RequestMapping("/update/status")
    //@RequiresPermissions("product1:brand:update")
    public R updateStatus(@Validated({UpdateStatusGroup.class}) @RequestBody BrandEntity brand){
        brandService.updateById(brand);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
   // @RequiresPermissions("product1:brand:delete")
    public R delete(@RequestBody Long[] brandIds){
		brandService.removeByIds(Arrays.asList(brandIds));

        return R.ok();
    }

}
