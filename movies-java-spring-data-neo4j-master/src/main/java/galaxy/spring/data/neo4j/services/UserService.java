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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import galaxy.spring.data.neo4j.domain.BelongsTo;
import galaxy.spring.data.neo4j.domain.Domain;
import galaxy.spring.data.neo4j.domain.DomainUser;
import galaxy.spring.data.neo4j.domain.DomainUserGroup;
import galaxy.spring.data.neo4j.domain.SpecialTransaction;
import galaxy.spring.data.neo4j.dto.RequestParamDTO;
import galaxy.spring.data.neo4j.repositories.UserRepository;

@Service
public class UserService {

    private final static Logger LOG = 
    		LoggerFactory.getLogger(UserService.class);

	private final UserRepository userRepository;
	private final UserGroupService usergroupService;
    private final PasswordEncoder passwordEncoder;
	
	public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder,UserGroupService ugs) {
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
		this.usergroupService = ugs;
	}

	private Map<String, Object> toD3Format(Collection<DomainUser> domainus) {
		List<Map<String, Object>> nodes = new ArrayList<>();
		List<Map<String, Object>> rels = new ArrayList<>();
		int i = 0;
		Iterator<DomainUser> result = domainus.iterator();
		while (result.hasNext()) {
			DomainUser domainuser = result.next();
			nodes.add(map("name", domainuser.getName(), "id",domainuser.getId(),"label", 
					"domainuser"));
			int target = i;
			i++;
			DomainUserGroup belongsto = domainuser.getBelongsTo();
			Map<String, Object> actor = map("name",belongsto.getName(),
					"Some","Some", "label", "domainusergroup");
			int source = nodes.indexOf(actor);
			if (source == -1) {
				nodes.add(actor);
				source = i++;
			}
			rels.add(map("source", source,"some","some", "target", target));
			
		}
		return map("nodes", nodes, "some","some","links", rels);
	}

	private Map<String, Object> map(String key1, Object value1, String key2, 
			Object value2,String key3, Object value3) {
		Map<String, Object> result = new HashMap<String, Object>(2);
		result.put(key1, value1);
		result.put(key2, value2);
		result.put(key3, value3);
		return result;
	}

    
	@Transactional(readOnly = true)
	public Map<String, Object>  graph1(int limit) {
		Collection<DomainUser> result = userRepository.findAllUsers();
		return toD3Format(result);
	}

	@Transactional
	public DomainUser save(DomainUser domainug) {
		// TODO Auto-generated method stub
		String passwd  = domainug.getPassword();
		String encode = passwordEncoder.encode(passwd);
		domainug.setPassword(encode);
		System.out.println("Password hash is " + encode);
		return userRepository.save(domainug);
	}

		
	public DomainUser delete(Long id) {
		
		 Optional<DomainUser> domainuObj = userRepository.findById(id);
		 userRepository.deleteById(id);
		 return domainuObj.get();
		// TODO Auto-generated method stub
	}

	public DomainUser update(Long id, DomainUser domainus) {
		
		// TODO Auto-generated method stub
		userRepository.save(domainus);
		Optional<DomainUser> domainusObj = userRepository.findById(id);
		 return domainusObj.get();
	}
	
	@Transactional(readOnly = true)
    public DomainUser findByName(String name) {
		DomainUser result = userRepository.findByName(name);
        return result;
    }
	
	@Transactional(readOnly = true)
    public DomainUser findByID(Long id) {
		Optional<DomainUser> findById = userRepository.findById(id);
		if(findById.isPresent()) {
			return findById.get();
		}
		else
			return null;
    }
	
	public Map<String, Object> getAllUsers1() {
		List<Map<String, Object>> startnodes = new ArrayList<>();
		List<Map<String, Object>> endnodes = new ArrayList<>();
		List<Map<String, Object>> rels = new ArrayList<>();
		int i = 0;int length = 0;int ocounter;
		Collection<DomainUser> domainuser =  userRepository.findAllUsers();
		Iterator<DomainUser> result = domainuser.iterator();
		String[] txnNames1,txnNames2,txnNames3,txnNames;
		Object[] txnObject1,txnObject2 ,txnObject3 ,txnObject;
		while (result.hasNext()) {
			length++;
			result.next();
		}
		result = domainuser.iterator();
		txnNames1 = new String[4];	txnObject1 = new Object[4];
		txnNames2 = new String[2];	txnObject2 = new Object[2];
		txnNames3 = new String[2];	txnObject3 = new Object[2];
		txnNames = new String[3];	txnObject = new Object[3];
		System.out.println("Number of domain users " + length);
		while (result.hasNext()) {
			ocounter =0;
			DomainUser domainuserf = result.next();			
			txnNames1[ocounter] = "name";	txnObject1[ocounter++] = domainuserf.getName();
			txnNames1[ocounter] = "id";	txnObject1[ocounter++] = domainuserf.getId();
			txnNames1[ocounter] = "firstName";	txnObject1[ocounter++] = domainuserf.getFirstName();
			txnNames1[ocounter] = "lastName";	txnObject1[ocounter++] = domainuserf.getLastName();

			startnodes.add(map1(txnNames1,txnObject1));
			int icounter1 = 0,icounter2 = 0;
				//DomainUserGroup belTo = domainuserf.getBelongsTo();
				DomainUserGroup belTo = usergroupService.findByName(domainuserf.getDusergroup());
				txnNames2[icounter1] = "name";	txnObject2[icounter1++] = belTo.getName();
				txnNames2[icounter1] = "label";	txnObject2[icounter1++] = "BelongsTo";
				txnNames3[icounter2] = "dgname";	
				txnObject3[icounter2++] = belTo.getName();
				txnNames3[icounter2] = "label";	txnObject3[icounter2++] = "domainusergroup";
			
			rels.add(map1(txnNames2,txnObject2));
			endnodes.add(map1(txnNames3,txnObject3));
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
		return map1(txnNames,txnObject);
	}
	
	private Map<String, Object> map1(String[] keys, Object[] values) {
		Map<String, Object> result = new HashMap<String, Object>();
		int vPtr = 0;
		for(String key : keys)
			result.put(key, values[vPtr++]);
		return result;
	}
	
	
	public DomainUser getUsers(String id) {
		
		Collection<DomainUser> domainus = userRepository.findAllUsers();
		for(DomainUser domainu : domainus) {
			if(domainu.getId().equals(Long.valueOf(id))) {
				return domainu;
			}
		}
		return null;
	}
}

