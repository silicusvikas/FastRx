package com.parkwoodrx.fastrx.repository;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.parkwoodrx.fastrx.constants.FastRxQueryConstants;
import com.parkwoodrx.fastrx.mapper.BillingDetailsRowMapper;
import com.parkwoodrx.fastrx.mapper.PrescriptionLogRowMapper;
import com.parkwoodrx.fastrx.model.BillingDetails;
import com.parkwoodrx.fastrx.model.Prescription;
import com.parkwoodrx.fastrx.model.PrescriptionLog;
import com.parkwoodrx.fastrx.security.PasswordGenerator;

@Repository
public class PrescriptionTransactionDaoImpl implements PrescriptionTransactionDao {
	private static final Logger logger = LoggerFactory.getLogger(PrescriptionTransactionDao.class);

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Autowired
	private PasswordGenerator passwordGenerator;

	@Override
	public BillingDetails getBillingDetailsForCorpId(long corpId) {
		logger.debug("PrescriptionTransactionDaoImpl :: getBillingDetailsForCorpId: {}",corpId);
		BillingDetails billingDetails = (BillingDetails) jdbcTemplate.queryForObject(
				FastRxQueryConstants.GET_BILLING_FOR_CORPORATION_ID, new Object[] { corpId },
				new BillingDetailsRowMapper());
		return billingDetails;
	}

	@Override
	public void batchInsertPrescriptionTransactions(List<Prescription> prescriptions, BillingDetails billingDetails,
			BillingDetails resbillingDetails) {
		logger.debug("PrescriptionTransactionDaoImpl :: batchInsertPrescriptionTransactions method");

		jdbcTemplate.batchUpdate(FastRxQueryConstants.INSERT_PRESCRIPTION_TRANSACTION,
				new BatchPreparedStatementSetter() {
					@Override
					public void setValues(PreparedStatement ps, int i) throws SQLException {
						Prescription prescription = prescriptions.get(i);
						ps.setLong(1, prescription.getRequestingCorpId());
						ps.setLong(2, prescription.getRespondingCorpId());
						ps.setLong(3, prescription.getRequestingPharmacyId());
						ps.setLong(4, prescription.getRespondingPharmacyId());

						if ((billingDetails.getTransactionType()).contains("perdayTransaction")) {
							ps.setDouble(5, 0);
							ps.setDouble(6, billingDetails.getTaxPerDay());
							ps.setString(7, billingDetails.getTransactionType());
							ps.setDouble(8, billingDetails.getTransactionRatePerDay());
							ps.setDouble(9, 0);
						} else {
							ps.setDouble(5, billingDetails.getTaxPerMonth());
							ps.setDouble(6, 0);
							ps.setString(7, billingDetails.getTransactionType());
							ps.setDouble(8, 0);
							ps.setDouble(9, billingDetails.getTransactionRatePerMonth());
						}

						ps.setString(10, "pending");
						ps.setString(11, prescription.getFastRxAudit().getCreatedBy());
						ps.setLong(12, prescription.getId());
					}

					@Override
					public int getBatchSize() {
						return prescriptions.size();
					}
				});

	}

	@Override
	public void updatePrescriptionResponse(Prescription prescription, BillingDetails billingDetails) {

		jdbcTemplate.update(FastRxQueryConstants.UPDATE_PRESCRIPTION_TRANSACTION_ON_RESPONSE,
				new Object[] {"Complete", java.sql.Timestamp.valueOf(java.time.LocalDateTime.now()),
						prescription.getFastRxAudit().getUpdatedBy(), prescription.getId() });

	}

