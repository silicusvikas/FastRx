package com.parkwoodrx.fastrx.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.parkwoodrx.fastrx.model.BillingDetails;

public class BillingDetailsRowMapper implements RowMapper<BillingDetails> {

	@Override
	public BillingDetails mapRow(ResultSet rs, int rowNum) throws SQLException {
		
			BillingDetails billingDetails = new BillingDetails();
			billingDetails.setTaxPerDay(rs.getDouble("tax_per_day"));
			billingDetails.setTaxPerMonth(rs.getDouble("tax_per_month"));
			billingDetails.setTransactionRatePerDay(rs.getDouble("transaction_rate_per_day"));	
			billingDetails.setTransactionRatePerMonth(rs.getDouble("transaction_rate_per_month"));
			billingDetails.setTransactionType(rs.getString("transaction_type"));
			billingDetails.setTransactionTypeChangeLog(rs.getTimestamp("transaction_type_change_log"));
			billingDetails.setStart_Day(rs.getTimestamp("start_Day"));
			billingDetails.setStart_txnType(rs.getString("start_txnType"));
			
			return billingDetails;
		
	}

}
