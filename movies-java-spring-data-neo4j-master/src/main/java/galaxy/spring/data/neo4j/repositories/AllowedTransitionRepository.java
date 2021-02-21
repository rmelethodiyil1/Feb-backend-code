package galaxy.spring.data.neo4j.repositories;

import java.util.Collection;

import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import galaxy.spring.data.neo4j.domain.AllowedTransition;
import galaxy.spring.data.neo4j.domain.Domain;
import galaxy.spring.data.neo4j.domain.SpecialTransaction;
import galaxy.spring.data.neo4j.domain.StartState;
import galaxy.spring.data.neo4j.domain.State;

/**
 * @author Michael Hunger
 * @author Mark Angrish
 * @author Michael J. Simons
 */
@RepositoryRestResource(collectionResourceRel = "allowedtransition", 
						path = "allowedtransition")
public interface AllowedTransitionRepository extends 
		Neo4jRepository<AllowedTransition, Long> {

	
    @Query("MATCH (s:StartState)-[r:ALLOWED_TRANSITION]-"
    		+ "(a:EndState) RETURN s,r,a LIMIT {limit}")
	Collection<StartState> graph(@Param("limit") int limit);

    
          
}