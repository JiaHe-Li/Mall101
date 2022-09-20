package com.mall.product1.vo.skuItemvo;

import com.mall.product1.vo.spvsavevo.Attr;
import lombok.Data;
import lombok.ToString;

import java.util.List;

@Data
@ToString
public class SpuItemAttrGroupVo {
    private String groupName;
    private List<Attr> attrValues;
}
