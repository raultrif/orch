package com.colt.orchestrator.message;

@ServiceResponseDescription(command = "Testbed")
public class TestbedServiceResponse extends ServiceResponse {
private static final long serialVersionUID = 760871123821824893L;
	private String domainId;
	private String snapshotId;

	public String getDomainId() {
		return domainId;
	}

	public void setDomainId(String domainId) {
		this.domainId = domainId;
	}

	public String getSnapshotId() {
		return snapshotId;
	}

	public void setSnapshotId(String snapshotId) {
		this.snapshotId = snapshotId;
	}
}
