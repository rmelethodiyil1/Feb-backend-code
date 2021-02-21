package galaxy.spring.data.neo4j.controller;

import java.net.URI;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
import galaxy.spring.data.neo4j.domain.CurrentState;
import galaxy.spring.data.neo4j.domain.Domain;
import galaxy.spring.data.neo4j.domain.DomainUser;
import galaxy.spring.data.neo4j.domain.EndState;
import galaxy.spring.data.neo4j.domain.Movie;
import galaxy.spring.data.neo4j.domain.SpecialTransaction;
import galaxy.spring.data.neo4j.domain.StartState;
import galaxy.spring.data.neo4j.domain.State;
import galaxy.spring.data.neo4j.domain.TransactionUsers;
import galaxy.spring.data.neo4j.domain.Unit;
import galaxy.spring.data.neo4j.domain.UnitInstanceOf;
import galaxy.spring.data.neo4j.dto.RequestParamDTO;
import galaxy.spring.data.neo4j.dto.ResponseParamDTO;
import galaxy.spring.data.neo4j.dto.UnitDTO;
import galaxy.spring.data.neo4j.services.AllowedTransitionService;
import galaxy.spring.data.neo4j.services.DomainService;
import galaxy.spring.data.neo4j.services.SpecialTransactionService;
import galaxy.spring.data.neo4j.services.StateService;
import galaxy.spring.data.neo4j.services.UnitService;
import galaxy.spring.data.neo4j.services.UserService;

/**
 * @author Mark Angrish
 * @author Michael J. Simons
 */
@RestController
@RequestMapping("/")
public class UnitController {

	private final UnitService unitService;
	private final StateService stateService;
	private final DomainService domainService;
	private final AllowedTransitionService allowedTransitionService;
	private final UserService userService;

	public UnitController(UnitService unitService,StateService stateService,
	DomainService domainService,AllowedTransitionService allowedTransitionService,UserService userService ) {
		this.unitService = unitService;
		this.domainService = domainService;
		this.stateService = stateService;
		this.allowedTransitionService = allowedTransitionService;
		this.userService = userService;
	}

//    @GetMapping("/unit/graph")
//	public Map<String, Object> graph(@RequestParam(value = "limit",required = 
//							false) Integer limit) {
//		return unitService.graph1(limit == null ? 100 : limit);
//	}
    
    @PostMapping("/galaxy/appuser/fetchunits")
    public @ResponseBody List<Unit> findAllUnitsofDomain(@RequestBody RequestParamDTO 
    								requestParam) {

        List<Unit> units = unitService.getUnitsforDomainGrp(requestParam.getParam2(),
        						requestParam.getParam1());        
        return units;
    }   
    @PostMapping("/galaxy/appuser/fetchtxnunits")
    public @ResponseBody List<UnitDTO> findAllUnitsofDomainforuser(@RequestBody RequestParamDTO 
    								requestParam) {

        List<UnitDTO> units = unitService.getUnitsforDomainandGrp(requestParam.getParam2(),
        				  requestParam.getParam1());        
        return units;
    }
    
	@PostMapping("/galaxy/appadmin/unit")
	public ResponseEntity<Object> createUnit(@RequestBody RequestParamDTO unitdto) {
		String uname = unitdto.getName();
		System.out.println(" Unit request is "+ uname);
		String domname = unitdto.getParam1();
		Domain domain = domainService.findByName(domname);
		State initialState = stateService.findStateByName(domain.getInitialStatename());
		Unit unitc = null;
		for(int cnt = 1; cnt <= Integer.valueOf(unitdto.getParam2()); cnt++) {
			unitc = unitService.save(new Unit(uname + "_" + cnt,"test",domname));
			UnitInstanceOf uiof = new UnitInstanceOf(domain,unitc);
			unitc.setAnInstanceOf(unitService.save(uiof));
			unitc.setCurrentState(stateService.createCurrentState(initialState,unitc));
			unitc.setCurrentStatename(domain.getInitialStatename());
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			unitc.addTransactedUser(new TransactionUsers(auth.getName(),domain.getInitialStatename()));
			unitService.save(unitc);
		}
		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").
							buildAndExpand(unitc.getId()).toUri();
		return ResponseEntity.created(location).build();
	}
	
