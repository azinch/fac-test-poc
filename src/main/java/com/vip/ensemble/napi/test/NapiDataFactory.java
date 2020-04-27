package com.vip.ensemble.napi.test;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


public class NapiDataFactory {
	
	protected static Logger logger = LoggerFactory.getLogger(NapiDataFactory.class.getName()); 
	
	protected static OperationalData operData = null;
	protected static int mode = NapiFactory.MODE_JAVA;
	protected static String wsnAdr = "http://np-facdev:8098";
	protected static int userId = -777;
	protected static String userPass = "a982d24302584e16ce9";
	
	protected static String dbConnectUrl = "jdbc:oracle:thin:vmpapp117/vmpapp117@CSKA:1521:RUS741"; 
	protected static SimpleDateFormat ensDateFrmt = new SimpleDateFormat("yyyyMMdd");
	protected static SimpleDateFormat ensDateTimeFrmt = new SimpleDateFormat("yyyyMMddHHmmss");
	
	// Common Prepared statements for database (cached in ORA), expected to used frequently.. 
	protected static Connection dbComConnect = null;
	protected static PreparedStatement acctpComStmt = null;
	protected static PreparedStatement banComStmt = null;
	protected static PreparedStatement sbsComStmt = null;
	protected static PreparedStatement ctnComStmt = null;
	
	// HashMap for PARALLEL testing. Thread-safety guaranteed by using JUnit5 @ResourceLock in the tests.
	protected static Map<Integer, String> subnoCache = null;
	protected static int SIZE_OF_SUBNO_CACHE = 500;
	protected static int curPosInSubnoCache = 0;
	protected static boolean preactSubscriberNeeded = true;
	
	static {
		NapiFactory.setMode(mode);
		com.vip.ensemble.napi.test.Authenticator napiAuth = NapiFactory.getAuthenticator();
		napiAuth.setWSNADDR(wsnAdr);
		napiAuth.setUSER_ID(userId);
		napiAuth.setPASSWORD(userPass);
		napiAuth.callService();
		int napiSessionId = napiAuth.getSESSION_ID();
		com.vip.ensemble.napi.test.GetLogicalDate logicalDate = NapiFactory.getGetLogicalDate(napiSessionId);
		
		operData = new OperationalData(mode, napiSessionId, logicalDate.getDATE());
	}
	
	
	public static OperationalData getOperData() {
		return operData;
	}
	
	public static Connection getDbConnection() 
	{
		try {
	    	Class.forName("oracle.jdbc.driver.OracleDriver").newInstance();
	    	Connection dbCon = null;
	    	dbCon = DriverManager.getConnection(dbConnectUrl);
	    	if(dbCon==null) {
	    		throw new RuntimeException("[NapiDataFactory:getDbConnection] : FAILED TO CREATE DB CONNECTION");
	    	}
	    	dbCon.setAutoCommit(false);
	    	return dbCon;
	    } catch (Exception e) {
	    	throw new RuntimeException(e.getMessage());
	    }
	}
	
