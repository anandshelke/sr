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
       
     LOGGER.info("received Payment Method for subscriber ID: "+subscriberId+" is"+payment_method +" and TOKEN will be: "+token);
        execution.setVariable("payment_method","credit-card");
     } catch (IOException e) {
         e.printStackTrace();
     }
     catch(Exception ex){
    	 ex.printStackTrace();
     }
 }

}
