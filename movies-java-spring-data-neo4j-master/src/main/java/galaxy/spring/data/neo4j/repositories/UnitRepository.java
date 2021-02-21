package galaxy.spring.data.neo4j.repositories;

import java.util.Collection;

import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import galaxy.spring.data.neo4j.domain.Domain;
import galaxy.spring.data.neo4j.domain.State;
import galaxy.spring.data.neo4j.domain.Unit;

/**
 * @author pdtyreus
 * @author Mark Angrish
 */
@RepositoryRestResource(collectionResourceRel = "unit", path = "unit")
public interface UnitRepository extends Neo4jRepository<Unit, Long> {

	//<-[r:CURRENT_STATE]-(a:State)
	 @Query("MATCH (d:Unit{name:{name}})-[r]-(a) RETURN d,r,a")
	 Unit findByName(@Param("name")String name);

	 @Query("MATCH (d:Unit)-[r:INSTANCE_OF]-(a:Domain) RETURN d,r,a LIMIT {limit}")
	 Collection<Unit> graph(@Param("limit") int limit);
	
	 @Query("MATCH (d:Unit)-[r:INSTANCE_OF]-(a:Domain{name:{name}}) RETURN d")
	 Collection<Unit> findAllUnitsfordomain(@Param("name") String name);
	 
	 @Query("MATCH (d:Unit{name:{name}})-[r:INSTANCE_OF]-(a:Domain) RETURN d,r,a")
	 Unit findwithDomainByName(@Param("name")String name);
	 
	 @Query("MATCH (d:Unit{id:{id}})-[r:INSTANCE_OF]-(a:Domain) RETURN d,r,a")
	 Unit findCustomById(@Param("id")Long id);
}