package com.colt.orchestrator.message;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

import javax.xml.bind.annotation.XmlRootElement;

import org.apache.commons.lang.builder.ToStringBuilder;

@XmlRootElement
public class ServiceRequest implements Serializable {
	private static final long serialVersionUID = 3911601585573510604L;
	private String sncId;
	private String command;
	private String msgUid;
	private Date msgCreated;

	public ServiceRequest() {
		this.msgCreated = new Date();
		this.msgUid = UUID.randomUUID().toString();

	}

	public String getSncId() {
		return sncId;
	}

	public void setSncId(String sncId) {
		this.sncId = sncId;
	}

	public String getCommand() {
		return command;
	}

	public void setCommand(String command) {
		this.command = command;
	}

	public String getMsgUid() {
		return msgUid;
	}

	public void setMsgUid(String msgUid) {
		this.msgUid = msgUid;
	}

	public Date getMsgCreated() {
		return msgCreated;
	}

	public void setMsgCreated(Date msgCreated) {
		this.msgCreated = msgCreated;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

}
