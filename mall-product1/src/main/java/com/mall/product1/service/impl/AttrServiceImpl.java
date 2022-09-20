package com.mall.product1.service.impl;

import com.fasterxml.jackson.databind.util.BeanUtil;
import com.mall.common.constant.ProductConstant;
import com.mall.product1.dao.AttrAttrgroupRelationDao;
import com.mall.product1.dao.AttrGroupDao;
import com.mall.product1.dao.CategoryDao;
import com.mall.product1.entity.AttrAttrgroupRelationEntity;
import com.mall.product1.entity.AttrGroupEntity;
import com.mall.product1.entity.CategoryEntity;
import com.mall.product1.service.CategoryService;
import com.mall.product1.vo.AttrGroupRelationVo;
import com.mall.product1.vo.AttrRespVo;
import com.mall.product1.vo.AttrVo;
import lombok.extern.slf4j.Slf4j;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mall.common.utils.PageUtils;
import com.mall.common.utils.Query;

import com.mall.product1.dao.AttrDao;
import com.mall.product1.entity.AttrEntity;
import com.mall.product1.service.AttrService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Slf4j
@Service("attrService")
public class AttrServiceImpl extends ServiceImpl<AttrDao, AttrEntity> implements AttrService {

    @Autowired
    AttrAttrgroupRelationDao relationDao;

    @Autowired
    AttrGroupDao attrGroupDao;
    @Autowired
    CategoryDao categoryDao;


    @Autowired
    CategoryService categoryService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<AttrEntity> page = this.page(
                new Query<AttrEntity>().getPage(params),
                new QueryWrapper<AttrEntity>()
        );

