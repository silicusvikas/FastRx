package com.parkwoodrx.fastrx.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.openxml4j.exceptions.NotOfficeXmlFileException;
import org.apache.poi.openxml4j.exceptions.OLE2NotOfficeXmlFileException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.monitorjbl.xlsx.StreamingReader;
import com.parkwoodrx.fastrx.constants.FastRxErrorCodes;
import com.parkwoodrx.fastrx.exception.FastRxException;
import com.parkwoodrx.fastrx.model.FastRxAudit;
import com.parkwoodrx.fastrx.model.MultiSelectDropDownObject;
import com.parkwoodrx.fastrx.model.NonRegPharmacy;
import com.parkwoodrx.fastrx.model.PharmacyLocation;
import com.parkwoodrx.fastrx.model.PharmacyLocationWithCorporation;
import com.parkwoodrx.fastrx.model.User;
import com.parkwoodrx.fastrx.repository.PharmacyCorporationDao;
import com.parkwoodrx.fastrx.repository.PharmacyLocationDao;
import com.parkwoodrx.fastrx.security.PasswordGenerator;

@Service
public class PharmacyLocationServiceImpl implements PharmacyLocationService {

	private static final Logger logger = LoggerFactory.getLogger(PharmacyLocationService.class);

	@Autowired
	private PharmacyLocationDao pharmacyLocationRepository;

	@Autowired
	private PasswordGenerator passwordGenerator;

	@Autowired
	private PharmacyLocationService pharmacyLocationService;

	@Autowired
	private EmailService emailService;

	@Autowired
	private PharmacyCorporationDao pharmacyDao;

	int min = 1000;
	int max = 999999;

	@Override
	public PharmacyLocation getPharmacyLocationByLocationPin(String locationPin) {
		logger.info("PharmacyLocationServiceImpl :: getPharmacyLocationByLocationPin method");
		PharmacyLocation pharmacyLocation = null;
		try {
			pharmacyLocation = pharmacyLocationRepository.getPharmacyLocationByLocationPin(locationPin);
		} catch (EmptyResultDataAccessException e) {
			logger.info("Location pin is invalid");
			throw new FastRxException(FastRxErrorCodes.INVALID_CODE, FastRxErrorCodes.INVALID_LOCATION_PIN_MSG);
		}
		return pharmacyLocation;
	}

	@Override
	public List<PharmacyLocation> getAllPharmacyLocationForCorporationId(long corporationId) {
		logger.info("PharmacyLocationServiceImpl :: getAllPharmacyLocationForCorporationId method");
		List<PharmacyLocation> pharmacyLocationList = null;
		try {
			pharmacyLocationList = pharmacyLocationRepository.getAllPharmacyLocationForCorporationId(corporationId);
		} catch (Exception e) {
			throw new FastRxException(FastRxErrorCodes.NOT_FOUND_CODE, FastRxErrorCodes.NO_LOCATION_FOUND_MSG);
		}
		return pharmacyLocationList;
	}

