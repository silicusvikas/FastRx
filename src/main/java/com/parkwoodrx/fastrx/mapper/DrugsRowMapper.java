package com.parkwoodrx.fastrx.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.parkwoodrx.fastrx.model.Drugs;


public class DrugsRowMapper implements RowMapper<Drugs> {

	@Override
	public Drugs mapRow(ResultSet rs, int rowNum) throws SQLException {
		Drugs drug=new Drugs();
		drug.setProductId(rs.getString("PRODUCTID"));
		drug.setProprietaryName(rs.getString("PROPRIETARYNAME"));
		drug.setProprietaryNameSuffix(rs.getString("PROPRIETARYNAMESUFFIX"));
		drug.setNonProprietaryName(rs.getString("NONPROPRIETARYNAME"));
		drug.setDosageFormName(rs.getString("DOSAGEFORMNAME"));
		drug.setActiveNumeratorStrength(rs.getString("ACTIVE_NUMERATOR_STRENGTH"));
		drug.setActiveIngredUnit(rs.getString("ACTIVE_INGRED_UNIT"));
		drug.setDeaSchedule(rs.getString("DEASCHEDULE"));
		return drug;
	}

}
