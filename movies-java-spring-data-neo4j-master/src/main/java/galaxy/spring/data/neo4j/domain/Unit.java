package galaxy.spring.data.neo4j.domain;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

import org.neo4j.ogm.annotation.GeneratedValue;
import org.neo4j.ogm.annotation.Id;
import org.neo4j.ogm.annotation.Index;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@NodeEntity
public class Unit {

	public String getDomainname() {
		return domainname;
	}

	@Id
	@GeneratedValue
	private Long id;
	
	private String name;
	
	private String currentStatename;
	
	private Set<TransactionUsers>  transactedUsers;
	
	public void addTransactedUser(TransactionUsers user) {
		if(transactedUsers == null)
			transactedUsers = new HashSet<TransactionUsers>();
		if(!transactedUsers.add(user)){
			transactedUsers.remove(user);
			transactedUsers.add(user);
		}
	}
	
	public Set<TransactionUsers> getTransactedUsers() {
		return transactedUsers;
	}

	public String getCurrentStatename() {
		return currentStatename;
	}



	public void setCurrentStatename(String currentStatename) {
		this.currentStatename = currentStatename;
	}



	public CurrentState getCurrentState() {
		return currentState;
	}



	public void setCurrentState(CurrentState currentState) {
		this.currentState = currentState;
	}



	public UnitInstanceOf getAnInstanceOf() {
		return anInstanceOf;
	}



	public void setAnInstanceOf(UnitInstanceOf anInstanceOf) {
		this.anInstanceOf = anInstanceOf;
	}



	public String getScreenType() {
		return screenType;
	}



	public void setName(String name) {
		this.name = name;
	}



	public void setDomainname(String domainname) {
		this.domainname = domainname;
	}

	private String screenType;
	
	private String domainname;

	@JsonIgnoreProperties("unit")
	@Relationship(type = "CURRENT_STATE", direction = Relationship.INCOMING)
	private CurrentState currentState;

	@Relationship(type = "INSTANCE_OF")
	private UnitInstanceOf anInstanceOf;
	
	public Unit() {
	}

	

	public Unit(String name, String screenType,String domainname) {
		this.name = name;
		this.screenType = screenType;
		this.domainname = domainname;
	}

	public Long getId() {
		return id;
	}

	
	
	
	public String getName() {
		// TODO Auto-generated method stub
		return name;
	}
	
	
}
