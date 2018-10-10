package com.parkwoodrx.fastrx.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import com.parkwoodrx.fastrx.constants.FastRxErrorCodes;
import com.parkwoodrx.fastrx.exception.FastRxException;
import com.parkwoodrx.fastrx.model.MultiSelectDropDownObject;
import com.parkwoodrx.fastrx.model.RespondPrescription;
import com.parkwoodrx.fastrx.model.RevenueReport;
import com.parkwoodrx.fastrx.model.TransactionReport;
import com.parkwoodrx.fastrx.model.TransactionTable;
import com.parkwoodrx.fastrx.repository.ReportsDao;
import com.parkwoodrx.fastrx.security.PasswordGenerator;
@Service
public class ReportsServiceImpl implements ReportsService {
	private static final Logger logger = LoggerFactory.getLogger(ReportsServiceImpl.class);
	
	@Autowired
	ReportsDao reportsDao;	
	@Autowired
	private PasswordGenerator passwordGenerator;
	@Override
	public List<RespondPrescription> getNoResponseReportForLocationIds(List<MultiSelectDropDownObject> locationList,
			String fromDate, String toDate) throws FastRxException {
		logger.info("ReportsServiceImpl :: getNoResponseReportForLocationIds method");
		List<RespondPrescription> prescriptionList;
		Set<Long> locIds= new HashSet<Long>();
		for (MultiSelectDropDownObject multiSelectDropDownObject : locationList) {
			locIds.add(multiSelectDropDownObject.getId());
		}
		try{
			prescriptionList= reportsDao.getNoResponseReportForLocationIds(locIds, fromDate, toDate);
		}catch (EmptyResultDataAccessException e) {
			logger.info("No records found");
			throw new FastRxException(FastRxErrorCodes.NOT_FOUND_CODE, FastRxErrorCodes.PRESCRIPTION_LIST_NOT_FOUND);
		}
		return prescriptionList;
	}

