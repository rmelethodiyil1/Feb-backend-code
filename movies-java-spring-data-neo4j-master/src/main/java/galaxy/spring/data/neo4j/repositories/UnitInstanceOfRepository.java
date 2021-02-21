package galaxy.spring.data.neo4j.repositories;

import java.util.Collection;

import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import galaxy.spring.data.neo4j.domain.BelongsTo;
import galaxy.spring.data.neo4j.domain.Domain;
import galaxy.spring.data.neo4j.domain.DomainUserGroup;
import galaxy.spring.data.neo4j.domain.UnitInstanceOf;

/**
 * @author Michael Hunger
 * @author Mark Angrish
 * @author Michael J. Simons
 */
@RepositoryRestResource(collectionResourceRel = "unitinstanceof", 
						path = "unitinstanceof")
public interface UnitInstanceOfRepository extends 
		Neo4jRepository<UnitInstanceOf, Long> {

	
    @Query("MATCH (s:Domain)-[r:INSTANCE_OF]-"
    		+ "(a:Unit) RETURN s,r,a LIMIT {limit}")
	Collection<Domain> graph(@Param("limit") int limit);
    
    
}