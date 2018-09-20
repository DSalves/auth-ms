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
	private Set<String> rules;
	
	/**
	 * Construct
	 */
	public User() {
		this.rules = new HashSet<>();
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

	public Set<String> getRules() {
		return rules;
	}

	public void setRules(Set<String> rules) {
		this.rules = rules;
	}
	
	@Override
    public String toString() {
        return String.format("User[name=%s, firstName='%s', lastName='%s']", name, "-", rules != null ? rules.toArray(new String[rules.size()]).toString() : null);
    }
}
