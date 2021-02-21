package galaxy.spring.data.neo4j.repositories;

import java.util.Collection;

import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import galaxy.spring.data.neo4j.domain.BelongsTo;
import galaxy.spring.data.neo4j.domain.DomainUserGroup;

/**
 * @author Michael Hunger
 * @author Mark Angrish
 * @author Michael J. Simons
 */
@RepositoryRestResource(collectionResourceRel = "belongsto", 
						path = "belongsto")
public interface BelongsToRepository extends 
		Neo4jRepository<BelongsTo, Long> {

	
    @Query("MATCH (s:DomainUserGroup)-[r:BELONGS_TO]-"
    		+ "(a:DomainUser) RETURN s,r,a LIMIT {limit}")
	Collection<DomainUserGroup> graph(@Param("limit") int limit);
    
    
}