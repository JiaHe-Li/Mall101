package com.mall.common.to;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class SpuBoundsTo {

    private Long spuId;
    /**
     * 购买积分
     */
    private BigDecimal buyBounds;
    /**
     * 成长积分
     */
    private BigDecimal growBounds;
}
