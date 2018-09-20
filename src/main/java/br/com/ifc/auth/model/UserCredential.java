package br.com.ifc.auth.model;

/**
 * User credential
 * @author thiago.colombo
 *
 */
public class UserCredential {
	
	/**
	 * Name
	 */
	private String name;
	
	/**
	 * Password
	 */
	private String password;

	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
}
