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
import galaxy.spring.data.neo4j.domain.Movie;
import galaxy.spring.data.neo4j.services.DomainService;

/**
 * @author Mark Angrish
 * @author Michael J. Simons
 */
@RestController
@RequestMapping("/")
public class DomainController {

	private final DomainService domainService;
	
	public DomainController(DomainService domainService) {
		this.domainService = domainService;
	}

    @GetMapping("/galaxy/appadmin/domain/graph")
	public Map<String, Object> graph(@RequestParam(value = "limit",required = false) Integer limit) {
		return domainService.graph1(limit == null ? 100 : limit);
	}
    
    @GetMapping(value="/galaxy/appadmin/domain")
    public @ResponseBody List<Domain> findAllDomains() {

        List<Domain> domains = domainService.getDomains();        
        return domains;
    }   
    
	@PostMapping("/galaxy/appadmin/domain")
	public ResponseEntity<Object> createDomain(@RequestBody Domain domain) {
		System.out.println(" Domain request is "+ domain.getName());
		Domain domainc = domainService.save(domain);
		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").
						buildAndExpand(domainc.getId()).toUri();
		return ResponseEntity.created(location).build();
	}
	
	@DeleteMapping("/galaxy/appadmin/domain/{id}")
	public ResponseEntity<?> deleteDomain(@PathVariable String id) {
		Domain domaintodelete = domainService.findByID(Long.valueOf(id));
		if (domaintodelete == null) {
			return new ResponseEntity<String>("No Domain found for ID " + id, HttpStatus.NOT_FOUND);
		}else {
			domainService.delete(domaintodelete.getId());
		}
			

		return new ResponseEntity<Long>(domaintodelete.getId(), HttpStatus.OK);

	}

	@PutMapping("/galaxy/appadmin/domain/{id}")
	public ResponseEntity<?> updateCustomer(@PathVariable Long id, @RequestBody Domain domain) {

		domain = domainService.update(id, domain);

		if (null == domain) {
			return new ResponseEntity<String>("No Customer found for ID " + id, HttpStatus.NOT_FOUND);
		}

		return new ResponseEntity<Domain>(domain, HttpStatus.OK);
	}
}
