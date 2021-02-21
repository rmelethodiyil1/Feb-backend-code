package galaxy.spring.data.neo4j.domain;

import org.neo4j.ogm.annotation.EndNode;
import org.neo4j.ogm.annotation.GeneratedValue;
import org.neo4j.ogm.annotation.Id;
import org.neo4j.ogm.annotation.RelationshipEntity;
import org.neo4j.ogm.annotation.StartNode;

/**
 * @author Mark Angrish
 */
@RelationshipEntity(type = "SINSTANCE_OF")
public class StateInstanceOf {

    @Id
    @GeneratedValue
	private Long id;
	private String instanceOfState;

	@StartNode
	private State state;

	@EndNode
	private Astate astate;

	public StateInstanceOf() {
	}

	public StateInstanceOf(State state, Astate sstate) {
		this.state = state;
		this.astate = sstate;
	}

	public Long getId() {
	    return id;
	}

	public String getInstanceOfState() {
	    return instanceOfState;
	}

	

    public State getState() {
		return state;
	}

	public Astate getAstate() {
		return astate;
	}

	public void setInstanceOfState(String instanceOfState) {
        
        this.instanceOfState = instanceOfState;
    }
}