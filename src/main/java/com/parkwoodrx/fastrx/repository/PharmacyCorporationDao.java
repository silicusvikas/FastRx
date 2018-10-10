package com.parkwoodrx.fastrx.repository;

import java.sql.SQLException;
import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;

import com.parkwoodrx.fastrx.exception.FastRxException;
import com.parkwoodrx.fastrx.model.BillingDetails;
import com.parkwoodrx.fastrx.model.CreditcardDetails;
import com.parkwoodrx.fastrx.model.InvoiceDetails;
import com.parkwoodrx.fastrx.model.MultiSelectDropDownObject;
import com.parkwoodrx.fastrx.model.PaymentType;
import com.parkwoodrx.fastrx.model.PharmacyCorporation;
import com.parkwoodrx.fastrx.model.PharmacyCorporationDetails;
import com.parkwoodrx.fastrx.model.State;

public interface PharmacyCorporationDao  {

	public CreditcardDetails createPaymentProfile(CreditcardDetails crediatCardDetail);
	public long registerPharmacyCorporation(PharmacyCorporationDetails pharmacyDetails);
	public List<PharmacyCorporation> getPharmacyCorporationsByNameOrPhone(String corpName,String phoneNo);
	public List<PharmacyCorporation> getAllPharmacyCorporations();
	public PharmacyCorporationDetails getCorporationProfileById(Long id);
	public List<PaymentType> getPaymentTypeList();
	public List<State> getStateList();
	public void updatePharmacyCorporation(PharmacyCorporation pharmacyCorporation) throws DataAccessException;
	public void updatePharmacyCorporationBilling(BillingDetails billingDetails) throws DataAccessException;
	public int getBillingDetailsByCorporationId(long id) throws DataAccessException;
	public BillingDetails getBillingDetailByCorporationId(long id) throws DataAccessException;
	public void addBillingdetails(BillingDetails billingDetails) throws DataAccessException;
	public int getPaymentProfileByCorporationId(long corporationId) throws EmptyResultDataAccessException;
	public void updatePaymentType(String paymentType,long corporationId,String updatedBy) throws DataAccessException;
	public CreditcardDetails getPaymentProfileIdByCorporationId(long corporationId) throws EmptyResultDataAccessException, SQLException;
	public void updatePaymentProfileDetails(CreditcardDetails creditcard) throws FastRxException,DataAccessException;
	public int getCorporationCount(String corporationName) throws EmptyResultDataAccessException;
	public void updateStatusOfCorporation(long corporationId,String updatedBy, String status);
	public PharmacyCorporation getPharmacyCorporationById(long corporationId);
	public List<MultiSelectDropDownObject> getAllPharmacyCorpsForMultiSelect();
	public void addInvoiceDetails(InvoiceDetails invoiceDetails)throws DataAccessException ;
	public String getTransactionType(long id ) throws SQLException;}