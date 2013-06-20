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

import com.c123.billbuddy.model.Merchant;
import com.c123.billbuddy.model.Payment;
import com.c123.billbuddy.model.ProcessingFee;


/** 
* IServiceFinder Interface. 
*  
* Define method which will be executed by remoting on top of the space
* 
* @author 123Completed
*/

public interface IServiceFinder {
  
	Payment[] findTop10Payments();
    
    Merchant[] findTop5MerchantFeeAmount();
    
    ProcessingFee[] findTop10ProcessingFees();
}
