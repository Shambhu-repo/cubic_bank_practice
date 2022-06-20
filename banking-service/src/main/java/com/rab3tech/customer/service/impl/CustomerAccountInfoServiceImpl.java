package com.rab3tech.customer.service.impl;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rab3tech.customer.dao.repository.CustomerAccountInfoRepository;
import com.rab3tech.customer.dao.repository.CustomerRepository;
import com.rab3tech.customer.dao.repository.IndividualTransactionRepo;
import com.rab3tech.customer.dao.repository.PayeeInfoRepository;
import com.rab3tech.customer.dao.repository.TransactionRepository;
import com.rab3tech.customer.service.CustomerAccountInfoService;
import com.rab3tech.customer.service.PayeeInfoService;
import com.rab3tech.dao.entity.Customer;
import com.rab3tech.dao.entity.CustomerAccountInfo;
import com.rab3tech.dao.entity.CustomerQuestionAnswer;
import com.rab3tech.dao.entity.IndividualTransaction;
import com.rab3tech.dao.entity.Login;
import com.rab3tech.dao.entity.PayeeInfo;
import com.rab3tech.dao.entity.Transaction;
import com.rab3tech.vo.CustomerVO;
import com.rab3tech.vo.PayeeInfoVO;
import com.rab3tech.vo.TransactionVO;

@Service
@Transactional
public class CustomerAccountInfoServiceImpl implements CustomerAccountInfoService {

	/* written by shambhu : 4/6/2021 */
	@Autowired
	private CustomerAccountInfoRepository customerAccountInfoRepo;

	@Autowired
	private CustomerRepository customerRepository;

	@Autowired
	PayeeInfoRepository payeeInfoRepo;

	@Autowired
	private TransactionRepository transactionRepository;

	@Autowired
	IndividualTransactionRepo individualTransactionRepo;

	@Override // task 4
	public List<CustomerVO> getCustomersForAccountCreation() { // working with createAccount.html
		/* this method will display all customer who have not opened the Account . */

		List<CustomerVO> customerVOs = new ArrayList<CustomerVO>();

		List<Customer> customers = customerRepository.findAll();

		for (Customer customer : customers) {
			Optional<CustomerAccountInfo> customerAccount = customerAccountInfoRepo
					.findByCustomerId(customer.getEmail());
			if (!customerAccount
					.isPresent()) { /*
					 * this will check customerId (email) in database table:
					 * customer_account_information_tbl whether given customrId is present or not .
					 * If not present means empty and it will copy this customer in customerVO to
					 * display in createAccount.html . EMpty in customer_account_information_tbl
					 * meaans particular customer does not have bank account
					 */
				CustomerVO customerVO = new CustomerVO();
				BeanUtils.copyProperties(customer, customerVO);
				customerVOs.add(customerVO);
			}
		}
		return customerVOs;

	}

	// task 4
	public void createAccount(int customerId) { // it will save the customer in CustomerAccountInfo with required
		// information ..// it is using in
		// employeeController : postmaping controller will let this method use
		// customerId which is displayed by getMapping controller in
		// createAccount.html : so it will only save those customer who does not have
		// account .
		Optional<Customer> customerData = customerRepository.findById(customerId);
		Customer customer = null;
		if (customerData.isPresent()) {
			customer = customerData.get();
		}

		CustomerAccountInfo customerAccountInfo = new CustomerAccountInfo();
		customerAccountInfo.setAccountNumber("1000" + customer.getId());
		customerAccountInfo.setAccountType("Saving");
		customerAccountInfo.setAvBalance(1000);
		customerAccountInfo.setBranch(customer.getAddress());
		customerAccountInfo.setCurrency("USD");
		customerAccountInfo.setCustomerId(customer.getEmail());
		customerAccountInfo.setStatusAsOf(new Date());
		customerAccountInfo.setTavBalance(1000);
		customerAccountInfoRepo.save(customerAccountInfo);
	}

