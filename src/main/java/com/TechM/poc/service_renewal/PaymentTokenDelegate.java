package com.TechM.poc.service_renewal;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class PaymentTokenDelegate  implements JavaDelegate{

	@Override
	public void execute(DelegateExecution execution) throws Exception {
		
		String subscriberId =(String) execution.getVariable("subscriber_id");
		Gson gson = new Gson();
		String url ="https://demo-vnext-om.tmbmarble.com/product-inventory-query-api/v1/inventory-query-api/products/search?filters=id="+subscriberId;
		
		String response =HttpConnection.httpConnection(url, "GET", null, null, null,null,"application/json:charset=UTF-8");
	    System.out.println("JSON String Result " + response.toString());
	       JsonParser parser = new JsonParser();
	       JsonObject object =(JsonObject) parser.parse(response);
	       
	    execution.setVariable("payment_method","credit-card");
		
	}


}
