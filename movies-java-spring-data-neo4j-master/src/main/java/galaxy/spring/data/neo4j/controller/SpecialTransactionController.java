package galaxy.spring.data.neo4j.controller;

import java.net.URI;
import java.util.Collection;
import java.util.Iterator;
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

import galaxy.spring.data.neo4j.domain.BelongsTo;
import galaxy.spring.data.neo4j.domain.Domain;
import galaxy.spring.data.neo4j.domain.DomainUser;
import galaxy.spring.data.neo4j.domain.DomainUserGroup;
import galaxy.spring.data.neo4j.domain.Movie;
import galaxy.spring.data.neo4j.domain.SpecialTransaction;
import galaxy.spring.data.neo4j.dto.RelationshipDTO;
import galaxy.spring.data.neo4j.dto.RequestParamDTO;
import galaxy.spring.data.neo4j.dto.ResponseParamDTO;
import galaxy.spring.data.neo4j.dto.SpecialTransactionDTO;
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
public class SpecialTransactionController {

	private final SpecialTransactionService specialTransactionService;
	private final UserGroupService usergroupService;
	private final DomainService domainService;

	
	public SpecialTransactionController(SpecialTransactionService 
	specialTransactionService,DomainService domainService,UserGroupService usergrService) {
		this.specialTransactionService = specialTransactionService;
		this.domainService = domainService;
		this.usergroupService = usergrService;
	}

    @GetMapping("/galaxy/appadmin/specialtransaction/graph")
	public Map<String, Object> graph(@RequestParam(value = "limit",
						required = false) Integer limit) {
		return specialTransactionService.graph1(limit == null ? 100 : limit);
	}
    
    @GetMapping(value="/galaxy/appadmin/specialtransaction/display/{name}")
    public @ResponseBody  Map<String, Object> findforaSpecialTransaction
    									(@PathVariable String name) {

        return
        	specialTransactionService.getAllSpclTxnwithrels(100,name);        
        
    }  
    
    @GetMapping(value="/galaxy/appadmin/specialtransaction")
    public @ResponseBody  List<ResponseParamDTO> findonlySpclTxn(){
  //  (@RequestParam(value = "limit",	required = false) Integer limit) {

       /* return
        	specialTransactionService.getAllSpclTxn(limit == null ? 100 : limit);  */
    	return specialTransactionService.getAllSPCLwithrels();
    	
        
    }
    
    @GetMapping(value="/galaxy/appadmin/specialtransaction/{domainname}/{domainusergroup}")
    public @ResponseBody  List<ResponseParamDTO> findonlySpclTxnforDomain(@PathVariable String domainname,
    		@PathVariable String domainusergroup){
  //  (@RequestParam(value = "limit",	required = false) Integer limit) {

       /* return
        	specialTransactionService.getAllSpclTxn(limit == null ? 100 : limit);  */
    	return specialTransactionService.getAllSPCLwithrels(domainname,domainusergroup);
    	
        
    }
	@PostMapping("/galaxy/appadmin/specialtransaction/removeall")
	public ResponseEntity<String> deleteallspclTransaction(@RequestBody 
			RelationshipDTO		spclrelationship) {
		
		System.out.println(" SpecialTransaction request to delete all with name " + spclrelationship.getName());
		Collection<SpecialTransaction> specialtransactionc = 
				specialTransactionService.findWithName(spclrelationship.getName());
		for(SpecialTransaction spclTxn : specialtransactionc) {
			System.out.println("Deleting spcl txn for id " + spclTxn.getId());
			specialTransactionService.delete(spclTxn.getId());
		}
		return ResponseEntity.ok("Success");
	}
	
	@DeleteMapping("/galaxy/appadmin/specialtransaction/{id}")
	public ResponseEntity<String> deleteSpclTxn(@PathVariable String id) {

		SpecialTransaction spclitem = specialTransactionService.findByID(Long.valueOf(id));

		
		System.out.println("Deleting spcl txn for id " + spclitem.getId());
		specialTransactionService.delete(spclitem.getId());
		
		return ResponseEntity.ok("Success");
		
	}

	@PutMapping("/galaxy/appadmin/specialtransaction/{id}")
	public ResponseEntity<?> updateSpclTxn(@PathVariable Long id, 
			@RequestBody SpecialTransaction specialtransaction) {

		specialtransaction = specialTransactionService.getSpclbyId(id, 
							 specialtransaction);

		if (null == specialtransaction) {
			return new ResponseEntity<String>("No SPCL found for ID " + id, 
					HttpStatus.NOT_FOUND);
		}

		return new ResponseEntity<SpecialTransaction>(specialtransaction,
				HttpStatus.OK);
	}
	
	@PostMapping("/galaxy/appadmin/specialtransaction")
	public ResponseEntity<Object> createSpecialTxn(@RequestBody 
			SpecialTransactionDTO 	spclrelationship) {
		System.out.println("Request is  " + spclrelationship.toString());
		Domain domain = domainService.findByName(spclrelationship.
				getStartnodeid());		
		DomainUserGroup dusergrp = 
				usergroupService.findByName(spclrelationship.getEndnodeid());
		System.out.println("spcl for usergroup with name " + dusergrp.getName() + " , " + 
				spclrelationship.getName());
		SpecialTransaction spclTxn = 
				new SpecialTransaction(spclrelationship.getName(),domain,
						dusergrp,Boolean.valueOf(spclrelationship.getIsAttached()),spclrelationship.getAttachedStateName());
		//System.out.println("Creating relation " + spclTxn);
		SpecialTransaction specialtransactionc = 
				specialTransactionService.save(spclTxn);
		URI location = ServletUriComponentsBuilder.
		fromCurrentRequest().path("/{id}").buildAndExpand(specialtransactionc.
				getId()).toUri();
		return ResponseEntity.created(location).build();
	}
}