	protected static void setUpCommonStatements() {
		String newLine = System.getProperty("line.separator");
		String sqlText;
		try 
		{
			dbComConnect = getDbConnection();
			
			acctpComStmt = dbComConnect.prepareStatement("select * from ACCOUNT_TYPES where AT_ACCOUNT_TYPE=?");
			
			banComStmt = dbComConnect.prepareStatement("select * from BILLING_ACCOUNT where BAN=?");
			
			sqlText = "select sbs.SUBSCRIBER_ID, to_char(sbs.INIT_ACTIVATION_DATE, 'yyyymmdd') INIT_ACT_DATE,"
					+ newLine
					+ "ba.BAN, ba.ACCOUNT_TYPE, sa.SOC, sbs.PRODUCT_CODE, sit.ITEM_ID, sit.SERIAL_NO, cs.NGP, cs.NL"
					+ newLine
					+ "from SUBSCRIBER sbs, BILLING_ACCOUNT ba, SERVICE_AGREEMENT sa, SERIAL_ITEM_INV sit, CTN_STOCK cs"
					+ newLine
					+ "where sbs.SUBSCRIBER_NO = ? and sbs.SUB_STATUS = 'A' and ba.BAN = sbs.CUSTOMER_BAN"
					+ newLine
					+ "and sa.BAN = ba.BAN and sa.SUBSCRIBER_NO = sbs.SUBSCRIBER_NO and sa.SERVICE_TYPE = 'P'"
					+ newLine
					+ "and sa.SOC_SEQ_NO = ( select max(SOC_SEQ_NO) from SERVICE_AGREEMENT sa2"
					+ newLine
					+ "where sa2.BAN = sa.BAN and sa2.SUBSCRIBER_NO = sa.SUBSCRIBER_NO" 
					+ newLine
					+ "and sa2.EFFECTIVE_DATE <= to_date('" + operData.getLogicalDate() + "', 'yyyymmdd') )"
					+ newLine
					+ "and cs.CTN = sbs.SUBSCRIBER_NO and sit.SERIAL_NO = cs.PAIRED_RESOURCE and sit.SERIAL_TYPE = 'S'";
			
			//logger.info(sqlText);
			sbsComStmt = dbComConnect.prepareStatement(sqlText);
			
			sqlText = "select * from CTN_STOCK" + newLine
					+ "where NGP = ? and NL = ? and CTN_STATUS = 'AA'" + newLine
					+ "and PAIRED_RESOURCE_TP is null and PAIRED_RESOURCE is null" + newLine
					+ "and ROWNUM < 2";
			
			ctnComStmt = dbComConnect.prepareStatement(sqlText);
		} 
		catch (SQLException ex) 
		{
			ex.printStackTrace();
			try 
			{
				if(acctpComStmt!=null) {
					acctpComStmt.close();
					acctpComStmt = null;
				}
				if(banComStmt!=null) {
					banComStmt.close();
					banComStmt = null;
				}
				if(sbsComStmt!=null) {
					sbsComStmt.close();
					sbsComStmt = null;
				}
				if(dbComConnect!=null) {
					dbComConnect.close();
					dbComConnect = null;
				}
			} catch (Exception ex2) { 
				ex2.printStackTrace();
			}
			
			throw new RuntimeException("[NapiDataFactory:setUpCommonStatements] : FAILED TO SETUP DB COMMONS");
		}
	}
	
	protected static void tearDownCommonStatements() {
		try 
		{
			if(acctpComStmt!=null) {
				acctpComStmt.close();
				acctpComStmt = null;
			}
			if(banComStmt!=null) {
				banComStmt.close();
				banComStmt = null;
			}
			if(sbsComStmt!=null) {
				sbsComStmt.close();
				sbsComStmt = null;
			}
			if(dbComConnect!=null) {
				dbComConnect.close();
				dbComConnect = null;
			}
		} catch (SQLException ex2) { 
			ex2.printStackTrace();
		}
	}
	
	/*
	 *  Create Tentative Ban with some convenient default values.
	 *  Defaults for the input parameters:
	 *  activityDate = logical date
	 *  accountType = 13 (prepaid, individual) 
	 */
	
