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
package com.c123.billbuddy.model;

import com.gigaspaces.annotation.pojo.SpaceClass;
import com.gigaspaces.annotation.pojo.SpaceId;
import com.gigaspaces.annotation.pojo.SpaceIndex;
import com.gigaspaces.annotation.pojo.SpaceRouting;
import com.gigaspaces.metadata.index.SpaceIndexType;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/** 
* Payment class is a POJO which has transaction information between merchant and BillBuddy 
* 
* @author 123Completed
*/
@Entity
@Table
@SuppressWarnings("serial")
@SpaceClass
public class ProcessingFee implements Serializable{
	@Id
	private String processingFeeId;
    private Integer payingAccountId;
    private String dependentPaymentId;
    private String description;
    private Double amount;
    private TransactionStatus status;
    private Date createdDate;
    
    
    public ProcessingFee(String processingFeeId) {
        this.processingFeeId = processingFeeId;
    }

    public ProcessingFee() {
    }

    @SpaceId(autoGenerate = true)
    public String getProcessingFeeId() {
        return processingFeeId;
    }

    public void setProcessingFeeId(String processingFeeId) {
        this.processingFeeId = processingFeeId;
    }

    @SpaceRouting
	@SpaceIndex(type=SpaceIndexType.BASIC)
	public Integer getPayingAccountId() {
		return payingAccountId;
	}

	public void setPayingAccountId(Integer payingAccountId) {
		this.payingAccountId = payingAccountId;
	}
	
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public TransactionStatus getStatus() {
		return status;
	}

	public void setStatus(TransactionStatus status) {
		this.status = status;
	}
	
	@SpaceIndex(type=SpaceIndexType.BASIC)
	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	
	public void setDependentPaymentId(String dependentPaymentId) {
		this.dependentPaymentId = dependentPaymentId;
	}

	@SpaceIndex(type=SpaceIndexType.BASIC)
	public String getDependentPaymentId() {
		return dependentPaymentId;
	}
	
}
