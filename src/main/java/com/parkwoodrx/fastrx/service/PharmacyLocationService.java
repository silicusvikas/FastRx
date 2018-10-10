package com.parkwoodrx.fastrx.service;

import java.io.File;
import java.util.List;

import com.parkwoodrx.fastrx.exception.FastRxException;
import com.parkwoodrx.fastrx.model.MultiSelectDropDownObject;
import com.parkwoodrx.fastrx.model.NonRegPharmacy;
import com.parkwoodrx.fastrx.model.PharmacyLocation;
import com.parkwoodrx.fastrx.model.PharmacyLocationWithCorporation;

public interface PharmacyLocationService {
	public PharmacyLocation getPharmacyLocationByLocationPin(String locationPin);
	public PharmacyLocation getPharmacyLocationByDEANumber(String deanumber);
	public List<PharmacyLocation> getAllPharmacyLocationForCorporationId(long corporationId);
	public void registerPharmacyLocation(PharmacyLocation pharmacyLocation) throws FastRxException;
	public List<PharmacyLocationWithCorporation> searchPharmacyLocationAndCorpId(String pharmacyName, String phoneNo,String corpId);
	public List<NonRegPharmacy> searchPharmacyLocation(String pharmacyName, String phoneNo,String city,String state,String zip)
			throws FastRxException;
	public PharmacyLocation getPharmacyLocationById(long id, long corpId);
	public void updatePharmacyLocation(PharmacyLocation pharmacyLocation);
	public void updatedStatusOfLocation(long locationId,String updatedBy, String status);
	public List<PharmacyLocationWithCorporation> getPharmacyLocationList(); 
	public boolean isLocationActive(long locationId);
	public PharmacyLocation getPharmacyLocationByUserId(long userId);
	public List<PharmacyLocation> getPharmacyLocationByCorpId(long corpId);
	public List<PharmacyLocation> uploadPharmacyloactions(File file,String loginUser);
	public List<MultiSelectDropDownObject> getAllLocationMultiSelectForCorporationId(long corporationId);
	public List<MultiSelectDropDownObject> getAllLocationsMultiSelectForCorpIds(List<MultiSelectDropDownObject> corpList);
	public String getPharmacyLocationNameByUserId(long userId);
	public void uploadNonRegPhar(File file, String loginUser);
}