	public static BanData getBan(String activityDate, String accountType) 
	{
		ResultSet resultSet = null;
		
		String locActivityDate = activityDate!=null ? activityDate : operData.getLogicalDate();
		String locAccountType = accountType!=null ? accountType : "13";
		
		char accountCategory = 0;
		BanData banData = new BanData();
				
		com.vip.ensemble.napi.test.CreateTentativeBan banSrv = NapiFactory.getCreateTentativeBan(operData.getSessionId());
		
		banSrv.setACTV_DATE(locActivityDate);
		banSrv.setACCOUNT_TYPE(locAccountType);
		
		try 
		{
			if(dbComConnect == null) {
				setUpCommonStatements();
			}
			acctpComStmt.setString(1, locAccountType);
			resultSet = acctpComStmt.executeQuery();
			if (resultSet.next()) {
				accountCategory = resultSet.getString("AT_ACCOUNT_CATEGORY").charAt(0);
			} else {
				logger.info("[NapiDataFactory:getBan] : No AT_ACCOUNT_CATEGORY found for accountType="+locAccountType);
				resultSet.close();
				return null;
			}
			resultSet.close();
		} 
		catch (SQLException ex) 
		{
			ex.printStackTrace();
			try {
				tearDownCommonStatements();
				if(resultSet!=null) {
					resultSet.close();
				}
			} catch (SQLException ex2) {
				ex2.printStackTrace();
			}
			return null;
		} 
		
		if (accountCategory!='B') {
			banSrv.setBIRTH_DATE("20010101");
			banSrv.setCUST_GENDER((byte) 'M');
			banSrv.setCUST_NATIONALITY("RUS");
			banSrv.setGUR_TAX_NUMBER("1111111111111111");
		}
		
		banSrv.setLAST_BUSINESS_NAME("Ivan Petrov");
		banSrv.setFIRST_NAME("Ivan");
		banSrv.setNAME_FORMAT((byte) 'P');
		banSrv.setADR_PRIMARY_LN("Tverskaya");
		banSrv.setADR_CITY("Moscow");
		banSrv.setADR_HOUSE_NO("10");
		banSrv.setADR_COUNTRY("RUS");
		banSrv.setADR_STREET_TYPE(".");
		banSrv.setADR_PLACE_TYPE(".");
		banSrv.setSALES_ENTITY_CODE("206"); // dealer_group
		banSrv.setBRANCH_CODE("206284"); // dealer
		banSrv.setSALES_REP_CODE("П206284");
	    
		banSrv.callService();
		logger.info("[NapiDataFactory:getBan:CreateTentativeBan] : " + banSrv.getSTATE());
		if(banSrv.getSTATE().equals("00000")!=true) {
			return null;
		}
		
		try 
		{
			banComStmt.setInt(1, banSrv.getBAN());
			resultSet = banComStmt.executeQuery();
			
			if (resultSet.next()) {
				banData.setBan(banSrv.getBAN());
				banData.setAccountType(locAccountType);
				
				banData.setBillCycle(resultSet.getShort("BILL_CYCLE"));
				banData.setArBalance(resultSet.getDouble("AR_BALANCE"));
				banData.setBanStatus(resultSet.getString("BAN_STATUS").charAt(0));
				banData.setDefaultBen(resultSet.getShort("DEFAULT_BEN"));
				
				Date date = resultSet.getDate("START_SERVICE_DATE");
				banData.setStartServiceDate( (date != null) ? ensDateFrmt.format(date) : null );
				
				banData.setColDelinqStatus(resultSet.getString("COL_DELINQ_STATUS").charAt(0));
				banData.setHierarchyId(resultSet.getLong("HIERARCHY_ID"));
				banData.setObjectId(resultSet.getLong("OBJECT_ID"));
				banData.setMarketCode(resultSet.getString("MARKET_CODE"));
				banData.setPayerBan(resultSet.getInt("PAYER_BAN"));
				banData.setPayerBanInd(resultSet.getString("PAYER_BAN_IND").charAt(0));
			} else {
				logger.info("[NapiDataFactory:getBan] : No BILLING_ACCOUNT found for =" + banSrv.getBAN());
				resultSet.close();
				return null;
			}
			resultSet.close();
		} 
		catch (SQLException ex) 
		{
			ex.printStackTrace();
			try {
				if(resultSet!=null) {
					resultSet.close();
				}
				tearDownCommonStatements();
			} catch (SQLException ex2) {
				ex2.printStackTrace();
			}
			return null;
		}
		
		return banData;
	}
	
