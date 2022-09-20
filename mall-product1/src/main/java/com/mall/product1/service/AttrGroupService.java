package com.mall.product1.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mall.common.utils.PageUtils;
import com.mall.product1.entity.AttrGroupEntity;
import com.mall.product1.vo.AttrGroupRelationVo;
import com.mall.product1.vo.AttrGroupWithAttrsVo;

import java.util.List;
import java.util.Map;

/**
 * 属性分组
 *
 * @author jiahe
 * @email lijiahe888888@gmail.com
 * @date 2022-09-04 21:29:00
 */
public interface AttrGroupService extends IService<AttrGroupEntity> {

    PageUtils queryPage(Map<String, Object> params);
    PageUtils queryPage(Map<String, Object> params, Long catelogId);
    void deleteRelation(AttrGroupRelationVo[] vos);


    PageUtils getNoRelation(Long attrgroupId, Map<String, Object> params);

    List<AttrGroupWithAttrsVo> getGroupAndAttr(Long catelogId);

}

