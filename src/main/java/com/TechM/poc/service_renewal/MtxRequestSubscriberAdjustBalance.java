package com.TechM.poc.service_renewal;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class MtxRequestSubscriberAdjustBalance {

	private int AdjustType;
	private Double Amount;
	private String Reason;
	
	@XmlElement
	public int getAdjustType() {
		return AdjustType;
	}
	
	public void setAdjustType(int adjustType) {
		AdjustType = adjustType;
	}
	@XmlElement
	public Double getAmount() {
		return Amount;
	}
	
	public void setAmount(Double amount) {
		Amount = amount;
	}
	
	@XmlElement
	public String getReason() {
		return Reason;
	}
	
	
	public void setReason(String reason) {
		Reason = reason;
	}
	
	
}
