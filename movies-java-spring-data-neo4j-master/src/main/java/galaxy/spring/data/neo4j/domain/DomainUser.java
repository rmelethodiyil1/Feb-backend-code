package galaxy.spring.data.neo4j.domain;


import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.neo4j.ogm.annotation.GeneratedValue;
import org.neo4j.ogm.annotation.Id;
import org.neo4j.ogm.annotation.Index;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * @author Mark Angrish
 */
@NodeEntity
public class DomainUser {

    @Id
    @GeneratedValue
	private Long id;
    @Index(unique=true)
	private String name;
	private String dusergroup;
	private String password;
	private String firstName;
	private String lastName;
	
		
	public String getPassword() {
		return password;
	}
	
	private DomainUserGroup userGroups;

	public DomainUser() {		
	}
	
	public void setName(String name) {
		this.name = name;
	}

	public void setDusergroup(String dusergroup) {
		this.dusergroup = dusergroup;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public DomainUser(String duname,String dusergroup,
			String password,String firstName, String lastName) {
		this.name = duname;
		this.dusergroup = dusergroup; 
		this.password = password;
		this.firstName = firstName;
		this.lastName = lastName;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	
	public String getDusergroup() {
		return dusergroup;
	}

	public void addBelongsTo(DomainUserGroup belongstoitem) {
		userGroups = belongstoitem;
	}
	
	public DomainUserGroup getBelongsTo() {
		return userGroups;
	}

	
	
	
}