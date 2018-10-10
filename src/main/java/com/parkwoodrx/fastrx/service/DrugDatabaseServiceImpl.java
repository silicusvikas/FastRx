package com.parkwoodrx.fastrx.service;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.openxml4j.exceptions.NotOfficeXmlFileException;
import org.apache.poi.openxml4j.exceptions.OLE2NotOfficeXmlFileException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import com.monitorjbl.xlsx.StreamingReader;
import com.parkwoodrx.fastrx.constants.FastRxErrorCodes;
import com.parkwoodrx.fastrx.exception.FastRxException;
import com.parkwoodrx.fastrx.model.Drugs;
import com.parkwoodrx.fastrx.repository.DrugDatabaseDao;

@Service
public class DrugDatabaseServiceImpl implements DrugDatabaseService {

	private static final Logger logger = LoggerFactory.getLogger(DrugDatabaseServiceImpl.class);

	@Autowired
	private DrugDatabaseDao drugDao;

	public void uploadDrugDatabase(File file, String loginUser) throws FastRxException {
		ArrayList<Drugs> drugList = null;
		try {
			logger.info("File reading started::" + java.sql.Timestamp.valueOf(java.time.LocalDateTime.now()));
			Workbook wb = StreamingReader.builder().rowCacheSize(100).bufferSize(4096).open(file);
			Sheet datatypeSheet = wb.getSheetAt(0);
			Iterator<Row> iterator = datatypeSheet.iterator();
			drugList = new ArrayList<Drugs>();
			while (iterator.hasNext()) {
				Row currentRow = iterator.next();
				Drugs drug = new Drugs();
				Iterator<Cell> cellIterator = currentRow.iterator();
				while (cellIterator.hasNext()) {
					Cell currentCell = cellIterator.next();
					if (currentCell.getRowIndex() == 0) {
						if (currentCell.getColumnIndex() == 0
								&& !currentCell.getStringCellValue().equalsIgnoreCase("PRODUCTID"))
							throw new FastRxException(FastRxErrorCodes.FILE_UPLOAD_READ_CODE,
									FastRxErrorCodes.INVALID_DATA_FORMAT);
						if (currentCell.getColumnIndex() == 3
								&& !currentCell.getStringCellValue().equalsIgnoreCase("PROPRIETARYNAME"))
							throw new FastRxException(FastRxErrorCodes.FILE_UPLOAD_READ_CODE,
									FastRxErrorCodes.INVALID_DATA_FORMAT);
						if (currentCell.getColumnIndex() == 4
								&& !currentCell.getStringCellValue().equalsIgnoreCase("PROPRIETARYNAMESUFFIX"))
							throw new FastRxException(FastRxErrorCodes.FILE_UPLOAD_READ_CODE,
									FastRxErrorCodes.INVALID_DATA_FORMAT);
						if (currentCell.getColumnIndex() == 5
								&& !currentCell.getStringCellValue().equalsIgnoreCase("NONPROPRIETARYNAME"))
							throw new FastRxException(FastRxErrorCodes.FILE_UPLOAD_READ_CODE,
									FastRxErrorCodes.INVALID_DATA_FORMAT);
						if (currentCell.getColumnIndex() == 6
								&& !currentCell.getStringCellValue().equalsIgnoreCase("DOSAGEFORMNAME"))
							throw new FastRxException(FastRxErrorCodes.FILE_UPLOAD_READ_CODE,
									FastRxErrorCodes.INVALID_DATA_FORMAT);
						if (currentCell.getColumnIndex() == 14
								&& !currentCell.getStringCellValue().equalsIgnoreCase("ACTIVE_NUMERATOR_STRENGTH"))
							throw new FastRxException(FastRxErrorCodes.FILE_UPLOAD_READ_CODE,
									FastRxErrorCodes.INVALID_DATA_FORMAT);
						if (currentCell.getColumnIndex() == 15
								&& !currentCell.getStringCellValue().equalsIgnoreCase("ACTIVE_INGRED_UNIT"))
							throw new FastRxException(FastRxErrorCodes.FILE_UPLOAD_READ_CODE,
									FastRxErrorCodes.INVALID_DATA_FORMAT);
						if (currentCell.getColumnIndex() == 17
								&& !currentCell.getStringCellValue().equalsIgnoreCase("DEASCHEDULE"))
							throw new FastRxException(FastRxErrorCodes.FILE_UPLOAD_READ_CODE,
									FastRxErrorCodes.INVALID_DATA_FORMAT);

					}
					if (currentCell.getRowIndex() != 0) {
						if (currentCell.getColumnIndex() == 0)
							drug.setProductId((currentCell.getStringCellValue()));
						if (currentCell.getColumnIndex() == 3)
							drug.setProprietaryName(currentCell.getStringCellValue());
						if (currentCell.getColumnIndex() == 4)
							drug.setProprietaryNameSuffix(currentCell.getStringCellValue());
						if (currentCell.getColumnIndex() == 5)
							drug.setNonProprietaryName(currentCell.getStringCellValue());
						if (currentCell.getColumnIndex() == 6)
							drug.setDosageFormName((currentCell.getStringCellValue()));
						if (currentCell.getColumnIndex() == 14)
							drug.setActiveNumeratorStrength(currentCell.getStringCellValue());
						if (currentCell.getColumnIndex() == 15)
							drug.setActiveIngredUnit(currentCell.getStringCellValue());
						if (currentCell.getColumnIndex() == 17)
							drug.setDeaSchedule(currentCell.getStringCellValue());
					}
				}
				if (null != drug.getProductId()) {
					drugList.add(drug);
				}

			}

			logger.info("File reading ends::" + java.sql.Timestamp.valueOf(java.time.LocalDateTime.now()));
			logger.info("No of drugs:: " + drugList.size());
			drugDao.uploadDrugDatabase(drugList);
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
			logger.error("SERVICE error while reading file :: " + e);
			throw new FastRxException(FastRxErrorCodes.FILE_UPLOAD_READ_CODE, FastRxErrorCodes.FILE_UPLOAD_READ_MSG);
		}
	}

	@Override
	public List<Drugs> searchDrug(String drugName) throws FastRxException {
		List<Drugs> drugList = null;
		try {
			drugList = drugDao.searchDrug(drugName);
			if (drugList.isEmpty()) {
				throw new FastRxException(FastRxErrorCodes.NOT_FOUND_CODE, FastRxErrorCodes.NO_DRUGS_FOUND_MSG);
			}
		} catch (DataAccessException e) {
			logger.error("SERVICE error while searching drug list :: " + e.getStackTrace());
			throw new FastRxException(FastRxErrorCodes.DATABASE_ERROR_CODE,
					FastRxErrorCodes.ERROR_SEARCHING_RECORDS_MSG);
		}
		return drugList;
	}

}
