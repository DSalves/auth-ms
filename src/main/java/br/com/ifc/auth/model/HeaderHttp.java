package br.com.ifc.auth.model;

public enum HeaderHttp {

	AUTHORIZATION("Authorization"),
	TOKEN_PREFIX("Bearer");

	private String name;

	private HeaderHttp(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}
}
