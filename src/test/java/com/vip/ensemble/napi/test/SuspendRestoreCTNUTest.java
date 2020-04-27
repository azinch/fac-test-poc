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
class SuspendRestoreCTNUTest {
	
	private static Logger logger = LoggerFactory.getLogger(SuspendRestoreCTNUTest.class.getName());
	
	final static String NAPI_FACTORY  = "com.vip.ensemble.napi.test.NapiFactory";
	final static String NAPI_DATA_FACTORY  = "com.vip.ensemble.napi.test.NapiDataFactory";
	
	private OperationalData operData;
	private SubscriberData sbs = null;

	@BeforeAll
	@ResourceLock(value=NAPI_DATA_FACTORY, mode=ResourceAccessMode.READ_WRITE)
	void setUpBeforeClass() throws Exception {
		operData = NapiDataFactory.getOperData();
	}

	@AfterAll
	void tearDownAfterClass() throws Exception {
	}

	@BeforeEach
	@ResourceLock(value=NAPI_DATA_FACTORY, mode=ResourceAccessMode.READ_WRITE)
	void setUp(TestInfo testInfo, TestReporter testReporter) throws Exception {
		if(testInfo.getDisplayName().contains("SuspendCTN_WithSomeConditions_00000_Test")) {
			sbs = NapiDataFactory.getPreActivatedSbs();
			OrderData tmp = NapiDataFactory.getSaleOrder(sbs);
			sbs = NapiDataFactory.getRegisteredSbs(sbs);
		}
		if (sbs==null) {
			logger.info("SuspendRestoreCTNUTest Data Preparation failed! Tests will be skipped");
		}
	}

	@AfterEach
	void tearDown() throws Exception {
	}

	@Test
	@Order(1)
	@DisplayName("SuspendCTN_WithSomeConditions_00000_Test ...")
	//@Tag("SuspendCTN_WithSomeConditions_00000_Test")
	@ResourceLock(value=NAPI_FACTORY, mode=ResourceAccessMode.READ_WRITE)
	@ResourceLock(value=NAPI_DATA_FACTORY, mode=ResourceAccessMode.READ_WRITE)
	void SuspendCTN_WithSomeConditions_00000_Test() {
		com.vip.ensemble.napi.test.SuspendCTN suspCtnSrv = NapiFactory.getSuspendCTN(operData.getSessionId());
		assumeTrue(sbs!=null);
		
		suspCtnSrv.setBAN(sbs.getBan());
		suspCtnSrv.setSUBSCRIBER_NO(sbs.getSubscriberNo());
		suspCtnSrv.setACTV_DATE(operData.getLogicalDate());
		suspCtnSrv.setACTIVITY_RSN_CODE("NR");
		suspCtnSrv.callService();
		assertEquals("00000", suspCtnSrv.getSTATE());
	}
	
	@Test
	@Order(2)
	@DisplayName("RestoreCTN_WithSomeConditions_ER007_Test ...")
	@ResourceLock(value=NAPI_FACTORY, mode=ResourceAccessMode.READ_WRITE)
	@ResourceLock(value=NAPI_DATA_FACTORY, mode=ResourceAccessMode.READ_WRITE)
	void RestoreCTN_WithSomeConditions_ER007_Test() {
		assumeTrue(sbs!=null);
		
		com.vip.ensemble.napi.test.RestoreCTN rstrCtnSrv = NapiFactory.getRestoreCTN(operData.getSessionId());
		rstrCtnSrv.setBAN(sbs.getBan());
		rstrCtnSrv.setSUBSCRIBER_NO(sbs.getSubscriberNo());
		rstrCtnSrv.setACTV_DATE(operData.getLogicalDate());
		rstrCtnSrv.setACTIVITY_RSN_CODE("NR");
		rstrCtnSrv.callService(); 
		assertEquals("ER007", rstrCtnSrv.getSTATE());
		//assertNotEquals("ER007", rstrCtnSrv.getSTATE());
	}

}
