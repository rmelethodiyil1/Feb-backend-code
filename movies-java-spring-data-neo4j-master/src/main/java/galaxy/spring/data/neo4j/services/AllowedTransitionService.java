package galaxy.spring.data.neo4j.services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import galaxy.spring.data.neo4j.domain.AllowedTransition;
import galaxy.spring.data.neo4j.domain.Domain;
import galaxy.spring.data.neo4j.domain.DomainUserGroup;
import galaxy.spring.data.neo4j.domain.SpecialTransaction;
import galaxy.spring.data.neo4j.domain.StartState;
import galaxy.spring.data.neo4j.domain.State;
import galaxy.spring.data.neo4j.dto.RelationshipDTO;
import galaxy.spring.data.neo4j.dto.ResponseParamDTO;
import galaxy.spring.data.neo4j.repositories.AllowedTransitionRepository;
import galaxy.spring.data.neo4j.repositories.SpecialTransactionRepository;

@Service
public class AllowedTransitionService {

    private final static Logger LOG = LoggerFactory.
    		getLogger(AllowedTransitionService.class);

	private final AllowedTransitionRepository allowedTransitionRepository;
	public AllowedTransitionService(AllowedTransitionRepository 
			allowedTransitionRepository) {
		this.allowedTransitionRepository = allowedTransitionRepository;
	}

	private Map<String, Object> toD3Format(Collection<StartState> allowedTrns) {
		List<Map<String, Object>> startnodes = new ArrayList<>();
		List<Map<String, Object>> endnodes = new ArrayList<>();
		List<Map<String, Object>> rels = new ArrayList<>();
		int i = 0;int length = 0;int ocounter =0, icounter1, icounter2;
		Iterator<StartState> result = allowedTrns.iterator();
		String[] txnNames1,txnNames2,txnNames3,txnNames;
		Object[] txnObject1,txnObject2 ,txnObject3 ,txnObject;
		while (result.hasNext()) {
			length++;
			result.next();
		}
		result = allowedTrns.iterator();
		txnNames1 = new String[2];	txnObject1 = new Object[2];
		txnNames2 = new String[4];	txnObject2 = new Object[4];
		txnNames3 = new String[2];	txnObject3 = new Object[2];
		txnNames = new String[3];	txnObject = new Object[3];
		System.out.println("Number of domains " + length);
		while (result.hasNext()) {
			StartState currstate = result.next();
			ocounter =0;
			txnNames1[ocounter] = "name";	txnObject1[ocounter++] = currstate.getName();
			txnNames1[ocounter] = "label";txnObject1[ocounter++] = "startstate";
			startnodes.add(map(txnNames1,txnObject1));
			
			for (AllowedTransition allowdTrn : currstate.getAllowedtrn()) {
				icounter2 = 0;
				for (String fordomain : allowdTrn.getallowedmMap().keySet()) {
					icounter1 = 0;
					txnNames2[icounter1] = "label";txnObject2[icounter1++] = fordomain;
					txnNames2[icounter1] = "name";txnObject2[icounter1++] = allowdTrn.getallowedmMap().get(fordomain);
					txnNames2[icounter1] = "label1";txnObject2[icounter1++] = currstate.getName();
					txnNames2[icounter1] = "label2";txnObject2[icounter1++] = allowdTrn.getEndState().getName();
					
				}
				txnNames3[icounter2] = "name";	
				txnObject3[icounter2++] = allowdTrn.getEndState().getName();
				txnNames3[icounter2] = "label";	txnObject3[icounter2++] = "endstate";
				rels.add(map(txnNames2,txnObject2));
			}
			endnodes.add(map(txnNames3,txnObject3));
			
		}
		ocounter = 0;
		txnNames[ocounter] = "startnodes";txnObject[ocounter] = startnodes;
		ocounter++;
		txnNames[ocounter] = "endnodes";txnObject[ocounter] = endnodes;
		ocounter++;
		txnNames[ocounter] = "links";txnObject[ocounter] = rels;
		for(Map<String, Object> listofobj : startnodes) {
			System.out.println(" for keys " + listofobj.keySet() +
					" values are " + listofobj.values());
		}
		return map(txnNames,txnObject);
	}

	private Map<String, Object> map(String[] keys, Object[] values) {
		Map<String, Object> result = new HashMap<String, Object>();
		int vPtr = 0;
		for(String key : keys)
			result.put(key, values[vPtr++]);
		return result;
	}

    
	@Transactional(readOnly = true)
	public Map<String, Object>  graph1(int limit) {
		Collection<StartState> result = allowedTransitionRepository.graph(limit);
		return toD3Format(result);
	}

	public List<ResponseParamDTO> getAllAllowedwithrels() {
		
		ArrayList<ResponseParamDTO> relDTO = new ArrayList<ResponseParamDTO>();
		Iterable<AllowedTransition> allTrns = allowedTransitionRepository.findAll();
		for(AllowedTransition givenTrn : allTrns) {
			
			Map<String, String> getallowedmMap = givenTrn.getallowedmMap();
			Iterator<Entry<String, String>> iterator = getallowedmMap.entrySet().iterator();
			while (iterator.hasNext()) {
			
				Map.Entry pair = (Map.Entry)iterator.next();
				ResponseParamDTO arelDTO = new ResponseParamDTO(givenTrn.getName(),
				givenTrn.getStartStatename(),givenTrn.getEndStatename(),(String)pair.getKey(),(String)pair.getValue(),
				givenTrn.getId());
				relDTO.add(arelDTO);
			}
		}
		return 	relDTO;
		
	}
	
	@Transactional
	public AllowedTransition save(AllowedTransition allowedTransition) {
		// TODO Auto-generated method stub
		return allowedTransitionRepository.save(allowedTransition);
	}
	
	@Transactional
	public AllowedTransition delete(Long id) {
		
		 Optional<AllowedTransition> allowedTransition = 
				 allowedTransitionRepository.findById(id);
		 System.out.println("Deleting Allowed trn " + allowedTransition.get().getName());
		 allowedTransitionRepository.deleteById(id);
		 return allowedTransition.get();
		// TODO Auto-generated method stub
	}

	public AllowedTransition update(Long id, AllowedTransition 
			allowedTransition) {
		
		// TODO Auto-generated method stub
		allowedTransitionRepository.save(allowedTransition);
		Optional<AllowedTransition> allowedTransitionobj = 
				allowedTransitionRepository.findById(id);
		return allowedTransitionobj.get();
	}

	

	public List<AllowedTransition> findWithName(String name) {
		 // TODO Auto-generated method stub
		System.out.println("Searching for all allowed transition with name" 
				+ name );
		Collection<StartState> retrievedstartstates = 
				allowedTransitionRepository.graph(100);
		List<AllowedTransition> allallwdTrn = new ArrayList<>();
		System.out.println("Total number = " + retrievedstartstates.size());		 
		Iterator<StartState> result = retrievedstartstates.iterator();
		while (result.hasNext()) {
			StartState startState = result.next();					
			for (AllowedTransition allwdTrn : startState.getAllowedtrn()) {
							
				if(allwdTrn.getName().equals(name)) {
				    System.out.println("Found 1 transition");
					allallwdTrn.add(allwdTrn);
				    		 
				}
			}
					
		}
		return  allallwdTrn;

	}
	
}
