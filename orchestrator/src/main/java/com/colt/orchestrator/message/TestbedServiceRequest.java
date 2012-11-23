package com.colt.orchestrator.message;

@ServiceRequestDescription(command = "Testbed") 
public class TestbedServiceRequest extends ServiceRequest {
	private static final long serialVersionUID = 7953377370550515567L;
	private String domainName;
	private String account;
	private String volumeId;

	public String getDomainName() {
		return domainName;
	}

	public void setDomainName(String domainName) {
		this.domainName = domainName;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getVolumeId() {
		return volumeId;
	}

	public void setVolumeId(String volumeId) {
		this.volumeId = volumeId;
	}

}
