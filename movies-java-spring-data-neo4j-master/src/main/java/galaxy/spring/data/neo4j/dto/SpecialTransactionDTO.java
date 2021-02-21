package galaxy.spring.data.neo4j.dto;

public class SpecialTransactionDTO implements Comparable<SpecialTransactionDTO>{

	

	public String getIsAttached() {
		return isAttached;
	}

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
	private String isAttached;
	private String attachedStateName;
	
	public SpecialTransactionDTO() {
		
	}
	
	@Override     
	public int compareTo(SpecialTransactionDTO candidate) {          
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

	public SpecialTransactionDTO(String name,String startnodeid, String endnodeid,
			String isAttached,String attachedStateName) {
		
		this.startnodeid = startnodeid;
		this.endnodeid = endnodeid;
		this.name = name;
		this.isAttached = isAttached;
		this.attachedStateName = attachedStateName;
	}
	public String getName() {
		return name;
	}

	public String getAttachedStateName() {
		return attachedStateName;
	}

	public String toString() {
		return "Startnode is " + startnodeid + " End node is " + endnodeid + " is Attached " +
	isAttached + " attached st " + attachedStateName;
	}
}
