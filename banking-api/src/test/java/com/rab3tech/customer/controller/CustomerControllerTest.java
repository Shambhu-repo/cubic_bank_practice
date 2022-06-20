package com.rab3tech.customer.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rab3tech.customer.service.CustomerAccountInfoService;
import com.rab3tech.customer.service.PayeeInfoService;
import com.rab3tech.vo.ApplicationResponseVO;
import com.rab3tech.vo.PayeeInfoVO;
import com.rab3tech.vo.TransactionVO;


public class CustomerControllerTest {
	
	ObjectMapper objectMapper = new ObjectMapper();

	
	private MockMvc mockmvc;
	
	
	@Mock
	private PayeeInfoService payeeInfoservice;
	
	@Mock
	private CustomerAccountInfoService customerAccountInfoService;
	
	
	@InjectMocks
	private CustomerController customerController;
	
		@Before
		public void init() {
			MockitoAnnotations.initMocks(this); //Initializing mocking for each test cases
			mockmvc = MockMvcBuilders.standaloneSetup(customerController)
					// .addFilters(new CorsFilter())
					.build();
		
		}

	


//	@GetMapping("/customer/PayeeList")
//	public List<PayeeInfoVO> getAllPayees(@RequestParam String customerId){  //using requestParam
//		List<PayeeInfoVO> payeeList= payeeInfoservice.findAllPayee(customerId);
//		return payeeList;
//	}
//	
	@Test // fail  // it was working fine before but latter on stoped to work
	public void getAllPayeeInfoListTest() throws Exception {
		List<PayeeInfoVO> payeeinfoVO = new ArrayList<PayeeInfoVO>();
		
		PayeeInfoVO payeeInfovo = new PayeeInfoVO();
		payeeInfovo.setId(1);
		payeeInfovo.setPayeeName("ram");
		payeeInfovo.setCustomerId("samb@gmail");
		payeeInfovo.setPayeeNickName("sa");
		payeeInfovo.setRemarks("paid");
		
		
		PayeeInfoVO payeeInfovo1 = new PayeeInfoVO();
		payeeInfovo1.setId(2);
		payeeInfovo1.setPayeeName("ramesh");
		payeeInfovo1.setCustomerId("samb@gmail");
		payeeInfovo1.setPayeeNickName("rain");
		payeeInfovo1.setRemarks("loss");
		
		payeeinfoVO.add(payeeInfovo);
		payeeinfoVO.add(payeeInfovo1);
		
		
		when(payeeInfoservice.findAllPayee("samb@gmail")).thenReturn(payeeinfoVO);

		mockmvc.perform(MockMvcRequestBuilders.get("/v3/customer/PayeeList?customerId=samb@gmail")
				//mockmvc.perform(MockMvcRequestBuilders.get("/v3/customer/PayeeList/samb@gmail")

		.accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk())		
		//.andExpect(jsonPath("$.id").exists())
		 .andExpect(jsonPath("$[0].id").value("1"))
	        .andExpect(jsonPath("$[0].payeeName").value("ram"))
	        .andExpect(jsonPath("$[0].customerId").value("samb@gmail"))
	        .andExpect(jsonPath("$[0].payeeNickName").value("sa"))
	        .andExpect(jsonPath("$[0].remarks").value("paid"))
			 
	        .andExpect(jsonPath("$[1].id").value("2"))
            .andExpect(jsonPath("$[1].payeeName").value("ramesh"))
	        .andExpect(jsonPath("$[1].customerId").value("samb@gmail"))
	        .andExpect(jsonPath("$[1].payeeNickName").value("rain"))
	        .andExpect(jsonPath("$[1].remarks").value("loss"))
			.andDo(print());

		
    	verify(payeeInfoservice, times(1)).findAllPayee("samb@gmail"); // one time interaction with service layer
		verifyNoMoreInteractions(payeeInfoservice);

		
}
	
	
//	@GetMapping("/customer/findPayee/{payeeId}")
//	public PayeeInfoVO getAllPayees(@PathVariable int payeeId){        //using pathvariable
//		PayeeInfoVO payee= payeeInfoservice.findPayeeById(payeeId);
//		return payee;
//	}
//
	
