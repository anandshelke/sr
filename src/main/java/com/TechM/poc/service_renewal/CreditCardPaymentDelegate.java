package com.TechM.poc.service_renewal;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

//import camundajar.com.google.gson.JsonObject;

import com.TechM.poc.service_renewal.AesEncryption;
import com.TechM.poc.service_renewal.EcnryptData;

public class CreditCardPaymentDelegate implements JavaDelegate {

	@Override
	public void execute(DelegateExecution execution) throws Exception {
		
		//String jsonReq = " {\"TRANSACTING_HID\": \"automation\",\"TRANSACTING_MID\": \"74000274\",\"TRANSACTING_TID\": \"88000047\",\"hierarchyCriteria\": \"B2\",\"amount\": \"12.0\",\"taxAmount\": \"1.0\"}";
		
		String jsonReq = "{ \"transactionAmount\": \"1\", \"taxIncluded\": \"1\",  \"taxAmount\": \"1\",  \"currencyCode\": \"INR\",  \"recurringFlag\": \"Y\",  \"recurrentToken\": \"RE20072213322600005\",   \"paymentChannelId\": \"123\",  \"hierLabelIdReference\": \"JmeterTest\",  \"hierarchyCriteria\": \"B2\",  \"mobile\": \"8756710035\",  \"email\": \"abc@def.com\",  \"mobilePrefix\": \"+91\" }" ;
		
		JsonParser parser = new JsonParser(); 
		JsonObject json = (JsonObject) parser.parse(jsonReq);
			
		String encrypt =  EcnryptData.aesEncryption(json.toString());
		System.out.println("Encrypted data is: " + encrypt);
			
		String decrypt = EcnryptData.aesDecryption(encrypt);
		System.out.println("Decrypted data is :" + decrypt);
		
		
		URL obj = new URL("http://125.16.139.20:8281/ecom/sale");
		HttpURLConnection postConnection = (HttpURLConnection) obj.openConnection();
		postConnection.setRequestMethod("POST");
		postConnection.setRequestProperty("Content-Type", "application/json");
		postConnection.setRequestProperty("Authorization","Bearer "+ "b66e581f-9d04-3701-a65e-d3960ac7a4e1");
		
		
		postConnection.setDoOutput(true);
		
		
		OutputStream os = postConnection.getOutputStream();
		os.write(encrypt.getBytes());
		os.flush();
		os.close();
	
		int responseCode = postConnection.getResponseCode();
	    System.out.println("Comviva POST Response Code :  " + responseCode);
	    System.out.println("Comviva POST Response Message : " + postConnection.getResponseMessage());
	    
	    if (responseCode == HttpURLConnection.HTTP_CREATED) { //success
	        BufferedReader in = new BufferedReader(new InputStreamReader(
	            postConnection.getInputStream()));
	        String inputLine;
	        StringBuffer response = new StringBuffer();
	        while ((inputLine = in .readLine()) != null) {
	            response.append(inputLine);
	        } in .close();
	        // print result
	        System.out.println(response.toString());
	    } else {
	        System.out.println("POST NOT WORKED");
	    }
	
	}

}
