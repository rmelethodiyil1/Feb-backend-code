package galaxy.spring.data.neo4j.domain;

import java.util.ArrayList;
import java.util.List;

import org.neo4j.ogm.annotation.GeneratedValue;
import org.neo4j.ogm.annotation.Id;
import org.neo4j.ogm.annotation.Index;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@NodeEntity
public class State {

	@Id
	@GeneratedValue
	private Long id;
	
	@Index(unique=true)
	private String name;
	
	private String screenType;

	@JsonIgnoreProperties("state")
	@Relationship(type = "PERMITTED_TO", direction = Relationship.INCOMING)
	private List<Permitted> permittedTo;
	
	@Relationship(type = "CURRENT_STATE")
	private List<CurrentState> currentStates;
	
	private boolean startState;
	
	private boolean endState;
	
	
	
	public State() {
	}

	public State(String name, String screenType,
			boolean startstate, boolean endstate) {
		this.name = name;
		this.screenType = screenType;
		this.startState = startstate;
		this.endState = endstate;
	}

	public Long getId() {
		return id;
	}

	
	public void addState(Permitted permittedfor) {
		if (this.permittedTo == null) {
			this.permittedTo = new ArrayList<>();
		}
		this.permittedTo.add(permittedfor);
	}

		
	public boolean isStartState() {
		return startState;
	}

	public boolean isEndState() {
		return endState;
	}

	public String getName() {
		// TODO Auto-generated method stub
		return name;
	}
	
	public List<Permitted> getPermittedStates() {
		return permittedTo;
	}

	public String getscreenType() {
		// TODO Auto-generated method stub
		return screenType;
	}

	public void addCurrentState(CurrentState currentState) {
		if (this.currentStates == null) {
			this.currentStates = new ArrayList<>();
		}
		this.currentStates.add(currentState);
	}
	
	public List<CurrentState> getCurrentState() {
		return currentStates;
	}
}