	@Test
	public void getPayeeinfoByidTest() throws Exception {
		
  PayeeInfoVO payeeInfovo = new PayeeInfoVO();
		payeeInfovo.setId(1);
		payeeInfovo.setPayeeName("ram");
		payeeInfovo.setCustomerId("samb@gamil");
		payeeInfovo.setPayeeNickName("sa");
		payeeInfovo.setRemarks("paid");
		
		
		when(payeeInfoservice.findPayeeById(1)).thenReturn(payeeInfovo); // meaning we connect with service layer throough VO and again VO will
		                                                               // helps to display the result in html . But here we dont use html so 
		                                                               // we just return VO which means displaying result like in html . 
																		//  In fact , MockMvc will act as controller and 
		mockmvc.perform(MockMvcRequestBuilders.get("/v3/customer/findPayee/1")
		.accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk())		
		.andExpect(jsonPath("$.id").exists()).andExpect(jsonPath("$.payeeName").exists())
		.andExpect(jsonPath("$.customerId").exists()).andExpect(jsonPath("$.payeeNickName").exists())
		.andExpect(jsonPath("$.remarks").exists())
		.andExpect(jsonPath("$.id").value("1"))
		.andExpect(jsonPath("$.payeeName").value("ram")).andExpect(jsonPath("$.customerId").value("samb@gamil"))
		.andExpect(jsonPath("$.payeeNickName").value("sa")).andExpect(jsonPath("$.remarks").value("paid"))
		.andDo(print());
		
       verify(payeeInfoservice, times(1)).findPayeeById(1);  // only one time interaction with service layer
		verifyNoMoreInteractions(payeeInfoservice);
}
	
//	@GetMapping("/customer/deletePayee/{payeeId}")
//	public ApplicationResponseVO deletePayees(@PathVariable int payeeId){        //using pathvariable
//		ApplicationResponseVO applicationResponseVO = new ApplicationResponseVO();
//		String message = payeeInfoservice.deletePayeeById(payeeId);
//		applicationResponseVO.setCode(200);
//		applicationResponseVO.setStatus("Success");
//		applicationResponseVO.setMessage(message);
//		return applicationResponseVO;
//	}
	
	
	
