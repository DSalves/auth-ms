package br.com.ifc.auth.model;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import org.springframework.data.annotation.Id;

/**
 * User
 * @author thiago.colombo
 *
 */
public class User implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Name
	 */
	@Id
	private String name;
	
	/**
	 * Password
	 */
	private String password;
	
	/**
	 * Rules
	 */
	private Set<String> authorities;
	
	/**
	 * Construct
	 */
	public User() {
		this.authorities = new HashSet<>();
	}
	
	/**
	 * Construct
	 * @param name
	 * @param password
	 */
	public User(String name, String password) {
		this();
		this.name = name;
		this.password = password;
	}

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

	public Set<String> getAuthorities() {
		return authorities;
	}

	public void setAuthorities(Set<String> authorities) {
		this.authorities = authorities;
	}
	
	@Override
    public String toString() {
        return String.format("User[name=%s, password='%s', authorities='%s']", name, "-", authorities != null ? authorities.toArray(new String[authorities.size()]).toString() : null);
    }
}