	@Override
	public List<TransactionTable> getTransactionReportForLocationIds(List<MultiSelectDropDownObject> locationList,
			String fromDate, String toDate, String transactiontype) throws FastRxException {
		logger.info("ReportsServiceImpl :: getTransactionReportForLocationIds method");
		List<TransactionReport> transactionList;
		Set<Long> locIds= new HashSet<Long>();
		for (MultiSelectDropDownObject multiSelectDropDownObject : locationList) {
			locIds.add(multiSelectDropDownObject.getId());
		}
		try{
			transactionList= reportsDao.getTransactionReportForLocationIds(locIds, fromDate, toDate, transactiontype);
		}catch (EmptyResultDataAccessException e) {
			logger.info("No records found");
			throw new FastRxException(FastRxErrorCodes.NOT_FOUND_CODE, FastRxErrorCodes.PRESCRIPTION_LIST_NOT_FOUND);
		}
		List<TransactionTable> finalList = new ArrayList<TransactionTable>();
		for (TransactionReport transaction : transactionList) {
			TransactionTable txnTable= new TransactionTable();
		//	if(transaction.getTransactionType().equals("transferIn")){
				txnTable.setCorporationName(transaction.getRequestingCorp().getCorporationName());
				txnTable.setPharmacyName(transaction.getRequestingPharmacy().getPharmacyName());
				txnTable.setCreatedOn(transaction.getTransaction().getFastRxAudit().getCreatedOn());
				txnTable.setTransactionType(transaction.getTransaction().getTransactionType());
				if(transaction.getTransaction().getTransactionType().contains("perMonthTransaction")){
					txnTable.setTaxPerMonth(transaction.getTransaction().getTaxPerMonth());
					txnTable.setTransactionRatePerMonth(transaction.getTransaction().getTransactionRatePerMonth());
				}else{
					txnTable.setTaxPerDay(transaction.getTransaction().getTaxPerDay());
					txnTable.setTransactionRatePerDay(transaction.getTransaction().getTransactionRatePerDay());
				}
				txnTable.setPaymentType(transaction.getRequestingCorp().getPaymentType());
				txnTable.setDrugName(transaction.getPrescription().getPrescDrugName());
				txnTable.setDrugQuantity(transaction.getPrescription().getPrescDrugQty());
				txnTable.setPatientDoB(passwordGenerator.getDecodedString(transaction.getPrescription().getPatientDob()));
				txnTable.setRefillsRemaining(transaction.getPrescription().getRefillsRemaining());
				txnTable.setRequestingCity(transaction.getRequestingPharmacy().getCity());
				txnTable.setRequestingState(transaction.getRequestingPharmacy().getState());
				txnTable.setRequestingZip(transaction.getRequestingPharmacy().getZipcode());
				txnTable.setRespondingCity(transaction.getRespondingPharmacy().getCity());
				txnTable.setRespondingState(transaction.getRespondingPharmacy().getState());
				txnTable.setRespondingZip(transaction.getRespondingPharmacy().getZipcode());
				
//			}else{
//				txnTable.setCorporationName(transaction.getRespondingCorp().getCorporationName());
//				txnTable.setPharmacyName(transaction.getRespondingPharmacy().getPharmacyName());
//				txnTable.setCreatedOn(transaction.getTransaction().getFastRxAudit().getCreatedOn());
//				txnTable.setTransactionType(transaction.getTransactionType());
//				txnTable.setTxnAmount(transaction.getTransaction().getResCharge());
//				txnTable.setEfaxAmount(transaction.getTransaction().getEfaxCharge());
//				txnTable.setTaxAmount(transaction.getTransaction().getResTaxCharge());
//				txnTable.setPaymentType(transaction.getRespondingCorp().getPaymentType());
//				txnTable.setDrugName(transaction.getPrescription().getPrescDrugName());
//				txnTable.setDrugQuantity(transaction.getPrescription().getPrescDrugQty());
//				txnTable.setPatientDoB(passwordGenerator.getDecodedString(transaction.getPrescription().getPatientDob()));
//				txnTable.setRefillsRemaining(transaction.getPrescription().getRefillsRemaining());
//				txnTable.setRequestingCity(transaction.getRequestingPharmacy().getCity());
//				txnTable.setRequestingState(transaction.getRequestingPharmacy().getState());
//				txnTable.setRequestingZip(transaction.getRequestingPharmacy().getZipcode());
//				txnTable.setRespondingCity(transaction.getRespondingPharmacy().getCity());
//				txnTable.setRespondingState(transaction.getRespondingPharmacy().getState());
//				txnTable.setRespondingZip(transaction.getRespondingPharmacy().getZipcode());
//			}
			finalList.add(txnTable);			
		}
		
		return finalList;
	}

	@Override
	public List<RevenueReport> getRevenueReportForLocationIdsForDateRange(List<MultiSelectDropDownObject> corpList, String fromDate,
			String toDate) {
		logger.info("ReportsServiceImpl :: getRevenueReportForLocationIdsForDateRange method");
		List<RevenueReport> revenueReport;
		Set<Long> corpIds= new HashSet<Long>();
		for (MultiSelectDropDownObject multiSelectDropDownObject : corpList) {
			corpIds.add(multiSelectDropDownObject.getId());
		}
		try{
			revenueReport= reportsDao.getRevenueReportForLocationIdsForDateRange(corpIds, fromDate, toDate);
		}catch (EmptyResultDataAccessException e) {
			logger.info("No records found");
			throw new FastRxException(FastRxErrorCodes.NOT_FOUND_CODE, FastRxErrorCodes.PRESCRIPTION_LIST_NOT_FOUND);
		}
		return revenueReport;
	}

	@Override
	public List<RevenueReport> getRevenueReportForLocationIds(List<MultiSelectDropDownObject> corpList) {
		logger.info("ReportsServiceImpl :: getRevenueReportForLocationIds method");
		List<RevenueReport> revenueReport;
		Set<Long> corpIds= new HashSet<Long>();
		for (MultiSelectDropDownObject multiSelectDropDownObject : corpList) {
			corpIds.add(multiSelectDropDownObject.getId());
		}
		try{
			revenueReport= reportsDao.getRevenueReportForLocationIds(corpIds);
		}catch (EmptyResultDataAccessException e) {
			logger.info("No records found");
			throw new FastRxException(FastRxErrorCodes.NOT_FOUND_CODE, FastRxErrorCodes.PRESCRIPTION_LIST_NOT_FOUND);
		}
		return revenueReport;
	}

}