	@Override
	@Transactional
	public void registerPharmacyLocation(PharmacyLocation pharmacyLocation) throws FastRxException {
		long userId = 0;
		long locationId = 0;
		String corporationName = null;
		User userdetails = new User();
		PharmacyLocation pharmacyLocationbyDea = new PharmacyLocation();
		try {
			int locationPin = ThreadLocalRandom.current().nextInt(min, max + 1);
			pharmacyLocation.setLocationPin(String.valueOf(locationPin));
			pharmacyLocation.setActive("Y");
			locationId = pharmacyLocationRepository.registerPharmacyLocation(pharmacyLocation);
		} catch (Exception e) {
			if (e instanceof DuplicateKeyException && e.getMessage().contains("phone_num")) {
				throw new FastRxException(FastRxErrorCodes.DUPLICATE_CODE,
						FastRxErrorCodes.DUPLICATE_PHARMACY_LOCATION_MSG + pharmacyLocation.getPhoneNumber());
			} else if (e instanceof DuplicateKeyException && e.getMessage().contains("dea_num_UNIQUE")) {
				logger.info("PharmacyLocationServiceImpl :: " + e.getMessage());
				throw new FastRxException(FastRxErrorCodes.DUPLICATE_DEA_CODE,
						FastRxErrorCodes.DUPLICATE_PHARMACY_DEA_MSG + pharmacyLocation.getDeaNumber());
			} else {
				logger.info("PharmacyLocationServiceImpl :: " + e.getMessage());
				throw new FastRxException(FastRxErrorCodes.CREATE_ERROR_CODE,
						FastRxErrorCodes.ERROR_CREATING_PHARMACY_LOCATION_MSG);
			}
		}
		try {

			pharmacyLocationbyDea = pharmacyLocationRepository
					.getPharmacyLocationByDeaNumber(pharmacyLocation.getDeaNumber());
		} catch (DuplicateKeyException de) {
			logger.info("PharmacyLocationServiceImpl :: " + de.getMessage());
			throw new FastRxException(FastRxErrorCodes.DUPLICATE_DEA_CODE,
					FastRxErrorCodes.DUPLICATE_PHARMACY_DEA_MSG + pharmacyLocation.getDeaNumber());

		} catch (Exception e) {
			logger.info("PharmacyLocationServiceImpl :: " + e.getMessage());
			throw new FastRxException(FastRxErrorCodes.CREATE_ERROR_CODE,
					FastRxErrorCodes.ERROR_CREATING_PHARMACY_LOCATION_MSG);
		}
		try {
			userdetails.setFirstName(pharmacyLocation.getFirstName());
			userdetails.setLastName(pharmacyLocation.getLastName());
			userdetails.setUsername(pharmacyLocation.getUsername());
			userdetails.setRoleId(3);
			userdetails.setActive("Y");
			String encryptedPassword;
			encryptedPassword = passwordGenerator.generateHash(pharmacyLocation.getPassword());
			userdetails.setPassword(encryptedPassword);
			FastRxAudit fastRxAudit = new FastRxAudit();
			fastRxAudit.setCreatedBy(pharmacyLocation.getFastRxAudit().getCreatedBy());
			userdetails.setFastRxAudit(fastRxAudit);
			userdetails.setPhoneNumber(pharmacyLocation.getPhoneNumber());
			userId = pharmacyLocationRepository.createUser(userdetails);
			if (userId > 0) {
				corporationName = pharmacyDao.getPharmacyCorporationById(pharmacyLocation.getCorporationId())
						.getCorporationName();
				pharmacyLocationRepository.createCorporationUserMapping(userId, pharmacyLocation.getCorporationId());
				emailService.sendUserRegistrationEmail(pharmacyLocation.getUsername(), corporationName);
			}

		} catch (DuplicateKeyException de) {
			logger.info("PharmacyLocationServiceImpl :: " + de.getMessage());
			throw new FastRxException(FastRxErrorCodes.DUPLICATE_CODE, FastRxErrorCodes.USER_EXISTS_MSG);
		} catch (Exception e) {
			logger.info("PharmacyLocationServiceImpl :: " + e.getMessage());
			throw new FastRxException(FastRxErrorCodes.CREATE_ERROR_CODE,
					FastRxErrorCodes.ERROR_CREATING_PHARMACY_LOCATION_MSG);
		}
		try {
			pharmacyLocationRepository.createLocationUserMapping(locationId, userId);

		} catch (DuplicateKeyException de) {
			logger.info("PharmacyLocationServiceImpl :: " + de.getMessage());
			throw new FastRxException(FastRxErrorCodes.DUPLICATE_CODE, FastRxErrorCodes.DUPLICATE_RECORD_MSG);
		} catch (Exception e) {
			logger.info("PharmacyLocationServiceImpl :: " + e.getMessage());
			throw new FastRxException(FastRxErrorCodes.CREATE_ERROR_CODE,
					FastRxErrorCodes.ERROR_CREATING_PHARMACY_LOCATION_MSG);
		}
	}

