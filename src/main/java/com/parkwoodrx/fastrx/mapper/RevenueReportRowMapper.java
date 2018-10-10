package com.parkwoodrx.fastrx.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.parkwoodrx.fastrx.model.RevenueReport;

public class RevenueReportRowMapper  implements RowMapper<RevenueReport>{

	@Override
	public RevenueReport mapRow(ResultSet rs, int rowNum) throws SQLException {
		RevenueReport revenueReport= new RevenueReport();
		revenueReport.setCorpId(rs.getLong("corpId"));
		revenueReport.setCorpName(rs.getString("corpName"));
		revenueReport.setTxnCharge(rs.getDouble("txnCharge"));
		revenueReport.setTaxCharge(rs.getDouble("taxCharge"));
		return revenueReport;
	}

}
