package com.vip.ensemble.napi.test;

import com.vip.ensemble.napi.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;

import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.TestReporter;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import org.junit.jupiter.api.parallel.ResourceAccessMode;
import org.junit.jupiter.api.parallel.ResourceLock;


@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(OrderAnnotation.class)
// @Execution(ExecutionMode.SAME_THREAD)
class ChangeCtnUTest {
	
	private static Logger logger = LoggerFactory.getLogger(ChangeCtnUTest.class.getName());
	
	final static String NAPI_FACTORY  = "com.vip.ensemble.napi.test.NapiFactory";
	final static String NAPI_DATA_FACTORY  = "com.vip.ensemble.napi.test.NapiDataFactory";
	
	private SubscriberData sbs;
	private CtnData ctn;
	private String ftrCode;
	private String reasonCode;

	@BeforeAll
	void setUpBeforeClass() throws Exception {
		sbs = null;
		ctn = null;
		ftrCode = null;
		reasonCode = null;
	}

	@AfterAll
	void tearDownAfterClass() throws Exception {
	}

	@BeforeEach
	@ResourceLock(value=NAPI_DATA_FACTORY, mode=ResourceAccessMode.READ_WRITE)
	void setUp(TestInfo testInfo, TestReporter testReporter) throws Exception {
		// Prepare common data for ChangeCtnUTest
		sbs = NapiDataFactory.getPreActivatedSbs();
		OrderData tmp = NapiDataFactory.getSaleOrder(sbs);
		sbs = NapiDataFactory.getRegisteredSbs(sbs);
		ctn = NapiDataFactory.getCompatibleCtn(sbs);
		// Set up specific conditions for each test..
		if(testInfo.getDisplayName().contains("ChangeCtn_WithSpecificFeature_0000_Test")) {
			if (sbs==null || ctn==null) {
				logger.info("ChangeCtn_WithSpecificFeature_0000_Test Data Preparation failed! Test will be skipped");
			}
			ftrCode = "CHCTNC";
			reasonCode = "OTHR";
		}
		if(testInfo.getDisplayName().contains("ChangeCtnTest_WithSpecificReason_ER188_Test")) {
			if (sbs==null || ctn==null) {
				logger.info("ChangeCtnTest_WithSpecificReason_ER188_Test Data Preparation failed! Test will be skipped");
			}
			ftrCode = "CHCTNF";
			reasonCode = "CCNT";
		}
	}

	@AfterEach
	void tearDown() throws Exception {
	}

	@Test
	//@Order(1)
	@DisplayName("ChangeCtn_WithSpecificFeature_0000_Test ...")
	//@Tag("ChangeCtn_WithSpecificFeature_Test")
	@ResourceLock(value=NAPI_FACTORY, mode=ResourceAccessMode.READ_WRITE)
	@ResourceLock(value=NAPI_DATA_FACTORY, mode=ResourceAccessMode.READ_WRITE)
	void ChangeCtn_WithSpecificFeature_0000_Test() {
		assumeTrue(sbs!=null && ctn!=null);
		
		ChangeCtn chgSrv = setUpService();		
		chgSrv.callService();
		assertEquals("00000", chgSrv.getSTATE());
	}
	
	@Test
	//@Order(2)
	@DisplayName("ChangeCtnTest_WithSpecificReason_ER188_Test ...")
	@ResourceLock(value=NAPI_FACTORY, mode=ResourceAccessMode.READ_WRITE)
	@ResourceLock(value=NAPI_DATA_FACTORY, mode=ResourceAccessMode.READ_WRITE)
	void ChangeCtnTest_WithSpecificReason_ER188_Test() {
		assumeTrue(sbs!=null && ctn!=null);
		
		ChangeCtn chgSrv = setUpService();		
		chgSrv.callService(); 
		assertEquals("ER188", chgSrv.getSTATE());
	}
	
	private ChangeCtn setUpService() {
		com.vip.ensemble.napi.test.ChangeCtn chgSrv 
		   = NapiFactory.getChangeCtn(NapiDataFactory.getOperData().getSessionId());
		
		chgSrv.setBAN(sbs.getBan());
		chgSrv.setFROM_CTN(sbs.getSubscriberNo());
		chgSrv.setTO_CTN(ctn.getCtn());
		chgSrv.setFEATURE_CODE(ftrCode);
		chgSrv.setACTV_DATE(NapiDataFactory.getOperData().getLogicalDate());
		
		return chgSrv;
	}

}
