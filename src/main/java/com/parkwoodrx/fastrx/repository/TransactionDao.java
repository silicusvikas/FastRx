package com.parkwoodrx.fastrx.repository;

import java.util.List;

import com.parkwoodrx.fastrx.model.BillingDetails;
import com.parkwoodrx.fastrx.model.BillingReport;

public interface TransactionDao {
	
public List<BillingReport> getBillingReportByCorpId(long corpId);
	public BillingDetails getBillingdetailsForCorp(long corpId);
	public void updateBillingdetailsForCorp();
	List<BillingReport> getMonthlyBillingReportByCorpId(long corpId);

}