	@Override
	public boolean checkValidAccountForCustomer(String customerId) { // task 5
		boolean check = false;
		Optional<CustomerAccountInfo> customerAccount = customerAccountInfoRepo.findByCustomerId(customerId); // customerId
		// is
		// user's
		// login
		// id
		if (customerAccount.isPresent()) {
			check = true;
		}
		return check;
	}

	@Override // by shambhu 4/13/2021
	public String transferAmmount(TransactionVO transactionVO) { // task 8th
		String message = null;

		// sender is having valid account or not
		Optional<CustomerAccountInfo> customerAccount = customerAccountInfoRepo
				.findByCustomerId(transactionVO.getCustomerId()); // //done
		if (!customerAccount.isPresent()) {
			message = "Customer is not having valid account ";
			return message;
			
		}
		// creditors account is valid or not
		Optional<CustomerAccountInfo> payeeAccount = customerAccountInfoRepo
				.findByAccountNumber(transactionVO.getCreditorAccountNumber()); // //done
		if (!payeeAccount.isPresent()) {
			message = "payee is not having valid account";
			return message;
		}

		// checking senders avilable ammout is sufficient to make transfer or not //done
		CustomerAccountInfo senderAccount = customerAccount.get();
		if (senderAccount.getAvBalance() < transactionVO.getAmount()) {
			message = "Can not transfer as You have insufficient balance";
			return message;
		}

		if (transactionVO.getAmount() < 0) {
			message = "You can not transfer amount less than zero";
			return message;
		}

		// add debtor account in VO as its null
		transactionVO.setDebitorAccountNumber(senderAccount.getAccountNumber());

		// Receiver is present in list or not // checking with combination of payer
		// customer id and payee's account nUmber in payeeInfo table
		Optional<PayeeInfo> payeeDetails = payeeInfoRepo.findBycustomerIdAndPayeeAccountNo(
				transactionVO.getCustomerId(), transactionVO.getCreditorAccountNumber()); // done
		if (!payeeDetails.isPresent()) {
			message = "payee is not found in your payee List ";
			return message;

		}

		// adding and subtracting in both receiver and sender

		// Reduce sender's amoount
		senderAccount.setAvBalance(senderAccount.getAvBalance() - transactionVO.getAmount());
		senderAccount.setTavBalance(senderAccount.getTavBalance() - transactionVO.getAmount());
		senderAccount.setStatusAsOf(new Date());
		customerAccountInfoRepo.save(senderAccount);

		// increse Receivers amount
		CustomerAccountInfo receiverAccount = payeeAccount.get();
		receiverAccount.setAvBalance(receiverAccount.getAvBalance() + transactionVO.getAmount());
		receiverAccount.setTavBalance(receiverAccount.getTavBalance() + transactionVO.getAmount());
		receiverAccount.setStatusAsOf(new Date());
		customerAccountInfoRepo.save(receiverAccount);

		// register the transaction
		Transaction transaction = new Transaction();
		BeanUtils.copyProperties(transactionVO, transaction);
		transaction.setDoe(new Timestamp(new Date().getTime()));
		transactionRepository.save(transaction);

		// register the individual transaction 5/2/2021

//		IndividualTransaction individualTransaction = new IndividualTransaction();
//		List <PayeeInfo> payees =payeeInfoRepo.findByCustomerId(transactionVO.getCustomerId()); 
//		for(PayeeInfo payee:payees) { 
//			if(transactionVO.getCreditorAccountNumber()==payee.getPayeeAccountNo()) {
//				individualTransaction.setDebitAmount(transactionVO.getAmount());
//                }
//			else{
//				individualTransaction.setCreditAmount(transactionVO.getAmount());
//                 }
//			individualTransaction.setCustomerId(payee.getCustomerId());
//			individualTransaction.setRemarks(transactionVO.getRemarks());
//			individualTransaction.setDoe(new Timestamp(new Date().getTime()));
//           }
//		individualTransactionRepo.save(individualTransaction);


		message = "Transfer success ";

		return message;
	}

}
