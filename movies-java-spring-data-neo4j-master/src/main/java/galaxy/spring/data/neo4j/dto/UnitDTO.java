package galaxy.spring.data.neo4j.dto;

public class UnitDTO implements Comparable<UnitDTO>{

	private Long id;
	
	private String name;
	
	private String currentStatename;
	
    private String screenType;
	
	private String domainname;

	public UnitDTO() {
		
	}
	
	public Long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getCurrentStatename() {
		return currentStatename;
	}

	public String getScreenType() {
		return screenType;
	}

	public String getDomainname() {
		return domainname;
	}

	@Override     
	public int compareTo(UnitDTO candidate) {          
	    return (this.getName().compareTo(candidate.getName()) < 0 ? -1 : 
	    	    (this.getName().compareTo(candidate.getName()) > 0 ? 1 :	    	    
	            (this.getDomainname().compareTo(candidate.getDomainname()) < 0 ? -1 : 
	            (this.getCurrentStatename().compareTo(candidate.getCurrentStatename()) > 0 ? +1 : 
	            (this.getScreenType().compareTo(candidate.getScreenType()) <0 ? +1 : -1)))));     
	} 
	
	
	public UnitDTO(Long id,String name, String currentStatename,
			String screenType,String domainname) {
		
		this.domainname = domainname;
		this.screenType = screenType;
		this.currentStatename = currentStatename;
		this.name = name;
		this.id = id;
		
	}
	

	public String toString() {
		return "ID is " + id + " Domain name " + domainname +  "name " + name;
	}
}
