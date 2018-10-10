package com.parkwoodrx.fastrx.repository;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.parkwoodrx.fastrx.constants.FastRxErrorCodes;
import com.parkwoodrx.fastrx.constants.FastRxQueryConstants;
import com.parkwoodrx.fastrx.exception.FastRxException;
import com.parkwoodrx.fastrx.mapper.BillingDetailsRowMapper;
import com.parkwoodrx.fastrx.mapper.BillingReportRowMapper;
import com.parkwoodrx.fastrx.model.BillingDetails;
import com.parkwoodrx.fastrx.model.BillingReport;

@Repository
public class TransactionDaoImpl implements TransactionDao {
	
	private static final Logger logger = LoggerFactory.getLogger(TransactionDaoImpl.class);
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
//
//	@Override
//	public List<BillingReport> getBillingReportByCorpId(long corpId) {
//		
//		List<BillingReport> billingReportList = null;
//		
//		try{
//		billingReportList = jdbcTemplate.query(FastRxQueryConstants.BILLING_CHARGE,
//				new Object[] {corpId,corpId,corpId }, new BillingReportRowMapper());
//		} catch (Exception e) {
//			throw new FastRxException(FastRxErrorCodes.FETCHING_BILLING_DETAILS_CODE, 
//					FastRxErrorCodes.FETCHING_BILLING_DETAILS_MSG+corpId);
//		}
//		return billingReportList;
//	}

	@Override
	public List<BillingReport> getBillingReportByCorpId(long corpId) {
		
		List<BillingReport> billingReportList = null;
		
		try{
			billingReportList =jdbcTemplate.query(FastRxQueryConstants.BILLING_CHARGE,
				new Object[] {corpId}, new BillingReportRowMapper());
		} catch (Exception e) {
			throw new FastRxException(FastRxErrorCodes.FETCHING_BILLING_DETAILS_CODE, 
					FastRxErrorCodes.FETCHING_BILLING_DETAILS_MSG+corpId);
		}
		return billingReportList;
	}

	@Override
	public BillingDetails getBillingdetailsForCorp(long corpId) {
		BillingDetails lastBillingDetails = null;
		try{
			 lastBillingDetails = (BillingDetails) jdbcTemplate.queryForObject(
					FastRxQueryConstants.GET_BILLING_BY_CORPORATION_ID, new Object[] { corpId },
						new BillingDetailsRowMapper());
				
			} catch (Exception e) {
				throw new FastRxException(FastRxErrorCodes.FETCHING_BILLING_DETAILS_CODE, 
						FastRxErrorCodes.FETCHING_BILLING_DETAILS_MSG+corpId);
			}
		return lastBillingDetails;
	}

	@Override
	public void updateBillingdetailsForCorp() {
		 jdbcTemplate.execute(FastRxQueryConstants.UPDATE_TXN_STATUS);
		
	}

	@Override
	public List<BillingReport> getMonthlyBillingReportByCorpId(long corpId) {
List<BillingReport> billingReportList = null;
		
		try{
			billingReportList =jdbcTemplate.query(FastRxQueryConstants.Monthly_BILLING_CHARGE,
				new Object[] {corpId}, new BillingReportRowMapper());
		} catch (Exception e) {
			throw new FastRxException(FastRxErrorCodes.FETCHING_BILLING_DETAILS_CODE, 
					FastRxErrorCodes.FETCHING_BILLING_DETAILS_MSG+corpId);
		}
		return billingReportList;
	}


}
