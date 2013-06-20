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
package com.c123.billbuddy.client;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;
import javax.annotation.PostConstruct;
import javax.annotation.Resource;

/** 
 * Payment Feeder class read user and merchant randomly  
 * by Spring framework. 
 *  
 * This class been injected by Spring framework and it 
 * performs loop on userNameList creates User class and writes it into space.
 * 
 * @author 123Completed
 */

@Component
public class PaymentFeederGenerator {
    private final Log log = LogFactory.getLog(PaymentFeederGenerator.class);
    private long defaultDelay = 1000;
    
    @Resource
    private PaymentFeeder paymentFeeder;

    @PostConstruct
    public void construct() throws Exception {
        log.info("Starting PaymentFeeder");
        
        while (true){
        	Thread.sleep(defaultDelay);
        	getPaymentFeeder().createPayment();
        }
        
    }


	private PaymentFeeder getPaymentFeeder() {
		return paymentFeeder;
	}

    
}
