package galaxy.spring.data.neo4j.domain;

import java.util.ArrayList;
import java.util.List;

import org.neo4j.ogm.annotation.GeneratedValue;
import org.neo4j.ogm.annotation.Id;
import org.neo4j.ogm.annotation.Index;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Property;
import org.neo4j.ogm.annotation.Relationship;
import org.neo4j.ogm.annotation.typeconversion.Convert;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import galaxy.spring.data.neo4j.utils.GalaxyConverter;

@NodeEntity
public class Domain {

	@Id
	@GeneratedValue
	private Long id;
	
	@Index(unique=true)
	private String name;
	@Index(unique=true)
	private String type;
	private String screenType;

	@Relationship(type = "SPECIALTRANSACTION_TO", direction = Relationship.INCOMING)
	private List<SpecialTransaction> specialTransaction;

	@Relationship(type = "INSTANCE_OF", direction = Relationship.INCOMING)
	private List<UnitInstanceOf> instances;
	
		
	public String getInitialStatename() {
		return initialStatename;
	}

	@Relationship(type = "PERMITTED_TO")
	private List<Permitted> permittedTo;
	
	@Convert(value = GalaxyConverter.class)
	private State initialState;
	
	@Property
	private String initialStatename;
	
	public Domain() {
	}

	public Domain(String name, String type, String screenType,String initialStatename) {
		this.name = name;
		this.type = type;
		this.screenType = screenType;
		this.initialStatename = initialStatename;
	}

	public Long getId() {
		return id;
	}

	public State getInitialState() {
		
		return initialState;
	}
	
	public void setInitialState(State initState) {
		initialState = initState;
	}
	
	public void addState(Permitted permittedfor) {
		if (this.permittedTo == null) {
			this.permittedTo = new ArrayList<>();
		}
		this.permittedTo.add(permittedfor);
	}
	
	public void addSpecialTransaction(SpecialTransaction spclTxn) {
		if (this.specialTransaction == null) {
			this.specialTransaction = new ArrayList<>();
		}
		this.specialTransaction.add(spclTxn);
	}

	public List<Permitted> getPermittedStates() {
		return permittedTo;
	}
	
	public void addInstanceOf(UnitInstanceOf instanceofunit) {
		if (this.instances == null) {
			this.instances = new ArrayList<>();
		}
		this.instances.add(instanceofunit);
	}
	
	public String getName() {
		// TODO Auto-generated method stub
		return name;
	}
	
	public List<SpecialTransaction> getSpecialTransaction() {
		return specialTransaction;
	}
	
	public List<UnitInstanceOf> getInstanceOf() {
		return instances;
	}

	public String getType() {
		// TODO Auto-generated method stub
		return type;
	}
}
