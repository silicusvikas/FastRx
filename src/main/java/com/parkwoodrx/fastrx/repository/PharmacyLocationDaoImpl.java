package com.parkwoodrx.fastrx.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import com.parkwoodrx.fastrx.constants.FastRxErrorCodes;
import com.parkwoodrx.fastrx.constants.FastRxQueryConstants;
import com.parkwoodrx.fastrx.exception.FastRxException;
import com.parkwoodrx.fastrx.mapper.MultiSelectDropDownObjectRowMapper;
import com.parkwoodrx.fastrx.mapper.NonRegPharMapper;
import com.parkwoodrx.fastrx.mapper.PharmacyLocationRowMapper;
import com.parkwoodrx.fastrx.mapper.PharmacyLocationWithCorporationRowMapper;
import com.parkwoodrx.fastrx.model.Drugs;
import com.parkwoodrx.fastrx.model.MultiSelectDropDownObject;
import com.parkwoodrx.fastrx.model.NonRegPharmacy;
import com.parkwoodrx.fastrx.model.PharmacyLocation;
import com.parkwoodrx.fastrx.model.PharmacyLocationWithCorporation;
import com.parkwoodrx.fastrx.model.User;

@Repository
public class PharmacyLocationDaoImpl implements PharmacyLocationDao {
	private static final Logger logger = LoggerFactory.getLogger(PharmacyLocationDao.class);

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
	public PharmacyLocation getPharmacyLocationByLocationPin(String locationPin) throws EmptyResultDataAccessException {
		logger.debug("PharmacyLocationDaoImpl :: getPharmacyLocationByLocationPin method");
		PharmacyLocation pharmacyLocation = jdbcTemplate.queryForObject(
				FastRxQueryConstants.GET_PHARMACY_LOCATION_BY_LOCATION_PIN, new Object[] { locationPin },
				new PharmacyLocationRowMapper());
		return pharmacyLocation;
	}

	@Override
	public List<PharmacyLocation> getAllPharmacyLocationForCorporationId(long corporationId)
			throws EmptyResultDataAccessException {
		logger.debug("PharmacyLocationDaoImpl :: getAllPharmacyLocationForCorporationId method");
		List<PharmacyLocation> pharmacyLocationList = jdbcTemplate.query(
				FastRxQueryConstants.GET_LOCATION_LIST_FOR_CORPORATION_ID, new Object[] { corporationId },
				new PharmacyLocationRowMapper());
		return pharmacyLocationList;
	}

	
	
