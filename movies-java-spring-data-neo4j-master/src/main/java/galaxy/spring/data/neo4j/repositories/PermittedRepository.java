package galaxy.spring.data.neo4j.repositories;

import java.util.Collection;

import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import galaxy.spring.data.neo4j.domain.Domain;
import galaxy.spring.data.neo4j.domain.Permitted;

/**
 * @author Michael Hunger
 * @author Mark Angrish
 * @author Michael J. Simons
 */
@RepositoryRestResource(collectionResourceRel = "permitted", 
						path = "permitted")
public interface PermittedRepository extends 
		Neo4jRepository<Permitted, Long> {

	
    @Query("MATCH (s:Domain)-[r:PERMITTED_TO]-"
    		+ "(a:State) RETURN s,r,a LIMIT {limit}")
	Collection<Domain> graph(@Param("limit") int limit);
    
    
}