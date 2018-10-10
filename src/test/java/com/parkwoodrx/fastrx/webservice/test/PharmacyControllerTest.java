package com.parkwoodrx.fastrx.webservice.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;

import java.util.Arrays;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.web.WebAppConfiguration;

import com.parkwoodrx.fastrx.FastrxApplication;
import com.parkwoodrx.fastrx.exception.FastRxException;
import com.parkwoodrx.fastrx.model.PaymentType;
import com.parkwoodrx.fastrx.model.PharmacyCorporation;
import com.parkwoodrx.fastrx.model.PharmacyCorporationDetails;
import com.parkwoodrx.fastrx.service.PharmacyCorporationService;
import com.parkwoodrx.fastrx.webservice.PharmacyCorporationController;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest(classes = FastrxApplication.class)
@WebAppConfiguration
public class PharmacyControllerTest {

	@InjectMocks
	private PharmacyCorporationController controllerUT = new PharmacyCorporationController();

	@Mock
	private PharmacyCorporationService ps;

	@Mock
	private PharmacyCorporationDetails corporationDetails;

	@Mock
	private PaymentType type1;

	@Mock
	private PharmacyCorporation corporation;

	@Test
	public void testMockCreation() {
		assertNotNull(corporationDetails);
		assertNotNull(type1);
		assertNotNull(corporation);
	}

	@Test
	public void registerPharmacyCorporation() throws FastRxException {
		long id = 1;
		when(ps.registerPharmacyCorporation(corporationDetails)).thenReturn(id);
		assertNotNull(id);
		assertEquals(id, ps.registerPharmacyCorporation(corporationDetails));
	}

	@Test
	public void updatePharmacyCorporation() {
		Mockito.doThrow(new FastRxException(1, "Error")).doNothing().when(ps).updatePharmacyCorporation(corporation);
	}

	@Test
	public void getPharmacyCorporationsByNameOrPhoneTest() {
		when(ps.getPharmacyCorporationsByNameOrPhone(null, "a")).thenReturn(Arrays.asList(corporation, corporation));
		assertEquals(2, ps.getPharmacyCorporationsByNameOrPhone(null, "a").size());
	}

	@Test
	public void getpaymentTypeListTest() {
		when(ps.getPaymentTypeList()).thenReturn(Arrays.asList(type1, type1, type1));
		assertEquals(3, ps.getPaymentTypeList().size());
	}

}
