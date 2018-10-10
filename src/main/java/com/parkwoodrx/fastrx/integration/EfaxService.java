package com.parkwoodrx.fastrx.integration;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.MessageFormat;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.parkwoodrx.fastrx.model.NonRegPharmacy;
import com.parkwoodrx.fastrx.model.PharmacyLocation;
import com.parkwoodrx.fastrx.model.Prescription;
import com.parkwoodrx.fastrx.security.PasswordGenerator;
import com.parkwoodrx.fastrx.service.EmailService;

import FaxWS.FaxWS;
import FaxWS.FaxWSPortType;

@Service
@PropertySource("file:efax.properties")
public class EfaxService {

	@Autowired
	private PasswordGenerator passwordGenerator;

	private static final Logger logger = LoggerFactory.getLogger(EmailService.class);

	@Value("${efax.fromAddress}")
	private String fromAddress;

	@Value("${efax.content}")
	private String content;

	@Value("${efax.prescriptions.content}")
	private String prescriptionContent;

	@Value("${efax.subject}")
	private String subject;

	@Value("${efax.country.code}")
	private String countryCode;

	@Value("${efax.username}")
	private String username;

	@Value("${efax.password}")
	private String password;

	@Value("${efax.host}")
	private String host;

	@Value("${efax.port}")
	private String port;

	@Value("${efax.protocol}")
	private String subprotocolject;

	@Value("${efax.auth}")
	private String auth;

	// send efax
	@Async
	public void sendFax(String faxnumber, Prescription prescription, NonRegPharmacy resPharmacyLocation,
			PharmacyLocation reqPharmacyLocation) throws Exception {
		logger.info("Inside EfaxService :: send Efax method");
		faxnumber = resPharmacyLocation.getPhy_efax();
		// faxnumber = "13233582254";// testing only
		String result = null;
		FaxWS faxws = new FaxWS();
		FaxWSPortType faxwsClient = faxws.getFaxWS();

		Properties props = new Properties();
		props.put("mail.smtp.host", host);
		props.put("mail.smtp.port", port);
		props.put("mail.smtp.auth", auth);
		props.put("mail.smtp.starttls.enable", "true");

		Session session = Session.getInstance(props, new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(username, password);
			}
		});
		MimeMessage message = new MimeMessage(session);
		message.setFrom(new InternetAddress(fromAddress));
		message.addRecipient(Message.RecipientType.TO, new InternetAddress(faxnumber + "@concordsend.com"));
		logger.info("Sending Efax to::" + faxnumber);
		message.setSubject("Efax::Pending Prescription");

		String reqadd = reqPharmacyLocation.getCity() + "," + reqPharmacyLocation.getState() + ","
				+ reqPharmacyLocation.getZipcode();
		String resadd = resPharmacyLocation.getPhy_city() + "," + resPharmacyLocation.getPhy_state() + ","
				+ resPharmacyLocation.getPhy_zip();
		String pharmacistName = prescription.getPharmacist();

		String fname = passwordGenerator.getDecodedString(prescription.getPatientFirstName());

		if (prescription.getRespTime() != null) {
			String time = LocalTime.parse(prescription.getRespTime()).format(DateTimeFormatter.ofPattern("h:mma"));
			result = MessageFormat.format(prescriptionContent, reqPharmacyLocation.getFaxNumber(), time,
					reqPharmacyLocation.getPharmacyName(), reqPharmacyLocation.getAddress(), reqadd,
					reqPharmacyLocation.getPhoneNumber(), reqPharmacyLocation.getFaxNumber(),
					reqPharmacyLocation.getDeaNumber(), resPharmacyLocation.getPharmacy_name(),
					resPharmacyLocation.getStore_address(), resadd, resPharmacyLocation.getPhy_phone(),
					resPharmacyLocation.getPhy_efax(), resPharmacyLocation.getDea(), pharmacistName,
					passwordGenerator.getDecodedString(prescription.getPatientFirstName()),
					passwordGenerator.getDecodedString(prescription.getPatientLastName()),
					passwordGenerator.getDecodedString(prescription.getPatientDob()), prescription.getPrescNumber(),
					prescription.getPrescDrugName(), prescription.getReqPharmacyComments(),
					passwordGenerator.getDecodedString(prescription.getPatientAddress()));

		} else {
			result = MessageFormat.format(prescriptionContent, reqPharmacyLocation.getFaxNumber(), "00:00",
					reqPharmacyLocation.getPharmacyName(), reqPharmacyLocation.getAddress(), reqadd,
					reqPharmacyLocation.getPhoneNumber(), reqPharmacyLocation.getFaxNumber(),
					reqPharmacyLocation.getDeaNumber(), resPharmacyLocation.getPharmacy_name(),
					resPharmacyLocation.getStore_address(), resadd, resPharmacyLocation.getPhy_phone(),
					resPharmacyLocation.getPhy_efax(), resPharmacyLocation.getDea(), pharmacistName,
					passwordGenerator.getDecodedString(prescription.getPatientFirstName()),
					passwordGenerator.getDecodedString(prescription.getPatientLastName()),
					passwordGenerator.getDecodedString(prescription.getPatientDob()), prescription.getPrescNumber(),
					prescription.getPrescDrugName(), prescription.getReqPharmacyComments(),
					passwordGenerator.getDecodedString(prescription.getPatientAddress()));
		}

		BodyPart messageBodyPart1 = new MimeBodyPart();
		messageBodyPart1.setText("Please find the attached file");
		MimeBodyPart messageBodyPart2 = new MimeBodyPart();
		createHTMLFile(result);
		String filename = "Test.html";
		DataSource source = new FileDataSource(filename);
		messageBodyPart2.setDataHandler(new DataHandler(source));
		messageBodyPart2.setFileName(filename);
		Multipart multipart = new MimeMultipart();
		multipart.addBodyPart(messageBodyPart1);
		multipart.addBodyPart(messageBodyPart2);
		message.setContent(multipart);
		Transport.send(message);

		logger.info("Efax sent sucessfully");
	}

	// create Dynamic HTML
	public static void createHTMLFile(String content) {
		BufferedWriter bw = null;
		FileWriter fw = null;
		try {
			fw = new FileWriter("Test.html");
			bw = new BufferedWriter(fw);
			bw.write(content);

		} catch (IOException e) {

			e.printStackTrace();

		} finally {

			try {

				if (bw != null)
					bw.close();

				if (fw != null)
					fw.close();

			} catch (IOException ex) {

				ex.printStackTrace();

			}

		}

	}

}
