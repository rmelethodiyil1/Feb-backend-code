package galaxy.spring.data.neo4j.domain;

import org.neo4j.ogm.annotation.EndNode;
import org.neo4j.ogm.annotation.GeneratedValue;
import org.neo4j.ogm.annotation.Id;
import org.neo4j.ogm.annotation.RelationshipEntity;
import org.neo4j.ogm.annotation.StartNode;

/**
 * @author Mark Angrish
 */
@RelationshipEntity(type = "INSTANCE_OF")
public class UnitInstanceOf {

    @Id
    @GeneratedValue
	private Long id;
	private String instanceOfDomain;

	@StartNode
	private Unit unit;

	@EndNode
	private Domain domain;

	public UnitInstanceOf() {
	}

	public UnitInstanceOf(Domain thedomain, Unit theUnit) {
		this.domain = thedomain;
		this.unit = theUnit;
	}

	public Long getId() {
	    return id;
	}

	public String getInstanceOfDomain() {
	    return instanceOfDomain;
	}

	public Domain getDomain() {
	    return domain;
	}

	public Unit getUnit() {
	    return unit;
	}

    public void setInstanceOfDomain(String instanceOfDomain) {
        
        this.instanceOfDomain = instanceOfDomain;
    }
}