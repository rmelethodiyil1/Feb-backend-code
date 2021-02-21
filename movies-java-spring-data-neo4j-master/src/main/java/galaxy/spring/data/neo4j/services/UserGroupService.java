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

import galaxy.spring.data.neo4j.domain.DomainUser;
import galaxy.spring.data.neo4j.domain.DomainUserGroup;
import galaxy.spring.data.neo4j.domain.Movie;
import galaxy.spring.data.neo4j.domain.SpecialTransaction;
import galaxy.spring.data.neo4j.repositories.UserGroupRepository;

@Service
public class UserGroupService {

    private final static Logger LOG = 
    		LoggerFactory.getLogger(UserGroupService.class);

	private final UserGroupRepository userGroupRepository;
	public UserGroupService(UserGroupRepository userGroupRepository) {
		this.userGroupRepository = userGroupRepository;
	}

	private Map<String, Object> toD3Format(Collection<DomainUserGroup> domainug) {
		List<Map<String, Object>> nodes = new ArrayList<>();
		List<Map<String, Object>> rels = new ArrayList<>();
		int i = 0;
		Iterator<DomainUserGroup> result = domainug.iterator();
		while (result.hasNext()) {
			DomainUserGroup domainugs = result.next();
			nodes.add(map("name", domainugs.getName(), "id",domainugs.getId(),"label", 
					"domain"));
			int target = i;
			i++;
			for (SpecialTransaction splTxn : domainugs.getSpecialTransaction()) {
				Map<String, Object> actor = map("name",splTxn.getDomainUserGroup().
						getName(),	"Some","Some", "label", "domainusergroup");
				int source = nodes.indexOf(actor);
				if (source == -1) {
					nodes.add(actor);
					source = i++;
				}
				rels.add(map("source", 1,"some","some", "target", target));
			}
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
		Collection<DomainUserGroup> result = userGroupRepository.graph(limit);
		return toD3Format(result);
	}

	@Transactional
	public DomainUserGroup save(DomainUserGroup domainug) {
		// TODO Auto-generated method stub
		return userGroupRepository.save(domainug);
	}

	public DomainUserGroup delete(Long id) {
		
		 Optional<DomainUserGroup> domainugObj = userGroupRepository.findById(id);
		 userGroupRepository.deleteById(id);
		 return domainugObj.get();
		// TODO Auto-generated method stub
	}

	public DomainUserGroup update(Long id, DomainUserGroup domainug) {
		
		// TODO Auto-generated method stub
		userGroupRepository.save(domainug);
		Optional<DomainUserGroup> domainObj = userGroupRepository.findById(id);
		 return domainObj.get();
	}
	
	@Transactional(readOnly = true)
    public DomainUserGroup findByName(String name) {
		DomainUserGroup result = userGroupRepository.findByName(name);
        return result;
    }

	@Transactional(readOnly = true)
    public DomainUserGroup findByID(Long id) {
		Optional<DomainUserGroup> findById = userGroupRepository.findById(id);
		if(findById.isPresent()) {
			return findById.get();
		}
		else
			return null;
    }
	
	public List<DomainUserGroup> getAllUsersGroup() {
		Collection<DomainUserGroup> domainusg = userGroupRepository.findAllUsersGroup();
		ArrayList<DomainUserGroup> domainsusglist = new ArrayList<DomainUserGroup>();
		for(DomainUserGroup domainug : domainusg) {
			domainsusglist.add(domainug);
		}
		return domainsusglist;
	}
	
	public DomainUserGroup getUsersGroup(String id) {
		Collection<DomainUserGroup> domainusg = userGroupRepository.findAllUsersGroup();
		DomainUserGroup retrvddomainUG = null;
		for(DomainUserGroup domainug : domainusg) {
			if(domainug.getId().equals(Long.valueOf(id))) {
				retrvddomainUG = domainug;
			}
		}
		return retrvddomainUG;
	}
}
