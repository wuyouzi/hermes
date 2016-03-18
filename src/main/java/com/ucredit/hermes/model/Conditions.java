/**
 * Copyright(c) 2011-2014 by YouCredit Inc.
 * All Rights Reserved
 */
package com.ucredit.hermes.model;

import java.io.ByteArrayOutputStream;
import java.io.Serializable;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author caoming
 */
@XmlRootElement
public class Conditions implements Serializable {
	/**
	 *
	 */
	private static final long serialVersionUID = 4631799734135262958L;

	private Condition condition;

	public Condition getCondition() {
		return this.condition;
	}

	// 样例消息中是GBK，原有代码中是GB2312，做成静态常量以便修改
	private static final String encodingString = "GB2312";

	/**
	 * add by xubaoyong
	 *
	 * @return String
	 */
	public String toSearchString() {
		String data = "";
		try {
			JAXBContext jaxbContext = JAXBContext.newInstance(Conditions.class);
			Marshaller jaxbMarshaller = jaxbContext.createMarshaller();

			jaxbMarshaller.setProperty("jaxb.encoding",
					Conditions.encodingString);
			jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			// jaxbMarshaller
			// .setProperty("com.sun.xml.bind.xmlDeclaration", false);
			jaxbMarshaller.setProperty(Marshaller.JAXB_FRAGMENT, Boolean.TRUE);
			@SuppressWarnings("resource")
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			jaxbMarshaller.marshal(this, baos);
			String version = "<?xml version=\"1.0\" encoding=\""
					+ Conditions.encodingString + "\"?>\n";
			data = version
					+ new String(baos.toByteArray(), Conditions.encodingString);
			baos.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return data;
	}

	@XmlElement
	public void setCondition(Condition condition) {
		this.condition = condition;
	}

	public static class Condition {
		private String queryType;
		private List<Item> item;

		public String getQueryType() {
			return this.queryType;
		}

		@XmlAttribute
		public void setQueryType(String queryType) {
			this.queryType = queryType;
		}

		public List<Item> getItem() {
			return this.item;
		}

		@XmlElements(value = { @XmlElement, @XmlElement })
		public void setItem(List<Item> item) {
			this.item = item;
		}

	}

	public static class Item {
		private String name;
		private String value;

		public String getName() {
			return this.name;
		}

		@XmlElement
		public void setName(String name) {
			this.name = name;
		}

		public String getValue() {
			return this.value;
		}

		@XmlElement
		public void setValue(String value) {
			this.value = value;
		}

	}
}
