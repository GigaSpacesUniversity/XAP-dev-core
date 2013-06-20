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

import java.util.LinkedList;
import java.util.Random;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openspaces.core.GigaSpace;
import org.springframework.stereotype.Component;

import com.c123.billbuddy.model.*;

/** 
 * User Feeder class reads userNameList which has been injected into application 
 * by Spring framework. 
 *  
 * This class been injected by Spring framework and it 
 * performs loop on userNameList creates User class and writes it into space.
 * 
 * @author 123Completed
 */

/*Configuring Spring component for 'component-scan'*/
@Component
public class UserFeeder {
	private final Log log = LogFactory.getLog(UserFeeder.class);
    /* @Resource the space object from the pu.xml configuration file */
    @Resource
	private GigaSpace gigaSpace;
    /* @Resource the User list object from the pu.xml configuration file */
    @Resource(name="userNameList")
    /* Specifying the list object to be used in this class
     * (there are two list objects in the pu.xml configuration file */
    private LinkedList<String> userList;
    /* Define the method to execute after Spring injected the relevant bean(s)*/
    @PostConstruct
    public void init() throws Exception {
        log.info("Starting User Feeder");
        
        Integer userAccountId = 1;
        
        for (String u : userList) {
 
			User foundUser = gigaSpace.readById(User.class,userAccountId);
            
            
            if (foundUser == null) {
            	User user = new User();
            	user.setName(u);
            	
            	Double balance = (Double.valueOf(Math.random()*10000));
                user.setBalance(Math.round(balance*100.0)/100.0);
 
                Double creditLimit = Math.random()*10000;
                creditLimit = creditLimit - (creditLimit%1000);
                creditLimit = Math.round(creditLimit*100.0)/100.0;
                
                user.setCreditLimit(Double.valueOf(-(creditLimit)));
                user.setStatus(AccountStatus.ACTIVE);
                user.setUserAccountId(userAccountId);

                Address tempAddress = new Address();
            	tempAddress.setCountry(CountryNames.values()[new Random().nextInt(CountryNames.values().length)]);
            	tempAddress.setCity("123Completed.com");
            	tempAddress.setState("GIGASPACES");
            	tempAddress.setStreet("Here and There");
            	tempAddress.setZipCode(new Random().nextInt());
            	
            	user.setAddress(tempAddress);
                  
            	// Writing the new user to the space
                gigaSpace.write(user);
                log.info(String.format("Added User object with name '%s'", user.getName()));
            }
            userAccountId++;
        }

        log.info("Stopping User Feeder");
    }
}
