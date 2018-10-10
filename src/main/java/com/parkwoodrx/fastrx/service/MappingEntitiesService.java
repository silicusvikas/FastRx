package com.parkwoodrx.fastrx.service;

public interface MappingEntitiesService {
	public long getCorporationIdForUser(long userId);
	public long getLocationIdForUser(long userId);
	public void addCorporationUserMapping(long corporationId, long userId);
	public void addLocationUserMapping(long locationId, long userId);
}
