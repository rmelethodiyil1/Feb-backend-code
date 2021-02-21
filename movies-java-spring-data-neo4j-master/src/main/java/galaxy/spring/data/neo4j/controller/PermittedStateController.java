package galaxy.spring.data.neo4j.controller;

import java.net.URI;
import java.util.Collection;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
import galaxy.spring.data.neo4j.domain.Permitted;
import galaxy.spring.data.neo4j.domain.State;
import galaxy.spring.data.neo4j.dto.RelationshipDTO;
import galaxy.spring.data.neo4j.services.DomainService;
import galaxy.spring.data.neo4j.services.PermittedService;
import galaxy.spring.data.neo4j.services.StateService;

/**
 * @author Mark Angrish
 * @author Michael J. Simons
 */
@RestController
@RequestMapping("/")
public class PermittedStateController {

	private final PermittedService permittedService;
	private final DomainService domainService;
	private final StateService stateService;
	
	public PermittedStateController(PermittedService permittedService,
			DomainService domainService,StateService stateService) {
		this.permittedService = permittedService;
		this.domainService = domainService;
		this.stateService = stateService;
	}

    @GetMapping("/galaxy/appadmin/permitted/graph")
	public Map<String, Object> graph(@RequestParam(value = "limit",
						required = false) Integer limit) {
		return permittedService.graph1(limit == null ? 100 : limit);
	}
    
    @GetMapping(value="/galaxy/appadmin/permitted")
    public @ResponseBody  Map<String, Object> findAllPermittedState
    (@RequestParam(value = "limit",	required = false) Integer limit) {

        return
        	permittedService.getAllPermittedState(limit == null ? 100 : limit);        
        
    }  
    
    @PostMapping("/galaxy/appadmin/permitted/removeall")
	public ResponseEntity<String> deleteallPermittedState(@RequestBody 
			RelationshipDTO		permittedDat) {
		
		System.out.println(" SpecialTransaction request to delete all with name " + permittedDat.getName());
		Collection<Permitted> permittedToc = 
				permittedService.findWithName(permittedDat.getName());
		for(Permitted permittedTo : permittedToc) {
			System.out.println("Deleting spcl txn for id " + permittedTo.getId());
			permittedService.delete(permittedTo.getId());
		}
		return ResponseEntity.ok("Success");
	}
    
		
	@PostMapping("/galaxy/appadmin/permittedto")
	public ResponseEntity<Object> createBelongsTo2(@RequestBody 
			RelationshipDTO		permittedto) {
		Domain domain = domainService.findByName(permittedto.
				getStartnodeid());
		State duser = stateService.findByName(permittedto.getEndnodeid());
		Permitted objpermittedto = new Permitted(domain,duser);
		System.out.println("Creating relation " + permittedto);
		Permitted specialtransactionc = 
				permittedService.save(objpermittedto);
		URI location = ServletUriComponentsBuilder.
		fromCurrentRequest().path("/{id}").buildAndExpand(specialtransactionc.
				getId()).toUri();
		return ResponseEntity.created(location).build();
	}
	
	@DeleteMapping("/galaxy/appadmin/permitted/{id}")
	public ResponseEntity<?> deleteBelongsTo(@PathVariable Long id) {

		if (null == permittedService.delete(id)) {
			return new ResponseEntity<String>("No Customer found for ID " + id, 
					HttpStatus.NOT_FOUND);
		}

		return new ResponseEntity<Long>(id, HttpStatus.OK);

	}

	@PutMapping("/galaxy/appadmin/permitted/{id}")
	public ResponseEntity<?> updatebelongsTo(@PathVariable Long id, 
			@RequestBody Permitted belongsTo) {

		belongsTo = permittedService.update(id, 
							 belongsTo);

		if (null == belongsTo) {
			return new ResponseEntity<String>("No Customer found for ID " + id, 
					HttpStatus.NOT_FOUND);
		}

		return new ResponseEntity<Permitted>(belongsTo,
				HttpStatus.OK);
	}
}
