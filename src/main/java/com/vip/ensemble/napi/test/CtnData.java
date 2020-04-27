package com.vip.ensemble.napi.test;


public class CtnData {
	private String ctn;
	private String ctnStatus;
	private String ngp;
	private String nl;
	private char pairedResourceTp;
	private String pairedResource;
	private char ngpType;
	private char niceClass;
	
	// TODO: other data.
	
	public CtnData() {
	}
	
	public CtnData(String ctn, String ctnStatus, String ngp, String nl, char pairedResourceTp, String pairedResource,
			char ngpType, char niceClass) {
		this.ctn = ctn;
		this.ctnStatus = ctnStatus;
		this.ngp = ngp;
		this.nl = nl;
		this.pairedResourceTp = pairedResourceTp;
		this.pairedResource = pairedResource;
		this.ngpType = ngpType;
		this.niceClass = niceClass;
	}
	
	public CtnData(CtnData ctnData) {
		this.ctn = ctnData.ctn;
		this.ctnStatus = ctnData.ctnStatus;
		this.ngp = ctnData.ngp;
		this.nl = ctnData.nl;
		this.pairedResourceTp = ctnData.pairedResourceTp;
		this.pairedResource = ctnData.pairedResource;
		this.ngpType = ctnData.ngpType;
		this.niceClass = ctnData.niceClass;
	}
	
	public String getCtn() {
		return ctn;
	}

	public void setCtn(String ctn) {
		this.ctn = ctn;
	}

	public String getCtnStatus() {
		return ctnStatus;
	}

	public void setCtnStatus(String ctnStatus) {
		this.ctnStatus = ctnStatus;
	}

	public String getNgp() {
		return ngp;
	}

	public void setNgp(String ngp) {
		this.ngp = ngp;
	}

	public String getNl() {
		return nl;
	}

	public void setNl(String nl) {
		this.nl = nl;
	}

	public char getPairedResourceTp() {
		return pairedResourceTp;
	}

	public void setPairedResourceTp(char pairedResourceTp) {
		this.pairedResourceTp = pairedResourceTp;
	}

	public String getPairedResource() {
		return pairedResource;
	}

	public void setPairedResource(String pairedResource) {
		this.pairedResource = pairedResource;
	}

	public char getNgpType() {
		return ngpType;
	}

	public void setNgpType(char ngpType) {
		this.ngpType = ngpType;
	}

	public char getNiceClass() {
		return niceClass;
	}

	public void setNiceClass(char niceClass) {
		this.niceClass = niceClass;
	}

	@Override
	public String toString() {
		return "CtnData [ctn=" + ctn + ", ctnStatus=" + ctnStatus + ", ngp=" + ngp + ", nl=" + nl
				+ ", pairedResourceTp=" + pairedResourceTp + ", pairedResource=" + pairedResource + ", ngpType="
				+ ngpType + ", niceClass=" + niceClass + "]";
	}

}
