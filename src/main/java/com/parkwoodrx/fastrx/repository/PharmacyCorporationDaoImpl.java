package com.parkwoodrx.fastrx.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import com.parkwoodrx.fastrx.constants.FastRxErrorCodes;
import com.parkwoodrx.fastrx.constants.FastRxQueryConstants;
import com.parkwoodrx.fastrx.exception.FastRxException;
import com.parkwoodrx.fastrx.mapper.BillingDetailsRowMapper;
import com.parkwoodrx.fastrx.mapper.MultiSelectDropDownObjectRowMapper;
import com.parkwoodrx.fastrx.mapper.PaymentTypeRowMapper;
import com.parkwoodrx.fastrx.mapper.PaymentdetailsRowMapper;
import com.parkwoodrx.fastrx.mapper.PharmacyCorporationDetailsRowMapper;
import com.parkwoodrx.fastrx.mapper.PharmacyCorporationRowMapper;
import com.parkwoodrx.fastrx.mapper.StateRowMapper;
import com.parkwoodrx.fastrx.model.BillingDetails;
import com.parkwoodrx.fastrx.model.CreditcardDetails;
import com.parkwoodrx.fastrx.model.InvoiceDetails;
import com.parkwoodrx.fastrx.model.MultiSelectDropDownObject;
import com.parkwoodrx.fastrx.model.PaymentProfileDetails;
import com.parkwoodrx.fastrx.model.PaymentType;
import com.parkwoodrx.fastrx.model.PharmacyCorporation;
import com.parkwoodrx.fastrx.model.PharmacyCorporationDetails;
import com.parkwoodrx.fastrx.model.State;
import com.parkwoodrx.fastrx.model.User;

@Repository
public class PharmacyCorporationDaoImpl implements PharmacyCorporationDao {

	private static final Logger logger = LoggerFactory.getLogger(PharmacyCorporationDaoImpl.class);

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Override
	public CreditcardDetails createPaymentProfile(CreditcardDetails creditcardDetails) {
		String cardNumber = creditcardDetails.getCardNumber();
		String maskCardNumber = null;
		String mask = "XXXXXXXXXXXX";
		if (cardNumber.length() == 4) {
			maskCardNumber = mask + cardNumber;
		} else if (cardNumber.length() > 4) {
			maskCardNumber = mask + cardNumber.substring(cardNumber.length() - 4);
		} else {
			throw new FastRxException(1111, "card number less than 4 digit");
		}
		creditcardDetails.setCardNumber(maskCardNumber);
		CreditcardDetails card = new CreditcardDetails();
		try {
			KeyHolder holder = new GeneratedKeyHolder();
			jdbcTemplate.update(new PreparedStatementCreator() {
				@Override
				public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
					PreparedStatement ps = connection.prepareStatement(FastRxQueryConstants.ADD_PAYMENT_PROFILE,
							Statement.RETURN_GENERATED_KEYS);
					ps.setLong(1, creditcardDetails.getCorporationId());
					ps.setString(2, creditcardDetails.getCustomerProfileId());
					ps.setString(3, creditcardDetails.getCustomerPaymentAcctId());
					ps.setString(4, "Y"); // to do
					ps.setString(5, creditcardDetails.getFastRxAudit().getCreatedBy());
					ps.setString(6, creditcardDetails.getCardNumber());
					ps.setString(7, creditcardDetails.getCardHolderName());
					return ps;
				}
			}, holder);
			long newUserId = holder.getKey().longValue();
			card.setId(newUserId);
		} catch (DataAccessException e) {
			logger.info("DAO::" + e.getMessage());
			throw new FastRxException(FastRxErrorCodes.DATABASE_ERROR_CODE, FastRxErrorCodes.DATABASE_ERROR_MSG,
					e.getMessage());
		}

