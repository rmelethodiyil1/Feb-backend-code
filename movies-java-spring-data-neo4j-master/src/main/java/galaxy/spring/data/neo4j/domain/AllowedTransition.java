package galaxy.spring.data.neo4j.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.neo4j.ogm.annotation.EndNode;
import org.neo4j.ogm.annotation.GeneratedValue;
import org.neo4j.ogm.annotation.Id;
import org.neo4j.ogm.annotation.Index;
import org.neo4j.ogm.annotation.RelationshipEntity;
import org.neo4j.ogm.annotation.StartNode;
import org.neo4j.ogm.annotation.typeconversion.Convert;

import galaxy.spring.data.neo4j.utils.GalaxyTransConverter;

/**
 * @author Mark Angrish
 */
@RelationshipEntity(type = "ALLOWED_TRANSITION")
public class AllowedTransition {

    @Id
    @GeneratedValue
	private Long id;
	private List<String> allowedTransition = new ArrayList<>();

	@StartNode
	private StartState startState;

	@EndNode
	private EndState endState;

	private String startStatename;
	private String endStatename;
	
	@Index(unique=true)
	private String starttoendState;
	
	@Index(unique=true)
	private String name;
	
	public Map<String,String> getallowedmMap() {
		return spclforDomains;
	}

	public String getspclForDomain(String domainName) {
		return spclforDomains.get(domainName);
	}

	public void addspclForDomain(String domainName,String spcltransaction) {
		spclforDomains.put(domainName,spcltransaction);
	}
	
		
	@Convert(value = GalaxyTransConverter.class)
	private Map<String,String> spclforDomains;
	
	public AllowedTransition() {
	}

	public AllowedTransition(StartState startState, EndState endState, String name,
			String spclTxnname, String forDomain) {
		this.startState = startState;
		this.endState = endState;
		this.name = name;
		this.spclforDomains = new java.util.concurrent.ConcurrentHashMap<String,String>();
		this.spclforDomains.put(forDomain,spclTxnname);
		this.startStatename = startState.getName();
		this.endStatename = endState.getName();
	}
 
	
	
	public String getStartStatename() {
		return startStatename;
	}

	public String getEndStatename() {
		return endStatename;
	}

	public Long getId() {
	    return id;
	}

	public String getName() {
		return name;
	}

	public List<String> getAllowedTransition() {
	    return allowedTransition;
	}

	
    public StartState getStartState() {
		return startState;
	}

	public EndState getEndState() {
		return endState;
	}

	public void addallowedTransition(String allwdTransition) {
        if (this.allowedTransition == null) {
            this.allowedTransition = new ArrayList<>();
        }
        this.allowedTransition.add(allwdTransition);
    }
}