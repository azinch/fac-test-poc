package com.vip.ensemble.napi.test;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestReporter;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.parallel.ResourceAccessMode;
import org.junit.jupiter.api.parallel.ResourceLock;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import static org.mockito.Mockito.*;


@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness=Strictness.WARN)
class ExternalSystemApiClientUTest {
	
	private ExternalSystemApiClient client = null;
	private ExternalSystemApiClient spyClient = null;

	@BeforeAll
	void setUpBeforeClass(@Mock ExternalSystemApi extObj) throws Exception {
		client = new ExternalSystemApiClient(extObj);
		// spyClient = spy(client);
	}
	
	@BeforeEach
	void setUp(TestInfo testInfo) throws Exception {
		if(testInfo.getDisplayName().contains("setExternalDataUTest")) {
			spyClient = spy(client);
		} 
	}
	
	@Test
	@DisplayName("getExternalDataUTest...")
	void getExternalDataUTest() {
		when(client.getExtObj().getData(any(Integer.class))).thenReturn("getExternalData");
		
		assertAll("whatever input, output of the getExternalData should always be the same",
				() -> assertEquals("getExternalData", client.getExternalData(1)),
				() -> assertEquals("getExternalData", client.getExternalData(2)));
	}
	
	@Test
	@DisplayName("setExternalDataUTest...")
	void setExternalDataUTest() {
		doNothing().when(spyClient).validateData(isA(String.class));
		boolean result = spyClient.setExternalData(1, "setExternalData");
		
		verify(spyClient, times(1)).validateData(isA(String.class));
		assertTrue(result);
	}
	
	@Test
	@DisplayName("setExternalDataWithExceptionUTest...")
	void setExternalDataWithExceptionUTest() {
		when(client.getExtObj().setData(any(Integer.class), any(String.class))).thenThrow(new ExternalSystemException("FAILURE"));
		ExternalSystemException thrown = 
			assertThrows(ExternalSystemException.class,
		                 () -> client.setExternalData(1, "setExternalData"),
		                 "Expected setExternalData to throw exception, but it have not!");

		    assertTrue(thrown.getMessage().contains("FAILURE"));
	}

	
}
