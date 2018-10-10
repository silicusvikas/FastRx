package com.parkwoodrx.fastrx.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.parkwoodrx.fastrx.model.BillingReport;

public class BillingReportRowMapper implements RowMapper<BillingReport>  {

	@Override
	public BillingReport mapRow(ResultSet rs, int rowNum) throws SQLException {
		BillingReport billingReport=new BillingReport();
		billingReport.setTotalRequestingCharge(rs.getDouble("txnCharge"));
		billingReport.setTotalRequestingTax(rs.getDouble("taxCharge"));
		//billingReport.setTotalEfaxRespondingCharge(rs.getDouble("efaxAndTax"));
		return billingReport;
	}
}
