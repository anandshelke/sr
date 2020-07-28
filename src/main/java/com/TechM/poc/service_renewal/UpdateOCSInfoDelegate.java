package com.TechM.poc.service_renewal;

import java.util.logging.Logger;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;

public class UpdateOCSInfoDelegate implements JavaDelegate{
	private final Logger LOGGER = Logger.getLogger(LoggerDelegate.class.getName());
	
	@Override
	public void execute(DelegateExecution execution) throws Exception {
    
	LOGGER.info("updating OCS Information from UpdateOCSInfoDelegate started.....");
	
	String amount = (String) execution.getVariable("amount");
	Double requestedAmount =Double.parseDouble(amount);
	MtxRequestSubscriberAdjustBalance adjustBalance = new MtxRequestSubscriberAdjustBalance();
	
		LOGGER.info("updating OCS information for payment success..");
		adjustBalance.setAdjustType(1);
		adjustBalance.setAmount(requestedAmount);
		adjustBalance.setReason("Goodwill");
		
	
	String xmlRequestString=HttpConnection.convertToXml(adjustBalance, MtxRequestSubscriberAdjustBalance.class);
	LOGGER.info("updating OCS info with input xml data: "+xmlRequestString);
	
	String url="http://10.7.23.86:8080/rsgateway/data/v3/subscriber/0-1-5-356/wallet/2/adjustment";
	String ocsResponse =HttpConnection.httpConnection(url, "PUT", xmlRequestString, null, null, null, "application/xml");
	LOGGER.info("OCS Update Response Information: "+ocsResponse);
	LOGGER.info("updating OCS Information from UpdateOCSInfoDelegate completed.....");
	
	}
	
	
}