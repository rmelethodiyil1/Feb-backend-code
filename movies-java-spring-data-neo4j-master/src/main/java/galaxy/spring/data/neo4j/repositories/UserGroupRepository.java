package galaxy.spring.data.neo4j.repositories;

import java.util.Collection;
import java.util.Optional;

import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

import galaxy.spring.data.neo4j.domain.Domain;
import galaxy.spring.data.neo4j.domain.DomainUser;
import galaxy.spring.data.neo4j.domain.DomainUserGroup;
import galaxy.spring.data.neo4j.domain.Person;

/**
 * @author pdtyreus
 * @author Mark Angrish
 */
@RepositoryRestResource(collectionResourceRel = "usergroup", path = "usergroup")
public interface UserGroupRepository extends Neo4jRepository<DomainUserGroup, Long> {

	DomainUserGroup findByName(@Param("name") String name);
	
	Optional<DomainUserGroup> findById(@Param("id") Long id);
	
	@Query("MATCH (d:DomainUserGroup) -[r:SPECIALTRANSACTION_TO]-(a:Domain) RETURN d,r,a LIMIT {limit}")
	Collection<DomainUserGroup> graph(@Param("limit") int limit);
	//-[r:SPECIALTRANSACTION_TO]-(a:Domain) RETURN d,r,a LIMIT

	@Query("MATCH (d:DomainUserGroup) RETURN d")
	Collection<DomainUserGroup> findAllUsersGroup();
}