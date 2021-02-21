package galaxy.spring.data.neo4j.domain;

import org.neo4j.ogm.annotation.GeneratedValue;
import org.neo4j.ogm.annotation.Id;
import org.neo4j.ogm.annotation.NodeEntity;
@NodeEntity
public class TransactionUsers {

 	 private  String userName;
	 public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getThisState() {
		return thisState;
	}

	public void setThisState(String thisState) {
		this.thisState = thisState;
	}

	private  String thisState;
	 
	 public TransactionUsers(String user, String thisState) {
		 this.userName = user;
		 this.thisState = thisState;
	 }
	 
	@Id
	@GeneratedValue
	private Long id;
	
	@Override
	public int hashCode() {
	    final int prime = 31;
	    int result = 3;
	    result = prime * result + thisState.hashCode();
	    result = prime * result + userName.hashCode();
	    return result;
	}

	@Override
	public boolean equals(Object obj) {
	    if (this == obj)
	        return true;
	    if (obj == null)
	        return false;
	    if (getClass() != obj.getClass())
	        return false;
	    TransactionUsers other = (TransactionUsers) obj;
	    if (thisState.equals(other.thisState))
	        return true;	    
	    return false;
	}
}
