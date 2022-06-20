package com.rab3tech.customer.dao.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.rab3tech.dao.entity.CustomerQuestionAnswer;
import com.rab3tech.dao.entity.SecurityQuestions;

/**
 * @author nagendra
 * Spring Data JPA repository
 *
 */
public interface SecurityQuestionsRepository extends JpaRepository<SecurityQuestions, Integer> {
	
	
}

