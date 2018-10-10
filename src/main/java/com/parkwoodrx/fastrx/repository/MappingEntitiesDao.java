package com.parkwoodrx.fastrx.repository;

import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;

public interface MappingEntitiesDao {
	public long getCorporationIdForUser(long userId) throws EmptyResultDataAccessException;
	public long getLocationIdForUser(long userId) throws EmptyResultDataAccessException;
	public void addCorporationUserMapping(long corporationId, long userId) throws DataAccessException;
	public void addLocationUserMapping(long locationId, long userId) throws DataAccessException;
}
