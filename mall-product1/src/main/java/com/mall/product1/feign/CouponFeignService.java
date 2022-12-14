package com.mall.product1.feign;

import com.mall.common.to.SkuReductionTo;
import com.mall.common.to.SpuBoundsTo;
import com.mall.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(value = "coupon")
public interface CouponFeignService {

        @PostMapping("/coupon/spubounds/saveSpuBoundTo")
        R saveSpuBounds(@RequestBody SpuBoundsTo spuBoundsTo);

        @PostMapping("/coupon/skufullreduction/saveInfo")
        R saveSkuReduction(SkuReductionTo skuReductionTo);
}
