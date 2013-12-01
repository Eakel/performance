package com.easyfun.eclipse.performance.http.model;

public enum HttpMethodEnum {

	/** GET���� */
	GET("GET"),

	/** POST���� */
	POST("POST"),

	/** HEAD���� */
	HEAD("HEAD"),

	/** OPTIONS���� */
	OPTIONS("OPTIONS"),

	/** TRACE���� */
	TRACE("TRACE"),

	/** PUT���� */
	PUT("PUT"),

	/** DELETE���� */
	DELETE("DELETE");

	private String str;

	private HttpMethodEnum(String str) {
		this.str = str;
	}

	public String toString() {
		return str;
	}
	
	public static HttpMethodEnum getHttpMethodByName(String str){
		if(str.equals("GET")){
			return GET;
		}else if(str.equals("POST")){
			return POST;
		}else if(str.equals("HEAD")){
			return HEAD;
		}else if(str.equals("OPTIONS")){
			return OPTIONS;
		}else if(str.equals("TRACE")){
			return TRACE;
		}else if(str.equals("PUT")){
			return PUT;
		}else if(str.equals("DELETE")){
			return DELETE;
		}
		return null;
	}

}
