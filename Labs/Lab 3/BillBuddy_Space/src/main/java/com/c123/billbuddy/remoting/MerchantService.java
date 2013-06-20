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
package com.c123.billbuddy.remoting;

import javax.annotation.Resource;

import com.c123.billbuddy.model.Merchant;
import com.c123.billbuddy.model.Payment;
import com.c123.billbuddy.model.ProcessingFee;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openspaces.core.GigaSpace;
import org.openspaces.remoting.RemotingService;
import org.springframework.beans.factory.annotation.Qualifier;

/** 
* ServiceFinder class. 
*  
* Implements IServiceFinder interface remoting capabilities on top of the space
* 
* @author 123Completed
*/

@RemotingService
public class MerchantService implements IMerchantService {
	private final Log log = LogFactory.getLog(MerchantService.class);
	@Resource
	@Qualifier(value = "gigaSpace")
	private GigaSpace gigaSpace;
	
	
	public Double getMerchantProfit(Merchant merchant) {
		log.info("Staring getMerchantProfit to merchantId: " + merchant.getMerchantAccountId());
		
		Payment payment = new Payment();
		payment.setReceivingMerchantId(merchant.getMerchantAccountId());
		Payment[] payments = gigaSpace.readMultiple(payment);
		
		Double paymentAmount = 0d;
		for (int i = 0; i < payments.length; i++) {
			paymentAmount+= payments[i].getPaymentAmount();
		}
		
		
		ProcessingFee processingFee = new ProcessingFee();
		processingFee.setPayingAccountId(merchant.getMerchantAccountId());
		ProcessingFee[] processingFees = gigaSpace.readMultiple(processingFee);
		
		Double processingFeeAmount = 0d;
		for (int i = 0; i < processingFees.length; i++) {
			processingFeeAmount+= processingFees[i].getAmount();
		}
		
		log.info("merchantId profit is: " + (paymentAmount - processingFeeAmount));
		
		return paymentAmount - processingFeeAmount;
	}
}
