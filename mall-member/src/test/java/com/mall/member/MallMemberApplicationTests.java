package com.mall.member;

import com.mall.common.utils.PageUtils;
import com.mall.common.utils.R;
import com.mall.member.service.MemberLevelService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;

@SpringBootTest
class MallMemberApplicationTests {
    @Autowired
    MemberLevelService memberLevelService;

    @Test
    void contextLoads() {
    }

}
