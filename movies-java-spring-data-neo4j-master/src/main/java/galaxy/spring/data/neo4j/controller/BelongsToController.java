package galaxy.spring.data.neo4j.controller;

import java.net.URI;
import java.util.Collection;
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

import galaxy.spring.data.neo4j.domain.BelongsTo;
import galaxy.spring.data.neo4j.domain.Domain;
import galaxy.spring.data.neo4j.domain.DomainUser;
import galaxy.spring.data.neo4j.domain.DomainUserGroup;
import galaxy.spring.data.neo4j.domain.Movie;
import galaxy.spring.data.neo4j.domain.SpecialTransaction;
import galaxy.spring.data.neo4j.dto.RelationshipDTO;
import galaxy.spring.data.neo4j.services.BelongsToService;
import galaxy.spring.data.neo4j.services.DomainService;
import galaxy.spring.data.neo4j.services.SpecialTransactionService;
import galaxy.spring.data.neo4j.services.UserGroupService;
import galaxy.spring.data.neo4j.services.UserService;

/**
 * @author Mark Angrish
 * @author Michael J. Simons
 */
@RestController
@RequestMapping("/")
public class BelongsToController {

	private final BelongsToService belongsToService;
	private final UserGroupService usergroupService;
	private final UserService userService;
	
	public BelongsToController(BelongsToService domainService,
			UserGroupService usergroupService,UserService userService) {
		this.belongsToService = domainService;
		this.usergroupService = usergroupService;
		this.userService = userService;
	}

    @GetMapping("/belongsto/graph")
	public Map<String, Object> graph(@RequestParam(value = "limit",
						required = false) Integer limit) {
		return belongsToService.graph1(limit == null ? 100 : limit);
	}
    
    @GetMapping(value="/belongsto")
    public @ResponseBody  Map<String, Object> findAllSpecialTransaction
    (@RequestParam(value = "limit",	required = false) Integer limit) {

        return
        	belongsToService.getAllBelongsTo(limit == null ? 100 : limit);        
        
    }  
    
   /* @PostMapping("/belongsto/removeall")
	public ResponseEntity<String> deleteallBelongsTo(@RequestBody 
			RelationshipDTO		belongsToDat) {
		
		System.out.println(" SpecialTransaction request to delete all with name " + belongsToDat.getName());
		Collection<BelongsTo> belongsToc = 
				belongsToService.findWithName(belongsToDat.getName());
		for(BelongsTo belongsTo : belongsToc) {
			System.out.println("Deleting spcl txn for id " + belongsTo.getId());
			belongsToService.delete(belongsTo.getId());
		}
		return ResponseEntity.ok("Success");
	}*/
    
	@PostMapping("/belongsto")
	public ResponseEntity<Object> createBelongsTo(@RequestBody 
			BelongsTo
		belongsTo) {
		System.out.println("Object created with " + 
		belongsTo.getDomainUser().getName() + " group " + 
				belongsTo.getDomainUserGroup().getName());
		BelongsTo specialtransactionc = 
				belongsToService.save(belongsTo);
		URI location = ServletUriComponentsBuilder.
		fromCurrentRequest().path("/{id}").buildAndExpand(specialtransactionc.
				getId()).toUri();
		return ResponseEntity.created(location).build();
	}
	
	@PostMapping("/belongstofrom")
	public ResponseEntity<Object> createBelongsTo2(@RequestBody 
			RelationshipDTO
		belongsTo) {
		DomainUserGroup userGroup = usergroupService.findByName(belongsTo.
				getStartnodeid());
		DomainUser duser = userService.findByName(belongsTo.getEndnodeid());
		BelongsTo objbelongsto = new BelongsTo(userGroup,duser);
		System.out.println("Creating relation " + belongsTo);
		BelongsTo specialtransactionc = 
				belongsToService.save(objbelongsto);
		URI location = ServletUriComponentsBuilder.
		fromCurrentRequest().path("/{id}").buildAndExpand(specialtransactionc.
				getId()).toUri();
		return ResponseEntity.created(location).build();
	}
	
	@DeleteMapping("/belongsto/{id}")
	public ResponseEntity<?> deleteBelongsTo(@PathVariable Long id) {

		if (null == belongsToService.delete(id)) {
			return new ResponseEntity<String>("No Customer found for ID " + id, 
					HttpStatus.NOT_FOUND);
		}

		return new ResponseEntity<Long>(id, HttpStatus.OK);

	}

	@PutMapping("/belongsto/{id}")
	public ResponseEntity<?> updatebelongsTo(@PathVariable Long id, 
			@RequestBody BelongsTo belongsTo) {

		belongsTo = belongsToService.update(id, 
							 belongsTo);

		if (null == belongsTo) {
			return new ResponseEntity<String>("No Customer found for ID " + id, 
					HttpStatus.NOT_FOUND);
		}

		return new ResponseEntity<BelongsTo>(belongsTo,
				HttpStatus.OK);
	}
}
