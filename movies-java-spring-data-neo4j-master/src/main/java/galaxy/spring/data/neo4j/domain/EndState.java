package galaxy.spring.data.neo4j.domain;

import java.util.List;

import org.neo4j.ogm.annotation.GeneratedValue;
import org.neo4j.ogm.annotation.Id;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@NodeEntity
public class EndState extends State{

	@Id
	@GeneratedValue
	private Long id;
	
	private String forDomain;
	public String getForDomain() {
		return forDomain;
	}

	public void setForDomain(String forDomain) {
		this.forDomain = forDomain;
	}

	public List<AllowedTransition> getAllowedtrn() {
		return allowedtrn;
	}

	public void addAllowedtrn(AllowedTransition allowedtrn) {
		this.allowedtrn.add(allowedtrn);
	}

	private String name;
	
	private String screenType;

	@JsonIgnoreProperties("unit")
	@Relationship(type = "ALLOWED_TRANSITION")
	private List<AllowedTransition> allowedtrn;

	public EndState(State state) {
		this.name = state.getName();
		this.screenType = state.getscreenType();
	}
	
	public EndState() {
	}

	public EndState(String name, String screenType) {
		this.name = name;
		this.screenType = screenType;
	}

	public Long getId() {
		return id;
	}

	
		
	public String getName() {
		// TODO Auto-generated method stub
		return name;
	}
	
}
