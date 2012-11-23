package com.colt.common.xml;

import java.io.IOException;
import java.io.StringWriter;

import javax.xml.transform.stream.StreamResult;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.oxm.Marshaller;
import org.springframework.oxm.Unmarshaller;
import org.springframework.oxm.XmlMappingException;
import org.springframework.xml.transform.StringSource;


public class XmlMarshaller {

	@Autowired
	Marshaller marshaller;
	@Autowired
	Unmarshaller unmarshaller;
	
	
	public String marshal(Object object) throws XmlMappingException, IOException {
		final StringWriter out = new StringWriter();
		marshaller.marshal(object, new StreamResult(out));
		return out.toString();
	}

	@SuppressWarnings("unchecked")
	public <O> O unmarshal(String string, Class<O>clazz) throws XmlMappingException, IOException {
		return (O) unmarshaller.unmarshal(new StringSource(string));
	}
}