        return new PageUtils(page);
    }
    @Transactional
    @Override
    public void saveAttr(AttrVo attr) {
        AttrEntity attrEntity =new AttrEntity();
        // 可以快速提取属性内容
        BeanUtils.copyProperties(attr,attrEntity);
        // 保存基本数据
        this.save(attrEntity);
        //保存关联关系
        if(attr.getAttrType() == ProductConstant.AttrEnum.ATTR_TYPE_BASE.getCode()){
            AttrAttrgroupRelationEntity attrAttrgroupRelationEntity = new AttrAttrgroupRelationEntity();
            attrAttrgroupRelationEntity.setAttrGroupId(attr.getAttrGroupId());
            attrAttrgroupRelationEntity.setAttrId(attrEntity.getAttrId());
            relationDao.insert(attrAttrgroupRelationEntity);
        }


    }

    @Override
    public PageUtils querBaseAttrPage(Map<String, Object> params, Long catelogId, String type) {

        int typenum = 0;

        if (type.equals("base")){
            typenum = 1;
        }else {
            typenum = 0;
        }
        QueryWrapper<AttrEntity> attrEntityQueryWrapper = new QueryWrapper<AttrEntity>().eq("attr_type",typenum);
        String key = (String) params.get("key");
        if (catelogId != 0){
            //类型是基本类型就查1，其他就查0
            attrEntityQueryWrapper.eq("catelog_id",catelogId);
        }
        if(!StringUtils.isEmpty(key)){
            attrEntityQueryWrapper.and((wrapper)->{
                wrapper.eq("attr_id",key).or().like("attr_name",key);
            });
        }

        IPage<AttrEntity> page = this.page(
                new Query<AttrEntity>().getPage(params),
                attrEntityQueryWrapper
        );

        PageUtils pageUtils = new PageUtils(page);
        //

        List<AttrEntity> records = page.getRecords();
        List<AttrRespVo> respVos = records.stream().map((attrEntity) -> {
            AttrRespVo attrRespVo = new AttrRespVo();
            BeanUtils.copyProperties(attrEntity, attrRespVo);
            //除了基本数据号要添加发呢类和分组信息
            if ("base".equalsIgnoreCase(type)){
                AttrAttrgroupRelationEntity attrid = relationDao.selectOne(new QueryWrapper<AttrAttrgroupRelationEntity>().eq("attr_id", attrEntity.getAttrId()));
                if (attrid != null) {
                    AttrGroupEntity attrGroupEntity = attrGroupDao.selectById(attrid.getAttrGroupId());
                    attrRespVo.setGroupName(attrGroupEntity.getAttrGroupName());
                }
            }

            CategoryEntity categoryEntity = categoryDao.selectById(attrEntity.getCatelogId());
            if (categoryEntity != null) {
                attrRespVo.setCatelogName(categoryEntity.getName());

            }
            return attrRespVo;
        }).collect(Collectors.toList());
        log.info("List之前" + typenum);
        pageUtils.setList(respVos);

        return pageUtils;
    }

    @Override
    public AttrRespVo getAttrInfo(Long attrId) {
        AttrEntity attrEntity = baseMapper.selectById(attrId);
        AttrRespVo attrResVo = new AttrRespVo();
        BeanUtils.copyProperties(attrEntity, attrResVo);

        if(attrEntity.getAttrType() == ProductConstant.AttrEnum.ATTR_TYPE_BASE.getCode()){
            //1设置分组信息
            AttrAttrgroupRelationEntity relationEntity = relationDao.selectOne(new QueryWrapper<AttrAttrgroupRelationEntity>()
                    .eq("attr_id", attrEntity.getAttrId()));
            // 将attrGroupId 和 GroupName 返回
            if (relationEntity != null) {
                attrResVo.setAttrGroupId(relationEntity.getAttrGroupId());
                AttrGroupEntity attrGroupEntity = attrGroupDao.selectById(relationEntity.getAttrGroupId());
                if (attrGroupEntity != null) {
                    attrResVo.setGroupName(attrGroupEntity.getAttrGroupName());
                }
            }
        }


        // 将catelogPath和 catlogName 返回
        Long[] categoryPath = categoryService.findCategoryPath(attrEntity.getCatelogId());
        attrResVo.setCatelogPath(categoryPath);
        CategoryEntity categoryEntity = categoryDao.selectById(attrEntity.getCatelogId());
        if (categoryEntity != null) {
            attrResVo.setCatelogName(categoryEntity.getName());
        }
        return attrResVo;
    }

    @Transactional
    @Override
    public void updateAttr(AttrVo attr) {
        // 修改基本属性
        AttrEntity attrEntity = new AttrEntity();
        BeanUtils.copyProperties(attr, attrEntity);
        baseMapper.updateById(attrEntity);
        //只有base数据类型才需要修改
        if(attrEntity.getAttrType() == ProductConstant.AttrEnum.ATTR_TYPE_BASE.getCode()){
            AttrAttrgroupRelationEntity relationEntity = new AttrAttrgroupRelationEntity();
            relationEntity.setAttrGroupId(attr.getAttrGroupId());
            relationEntity.setAttrId(attr.getAttrId());

            Integer count = relationDao.selectCount(new QueryWrapper<AttrAttrgroupRelationEntity>()
                    .eq("attr_id", attr.getAttrId()));
            if (count > 0) {
                //大于0就修改，否则是空值就新增
                relationDao.update(relationEntity, new QueryWrapper<AttrAttrgroupRelationEntity>()
                        .eq("attr_id", attrEntity.getAttrId()));
            } else {
                relationEntity.setAttrId(attr.getAttrId());
                relationDao.insert(relationEntity);
            }
        }
        // 修改分组关联


    }

    @Override
    public List<AttrEntity> getRelationAttr(Long attrgroupId) {
        List<AttrAttrgroupRelationEntity> entities = relationDao.selectList(new QueryWrapper<AttrAttrgroupRelationEntity>().eq("attr_group_id", attrgroupId));
        List<Long> collect = entities.stream().map((attr) -> {
            return attr.getAttrId();
        }).collect(Collectors.toList());
        if (collect ==null || collect.size() == 0){
            return  null;
        }
        Collection<AttrEntity> attrEntities = this.listByIds(collect);
        return (List<AttrEntity>) attrEntities ;
    }


}