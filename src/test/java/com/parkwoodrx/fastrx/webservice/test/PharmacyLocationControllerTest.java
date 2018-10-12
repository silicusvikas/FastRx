package com.parkwoodrx.fastrx.webservice.test;

/*import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;

import java.util.Arrays;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.web.WebAppConfiguration;

import com.parkwoodrx.fastrx.FastrxApplication;
import com.parkwoodrx.fastrx.model.PharmacyLocation;
import com.parkwoodrx.fastrx.model.PharmacyLocationWithCorporation;
import com.parkwoodrx.fastrx.service.PharmacyLocationService;
import com.parkwoodrx.fastrx.webservice.PharmacyLocationController;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest(classes = FastrxApplication.class)
@WebAppConfiguration*/
public class PharmacyLocationControllerTest {

	/*@InjectMocks
	private PharmacyLocationController controllerUT = new PharmacyLocationController();

	@Mock
	private PharmacyLocationService locationService;

	@Mock
	private PharmacyLocation location1;

	@Mock
	private PharmacyLocation location2;

	@Mock
	private PharmacyLocationWithCorporation loc1;
	@Mock
	private PharmacyLocationWithCorporation loc2;

	@Test
	public void testMockCreation() {
		assertNotNull(location1);
		assertNotNull(location2);
	}

	@Test
	public void registerPharmacyLocation() {
		locationService.registerPharmacyLocation(location1);
	}

	@Test
	public void getLocationListTest() throws Exception {
		when(locationService.getPharmacyLocationList()).thenReturn(Arrays.asList(loc1, loc2));
		assertEquals(2, locationService.getPharmacyLocationList().size());

	}

	@Test
	public void getPharmacyLocationByCorpId() {
		long corpId = 1;
		when(locationService.getAllPharmacyLocationForCorporationId(corpId))
				.thenReturn(Arrays.asList(location1, location2));
		assertEquals(2, locationService.getAllPharmacyLocationForCorporationId(corpId).size());
	}

	@Test
	public void getPharmacyLocationByPin() {
		String locationPin = "1";
		when(locationService.getPharmacyLocationByLocationPin(locationPin)).thenReturn(location1);
		assertEquals(location1, locationService.getPharmacyLocationByLocationPin(locationPin));
		assertNotEquals(location2, locationService.getPharmacyLocationByLocationPin(locationPin));
	}*/

}
