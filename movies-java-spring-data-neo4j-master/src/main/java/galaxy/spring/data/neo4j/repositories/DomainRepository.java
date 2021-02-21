package galaxy.spring.data.neo4j.repositories;

import java.util.Collection;
import java.util.Optional;

import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import galaxy.spring.data.neo4j.domain.Domain;
import galaxy.spring.data.neo4j.domain.DomainUser;
import galaxy.spring.data.neo4j.domain.Movie;

/**
 * @author Michael Hunger
 * @author Mark Angrish
 * @author Michael J. Simons
 */
@RepositoryRestResource(collectionResourceRel = "domain", path = "domain")
public interface DomainRepository extends Neo4jRepository<Domain, Long> {

	
    @Query("MATCH (d:Domain)<-[r:SPECIALTRANSACTION_TO]-(a:DomainUserGroup) RETURN d,r,a LIMIT {limit}")
	Collection<Domain> graph(@Param("limit") int limit);

    @Query("MATCH (d:Domain) RETURN d")
	Collection<Domain> findAllDomains();
    
    Optional<Domain> findById(@Param("id") Long id);
    
    
}