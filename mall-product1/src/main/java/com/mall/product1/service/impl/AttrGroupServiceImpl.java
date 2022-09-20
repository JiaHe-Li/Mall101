package com.mall.product1.service.impl;

import com.mall.product1.dao.AttrAttrgroupRelationDao;
import com.mall.product1.dao.AttrDao;
import com.mall.product1.dao.CategoryDao;
import com.mall.product1.entity.AttrAttrgroupRelationEntity;
import com.mall.product1.entity.AttrEntity;
import com.mall.product1.entity.CategoryEntity;
import com.mall.product1.service.AttrService;
import com.mall.product1.vo.AttrGroupRelationVo;
import com.mall.product1.vo.AttrGroupWithAttrsVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mall.common.utils.PageUtils;
import com.mall.common.utils.Query;

import com.mall.product1.dao.AttrGroupDao;
import com.mall.product1.entity.AttrGroupEntity;
import com.mall.product1.service.AttrGroupService;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;


@Slf4j
@Service("attrGroupService")
public class AttrGroupServiceImpl extends ServiceImpl<AttrGroupDao, AttrGroupEntity> implements AttrGroupService {

    @Autowired
    private AttrAttrgroupRelationDao relationDao;

    @Autowired
    private CategoryDao categoryDao;

    @Resource
    private AttrService attrService;

    @Resource
    private AttrDao attrDao;
    @Override
    public void deleteRelation(AttrGroupRelationVo[] vos) {
        //relationDao.delete(new QueryWrapper<>().eq("attr_id",1L).eq("attr_group_id",1L));
        //希望能只发一次请求完成批量删除
        log.info("service执行前");
        List<AttrAttrgroupRelationEntity> collect = Arrays.stream(vos).map(item -> {
            AttrAttrgroupRelationEntity attrAttrgroupRelationEntity = new AttrAttrgroupRelationEntity();
            BeanUtils.copyProperties(item, attrAttrgroupRelationEntity);
            return attrAttrgroupRelationEntity;
        }).collect(Collectors.toList());
        log.info("service执行后");
        relationDao.deleteBatchRelation(collect);
        log.info("mapper执行后");
    }

    @Override
    public PageUtils getNoRelation(Long attrgroupId, Map<String, Object> params) {
        // 当前分组只能关联所属分类里的所有属性
        AttrGroupEntity attrGroupEntity = baseMapper.selectById(attrgroupId);
        CategoryEntity categoryEntity = null;
        PageUtils pageUtils = null;
        if (attrGroupEntity != null) {
            categoryEntity = categoryDao.selectById(attrGroupEntity.getCatelogId());
            // 没有被其他引用(查询当前分类下的其他分组)
            List<AttrGroupEntity> groupEntityList = baseMapper.selectList(new QueryWrapper<AttrGroupEntity>()
                    .eq("catelog_id", attrGroupEntity.getCatelogId())
                    .ne("attr_group_id", attrgroupId));
            List<Long> groupIdList = groupEntityList.stream().map((item) -> {
                return item.getAttrGroupId();
            }).collect(Collectors.toList());
            QueryWrapper<AttrAttrgroupRelationEntity> wrapper1 = new QueryWrapper<>();
            if (groupIdList != null && groupEntityList.size() > 0) {
                wrapper1.in("attr_group_id", groupIdList);
            }
            // 查询这些分组关联的属性
            List<AttrAttrgroupRelationEntity> relationEntityList = relationDao.selectList(wrapper1);
            List<Long> attrIdList = relationEntityList.stream().map((item) -> {
                return item.getAttrId();
            }).collect(Collectors.toList());
            // 获取未被关联的属性的条件
            QueryWrapper<AttrEntity> wrapper2 = new QueryWrapper<AttrEntity>()
                    .eq("catelog_id", attrGroupEntity.getCatelogId());
            if (attrIdList != null && attrIdList.size() > 0) {
                wrapper2.notIn("attr_id", attrIdList);
            }
            String key = (String) params.get("key");
            if (!StringUtils.isEmpty(key)) {
                wrapper2.and((w) -> {
                    w.eq("attr_id", key).or().like("attr_name", key);
                });
            }
            IPage<AttrEntity> page = attrService.page(new Query<AttrEntity>().getPage(params), wrapper2);
            pageUtils = new PageUtils(page);
        }
        return pageUtils;
    }

    @Override
    public List<AttrGroupWithAttrsVo> getGroupAndAttr(Long catelogId) {
        List<AttrGroupEntity> groupEntityList = baseMapper.selectList(new QueryWrapper<AttrGroupEntity>().eq("catelog_id", catelogId));
        List<AttrGroupWithAttrsVo> attrVoList = new ArrayList<>(100);
        for (int i = 0; i < groupEntityList.size(); i++) {
            AttrGroupEntity attrGroupEntity = groupEntityList.get(i);
            List<AttrAttrgroupRelationEntity> attrAttrgroupRelationEntityList = relationDao.selectList(new QueryWrapper<AttrAttrgroupRelationEntity>()
                    .eq("attr_group_id", attrGroupEntity.getAttrGroupId()));
            List<Long> attrIds = attrAttrgroupRelationEntityList.stream().map(item -> {
                return item.getAttrId();
            }).collect(Collectors.toList());
            List<AttrEntity> attrEntities = null;
            if (attrIds != null && attrIds.size() > 0) {
                attrEntities = attrDao.selectBatchIds(attrIds);
            }
            AttrGroupWithAttrsVo attrGroupAndAttrVo = new AttrGroupWithAttrsVo();
            BeanUtils.copyProperties(attrGroupEntity, attrGroupAndAttrVo);
            attrGroupAndAttrVo.setAttrs(attrEntities);
            attrVoList.add(attrGroupAndAttrVo);
        }
        return attrVoList;
    }

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<AttrGroupEntity> page = this.page(
                new Query<AttrGroupEntity>().getPage(params),
                new QueryWrapper<AttrGroupEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public PageUtils queryPage(Map<String, Object> params, Long catelogId) {
        String key = (String) params.get("key");
        QueryWrapper<AttrGroupEntity> wrapper = new QueryWrapper<AttrGroupEntity>();
        if(!StringUtils.isEmpty(key)){
            wrapper.and((obj) ->{
                obj.eq("attr_group_id",key).or().like("attr_group_name",key);
            });
        }
        if(catelogId == 0){
            IPage<AttrGroupEntity> page = this.page(
                    new Query<AttrGroupEntity>().getPage(params),
                    //new QueryWrapper<AttrGroupEntity>()
                    wrapper
            );

            return new PageUtils(page);
        }else {
//            String key = (String) params.get("key");
//            QueryWrapper<AttrGroupEntity> wrapper = new QueryWrapper<AttrGroupEntity>().eq("catelog_id",catelogId);
//            if(!StringUtils.isEmpty(key)){
//                wrapper.and((obj) ->{
//                    obj.eq("attr_group_id",key).or().like("attr_group_name",key);
//                });
//            }
            wrapper.eq("catelog_id",catelogId);
            IPage<AttrGroupEntity> page = this.page(
                    new Query<AttrGroupEntity>().getPage(params),
                   wrapper
            );
            return new PageUtils(page);
        }
    }

}