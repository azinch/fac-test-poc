package com.vip.ensemble.napi.test;

public class OrderData {
	private String posStoreId;
	private char posActivityType;
	private int posOrderOid;
	private String posBarCodeNumber;
	
	private String rmsInvoiceStoreId;
	private char rmsActivityType;
	private int rmsInvoiceId;
	private String arInvoiceNo;
	
	// TODO: other data.
	
	
	public OrderData() {
	}

	public OrderData(String posStoreId, char posActivityType, int posOrderOid, String posBarCodeNumber,
			String rmsInvoiceStoreId, char rmsActivityType, int rmsInvoiceId, String arInvoiceNo) {
		this.posStoreId = posStoreId;
		this.posActivityType = posActivityType;
		this.posOrderOid = posOrderOid;
		this.posBarCodeNumber = posBarCodeNumber;
		this.rmsInvoiceStoreId = rmsInvoiceStoreId;
		this.rmsActivityType = rmsActivityType;
		this.rmsInvoiceId = rmsInvoiceId;
		this.arInvoiceNo = arInvoiceNo;
	}
	
	public OrderData(OrderData posOrder) {
		this.posStoreId = posOrder.posStoreId;
		this.posActivityType = posOrder.posActivityType;
		this.posOrderOid = posOrder.posOrderOid;
		this.posBarCodeNumber = posOrder.posBarCodeNumber;
		this.rmsInvoiceStoreId = posOrder.rmsInvoiceStoreId;
		this.rmsActivityType = posOrder.rmsActivityType;
		this.rmsInvoiceId = posOrder.rmsInvoiceId;
		this.arInvoiceNo = posOrder.arInvoiceNo;
	}

	public String getPosStoreId() {
		return posStoreId;
	}

	public void setPosStoreId(String posStoreId) {
		this.posStoreId = posStoreId;
	}

	public char getPosActivityType() {
		return posActivityType;
	}

	public void setPosActivityType(char posActivityType) {
		this.posActivityType = posActivityType;
	}

	public int getPosOrderOid() {
		return posOrderOid;
	}

	public void setPosOrderOid(int posOrderOid) {
		this.posOrderOid = posOrderOid;
	}

	public String getPosBarCodeNumber() {
		return posBarCodeNumber;
	}

	public void setPosBarCodeNumber(String posBarCodeNumber) {
		this.posBarCodeNumber = posBarCodeNumber;
	}

	public String getRmsInvoiceStoreId() {
		return rmsInvoiceStoreId;
	}

	public void setRmsInvoiceStoreId(String rmsInvoiceStoreId) {
		this.rmsInvoiceStoreId = rmsInvoiceStoreId;
	}

	public char getRmsActivityType() {
		return rmsActivityType;
	}

	public void setRmsActivityType(char rmsActivityType) {
		this.rmsActivityType = rmsActivityType;
	}

	public int getRmsInvoiceId() {
		return rmsInvoiceId;
	}

	public void setRmsInvoiceId(int rmsInvoiceId) {
		this.rmsInvoiceId = rmsInvoiceId;
	}

	public String getArInvoiceNo() {
		return arInvoiceNo;
	}

	public void setArInvoiceNo(String arInvoiceNo) {
		this.arInvoiceNo = arInvoiceNo;
	}

	@Override
	public String toString() {
		return "OrderData [posStoreId=" + posStoreId + ", posActivityType=" + posActivityType + ", posOrderOid="
				+ posOrderOid + ", posBarCodeNumber=" + posBarCodeNumber + ", rmsInvoiceStoreId=" + rmsInvoiceStoreId
				+ ", rmsActivityType=" + rmsActivityType + ", rmsInvoiceId=" + rmsInvoiceId + ", arInvoiceNo="
				+ arInvoiceNo + "]";
	}
	
}
