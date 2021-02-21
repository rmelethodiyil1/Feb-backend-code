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

import galaxy.spring.data.neo4j.domain.Domain;
import galaxy.spring.data.neo4j.domain.DomainUser;
import galaxy.spring.data.neo4j.domain.SpecialTransaction;
import galaxy.spring.data.neo4j.repositories.DomainRepository;

@Service
public class DomainService {

    private final static Logger LOG = LoggerFactory.getLogger(DomainService.class);

	private final DomainRepository domainRepository;
	public DomainService(DomainRepository domainRepository) {
		this.domainRepository = domainRepository;
	}

	private Map<String, Object> toD3Format(Collection<Domain> domains) {
		List<Map<String, Object>> nodes = new ArrayList<>();
		List<Map<String, Object>> rels = new ArrayList<>();
		int i = 0;
		Iterator<Domain> result = domains.iterator();
		while (result.hasNext()) {
			Domain domain = result.next();
			nodes.add(map("type", domain.getType(), "id",domain.getId(),"label", "domain"));
			int target = i;
			i++;
			for (SpecialTransaction splTxn : domain.getSpecialTransaction()) {
				Map<String, Object> actor = map("name", splTxn.getDomainUserGroup().
						getName(),"Some","Some", "label", "domainusergroup");
				int source = nodes.indexOf(actor);
				if (source == -1) {
					nodes.add(actor);
					source = i++;
				}
				rels.add(map("source", source,"some","some", "target", target));
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
		Collection<Domain> result = domainRepository.graph(limit);
		return toD3Format(result);
	}

	@Transactional
	public Domain save(Domain domain) {
		// TODO Auto-generated method stub
		return domainRepository.save(domain);
	}

	public Domain delete(Long id) {
		
		 Optional<Domain> domainObj = domainRepository.findById(id);
		 domainRepository.deleteById(id);
		 return domainObj.get();
		// TODO Auto-generated method stub
	}

	public Domain update(Long id, Domain domain) {
		
		// TODO Auto-generated method stub
		domainRepository.save(domain);
		Optional<Domain> domainObj = domainRepository.findById(id);
		 return domainObj.get();
	}

	public List<Domain> getDomains() {
		// TODO Auto-generated method stub
		Collection<Domain> domains = domainRepository.findAllDomains();
		ArrayList<Domain> domainslist = new ArrayList<Domain>();
		for(Domain domain : domains) {
			domainslist.add(domain);
		}
		return domainslist;
	}
	
	@Transactional(readOnly = true)
    public Domain findByName(String name) {
		Collection<Domain> domains = domainRepository.findAllDomains();
		for(Domain domain : domains) {
			System.out.println("searching for domain " + domain.getName() + " in " + name);
			if(domain.getName().equals(name)) {
				System.out.println("matching for domain " + name);
				return domain;
			}
		}
		return null;
    }
	
	@Transactional(readOnly = true)
    public Domain findByID(Long id) {
		Optional<Domain> findById = domainRepository.findById(id);
		if(findById.isPresent()) {
			return findById.get();
		}
		else
			return null;
    }
}
