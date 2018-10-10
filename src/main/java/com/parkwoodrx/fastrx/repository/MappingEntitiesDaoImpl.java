package com.parkwoodrx.fastrx.repository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.parkwoodrx.fastrx.constants.FastRxQueryConstants;

@Repository
public class MappingEntitiesDaoImpl implements MappingEntitiesDao {
	
	private static final Logger logger = LoggerFactory.getLogger(MappingEntitiesDaoImpl.class);
	
	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Override
	public long getCorporationIdForUser(long userId) throws EmptyResultDataAccessException{
		logger.debug("MappingEntitiesDaoImpl :: getCorporationIdForUser method");
		long corporationId=jdbcTemplate.queryForObject(FastRxQueryConstants.GET_PHARMACY_USER_MAPPING, new Object[]{userId}, Long.class);
		return corporationId;
	}
	
	@Override
	public long getLocationIdForUser(long userId) throws EmptyResultDataAccessException{
		logger.debug("MappingEntitiesDaoImpl :: getLocationIdForUser method");
		long locationId=jdbcTemplate.queryForObject(FastRxQueryConstants.GET_LOCATION_USER_MAPPING, new Object[]{userId}, Long.class);
		return locationId;
	}

	@Override
	public void addCorporationUserMapping(long corporationId, long userId) throws DataAccessException{
		logger.debug("MappingEntitiesDaoImpl :: addCorporationUserMapping method");
		jdbcTemplate.update(FastRxQueryConstants.INSERT_CORPORATION_USER_MAPPING, new Object[]{corporationId, userId});
	}

	@Override
	public void addLocationUserMapping(long locationId, long userId) throws DataAccessException {
		logger.debug("MappingEntitiesDaoImpl :: addLocationUserMapping method");
		jdbcTemplate.update(FastRxQueryConstants.INSERT_LOCATION_USER_MAPPING, new Object[]{locationId, userId});
	}


}
