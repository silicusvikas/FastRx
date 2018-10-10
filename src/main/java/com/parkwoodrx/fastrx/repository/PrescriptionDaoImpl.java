package com.parkwoodrx.fastrx.repository;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.parkwoodrx.fastrx.constants.FastRxQueryConstants;
import com.parkwoodrx.fastrx.mapper.PrescriptionRowMapper;
import com.parkwoodrx.fastrx.mapper.RespondPrescriptionRowMapper;
import com.parkwoodrx.fastrx.model.Prescription;
import com.parkwoodrx.fastrx.model.RespondPrescription;
import com.parkwoodrx.fastrx.security.PasswordGenerator;

@Repository
public class PrescriptionDaoImpl implements PrescriptionDao {
	private static final Logger logger = LoggerFactory.getLogger(PrescriptionDao.class);

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Autowired
	private PasswordGenerator passwordGenerator;

	@Override
	public void batchInsertPrescriptionReqs(List<Prescription> prescriptions, String reqToken, long reqCorpId,
			long resCorpId) throws DataAccessException {
		logger.debug("PrescriptionDaoImpl :: batchInsertPrescriptionReqs method");

		jdbcTemplate.batchUpdate(FastRxQueryConstants.ADD_PRESCRIPTION, new BatchPreparedStatementSetter() {
			@Override
			public void setValues(PreparedStatement ps, int i) throws SQLException {
				Prescription prescription = prescriptions.get(i);
				ps.setLong(1, reqCorpId);
				ps.setLong(2, resCorpId);
				ps.setLong(3, prescription.getRequestingPharmacyId());
				ps.setLong(4, prescription.getRespondingPharmacyId());
				ps.setString(5, passwordGenerator.getEncodedString(prescription.getPatientFirstName()));
				ps.setString(6, passwordGenerator.getEncodedString(prescription.getPatientLastName()));
				ps.setString(7, passwordGenerator.getEncodedString(prescription.getPatientDob()));
				ps.setString(8, passwordGenerator.getEncodedString(prescription.getPatientAddress()));
				ps.setString(9, prescription.getPrescNumber());
				ps.setString(10, prescription.getPrescDrugName());
				ps.setString(11, prescription.getReqPharmacyComments());
				ps.setString(12, "Pending");
				ps.setString(13, prescription.getFastRxAudit().getCreatedBy());
				ps.setString(14, reqToken);
				ps.setString(15, prescription.getCompoundCheckFlag());
				ps.setString(16, prescription.getRespTime());
				ps.setString(17, "N");
			}

			@Override
			public int getBatchSize() {
				return prescriptions.size();
			}
		});
	}

	@Override
	public List<Prescription> getListOfPrescriptionsForLocation(long locationId) {
		logger.debug("PrescriptionDaoImpl :: getListOfPrescriptionsForLocation method");
		return jdbcTemplate.query(FastRxQueryConstants.GET_PRISCRIPTIONS_BY_LOCATION, new Object[] { locationId },
				new PrescriptionRowMapper());
	}

	@Override
	public RespondPrescription getPrescriptionDetailsById(long prescriptionId) {
		logger.debug("PrescriptionDaoImpl :: getPrescriptionDetailsById method");
		return jdbcTemplate.queryForObject(FastRxQueryConstants.TRANSFER_OUT_PRESCRIPTION,
				new Object[] { prescriptionId }, new RespondPrescriptionRowMapper());
	}

	@Override
	public List<Prescription> getPrescriptionListForReqToken(String reqToken) {
		logger.debug("PrescriptionDaoImpl :: getPrescriptionListForReqToken method");
		return jdbcTemplate.query(FastRxQueryConstants.GET_PRESCRIPTIONS_FOR_REQTOKEN, new Object[] { reqToken },
				new PrescriptionRowMapper());
	}

	@Override
	public void respondPrescription(Prescription prescription) {
		logger.debug("PrescriptionDaoImpl :: respondPrescription method");
		jdbcTemplate.update(FastRxQueryConstants.UPDATE_PRESCRIPTION, new Object[] {
				passwordGenerator.getEncodedString(prescription.getPatientFirstName()),
				passwordGenerator.getEncodedString(prescription.getPatientLastName()),
				passwordGenerator.getEncodedString(prescription.getPatientDob()),
				passwordGenerator.getEncodedString(prescription.getPatientAddress()), prescription.getPrescNumber(),
				prescription.getPrescDrugName(), prescription.getPrescDrugQty(), prescription.getDirections(),
				prescription.getOrigdateWritten(), prescription.getDateLastFilled(), prescription.getRefillsRemaining(),
				prescription.getProviderName(), prescription.getProviderPhoneNumber(), prescription.getProviderNpi(),
				prescription.getProviderDea(), prescription.getResPharmacyComments(), "Complete",
				java.sql.Timestamp.valueOf(java.time.LocalDateTime.now()), prescription.getFastRxAudit().getUpdatedBy(),
				prescription.getId() });
	}

	@Override
	public void addFaxDetails(String faxJobId, String faxStatus, String updatedBy, long prescriptionId) {
		logger.debug("PrescriptionDaoImpl :: addFaxDetails method");
		jdbcTemplate.update(FastRxQueryConstants.UPDATE_FAX_STATUS_BY_PRESCRIPTION_ID, new Object[] { faxJobId,
				faxStatus, java.sql.Timestamp.valueOf(java.time.LocalDateTime.now()), updatedBy, prescriptionId });

	}

	@Override
	public void updateFaxStatusByFaxId(String updatedBy, String faxJobId, String faxStatus) {
		logger.debug("PrescriptionDaoImpl :: updateFaxStatusByFaxId method");
		jdbcTemplate.update(FastRxQueryConstants.UPDATE_FAX_STATUS_BY_FAXJOB_ID, new Object[] { faxStatus,
				java.sql.Timestamp.valueOf(java.time.LocalDateTime.now()), updatedBy, faxJobId });
	}

}
