package com.vip.ensemble.napi.test;


public class BanData {
	private int ban;
	private String accountType;
	private short billCycle;
	private double arBalance;
	private char banStatus;
	private short defaultBen;
	private String startServiceDate;
	private char colDelinqStatus;
	private long hierarchyId;
	private long objectId;
	private String marketCode;
	private int payerBan;
	private char payerBanInd;
	
	// TODO: other data.
	
	public BanData() {
	}
	
	public BanData(int ban, String accountType, short billCycle, double arBalance, char banStatus, short defaultBen,
			String startServiceDate, char colDelinqStatus, long hierarchyId, long objectId, String marketCode,
			int payerBan, char payerBanInd) {
		this.ban = ban;
		this.accountType = accountType;
		this.billCycle = billCycle;
		this.arBalance = arBalance;
		this.banStatus = banStatus;
		this.defaultBen = defaultBen;
		this.startServiceDate = startServiceDate;
		this.colDelinqStatus = colDelinqStatus;
		this.hierarchyId = hierarchyId;
		this.objectId = objectId;
		this.marketCode = marketCode;
		this.payerBan = payerBan;
		this.payerBanInd = payerBanInd;
	}
	
	public BanData(BanData banData) {
		this.ban = banData.ban;
		this.accountType = banData.accountType;
		this.billCycle = banData.billCycle;
		this.arBalance = banData.arBalance;
		this.banStatus = banData.banStatus;
		this.defaultBen = banData.defaultBen;
		this.startServiceDate = banData.startServiceDate;
		this.colDelinqStatus = banData.colDelinqStatus;
		this.hierarchyId = banData.hierarchyId;
		this.objectId = banData.objectId;
		this.marketCode = banData.marketCode;
		this.payerBan = banData.payerBan;
		this.payerBanInd = banData.payerBanInd;
	}
	
	public int getBan() {
		return ban;
	}

	public void setBan(int ban) {
		this.ban = ban;
	}

	public String getAccountType() {
		return accountType;
	}

	public void setAccountType(String accountType) {
		this.accountType = accountType;
	}

	public short getBillCycle() {
		return billCycle;
	}

	public void setBillCycle(short billCycle) {
		this.billCycle = billCycle;
	}

	public double getArBalance() {
		return arBalance;
	}

	public void setArBalance(double arBalance) {
		this.arBalance = arBalance;
	}

	public char getBanStatus() {
		return banStatus;
	}

	public void setBanStatus(char banStatus) {
		this.banStatus = banStatus;
	}

	public short getDefaultBen() {
		return defaultBen;
	}

	public void setDefaultBen(short defaultBen) {
		this.defaultBen = defaultBen;
	}

	public String getStartServiceDate() {
		return startServiceDate;
	}

	public void setStartServiceDate(String startServiceDate) {
		this.startServiceDate = startServiceDate;
	}

	public char getColDelinqStatus() {
		return colDelinqStatus;
	}

	public void setColDelinqStatus(char colDelinqStatus) {
		this.colDelinqStatus = colDelinqStatus;
	}

	public long getHierarchyId() {
		return hierarchyId;
	}

	public void setHierarchyId(long hierarchyId) {
		this.hierarchyId = hierarchyId;
	}

	public long getObjectId() {
		return objectId;
	}

	public void setObjectId(long objectId) {
		this.objectId = objectId;
	}

	public String getMarketCode() {
		return marketCode;
	}

	public void setMarketCode(String marketCode) {
		this.marketCode = marketCode;
	}

	public int getPayerBan() {
		return payerBan;
	}

	public void setPayerBan(int payerBan) {
		this.payerBan = payerBan;
	}

	public char getPayerBanInd() {
		return payerBanInd;
	}

	public void setPayerBanInd(char payerBanInd) {
		this.payerBanInd = payerBanInd;
	}

	@Override
	public String toString() {
		return "BanData [ban=" + ban + ", accountType=" + accountType + ", billCycle=" + billCycle + ", arBalance="
				+ arBalance + ", banStatus=" + banStatus + ", defaultBen=" + defaultBen + ", startServiceDate="
				+ startServiceDate + ", colDelinqStatus=" + colDelinqStatus + ", hierarchyId=" + hierarchyId
				+ ", objectId=" + objectId + ", marketCode=" + marketCode + ", payerBan=" + payerBan + ", payerBanInd="
				+ payerBanInd + "]";
	}
	
}
