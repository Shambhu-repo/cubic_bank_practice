package com.rab3tech.customer.controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.rab3tech.customer.service.CustomerAccountInfoService;
import com.rab3tech.customer.service.PayeeInfoService;
import com.rab3tech.vo.ApplicationResponseVO;
import com.rab3tech.vo.LoginVO;
import com.rab3tech.vo.PayeeInfoVO;
import com.rab3tech.vo.TransactionVO;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/v3")
public class CustomerController {
	
	@Autowired
	private PayeeInfoService payeeInfoservice;
	
	@Autowired
	CustomerAccountInfoService customerAccountInfoService;
	
	/*
	//api to get payeelist :- localhost:999/v3/customer/PayeeList?customerId=sam@gmail.com
	@GetMapping("/customer/PayeeList")
	public List<PayeeInfoVO> getAllPayees(@RequestParam String customerId){  //using requestParam
		List<PayeeInfoVO> payeeList= payeeInfoservice.findAllPayee(customerId);
		return payeeList;   
	}   */
	
//	localhost:999/v3/customer/findPayee/21
	@GetMapping("/customer/findPayee/{payeeId}")
	public PayeeInfoVO getAllPayees(@PathVariable int payeeId){        //using pathvariable
		PayeeInfoVO payee= payeeInfoservice.findPayeeById(payeeId);
		return payee;
	}
	
	//localhost:999/v3/customer/deletePayee/21

	@GetMapping("/customer/deletePayee/{payeeId}")
	public ApplicationResponseVO deletePayees(@PathVariable int payeeId){        //using pathvariable
		ApplicationResponseVO applicationResponseVO = new ApplicationResponseVO();
		String message = payeeInfoservice.deletePayeeById(payeeId);
		applicationResponseVO.setCode(200);
		applicationResponseVO.setStatus("Success");
		applicationResponseVO.setMessage(message);
		return applicationResponseVO;
	}
	
	
	//localhost:999/v3/customer/payee/update
      @PostMapping("/customer/payee/update")
      
	public ApplicationResponseVO updatePayees(@RequestBody PayeeInfoVO payeeInfoVO){        //RequestBody is used to post maping 
		ApplicationResponseVO response = new ApplicationResponseVO();
		String message = payeeInfoservice.updatePayee(payeeInfoVO);  //it will provide "update successfully" message
		response.setCode(200);
		response.setStatus("Success");
		response.setMessage(message);
		return response;
	}
      
      
     // localhost:999/v3/customer/transferApi/sam@gmail.com
      @GetMapping("/customer/transferApi/{customerId}")
  	public List<PayeeInfoVO> transferApi(@PathVariable String customerId ){  //using requestParam
    	  List<PayeeInfoVO> payeeList= payeeInfoservice.findAllPayee(customerId);
    	  System.out.println("***** checking " + payeeList);
         return payeeList;
  	}
  	
//      @GetMapping("/customer/transferApi1/{customerId}")
//    	public List<PayeeInfoVO> transferApi(@PathVariable String customerId1 ){  //using requestParam
//      	  List<PayeeInfoVO> payeeList= payeeInfoservice.findAllPayee(customerId1);
//      	  System.out.println("***** checking " + payeeList);
//           return payeeList;
//    	}
//    	
  	
      //localhost:999/v3/customer/payee/transfer
      @PostMapping("/customer/payee/trnasfer")
  	public ApplicationResponseVO moneyTransfer(@RequestBody TransactionVO transactionVO){        //RequestBody is used to post maping 
  		ApplicationResponseVO response = new ApplicationResponseVO();
  		String message =customerAccountInfoService.transferAmmount(transactionVO);
  		response.setCode(200);
  		response.setStatus("Success");
  		response.setMessage(message);
  		System.out.println("checking response : " + response);
  		return response;
  	}
      
}
