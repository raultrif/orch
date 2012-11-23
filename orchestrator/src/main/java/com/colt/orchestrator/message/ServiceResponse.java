package com.colt.orchestrator.message;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlRootElement;

import org.apache.commons.lang.builder.ToStringBuilder;

@XmlRootElement 
public class ServiceResponse implements Serializable  {
	private static final long serialVersionUID = 9094851574966808308L;

	@Override
	public String toString() {
		 return ToStringBuilder.reflectionToString(this);
	}
}
