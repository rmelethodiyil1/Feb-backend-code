package galaxy.spring.data.neo4j.domain;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.neo4j.ogm.annotation.*;

/**
 * @author Mark Angrish
 */
@RelationshipEntity(type = "SPECIALTRANSACTION_TO")
public class SpecialTransaction {

    @Id
    @GeneratedValue
	private Long id;
    
	private String name;
	private List<String> txnSupported = new ArrayList<>();
	@StartNode
	private DomainUserGroup dusergrp;
	@EndNode
	private Domain domain;

	private boolean isAttachedTransition;
	
	public String getAttachedToState() {
		return attachedToState;
	}

	private String attachedToState;
	
	public SpecialTransaction() {
	}

	public SpecialTransaction(String name,Domain domain, DomainUserGroup dusergroup, boolean attached,String attachedToState) {
		this.domain = domain;
		this.dusergrp = dusergroup;
		this.name = name;
		this.isAttachedTransition = attached;
		this.attachedToState = attachedToState;
	}

	public boolean isAttachedTransition() {
		return isAttachedTransition;
	}

	public String getName() {
		return name;
	}

	public Long getId() {
	    return id;
	}

	public List<String> getTxnSupported() {
	    return txnSupported;
	}

	public DomainUserGroup getDomainUserGroup() {
	    return dusergrp;
	}

	public Domain getDomain() {
	    return domain;
	}

	@Override
    public boolean equals(Object o) { 
  
        // If the object is compared with itself then return true   
        if (o == this) { 
            return true; 
        }   
        /* Check if o is an instance of Complex or not 
          "null instanceof [type]" also returns false */
        if (!(o instanceof SpecialTransaction)) { 
            return false; 
        } 
          
        // typecast o to Complex so that we can compare data members  
        SpecialTransaction c = (SpecialTransaction) o; 
          
        // Compare the data members and return accordingly  
        return Double.compare(id, c.id) == 0;
                
    } 
	@Override
    public int hashCode() 
    { 
		return this.id.intValue();
    }
	
    public void addSpecialTransactionName(String spcltxn) {
        if (this.txnSupported == null) {
            this.txnSupported = new ArrayList<>();
        }
        this.txnSupported.add(spcltxn);
    }
}