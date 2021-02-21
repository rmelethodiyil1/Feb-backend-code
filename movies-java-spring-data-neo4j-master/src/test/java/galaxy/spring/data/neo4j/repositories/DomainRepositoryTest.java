package galaxy.spring.data.neo4j.repositories;

import static org.junit.Assert.*;

import java.util.Collection;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import galaxy.spring.data.neo4j.domain.Domain;
import galaxy.spring.data.neo4j.domain.DomainUser;
import galaxy.spring.data.neo4j.domain.DomainUserGroup;
import galaxy.spring.data.neo4j.domain.Movie;
import galaxy.spring.data.neo4j.domain.Person;
import galaxy.spring.data.neo4j.domain.Role;
import galaxy.spring.data.neo4j.domain.SpecialTransaction;
import galaxy.spring.data.neo4j.repositories.DomainRepository;
import galaxy.spring.data.neo4j.repositories.PersonRepository;

/**
 * @author pdtyreus
 */

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Transactional
public class DomainRepositoryTest {

	@Autowired
	private MovieRepository movieRepository;

	@Autowired
	private PersonRepository personRepository;
	
	@Autowired
	private DomainRepository domainRepository;

	@Before
	public void setUp() {
		
		Movie matrix = new Movie("The Matrix", 1999, "what is this movie");
		movieRepository.save(matrix);

		Person keanu = new Person("Thonna moocha", 1964);
		personRepository.save(keanu);

		Role neo = new Role(matrix, keanu);
		neo.addRoleName("Neo");
		matrix.addRole(neo);
		movieRepository.save(matrix);
		
		//Domain domain = new Domain("Book","String","1");
		//domainRepository.save(domain);
		DomainUserGroup duserGroup = new DomainUserGroup("RajuLibraryUsers","ROLE_BOTH");		
		DomainUser duser = new DomainUser("TestUser","RajuLibraryUsers" ,"12345","RAJKUMAR", "MANGANATHAN");
		
		//SpecialTransaction spclTxn = new SpecialTransaction("Issued",domain,duserGroup);
		//domain.addSpecialTransaction(spclTxn);
		//domainRepository.save(domain);
	}

	/**
	 * Test of findByTitle method, of class MovieRepository.
	 */
	@Test
	public void testFindByTitle() {

		String title = "The Matrix";
		Movie result = movieRepository.findByTitle(title);
		assertNotNull(result);
		assertEquals(1999, result.getReleased());
	}

	/**
	 * Test of findByTitleContaining method, of class MovieRepository.
	 */
	@Test
	public void testFindByTitleContaining() {
		String title = "*Matrix*";
		Collection<Movie> result = movieRepository.findByTitleLike(title);
		assertNotNull(result);
		assertEquals(1, result.size());
	}

	/**
	 * Test of graph method, of class MovieRepository.
	 */
	@Test
	public void testGraph() {
		/*Collection<Movie> graph = movieRepository.graph(5);

		assertEquals(1, graph.size());

		Movie movie = graph.iterator().next();

		assertEquals(1, movie.getRoles().size());

		assertEquals("The Matrix", movie.getTitle());
		assertEquals("Keanu Reeves", movie.getRoles().iterator().next().getPerson().getName()); */
		
		Collection<Domain> graph = domainRepository.graph(5);
		
		assertEquals(1,graph.size());
		Domain domain = graph.iterator().next();
		assertEquals(1,domain.getSpecialTransaction().size());
		assertEquals("Book",domain.getName());
		assertEquals("RajuLibraryUsers",domain.getSpecialTransaction().iterator().next().getDomainUserGroup().getName());
	}
}