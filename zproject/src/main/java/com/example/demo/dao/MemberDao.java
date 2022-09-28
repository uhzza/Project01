package com.example.demo.dao;

import java.util.*;

import org.apache.ibatis.annotations.*;

import com.example.demo.entity.*;

@Mapper
public interface MemberDao {

	public Boolean existsById(String username);

	public Boolean existsByEmail(String email);

	public Integer save(Member entity);

	public Optional<Member> findByEmail(String email);

	public Optional<Member> findById(String username);

	public Integer update(Member member);
	
	public Integer deleteById(String username);

	public Optional<Member> findByCheckcode(String checkcode);

	public List<String> findByCheckcodeIsNotNull();

	public void deleteByUsernames(List<String> usernames);

}
