package com.parkwoodrx.fastrx.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.parkwoodrx.fastrx.model.PrescriptionLog;
import com.parkwoodrx.fastrx.model.PrescriptionLog.Event;
import com.parkwoodrx.fastrx.model.PrescriptionLog.Status;

public class PrescriptionLogRowMapper implements RowMapper<PrescriptionLog> {

	@Override
	public PrescriptionLog mapRow(ResultSet rs, int rowNum) throws SQLException {
		PrescriptionLog prescriptionLog=new PrescriptionLog();
		prescriptionLog.setId(rs.getLong("id"));
		prescriptionLog.setDate(rs.getTimestamp("date"));
		prescriptionLog.setPrescNumber(rs.getString("presc_number"));
		prescriptionLog.setPatientFirstName(rs.getString("patient_firstname"));
		prescriptionLog.setPatientLastName(rs.getString("patient_lastname"));
		prescriptionLog.setPatientDob(rs.getString("patient_dob"));
		prescriptionLog.setPatientAddress(rs.getString("patient_address"));
		prescriptionLog.setPrescNumber(rs.getString("presc_number"));
		prescriptionLog.setPrescDrugName(rs.getString("presc_drug_name"));
		prescriptionLog.setEvent(Event.valueOf(rs.getString("event")));
		prescriptionLog.setRemarks(rs.getString("remarks"));
		prescriptionLog.setStatus((Status.valueOf(rs.getString("status"))));
		prescriptionLog.setUpdatedBy(rs.getString("updated_by"));
		return prescriptionLog;
	}

}
