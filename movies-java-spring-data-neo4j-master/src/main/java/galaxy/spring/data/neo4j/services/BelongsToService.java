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

import galaxy.spring.data.neo4j.domain.BelongsTo;
import galaxy.spring.data.neo4j.domain.Domain;
import galaxy.spring.data.neo4j.domain.DomainUserGroup;
import galaxy.spring.data.neo4j.domain.SpecialTransaction;
import galaxy.spring.data.neo4j.repositories.BelongsToRepository;

@Service
public class BelongsToService {

    private final static Logger LOG = LoggerFactory.
    		getLogger(BelongsToService.class);

	private final BelongsToRepository belongsToRepository;
	public BelongsToService(BelongsToRepository 
			belongsToRepository) {
		this.belongsToRepository = belongsToRepository;
	}

	/*private Map<String, Object> toD3Format(Collection<DomainUserGroup> domainsug) {
		List<Map<String, Object>> startnodes = new ArrayList<>();
		List<Map<String, Object>> endnodes = new ArrayList<>();
		List<Map<String, Object>> rels = new ArrayList<>();
		int i = 0;int length = 0;int ocounter =0;
		Iterator<DomainUserGroup> result = domainsug.iterator();
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
			DomainUserGroup domainug = result.next();			
			txnNames1[ocounter] = "id";	txnObject1[ocounter++] = domainug.getId();
			txnNames1[ocounter] = "label";txnObject1[ocounter++] = "domainusergroup";
			startnodes.add(map(txnNames1,txnObject1));
			int icounter1 = 0,icounter2 = 0;
			for (BelongsTo belTo : domainug.getBelongsTo()) {
				txnNames2[icounter1] = "name";	txnObject2[icounter1++] = belTo.getName();
				txnNames2[icounter1] = "label";	txnObject2[icounter1++] = "belongsTo";
				txnNames3[icounter2] = "name";	
				txnObject3[icounter2++] = belTo.getDomainUser().getName();
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
	} */

	private Map<String, Object> map(String[] keys, Object[] values) {
		Map<String, Object> result = new HashMap<String, Object>();
		int vPtr = 0;
		for(String key : keys)
			result.put(key, values[vPtr++]);
		return result;
	}

    
	@Transactional(readOnly = true)
	public Map<String, Object>  graph1(int limit) {
		Collection<DomainUserGroup> result = belongsToRepository.graph(limit);
		//return toD3Format(result);
		return null;
	}


	@Transactional
	public BelongsTo save(BelongsTo belongsTo) {
		// TODO Auto-generated method stub
		return belongsToRepository.save(belongsTo);
	}

	public BelongsTo delete(Long id) {
		
		 Optional<BelongsTo> belongsTo = 
				 belongsToRepository.findById(id);
		 belongsToRepository.deleteById(id);
		 return belongsTo.get();
		// TODO Auto-generated method stub
	}

	public BelongsTo update(Long id, BelongsTo 
											belongsTo) {
		
		// TODO Auto-generated method stub
		belongsToRepository.save(belongsTo);
		Optional<BelongsTo> belongsToobj = 
				belongsToRepository.findById(id);
		return belongsToobj.get();
	}

	@Transactional(readOnly = true)
	public Map<String, Object> getAllBelongsTo(int limit) {
		
		// TODO Auto-generated method stub
		Collection<DomainUserGroup> domains = belongsToRepository.graph(limit);
		List<Map<String, Object>> nodes = new ArrayList<>();
		List<Map<String, Object>> rels = new ArrayList<>();
		int i = 0;
		Iterator<DomainUserGroup> result = domains.iterator();
		while (result.hasNext()) {
			DomainUserGroup domaingrp = result.next();
			//nodes.add(map("type", domain.getType(), "id",domain.getId(),"label", "domain"));
			int target = i;
			i++;
			for (SpecialTransaction splTxn : domaingrp.getSpecialTransaction()) {
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

	/*public Collection<BelongsTo> findWithName(String name) {
		 // TODO Auto-generated method stub
		System.out.println("Searching for all Special transactions with name");
		Collection<DomainUserGroup> retrievedDomainsgrp = 
				belongsToRepository.graph(100);
		List<BelongsTo> allBelongsTo = new ArrayList<>();
		System.out.println("Total number = " + retrievedDomainsgrp.size());		 
		Iterator<DomainUserGroup> result = retrievedDomainsgrp.iterator();
		while (result.hasNext()) {
			DomainUserGroup domaingrp = result.next();					
			for (BelongsTo belongsto : domaingrp.getBelongsTo()) {
							
				if(belongsto.getName().equals(name)) {
				    		 
					allBelongsTo.add(belongsto);
				    		 
				}
			}
					
		}
		return  allBelongsTo;

	}*/
	
	
	
}