	@Test
	public void deletPayeeinfoByidTest() throws Exception {
		//mocking the method which does return anything
		
			//doNothing().when(payeeInfoservice).deletePayeeById(any(Integer.class));
		when(payeeInfoservice.deletePayeeById(any(Integer.class))).thenReturn(any(String.class));

		mockmvc.perform(MockMvcRequestBuilders.delete("/v3/customer/deletePayee/21")
			 	        //What format of data we are expecting from server
			 			.accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
			             //output we are validating here 
					     .andExpect(jsonPath("$.code").exists())
					     .andExpect(jsonPath("$.message").exists())
				        .andExpect(jsonPath("$.code").value("200"))
				        .andExpect(jsonPath("$.message").value("payee deleted successfully"))
			 			.andDo(print());

				verify(payeeInfoservice, times(1)).deletePayeeById(any(Integer.class));
				verifyNoMoreInteractions(payeeInfoservice);
	

}
	
	
	//localhost:999/v3/customer/payee/update
//    @PostMapping("/customer/payee/update")
//	public ApplicationResponseVO updatePayees(@RequestBody PayeeInfoVO payeeInfoVO){        //RequestBody is used to post maping 
//		ApplicationResponseVO response = new ApplicationResponseVO();
//		Strin message = payeeInfoservice.updatePayee(payeeInfoVO);  //it will provide "update successfully" message
//		response.setCode(200);
//		response.setStatus("Success");
//		response.setMessage(message);
//		return response;
	
  
	
	@Test
	public void updatePayeeTest() throws Exception {
		

		ApplicationResponseVO  applicationResponseVO = new ApplicationResponseVO();
		applicationResponseVO.setCode(300);
		applicationResponseVO.setMessage("Payee updated successfully");
		
		
		PayeeInfoVO payeeInfoVO = new PayeeInfoVO();
		payeeInfoVO.setId(3);
		payeeInfoVO.setRemarks("success");
		payeeInfoVO.setPayeeName("Sumedhya");
		payeeInfoVO.setCustomerId("sumedhay@gmail.com");
		
		
	
		
		//when(payeeInfoservice.upadePayee(payeeInfoVO)).thenReturn(applicationResponseVO);
	   when(payeeInfoservice.updatePayee(payeeInfoVO)).thenReturn(any(String.class));
		
        String json = objectMapper.writeValueAsString(payeeInfoVO);
       // String json = objectMapper.writeValueAsString(applicationResponseVO);


		mockmvc.perform(MockMvcRequestBuilders.post("/v3/customer/payee/update")
			     //what format of data we are sending here
				 .contentType(MediaType.APPLICATION_JSON).characterEncoding("utf-8")
	 	         //setting  json data into request body
	 	        .content(json)
	 	        //What format of data we are expecting from server
	 			.accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
	             //output we are validating here 
		// .andExpect(jsonPath("$.code").exists())
	  // .andExpect(jsonPath("$.message").exists())
     //   .andExpect(jsonPath("$.code").value("200"))
      // .andExpect(jsonPath("$.message").value("Payee updated successfully"))
			.andDo(print());
		
		
	 			//.andExpect(jsonPath("$.payeeName").exists())
	 			//.andExpect(jsonPath("$.customerId").exists())
//	 			.andExpect(jsonPath("$.city").exists())     convertObjectToJsonBytes
//	 			.andExpect(jsonPath("$.code").exists())
//	 			.andExpect(jsonPath("$.ifsc").exists())
	 			//.andExpect(jsonPath("$.payeeName").value("Sumedhya"))
	 			//.andExpect(jsonPath("$.customerId").value("sumedhay@gmail.com"))
//	 			.andExpect(jsonPath("$.city").value("HAYA"))
//	 			.andExpect(jsonPath("$.code").value("O93242"))
//	 			.andExpect(jsonPath("$.ifsc").value("ICICI9234"))
	 			//.andDo(print());
			System.out.println("hello");

	}
	

	
	
	 //localhost:999/v3/customer/payee/transfer
//    @PostMapping("/customer/payee/trnasfer")
//	public ApplicationResponseVO moneyTransfer(@RequestBody TransactionVO transactionVO){        //RequestBody is used to post maping 
//		ApplicationResponseVO response = new ApplicationResponseVO();
//		String message =customerAccountInfoService.transferAmmount(transactionVO);
//		response.setCode(200);
//		response.setStatus("Success");
//		response.setMessage(message);
//		System.out.println("checking response : " + response);
//		return response;
//	}
	
	@Test  //fail
	public void transerMoneyTest() throws Exception {
		
		
		ApplicationResponseVO  applicationResponseVO = new ApplicationResponseVO();
		applicationResponseVO.setCode(200);
		applicationResponseVO.setStatus("success");

		TransactionVO transactionVO = new TransactionVO();
		transactionVO.setAmount(100);
		transactionVO.setCreditorAccountNumber("1245");
		
		
		
		
	 when(customerAccountInfoService.transferAmmount(transactionVO)).thenReturn(any(String.class));
		
        String json = objectMapper.writeValueAsString(applicationResponseVO);
       // String json = objectMapper.writeValueAsString(transactionVO);


		mockmvc.perform(MockMvcRequestBuilders.post("/v3/customer/payee/transfer")
			     //what format of data we are sending here
				 .contentType(MediaType.APPLICATION_JSON).characterEncoding("utf-8")
	 	         //setting  json data into request body
	 	        .content(json)
	 	        //What format of data we are expecting from server
	 			.accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
	          	.andDo(print());
		
		
	 		System.out.println("hello");

		
		
		
		
	}

}
