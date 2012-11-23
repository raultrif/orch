package com.colt.orchestrator.message;

@ServiceResponseDescription(command = "CreateSnapshot")
public class CreateSnapshotServiceResponse extends ServiceResponse {
	private static final long serialVersionUID = 1549976450663709519L;
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
