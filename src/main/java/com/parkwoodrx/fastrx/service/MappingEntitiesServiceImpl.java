package com.parkwoodrx.fastrx.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import com.parkwoodrx.fastrx.constants.FastRxErrorCodes;
import com.parkwoodrx.fastrx.exception.FastRxException;
import com.parkwoodrx.fastrx.repository.MappingEntitiesDao;

@Service
public class MappingEntitiesServiceImpl implements MappingEntitiesService {

	private static final Logger logger = LoggerFactory.getLogger(MappingEntitiesService.class);

	@Autowired
	private MappingEntitiesDao mappingEntitiesDao;

	@Override
	public long getCorporationIdForUser(long userId) {
		logger.debug("MappingEntitiesServiceImpl :: getCorporationIdForUser method");
		long corporationId;
		try {
			corporationId = mappingEntitiesDao.getCorporationIdForUser(userId);
		} catch (EmptyResultDataAccessException e) {
			throw new FastRxException(FastRxErrorCodes.NOT_FOUND_CODE,
					FastRxErrorCodes.USER_CORPORATION_RELATION_NOT_FOUND_MSG);
		}
		return corporationId;
	}

	@Override
	public long getLocationIdForUser(long userId) {
		logger.debug("MappingEntitiesServiceImpl :: getLocationIdForUser method");
		long locationId;
		try {
			locationId = mappingEntitiesDao.getLocationIdForUser(userId);
		} catch (EmptyResultDataAccessException e) {
			throw new FastRxException(FastRxErrorCodes.NOT_FOUND_CODE,
					FastRxErrorCodes.NO_PHARMACY_FOUND_MSG);
		}
		return locationId;
	}
	@Override
	public void addCorporationUserMapping(long corporationId, long userId) {
		logger.debug("MappingEntitiesServiceImpl :: addCorporationUserMapping method");
		try {
			mappingEntitiesDao.addCorporationUserMapping(corporationId, userId);
		} catch (DataAccessException e) {
			throw new FastRxException(FastRxErrorCodes.UPDATE_ERROR_CODE,
					FastRxErrorCodes.UPDATE_USER_CORPORATION_MAPPING_ERROR_MSG);
		}
	}

	@Override
	public void addLocationUserMapping(long locationId, long userId) {
		logger.debug("MappingEntitiesServiceImpl :: addLocationUserMapping method");
		try {
			mappingEntitiesDao.addLocationUserMapping(locationId, userId);
		} catch (DataAccessException e) {
			throw new FastRxException(FastRxErrorCodes.UPDATE_ERROR_CODE,
					FastRxErrorCodes.UPDATE_USER_LOCATION_MAPPING_ERROR_MSG);
		}
	}

}
