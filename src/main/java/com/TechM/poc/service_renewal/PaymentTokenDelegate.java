package com.TechM.poc.service_renewal;

import java.io.IOException;
import java.util.logging.Logger;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;



public class PaymentTokenDelegate  implements JavaDelegate{
	JsonParser parser = new JsonParser();
    Gson gson = new Gson();
    private final Logger LOGGER = Logger.getLogger(LoggerDelegate.class.getName());
 @Override
 public void execute(DelegateExecution execution){
	 try {
		 LOGGER.info("payment token delegate started..");
     String subscriberId =(String) execution.getVariable("subscriber_id");
     LOGGER.info("Payment token request for subscriber ID: " + subscriberId);
    
     String url ="https://demo-vnext-om.tmbmarble.com/product-inventory-query-api/v1/inventory-query-api/products/search?filters=id="+subscriberId;
    
     String response = HttpConnection.httpConnection(url, "GET", null, null, null,null,"application/json:charset=UTF-8");

     LOGGER.info("Payment token Result response: " + response.toString());
    
        JsonArray array =(JsonArray) parser.parse(response);
        JsonObject jsonObject =(JsonObject) array.get(0);
        JsonArray paymentArray =(JsonArray)jsonObject.get("payment");
        JsonObject paymentJsonObject =paymentArray.get(0).getAsJsonObject();
  
        String payment_method = paymentJsonObject.get("method").getAsString();
        String token = paymentJsonObject.get("token").getAsString();
        
        String in_payment_method = (String) execution.getVariable("in_payment_method");
        String in_payment_token = (String) execution.getVariable("in_payment_token");
        
        //If BM provides proper value, use it. Else use the invocation time value
        if(payment_method.equals("credit-card") || payment_method.equals("prepaid-balance"))
	   	 {
        	execution.setVariable("payment_method",payment_method);
        	LOGGER.info("Setting value from the payment mode value of " + payment_method);
	   		 
	   	 }
	   	 else
	   	 {
		   	execution.setVariable("payment_method", in_payment_method);	   		 
	   		LOGGER.info("Setting it to call value to " + in_payment_method);
	   	 }
        
        //Setting token to the token received from BM
        execution.setVariable("payment_token", token);
        
        //This should be removed if BM is sending correct token
        execution.setVariable("payment_token",in_payment_token);
       
     LOGGER.info("received Payment Method for subscriber ID: "+subscriberId+" is"+payment_method +" and TOKEN will be: "+token);
     
     //Anand - Using temporary check such that all payments below 30 are CC and above 30 are Balance debit
//     String amount = (String) execution.getVariable("amount");
//	 Double requestedAmount =Double.parseDouble(amount);
//	 if(requestedAmount<30)
//		 execution.setVariable("payment_method","credit-card");
//	 else
//		 execution.setVariable("payment_method","balance-debit");
     
	 LOGGER.info("payment token delegate completed..");
     
     } catch (IOException e) {
         e.printStackTrace();
     }
     catch(Exception ex){
    	 ex.printStackTrace();
     }
 }
 
// public static void main(String[] args) {
//	 String token = "credit-card1";
//	 if(token.equals("credit-card") || token.equals("prepaid-balance"))
//	 {
//		 System.out.println("Setting value from the input value of " + token);
//		 
//	 }
//	 else
//	 {
//		 System.out.println("Setting it to call value to credit card");
//		 token = "credit-card";
//	 }
//			 
//}

}
