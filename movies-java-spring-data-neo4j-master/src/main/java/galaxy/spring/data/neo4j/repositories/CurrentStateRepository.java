package galaxy.spring.data.neo4j.repositories;

import java.util.Collection;

import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import galaxy.spring.data.neo4j.domain.CurrentState;
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
public interface CurrentStateRepository extends Neo4jRepository<CurrentState, Long> {

	@Query("MATCH (d:CurrentState{name:{name}}) RETURN d")
	CurrentState findByName(@Param("name") String name);
	
	@Query("MATCH (d:StartState) RETURN d")
	Collection<CurrentState> findAllCurrentStates();

}