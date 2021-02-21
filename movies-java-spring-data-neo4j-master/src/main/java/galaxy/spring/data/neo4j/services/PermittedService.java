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

import galaxy.spring.data.neo4j.domain.Permitted;
import galaxy.spring.data.neo4j.domain.Domain;
import galaxy.spring.data.neo4j.domain.DomainUserGroup;
import galaxy.spring.data.neo4j.domain.SpecialTransaction;
import galaxy.spring.data.neo4j.repositories.PermittedRepository;

@Service
public class PermittedService {

    private final static Logger LOG = LoggerFactory.
    		getLogger(PermittedService.class);

	private final PermittedRepository permittedRepository;
	public PermittedService(PermittedRepository	permittedRepository) {
		this.permittedRepository = permittedRepository;
	}

	private Map<String, Object> toD3Format(Collection<Domain> domainsug) {
		List<Map<String, Object>> startnodes = new ArrayList<>();
		List<Map<String, Object>> endnodes = new ArrayList<>();
		List<Map<String, Object>> rels = new ArrayList<>();
		int i = 0;int length = 0;int ocounter =0;
		Iterator<Domain> result = domainsug.iterator();
		String[] txnNames1,txnNames2,txnNames3,txnNames;
		Object[] txnObject1,txnObject2 ,txnObject3 ,txnObject;
		while (result.hasNext()) {
			length++;
			result.next();
		}
		result = domainsug.iterator();
		txnNames1 = new String[3];	txnObject1 = new Object[2];
		txnNames2 = new String[2];	txnObject2 = new Object[2];
		txnNames3 = new String[2];	txnObject3 = new Object[2];
		txnNames = new String[3];	txnObject = new Object[3];
		System.out.println("Number of domains " + length);
		while (result.hasNext()) {
			Domain domainug = result.next();			
			txnNames1[ocounter] = "id";	txnObject1[ocounter++] = domainug.getId();
			txnNames1[ocounter] = "label";txnObject1[ocounter++] = "domainusergroup";
			startnodes.add(map(txnNames1,txnObject1));
			int icounter1 = 0,icounter2 = 0;
			for (Permitted belTo : domainug.getPermittedStates()) {
				txnNames2[icounter1] = "name";	txnObject2[icounter1++] = belTo.getName();
				txnNames2[icounter1] = "label";	txnObject2[icounter1++] = "belongsTo";
				txnNames3[icounter2] = "name";	
				txnObject3[icounter2++] = belTo.getState().getName();
				txnNames3[icounter2] = "label";	txnObject3[icounter2++] = "domainuser";
			}
			rels.add(map(txnNames2,txnObject2));
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
		Collection<Domain> result = permittedRepository.graph(limit);
		return toD3Format(result);
	}


	@Transactional
	public Permitted save(Permitted belongsTo) {
		// TODO Auto-generated method stub
		return permittedRepository.save(belongsTo);
	}

	public Permitted delete(Long id) {
		
		 Optional<Permitted> belongsTo = 
				 permittedRepository.findById(id);
		 permittedRepository.deleteById(id);
		 return belongsTo.get();
		// TODO Auto-generated method stub
	}

	public Permitted update(Long id, Permitted 
											belongsTo) {
		
		// TODO Auto-generated method stub
		permittedRepository.save(belongsTo);
		Optional<Permitted> belongsToobj = 
				permittedRepository.findById(id);
		return belongsToobj.get();
	}

	@Transactional(readOnly = true)
	public Map<String, Object> getAllPermittedState(int limit) {
		
		// TODO Auto-generated method stub
		Collection<Domain> domains = permittedRepository.graph(limit);
		List<Map<String, Object>> nodes = new ArrayList<>();
		List<Map<String, Object>> rels = new ArrayList<>();
		int i = 0;
		Iterator<Domain> result = domains.iterator();
		while (result.hasNext()) {
			Domain domaingrp = result.next();
			//nodes.add(map("type", domain.getType(), "id",domain.getId(),"label", "domain"));
			int target = i;
			i++;
			for (Permitted splTxn : domaingrp.getPermittedStates()) {
				Map<String, Object> output = new HashMap<String, Object>(2);
				output.put("name", splTxn.getName());
				output.put("id", splTxn.getId());
				int source = nodes.indexOf(output);
				if (source == -1) {
					nodes.add(output);
					source = i++;
				}
			}
		}
		Map<String,Object> finaloutput = new HashMap<String, Object>(2);
		finaloutput.put("nodes", nodes);
			return finaloutput;
		}

	public Collection<Permitted> findWithName(String name) {
		 // TODO Auto-generated method stub
		System.out.println("Searching for all Special transactions with name");
		Collection<Domain> retrievedDomainsgrp = 
				permittedRepository.graph(100);
		List<Permitted> allBelongsTo = new ArrayList<>();
		System.out.println("Total number = " + retrievedDomainsgrp.size());		 
		Iterator<Domain> result = retrievedDomainsgrp.iterator();
		while (result.hasNext()) {
			Domain domaingrp = result.next();					
			for (Permitted belongsto : domaingrp.getPermittedStates()) {
							
				if(belongsto.getName().equals(name)) {
				    		 
					allBelongsTo.add(belongsto);
				    		 
				}
			}
					
		}
		return  allBelongsTo;

	}
	
	
	
}