	@Override
	public List<PharmacyLocationWithCorporation> searchPharmacyLocationAndCorpId(String locationName, String phoneNo,
			String corpId) throws FastRxException {
		List<PharmacyLocationWithCorporation> pharmacyList = null;
		if ((null == locationName || locationName.isEmpty()) && (null == phoneNo || phoneNo.isEmpty())) {
			throw new FastRxException(FastRxErrorCodes.EMPTY_FIELD_CODE, FastRxErrorCodes.EMPTY_SEARCH_FIELDS_MSG);
		}
		try {
			if (Long.parseLong(corpId) != 0) {
				pharmacyList = pharmacyLocationRepository.searchPharmacyLocationByCorpId(locationName, phoneNo, corpId);
			} else {
				pharmacyList = pharmacyLocationRepository.searchPharmacyLocationByPhoneAndName(locationName, phoneNo);
			}

			if (null == pharmacyList || pharmacyList.size() == 0) {
				throw new FastRxException(FastRxErrorCodes.NOT_FOUND_CODE, FastRxErrorCodes.NO_PHARMACY_FOUND_MSG);
			}
		} catch (Exception e) {
			logger.info("SERVICE::" + e.getMessage());
			throw new FastRxException(FastRxErrorCodes.NOT_FOUND_CODE, FastRxErrorCodes.NO_PHARMACY_FOUND_MSG);
		}

		return pharmacyList;
	}

	@Override
	public List<NonRegPharmacy> searchPharmacyLocation(String pharmacyName, String phoneNo, String city, String state,
			String zip) throws FastRxException {
		List<NonRegPharmacy> pharmacyList = null;
		try {
			pharmacyList = pharmacyLocationRepository.searchPharmacyLocationByPhoneAndName(pharmacyName, phoneNo, city,
					state, zip);
			if (null == pharmacyList || pharmacyList.size() == 0) {
				throw new FastRxException(FastRxErrorCodes.NOT_FOUND_CODE, FastRxErrorCodes.NO_PHARMACY_FOUND_MSG);
			}
		} catch (Exception e) {
			logger.info("SERVICE::" + e.getMessage());
			throw new FastRxException(FastRxErrorCodes.NOT_FOUND_CODE, FastRxErrorCodes.NO_PHARMACY_FOUND_MSG);
		}

		return pharmacyList;
	}

	@Override
	public PharmacyLocation getPharmacyLocationById(long id, long corpId) {
		PharmacyLocation pharmacyLocation = null;
		try {
			pharmacyLocation = pharmacyLocationRepository.getPharmacyLocationByIdAndCorpId(id, corpId);
			if (null == pharmacyLocation) {
				throw new FastRxException(FastRxErrorCodes.NOT_FOUND_CODE, FastRxErrorCodes.NO_PHARMACY_FOUND_MSG);
			}
		} catch (Exception e) {
			logger.info("SERVICE::" + e.getMessage());
			throw new FastRxException(FastRxErrorCodes.NOT_FOUND_CODE, FastRxErrorCodes.NO_PHARMACY_FOUND_MSG);
		}

		return pharmacyLocation;
	}

	@Override
	public void updatePharmacyLocation(PharmacyLocation pharmacyLocation) {
		try {
			pharmacyLocationRepository.updatePharmacyLocation(pharmacyLocation);
		} catch (Exception e) {
			logger.info("PharmacyLocationServiceImpl :: " + e.getMessage());
			throw new FastRxException(FastRxErrorCodes.UPDATE_ERROR_CODE, FastRxErrorCodes.LOCATION_UPDATE_ERROR_MSG);
		}

	}

	@Override
	public void updatedStatusOfLocation(long locationId, String updatedBy, String status) {
		try {
			pharmacyLocationRepository.updateStatusOfPharmacyLocation(locationId, updatedBy, status);
		} catch (Exception e) {
			logger.info("SERVICE::" + e.getMessage());
			throw new FastRxException(FastRxErrorCodes.UPDATE_ERROR_CODE,
					FastRxErrorCodes.PHARMACY_LOCATION_STATUS_UPDATE_ERROR_MSG);
		}

	}

	@Override
	public List<PharmacyLocationWithCorporation> getPharmacyLocationList() {
		List<PharmacyLocationWithCorporation> pharmacyLocationList = null;
		try {
			pharmacyLocationList = pharmacyLocationRepository.getPharmacyLocationList();
		} catch (Exception e) {
			logger.info("SERVICE :: " + e.getMessage());
			throw new FastRxException(FastRxErrorCodes.NOT_FOUND_CODE, FastRxErrorCodes.NO_LOCATION_FOUND_MSG);
		}
		return pharmacyLocationList;
	}

