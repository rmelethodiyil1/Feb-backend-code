package galaxy.spring.data.neo4j.repositories;

import java.util.Collection;

import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import galaxy.spring.data.neo4j.domain.Domain;
import galaxy.spring.data.neo4j.domain.DomainUserGroup;
import galaxy.spring.data.neo4j.domain.SpecialTransaction;

/**
 * @author Michael Hunger
 * @author Mark Angrish
 * @author Michael J. Simons
 */
@RepositoryRestResource(collectionResourceRel = "specialtransaction", 
						path = "specialtransaction")
public interface SpecialTransactionRepository extends 
		Neo4jRepository<SpecialTransaction, Long> {

	
    @Query("MATCH (s:Domain)<-[r:SPECIALTRANSACTION_TO]->(a:DomainUserGroup) RETURN s,r,a LIMIT {limit}")
	Collection<Domain> newgraph(@Param("limit") int limit);

    @Query("MATCH (s:Domain)<-[r:SPECIALTRANSACTION_TO]->(a:DomainUserGroup) RETURN s,r,a LIMIT {limit}")
	Collection<Domain> graph(@Param("limit") int limit);
    
    @Query("MATCH p = (s:Domain{name:{domainname}})-[r:SPECIALTRANSACTION_TO]-(a:DomainUserGroup{name:{ugname}}) "
    		+ "return relationships(p)")
    Collection<SpecialTransaction> findforname(@Param("domainname") String domainname,
    		@Param("ugname") String ugname);
	@Query("MATCH (s:SpecialTransaction) RETURN s")
	Collection<SpecialTransaction> findAllspcl();
	
	@Query("MATCH (givendom{ name:{domainname}})<-[r:SPECIALTRANSACTION_TO]-(a:DomainUserGroup) RETURN r.name")
	Collection<String> findrelspcl(@Param("domainname") String domainname);
	
	
          
}