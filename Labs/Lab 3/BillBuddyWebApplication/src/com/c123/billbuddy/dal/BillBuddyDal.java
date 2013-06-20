/***************************************************************************
****
 * Copyright (c) 2013 GigaSpaces Technologies Ltd. and 123Completed Ltd. All rights reserved
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 
****************************************************************************
***/
package com.c123.billbuddy.dal;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.openspaces.core.GigaSpace;
import org.openspaces.remoting.ExecutorProxy;

import org.springframework.stereotype.Component;

import com.c123.billbuddy.model.Merchant;
import com.c123.billbuddy.model.Payment;
import com.c123.billbuddy.model.ProcessingFee;
import com.c123.billbuddy.model.User;
import com.c123.billbuddy.remoting.IServiceFinder;
import com.c123.billbuddy.report.ProcessServiceAmountTask;
import com.gigaspaces.async.AsyncFuture;
import com.gigaspaces.document.SpaceDocument;
import java.util.Arrays;
import com.j_spaces.core.client.SQLQuery;

/** 
 *	Data Access Layer design pattern.
 *	
 *	Enables access to High level business statistics(total revenue, top users, etc.)
 *
 *	Dal object is injected by Spring framework as a singleton, thus, BillBuddy space 
 *	also injected to Dal instance.
 * 
 * @author 123Completed
 */
@Component
public class BillBuddyDal {

	private static BillBuddyDal instance;

	@Resource
	private GigaSpace gigaSpace;

	@ExecutorProxy(gigaSpace="gigaSpace",
	    	    broadcast=true,
	    	    remoteResultReducerType=Top5MerchantFeeAmountReducer.class)
	private IServiceFinder merchantServiceFinder;
	@ExecutorProxy(gigaSpace="gigaSpace",
    	    broadcast=true,
    	    remoteResultReducerType=Top10ProcessingFeeReducer.class)
	private IServiceFinder processingFeeServiceFinder;
	@ExecutorProxy(gigaSpace="gigaSpace",
    	    broadcast=true,
    	    remoteResultReducerType=Top10PaymentReducer.class)
	private IServiceFinder paymentServiceFinder;
	
	
	public static BillBuddyDal getInstance() {
		return instance;
	}

	public BillBuddyDal() {
	}
	
	@SuppressWarnings("unused")
	@PostConstruct
	private void init()
	{
		instance=this;
	}
	
	public Double getBillBuddyRevenue() throws Exception, ExecutionException {
		AsyncFuture<Double> future = gigaSpace
				.execute(new ProcessServiceAmountTask());
		Double processingFeeAmount = future.get();

		return processingFeeAmount;
	}

	public List<Merchant> getTop5Merchants() throws Exception {
		List<Merchant> top5MerchantList = null;
		top5MerchantList = Arrays.asList(merchantServiceFinder.findTop5MerchantFeeAmount());
		return top5MerchantList;
	}

	public List<Payment> getTop10Payments() {
		List<Payment> top10PaymentList = null;
		top10PaymentList = Arrays.asList(paymentServiceFinder.findTop10Payments());
		return top10PaymentList;
	}

	public List<ProcessingFee> getTop10ProcessingFees() {
		List<ProcessingFee> top10ProcessingFeeList = null;
		top10ProcessingFeeList = Arrays.asList(processingFeeServiceFinder.findTop10ProcessingFees());
		return top10ProcessingFeeList;
	}

	public Payment getPaymentById(String paymentId) {
		SQLQuery<Payment> query = new SQLQuery<Payment>(Payment.class,
				"paymentId = ?");
		query.setParameter(1, paymentId);
		return gigaSpace.read(query);
	}
	public SpaceDocument getContractDocument(Integer merchantID){
		SpaceDocument template  = new SpaceDocument("ContractDocument");
		template.setProperty("merchantId", merchantID);
		System.out.println(template);
		return gigaSpace.read(template);
	}
	public HashMap<String,Integer> getTotalObjectCount()
	{
		HashMap<String,Integer> totals = new HashMap<String, Integer>();
		totals.put("users", gigaSpace.count(new User()));
		totals.put("merchants", gigaSpace.count(new Merchant()));
		totals.put("payments", gigaSpace.count(new Payment()));
		return totals;
	}
}


