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
package com.c123.billbuddy.events;





import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openspaces.events.EventDriven;
import org.openspaces.events.EventTemplate;
import org.openspaces.events.TransactionalEvent;
import org.openspaces.events.adapter.SpaceDataEvent;
import org.openspaces.events.polling.Polling;

import com.c123.billbuddy.model.Payment;
import com.c123.billbuddy.model.TransactionStatus;

/** 
 * AuditPaymentEventContainer class. 
 *  
 * Pooling Event starts when new Payment with Transaction status NEW written into space
 * It takes the Payment object and change it's status to AUDIT and write it into space
 * 
 * @author 123Completed
 */

@EventDriven
@Polling(gigaSpace="gigaSpace")
@TransactionalEvent
public class AuditPaymentEventContainer {
    private final Log log = LogFactory.getLog(AuditPaymentEventContainer.class);
   

    @EventTemplate
    public Payment findNewPayments() {
    	log.info("Starting AuditPaymentEventContainer EventTemplate for Payment with NEW status.");
    	
    	Payment paymentTemplate = new Payment();
    	paymentTemplate.setStatus(TransactionStatus.NEW);
   
        return paymentTemplate;
    }

    @SpaceDataEvent
    public Payment processPayment(Payment payment) {
    	
    	log.info("Starting AuditPaymentEventContainer SpaceDataEvent.");
    	log.info("Payment ID: "+payment.getPaymentId() +" Merchant ID: "+ payment.getReceivingMerchantId() +
    			" User ID: "	+ payment.getPayingAccountId() + " Payment Amount: "	+ payment.getPaymentAmount());
    	// Set payment status
    	payment.setStatus(TransactionStatus.AUDITED);
    	
        return payment;
    }
    
   
}
