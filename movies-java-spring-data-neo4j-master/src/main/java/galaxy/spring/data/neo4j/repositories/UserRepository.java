package galaxy.spring.data.neo4j.repositories;

import java.util.Collection;
import java.util.Optional;

import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

import galaxy.spring.data.neo4j.domain.BelongsTo;
import galaxy.spring.data.neo4j.domain.Domain;
import galaxy.spring.data.neo4j.domain.DomainUser;
import galaxy.spring.data.neo4j.domain.DomainUserGroup;
import galaxy.spring.data.neo4j.domain.Movie;
import galaxy.spring.data.neo4j.domain.Person;

/**
 * @author pdtyreus
 * @author Mark Angrish
 */
@RepositoryRestResource(collectionResourceRel = "user", path = "user")
public interface UserRepository extends Neo4jRepository<DomainUser, Long> {

	@Query("MATCH (d:DomainUser{name:{name}}) RETURN d")
	DomainUser findByName(@Param("name") String name);
	
	Optional<DomainUser> findById(@Param("id") Long id);
	
	/*@Query("MATCH (d:DomainUser)-[r:BELONGS_TO]-(a:DomainUserGroup) RETURN d,r,a LIMIT{limit}")
	Collection<DomainUser> graph(@Param("limit") int limit); */
	//-r:BELONGS_TO]-(a:DomainUserGroup) RETURN d,r,a LIMIT 

	@Query("MATCH (d:DomainUser) RETURN d")
	Collection<DomainUser> findAllUsers();
}