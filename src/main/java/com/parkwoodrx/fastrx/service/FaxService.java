package com.parkwoodrx.fastrx.service;

import java.io.File;
import java.io.FileInputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import javax.xml.ws.Holder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.parkwoodrx.fastrx.constants.FastRxErrorCodes;
import com.parkwoodrx.fastrx.exception.FastRxException;

import FaxWS.FaxJobFile;
import FaxWS.FaxJobFiles;
import FaxWS.FaxJobId;
import FaxWS.FaxJobIds;
import FaxWS.FaxJobRecipient;
import FaxWS.FaxJobRecipients;
import FaxWS.FaxStatusList;
import FaxWS.FaxWS;
import FaxWS.FaxWSPortType;
import FaxWS.RealTimeFaxStatusList;
import FaxWS.WSError;

@Service
@PropertySource("file:email.properties")

public class FaxService {
	private static final Logger logger = LoggerFactory.getLogger(FaxService.class);

	@Value("${concord.filetypeid}")
	public int fileTypeId;

	@Value("${concord.username}")
	private String concordUsername;

	@Value("${concord.password}")
	private String concordPassword;

	@Value("${efax.prescriptions.content}")
	private String prescriptionContent;

	@Value("${concord.fax.suffix}")
	private String faxSuffix;

	@Value("${concord.fax.prefix}")
	private String faxPrefix;

	@Async
	public CompletableFuture<Map<String, String>> sendFax(String faxNumber, String fileName) throws Exception {
		FileInputStream fin = null;
		Map<String, String> faxdDetails = new HashMap<String, String>() {
			private static final long serialVersionUID = 1L;

			{
				put("FaxJobId", null);
				put("Status", null);
			}
		};
		try {
			// Read fax file to buffer
			File file = new File(fileName);

			fin = new FileInputStream(file);
			/*
			 * Create byte array large enough to hold the content of the file.
			 * Use File.length to determine size of the file in bytes.
			 */

			byte fileContent[] = new byte[(int) file.length()];

			/*
			 * To read content of the file in byte array, use int read(byte[]
			 * byteArray) method of java FileInputStream class.
			 *
			 */
			fin.read(fileContent);

			FaxWS faxws = new FaxWS();
			FaxWSPortType faxwsClient = faxws.getFaxWS();

			FaxJobFile faxJobFile = new FaxJobFile();
			// setting FileTypeId, id 111 is for .html file format
			faxJobFile.setFileTypeId(fileTypeId);
			// Adding file content
			faxJobFile.setFileData(fileContent);
			FaxJobFiles faxJobFiles = new FaxJobFiles();
			faxJobFiles.getItem().add(faxJobFile);

			// Adding recipients
			FaxJobRecipients recipients = new FaxJobRecipients();

			FaxJobRecipient recipent = new FaxJobRecipient();
			recipent.setRecipFaxNumber(faxPrefix + faxNumber + faxSuffix);
			recipients.getItem().add(recipent);

			Holder<Long> ttfp = new Holder<>();
			Holder<WSError> wsError = new Holder<>();
			Holder<FaxJobIds> faxJobIds = new Holder<>();
			Holder<FaxStatusList> faxStatusList = new Holder<>();
			Holder<RealTimeFaxStatusList> realTimeFaxStatusList = new Holder<>();

			/*
			 * Use this method for sending any format attachment only, Just set
			 * proper FileTypeId before changing the file type
			 */
			logger.info("Sending fax To : {}", recipent.getRecipFaxNumber());
			faxwsClient.sendFax(concordUsername, concordPassword, recipients, faxJobFiles, faxJobIds, ttfp, wsError);
			if (null != faxJobIds.value) {
				// Retrieving fax id's returned by sendFax
				FaxJobIds currentFaxIds = faxJobIds.value;
				// Fetching fax status
				faxwsClient.getRealTimeFaxStatus(concordUsername, concordPassword, currentFaxIds, realTimeFaxStatusList,
						wsError);

				for (int i = 0; i < realTimeFaxStatusList.value.getItem().size(); i++) {
					faxdDetails.put("FaxJobId", realTimeFaxStatusList.value.getItem().get(i).getFaxJobId());
					faxdDetails.put("Status", realTimeFaxStatusList.value.getItem().get(i).getStatusDescription());
					logger.info("FaxJobId : {}", realTimeFaxStatusList.value.getItem().get(i).getFaxJobId());
					logger.info("Fax Status : {}", realTimeFaxStatusList.value.getItem().get(i).getStatusDescription());
					logger.info("Reciepent State : {}",
							realTimeFaxStatusList.value.getItem().get(i).getRecipientState());
					logger.info("Fax Job Status Id : {}",
							realTimeFaxStatusList.value.getItem().get(i).getFaxJobStatusId());
					logger.info("Fax Error : {}", wsError.value.getErrorString());
				}

			} else {
				throw new FastRxException(FastRxErrorCodes.INVALID_CODE, wsError.value.getErrorString());
			}

		} catch (Exception e) {
			logger.info("Exception in sending fax:: {}", e);
			throw e;

		} finally {
			if (null != fin) {
				fin.close();
			}

		}
		return CompletableFuture.completedFuture(faxdDetails);

	}

	public Map<String, Integer> getFaxStatusByFaxJobId(List<String> myfaxJobIds) {
		Map<String, Integer> faxdDetails = new HashMap<>();
		FaxWS faxws = new FaxWS();
		FaxWSPortType faxwsClient = faxws.getFaxWS();
		FaxJobIds faxJobIds = new FaxJobIds();
		Holder<WSError> wsError = new Holder<>();
		Holder<RealTimeFaxStatusList> realTimeFaxStatusList = new Holder<>();
		try {
			for (String myfaxJobId : myfaxJobIds) {
				FaxJobId faxJobId = new FaxJobId();
				faxJobId.setJobId(myfaxJobId);
				faxJobIds.getItem().add(faxJobId);
			}

			logger.info("###################   getRealTimeFaxStatus      ##########################");
			faxwsClient.getRealTimeFaxStatus(concordUsername, concordPassword, faxJobIds, realTimeFaxStatusList,
					wsError);

			/* Setting fax job status againt its fax job IDs */
			for (int i = 0; i < realTimeFaxStatusList.value.getItem().size(); i++) {
				faxdDetails.put(realTimeFaxStatusList.value.getItem().get(i).getFaxJobId(),
						realTimeFaxStatusList.value.getItem().get(i).getFaxJobStatusId());
				logger.info("FaxJobId : {}", realTimeFaxStatusList.value.getItem().get(i).getFaxJobId());
				logger.info("Fax Status : {}", realTimeFaxStatusList.value.getItem().get(i).getStatusDescription());
				logger.info("Fax Job Status Id : {}", realTimeFaxStatusList.value.getItem().get(i).getFaxJobStatusId());
				logger.info("Reciepent State : {}", realTimeFaxStatusList.value.getItem().get(i).getRecipientState());
				logger.info("Fax Error : {}", wsError.value.getErrorString());
				logger.info("Fax Error : {}", wsError.value.getErrorString());
			}

		} catch (Exception e) {
			logger.info("Exception in sending fax:: {}", e);
			throw e;
		}

		return faxdDetails;
	}

}
