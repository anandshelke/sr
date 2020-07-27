package com.TechM.poc.service_renewal;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.logging.Logger;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

public class UpdateBMStatusDelegate implements JavaDelegate{
	
	private final Logger LOGGER = Logger.getLogger(LoggerDelegate.class.getName());
	
	@Override
	public void execute(DelegateExecution execution) throws Exception {
		
	LOGGER.info("updating BM status from UpdateBMStatusDelegate started....");
	JsonObject requestObject = new JsonObject();
	String subscriberId =(String) execution.getVariable("subscriber_id");
	Gson gson = new Gson();
	Boolean status = (Boolean) execution.getVariable("isPaymentSucess");
	String url= "https://demo-vnext-om.tmbmarble.com/product-inventory-business-service/v1/product-inventory/product/"+subscriberId;
	if(status==Boolean.TRUE){
		LOGGER.info("updating BM status as Active..");
		String date =ZonedDateTime.now( ZoneOffset.UTC ).plusMonths(1).format( DateTimeFormatter.ISO_INSTANT );
		requestObject.addProperty("status", "ACTIVE");
		requestObject.addProperty("contractEndDate",date);
		
	}
	else{
		LOGGER.info("updating BM status as suspended..");
		requestObject.addProperty("status", "SUSPENDED");
	}
	
	String bmResponse =HttpConnection.httpConnection(url, "PATCH", gson.toJson(requestObject), null, null, null, "application/json");
	LOGGER.info("BM status Update response: "+bmResponse);
	
	LOGGER.info("updating BM status from UpdateBMStatusDelegate completed....");
	}
}