		return card;
	}

	@Override
	public long registerPharmacyCorporation(PharmacyCorporationDetails pharmacyDetails) {
		PharmacyCorporation pharmacyCorporationDetails = pharmacyDetails.getPharmacyCorporation();
		long corporationId;

		/* Pharmacy Registration */
		KeyHolder holder = new GeneratedKeyHolder();
		jdbcTemplate.update(new PreparedStatementCreator() {
			@Override
			public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
				PreparedStatement ps = connection.prepareStatement(FastRxQueryConstants.ADD_PHARMACY_CORPORATION,
						Statement.RETURN_GENERATED_KEYS);
				ps.setString(1, pharmacyCorporationDetails.getCorporationName());
				ps.setString(2, pharmacyCorporationDetails.getAddress());
				ps.setString(3, pharmacyCorporationDetails.getCity());
				ps.setString(4, pharmacyCorporationDetails.getState());
				ps.setString(5, pharmacyCorporationDetails.getZipCode());
				ps.setString(6, pharmacyCorporationDetails.getPrimaryContactPersonFirstname());
				ps.setString(7, pharmacyCorporationDetails.getPrimaryContactPersonLastname());
				ps.setString(8, pharmacyCorporationDetails.getEmailAddrForInvoice());
				ps.setString(9, pharmacyCorporationDetails.getPhoneNumber());
				ps.setString(10, pharmacyCorporationDetails.getFaxNumber());
				ps.setString(11, pharmacyCorporationDetails.getFastRxAudit().getCreatedBy());
				ps.setString(12, pharmacyCorporationDetails.getActive());
				return ps;
			}
		}, holder);
		corporationId = holder.getKey().longValue();
		if (corporationId == 0) {
			throw new FastRxException(FastRxErrorCodes.CREATE_ERROR_CODE, FastRxErrorCodes.ERROR_CREATING_PHARMACY_MSG);
		}

		/* Adding User */
		int countUser = (int) jdbcTemplate.queryForObject(FastRxQueryConstants.COUNT_USER,
				new Object[] { pharmacyDetails.getUserDetails().getUsername() }, int.class);
		if (countUser != 0) {
			throw new FastRxException(FastRxErrorCodes.DUPLICATE_CODE, FastRxErrorCodes.USER_EXISTS_MSG);
		}
		User userdetails = pharmacyDetails.getUserDetails();
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
		long userId = holder.getKey().longValue();
		if (userId == 0) {
			throw new FastRxException(FastRxErrorCodes.CREATE_ERROR_CODE, FastRxErrorCodes.ERROR_CREATING_USER_MSG);
		}

		/* Adding Billing details */
		if (null != pharmacyDetails.getBillingDetails()) {
			BillingDetails billingDetails = pharmacyDetails.getBillingDetails();
			billingDetails.setCorporationId(corporationId);
			jdbcTemplate.update(new PreparedStatementCreator() {
				@Override
				public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
					PreparedStatement ps = connection.prepareStatement(FastRxQueryConstants.ADD_BILLING_DETAILS,
							Statement.RETURN_GENERATED_KEYS);
					ps.setLong(1, billingDetails.getCorporationId());
					ps.setDouble(2, billingDetails.getTaxPerMonth());
					ps.setDouble(3, billingDetails.getTaxPerDay());
					ps.setString(4, billingDetails.getTransactionType());
					ps.setDouble(5, billingDetails.getTransactionRatePerDay());
					ps.setDouble(6, billingDetails.getTransactionRatePerMonth());
					ps.setString(7, billingDetails.getFastRxAudit().getCreatedBy());

					return ps;
				}
			}, holder);
			long billingId = holder.getKey().longValue();
			if (billingId == 0) {
				throw new FastRxException(FastRxErrorCodes.CREATE_ERROR_CODE,
						FastRxErrorCodes.ERROR_AADING_BILLING_MSG);
			}

		} else {
			jdbcTemplate.update(new PreparedStatementCreator() {
				@Override
				public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
					PreparedStatement ps = connection.prepareStatement(FastRxQueryConstants.ADD_DEFAULT_BILLING_DETAILS,
							Statement.RETURN_GENERATED_KEYS);
					ps.setLong(1, corporationId);
					ps.setString(2, pharmacyDetails.getUserDetails().getFastRxAudit().getCreatedBy());
					ps.setString(3, "perdayTransaction");
					return ps;
				}
			});
		}

		/* corporation-user mapping data */
		try {
			jdbcTemplate.update("INSERT INTO corporation_users(corporation_id, user_id) VALUES(?,?)", corporationId,
					userId);

		} catch (Exception e) {
			logger.info("DAO::" + e.getMessage());
			throw new FastRxException(FastRxErrorCodes.DATABASE_ERROR_CODE, FastRxErrorCodes.DATABASE_ERROR_MSG,
					e.getMessage());
		}
		return corporationId;
	}

	@Override
	public List<PharmacyCorporation> getPharmacyCorporationsByNameOrPhone(String corpName, String phoneNo)
			throws DataAccessException {
		String name = "%" + corpName + "%";
		String phone = "%" + phoneNo + "%";
		List<PharmacyCorporation> corporationList = null;
		if (!phoneNo.isEmpty() && !corpName.isEmpty()) {
			corporationList = jdbcTemplate.query(FastRxQueryConstants.SEARCH_CORPORATION_LIKE_NAME_AND_PHONE,
					new Object[] { name, phone }, new PharmacyCorporationRowMapper());
		} else if (!corpName.isEmpty()) {
			corporationList = jdbcTemplate.query(FastRxQueryConstants.SEARCH_CORPORATION_LIKE_NAME,
					new Object[] { name }, new PharmacyCorporationRowMapper());
		} else {
			corporationList = jdbcTemplate.query(FastRxQueryConstants.SEARCH_CORPORATION_LIKE_PHONE,
					new Object[] { phone }, new PharmacyCorporationRowMapper());
		}
		return corporationList;
	}

	@Override
	public List<PharmacyCorporation> getAllPharmacyCorporations() throws DataAccessException {
		List<PharmacyCorporation> corporationList = jdbcTemplate.query(FastRxQueryConstants.GET_CORPORATION_LIST,
				new PharmacyCorporationRowMapper());
		return corporationList;
	}

	@Override
	public PharmacyCorporationDetails getCorporationProfileById(Long id)
			throws FastRxException, EmptyResultDataAccessException {

		PharmacyCorporationDetails corporationDetail = (PharmacyCorporationDetails) jdbcTemplate.queryForObject(
				FastRxQueryConstants.GET_USER_AND_CORPORATION_BY_ID, new Object[] { id },
				new PharmacyCorporationDetailsRowMapper());
		if (null != corporationDetail.getPharmacyCorporation().getPaymentType()
				&& !"Check".equalsIgnoreCase(corporationDetail.getPharmacyCorporation().getPaymentType())) {
			try {
				PaymentProfileDetails paymentDetails = (PaymentProfileDetails) jdbcTemplate.queryForObject(
						FastRxQueryConstants.GET_PAYMENT_DETALS_BY_CORPORATION_ID, new Object[] { id },
						new PaymentdetailsRowMapper());
				if (null != paymentDetails) {
					corporationDetail.setPaymentProfileDetails(paymentDetails);
				}

			} catch (Exception e) {
				logger.info("DAO::Payment type not creditcard");
			}
		}

		try {
			BillingDetails billingDetails = (BillingDetails) jdbcTemplate.queryForObject(
					FastRxQueryConstants.GET_BILLING_BY_CORPORATION_ID, new Object[] { id },
					new BillingDetailsRowMapper());
			if (null != billingDetails) {
				corporationDetail.setBillingDetails(billingDetails);
			}

		} catch (Exception e) {

			logger.info("DAO::Billing details not available");
		}

		return corporationDetail;
	}

	@Override
	public List<PaymentType> getPaymentTypeList() throws DataAccessException {
		List<PaymentType> paymentTypeList = jdbcTemplate.query(FastRxQueryConstants.GET_PAYMENT_TYPE_LIST,
				new PaymentTypeRowMapper());
		return paymentTypeList;
	}

	@Override
	public List<State> getStateList() throws DataAccessException {
		List<State> stateList = jdbcTemplate.query(FastRxQueryConstants.GET_STATE_LIST, new StateRowMapper());
		return stateList;
	}

	@Override
	public void updatePharmacyCorporation(PharmacyCorporation pharmacyCorporation) throws DataAccessException {

		jdbcTemplate.update(FastRxQueryConstants.UPDATE_CORPORATION_DETAILS, new Object[] {
				pharmacyCorporation.getAddress(), pharmacyCorporation.getCity(), pharmacyCorporation.getState(),
				pharmacyCorporation.getZipCode(), pharmacyCorporation.getPrimaryContactPersonFirstname(),
				pharmacyCorporation.getPrimaryContactPersonLastname(), pharmacyCorporation.getEmailAddrForInvoice(),
				pharmacyCorporation.getPhoneNumber(), pharmacyCorporation.getFaxNumber(),
				pharmacyCorporation.getFastRxAudit().getUpdatedBy(), pharmacyCorporation.getActive(),
				java.sql.Timestamp.valueOf(java.time.LocalDateTime.now()), pharmacyCorporation.getId() });

		jdbcTemplate.update(FastRxQueryConstants.UPDATE_LOCATION_ADDRESS,
				new Object[] { pharmacyCorporation.getAddress(), pharmacyCorporation.getCity(),
						pharmacyCorporation.getState(), pharmacyCorporation.getZipCode(),
						pharmacyCorporation.getFastRxAudit().getUpdatedBy(),
						java.sql.Timestamp.valueOf(java.time.LocalDateTime.now()), pharmacyCorporation.getId() });

	}

	@Override
	public void updatePharmacyCorporationBilling(BillingDetails billingDetails) throws DataAccessException {
		
		if(billingDetails.getStart_txnType()!=null) {
			if (billingDetails.getStart_txnType().equalsIgnoreCase("perdayTransaction")) {
				jdbcTemplate.update(FastRxQueryConstants.UPDATE_PER_DAY_BILLING_DETAILS,
						new Object[] { billingDetails.getTaxPerDay(), billingDetails.getTransactionType(),
								billingDetails.getFastRxAudit().getUpdatedBy(), billingDetails.getTransactionRatePerDay(),
								java.sql.Timestamp.valueOf(java.time.LocalDateTime.now()),
								billingDetails.getTransactionTypeChangeLog(), billingDetails.getStart_Day(),
								billingDetails.getStart_txnType(), billingDetails.getCorporationId(),

						});

			} else if (billingDetails.getStart_txnType().equalsIgnoreCase("perMonthTransaction")) {
				jdbcTemplate.update(FastRxQueryConstants.UPDATE_PER_MONTH_BILLING_DETAILS,
						new Object[] { billingDetails.getTaxPerMonth(), billingDetails.getTransactionType(),
								billingDetails.getFastRxAudit().getUpdatedBy(), billingDetails.getTransactionRatePerMonth(),
								java.sql.Timestamp.valueOf(java.time.LocalDateTime.now()),
								billingDetails.getTransactionTypeChangeLog(), billingDetails.getStart_Day(),
								billingDetails.getStart_txnType(), billingDetails.getCorporationId(),

						});
			}
			
		}else {
			if (billingDetails.getTransactionType().equalsIgnoreCase("perdayTransaction")) {
				jdbcTemplate.update(FastRxQueryConstants.UPDATE_PER_DAY_BILLING_DETAILS,
						new Object[] { billingDetails.getTaxPerDay(), billingDetails.getTransactionType(),
								billingDetails.getFastRxAudit().getUpdatedBy(), billingDetails.getTransactionRatePerDay(),
								java.sql.Timestamp.valueOf(java.time.LocalDateTime.now()),
								billingDetails.getTransactionTypeChangeLog(), billingDetails.getStart_Day(),
								billingDetails.getStart_txnType(), billingDetails.getCorporationId(),

						});

			} else if (billingDetails.getTransactionType().equalsIgnoreCase("perMonthTransaction")) {
				jdbcTemplate.update(FastRxQueryConstants.UPDATE_PER_MONTH_BILLING_DETAILS,
						new Object[] { billingDetails.getTaxPerMonth(), billingDetails.getTransactionType(),
								billingDetails.getFastRxAudit().getUpdatedBy(), billingDetails.getTransactionRatePerMonth(),
								java.sql.Timestamp.valueOf(java.time.LocalDateTime.now()),
								billingDetails.getTransactionTypeChangeLog(), billingDetails.getStart_Day(),
								billingDetails.getStart_txnType(), billingDetails.getCorporationId(),

						});
			}
			
		}
		

	}

	@Override
	public int getBillingDetailsByCorporationId(long id) {
		return jdbcTemplate.queryForObject(FastRxQueryConstants.GET_BILLING_COUNT_BY_CORPORATION_ID,
				new Object[] { id }, Integer.class);
	}

	@Override
	public void addBillingdetails(BillingDetails billingDetails) throws DataAccessException {
		jdbcTemplate.update(FastRxQueryConstants.ADD_BILLING_DETAILS,
				new Object[] { billingDetails.getCorporationId(), billingDetails.getTaxPerMonth(),
						billingDetails.getTaxPerDay(), billingDetails.getTransactionType(),
						billingDetails.getTransactionRatePerDay(), billingDetails.getTransactionRatePerMonth(),
						billingDetails.getFastRxAudit().getCreatedBy() });

	}

	@Override
	public int getPaymentProfileByCorporationId(long corporationId) throws EmptyResultDataAccessException {
		return jdbcTemplate.queryForObject(FastRxQueryConstants.GET_CREDITCARD_COUNT_BY_CORPORATION_ID,
				new Object[] { corporationId }, Integer.class);
	}

	@Override
	public void updatePaymentType(String paymentType, long corporationId, String updatedBy) throws DataAccessException {
		jdbcTemplate.update(FastRxQueryConstants.UPDATE_PAYMENT_TYPE, new Object[] { paymentType, updatedBy,
				java.sql.Timestamp.valueOf(java.time.LocalDateTime.now()), corporationId });

	}

	@Override
	public CreditcardDetails getPaymentProfileIdByCorporationId(long corporationId)
			throws EmptyResultDataAccessException, SQLException {
		return (CreditcardDetails) jdbcTemplate.queryForObject(
				FastRxQueryConstants.GET_PAYMENT_PROFILE_BY_CORPORATION_ID, new Object[] { corporationId },
				new RowMapper<CreditcardDetails>() {
					@Override
					public CreditcardDetails mapRow(ResultSet rs, int rowNum) throws SQLException {
						CreditcardDetails card = new CreditcardDetails();
						card.setCustomerPaymentAcctId(rs.getString("cust_payment_acct_id"));
						card.setCustomerProfileId(rs.getString("cust_profile_id"));
						return card;
					}
				});

	}

	@Override
	public void updatePaymentProfileDetails(CreditcardDetails creditcard) throws FastRxException, DataAccessException {
		String cardNumber = creditcard.getCardNumber();
		String maskCardNumber = null;
		String mask = "XXXXXXXXXXXX";
		if (cardNumber.length() == 4) {
			maskCardNumber = mask + cardNumber;
		} else if (cardNumber.length() > 4) {
			maskCardNumber = mask + cardNumber.substring(cardNumber.length() - 4);
		} else {
			throw new FastRxException(1111, "card number less than 4 digit");
		}
		creditcard.setCardNumber(maskCardNumber);
		jdbcTemplate.update(FastRxQueryConstants.UPDATE_PAYMENT_PROFILE,
				new Object[] { creditcard.getCardNumber(), creditcard.getCardHolderName(), creditcard.getCardNumber(),
						java.sql.Timestamp.valueOf(java.time.LocalDateTime.now()), creditcard.getCorporationId() });

	}

	@Override
	public int getCorporationCount(String corporationName) throws EmptyResultDataAccessException {
		return jdbcTemplate.queryForObject(FastRxQueryConstants.VALIDATE_CORPORATION, new Object[] { corporationName },
				int.class);

	}

	@Override
	public void updateStatusOfCorporation(long corporationId, String updatedBy, String status)
			throws DataAccessException {
		jdbcTemplate.update(FastRxQueryConstants.UPDATE_PHARMACY_CORPORATION_STATUS, new Object[] { status,
				java.sql.Timestamp.valueOf(java.time.LocalDateTime.now()), updatedBy, corporationId });

	}

	@Override
	public PharmacyCorporation getPharmacyCorporationById(long corporationId) {
		PharmacyCorporation pharmacyCorporation = null;
		try {
			pharmacyCorporation = (PharmacyCorporation) jdbcTemplate.queryForObject(
					FastRxQueryConstants.GET_CORPORATION_BY_ID, new Object[] { corporationId },
					new PharmacyCorporationRowMapper());
		} catch (Exception e) {
			logger.error("Exception in retriving corporation: ", e);
			throw new FastRxException(FastRxErrorCodes.NOT_FOUND_CODE, FastRxErrorCodes.NO_CORPORATION_FOUND_MSG);
		}
		return pharmacyCorporation;
	}

	@Override
	public List<MultiSelectDropDownObject> getAllPharmacyCorpsForMultiSelect() throws DataAccessException {
		List<MultiSelectDropDownObject> corporationList = jdbcTemplate
				.query(FastRxQueryConstants.GET_CORPORATION_LIST_MULTISELECT, new MultiSelectDropDownObjectRowMapper());
		return corporationList;
	}

	@Override
	public void addInvoiceDetails(InvoiceDetails invoiceDetails) throws DataAccessException {
		try {
			jdbcTemplate.update(FastRxQueryConstants.ADD_INVOICE_DETAILS,
					new Object[] { invoiceDetails.getCorporationId(), invoiceDetails.getBillingAmount(),
							invoiceDetails.getBillingPeriod(), invoiceDetails.getTxnRefId(), invoiceDetails.getStatus(),
							invoiceDetails.getReason(), invoiceDetails.getFastRxAudit().getCreatedBy() });
		} catch (Exception e) {
			logger.error("error while inserting in invoiceDetails", e);
		}

	}

	@Override
	public String getTransactionType(long id) throws SQLException {
		String transactionType = jdbcTemplate.queryForObject(
				FastRxQueryConstants.GET_transaction_type_BY_CORPORATION_ID, new Object[] { id }, String.class);
		return transactionType;

	}

	@Override
	public BillingDetails getBillingDetailByCorporationId(long id) throws DataAccessException {
		BillingDetails lastBillingDetails = (BillingDetails) jdbcTemplate.queryForObject(
				FastRxQueryConstants.GET_BILLING_BY_CORPORATION_ID, new Object[] { id }, new BillingDetailsRowMapper());
		return lastBillingDetails;
	}
}
