package com.TechM.poc.service_renewal;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;

public class DirectDebitFromBalanceDelegate implements JavaDelegate {

	@Override
	public void execute(DelegateExecution arg0) throws Exception {
		System.out.println(" from DirectDebitFromBalanceDelegate method..");
		
	}

}