	@Override
	public boolean isLocationActive(long locationId) {
		Boolean isLocationactive = false;
		PharmacyLocation pharmacyLocation = pharmacyLocationRepository.getPharmacyLocationById(locationId);
		if (pharmacyLocation.getActive().equalsIgnoreCase("Y")) {
			isLocationactive = true;
		}
		return isLocationactive;
	}

	@Override
	public PharmacyLocation getPharmacyLocationByUserId(long userId) {
		PharmacyLocation pharmacyLocation = null;
		try {
			pharmacyLocation = pharmacyLocationRepository.getPharmacyLocationByUserId(userId);
			if (null == pharmacyLocation) {
				throw new FastRxException(FastRxErrorCodes.NOT_FOUND_CODE, FastRxErrorCodes.NO_PHARMACY_FOUND_MSG);
			}
		} catch (Exception e) {
			logger.info("SERVICE::" + e.getMessage());
			throw new FastRxException(FastRxErrorCodes.NOT_FOUND_CODE, FastRxErrorCodes.NO_PHARMACY_FOUND_MSG);
		}

		return pharmacyLocation;
	}

	@Override
	public List<PharmacyLocation> getPharmacyLocationByCorpId(long corpId) {
		List<PharmacyLocation> pharmacyLocationList = null;
		try {
			pharmacyLocationList = pharmacyLocationRepository.getPharmacyLocationByCorpId(corpId);
		} catch (Exception e) {
			logger.info("SERVICE :: " + e.getMessage());
			throw new FastRxException(FastRxErrorCodes.NOT_FOUND_CODE, FastRxErrorCodes.NO_LOCATION_FOUND_MSG);
		}
		return pharmacyLocationList;
	}

	@Override
	public List<PharmacyLocation> uploadPharmacyloactions(File fileInput, String loginUser) {
		logger.info("**********START PharmacyLocationServiceImpl :uploadPharmacyloactions*********");
		List<PharmacyLocation> pharmacyLocationList = new ArrayList<PharmacyLocation>();
		List<PharmacyLocation> failedPharmacyLocationList = new ArrayList<PharmacyLocation>();
		String line = "";
		String cvsSplitBy = ",";
		String locationName = "";
		long corporationId = 0;
		int count = 0;
		try {
			try (BufferedReader br = new BufferedReader(new FileReader(fileInput))) {
				while ((line = br.readLine()) != null) {
					if (count != 0) {
						PharmacyLocation pharmacyLocation = new PharmacyLocation();
						pharmacyLocation.setFastRxAudit(new FastRxAudit());
						String[] location = line.split(cvsSplitBy);
						if (corporationId == 0) {
							try {
								corporationId = pharmacyLocationRepository
										.getCorpIdByPharmacyCorporationName(location[1].trim());
							} catch (Exception e) {
								throw new FastRxException(FastRxErrorCodes.FILE_UPLOAD_CORP_CODE,
										FastRxErrorCodes.FILE_UPLOAD_CORP_MSG + location[1].trim()
												+ " is not Registered");
							}
						}
						pharmacyLocation.setCorporationId(corporationId);
						pharmacyLocation.setPharmacyName(location[2]);
						pharmacyLocation.setStateLicenseNumber(location[3]);
						pharmacyLocation.setNcpdpNumber(location[4]);
						pharmacyLocation.setNpiNumber(location[5]);
						pharmacyLocation.setDeaNumber(location[6]);
						pharmacyLocation.setAddress(location[7]);
						pharmacyLocation.setCity(location[8]);
						pharmacyLocation.setState(location[9]);
						pharmacyLocation.setZipcode(location[10]);
						pharmacyLocation.setPhoneNumber(location[11]);
						pharmacyLocation.setFaxNumber(location[12]);
						pharmacyLocation.setPharmacyStoreNumber(location[13]);
						pharmacyLocation.setFirstName(location[14]);
						pharmacyLocation.setLastName(location[15]);
						pharmacyLocation.setUsername(location[16]);
						pharmacyLocation.setAddressSameAsCorp(location[17]);
						pharmacyLocation.getFastRxAudit().setCreatedBy(loginUser);
						pharmacyLocationList.add(pharmacyLocation);
					}
					count++;
				}
				if (1 == count) {
					throw new FastRxException(FastRxErrorCodes.BLANK_FILE_UPLOAD_CODE,
							FastRxErrorCodes.BLANK_FILE_UPLOAD_MSG);
				}
			} catch (IOException e) {
				logger.error("SERVICE error while reading file :: " + e);
				throw new FastRxException(FastRxErrorCodes.FILE_UPLOAD_READ_CODE,
						FastRxErrorCodes.FILE_UPLOAD_READ_MSG);
			}
			Iterator<PharmacyLocation> itr = pharmacyLocationList.listIterator();

			while (itr.hasNext()) {
				PharmacyLocation pharmacyLocation = itr.next();
				try {
					pharmacyLocationService.registerPharmacyLocation(pharmacyLocation);
				} catch (FastRxException e) {
					pharmacyLocation.setFailReason(e.getMessage());
					failedPharmacyLocationList.add(pharmacyLocation);
				} catch (Exception e) {
					pharmacyLocation.setFailReason("No proper information provided");
					failedPharmacyLocationList.add(pharmacyLocation);
				}
			}
			logger.info("failed Pharmacy loactions list" + failedPharmacyLocationList);
		} catch (FastRxException e) {
			logger.error("SERVICE :: " + e.getMessage());
			throw e;
		} catch (Exception e) {
			logger.error("SERVICE :: " + e.getMessage());
			throw new FastRxException(FastRxErrorCodes.FILE_UPLOAD_CODE,
					FastRxErrorCodes.FILE_UPLOAD_MSG + locationName);
		}
		logger.info("**********END PharmacyLocationServiceImpl: uploadPharmacyloactions*********");
		return failedPharmacyLocationList;
	}

