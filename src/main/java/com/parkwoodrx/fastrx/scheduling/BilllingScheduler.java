package com.parkwoodrx.fastrx.scheduling;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;

import com.parkwoodrx.fastrx.integration.CustomerPaymentService;
import com.parkwoodrx.fastrx.model.PharmacyCorporation;
import com.parkwoodrx.fastrx.repository.PharmacyCorporationDao;
import com.parkwoodrx.fastrx.repository.TransactionDao;
import com.parkwoodrx.fastrx.service.EmailService;

public class BilllingScheduler {
	
	private static final Logger logger = LoggerFactory.getLogger(BilllingScheduler.class);
	@Autowired
	private PharmacyCorporationDao pharmacyDao;
	
	@Autowired
	TransactionDao transactionDao;
	
	@Autowired
	private EmailService emailService;
	
	@Autowired
	private CustomerPaymentService paymentService;
	
	ExecutorService executor = Executors.newFixedThreadPool(10);
	
   @Scheduled(cron="0 0 0 1 * ?")
	//@Scheduled(cron="*/10 * * * * *")//for testing
	 public void generateBillingDetails() {
		 ArrayList<PharmacyCorporation> corpList = null;
		 try{
			 corpList = (ArrayList<PharmacyCorporation>) pharmacyDao.getAllPharmacyCorporations();
		 } catch (Exception e) {
			 logger.info("Error while fetching All Pharmacy CorporationIds And Email Map",e);
		}
		 if(corpList != null && !corpList.isEmpty()){
			 for (PharmacyCorporation pharmacyCorporation : corpList){
				 logger.info("Generating Payment Details for CorpID"+pharmacyCorporation.getId());
				 try{
				 Runnable billingGenerator = new BillingGeneratorThread
						 (pharmacyCorporation,transactionDao,emailService,pharmacyDao,paymentService);
				 executor.execute(billingGenerator);
				 } catch (Exception e) {
					 logger.error("Error while billing process process for corpID - "+pharmacyCorporation.getId(),e);
				}
			 } 
		  }
	   }
	}
