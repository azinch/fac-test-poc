package com.vip.ensemble.napi.test;

public class ExternalSystemApiClient {
	private ExternalSystemApi extObj;

	public ExternalSystemApiClient(ExternalSystemApi extObj) {
		this.extObj = extObj;
	}
	
	public String getExternalData(Integer key) {
		// some logic..
		return extObj.getData(key);
	}
	
	public boolean setExternalData(Integer key, String data) {
		validateData(data);
		extObj.setData(key, data);
		return true;
	}
	
	public void validateData(String data) {
		// some logic..
	}

	public ExternalSystemApi getExtObj() {
		return extObj;
	}

}
