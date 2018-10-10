package com.parkwoodrx.fastrx.scheduling;

import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.parkwoodrx.fastrx.constants.FastRxErrorCodes;
import com.parkwoodrx.fastrx.exception.FastRxException;
import com.parkwoodrx.fastrx.integration.CustomerPaymentService;
import com.parkwoodrx.fastrx.model.BillingDetails;
import com.parkwoodrx.fastrx.model.BillingReport;
import com.parkwoodrx.fastrx.model.CreditcardDetails;
import com.parkwoodrx.fastrx.model.FastRxAudit;
import com.parkwoodrx.fastrx.model.InvoiceDetails;
import com.parkwoodrx.fastrx.model.PharmacyCorporation;
import com.parkwoodrx.fastrx.repository.PharmacyCorporationDao;
import com.parkwoodrx.fastrx.repository.TransactionDao;
import com.parkwoodrx.fastrx.service.EmailService;

public class BillingGeneratorThread implements Runnable {

	private static final Logger logger = LoggerFactory.getLogger(BillingGeneratorThread.class);

	private TransactionDao transactionDao = null;

	private PharmacyCorporation pharmacyCorporation;

	private PharmacyCorporationDao pharmacyDao;

	private CustomerPaymentService paymentService;

	private EmailService emailService = null;

	long corpId = 0;

	BillingGeneratorThread(PharmacyCorporation pharmacyCorporation, TransactionDao transactionDao,
			EmailService emailService, PharmacyCorporationDao pharmacyDao, CustomerPaymentService paymentService) {
		this.pharmacyDao = pharmacyDao;
		this.corpId = pharmacyCorporation.getId();
		this.pharmacyCorporation = pharmacyCorporation;
		this.transactionDao = transactionDao;
		this.emailService = emailService;
		this.paymentService = paymentService;

	}

	@Override
	public void run() {
		logger.info("BillingGeneratorThread run(): START corpId - " + corpId);
		try {
			// get txn type
			BillingDetails billingDetails = transactionDao.getBillingdetailsForCorp(corpId);
			ArrayList<BillingReport> billingReportForCorp = new ArrayList();
			if (billingDetails.getTransactionType().contains("perdayTransaction")) {
				billingReportForCorp = (ArrayList<BillingReport>) transactionDao.getBillingReportByCorpId(corpId);
			} else {
				billingReportForCorp = (ArrayList<BillingReport>) transactionDao
						.getMonthlyBillingReportByCorpId(corpId);
			}
			BillingReport billingReport = buildBillingReport(billingReportForCorp);
			if (billingReport.getTotalRequestingCharge() > 0) {

				boolean isReportSent = generateBillingReportandSendMail(billingReport);

				logger.info("Transation report send for corpId - " + corpId + " " + isReportSent);

				if ("Credit Card".equalsIgnoreCase(pharmacyCorporation.getPaymentType())) {

					boolean isPaymentDone = getPaymnentDeatilsAndDebitPayment(billingReport);

					logger.info("Payment Done for corpId - " + corpId + " " + isPaymentDone);

					boolean isPaymentStatusReportSent = SendPaymentStatusReport(isPaymentDone, billingReport);

					logger.info("Payment Done report send for corpId - " + corpId + " " + isPaymentStatusReportSent);
				}
				// update txn status on 1st of month
				updateTxnStatus();

			} else {
				logger.info("No transations found for corpId - " + corpId);
			}
			logger.info("BillingGeneratorThread run(): END corpId - " + corpId);
		} catch (FastRxException e) {
			logger.error("Error::BillingGeneratorThread", e);
		} catch (Exception e) {
			logger.error("Error while executing thread " + Thread.currentThread().getName() + " for corpId = " + corpId
					+ " ", e);
		}

	}