	@Override
	public List<MultiSelectDropDownObject> getAllLocationMultiSelectForCorporationId(long corporationId) {
		logger.info("PharmacyLocationServiceImpl :: getAllLocationMultiSelectForCorporationId method");
		List<MultiSelectDropDownObject> pharmacyLocationList = null;
		try {
			pharmacyLocationList = pharmacyLocationRepository
					.getAllPharmacyLocationMultiSelectForCorporationId(corporationId);
		} catch (Exception e) {
			throw new FastRxException(FastRxErrorCodes.NOT_FOUND_CODE, FastRxErrorCodes.NO_LOCATION_FOUND_MSG);
		}
		return pharmacyLocationList;
	}

	@Override
	public List<MultiSelectDropDownObject> getAllLocationsMultiSelectForCorpIds(
			List<MultiSelectDropDownObject> corpList) {
		logger.info("PharmacyLocationServiceImpl :: getAllLocationsMultiSelectForCorpIds method");

		Set<Long> corpIds = new HashSet<Long>();
		for (MultiSelectDropDownObject multiSelectDropDownObject : corpList) {
			corpIds.add(multiSelectDropDownObject.getId());
		}

		List<MultiSelectDropDownObject> pharmacyLocationList = null;
		try {
			pharmacyLocationList = pharmacyLocationRepository.getAllLocationsMultiSelectForCorpIds(corpIds);
		} catch (Exception e) {
			throw new FastRxException(FastRxErrorCodes.NOT_FOUND_CODE, FastRxErrorCodes.NO_LOCATION_FOUND_MSG);
		}
		return pharmacyLocationList;
	}

	@Override
	public String getPharmacyLocationNameByUserId(long userId) {
		String locationName = "";
		try {
			locationName = pharmacyLocationRepository.getPharmacyLocationByUserId(userId).getPharmacyName();
		} catch (Exception e) {
			logger.info("SERVICE::" + e.getMessage());
		}
		return locationName;
	}

	@Override
	public PharmacyLocation getPharmacyLocationByDEANumber(String deanumber) {
		logger.info("PharmacyLocationServiceImpl :: getPharmacyLocationByDEANumber method");
		PharmacyLocation pharmacyLocation = null;
		try {
			pharmacyLocation = pharmacyLocationRepository.getPharmacyLocationByDeaNumber(deanumber);
		} catch (EmptyResultDataAccessException | DuplicateKeyException | SQLException e) {
			logger.info("DEA Number is invalid");
			throw new FastRxException(FastRxErrorCodes.INVALID_CODE, FastRxErrorCodes.INVALID_DEA_NUMBER_MSG);
		}
		return pharmacyLocation;
	}

