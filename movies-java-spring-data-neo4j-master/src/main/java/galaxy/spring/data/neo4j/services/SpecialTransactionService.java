package galaxy.spring.data.neo4j.services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import galaxy.spring.data.neo4j.domain.Domain;
import galaxy.spring.data.neo4j.domain.DomainUserGroup;
import galaxy.spring.data.neo4j.domain.SpecialTransaction;
import galaxy.spring.data.neo4j.dto.RelationshipDTO;
import galaxy.spring.data.neo4j.dto.RequestParamDTO;
import galaxy.spring.data.neo4j.dto.ResponseParamDTO;
import galaxy.spring.data.neo4j.repositories.SpecialTransactionRepository;

@Service
public class SpecialTransactionService {

    private final static Logger LOG = LoggerFactory.
    		getLogger(SpecialTransactionService.class);

	private final SpecialTransactionRepository specialTransactionRepository;
	private final DomainService domainservice;
	private final UserGroupService ugservice;
	
	public SpecialTransactionService(SpecialTransactionRepository 
			specialTransactionRepository,DomainService domainservice,UserGroupService ugservice) {
		this.specialTransactionRepository = specialTransactionRepository;
		this.domainservice = domainservice;
		this.ugservice = ugservice;
	}

	private Map<String, Object> toD3Format(Collection<Domain> domains) {
		List<Map<String, Object>> startnodes = new ArrayList<>();
		List<Map<String, Object>> endnodes = new ArrayList<>();
		List<Map<String, Object>> rels = new ArrayList<>();
		int i = 0;int length = 0;int ocounter =0;
		Iterator<Domain> result = domains.iterator();
		String[] txnNames1,txnNames2,txnNames3,txnNames;
		Object[] txnObject1,txnObject2 ,txnObject3 ,txnObject;
		while (result.hasNext()) {
			length++;
			result.next();
		}
		result = domains.iterator();
		txnNames1 = new String[3];	txnObject1 = new Object[3];
		txnNames2 = new String[2];	txnObject2 = new Object[2];
		txnNames3 = new String[2];	txnObject3 = new Object[2];
		txnNames = new String[3];	txnObject = new Object[3];
		System.out.println("Number of domains " + length);
		while (result.hasNext()) {
			Domain domain = result.next();			
			txnNames1[ocounter] = "type";	txnObject1[ocounter++] = domain.getType();
			txnNames1[ocounter] = "id";	txnObject1[ocounter++] = domain.getId();
			txnNames1[ocounter] = "label";txnObject1[ocounter++] = "domain";
			startnodes.add(map(txnNames1,txnObject1));
			int icounter1 = 0,icounter2 = 0;
			for (SpecialTransaction splTxn : domain.getSpecialTransaction()) {
				txnNames2[icounter1] = "name";	txnObject2[icounter1++] = splTxn.getName();
				txnNames2[icounter1] = "label";	txnObject2[icounter1++] = "specialtransaction";
				txnNames3[icounter2] = "name";	
				txnObject3[icounter2++] = splTxn.getDomainUserGroup().getName();
				txnNames3[icounter2] = "label";	txnObject3[icounter2++] = "domainusergroup";
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
		Collection<Domain> result = specialTransactionRepository.graph(limit);
		return toD3Format(result);
	}


	@Transactional
	public SpecialTransaction save(SpecialTransaction specialTransaction) {
		// TODO Auto-generated method stub
		return specialTransactionRepository.save(specialTransaction);
	}
	
	@Transactional
	public SpecialTransaction delete(Long id) {
		
		 Optional<SpecialTransaction> specialTransaction = 
				 specialTransactionRepository.findById(id);
		 System.out.println("Deleting Spcl txn " + specialTransaction.get().getName());
		 specialTransactionRepository.deleteById(id);
		 return specialTransaction.get();
		// TODO Auto-generated method stub
	}

	public SpecialTransaction getSpclbyId(Long id, SpecialTransaction 
											specialTransaction) {
		
		// TODO Auto-generated method stub
		specialTransactionRepository.save(specialTransaction);
		Optional<SpecialTransaction> specialTransactionobj = 
				specialTransactionRepository.findById(id);
		return specialTransactionobj.get();
	}
	
	@Transactional(readOnly = true)
	public List<RequestParamDTO> getAllSpclTxn(int limit) {
		ArrayList<RequestParamDTO> allspcl = new ArrayList<RequestParamDTO>();
		Collection<Domain> domains = specialTransactionRepository.graph(limit);
		List<Map<String, Object>> nodes = new ArrayList<>();
		List<Map<String, Object>> rels = new ArrayList<>();
		int i = 0;
		Iterator<Domain> result = domains.iterator();
		while (result.hasNext()) {
			Domain domain = result.next();
			Map<String, Object> outputd = new HashMap<String, Object>(2);
			outputd.put("name", domain.getName());
			outputd.put("id", domain.getId());
			nodes.add(outputd);
			i++;
			for (SpecialTransaction splTxn : domain.getSpecialTransaction()) {
				Map<String, Object> output = new HashMap<String, Object>(2);
				output.put("name", splTxn.getName());
				//output.put("id", splTxn.getId());
				//output.put("domain",splTxn.getDomain().getName());
				//output.put("domainusergroup",splTxn.getDomainUserGroup().getName());
				int source = nodes.indexOf(output);
				if (source == -1) {
					nodes.add(output);
					allspcl.add(new RequestParamDTO(
							"",	"",(String)output.get("name")));
					source = i++;
				}
				
			}
		}
		Map<String,Object> finaloutput = new HashMap<String, Object>(2);
		return allspcl;
	}
			

	@Transactional(readOnly = true)
	public Map<String, Object> getAllSpclTxnwithrels(int limit,String filtername) {
		// TODO Auto-generated method stub
		Collection<Domain> domains = specialTransactionRepository.graph(limit);
		List<Map<String, Object>> nodes = new ArrayList<>();
		List<Map<String, Object>> rels = new ArrayList<>();
		int i = 0;
		Iterator<Domain> result = domains.iterator();
		while (result.hasNext()) {
			Domain domain = result.next();
			Map<String, Object> outputd = new HashMap<String, Object>(2);
			outputd.put("name", domain.getName());
			outputd.put("id", domain.getId());			
			int target = i;
			i++;
			for (SpecialTransaction splTxn : domain.getSpecialTransaction()) {
				
				int source = nodes.indexOf(outputd);
				if (source == -1 && splTxn.getName().equals(filtername)) {
					nodes.add(outputd);
					source = i++;
					Map<String, Object> outputug = new HashMap<String, Object>(2);
					outputug.put("name", splTxn.getDomainUserGroup().getName());
					outputug.put("id", splTxn.getDomainUserGroup().getId());
					rels.add(outputug);
				}
				
			}
		}
		Map<String,Object> finaloutput = new HashMap<String, Object>(2);
		finaloutput.put("nodes", nodes);
		finaloutput.put("rels", rels);
		return finaloutput;
	}

	public Collection<SpecialTransaction> findWithName(String name) {
		 // TODO Auto-generated method stub
		System.out.println("Searching for all Special transactions with name" + name);
		Collection<Domain> retrievedDomains = 
		specialTransactionRepository.graph(100);
		List<SpecialTransaction> allSpclTxn = new ArrayList<>();
		System.out.println("Total domain number = " + retrievedDomains.size());		 
		Iterator<Domain> result = retrievedDomains.iterator();
		while (result.hasNext()) {
			Domain domain = result.next();					
			for (SpecialTransaction splTxn : domain.getSpecialTransaction()) {
							
				if(splTxn.getName().equals(name)) {
				    		 
					allSpclTxn.add(splTxn);
					System.out.println("Total spcl now = " + allSpclTxn.size());		 
				}
			}
					
		}
		return  allSpclTxn;

	}

	public SpecialTransaction findForNameandUser(String specialTxnname,String domainname,String ugname) {
		
		Collection<SpecialTransaction> findforname = 
				specialTransactionRepository.findforname(domainname, ugname);
		for(SpecialTransaction spclTxn:findforname) {
			if(spclTxn.getName().equals(specialTxnname)) {
				return spclTxn;
			}
		}
		return null;
	}
	
	public List<ResponseParamDTO> getAllSPCLwithrels() {
		List<Domain> alldomains = domainservice.getDomains();
		List<DomainUserGroup> allDG = ugservice.getAllUsersGroup();
		List<ResponseParamDTO> allDTO = new ArrayList<ResponseParamDTO>();
		for(Domain givendom : alldomains) {
			for(DomainUserGroup givenDg : allDG) {
				Collection<SpecialTransaction> findforname = 
						specialTransactionRepository.findforname(givendom.getName(), givenDg.getName());
				for(SpecialTransaction spclid : findforname) {
					ResponseParamDTO aDTO = new ResponseParamDTO
							(spclid.getName(),givenDg.getName(),givendom.getName(),"domainusergroup","domain",spclid.getId());
					aDTO.setStatus(spclid.isAttachedTransition());
					aDTO.setLabel3(spclid.getAttachedToState());
					allDTO.add(aDTO);
				}
			}
			
		}
		Collections.sort(allDTO);
		return allDTO;
	}
	
	public List<ResponseParamDTO> getAllSPCLwithrels(String domainname, String usergroup) {
		
		List<ResponseParamDTO> allDTO = new ArrayList<ResponseParamDTO>();
		List<Domain> domains = domainservice.getDomains();
		List<DomainUserGroup> allUsersGroup = ugservice.getAllUsersGroup();
		for( Domain adomain : domains) {
			if(domainname.equals("all") || domainname.equals(adomain.getName())) {
				for(DomainUserGroup aduG: allUsersGroup) {
					if(usergroup.equals("all") || usergroup.equals(aduG.getName())) {
						Collection<SpecialTransaction> findforname = 
								specialTransactionRepository.findforname(adomain.getName(), aduG.getName());
						for(SpecialTransaction spclid : findforname) {
							ResponseParamDTO aDTO = new ResponseParamDTO
									(spclid.getName(),usergroup,domainname,"domainusergroup","domain",spclid.getId());
							aDTO.setStatus(spclid.isAttachedTransition());
							aDTO.setLabel3(spclid.getAttachedToState());
							allDTO.add(aDTO);
						}
					}

				}
			}
		}
			
		Collections.sort(allDTO);
		return allDTO;
	}
	
	@Transactional(readOnly = true)
    public SpecialTransaction findByID(Long id) {
		Optional<SpecialTransaction> findById = specialTransactionRepository.findById(id);
		if(findById.isPresent()) {
			return findById.get();
		}
		else
			return null;
    }
}