	private BillingReport buildBillingReport(ArrayList<BillingReport> billingReportForCorp) {
		logger.info("BillingGeneratorThread buildBillingReport(): START corpId - " + corpId);
		BillingReport billingReport = new BillingReport();
		try {
			billingReport.setTotalRequestingCharge(billingReportForCorp.get(0).getTotalRequestingCharge());
			billingReport.setTotalRequestingTax(billingReportForCorp.get(0).getTotalRequestingTax());
			logger.info("BillingGeneratorThread buildBillingReport(): END corpId - " + corpId);
		} catch (Exception e) {
			logger.error("ERROR while building report object", e);
			throw new FastRxException(FastRxErrorCodes.BUILDING_BILLING_CODE, FastRxErrorCodes.BUILDING_BILLING_MSG);
		}
		return billingReport;
	}

	private void updateTxnStatus() {
		logger.info("Updating Txn status ");
		transactionDao.updateBillingdetailsForCorp();
		logger.info("Updating Txn status Ends ");
	}

	private boolean generateBillingReportandSendMail(BillingReport billingReport) {
		logger.info("BillingGeneratorThread generateBillingReportandSendMail(): START corpId - " + corpId);
		boolean isReportSent = false;
		try {

			double totalChargeAmount = calculateTotalChargeAmount(billingReport);

			String billlingReportMsg = "<b>Please find below your billing details for the month of <b>"
					+ calculateBillingPeriod();
			billlingReportMsg = billlingReportMsg
					+ "<p> </p><table width='100%' border='1' bordercolor:'#cccccc' cellspacing='0' cellpadding='5'>"
					+ "<tr><td cellpadding='2'>Total Requesting charge</td>" + "<td cellpadding='2'>$"
					+ billingReport.getTotalRequestingCharge() + "</td>" + "</tr>" + "<tr>"
					+ "<td>Total Requesting tax</td>" + "<td>$" + billingReport.getTotalRequestingTax() + "</td>"
					+ "</tr>" + "<tr>" + "<td>Total Amount</td><" + "td>$" + totalChargeAmount + "</td>"
					+ "</tr></table>";

			String subject = "Invoice detail for " + calculateBillingPeriod() + "-"
					+ pharmacyCorporation.getCorporationName();

			emailService.sendEmailForBiliingReport(billlingReportMsg, pharmacyCorporation.getEmailAddrForInvoice(),
					subject);

			// emailService.sendEmailForBiliingReport(billlingReportMsg,"abc@gmail.com",subject);//
			// //for testing only
			isReportSent = true;
			logger.info("BillingGeneratorThread generateBillingReportandSendMail(): END corpId - " + corpId);
		} catch (FastRxException e) {
			isReportSent = false;
			throw e;
		} catch (Exception e) {
			logger.error("ERROR while generating Billing Report and SendMail", e);
			isReportSent = false;
			throw new FastRxException(FastRxErrorCodes.GENERATING_BILLING_REPORT_EMAIL_CODE,
					FastRxErrorCodes.GENERATING_BILLING_REPORT_EMAIL_MSG);
		}
		return isReportSent;
	}

	private boolean getPaymnentDeatilsAndDebitPayment(BillingReport billingReport) {
		boolean isPaymentDone = false;
		CreditcardDetails creditcardDetails = null;
		try {
			try {
				creditcardDetails = pharmacyDao.getPaymentProfileIdByCorporationId(corpId);
			} catch (Exception e) {
				logger.error("ERROR while fetching credit card details::", e);
				throw new FastRxException(FastRxErrorCodes.FETCHING_PAYMENT_DETAILS_FOR_DEBIT_CODE,
						FastRxErrorCodes.FETCHING_PAYMENT_DETAILS_FOR_DEBIT_MSG);
			}
			if (creditcardDetails != null) {
				InvoiceDetails invoiceDetails = new InvoiceDetails();
				FastRxAudit fastRxAudit = new FastRxAudit();
				fastRxAudit.setCreatedBy("admin@fastrxtransfer.com");
				invoiceDetails.setFastRxAudit(fastRxAudit);
				invoiceDetails.setCorporationId(corpId);
				invoiceDetails.setBillingPeriod(calculateBillingPeriod());
				isPaymentDone = paymentService.ChargeCustomerProfile(creditcardDetails.getCustomerProfileId(),
						creditcardDetails.getCustomerPaymentAcctId(), calculateTotalChargeAmount(billingReport),
						invoiceDetails);
			} else {
				throw new FastRxException(FastRxErrorCodes.DEBITING_PAYMENT_FROM_CARD_CODE,
						FastRxErrorCodes.DEBITING_PAYMENT_FROM_CARD_MSG);
			}
		} catch (FastRxException e) {
			throw e;
		} catch (Exception e) {
			logger.error("ERROR while debiting payment from credit card", e);
			throw new FastRxException(FastRxErrorCodes.DEBITING_PAYMENT_FROM_CARD_CODE,
					FastRxErrorCodes.DEBITING_PAYMENT_FROM_CARD_MSG);
		}
		return isPaymentDone;
	}

