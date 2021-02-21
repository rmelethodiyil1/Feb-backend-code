package galaxy.spring.data.neo4j.controller;

import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import galaxy.spring.data.neo4j.domain.Domain;
import galaxy.spring.data.neo4j.domain.DomainUser;
import galaxy.spring.data.neo4j.domain.EndState;
import galaxy.spring.data.neo4j.domain.Movie;
import galaxy.spring.data.neo4j.domain.Permitted;
import galaxy.spring.data.neo4j.domain.StartState;
import galaxy.spring.data.neo4j.domain.State;
import galaxy.spring.data.neo4j.services.DomainService;
import galaxy.spring.data.neo4j.services.PermittedService;
import galaxy.spring.data.neo4j.services.StateService;

/**
 * @author Mark Angrish
 * @author Michael J. Simons
 */
@RestController
@RequestMapping("/")
public class StateController {

	private final StateService stateService;
	private final PermittedService permittedService;
	private final DomainService	domainService;
	
	public StateController(StateService stateService,
					PermittedService permittedService,DomainService domainService) {
		this.stateService = stateService;
		this.permittedService = permittedService;
		this.domainService = domainService;
	}

    @GetMapping("/galaxy/appadmin/state/graph")
	public Map<String, Object> graph(@RequestParam(value = "limit",required = false) Integer limit) {
		return stateService.graph1(limit == null ? 100 : limit);
	}
    
    @GetMapping(value="/galaxy/appadmin/startstate")
    public @ResponseBody Map<String, Object> findStartState() {

    	Map<String, Object> states = stateService.getStartStates();        
        return states;
    }   
    @GetMapping(value="/galaxy/appadmin/endstate")
    public @ResponseBody Map<String, Object> findEndState() {

    	Map<String, Object> states = stateService.getEndStates();        
        return states;
    }
    @GetMapping(value="/galaxy/appadmin/allstartstate")
    public @ResponseBody List<StartState> findAllStartState() {

    	List<StartState> states = stateService.getAllStartStates();        
        return states;
    }
    @GetMapping(value="/galaxy/appadmin/allstates")
    public @ResponseBody List<State> findAllState() {

    	List<State> states = stateService.getAllStates();        
        return states;
    }
    @GetMapping(value="/galaxy/appadmin/allendstate")
    public @ResponseBody List<EndState>  findAllEndState() {

    	List<EndState> states = stateService.getAllEndStates();        
        return states;
    }
	@PostMapping("/galaxy/appadmin/state")
	public ResponseEntity<Object> createState(@RequestBody State state) {
		System.out.println(" State request is "+ state.getName());
		State createdState = null;
		State createstate = stateService.save(state);
		if (state.isStartState()) {
			 createdState = 
					stateService.createStartState(state);
		}
		if(state.isEndState()) {
			createdState = stateService.createEndState(state);
		}
		
		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").
						buildAndExpand(createstate.getId()).toUri();
		return ResponseEntity.created(location).build();
	}
	
	@DeleteMapping("/galaxy/appadmin/startstate/{id}")
	public ResponseEntity<?> deletestartState(@PathVariable String id) {

		StartState startState = stateService.findStartStateByName(id);
		if (startState == null) {
			return new ResponseEntity<String>("No StartState found for ID " + 
					id, HttpStatus.NOT_FOUND);
		}else {
			stateService.deleteStartState(startState.getId());
		}
		return new ResponseEntity<Long>(startState.getId(), HttpStatus.OK);
	}

	@DeleteMapping("/galaxy/appadmin/endstate/{id}")
	public ResponseEntity<?> deleteendState(@PathVariable String id) {

		EndState endState = stateService.findEndStateByName(id);
		if (endState == null) {
			return new ResponseEntity<String>("No EndState found for ID " + 
					id, HttpStatus.NOT_FOUND);
		}else {
			stateService.deleteendState(endState.getId());
		}
		return new ResponseEntity<Long>(endState.getId(), HttpStatus.OK);
	}
	
	@DeleteMapping("/galaxy/appadmin/state/{id}")
	public ResponseEntity<?> deleteState(@PathVariable String id) {

		State state = stateService.findByID(Long.valueOf(id));
		if (state == null) {
			return new ResponseEntity<String>("No DomainUser found for ID " + 
					id, HttpStatus.NOT_FOUND);
		}else {
			if(state.isStartState())
				stateService.deleteStartState(stateService.findStartStateByName(state.getName()).getId());
			if(state.isEndState())
				stateService.deleteendState(stateService.findEndStateByName(state.getName()).getId());
			stateService.delete(state.getId());
		}
		return new ResponseEntity<Long>(state.getId(), HttpStatus.OK);
		
	}
	
	@PutMapping("/galaxy/appadmin/state/{id}")
	public ResponseEntity<?> updateCustomer(@PathVariable Long id, @RequestBody State state) {

		state = stateService.update(id, state);

		if (null == state) {
			return new ResponseEntity<String>("No Customer found for ID " + id, HttpStatus.NOT_FOUND);
		}

		return new ResponseEntity<State>(state, HttpStatus.OK);
	}
}
