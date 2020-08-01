package com.TechM.poc.service_renewal;

import java.io.IOException;
import java.util.logging.Logger;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;


import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class CreditCardPaymentDelegate implements JavaDelegate {

	private final Logger LOGGER = Logger.getLogger(LoggerDelegate.class.getName());
	@Override
	public void execute(DelegateExecution execution) throws Exception {
		
		LOGGER.info("credit card payment from CreditCardPaymentDelegate started..");
			Gson gson = new Gson();
		String jsonReq = "{ \"transactionAmount\": \"1\", \"taxIncluded\": \"1\",  \"taxAmount\": \"1\",  \"currencyCode\": \"INR\",  \"recurringFlag\": \"Y\",  \"recurrentToken\": \"RE20072908044300387\",   \"paymentChannelId\": \"123\",  \"hierLabelIdReference\": \"JmeterTest\",  \"hierarchyCriteria\": \"B2\",  \"mobile\": \"8756710035\",  \"email\": \"abc@def.com\" }" ;
		
		
		JsonParser parser = new JsonParser(); 
		JsonObject json = (JsonObject) parser.parse(jsonReq);
			json.addProperty("mobilePrefix", "+49");
		String encrypt =  EcnryptData.aesEncryption(json.toString());
		LOGGER.info("Encrypted data is: " + encrypt);
			
		String decrypt = EcnryptData.aesDecryption(encrypt);
		LOGGER.info("Decrypted data is :" + decrypt);
		
		OkHttpClient client = new OkHttpClient().newBuilder()
			.build();
		MediaType mediaType = MediaType.parse("application/json");
		RequestBody body = RequestBody.create(mediaType, encrypt);
		Request request = new Request.Builder()
			  .url("http://125.16.139.20:8281/ecom/sale")
			  .method("POST", body)
			  .addHeader("Content-Type", "application/json")
			  .addHeader("Authorization", "Bearer b66e581f-9d04-3701-a65e-d3960ac7a4e1")
			  .build();
		Response response = client.newCall(request).execute();
		System.out.println(response.code());
		String res = EcnryptData.aesDecryption(response.body().string());
		LOGGER.info("The response is " + res);
		
		JsonParser resparser = new JsonParser(); 
		JsonObject resjson = (JsonObject) parser.parse(res);
		System.out.println(resjson.getAsJsonObject());

		
		//String response =HttpConnection.httpConnection("http://125.16.139.20:8281/ecom/sale", "POST", gson.toJson(json), null, null,"b66e581f-9d04-3701-a65e-d3960ac7a4e1","application/json");
		//LOGGER.info("credit card response: "+response);
		//JsonObject responseObject = (JsonObject) parser.parse(response);
		//if(responseObject.get("responseCode")!= null && (responseObject.get("responseCode").equals("0000")))
		execution.setVariable("isPaymentSucess", Boolean.TRUE);
		//else
			//execution.setVariable("isPaymentSucess", Boolean.FALSE);
		
		//Anand - Work around to fake success or failure. Should be removed when Comviva call works
//		String amount = (String) execution.getVariable("amount");
//		Double requestedAmount =Double.parseDouble(amount);
		
//		if(requestedAmount<30)
//			execution.setVariable("isPaymentSucess", Boolean.TRUE);
//		else
//			execution.setVariable("isPaymentSucess", Boolean.FALSE);
		
		LOGGER.info("credit card payment from CreditCardPaymentDelegate completed..");
	}

}
