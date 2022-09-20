package com.mall.product1;



import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.mall.product1.entity.BrandEntity;
import com.mall.product1.service.BrandService;
import com.mall.product1.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.io.ByteArrayInputStream;
import java.util.Arrays;
import java.util.List;
@Slf4j
@SpringBootTest
class MallProduct1ApplicationTests {

    @Autowired
    BrandService brandService;
    @Autowired
    CategoryService categoryService;

    @Test
    public void testFindPath(){
        Long[] catelogPath = categoryService.findCatelogPath(225L);
        log.info("完整路径是{}", Arrays.asList(catelogPath));
    }

    @Test
    void contextLoads() {

        List<BrandEntity> brand_id = brandService.list(new QueryWrapper<BrandEntity>().eq("brand_id", 9L));
        brand_id.forEach((item)->{
            System.out.println(item);
        });
    }

}
