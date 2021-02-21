package galaxy.spring.data.neo4j.domain;

import java.util.ArrayList;
import java.util.List;

import org.neo4j.ogm.annotation.EndNode;
import org.neo4j.ogm.annotation.GeneratedValue;
import org.neo4j.ogm.annotation.Id;
import org.neo4j.ogm.annotation.RelationshipEntity;
import org.neo4j.ogm.annotation.StartNode;

/**
 * @author Mark Angrish
 */
@RelationshipEntity(type = "PERMITTED_TO")
public class Permitted {

    @Id
    @GeneratedValue
	private Long id;
	private List<String> permittedList = new ArrayList<>();

	@StartNode
	private Domain domain;

	@EndNode
	private State state;

	private String name;
	
	public Permitted() {
	}

	public String getName() {
		return name;
	}

	public Permitted(Domain domain, State state) {
		this.domain = domain;
		this.state = state;
		this.name = domain.getName() + "_" + state.getName();
				
	}

	public Long getId() {
	    return id;
	}

	public List<String> getPermitted() {
	    return permittedList;
	}

	public State getState() {
	    return state;
	}

	public Domain getDomain() {
	    return domain;
	}

    public void addPermitteds(String permitted) {
        if (this.permittedList == null) {
            this.permittedList = new ArrayList<>();
        }
        this.permittedList.add(permitted);
    }
}