package galaxy.spring.data.neo4j.domain;

import java.util.ArrayList;
import java.util.List;

import org.neo4j.ogm.annotation.GeneratedValue;
import org.neo4j.ogm.annotation.Id;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@NodeEntity
public class StartState extends State {

	@Id
	@GeneratedValue
	private Long id;
	
	

	public ArrayList<AllowedTransition> getAllowedtrn() {
		return allowedtrn;
	}

	public void addAllowedtrn(AllowedTransition allowedtrn) {
		this.allowedtrn.add(allowedtrn);
	}

	private String name;
	
	private String screenType;

	//@JsonIgnoreProperties("unit")
	@Relationship(type = "ALLOWED_TRANSITION")
	private ArrayList<AllowedTransition> allowedtrn;

	
	
	public StartState() {
	}

	public StartState(State state) {
		
		this.name = state.getName();
		this.screenType = state.getscreenType();
	}
	public StartState(String name, String screenType) {
		this.name = name;
		this.screenType = screenType;
		this.allowedtrn = new ArrayList<AllowedTransition>();
	}

	public Long getId() {
		return id;
	}

	
		
	public String getName() {
		// TODO Auto-generated method stub
		return name;
	}
	
		
}
