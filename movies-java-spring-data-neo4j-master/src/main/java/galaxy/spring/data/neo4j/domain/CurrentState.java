package galaxy.spring.data.neo4j.domain;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.neo4j.ogm.annotation.*;

/**
 * @author Mark Angrish
 */
@RelationshipEntity(type = "CURRENT_STATE")
public class CurrentState {

    @Id
    @GeneratedValue
	private Long id;

	@StartNode
	private  State state;

	@EndNode
	private Unit unit;
	
	 

	public void setState(State state) {
		this.state = state;
	}

	public CurrentState() {
	}

	public CurrentState(State theState, Unit theUnit) {
		this.state = theState;
		this.unit = theUnit;
		
	}

	public Long getId() {
	    return id;
	}

	

	public State getState() {
	    return state;
	}

	public Unit getUnit() {
	    return unit;
	}

	@Override
	public String toString() {
		return state.getName();
	}

	
    
}