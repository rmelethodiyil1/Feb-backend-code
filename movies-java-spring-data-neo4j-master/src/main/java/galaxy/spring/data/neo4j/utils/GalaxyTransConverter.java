package galaxy.spring.data.neo4j.utils;

import java.util.Map;
import java.util.StringTokenizer;
import java.util.concurrent.ConcurrentHashMap;

import org.neo4j.ogm.typeconversion.AttributeConverter;

public class GalaxyTransConverter  implements AttributeConverter<Map<String,String>, String> {

	@Override
    public String toGraphProperty(Map<String,String> value) {
		StringBuffer graphString = new StringBuffer();
		for(String eachdom : value.keySet()) {
			graphString = graphString.append(eachdom + "!@#");
			graphString = graphString.append(value.get(eachdom) + "+-");
			
		}
        return graphString.toString();
    }

    @Override                                    
    public Map<String,String> toEntityAttribute(String value) {
        StringTokenizer objectValue = new StringTokenizer(value,"+-");
        Map<String,String> allowedList = new ConcurrentHashMap<String,String>();
        while(objectValue.hasMoreTokens()) {
        	StringTokenizer domSpclmap = new StringTokenizer(objectValue.nextToken(),"!@#");
        	String domain = domSpclmap.nextToken();
        	String spclName = domSpclmap.nextToken();
        	allowedList.put(domain,spclName);
        }
        return allowedList;
    }
	
}

