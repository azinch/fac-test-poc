package com.vip.ensemble.napi.test;


public class ExternalSystemApi {
	
	public String getData(Integer key) {
		// some logic..
		return "data to return"; 
	}
	
	public boolean setData(Integer key, String data) {
		if(key <= 0) {
			throw new ExternalSystemException("input key is incorrect");
		}
		// some logic..
		return true;
	}
	
}