	@Override
	public void addPrescriptionLog(PrescriptionLog prescriptionLog) {
		jdbcTemplate.update(FastRxQueryConstants.ADD_PRESCRIPTION_LOG, prescriptionLog.getPrescNumber(),
				passwordGenerator.getEncodedString(prescriptionLog.getPatientFirstName()),
				passwordGenerator.getEncodedString(prescriptionLog.getPatientLastName()),
				passwordGenerator.getEncodedString(prescriptionLog.getPatientDob()),
				passwordGenerator.getEncodedString(prescriptionLog.getPatientAddress()),
				prescriptionLog.getPrescDrugName(), prescriptionLog.getEvent().toString(),
				prescriptionLog.getStatus().toString(), prescriptionLog.getRemarks(), prescriptionLog.getUpdatedBy());

	}

	public List<PrescriptionLog> getPrescriptionLogs() {
		return jdbcTemplate.query(FastRxQueryConstants.GET_PRESCRIPTION_LOG_LIST,
				new PrescriptionLogRowMapper());
	}

	@Override
	public void batchInsertPrescriptionLog(List<PrescriptionLog> prescriptionLogList) {
		logger.debug("PrescriptionTransactionDaoImpl :: batchInsertPrescriptionLogList method");

		jdbcTemplate.batchUpdate(FastRxQueryConstants.ADD_PRESCRIPTION_LOG, new BatchPreparedStatementSetter() {
			@Override
			public void setValues(PreparedStatement ps, int i) throws SQLException {
				PrescriptionLog prescriptionLog = prescriptionLogList.get(i);
				ps.setString(1, prescriptionLog.getPrescNumber());
				ps.setString(2, passwordGenerator.getEncodedString(prescriptionLog.getPatientFirstName()));
				ps.setString(3, passwordGenerator.getEncodedString(prescriptionLog.getPatientLastName()));
				ps.setString(4, passwordGenerator.getEncodedString(prescriptionLog.getPatientDob()));
				ps.setString(5, passwordGenerator.getEncodedString(prescriptionLog.getPatientAddress()));
				ps.setString(6, prescriptionLog.getPrescDrugName());
				ps.setString(7, prescriptionLog.getEvent().toString());
				ps.setString(8, prescriptionLog.getStatus().toString());
				ps.setString(9, prescriptionLog.getRemarks());
				ps.setString(10, prescriptionLog.getUpdatedBy());

			}

			@Override
			public int getBatchSize() {
				return prescriptionLogList.size();
			}
		});

	}

	public java.sql.Date convertStringToDate(String inputDate, Boolean isEndDAte) throws ParseException {
		Date convertedDate = null;
		java.sql.Date sqlStartDate = null;
		try {
			DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
			convertedDate = (Date) formatter.parse(inputDate);
			if (isEndDAte) {
				Calendar c = Calendar.getInstance();
				c.setTime(convertedDate);
				c.add(Calendar.DATE, 1);
				convertedDate = (Date) formatter.parse(formatter.format(c.getTime()));
			}
			sqlStartDate = new java.sql.Date(convertedDate.getTime());

		} catch (Exception e) {
			logger.debug(e.getMessage());
		}

		return sqlStartDate;

	}

	@Override
	public List<PrescriptionLog> searchUserLogByNameAndDate(String name, String startDate, String endDate)
			throws DataAccessException, ParseException {
		String un = "%" + name + "%";
		java.sql.Date sDate = convertStringToDate(startDate, false);
		java.sql.Date eDate = convertStringToDate(endDate, true);
		List<PrescriptionLog> PrescriptionLogList = null;

		if (!startDate.isEmpty() && !endDate.isEmpty()) {
			PrescriptionLogList = jdbcTemplate.query(
					FastRxQueryConstants.SEARCH_PRESCRIPTION_LOG_LIST_BY_USERNAME_AND_DATE,
					new Object[] { un, sDate, eDate }, new PrescriptionLogRowMapper());
		}
		return PrescriptionLogList;
	}

	@Override
	public void updateSendFaxStatus(String prescId) {
		logger.debug("PrescriptionTransactionDaoImpl :: Update resend efax status for prescription id: ",prescId);
		jdbcTemplate.update((FastRxQueryConstants.UPDATE_RESEND_PRESCRIPTION_STATUS), prescId);

	}

}