	private boolean SendPaymentStatusReport(boolean isPaymentDone, BillingReport billingReport) {
		boolean isPaymentStatusReportSent = false;
		String CardPaymnetMsg = "";

		logger.info("BillingGeneratorThread SendPaymentStatusReport(): START corpId - " + corpId);
		try {

			double totalChargeAmount = calculateTotalChargeAmount(billingReport);

			if (isPaymentDone) {
				CardPaymnetMsg = "Credit Card Payment of $" + totalChargeAmount + " is done for "
						+ pharmacyCorporation.getCorporationName();
			} else {
				CardPaymnetMsg = "Credit Card Payment of $" + totalChargeAmount + " is fail for "
						+ pharmacyCorporation.getCorporationName();
			}
			String subject = "Credit Card Payment for " + calculateBillingPeriod() + "-"
					+ pharmacyCorporation.getCorporationName();
			emailService.sendEmailForBiliingReport(CardPaymnetMsg, pharmacyCorporation.getEmailAddrForInvoice(),
					subject);
			// emailService.sendEmailForBiliingReport(CardPaymnetMsg,
			// "abc@gmail.com", subject); // for testing
			isPaymentStatusReportSent = true;
			logger.info("BillingGeneratorThread SendPaymentStatusReport(): END corpId - " + corpId);
		} catch (FastRxException e) {
			throw e;
		} catch (Exception e) {
			logger.error("ERROR while credit card paymnet status SendMail", e);
			throw new FastRxException(FastRxErrorCodes.CREDIT_CARD_EMAIL_CODE, FastRxErrorCodes.CREDIT_CARD_EMAIL_MSG);
		}
		return isPaymentStatusReportSent;
	}

	private double calculateTotalChargeAmount(BillingReport billingReport) {
		logger.info("BillingGeneratorThread calculateTotalChargeAmount(): START corpId - " + corpId);
		double totalCharge = 0;
		try {

			totalCharge = billingReport.getTotalRequestingCharge() + billingReport.getTotalRequestingTax();
			totalCharge = Math.round(totalCharge * 100D) / 100D;
			logger.info("BillingGeneratorThread calculateTotalChargeAmount(): END corpId - " + corpId);
		} catch (Exception e) {
			logger.error("ERROR while calculating Total Charge Amount", e);
			throw new FastRxException(FastRxErrorCodes.CALCULATING_TOTAL_CODE, FastRxErrorCodes.CALCULATING_TOTAL_MSG);
		}
		return totalCharge;
	}

	private String calculateBillingPeriod() {
		logger.info("BillingGeneratorThread calculateBillingPeriod(): START corpId - " + corpId);
		String biliingPeriod = "";
		try {
			YearMonth thisMonth = YearMonth.now();
			YearMonth lastMonth = thisMonth.minusMonths(1);
			DateTimeFormatter monthYearFormatter = DateTimeFormatter.ofPattern("MMMM yyyy");
			biliingPeriod = lastMonth.format(monthYearFormatter);
		} catch (Exception e) {
			logger.error("ERROR while calculating billing period", e);
			throw new FastRxException(FastRxErrorCodes.CALCULATING_PERIOD_CODE,
					FastRxErrorCodes.CALCULATING_PERIOD_MSG);
		}
		return biliingPeriod;
	}

}