	/*
	 * Depending on preactSubscriberNeeded flag:
	 * - pre-activate Subscriber with PreAct NAPI from PRE FR initially created by NAPIBOXSTART job
	 * or
	 * - get Subscriber already pre-activated with CSMPARFUT job   
	 * 
	 * Use subnoCache for PARALLEL tests 
	 * (cache thread-safety is guaranteed by JUnit5 @ResourseLock in the tests)
	 */
	public static SubscriberData getPreActivatedSbs() 
	{
		ResultSet resultSet = null;
		String subno = null;
		SubscriberData sbsData = new SubscriberData();
		
		if(subnoCache==null) {
			loadSubnoCacheFromDb();
			curPosInSubnoCache = 0;
		}
		
		subno = subnoCache.get(curPosInSubnoCache);
		
		if (preactSubscriberNeeded) {
			// PreActBox
			com.vip.ensemble.napi.test.PreActBox preActSrv = NapiFactory.getPreActBox(operData.getSessionId());
			preActSrv.setSUBNO(subno);
			
			preActSrv.callService();
			logger.info("[NapiDataFactory:getPreActivedSbs:PreActBox] : " + preActSrv.getSTATE());
			if(preActSrv.getSTATE().equals("00000") == false) {
				return null;
			}
		}
		
		try 
		{
			sbsComStmt.setString(1, subno);
			resultSet = sbsComStmt.executeQuery();
			if (resultSet.next()) {
				sbsData.setSubscriberNo(subno);
				sbsData.setSubStatus('A');
				
				sbsData.setSubscriberId(resultSet.getInt("SUBSCRIBER_ID"));
				sbsData.setInitActivationDate(resultSet.getString("INIT_ACT_DATE"));
				sbsData.setBan(resultSet.getInt("BAN"));
				sbsData.setAccountType(resultSet.getString("ACCOUNT_TYPE"));
				sbsData.setPricePlan(resultSet.getString("SOC"));
				sbsData.setProductCode(resultSet.getString("PRODUCT_CODE"));
				sbsData.setItemId(resultSet.getString("ITEM_ID"));
				sbsData.setSimSerialNo(resultSet.getString("SERIAL_NO"));
				sbsData.setCtnNgp(resultSet.getString("NGP"));
				sbsData.setCtnNl(resultSet.getString("NL"));
			} else {
				logger.info("[NapiDataFactory:getPreActivedSbs] : No SUBSCRIBER found for subno="+subno);
				resultSet.close();
				return null;
			}
			resultSet.close();
		} 
		catch (SQLException ex) 
		{
			ex.printStackTrace();
			try {
				if(resultSet!=null) {
					resultSet.close();
				}
				tearDownCommonStatements();
			} catch (SQLException ex2) {
				ex2.printStackTrace();
			}
			return null;
		}
		
		curPosInSubnoCache++;
	    return sbsData;
	}
	
	/*
	 * Depending on preactSubscriberNeeded we get SUBNO from PRE FR created by NAPIBOX
	 * or SUBNO from SUBSCRIBER pre-activated by PARFUT. 
	 */
	protected static void loadSubnoCacheFromDb() {
		Connection dbCon = null;
		Statement sbsStmt = null;
		ResultSet resultSet = null;
		String newLine = System.getProperty("line.separator");
		String sqlText;
		
		try 
		{
			subnoCache = new HashMap<>();
					
			if(preactSubscriberNeeded) {
				sqlText = "select CFR_SUBSCRIBER_NO SUBNO from CSM_FUTURE_REQUEST" + newLine
						+ "where CFR_ACTIVITY_CD='PRE' and CFR_STATUS='R' and APPLICATION_ID='NAPIBC'" + newLine
						+ "and ROWNUM <= " + SIZE_OF_SUBNO_CACHE;
			} else {
				sqlText = "select SUBSCRIBER_NO SUBNO from SUBSCRIBER" + newLine
						+ "where SUB_STATUS='A' and SUB_STATUS_LAST_ACT='NAC' and APPLICATION_ID='PARFUT'" + newLine
						+ "and ROWNUM <= " + SIZE_OF_SUBNO_CACHE;
			}
			
			dbCon = getDbConnection();
			sbsStmt = dbCon.createStatement();
			resultSet = sbsStmt.executeQuery(sqlText);
			int key = 0;
			while(resultSet.next()) {
				subnoCache.put(key, resultSet.getString("SUBNO"));
				key++;
			}
			logger.info("[NapiDataFactory:loadSubnoCacheFromDb] : SUBNO CACHE SIZE LOADED: " + subnoCache.size());
			resultSet.close();
		}
		catch (SQLException ex) {
			ex.printStackTrace();
			try {
				if(resultSet!=null) {
					resultSet.close();
				} if(sbsStmt!=null) {
					sbsStmt.close();
				} if(dbCon!=null) {
					dbCon.close();
				}
			} catch (Exception ex2) { 
				ex2.printStackTrace();
			}
			throw new RuntimeException("[NapiDataFactory:loadSubnoCacheFromDb] : FAILED TO LOAD SUBNO CACHE");
		}
		
		if(subnoCache.isEmpty()) {
			throw new RuntimeException("[NapiDataFactory:loadSubnoCacheFromDb] : NO DATA LOADED IN SUBNO CACHE");
		}
	}
	
