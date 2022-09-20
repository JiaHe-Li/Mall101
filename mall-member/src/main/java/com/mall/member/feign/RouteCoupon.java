package com.mall.member.feign;

import com.mall.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;

@FeignClient("mall-coupon")
public interface RouteCoupon {

    @RequestMapping("/coupon/coupon/membercouponlist")
    public R membercoupons();
}

