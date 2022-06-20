package com.rab3tech.customer.dao.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.rab3tech.dao.entity.CustomerQuestionAnswer;
import com.rab3tech.dao.entity.Login;

/**
 * @author nagendra
 * Spring Data JPA repository
 *
 */
public interface CustomerQuestionsAnsRepository extends JpaRepository<CustomerQuestionAnswer, Integer> {
    
	@Query("SELECT c FROM CustomerQuestionAnswer c where c.login.loginid = :ploginid")
	List<CustomerQuestionAnswer> findQuestionAnswer(@Param("ploginid") String ploginid);
	
	
	
	
	
	
	
    
}