	/*
	 * Create Sale invoice for Pre-activated box
	 */
	public static OrderData getSaleOrder(SubscriberData preactSbs) {
		OrderData orderData = new OrderData();
		
		if(preactSbs==null) {
			logger.info("[NapiDataFactory:getSaleOrder] : input preactSbs is null");
			return null;
		}
				
		com.vip.ensemble.napi.test.SavePosOrderLine saveOrdSrv = NapiFactory.getSavePosOrderLine(operData.getSessionId());
		saveOrdSrv.setACTION((byte) 'I');
		saveOrdSrv.setORD_STORE_ID("000001");
		saveOrdSrv.setORD_ACTIVITY_TYPE((byte) 'O');
		saveOrdSrv.setBAN(preactSbs.getBan());
		saveOrdSrv.setPRICE_CODE("NEW_R");
		saveOrdSrv.setSUBSCRIBER_NO(preactSbs.getSubscriberNo());
		saveOrdSrv.setLINEIND((byte) 'I');
		saveOrdSrv.setLN_LINE_SEQ(1);
		saveOrdSrv.setLN_LINE_NUMBER(1);
		saveOrdSrv.setITEM_ID(preactSbs.getItemId());
		saveOrdSrv.setQTY_UNRELEASED(1);
		saveOrdSrv.setUNIT_PRICE(0.0);
		saveOrdSrv.setTAX_IND((byte) 'N');
		saveOrdSrv.setROWCOUNT(1);
		saveOrdSrv.setSLINE_MODE_ESN((byte) 'I', 0);
		saveOrdSrv.setESN(preactSbs.getSimSerialNo(), 0);
		
		saveOrdSrv.callService();
		logger.info("[NapiDataFactory:getSaleOrder:SavePosOrderLine] : " + saveOrdSrv.getSTATE());
		if(saveOrdSrv.getSTATE().equals("00000") == false) {
			return null;
		}
		orderData.setPosStoreId(saveOrdSrv.getSTORE_ID());
		orderData.setPosActivityType((char) saveOrdSrv.getACTIVITY_TYPE());
		orderData.setPosOrderOid(saveOrdSrv.getORDER_OID());
		orderData.setPosBarCodeNumber(saveOrdSrv.getBAR_CODE_NUMBER());
		
		com.vip.ensemble.napi.test.SettlePosOrder settleOrdSrv = NapiFactory.getSettlePosOrder(operData.getSessionId());
		settleOrdSrv.setORD_STORE_ID(saveOrdSrv.getSTORE_ID());
		settleOrdSrv.setORD_ACTIVITY_TYPE(saveOrdSrv.getACTIVITY_TYPE());
		settleOrdSrv.setORD_ORDER_OID(saveOrdSrv.getORDER_OID());
		
		settleOrdSrv.callService();
		logger.info("[NapiDataFactory:getSaleOrder:SettlePosOrder] : " + settleOrdSrv.getSTATE());
		if(saveOrdSrv.getSTATE().equals("00000") == false) {
			return null;
		}
		orderData.setRmsInvoiceStoreId(settleOrdSrv.getRMS_INVOICE_STORE_ID());
		orderData.setRmsActivityType((char) settleOrdSrv.getACTIVITY_TYPE());
		orderData.setRmsInvoiceId(settleOrdSrv.getRMS_INVOICE_ID());
		orderData.setArInvoiceNo(settleOrdSrv.getAR_INVOICE_NO());
		
		return orderData;
	}
	
