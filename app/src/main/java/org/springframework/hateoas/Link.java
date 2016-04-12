package org.springframework.hateoas;


import java.io.Serializable;

public class Link implements Serializable {

	public static final String REL_SELF = "self";
	public static final String REL_FIRST = "first";
	public static final String REL_PREVIOUS = "prev";
	public static final String REL_NEXT = "next";
	public static final String REL_LAST = "last";

	private String rel;
	private String href;

	public String getHref() {
		return href;
	}

	public String getRel() {
		return rel;
	}

}
