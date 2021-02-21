package galaxy.spring.data.neo4j.dto;

public class RequestParamDTO {

	private String param1;
	private String param2;
	private String name;
	
	
	public RequestParamDTO() {
		
	}
	
	public RequestParamDTO(String param1,String param2,String name) {
		
		this.param1 = param1;
		this.param2 = param2;
		this.name = name;
		
	}
	
	public String getParam1() {
		return param1;
	}

	public String getParam2() {
		return param2;
	}

	public String getName() {
		return name;
	}

	public String toString() {
		return "Startnode is " + param1 + " End node is " + param2;
	}
}
