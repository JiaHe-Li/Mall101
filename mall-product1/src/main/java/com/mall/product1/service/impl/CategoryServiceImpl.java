package com.mall.product1.service.impl;

import com.mall.product1.service.CategoryBrandRelationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mall.common.utils.PageUtils;
import com.mall.common.utils.Query;

import com.mall.product1.dao.CategoryDao;
import com.mall.product1.entity.CategoryEntity;
import com.mall.product1.service.CategoryService;
import org.springframework.transaction.annotation.Transactional;


@Service("categoryService")
public class CategoryServiceImpl extends ServiceImpl<CategoryDao, CategoryEntity> implements CategoryService {
    @Autowired
    CategoryBrandRelationService categoryBrandRelationService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<CategoryEntity> page = this.page(
                new Query<CategoryEntity>().getPage(params),
                new QueryWrapper<CategoryEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public List<CategoryEntity> listWithTree() {

        //1.查出所偶有分类，这里baseMapper是该类实现结构的泛型传入的参数就是上面泛型里的
        List<CategoryEntity> entities = baseMapper.selectList(null);
        //2.组装树形结构


        List<CategoryEntity> level1 = entities.stream().filter(categoryEntity -> categoryEntity.getParentCid()==0
        ).map(menu->{
            menu.setChildren(getChildrens(menu,entities));
            return menu;
        }).collect(Collectors.toList());


        return level1;
    }

    @Override
    public void removeMenuByIds(List<Long> asList) {
        //TODO 1.应该先检查有没有引用想

        //这样直接物理删除 一般都会逻辑删除
        //表中的show——status就可以用来干这个 不显示就用0
        baseMapper.deleteBatchIds(asList);
    }
    //路径结构 [2,25,225]
    @Override
    public Long[] findCatelogPath(Long catelogId) {
        List<Long> paths = new ArrayList<>();
        List<Long> parentPath = findParentPath(catelogId, paths);
        //收集的结果是反的，这里要转过来
        Collections.reverse(parentPath);
        return (Long[]) parentPath.toArray(new Long[parentPath.size()]);
    }
    //级联更新所有关联数据

    @Override
    public void updateCascade(CategoryEntity category) {
        this.updateById(category);

        categoryBrandRelationService.updateCategory(category.getCatId(),category.getName());
    }

    @Override
    public Long[] findCategoryPath(Long catelogId) {
        List<Long> path = new ArrayList<>();
        List<Long> parentPath = findParentPath(catelogId, path);
        Collections.reverse(parentPath);
        return parentPath.toArray(new Long[parentPath.size()]);
    }

    //迭代查找Path的递归方法
    //结果是[225,25,2]
    private  List<Long> findParentPath (Long catelogId,List<Long> paths){
        //手机当前节点Id
        paths.add(catelogId);
        CategoryEntity byId = this.getById(catelogId);
        if(byId.getParentCid()!=0){
            findParentPath(byId.getParentCid(),paths);
        }
        return paths;
    }

    private List<CategoryEntity> getChildrens(CategoryEntity root,List<CategoryEntity> all){
        List<CategoryEntity> children = all.stream().filter(categoryEntity -> categoryEntity.getParentCid() == root.getCatId()).map(categoryEntity -> {
            //这里递归的设置了子菜单
            categoryEntity.setChildren(getChildrens(categoryEntity, all));
            return categoryEntity;
        }).collect(Collectors.toList());
        return children;
    }



}