package galaxy.spring.data.neo4j.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import galaxy.spring.data.neo4j.repositories.BelongsToRepository;
import galaxy.spring.data.neo4j.repositories.UserGroupRepository;
import galaxy.spring.data.neo4j.repositories.UserRepository;

import org.neo4j.ogm.annotation.*;

/**
 * @author Mark Angrish
 */
@RelationshipEntity(type = "BELONGS_TO")
public class BelongsTo implements Serializable {

    @Id
    @GeneratedValue
	private Long id;
    
    @StartNode
	private DomainUser duser;

	@EndNode
	private DomainUserGroup domainug;
	
	private List<String> userGroupsubscription = new ArrayList<>();
	private String name;

	

		
	public BelongsTo() {System.out.println("Calling empty constructor");}
	
	public BelongsTo(DomainUserGroup domainug, DomainUser duser) {
		this.duser = duser;
		this.domainug = domainug;
		this.name = duser.getName() + "_" + domainug.getName();
	}

	public Long getId() {
	    return id;
	}

	public List<String> getuUserGroupsubscription() {
	    return userGroupsubscription;
	}

	public DomainUser getDomainUser() {
	    return duser;
	}

	public DomainUserGroup getDomainUserGroup() {
	    return domainug;
	}

    public void addDomainUserGroup(String userGroupSubs) {
        if (this.userGroupsubscription == null) {
            this.userGroupsubscription = new ArrayList<>();
        }
        this.userGroupsubscription.add(userGroupSubs);
    }

	public String getName() {
		// TODO Auto-generated method stub
		return name;
	}
}