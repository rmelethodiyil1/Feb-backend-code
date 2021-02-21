package galaxy.spring.data.neo4j.domain;


import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.neo4j.ogm.annotation.GeneratedValue;
import org.neo4j.ogm.annotation.Id;
import org.neo4j.ogm.annotation.Index;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * @author Mark Angrish
 */
@NodeEntity
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DomainUserGroup {

    @Id
    @GeneratedValue
	private Long id;
    @Index(unique=true)
	private String name;
	private String authorityname;
	@Relationship(type = "BELONGS_TO" , direction = Relationship.INCOMING)
	private Set<DomainUser> belongsTo;
	
	@Relationship(type = "SPECIALTRANSACTION_TO")
	private List<SpecialTransaction> specialTransaction;
	
	private Set<AuthorityType> authorities = new HashSet<>();
	
    private enum  AuthorityType {
        ROLE_ADMIN,
        ROLE_USER,
        ROLE_BOTH
    }
    
        
	public String getAuthorityname() {
		return authorityname;
	}

	public void setAuthorityname(String authorityname) {
		this.authorityname = authorityname;
	}

	public DomainUserGroup() {
	}

	public DomainUserGroup(String dugname,String authorityval) {
		this.name = dugname;
		this.authorityname = authorityval;
	}

	public List<SpecialTransaction> getSpecialTransaction() {
		return specialTransaction;
	}
	
	public void addSpecialTransaction(SpecialTransaction spclTxn) {
		if (this.specialTransaction == null) {
			this.specialTransaction = new ArrayList<>();
		}
		this.specialTransaction.add(spclTxn);
	}
	
	public Set<DomainUser> getBelongsTo() {
		return belongsTo;
	}
	
	public void addBelongsTo(DomainUser belongsto) {
		if (this.belongsTo == null) {
			this.belongsTo = new HashSet<>();
		}
		this.belongsTo.add(belongsto);
	}
	
	public Long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	
	public Collection<? extends GrantedAuthority> getAuthorities() {
        
		return authorities.stream().map(authority -> 
		new SimpleGrantedAuthority(authority.toString())).collect(Collectors.toList());
	}
}