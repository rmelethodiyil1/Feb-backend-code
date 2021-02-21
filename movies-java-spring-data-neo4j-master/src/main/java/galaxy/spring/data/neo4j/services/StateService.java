package galaxy.spring.data.neo4j.services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import galaxy.spring.data.neo4j.domain.CurrentState;
import galaxy.spring.data.neo4j.domain.Domain;
import galaxy.spring.data.neo4j.domain.DomainUser;
import galaxy.spring.data.neo4j.domain.EndState;
import galaxy.spring.data.neo4j.domain.Permitted;
import galaxy.spring.data.neo4j.domain.SpecialTransaction;
import galaxy.spring.data.neo4j.domain.StartState;
import galaxy.spring.data.neo4j.domain.State;
import galaxy.spring.data.neo4j.domain.Unit;
import galaxy.spring.data.neo4j.repositories.CurrentStateRepository;
import galaxy.spring.data.neo4j.repositories.EndStateRepository;
import galaxy.spring.data.neo4j.repositories.StartStateRepository;
import galaxy.spring.data.neo4j.repositories.StateRepository;

@Service
public class StateService {

    private final static Logger LOG = LoggerFactory.getLogger(StateService.class);

	private final StateRepository stateRepository;
	private final StartStateRepository startstaterepository;
	private final EndStateRepository endStaterepository;
	private final CurrentStateRepository currentStateRepository;
	
	public StateService(StateRepository stateRepository,StartStateRepository startstaterepository,
			EndStateRepository endStaterepository,CurrentStateRepository currentStateRepository) {
		this.stateRepository = stateRepository;
		this.startstaterepository = startstaterepository;
		this.endStaterepository = endStaterepository;
		this.currentStateRepository = currentStateRepository;
	}

	

	private Map<String, Object> map(String key1, Object value1, String key2, 
			Object value2) {
		Map<String, Object> result = new HashMap<String, Object>(2);
		result.put(key1, value1);
		result.put(key2, value2);
		return result;
	}

    
	@Transactional(readOnly = true)
	public Map<String, Object>  graph1(int limit) {
		Collection<State> result = stateRepository.graph(limit);
		return toD3Format(result);
	}

	@Transactional
	public State save(State state) {
		// TODO Auto-generated method stub
		return stateRepository.save(state);
	}
	
	@Transactional
	public CurrentState save(CurrentState currstate) {
		// TODO Auto-generated method stub
		return currentStateRepository.save(currstate);
	}

	public State delete(Long id) {
		
		 Optional<State> stateObj = stateRepository.findById(id);
		 stateRepository.deleteById(id);
		 return stateObj.get();
		// TODO Auto-generated method stub
	}
	
	@Transactional(readOnly = true)
    public State findByID(Long id) {
		Optional<State> findById = stateRepository.findById(id);
		if(findById.isPresent()) {
			return findById.get();
		}
		else
			return null;
    }

	public State update(Long id, State state) {
		
		// TODO Auto-generated method stub
		stateRepository.save(state);
		Optional<State> stateObj = stateRepository.findById(id);
		 return stateObj.get();
	}

	private Map<String, Object> toD3Format(Collection<? extends State> states) {
		List<Map<String, Object>> nodes = new ArrayList<>();
		List<Map<String, Object>> rels = new ArrayList<>();
		int i = 0;
		Iterator<? extends State> result = states.iterator();
		while (result.hasNext()) {
			State state = result.next();
			nodes.add(map("name",state.getName(),"label", "state"));
			int target = i;
			i++;
			for (Permitted permitted : state.getPermittedStates()) {
				Map<String, Object> actor = map("name",permitted.getDomain().getName(),
						"label", "domain");
				int source = nodes.indexOf(actor);
				if (source == -1) {
					nodes.add(actor);
					source = i++;
				}
				rels.add(map("name", permitted.getName(),"label", "PermittedTo"));
			}
		}
		return map("nodes", nodes, "links", rels);
	}
	
	public Map<String, Object> getStartStates() {
		// TODO Auto-generated method stub
		Collection<StartState> states = startstaterepository.findAllStartStates(100);
		return toD3Format(states);
	}
	
	public Map<String, Object> getEndStates(){
		
		Collection<EndState> states = endStaterepository.findAllEndStates(100);
		return toD3Format(states);
	}
	
	public List<State> getAllStates() {
		// TODO Auto-generated method stub
		Collection<State> states = stateRepository.findAllStates();
		ArrayList<State> listof = new ArrayList<State>();
		for(State state : states)
			if(!(state instanceof StartState || state instanceof EndState))
				listof.add(state);
		return listof;
	}
	
	public List<StartState> getAllStartStates() {
		// TODO Auto-generated method stub
		Collection<StartState> states = startstaterepository.findOnlyStartStates();
		ArrayList<StartState> listof = new ArrayList<StartState>();
		for(StartState state : states)
			listof.add(state);
		return listof;
	}
	
	public List<EndState> getAllEndStates(){
		
		Collection<EndState> states = endStaterepository.findOnlyEndStates();
		ArrayList<EndState> listof = new ArrayList<EndState>();
		for(EndState state : states)
			listof.add(state);
		return listof;
	}
	@Transactional(readOnly = true)
    public State findByName(String name) {
		State result = stateRepository.finduntestedName(name);
        return result;
    }

	public StartState createStartState(State state) {
		
		return startstaterepository.save(new StartState(state));
	}

	public EndState createEndState(State state) {
		// TODO Auto-generated method stub
		return endStaterepository.save(new EndState(state));
	}
	@Transactional(readOnly = true)
    public StartState findStartStateByName(String name) {
		StartState result = startstaterepository.findOnlyStartState(name);
		System.out.println("Start state is " + result.getName() );
        return result;
    }
	@Transactional(readOnly = true)
    public StartState findStartStatewithTrn(String name) {
		StartState result = startstaterepository.findByName(name);
		System.out.println("Start state is " + result.getName() + 
				" Allowed size is " +  result.getAllowedtrn().size());
        return result;
    }
	@Transactional(readOnly = true)
    public EndState findEndStateByName(String name) {
		EndState result = endStaterepository.findByName(name);
        return result;
    }
	
	
	public CurrentState createCurrentState(State state,Unit unit) {
		
		return currentStateRepository.save(new CurrentState(state,unit));
	}
	public StartState deleteStartState(Long id) {
		
		 Optional<StartState> stateObj = startstaterepository.findById(id);
		 startstaterepository.deleteById(id);
		 return stateObj.get();
		// TODO Auto-generated method stub
	}
	public EndState deleteendState(Long id) {
		
		 Optional<EndState> stateObj = endStaterepository.findById(id);
		 endStaterepository.deleteById(id);
		 return stateObj.get();
		// TODO Auto-generated method stub
	}
	public void deleteCurrentState(CurrentState currState) {
		
		currentStateRepository.delete(currState);
	}



	public State findStateByName(String initialStatename) {

		Collection<State> result = stateRepository.findByName(initialStatename);
		ArrayList<State> listof = new ArrayList<State>();
		for(State state : result)
			if(!(state instanceof StartState || state instanceof EndState)) {
				return state;
			}
		return null;
	
	}
}
