package galaxy.spring.data.neo4j.utils;

import org.neo4j.ogm.typeconversion.AttributeConverter;

import galaxy.spring.data.neo4j.domain.*;

public class GalaxyConverter implements AttributeConverter<State, String> {

	@Override
    public String toGraphProperty(State value) {
        if(value != null) {
        	return value.getName() + "!@#" + value.getscreenType() +
        		"!@#"  + value.isStartState() + "!@#" + value.isEndState();
        }else
        	return null;
    }

    @Override                                    
    public State toEntityAttribute(String value) {
        if(value != null) {
        	String[] split = value.split("!@#");
        	return new State(split[0], split[1],Boolean.valueOf(split[2]),Boolean.valueOf(split[3]));
        }else
        	return null;
    }
	
}