	/*
	 * Register Subscriber sold
	 */
	public static SubscriberData getRegisteredSbs(SubscriberData soldSbs) {
		
		if(soldSbs==null) {
			logger.info("[NapiDataFactory:getRegisteredSbs] : input soldSbs is null");
			return null;
		}
		
		com.vip.ensemble.napi.test.EcareRegistration regSrv = NapiFactory.getEcareRegistration(operData.getSessionId());
		regSrv.setBAN(soldSbs.getBan());
		regSrv.setSUBSCRIBER_NO(soldSbs.getSubscriberNo());
		regSrv.setPREP_RSN_CODE("NCTN");
		
		regSrv.setROWCOUNT(3);

		regSrv.setLINK_TYPE((byte) 'T', 0);
		regSrv.setNAME_FORMAT((byte) 'D', 0);
		regSrv.setLAST_BUSINESS_NAME("OOO Иванов и Компани", 0);
		regSrv.setNAME_TITLE("ООО", 0);
		regSrv.setADR_STREET_TYPE("ул.", 0);
		regSrv.setADR_PLACE_TYPE("г.", 0);
		regSrv.setADR_CITY("Москва г", 0);
		regSrv.setADR_COUNTRY("RUS", 0);
		regSrv.setADR_HOUSE_NO("1", 0);
		regSrv.setADR_PRIMARY_LN("Первомайская", 0);
		
		regSrv.setLINK_TYPE((byte) 'A', 1);
		regSrv.setNAME_FORMAT((byte) 'P', 1);
		regSrv.setFIRST_NAME("Иван", 1);
		regSrv.setLAST_BUSINESS_NAME("Иванов", 1);
		regSrv.setNAME_TITLE("Г-н", 1);
		regSrv.setADR_STREET_TYPE("ул.", 1);
		regSrv.setADR_PLACE_TYPE("г.", 1);
		regSrv.setADR_CITY("Москва г", 1);
		regSrv.setADR_COUNTRY("RUS", 1);
		regSrv.setADR_HOUSE_NO("1", 1);
		regSrv.setADR_PRIMARY_LN("Первомайская", 1);
		
		regSrv.setLINK_TYPE((byte) 'C', 2);
		regSrv.setNAME_FORMAT((byte) 'P', 2);
		regSrv.setFIRST_NAME("Иван", 2);
		regSrv.setLAST_BUSINESS_NAME("Иванов", 2);
		regSrv.setNAME_TITLE("Г-н", 2);
		regSrv.setADR_STREET_TYPE("б-р", 2);
		regSrv.setADR_PLACE_TYPE("г.", 2);
		regSrv.setADR_CITY("Москва г", 2);
		regSrv.setADR_COUNTRY("RUS", 2);
		regSrv.setADR_HOUSE_NO("1", 2);
		regSrv.setADR_PRIMARY_LN("Первомайский б-р", 2);
		regSrv.setHOME_TELNO("9061471111", 2);
		
		regSrv.setBIRTH_DATE("20010101");
		regSrv.setCUST_GENDER((byte) 'M');
		regSrv.setCUST_DOC_TYPE((short) 1);
		regSrv.setCUST_DOC_ID("1234");
		regSrv.setCUST_DOC_NO("1501123476");
		regSrv.setCUST_DOC_DATE("20170101");
		
		regSrv.callService();
		logger.info("[NapiDataFactory:getRegisteredSbs:EcareRegistration] : " + regSrv.getSTATE());
		if(regSrv.getSTATE().equals("00000") == false) {
			return null;
		}
		
		// soldSbs.setSomeRegisterData...
		
		return soldSbs;
	}
	
	/*
	 * Retrieve from CTN_STOCK available/unpared CTN compatible with the one of input active Sbs 
	 */
	public static CtnData getCompatibleCtn(SubscriberData activeSbs) {
		ResultSet resultSet = null;
		CtnData ctnData = new CtnData();
		
		if(activeSbs==null) {
			logger.info("[NapiDataFactory:getCompatibleCtn] : input activeSbs is null");
			return null;
		}
		
		try 
		{
			if(dbComConnect == null) {
				setUpCommonStatements();
			}
			ctnComStmt.setString(1, activeSbs.getCtnNgp());
			ctnComStmt.setString(2, activeSbs.getCtnNl());
			
			resultSet = ctnComStmt.executeQuery();
			if (resultSet.next()) {
				ctnData.setCtn(resultSet.getString("CTN"));
				ctnData.setCtnStatus(resultSet.getString("CTN_STATUS"));
				ctnData.setNgp(activeSbs.getCtnNgp());
				ctnData.setNl(activeSbs.getCtnNl());
				ctnData.setPairedResourceTp(resultSet.getString("PAIRED_RESOURCE_TP").charAt(0));
				ctnData.setPairedResource(resultSet.getString("PAIRED_RESOURCE"));
				ctnData.setNgpType(resultSet.getString("NGP_TYPE").charAt(0));
				ctnData.setNiceClass(resultSet.getString("NICE_CLASS").charAt(0));
			} else {
				logger.info("[NapiDataFactory:getCompatibleCtn] : No available data in CTN_STOCK for NGP/NL ="
						 + activeSbs.getCtnNgp() + "/" +  activeSbs.getCtnNl());
				resultSet.close();
				return null;
			}
			resultSet.close();
		} 
		catch (SQLException ex)
		{
			ex.printStackTrace();
			try {
				tearDownCommonStatements();
				if(resultSet!=null) {
					resultSet.close();
				}
			} catch (SQLException ex2) {
				ex2.printStackTrace();
			}
			return null;
		} 
		
		return ctnData;
	}
	
}
