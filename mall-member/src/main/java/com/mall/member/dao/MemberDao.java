package com.mall.member.dao;

import com.mall.member.entity.MemberEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 会员
 * 
 * @author jiahe
 * @email lijiahe888888@gmail.com
 * @date 2022-09-04 22:55:49
 */
@Mapper
public interface MemberDao extends BaseMapper<MemberEntity> {
	
}
