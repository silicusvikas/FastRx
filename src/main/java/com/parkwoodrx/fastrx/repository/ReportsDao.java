package com.parkwoodrx.fastrx.repository;

import java.util.List;
import java.util.Set;

import com.parkwoodrx.fastrx.model.RespondPrescription;
import com.parkwoodrx.fastrx.model.RevenueReport;
import com.parkwoodrx.fastrx.model.TransactionReport;

public interface ReportsDao {
	
	public List<RespondPrescription> getNoResponseReportForLocationIds(Set<Long> locationList, 
			String fromDate, String toDate);
	
	public List<TransactionReport> getTransactionReportForLocationIds(Set<Long> locationList,
			String fromDate, String toDate, String transactiontype);
	
	public List<RevenueReport> getRevenueReportForLocationIdsForDateRange(Set<Long> corpList,
			String fromDate, String toDate);
	
	public List<RevenueReport> getRevenueReportForLocationIds(Set<Long> corpList);
}
