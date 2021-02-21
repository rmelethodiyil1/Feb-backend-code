package galaxy.spring.data.neo4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import galaxy.spring.data.neo4j.domain.DomainUser;
import galaxy.spring.data.neo4j.domain.DomainUserGroup;
import galaxy.spring.data.neo4j.domain.Movie;
import galaxy.spring.data.neo4j.domain.Person;
import galaxy.spring.data.neo4j.domain.Role;
import galaxy.spring.data.neo4j.repositories.BelongsToRepository;
import galaxy.spring.data.neo4j.repositories.DomainRepository;
import galaxy.spring.data.neo4j.repositories.EndStateRepository;
import galaxy.spring.data.neo4j.repositories.MovieRepository;
import galaxy.spring.data.neo4j.repositories.PermittedRepository;
import galaxy.spring.data.neo4j.repositories.PersonRepository;
import galaxy.spring.data.neo4j.repositories.StartStateRepository;
import galaxy.spring.data.neo4j.repositories.StateRepository;
import galaxy.spring.data.neo4j.repositories.UnitRepository;
import galaxy.spring.data.neo4j.repositories.UserGroupRepository;
import galaxy.spring.data.neo4j.services.StateService;
import galaxy.spring.data.neo4j.services.UnitService;
import galaxy.spring.data.neo4j.services.UserService;



@Component
public class RepoInit {

	public void fillWithTestdata() {
		//setUp1();
		//setUp2();
	}

	@Autowired
	private MovieRepository movieRepository;
	@Autowired
	private PersonRepository personRepository;
	@Autowired
	private DomainRepository domainRepository;
	@Autowired
	private UserService userRepository;
	@Autowired
	private UserGroupRepository domainUsergroupRepository;
	@Autowired
	private StateRepository stateRepository;
	@Autowired
	private UnitRepository unitRepository;
	@Autowired
	private StartStateRepository startstateRepository;
	@Autowired
	private EndStateRepository endStateRepository;
	@Autowired
	private BelongsToRepository belongsToRepository;
	@Autowired
	private PermittedRepository permittedRepository;
	@Autowired
	private StateService stateService;
	@Autowired
	private UnitService unitService;
	
	public void setUp1() {
		Movie matrix = new Movie("The Matrix", 1999, 
				"Welcome to the Real World");

		movieRepository.save(matrix);

		Person keanu = new Person("Keanu Reeves", 1964);

		personRepository.save(keanu);

		Role neo = new Role(matrix, keanu);
		neo.addRoleName("Neo");

		matrix.addRole(neo);

		movieRepository.save(matrix);
	}

	public void setUp2() {
		
		/*State state1 = new State("Issued","REGULAR",true,true);
		stateRepository.save(state1);
		
		State state2 = new State("Available","REGULAR",true,true);
		stateRepository.save(state2);
		Domain book = new Domain("Book", "Library", 
				"Regular",state2.getName());
		Domain calendar = new Domain("Calendar", "Office", 
				"Regular",state2.getName());
		book.setInitialState(state1);
		calendar.setInitialState(state1);
		
		domainRepository.save(book);
		domainRepository.save(calendar);*/		
		DomainUserGroup ug = new DomainUserGroup("MyAdmin","ADMIN");		
		DomainUser keanu = new DomainUser("admin","MyAdmin","admin","SANTHOSH","GERUGAMPATTY");
		
		//BelongsTo keanuBelongsto = new BelongsTo(ug,keanu);
		domainUsergroupRepository.save(ug);
		keanu.setDusergroup(ug.getName());
		userRepository.save(keanu);
		//belongsToRepository.save(keanuBelongsto);
		//DomainUserGroup ug2 = new DomainUserGroup("Public");
		/*SpecialTransaction issued = new SpecialTransaction("Issued",book, ug);
		SpecialTransaction issued1 = new SpecialTransaction("Issued",calendar, ug2);
		issued.addSpecialTransactionName("Issue");
		issued1.addSpecialTransactionName("Issue");
		
		book.addSpecialTransaction(issued);
		calendar.addSpecialTransaction(issued);
		calendar.addSpecialTransaction(issued1);
		keanuBelongsto.addDomainUserGroup("MyCompanyGroup");		
		domainRepository.save(book);
		domainRepository.save(calendar);
		
		domainUsergroupRepository.save(ug2);
		*/
		
		
		//permittedstate2.addPermitteds("BookAvailable");
		
		/*StartState startState = new StartState(state2.getName(),"REGULAR");
		EndState endState =  new EndState(state1.getName(),"REGULAR");
		Permitted permittedstate1 = new Permitted(book,startState);
		permittedstate1.addPermitteds("BookIssued");
		Permitted permittedstate2 = new Permitted(book,endState);
		startstateRepository.save(startState);
		endStateRepository.save(endState);
		permittedRepository.save(permittedstate1);
		permittedRepository.save(permittedstate2);
		AllowedTransition allwd = new AllowedTransition(startState,endState,
				issued.getName(),issued.getName(),book.getName());
		
		Unit aBook = new Unit("The sun sets in the west","REGULAR",book.getName());
		UnitInstanceOf uof = new UnitInstanceOf(book,aBook);
		unitRepository.save(aBook);
		unitService.save(uof);*/
		//State initialState = stateRepository.findStartStateByName(book.getInitialStatename());
		/*StartState initialState = stateService.findStartStateByName(book.getInitialStatename());
		stateService.createCurrentState(initialState,aBook);
		aBook.setInstanceOf(uof);*/
		
		
	}
}
