package com.parkwoodrx.fastrx.service;

import java.util.List;

import com.parkwoodrx.fastrx.exception.FastRxException;
import com.parkwoodrx.fastrx.model.BillingDetails;
import com.parkwoodrx.fastrx.model.CreditcardDetails;
import com.parkwoodrx.fastrx.model.MultiSelectDropDownObject;
import com.parkwoodrx.fastrx.model.PaymentType;
import com.parkwoodrx.fastrx.model.Pharmacy;
import com.parkwoodrx.fastrx.model.PharmacyCorporation;
import com.parkwoodrx.fastrx.model.PharmacyCorporationDetails;
import com.parkwoodrx.fastrx.model.State;

public interface PharmacyCorporationService {

	public List<Pharmacy> getPharmacies() throws FastRxException;

	public long registerPharmacyCorporation(PharmacyCorporationDetails pharmacyDetails) throws FastRxException;

	public List<PharmacyCorporation> getPharmacyCorporationsByNameOrPhone(String corpName,String phoneNo) throws FastRxException;

	public List<PharmacyCorporation> getAllPharmacyCorporations() throws FastRxException;

	public PharmacyCorporationDetails getCorporationProfileById(Long id) throws FastRxException;

	public List<PaymentType> getPaymentTypeList();

	public List<State> getStateList();

	public void updatePharmacyCorporation(PharmacyCorporation pharmacyCorporation) throws FastRxException;

	public void updatePharmacyCorporationBilling(BillingDetails billingDetails) throws FastRxException;

	public int getPaymentProfileByCorporationId(long corporationId);

	public void updatePaymentType(String paymentType,long corporationId,String updatedBy);

	public CreditcardDetails getPaymentProfileIdByCorporationId(long corporationId);
	
	public void updatedStatusOfCorporation(long corpId,String updatedBy, String status); 
	
	public void activateCorporation(long corpId,String updatedBy, String status);
	
	public boolean isCorporationActive(long corporationId);
	
	public List<MultiSelectDropDownObject> getAllPharmacyCorpsMultiSelect();
	
	public String getPharmacyCorpNameById(long corporationId);

}
