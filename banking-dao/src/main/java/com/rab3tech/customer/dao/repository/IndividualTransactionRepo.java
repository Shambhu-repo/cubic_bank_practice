package com.rab3tech.customer.dao.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.rab3tech.dao.entity.IndividualTransaction;

public interface IndividualTransactionRepo extends  JpaRepository<IndividualTransaction, Integer> {

}
