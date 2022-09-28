package com.example.demo.service;

import java.util.*;

import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.*;
import org.springframework.transaction.annotation.*;

import com.example.demo.dao.*;
import com.example.demo.dto.*;
import com.example.demo.entity.*;
import com.example.demo.exception.*;

@Service
public class CommentService {
	@Autowired
	private CommentDao commentDao;
	@Autowired
	private BoardDao boardDao;
	
	@Transactional
	public List<CommentDto.Read> write(CommentDto.Write dto, String loginId) {
		commentDao.save(dto.toEntity().addWriter(loginId));
		boardDao.update(Board.builder().bno(dto.getBno()).commentCnt(1).build());
		return commentDao.findByBno(dto.getBno());
	}

	@Transactional
	public List<CommentDto.Read> delete(CommentDto.Delete dto, String loginId) {
		String writer = commentDao.findWriterById(dto.getCno()).orElseThrow(CommentNotFoundException::new);
		if(!writer.equals(loginId))
			throw new JobFailException("삭제할 수 없습니다");
		commentDao.deleteByCno(dto.getCno());
		return commentDao.findByBno(dto.getBno());
	}
}
