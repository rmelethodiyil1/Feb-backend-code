package galaxy.spring.data.neo4j.repositories;

import java.util.Collection;

import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.repository.query.Param;
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
public interface EndStateRepository extends Neo4jRepository<EndState, Long> {

	@Query("MATCH (d:EndState{name:{name}}) RETURN d")
	EndState findByName(@Param("name") String name);

	@Query("MATCH (d:EndState)<-[r:PERMITTED_TO]-(a:Domain) RETURN d,r,a LIMIT {limit}")
	Collection<EndState> findAllEndStates(@Param("limit") long limit);

	@Query("MATCH (d:EndState) RETURN d")
	Collection<EndState> findOnlyEndStates();
}