package galaxy.spring.data.neo4j.controller;

import java.net.URI;
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

import galaxy.spring.data.neo4j.domain.AllowedTransition;
import galaxy.spring.data.neo4j.domain.BelongsTo;
import galaxy.spring.data.neo4j.domain.Domain;
import galaxy.spring.data.neo4j.domain.DomainUser;
import galaxy.spring.data.neo4j.domain.DomainUserGroup;
import galaxy.spring.data.neo4j.domain.EndState;
import galaxy.spring.data.neo4j.domain.Movie;
import galaxy.spring.data.neo4j.domain.SpecialTransaction;
import galaxy.spring.data.neo4j.domain.StartState;
import galaxy.spring.data.neo4j.domain.State;
import galaxy.spring.data.neo4j.dto.SpecialTransactionDTO;
import galaxy.spring.data.neo4j.dto.RelationshipDTO;
import galaxy.spring.data.neo4j.dto.ResponseParamDTO;
import galaxy.spring.data.neo4j.services.AllowedTransitionService;
import galaxy.spring.data.neo4j.services.DomainService;
import galaxy.spring.data.neo4j.services.SpecialTransactionService;
import galaxy.spring.data.neo4j.services.StateService;
import galaxy.spring.data.neo4j.services.UserGroupService;
import galaxy.spring.data.neo4j.services.UserService;

/**
 * @author Mark Angrish
 * @author Michael J. Simons
 */
@RestController
@RequestMapping("/")
public class AllowedTransitionController {

	private final AllowedTransitionService allowedTransitionService;
	private final StateService stateService;

	
	public AllowedTransitionController(AllowedTransitionService 
			allowedTransitionService,StateService stateService) {
		this.allowedTransitionService = allowedTransitionService;
		this.stateService = stateService;
	}

    @GetMapping("/galaxy/appadmin/allowedtransition/graph")
	public Map<String, Object> graph(@RequestParam(value = "limit",
						required = false) Integer limit) {
		return allowedTransitionService.graph1(limit == null ? 100 : limit);
	}
    
    @GetMapping(value="/galaxy/appadmin/allowedtransition")
    public @ResponseBody List<ResponseParamDTO> findAllAllowedTransition() {

        return
        	allowedTransitionService.getAllAllowedwithrels();     
        
    }  
    
	@PostMapping("/galaxy/appadmin/allowedtransition/removeall")
	public ResponseEntity<String> deleteallAllowedTrn(@RequestBody 
			RelationshipDTO		allowdTransition) {
		
		System.out.println(" AllowedTransition request to delete all with name " + allowdTransition.getName());
		Collection<AllowedTransition> allowedTrnsc = 
				allowedTransitionService.findWithName(allowdTransition.getName());
		for(AllowedTransition allowedTrns : allowedTrnsc) {
			System.out.println("Deleting spcl txn for id " + allowedTrns.getId());
			allowedTransitionService.delete(allowedTrns.getId());
		}
		return ResponseEntity.ok("Success");
	}
	
	@DeleteMapping("/galaxy/appadmin/allowedtransition/{id}")
	public ResponseEntity<?> deleteAllwdTrn(@PathVariable Long id) {

		if (null == allowedTransitionService.delete(id)) {
			return new ResponseEntity<String>("No Customer found for ID " + id, 
					HttpStatus.NOT_FOUND);
		}

		return new ResponseEntity<Long>(id, HttpStatus.OK);

	}

	@PutMapping("/galaxy/appadmin/allowedtransition/{id}")
	public ResponseEntity<?> updateSpclTxn(@PathVariable Long id, 
			@RequestBody AllowedTransition allowedTrnsition) {

		allowedTrnsition = allowedTransitionService.update(id, 
				allowedTrnsition);

		if (null == allowedTrnsition) {
			return new ResponseEntity<String>("No Customer found for ID " + id, 
					HttpStatus.NOT_FOUND);
		}

		return new ResponseEntity<AllowedTransition>(allowedTrnsition,
				HttpStatus.OK);
	}
	
	@PostMapping("/galaxy/appadmin/allowedtransition")
	public ResponseEntity<Object> createAllowedTransition(@RequestBody 
			RelationshipDTO		allowedTransition) {
		List<AllowedTransition> allowedTrn = 
				allowedTransitionService.findWithName(allowedTransition.getName());
		AllowedTransition allowedTrnsc = null;
		if(allowedTrn.size() == 0) {
			StartState startstate = stateService.findStartStateByName(allowedTransition.
					getStartnodeid());
			System.out.println("Start state is " + startstate.getName());
			EndState endstate = 
					stateService.findEndStateByName(allowedTransition.getEndnodeid());
			System.out.println("End state is " + endstate.getName());
			AllowedTransition allwdTxn = 
			new AllowedTransition(startstate,endstate,allowedTransition.getName(),
					allowedTransition.getLabel1(),allowedTransition.getLabel2());
			System.out.println("Creating relation " + allwdTxn);
			allowedTrnsc = 
					allowedTransitionService.save(allwdTxn);
		}else {
			AllowedTransition trn = allowedTrn.get(0);
			if(!(trn.getspclForDomain(allowedTransition.getLabel2()) == null)) {
				trn.addspclForDomain(allowedTransition.getLabel2(),allowedTransition.getLabel1());
				allowedTrnsc = 
					allowedTransitionService.save(trn);
			}
				
		}
		URI location = ServletUriComponentsBuilder.
		fromCurrentRequest().path("/{id}").buildAndExpand(allowedTrnsc.
				getId()).toUri();
		return ResponseEntity.created(location).build();
	}
}
