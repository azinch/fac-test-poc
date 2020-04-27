package com.vip.ensemble.napi.test;


public class SubscriberData {
	
	private String subscriberNo;
	private int subscriberId;
	private char subStatus;
	private String initActivationDate;
	private int ban;
	private String accountType;
	private String pricePlan;
	private String productCode;
	
	private String itemId;
	private String simSerialNo;
	private String ctnNgp;
	private String ctnNl;
	
	// TODO: other data, e.g Registration, etc.
	
	public SubscriberData() {
	}
	
	public SubscriberData(String subscriberNo, int subscriberId, char subStatus, String initActivationDate, int ban,
			String accountType, String pricePlan, String productCode, String itemId, String simSerialNo, String ctnNgp,
			String ctnNl) {
		this.subscriberNo = subscriberNo;
		this.subscriberId = subscriberId;
		this.subStatus = subStatus;
		this.initActivationDate = initActivationDate;
		this.ban = ban;
		this.accountType = accountType;
		this.pricePlan = pricePlan;
		this.productCode = productCode;
		this.itemId = itemId;
		this.simSerialNo = simSerialNo;
		this.ctnNgp = ctnNgp;
		this.ctnNl = ctnNl;
	}
	
	public SubscriberData(SubscriberData sbsData) {
		this.subscriberNo = sbsData.subscriberNo;
		this.subscriberId = sbsData.subscriberId;
		this.subStatus = sbsData.subStatus;
		this.initActivationDate = sbsData.initActivationDate;
		this.ban = sbsData.ban;
		this.accountType = sbsData.accountType;
		this.pricePlan = sbsData.pricePlan;
		this.productCode = sbsData.productCode;
		this.itemId = sbsData.itemId;
		this.simSerialNo = sbsData.simSerialNo;
		this.ctnNgp = sbsData.ctnNgp;
		this.ctnNl = sbsData.ctnNl;
	}
	
	public String getSubscriberNo() {
		return subscriberNo;
	}

	public void setSubscriberNo(String subscriberNo) {
		this.subscriberNo = subscriberNo;
	}

	public int getSubscriberId() {
		return subscriberId;
	}

	public void setSubscriberId(int subscriberId) {
		this.subscriberId = subscriberId;
	}

	public char getSubStatus() {
		return subStatus;
	}

	public void setSubStatus(char subStatus) {
		this.subStatus = subStatus;
	}

	public String getInitActivationDate() {
		return initActivationDate;
	}

	public void setInitActivationDate(String initActivationDate) {
		this.initActivationDate = initActivationDate;
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

	public String getPricePlan() {
		return pricePlan;
	}

	public void setPricePlan(String pricePlan) {
		this.pricePlan = pricePlan;
	}

	public String getProductCode() {
		return productCode;
	}

	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}

	public String getItemId() {
		return itemId;
	}

	public void setItemId(String itemId) {
		this.itemId = itemId;
	}

	public String getSimSerialNo() {
		return simSerialNo;
	}

	public void setSimSerialNo(String simSerialNo) {
		this.simSerialNo = simSerialNo;
	}

	public String getCtnNgp() {
		return ctnNgp;
	}

	public void setCtnNgp(String ctnNgp) {
		this.ctnNgp = ctnNgp;
	}

	public String getCtnNl() {
		return ctnNl;
	}

	public void setCtnNl(String ctnNl) {
		this.ctnNl = ctnNl;
	}

	@Override
	public String toString() {
		return "SubscriberData [subscriberNo=" + subscriberNo + ", subscriberId=" + subscriberId + ", subStatus="
				+ subStatus + ", initActivationDate=" + initActivationDate + ", ban=" + ban + ", accountType="
				+ accountType + ", pricePlan=" + pricePlan + ", productCode=" + productCode + ", itemId=" + itemId
				+ ", simSerialNo=" + simSerialNo + ", ctnNgp=" + ctnNgp + ", ctnNl=" + ctnNl + "]";
	}
	
}

