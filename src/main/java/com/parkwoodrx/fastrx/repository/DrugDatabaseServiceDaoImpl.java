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
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import com.parkwoodrx.fastrx.constants.FastRxQueryConstants;
import com.parkwoodrx.fastrx.mapper.DrugsRowMapper;
import com.parkwoodrx.fastrx.model.Drugs;

@Repository
public class DrugDatabaseServiceDaoImpl implements DrugDatabaseDao {
	private static final Logger logger = LoggerFactory.getLogger(DrugDatabaseServiceDaoImpl.class);

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Autowired
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

	public JdbcTemplate getJdbcTemplate() {
		return jdbcTemplate;
	}

	public NamedParameterJdbcTemplate geNamedParameterJdbcTemplate() {
		return namedParameterJdbcTemplate;
	}

	@Override
	public void uploadDrugDatabase(List<Drugs> drugList) {
		jdbcTemplate.update("TRUNCATE TABLE drugs");
		logger.info("Batch update started:: {}",java.sql.Timestamp.valueOf(java.time.LocalDateTime.now()));
		jdbcTemplate.batchUpdate(FastRxQueryConstants.UPDATE_DRUG_DATABASE, new BatchPreparedStatementSetter() {
			@Override
			public void setValues(PreparedStatement ps, int i) throws SQLException {
				Drugs drug = drugList.get(i);
				ps.setString(1, drug.getProductId());
				ps.setString(2, drug.getProprietaryName());
				ps.setString(3, drug.getProprietaryNameSuffix());
				ps.setString(4, drug.getNonProprietaryName());
				ps.setString(5, drug.getDosageFormName());
				ps.setString(6, drug.getActiveNumeratorStrength());
				ps.setString(7, drug.getActiveIngredUnit());
				ps.setString(8, drug.getDeaSchedule());
			}

			@Override
			public int getBatchSize() {
				return drugList.size();
			}
		});
		logger.info("Batch update ends:: {}" ,java.sql.Timestamp.valueOf(java.time.LocalDateTime.now()));
	}

	@Override
	public List<Drugs> searchDrug(String drugName) throws DataAccessException {
		List<Drugs> drugList = null;
		drugList = jdbcTemplate.query(FastRxQueryConstants.SEARCH_DRUG_bY_PROP_AND_NONPROP_NAME, new DrugsRowMapper(),
				drugName + "%", drugName + "%");

		return drugList;
	}

}
