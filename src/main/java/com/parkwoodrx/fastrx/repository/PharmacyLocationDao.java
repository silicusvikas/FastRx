package com.parkwoodrx.fastrx.repository;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.dao.DataAccessException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;

import com.parkwoodrx.fastrx.exception.FastRxException;
import com.parkwoodrx.fastrx.mapper.NonRegPharMapper;
import com.parkwoodrx.fastrx.model.MultiSelectDropDownObject;
import com.parkwoodrx.fastrx.model.NonRegPharmacy;
import com.parkwoodrx.fastrx.model.PharmacyLocation;
import com.parkwoodrx.fastrx.model.PharmacyLocationWithCorporation;
import com.parkwoodrx.fastrx.model.User;

public interface PharmacyLocationDao {
	public PharmacyLocation getPharmacyLocationByLocationPin(String locationPin) throws EmptyResultDataAccessException;
	public PharmacyLocation getPharmacyLocationByDeaNumber(String deaNumber) throws DuplicateKeyException, SQLException;

	public List<PharmacyLocation> getAllPharmacyLocationForCorporationId(long corporationId)
			throws EmptyResultDataAccessException;

	public long registerPharmacyLocation(PharmacyLocation pharmacyLocation) throws FastRxException;

	public List<PharmacyLocationWithCorporation> searchPharmacyLocationByCorpId(String locationName, String phoneNo,
			String corpId);

	public List<PharmacyLocationWithCorporation> searchPharmacyLocationByPhoneAndName(String locationName,
			String phoneNumber) throws FastRxException;

	public PharmacyLocation getPharmacyLocationByIdAndCorpId(long id, long corpId)
			throws EmptyResultDataAccessException;

	public void updatePharmacyLocation(PharmacyLocation pharmacyLocation) throws FastRxException;

	public long createUser(User userdetails) throws DuplicateKeyException, SQLException;

	public void createCorporationUserMapping(long userId, long corpId) throws SQLException;

	public void updateStatusOfPharmacyLocation(long locationId, String updatedBy, String status);

	public List<PharmacyLocationWithCorporation> getPharmacyLocationList() throws DataAccessException;

	public PharmacyLocation getPharmacyLocationById(long id) throws EmptyResultDataAccessException;

	public void createLocationUserMapping(long locationId, long userId) throws DuplicateKeyException, SQLException;

	public PharmacyLocation getPharmacyLocationByUserId(long userId);

	public List<PharmacyLocation> getPharmacyLocationByCorpId(long corpId) throws DataAccessException;

	public long getCorpIdByPharmacyCorporationName(String corporationName) throws DataAccessException;

	public List<MultiSelectDropDownObject> getAllPharmacyLocationMultiSelectForCorporationId(long corporationId);

	public List<MultiSelectDropDownObject> getAllLocationsMultiSelectForCorpIds(Set<Long> corpIds);

	List<NonRegPharmacy> searchPharmacyLocationByPhoneAndName(String pharmacyName, String phoneNo,
			String city, String state, String zip) throws FastRxException;
	
	public void uploadNonRegPhar(ArrayList<NonRegPharmacy> nonRegPharmacyList);
	public NonRegPharmacy getNonRegPharmacyById(long id) throws EmptyResultDataAccessException;
	
	
}
