package galaxy.spring.data.neo4j.dto;

public class RelationshipDTO implements Comparable<RelationshipDTO>{

	public String getStartnodeid() {
		return startnodeid;
	}

	public String getEndnodeid() {
		return endnodeid;
	}

	private String startnodeid;
	private String endnodeid;
	private String name;
	private String label1;
	private String label2;
	
	public RelationshipDTO() {
		
	}
	
	@Override     
	public int compareTo(RelationshipDTO candidate) {          
	    return (this.getName().compareTo(candidate.getName()) < 0 ? -1 : 
	    	    (this.getName().compareTo(candidate.getName()) > 0 ? 1 :	    	    
	            (this.getStartnodeid().compareTo(candidate.getStartnodeid()) < 0 ? -1 : 
	            (this.getStartnodeid().compareTo(candidate.getStartnodeid()) > 0 ? +1 : 
	            (this.getEndnodeid().compareTo(candidate.getEndnodeid()) <0 ? -1 : 
	            (this.getEndnodeid().compareTo(candidate.getEndnodeid()) >0 ? +1 : -1))))));     
	} 
	
	public String getLabel1() {
		return label1;
	}

	public String getLabel2() {
		return label2;
	}

	public RelationshipDTO(String name,String startnodeid, String endnodeid,
			String label1,String label2) {
		
		this.startnodeid = startnodeid;
		this.endnodeid = endnodeid;
		this.name = name;
		this.label1 = label1;
		this.label2 = label2;
		
	}
	public String getName() {
		return name;
	}

	public String toString() {
		return "Startnode is " + startnodeid + " End node is " + endnodeid;
	}
}
