package com.parkwoodrx.fastrx.service;

import java.util.List;

import com.parkwoodrx.fastrx.exception.FastRxException;
import com.parkwoodrx.fastrx.model.MultiSelectDropDownObject;
import com.parkwoodrx.fastrx.model.RespondPrescription;
import com.parkwoodrx.fastrx.model.RevenueReport;
import com.parkwoodrx.fastrx.model.TransactionTable;

public interface ReportsService {
	public List<RespondPrescription> getNoResponseReportForLocationIds(List<MultiSelectDropDownObject> locationList, 
			String fromDate, String toDate) throws FastRxException;
	
	public List<TransactionTable> getTransactionReportForLocationIds(List<MultiSelectDropDownObject> locationList,
			String fromDate, String toDate, String transactiontype) throws FastRxException;
	
	public List<RevenueReport> getRevenueReportForLocationIdsForDateRange(List<MultiSelectDropDownObject> corpList,
			String fromDate, String toDate);
	
	public List<RevenueReport> getRevenueReportForLocationIds(List<MultiSelectDropDownObject> corpList);
}