	@Override
	public void uploadNonRegPhar(File file, String loginUser) {
		ArrayList<NonRegPharmacy> nonRegPharmacyList = null;
		try {
			logger.info("File reading started::" + java.sql.Timestamp.valueOf(java.time.LocalDateTime.now()));
			Workbook wb = StreamingReader.builder().rowCacheSize(100).bufferSize(4096).open(file);
			Sheet datatypeSheet = wb.getSheetAt(0);
			Iterator<Row> iterator = datatypeSheet.iterator();
			nonRegPharmacyList = new ArrayList<NonRegPharmacy>();
			while (iterator.hasNext()) {
				Row currentRow = iterator.next();
				NonRegPharmacy nonRegPharmacy = new NonRegPharmacy();
				Iterator<Cell> cellIterator = currentRow.iterator();
				while (cellIterator.hasNext() && !checkIfRowIsEmpty(currentRow)) {
					Cell currentCell = cellIterator.next();
					if (currentCell.getRowIndex() == 0) {
						if (currentCell.getColumnIndex() == 0
								&& !currentCell.getStringCellValue().equalsIgnoreCase("DBA Name"))
							throw new FastRxException(FastRxErrorCodes.FILE_UPLOAD_READ_CODE,
									FastRxErrorCodes.INVALID_DATA_FORMAT);
						if (currentCell.getColumnIndex() == 1
								&& !currentCell.getStringCellValue().equalsIgnoreCase("store number"))
							throw new FastRxException(FastRxErrorCodes.FILE_UPLOAD_READ_CODE,
									FastRxErrorCodes.INVALID_DATA_FORMAT);
						if (currentCell.getColumnIndex() == 2
								&& !currentCell.getStringCellValue().equalsIgnoreCase("store address"))
							throw new FastRxException(FastRxErrorCodes.FILE_UPLOAD_READ_CODE,
									FastRxErrorCodes.INVALID_DATA_FORMAT);
						if (currentCell.getColumnIndex() == 3
								&& !currentCell.getStringCellValue().equalsIgnoreCase("phys location city"))
							throw new FastRxException(FastRxErrorCodes.FILE_UPLOAD_READ_CODE,
									FastRxErrorCodes.INVALID_DATA_FORMAT);
						if (currentCell.getColumnIndex() == 4
								&& !currentCell.getStringCellValue().equalsIgnoreCase("state"))
							throw new FastRxException(FastRxErrorCodes.FILE_UPLOAD_READ_CODE,
									FastRxErrorCodes.INVALID_DATA_FORMAT);
						if (currentCell.getColumnIndex() == 5
								&& !currentCell.getStringCellValue().equalsIgnoreCase("zip"))
							throw new FastRxException(FastRxErrorCodes.FILE_UPLOAD_READ_CODE,
									FastRxErrorCodes.INVALID_DATA_FORMAT);
						if (currentCell.getColumnIndex() == 6
								&& !currentCell.getStringCellValue().equalsIgnoreCase("phone #"))
							throw new FastRxException(FastRxErrorCodes.FILE_UPLOAD_READ_CODE,
									FastRxErrorCodes.INVALID_DATA_FORMAT);
						if (currentCell.getColumnIndex() == 7
								&& !currentCell.getStringCellValue().equalsIgnoreCase("ext"))
							throw new FastRxException(FastRxErrorCodes.FILE_UPLOAD_READ_CODE,
									FastRxErrorCodes.INVALID_DATA_FORMAT);
						if (currentCell.getColumnIndex() == 8
								&& !currentCell.getStringCellValue().equalsIgnoreCase("fax #"))
							throw new FastRxException(FastRxErrorCodes.FILE_UPLOAD_READ_CODE,
									FastRxErrorCodes.INVALID_DATA_FORMAT);
						if (currentCell.getColumnIndex() == 17
								&& !currentCell.getStringCellValue().equalsIgnoreCase("DEA"))
							throw new FastRxException(FastRxErrorCodes.FILE_UPLOAD_READ_CODE,
									FastRxErrorCodes.INVALID_DATA_FORMAT);

					}
					if (currentCell.getRowIndex() != 0) {
						if (currentCell.getColumnIndex() == 0) {
							if (!currentCell.getStringCellValue().isEmpty()) {
								if (currentCell.getStringCellValue().length() > 200) {
									throw new FastRxException(FastRxErrorCodes.FILE_UPLOAD_READ_CODE,
											"Pharmacy Name can not be greter than 200 characters");
								} else {
									nonRegPharmacy.setPharmacy_name(currentCell.getStringCellValue());
								}
							} else {
								throw new FastRxException(FastRxErrorCodes.FILE_UPLOAD_READ_CODE,
										"Pharmacy Name can not be empty");
							}
						}

						if (currentCell.getColumnIndex() == 1) {
							if (!currentCell.getStringCellValue().isEmpty()) {
								nonRegPharmacy.setStore_number(currentCell.getStringCellValue());
							} else if(currentCell.getStringCellValue().length() > 100) {
								throw new FastRxException(FastRxErrorCodes.FILE_UPLOAD_READ_CODE,
										"Store Number can not be greter than 100 characters");
							}
						}

						if (currentCell.getColumnIndex() == 2) {
							if (!currentCell.getStringCellValue().isEmpty()) {

								if (currentCell.getStringCellValue().length() > 300) {
									throw new FastRxException(FastRxErrorCodes.FILE_UPLOAD_READ_CODE,
											"Store Address can not be greter than 300 characters");
								} else {
									nonRegPharmacy.setStore_address(currentCell.getStringCellValue());
								}
							} else {
								throw new FastRxException(FastRxErrorCodes.FILE_UPLOAD_READ_CODE,
										"Store Address can not be empty");
							}
						}

						if (currentCell.getColumnIndex() == 3) {
							if (!currentCell.getStringCellValue().isEmpty()) {

								if (currentCell.getStringCellValue().length() > 200) {
									throw new FastRxException(FastRxErrorCodes.FILE_UPLOAD_READ_CODE,
											"City Name can not be greter than 200 characters");
								} else {
									nonRegPharmacy.setPhy_city((currentCell.getStringCellValue()));
								}
							} else {
								throw new FastRxException(FastRxErrorCodes.FILE_UPLOAD_READ_CODE,
										"Pharmacy City can not be empty");
							}
						}

						if (currentCell.getColumnIndex() == 4) {
							if (!currentCell.getStringCellValue().isEmpty()) {
								if (currentCell.getStringCellValue().length() > 200) {
									throw new FastRxException(FastRxErrorCodes.FILE_UPLOAD_READ_CODE,
											"Pharmacy State can not be greter than 200 characters");
								} else {
									nonRegPharmacy.setPhy_state(currentCell.getStringCellValue());
								}
							} else {
								throw new FastRxException(FastRxErrorCodes.FILE_UPLOAD_READ_CODE,
										"Pharmacy State can not be empty");
							}
						}

						if (currentCell.getColumnIndex() == 5) {
							if (!currentCell.getStringCellValue().isEmpty()) {

								if (currentCell.getStringCellValue().length() > 50) {
									throw new FastRxException(FastRxErrorCodes.FILE_UPLOAD_READ_CODE,
											"Pharmacy Zipcode should be of 50 digits");
								} else {
									nonRegPharmacy.setPhy_zip(currentCell.getStringCellValue());
								}
							} else {
								throw new FastRxException(FastRxErrorCodes.FILE_UPLOAD_READ_CODE,
										"Pharmacy Zipcode can not be empty");
							}
						}

						if (currentCell.getColumnIndex() == 6) {
							if (!currentCell.getStringCellValue().isEmpty()) {

								if (currentCell.getStringCellValue().length() >50 ) {
									throw new FastRxException(FastRxErrorCodes.FILE_UPLOAD_READ_CODE,
											"Pharmacy Phone Number should be of 50 digits");
								} else {
									nonRegPharmacy.setPhy_phone(currentCell.getStringCellValue());
								}
							} else {
								throw new FastRxException(FastRxErrorCodes.FILE_UPLOAD_READ_CODE,
										"Pharmacy Phone Number can not be empty");
							}
						}

						if (currentCell.getColumnIndex() == 7) {
							nonRegPharmacy.setPhy_ext(currentCell.getStringCellValue());
							if (currentCell.getStringCellValue().length() >50) {
								throw new FastRxException(FastRxErrorCodes.FILE_UPLOAD_READ_CODE,
										"Pharmacy Extension Number can not be greater than 50 digits");
							} else {
								nonRegPharmacy.setPhy_efax(currentCell.getStringCellValue());
							}
						}
						if (currentCell.getColumnIndex() == 8) {
							if (!currentCell.getStringCellValue().isEmpty()) {

								if (currentCell.getStringCellValue().length() != 10) {
									throw new FastRxException(FastRxErrorCodes.FILE_UPLOAD_READ_CODE,
											"Pharmacy Fax Number should be of 10 digits");
								} else {
									nonRegPharmacy.setPhy_efax(currentCell.getStringCellValue());
								}
							} else {
								throw new FastRxException(FastRxErrorCodes.FILE_UPLOAD_READ_CODE,
										"Pharmacy Fax Number can not be empty");
							}
						}

						if (currentCell.getColumnIndex() == 17) {
							if (!currentCell.getStringCellValue().isEmpty()) {

								if (currentCell.getStringCellValue().length() > 100) {
									throw new FastRxException(FastRxErrorCodes.FILE_UPLOAD_READ_CODE,
											"Pharmacy DEA Number should be of 100 characters");
								} else {
									nonRegPharmacy.setDea(currentCell.getStringCellValue());
								}
							} else {
								throw new FastRxException(FastRxErrorCodes.FILE_UPLOAD_READ_CODE,
										"Pharmacy DEA Number can not be empty");
							}
						}

					}
				}
				if (null != nonRegPharmacy.getDea()) {
					nonRegPharmacyList.add(nonRegPharmacy);
				}
			}
			logger.info("File reading ends::" + java.sql.Timestamp.valueOf(java.time.LocalDateTime.now()));
			logger.info("No of drugs:: " + nonRegPharmacyList.size());
			pharmacyLocationRepository.uploadNonRegPhar(nonRegPharmacyList);
		} catch (FastRxException fe) {
			logger.error("SERVICE error while reading file :: " + fe.getMessage());
			throw fe;
		} catch (OLE2NotOfficeXmlFileException onx) {
			logger.error("SERVICE error while reading file :: " + onx);
			throw new FastRxException(FastRxErrorCodes.FILE_UPLOAD_READ_CODE, FastRxErrorCodes.INVALID_FILE_FORMAT);
		} catch (NotOfficeXmlFileException nf) {
			logger.error("SERVICE error while reading file :: " + nf);
			throw new FastRxException(FastRxErrorCodes.FILE_UPLOAD_READ_CODE, FastRxErrorCodes.INVALID_FILE_FORMAT);
		} catch (Exception e) {
			logger.error("SERVICE error while reading file :: " + e.getMessage());
			if (e.getMessage().contains("phy_phone_UNIQUE")) {
				throw new FastRxException(FastRxErrorCodes.FILE_UPLOAD_READ_CODE,
						e.getCause().getMessage().split("key")[0] + " Pharmacy Phone Number");
			} else if (e.getMessage().contains("dea_UNIQUE")) {
				throw new FastRxException(FastRxErrorCodes.FILE_UPLOAD_READ_CODE,
						e.getCause().getMessage().split("key")[0] + " Pharmacy DEA Number");
			} else {
				throw new FastRxException(FastRxErrorCodes.FILE_UPLOAD_READ_CODE,
						FastRxErrorCodes.FILE_UPLOAD_READ_MSG);
			}

		}

	}

	private boolean checkIfRowIsEmpty(Row row) {
		if (row == null) {
			return true;
		}
		if (row.getLastCellNum() <= 0) {
			return true;
		}
		for (int cellNum = row.getFirstCellNum(); cellNum < row.getLastCellNum(); cellNum++) {
			Cell cell = row.getCell(cellNum);
			if (cell != null && cell.getCellTypeEnum() != CellType.BLANK && StringUtils.isNotBlank(cell.toString())) {
				return false;
			}
		}
		return true;
	}

}
