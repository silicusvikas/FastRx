package com.parkwoodrx.fastrx.repository;


import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import com.parkwoodrx.fastrx.constants.FastRxQueryConstants;
import com.parkwoodrx.fastrx.mapper.RespondPrescriptionRowMapper;
import com.parkwoodrx.fastrx.mapper.RevenueReportRowMapper;
import com.parkwoodrx.fastrx.mapper.TransactionReportRowMapper;
import com.parkwoodrx.fastrx.model.RespondPrescription;
import com.parkwoodrx.fastrx.model.RevenueReport;
import com.parkwoodrx.fastrx.model.TransactionReport;

@Repository
public class ReportsDaoImpl implements ReportsDao {
	private static final Logger logger = LoggerFactory.getLogger(ReportsDaoImpl.class);

	@Autowired
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
	
	public NamedParameterJdbcTemplate geNamedParameterJdbcTemplate() {
		return namedParameterJdbcTemplate;
	}
	
	@Override
	public List<RespondPrescription> getNoResponseReportForLocationIds(Set<Long> locationList, String fromDate,
			String toDate) {
		logger.debug("ReportsDaoImpl :: getNoResponseReportForLocationIds method");
		
		MapSqlParameterSource parameters = new MapSqlParameterSource();
		parameters.addValue("ids", locationList);
		parameters.addValue("fromDate", fromDate);
		parameters.addValue("toDate", toDate);
		
		List<RespondPrescription> prescriptionList = namedParameterJdbcTemplate.query(
				FastRxQueryConstants.GET_NO_RESPONSE_PRESC_FOR_LOC_IDS_FOR_DATE_RANGE, parameters,
				new RespondPrescriptionRowMapper());
		return prescriptionList;
	}

	@Override
	public List<TransactionReport> getTransactionReportForLocationIds(Set<Long> locationList, String fromDate,
			String toDate, String transactiontype) {
		logger.debug("ReportsDaoImpl :: getTransactionReportForLocationIds method");
		
		MapSqlParameterSource parameters = new MapSqlParameterSource();
		parameters.addValue("ids", locationList);
		parameters.addValue("fromDate", fromDate);
		parameters.addValue("toDate", toDate);
		
		List<TransactionReport> transactionList = null;
		
		if(transactiontype.equalsIgnoreCase("transferIn")){
			transactionList = namedParameterJdbcTemplate.query(
					FastRxQueryConstants.GET_TX_IN_TRANSACTION_REPORTS_FOR_LOC_IDS_FOR_DATE_RANGE, parameters,
					new TransactionReportRowMapper());
			
		}else if (transactiontype.equalsIgnoreCase("transferOut")) {
			transactionList = namedParameterJdbcTemplate.query(
					FastRxQueryConstants.GET_TX_OUT_TRANSACTION_REPORTS_FOR_LOC_IDS_FOR_DATE_RANGE, parameters,
					new TransactionReportRowMapper());
		}else{
			transactionList = namedParameterJdbcTemplate.query(
					FastRxQueryConstants.GET_ALL_TRANSACTION_REPORTS_FOR_LOC_IDS_FOR_DATE_RANGE, parameters,
					new TransactionReportRowMapper());
		}
		
		return transactionList;
	}

	@Override
	public List<RevenueReport> getRevenueReportForLocationIdsForDateRange(Set<Long> corpList, String fromDate,
			String toDate) {
		logger.debug("ReportsDaoImpl :: getRevenueReportForLocationIdsForDateRange method");
		List<RevenueReport> revenueReportList = new ArrayList<RevenueReport>();
		MapSqlParameterSource parameters = new MapSqlParameterSource();
		parameters.addValue("ids", corpList);
		parameters.addValue("fromDate", fromDate);
		parameters.addValue("toDate", toDate);
		revenueReportList=namedParameterJdbcTemplate.query(
				FastRxQueryConstants.GET_REVENUE_DETAILS_FOR_CORP_FOR_DATE_RANGE, parameters,
				new RevenueReportRowMapper());
		return revenueReportList;
	}

	@Override
	public List<RevenueReport> getRevenueReportForLocationIds(Set<Long> corpList) {
		logger.debug("ReportsDaoImpl :: getRevenueReportForLocationIds method");
		List<RevenueReport> revenueReportList = new ArrayList<RevenueReport>();
		MapSqlParameterSource parameters = new MapSqlParameterSource();
		parameters.addValue("ids", corpList);
		revenueReportList=namedParameterJdbcTemplate.query(
				FastRxQueryConstants.GET_REVENUE_DETAILS_FOR_CORP, parameters,
				new RevenueReportRowMapper());
		return revenueReportList;
	}

}
