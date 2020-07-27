package com.TechM.poc.service_renewal;

import java.util.logging.Logger;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class CreditCardPaymentDelegate implements JavaDelegate {

	private final Logger LOGGER = Logger.getLogger(LoggerDelegate.class.getName());
	@Override
	public void execute(DelegateExecution execution) throws Exception {
		
		LOGGER.info("credit card payment from CreditCardPaymentDelegate started..");
			Gson gson = new Gson();
		String jsonReq = "{ \"transactionAmount\": \"1\", \"taxIncluded\": \"1\",  \"taxAmount\": \"1\",  \"currencyCode\": \"INR\",  \"recurringFlag\": \"Y\",  \"recurrentToken\": \"RE20072213322600005\",   \"paymentChannelId\": \"123\",  \"hierLabelIdReference\": \"JmeterTest\",  \"hierarchyCriteria\": \"B2\",  \"mobile\": \"8756710035\",  \"email\": \"abc@def.com\" }" ;
		
		
		JsonParser parser = new JsonParser(); 
		JsonObject json = (JsonObject) parser.parse(jsonReq);
			json.addProperty("mobilePrefix", "+49");
		String encrypt =  EcnryptData.aesEncryption(json.toString());
		LOGGER.info("Encrypted data is: " + encrypt);
			
		String decrypt = EcnryptData.aesDecryption(encrypt);
		LOGGER.info("Decrypted data is :" + decrypt);
		
		String response =HttpConnection.httpConnection("http://125.16.139.20:8281/ecom/sale", "POST", gson.toJson(json), null, null,"b66e581f-9d04-3701-a65e-d3960ac7a4e1","application/json");
		LOGGER.info("credit card response: "+response);
		JsonObject responseObject = (JsonObject) parser.parse(response);
		if(responseObject.get("responseCode")!= null && (responseObject.get("responseCode").equals("0000")))
		execution.setVariable("isPaymentSucess", Boolean.TRUE);
		else
			execution.setVariable("isPaymentSucess", Boolean.FALSE);
		
		LOGGER.info("credit card payment from CreditCardPaymentDelegate completed..");
	}

}
