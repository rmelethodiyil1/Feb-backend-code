package galaxy.spring.data.neo4j.repositories;

import java.util.Collection;

import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

import galaxy.spring.data.neo4j.domain.DomainUser;
import galaxy.spring.data.neo4j.domain.DomainUserGroup;
import galaxy.spring.data.neo4j.domain.EndState;
import galaxy.spring.data.neo4j.domain.Person;
import galaxy.spring.data.neo4j.domain.StartState;
import galaxy.spring.data.neo4j.domain.State;

/**
 * @author pdtyreus
 * @author Mark Angrish
 */
@RepositoryRestResource(collectionResourceRel = "startstate", path = "startstate")
public interface StartStateRepository extends Neo4jRepository<StartState, Long> {

	@Query("MATCH (d:StartState{name:{name}})-[r:ALLOWED_TRANSITION]-(a:EndState) RETURN d,collect(r),collect(a)")
	StartState findByName(@Param("name") String name);
	
	@Query("MATCH (d:StartState{name:{name}}) return d")
	StartState findOnlyStartState(@Param("name") String name);
	
	@Query("MATCH (d:StartState)-[r:PERMITTED_TO]-(a:Domain) RETURN d,r,a LIMIT {limit}")
	Collection<StartState> findAllStartStates(@Param("limit") int limit);

	@Query("MATCH (d:StartState) RETURN d")
	Collection<StartState> findOnlyStartStates();
}