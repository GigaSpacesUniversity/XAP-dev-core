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

import java.util.Calendar;
import java.util.LinkedList;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openspaces.core.GigaSpace;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.c123.billbuddy.model.AccountStatus;
import com.c123.billbuddy.model.CategoryType;
import com.c123.billbuddy.model.Merchant;
import com.gigaspaces.document.DocumentProperties;
import com.gigaspaces.document.SpaceDocument;
import com.gigaspaces.metadata.SpaceTypeDescriptor;
import com.gigaspaces.metadata.SpaceTypeDescriptorBuilder;

/** 
 * Merchant Feeder class reads merchantList which has been injected into application 
 * by Spring framework. 
 *   
 * This class been injected by Spring framework and it 
 * performs loop on merchantList creates Merchant class and writes it into space.
 * Register contract document into space and for each merchant writes a contract between
 * merchant and BillBuddy 
 * 
 * @author 123Completed
 */

/*Configuring Spring component for 'component-scan'*/
@Component
public class MerchantFeeder {
     
	private final Log log = LogFactory.getLog(MerchantFeeder.class);
    /* Autowiring the space object from the pu.xml configuration file */
	@Resource
	private GigaSpace gigaSpace;
    /* Autowiring the Merchant list object from the pu.xml configuration file */
	@Resource(name="merchantList")
    /* Specifying the list object to be used in this class
     * (there are two list objects in the pu.xml configuration file */
    @Qualifier(value="merchantList")
    private LinkedList<String> merchantList;
    /* Define the method to execute after Spring injected the relevant bean(s)*/
    @PostConstruct
    public void init() throws Exception {
        log.info("Starting Merchant Feeder");
     
        registerContractType();
        
        Integer merchantAccountId = 1;
        
        for (String merchantName : merchantList) {
        	
			Merchant foundMerchant = gigaSpace.readById(Merchant.class,merchantAccountId);

            
            if (foundMerchant == null) {
            	Merchant merchant = new Merchant();
            	
            	merchant.setName(merchantName);
                merchant.setReceipts(0d);
                merchant.setFeeAmount(0d);
                // Select Random Category
                CategoryType[] categoryTypes = CategoryType.values();
                merchant.setCategory(categoryTypes[(int) ((categoryTypes.length - 1) * Math.random())]);
                merchant.setStatus(AccountStatus.ACTIVE);
                merchant.setMerchantAccountId(merchantAccountId);
                // Merchant is not found, let's add it.
                gigaSpace.write(merchant);
                
                log.info(String.format("Added Merchant object with name '%s'", merchant.getName()));
                
                createMerchantContract(merchant.getMerchantAccountId());
            }
            
            merchantAccountId++;
        }
        
        log.info("Stopping Merchant Feeder");
    }
    
    /** 
     * Creates SpaceDocument with the terms between Merchant and BillBuddy 
     */ 
    private void createMerchantContract(Integer merchantId) {
    	
    	Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(System.currentTimeMillis());

		DocumentProperties documentProperties = new DocumentProperties();
	
		// 1. Create the properties:
		documentProperties.setProperty("transactionPrecentFee", 
				Double.valueOf(Math.random()/10)).
		setProperty("contractDate", calendar.getTime()).
		setProperty("merchantId", merchantId);
	
    	// 2. Create the document using the type name and properties: 
        SpaceDocument document = new SpaceDocument("ContractDocument", documentProperties);
        
        // 3. Write the document to the space:
        gigaSpace.write(document);
        
        log.info(String.format("Added MerchantContract object with id '%s'", document.getProperty("id")));
		
	}
    /** 
     * Register ContractDocument SpaceDocument into Space 
     */ 
    private void registerContractType() {
        // Create type descriptor:
        SpaceTypeDescriptor typeDescriptor = 
            new SpaceTypeDescriptorBuilder("ContractDocument")
            // ... Other type settings
            .idProperty("id", true).routingProperty("merchantId").create();
        // Register type:
        gigaSpace.getTypeManager().registerTypeDescriptor(typeDescriptor);
    }
}