	@DeleteMapping("/galaxy/appadmin/unit/{id}")
	public ResponseEntity<?> deleteUnit(@PathVariable Long id) {

		if (null == unitService.delete(id)) {
			return new ResponseEntity<String>("No Customer found for ID " + id, HttpStatus.NOT_FOUND);
		}

		return new ResponseEntity<Long>(id, HttpStatus.OK);

	}

	@PutMapping("/galaxy/appadmin/unit/{id}")
	public ResponseEntity<?> updateCustomer(@PathVariable Long id, @RequestBody Unit unit) {

		unit = unitService.update(id, unit);

		if (null == unit) {
			return new ResponseEntity<String>("No Customer found for ID " + id, HttpStatus.NOT_FOUND);
		}

		return new ResponseEntity<Unit>(unit, HttpStatus.OK);
	}
	
	@PostMapping("/galaxy/appuser/unit/specialtransaction")
	public @ResponseBody List<RequestParamDTO> findOperationsforUnit(@RequestBody 
			RequestParamDTO requestParam)  {
		ArrayList<RequestParamDTO> allspcl = new ArrayList<RequestParamDTO>();
		Unit forUnit = unitService.findByID(Long.valueOf(requestParam.getParam2()));
		String foruser = SecurityContextHolder.getContext().getAuthentication().getName();
		String domainname = requestParam.getParam1();
		DomainUser therequestingUser = userService.findByName(
				foruser);
		State state = forUnit.getCurrentState().getState();
		StartState startState = stateService.findStartStatewithTrn(state.getName());
		List<AllowedTransition> allowedtrnlist = startState.getAllowedtrn();
		for(AllowedTransition allowedtrans : allowedtrnlist) {
			
			String specialTxnname = allowedtrans.getallowedmMap().get(domainname);
			if(specialTxnname != null && unitService.ifspclapplicableforug(specialTxnname,
					therequestingUser.getDusergroup(),domainname,forUnit,state.getName(),
					foruser)) {
				allspcl.add(new RequestParamDTO(
					String.valueOf(forUnit.getId()),forUnit.getName(),specialTxnname));
			}
			
		}
        System.out.println("Fetched spcl size is " + allspcl.size());      
        return allspcl;
    }    
	
	
	@PostMapping("/galaxy/appuser/unit/transact")
	public @ResponseBody ResponseParamDTO transactUnit(@RequestBody RequestParamDTO requestParam) {
		
		Unit unittoTransact = unitService.findByID(Long.valueOf(requestParam.getParam1()));
		Domain dofUnit = unittoTransact.getAnInstanceOf().getDomain();
		String domainName = dofUnit.getName();
		UnitInstanceOf uof = unittoTransact.getAnInstanceOf();
		CurrentState currState = unittoTransact.getCurrentState();
		
		String endStateforUnit = null;
		@SuppressWarnings("unchecked")
		List<Map<String,Object>> allowedGraph = (List<Map<String,Object>>)
				(allowedTransitionService.graph1(100).get("links"));
		for(Map<String,Object> nameValue : allowedGraph) {
			String domain = (String)nameValue.get("label");
			String spclname = (String)nameValue.get("name");
			String startState = (String)nameValue.get("label1");
			String endStatename = (String)nameValue.get("label2");
			if(dofUnit.getName().equals(domain) && requestParam.getParam2().equals(spclname)
					&& startState.equals(currState.getState().getName())) {
				endStateforUnit = endStatename;
				break;
			}
		}
		System.out.println("The end state required is " + endStateforUnit);
		stateService.deleteCurrentState(currState);
		unitService.deleteUnitInstanceof(uof.getId());
		UnitInstanceOf uiof = new UnitInstanceOf(dofUnit,unittoTransact);
		unittoTransact.setAnInstanceOf(unitService.save(uiof));		
		CurrentState newState = stateService.createCurrentState(
				stateService.findStateByName(endStateforUnit),unittoTransact);
		unittoTransact.setCurrentState(newState);
		unittoTransact.setCurrentStatename(newState.toString());
		unittoTransact.addTransactedUser(new TransactionUsers(
				SecurityContextHolder.getContext().getAuthentication().getName(),newState.toString()));
		//unitService.save(uof);
		unitService.save(unittoTransact);
		//Transact the unit
		ResponseParamDTO location = new ResponseParamDTO();
		return location;
	}
}