	public long registerPharmacyLocation(PharmacyLocation pharmacyLocation) throws DataAccessException {
		KeyHolder holder = new GeneratedKeyHolder();
		jdbcTemplate.update(new PreparedStatementCreator() {
			@Override
			public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
				PreparedStatement ps = connection.prepareStatement(FastRxQueryConstants.INSERT_PHARMACY_LOCATION,
						Statement.RETURN_GENERATED_KEYS);
				ps.setLong(1, Long.valueOf(pharmacyLocation.getCorporationId()));
				ps.setString(2, pharmacyLocation.getPharmacyName());
				ps.setString(3, pharmacyLocation.getStateLicenseNumber());
				ps.setString(4, pharmacyLocation.getNcpdpNumber());
				ps.setString(5, pharmacyLocation.getNpiNumber());
				ps.setString(6, pharmacyLocation.getDeaNumber());
				ps.setString(7, pharmacyLocation.getLocationPin());
				ps.setString(8, pharmacyLocation.getAddress());
				ps.setString(9, pharmacyLocation.getCity());
				ps.setString(10, pharmacyLocation.getState());
				ps.setString(11, pharmacyLocation.getZipcode());
				ps.setString(12, pharmacyLocation.getPhoneNumber());
				ps.setString(13, pharmacyLocation.getFaxNumber());
				ps.setString(14, pharmacyLocation.getFastRxAudit().getCreatedBy());
				ps.setString(15, pharmacyLocation.getActive());
				ps.setString(16, pharmacyLocation.getPharmacyStoreNumber());
				ps.setString(17, pharmacyLocation.getAddressSameAsCorp().trim());
				return ps;
			}
		}, holder);
		long locationId = holder.getKey().longValue();
		return locationId;
	}

	public void updatePharmacyLocation(PharmacyLocation pharmacyLocation) throws DataAccessException {
		jdbcTemplate.update(FastRxQueryConstants.UPDATE_PHARMACY_LOCATION,
				new Object[] { pharmacyLocation.getPharmacyName(), pharmacyLocation.getStateLicenseNumber(),
						pharmacyLocation.getNcpdpNumber(), pharmacyLocation.getNpiNumber(),
						pharmacyLocation.getDeaNumber(), pharmacyLocation.getAddress(), pharmacyLocation.getCity(),
						pharmacyLocation.getState(), pharmacyLocation.getZipcode(), pharmacyLocation.getFaxNumber(),
						pharmacyLocation.getFastRxAudit().getUpdatedBy(), pharmacyLocation.getPharmacyStoreNumber(),
						java.sql.Timestamp.valueOf(java.time.LocalDateTime.now()),pharmacyLocation.getAddressSameAsCorp(), pharmacyLocation.getId() });
	}

	public void updatePharmacyLocationStatus(PharmacyLocation pharmacyLocation) throws FastRxException {

		try {
			jdbcTemplate.update(FastRxQueryConstants.UPDATE_PHARMACY_LOCATION_STATUS, new Object[] {

					pharmacyLocation.getFastRxAudit().getUpdatedBy(), pharmacyLocation.getActive(),
					pharmacyLocation.getId() });
		} catch (Exception e) {
			throw new FastRxException(FastRxErrorCodes.CREATE_ERROR_CODE,
					FastRxErrorCodes.ERROR_UPDATING_PHARMACY_LOCATION_MSG);
		}
	}

	public List<PharmacyLocation> getPharmacyLocationDetail(long pharmacyLocationId) throws FastRxException {

		List<PharmacyLocation> locationList = null;
		try {
			locationList = jdbcTemplate.query(FastRxQueryConstants.GET_PHARMACY_LOCATION_DETAIL,
					new PharmacyLocationRowMapper(), pharmacyLocationId);
		} catch (Exception e) {
			throw new FastRxException(FastRxErrorCodes.ERROR_FETCHING_RECORDS_CODE,
					FastRxErrorCodes.ERROR_FETCHING_RECORDS_MSG);
		}
		return locationList;
	}

	@Override
	public List<PharmacyLocationWithCorporation> searchPharmacyLocationByCorpId(String locationName, String phoneNumber,
			String corpId) throws FastRxException {

		List<PharmacyLocationWithCorporation> locationList = null;
		try {
			if ((locationName != null && phoneNumber != null)
					&& (locationName.length() > 0 && phoneNumber.length() > 0)) {
				locationList = jdbcTemplate.query(
						FastRxQueryConstants.SEARCH_PHARMACY_LOCATION_BY_NAME_PHONE_CORPORATION,
						new PharmacyLocationWithCorporationRowMapper(), "%" + locationName + "%",
						"%" + phoneNumber + "%", corpId);
			} else if (locationName != null && locationName.length() > 0) {
				locationList = jdbcTemplate.query(FastRxQueryConstants.SEARCH_PHARMACY_LOCATION_BY_NAME_CORPORATION,
						new PharmacyLocationWithCorporationRowMapper(), "%" + locationName + "%", corpId);
			} else if (phoneNumber != null && phoneNumber.length() > 0) {
				locationList = jdbcTemplate.query(FastRxQueryConstants.SEARCH_PHARMACY_LOCATION_BY_PHONE_CORPORATION,
						new PharmacyLocationWithCorporationRowMapper(), "%" + phoneNumber + "%", corpId);
			}

		} catch (Exception e) {
			throw new FastRxException(FastRxErrorCodes.ERROR_FETCHING_RECORDS_CODE,
					FastRxErrorCodes.ERROR_FETCHING_RECORDS_MSG);
		}
		return locationList;
	}

	@Override
	public List<PharmacyLocationWithCorporation> searchPharmacyLocationByPhoneAndName(String locationName,
			String phoneNumber) throws FastRxException {

		List<PharmacyLocationWithCorporation> locationList = null;
		try {
			if ((locationName != null && phoneNumber != null)
					&& (locationName.length() > 0 && phoneNumber.length() > 0)) {
				locationList = jdbcTemplate.query(FastRxQueryConstants.SEARCH_PHARMACY_LOCATION,
						new PharmacyLocationWithCorporationRowMapper(), "%" + locationName + "%",
						"%" + phoneNumber + "%");
			} else if (locationName != null && locationName.length() > 0) {
				locationList = jdbcTemplate.query(FastRxQueryConstants.SEARCH_PHARMACY_LOCATION_BY_NAME,
						new PharmacyLocationWithCorporationRowMapper(), "%" + locationName + "%");
			} else if (phoneNumber != null && phoneNumber.length() > 0) {
				locationList = jdbcTemplate.query(FastRxQueryConstants.SEARCH_PHARMACY_LOCATION_BY_PHONE,
						new PharmacyLocationWithCorporationRowMapper(), "%" + phoneNumber + "%");

			}

		} catch (Exception e) {
			throw new FastRxException(FastRxErrorCodes.ERROR_FETCHING_RECORDS_CODE,
					FastRxErrorCodes.ERROR_FETCHING_RECORDS_MSG);
		}
		return locationList;
	}

	@Override
	public List<NonRegPharmacy> searchPharmacyLocationByPhoneAndName(String locationName,
			String phoneNumber, String city, String state, String zip) throws FastRxException {
		List<NonRegPharmacy> locationList = null;
		ArrayList<String> searchParams = new ArrayList<String>();

		try {
			//String selectQuery = "SELECT pl.id, pl.corporation_id,pc.corporation_name,pl.pharmacy_name, pl.state_license_num, pl.ncpdp_num, pl.npi_num, pl.dea_num, pl.location_pin, pl.address,pl.city,pl.state, pl.zip_code, pl.phone_num, pl.fax_num, pl.created_on, pl.created_by,pl.updated_on, pl.updated_by, pl.active,pl.pharmacy_store_num FROM pharmacy_locations pl INNER JOIN pharmacy_corporation pc ON pl.corporation_id=pc.id and pl.active='Y' and pc.active='Y'  WHERE ";
			String selectQuery = "SELECT nr.id as id, nr.pharmacy_name as pharmacy_name, nr.store_address as address, nr.phy_city as city, nr.phy_state as state, nr.phy_zip as zip_code,nr.phy_phone as phone_num, nr.phy_efax as fax_num, nr.npi as npi_num, nr.dea as dea_num,nr.store_number as store_number FROM non_reg_phar nr WHERE";
			if (StringUtils.isNotBlank(locationName)) {
				String likeName = " nr.pharmacy_name LIKE " + "'%" + locationName + "%'";
				searchParams.add(likeName);
			}
			if (StringUtils.isNotBlank(phoneNumber)) {
				String likePhone = " nr.phy_phone LIKE " + "'%" + phoneNumber + "%'";
				searchParams.add(likePhone);
			}
			if (StringUtils.isNotBlank(city)) {
				String likeCity = " nr.phy_city LIKE " + "'%" + city + "%'";
				searchParams.add(likeCity);
			}
			if (StringUtils.isNotBlank(state)) {
				String likeState = " nr.phy_state LIKE " + "'%" + state + "%'";
				searchParams.add(likeState);
			}
			if (StringUtils.isNotBlank(zip)) {
				String likeZip = " nr.phy_zip LIKE " + "'%" + zip + "%'";
				searchParams.add(likeZip);
			}
			int length = searchParams.size();
			for (int i = 0; i < length; i++) {
				if (i == 0) {
					selectQuery += searchParams.get(i);
				} else {
					selectQuery += " AND " + searchParams.get(i);
				}

			}
			locationList = jdbcTemplate.query(selectQuery, new NonRegPharMapper());

		} catch (Exception e) {
			throw new FastRxException(FastRxErrorCodes.ERROR_FETCHING_RECORDS_CODE,
					FastRxErrorCodes.ERROR_FETCHING_RECORDS_MSG);
		}

		return locationList;
	}

	@Override
	public PharmacyLocation getPharmacyLocationByIdAndCorpId(long locationId, long corpId)
			throws EmptyResultDataAccessException {
		if (corpId != 0) {
			return (PharmacyLocation) jdbcTemplate.queryForObject(
					FastRxQueryConstants.GET_PHARMACY_LOCATION_BY_ID_CORPID, new Object[] { locationId, corpId },
					new PharmacyLocationRowMapper());
		} else {
			return (PharmacyLocation) jdbcTemplate.queryForObject(FastRxQueryConstants.GET_PHARMACY_LOCATION_BY_ID,
					new Object[] { locationId }, new PharmacyLocationRowMapper());
		}

	}

	@Override
	public long createUser(User userdetails) throws DuplicateKeyException, SQLException {
		long userId = 0;
		KeyHolder holder = new GeneratedKeyHolder();
		jdbcTemplate.update(new PreparedStatementCreator() {
			@Override
			public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
				PreparedStatement ps = connection.prepareStatement(FastRxQueryConstants.CREATE_USER,
						Statement.RETURN_GENERATED_KEYS);
				ps.setLong(1, userdetails.getRoleId());
				ps.setString(2, userdetails.getFirstName());
				ps.setString(3, userdetails.getLastName());
				ps.setString(4, userdetails.getUsername());
				ps.setString(5, userdetails.getPassword());
				ps.setString(6, userdetails.getPhoneNumber());
				ps.setString(7, userdetails.getActive());
				ps.setString(8, userdetails.getPharmacistLicenseNumber());
				ps.setString(9, userdetails.getFastRxAudit().getCreatedBy());
				ps.setString(10, userdetails.getStateLicenseNumber());
				ps.setString(11, userdetails.getEmail());

				return ps;
			}
		}, holder);
		userId = holder.getKey().longValue();
		return userId;

	}

	@Override
	public void createCorporationUserMapping(long userId, long corpId) throws SQLException {
		jdbcTemplate.update("INSERT INTO corporation_users(corporation_id, user_id) VALUES(?,?)", corpId, userId);
	}

	@Override
	public void updateStatusOfPharmacyLocation(long locationId, String updatedBy, String status) {
		jdbcTemplate.update(FastRxQueryConstants.UPDATE_PHARMACY_LOCATION_STATUS, new Object[] { status,
				java.sql.Timestamp.valueOf(java.time.LocalDateTime.now()), updatedBy, locationId });
	}

	@Override
	public List<PharmacyLocationWithCorporation> getPharmacyLocationList() throws DataAccessException {
		List<PharmacyLocationWithCorporation> pharmacyLocationList = jdbcTemplate
				.query(FastRxQueryConstants.GET_PHARMACY_LOCATION_LIST, new PharmacyLocationWithCorporationRowMapper());
		return pharmacyLocationList;
	}

	@Override
	public PharmacyLocation getPharmacyLocationById(long id) throws EmptyResultDataAccessException {
		return (PharmacyLocation) jdbcTemplate.queryForObject(FastRxQueryConstants.GET_PHARMACY_LOCATION_BY_ID,
				new Object[] { id }, new PharmacyLocationRowMapper());
	}
	
	@Override
	public NonRegPharmacy getNonRegPharmacyById(long id) throws EmptyResultDataAccessException {
		return (NonRegPharmacy) jdbcTemplate.queryForObject(FastRxQueryConstants.GET_NON_REGPHARMACY_BY_ID,
				new Object[] { id }, new NonRegPharMapper());
	}

	@Override
	public void createLocationUserMapping(long locationId, long userId) throws DuplicateKeyException, SQLException {
		jdbcTemplate.update(FastRxQueryConstants.INSERT_LOCATION_USER_MAPPING, new Object[] { locationId, userId });

	}

	@Override
	public PharmacyLocation getPharmacyLocationByUserId(long userId) {
		long locationId = (Long) jdbcTemplate.queryForObject("SELECT location_id FROM location_users WHERE user_id=?",
				new Object[] { userId }, long.class);

		return (PharmacyLocation) jdbcTemplate.queryForObject(FastRxQueryConstants.GET_PHARMACY_LOCATION_BY_ID,
				new Object[] { locationId }, new PharmacyLocationRowMapper());
	}

	@Override
	public List<PharmacyLocation> getPharmacyLocationByCorpId(long corpId) throws DataAccessException {
		List<PharmacyLocation> pharmacyLocationList = jdbcTemplate.query(
				FastRxQueryConstants.GET_PHARMACY_LOCATION_BY_CORPID, new Object[] { corpId },
				new PharmacyLocationRowMapper());
		return pharmacyLocationList;
	}

	@Override
	public long getCorpIdByPharmacyCorporationName(String corporationName) throws DataAccessException {
		long corpId = jdbcTemplate.queryForObject("SELECT id from pharmacy_corporation where corporation_name = ?",
				new Object[] { corporationName }, long.class);
		return corpId;
	}

	@Override
	public List<MultiSelectDropDownObject> getAllPharmacyLocationMultiSelectForCorporationId(long corporationId)
			throws EmptyResultDataAccessException {
		logger.debug("PharmacyLocationDaoImpl :: getAllPharmacyLocationMultiSelectForCorporationId method");
		List<MultiSelectDropDownObject> pharmacyLocationList = jdbcTemplate.query(
				FastRxQueryConstants.GET_LOCATION_LIST_FOR_CORPORATION_ID_MULTISELECT, new Object[] { corporationId },
				new MultiSelectDropDownObjectRowMapper());
		return pharmacyLocationList;
	}

	@Override
	public List<MultiSelectDropDownObject> getAllLocationsMultiSelectForCorpIds(Set<Long> corpIds) {
		logger.debug("PharmacyLocationDaoImpl :: getAllLocationsMultiSelectForCorpIds method");

		MapSqlParameterSource parameters = new MapSqlParameterSource();
		parameters.addValue("ids", corpIds);

		List<MultiSelectDropDownObject> pharmacyLocationList = namedParameterJdbcTemplate.query(
				FastRxQueryConstants.GET_LOCATION_LIST_FOR_CORP_IDS_MULTISELECT, parameters,
				new MultiSelectDropDownObjectRowMapper());
		return pharmacyLocationList;
	}
	

	@Override
	public PharmacyLocation getPharmacyLocationByDeaNumber(String deaNumber)
			throws DuplicateKeyException, SQLException {
		logger.debug("PharmacyLocationDaoImpl :: getPharmacyLocationByDeaNumber method");
		PharmacyLocation pharmacyLocationbyDea = jdbcTemplate.queryForObject(
				FastRxQueryConstants.GET_PHARMACY_LOCATION__BY_DEA_NUMBER, new Object[] { deaNumber },
				new PharmacyLocationRowMapper());
		return pharmacyLocationbyDea;
		
	}

	@Override
	public void uploadNonRegPhar(ArrayList<NonRegPharmacy> nonRegPharmacyList) {
		
			jdbcTemplate.update("TRUNCATE TABLE non_reg_phar");
			logger.info("Batch update started::" + java.sql.Timestamp.valueOf(java.time.LocalDateTime.now()));
			jdbcTemplate.batchUpdate(FastRxQueryConstants.UPDATE_NONREGPHAR_DATABASE, new BatchPreparedStatementSetter() {
				@Override
				public void setValues(PreparedStatement ps, int i) throws SQLException {
					NonRegPharmacy nonRegPharmacy = nonRegPharmacyList.get(i);
					ps.setString(1, nonRegPharmacy.getPharmacy_name());
					ps.setString(2, nonRegPharmacy.getStore_address());
					ps.setString(3, nonRegPharmacy.getPhy_state());
					ps.setString(4, nonRegPharmacy.getPhy_city());
					ps.setString(5, nonRegPharmacy.getPhy_zip());
					ps.setString(6, nonRegPharmacy.getPhy_efax());
					ps.setString(7, nonRegPharmacy.getPhy_phone());
					ps.setString(8, nonRegPharmacy.getPhy_ext());
					ps.setString(9, nonRegPharmacy.getDea());
					ps.setString(10, nonRegPharmacy.getNpi());
					ps.setString(11, nonRegPharmacy.getStore_number());
				}

				@Override
				public int getBatchSize() {
					return nonRegPharmacyList.size();
				}
			});
			logger.info("Batch update ends::" + java.sql.Timestamp.valueOf(java.time.LocalDateTime.now()));
		}		
}
