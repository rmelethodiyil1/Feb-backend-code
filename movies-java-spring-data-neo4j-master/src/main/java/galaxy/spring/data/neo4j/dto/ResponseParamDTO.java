package galaxy.spring.data.neo4j.dto;

public class ResponseParamDTO implements Comparable<ResponseParamDTO>{

	public String getStartnodeid() {
		return startnodeid;
	}

	public String getEndnodeid() {
		return endnodeid;
	}

	public Long getId() {
		return id;
	}
	private boolean status;
	private String startnodeid;
	private String endnodeid;
	private String name;
	private String label1;
	private String label2;
	private Long id;
	private String label3;
	
	public String getLabel3() {
		return label3;
	}

	public void setLabel3(String label3) {
		this.label3 = label3;
	}

	public ResponseParamDTO() {
		
	}
	
	@Override     
	public int compareTo(ResponseParamDTO candidate) {          
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

	public ResponseParamDTO(String name,String startnodeid, String endnodeid,
			String label1,String label2,Long id) {
		
		this.startnodeid = startnodeid;
		this.endnodeid = endnodeid;
		this.name = name;
		this.label1 = label1;
		this.label2 = label2;
		this.id = id;
		
	}
	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}

	public String getName() {
		return name;
	}

	public String toString() {
		return "Startnode is " + startnodeid + " End node is " + endnodeid;
	}
}
