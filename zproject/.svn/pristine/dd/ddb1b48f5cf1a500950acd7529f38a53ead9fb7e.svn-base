package com.example.demo.dao;

import java.util.*;

import org.apache.ibatis.annotations.*;

@Mapper
public interface MemberBoardDao {
	@Select("select count(*) from member_board where username=#{username} and bno=#{bno} and rownum<=1")
	public Boolean existsById(Map<String, Object> map);
	
	@Insert("insert into member_board values(#{username}, #{bno})")
	public Integer save(Map<String,Object> map);	
}
