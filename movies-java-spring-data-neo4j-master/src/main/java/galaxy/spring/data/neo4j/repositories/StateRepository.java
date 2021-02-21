package galaxy.spring.data.neo4j.repositories;

import java.util.Collection;

import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

import galaxy.spring.data.neo4j.domain.Domain;
import galaxy.spring.data.neo4j.domain.DomainUser;
import galaxy.spring.data.neo4j.domain.DomainUserGroup;
import galaxy.spring.data.neo4j.domain.Person;
import galaxy.spring.data.neo4j.domain.State;

/**
 * @author pdtyreus
 * @author Mark Angrish
 */
@RepositoryRestResource(collectionResourceRel = "state", path = "state")
public interface StateRepository extends Neo4jRepository<State, Long> {

	@Query("MATCH (d:Domain)<-[r:PERMITTED_TO]-(a:State) RETURN d,r,a LIMIT {limit}")
	Collection<State> graph(@Param("limit") int limit);
	
		
	@Query("MATCH (d:State{name:{name}}) RETURN d")
	Collection<State> findByName(@Param("name") String name);
	
	@Query("MATCH (d:State{name:{name}}) RETURN d")
	State finduntestedName(@Param("name") String name);
		
	@Query("MATCH (d:State) RETURN d")
	Collection<State> findAllStates();

	
}