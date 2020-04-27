package com.vip.ensemble.napi.test;



public class OperationalData {
	private int operMode;
	private int sessionId;
	private String logicalDate;
	
	public OperationalData(int operMode, int sessionId, String logicalDate) {
		this.operMode = operMode;
		this.sessionId = sessionId;
		this.logicalDate = logicalDate;
	}
	
	public int getOperMode() {
		return operMode;
	}
	public int getSessionId() {
		return sessionId;
	}
	public String getLogicalDate() {
		return logicalDate;
	}

	@Override
	public String toString() {
		return "OperationalData [operMode=" + operMode + ", sessionId=" + sessionId + ", logicalDate=" + logicalDate
				+ "]";
	}
	
}
