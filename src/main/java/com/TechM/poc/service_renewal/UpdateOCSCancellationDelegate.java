package com.TechM.poc.service_renewal;

import java.util.logging.Logger;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;

public class UpdateOCSCancellationDelegate implements JavaDelegate{
	
	private final Logger LOGGER = Logger.getLogger(LoggerDelegate.class.getName());
	
	@Override
	public void execute(DelegateExecution execution) throws Exception {
		// TODO Auto-generated method stub
		
		LOGGER.info("Requesting OCS for service cancellation. UpdateOCSCancellationDelegate started.....");
		
		OkHttpClient client = new OkHttpClient().newBuilder()
		  .build();
		MediaType mediaType = MediaType.parse("text/plain");
		RequestBody body = RequestBody.create(mediaType, "");
		Request request = new Request.Builder()
		  .url("http://10.7.23.86:8080/rsgateway/data/v3/subscriber/0-1-5-416/offers/2")
		  .method("DELETE", body)
		  .build();
		Response ocsResponse = client.newCall(request).execute();
		
//		String url="http://10.7.23.86:8080/rsgateway/data/v3/subscriber/0-1-5-416/offers/2";
//		String ocsResponse =HttpConnection.httpConnection(url, "DELETE", null, null, null, null, "application/xml");
		LOGGER.info("OCS cancellation Response Information: "+ ocsResponse.body().toString());
		LOGGER.info("Cancellation of service in OCS completed. UpdateOCSCancellationDelegate completed.....");
		
	}

}
