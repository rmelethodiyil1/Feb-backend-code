package galaxy.spring.data.neo4j.services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import galaxy.spring.data.neo4j.domain.AllowedTransition;
import galaxy.spring.data.neo4j.domain.Domain;
import galaxy.spring.data.neo4j.domain.DomainUser;
import galaxy.spring.data.neo4j.domain.SpecialTransaction;
import galaxy.spring.data.neo4j.domain.StartState;
import galaxy.spring.data.neo4j.domain.State;
import galaxy.spring.data.neo4j.domain.TransactionUsers;
import galaxy.spring.data.neo4j.domain.Unit;
import galaxy.spring.data.neo4j.domain.UnitInstanceOf;
import galaxy.spring.data.neo4j.dto.RequestParamDTO;
import galaxy.spring.data.neo4j.dto.ResponseParamDTO;
import galaxy.spring.data.neo4j.dto.UnitDTO;
import galaxy.spring.data.neo4j.repositories.DomainRepository;
import galaxy.spring.data.neo4j.repositories.UnitInstanceOfRepository;
import galaxy.spring.data.neo4j.repositories.UnitRepository;

@Service
public class UnitService {

    private final static Logger LOG = LoggerFactory.getLogger(UnitService.class);

	private final UnitRepository unitRepository;
	private final UnitInstanceOfRepository unitinstanceOfRepository;
	private final StateService stateService;
	private final UserService userService;
	private final SpecialTransactionService spclService;
	
	public UnitService(UnitRepository unitRepository,
			UnitInstanceOfRepository unitinstanceOfRepository,StateService stateService,UserService userService,
			SpecialTransactionService spclService) {
		this.unitRepository = unitRepository;
		this.unitinstanceOfRepository = unitinstanceOfRepository;
		this.stateService = stateService;
		this.userService = userService;
		this.spclService = spclService;
	}

	@Transactional
	public Unit save(Unit unit) {
		// TODO Auto-generated method stub
		return unitRepository.save(unit);
	}
	@Transactional
	public UnitInstanceOf deleteUnitInstanceof(Long id) {
		
		 Optional<UnitInstanceOf> unitObj = unitinstanceOfRepository.findById(id);
		 unitinstanceOfRepository.deleteById(id);
		 return unitObj.get();
		// TODO Auto-generated method stub
	}
	@Transactional
	public UnitInstanceOf save(UnitInstanceOf unitinstanceof) {
		
		return unitinstanceOfRepository.save(unitinstanceof);
	}
	public Unit delete(Long id) {
		
		 Optional<Unit> unitObj = unitRepository.findById(id);
		 unitRepository.deleteById(id);
		 return unitObj.get();
		// TODO Auto-generated method stub
	}

	public Unit update(Long id, Unit unit) {
		
		// TODO Auto-generated method stub
		unitRepository.save(unit);
		Optional<Unit> unitObj = unitRepository.findById(id);
		 return unitObj.get();
	}

	public List<Unit> getAllUnits(String domain) {
		// TODO Auto-generated method stub
		Collection<Unit> unitc = unitRepository.findAllUnitsfordomain(domain);
		ArrayList<Unit> unitlist = new ArrayList<Unit>();
		for(Unit unit : unitc) {
			unitlist.add(unit);
		}
		return unitlist;
	}
	
	@Transactional(readOnly = true)
    public Unit findByName(String name) {
		Unit result = unitRepository.findByName(name);
		System.out.println("Unit fetched by name is " + result.getName() + " curr state is" + 
		result.getCurrentState().getId());
        return result;
    }

	public List<Unit> getUnitsforDomainGrp(String fordomain, String foruser) {
		// TODO Auto-generated method stub
		List<Unit> unitc = getAllUnits(fordomain);
		ArrayList<Unit> onlydomunits = new ArrayList<Unit>();
		for (Unit eachUnit: unitc) {
				onlydomunits.add(eachUnit);
		}
		return onlydomunits;
	}
	
	public List<UnitDTO> getUnitsforDomainandGrp(String fordomain, String foruser) {
		// TODO Auto-generated method stub
		ArrayList<UnitDTO> applicableUnit = new ArrayList<UnitDTO>();
		List<Unit> unitc = getAllUnits(fordomain);
		DomainUser therequestingUser = userService.findByName(foruser);
		for (Unit eachUnit: unitc) {
			Unit forUnit = findByName(eachUnit.getName());
			State state = forUnit.getCurrentState().getState();
			StartState startState = stateService.findStartStatewithTrn(state.getName());
			List<AllowedTransition> allowedtrnlist = startState.getAllowedtrn();
			for(AllowedTransition allowedtrans : allowedtrnlist) {
				String specialTxnname = allowedtrans.getallowedmMap().get(fordomain);
				if(specialTxnname != null && ifspclapplicableforug(specialTxnname,
						therequestingUser.getDusergroup(),fordomain,forUnit,state.getName(),
						foruser)) {
							applicableUnit.add(new UnitDTO(forUnit.getId(),forUnit.getName(),forUnit.getCurrentStatename(),
									forUnit.getScreenType(),forUnit.getDomainname()));
							break;
				
				}
			}
		}
		
		return applicableUnit;
	}
	
	public boolean ifspclapplicableforug(String spclname, String domainusergroupname,String domainname,
			Unit currUnit,String currentState,String owningUser) {
		
		List<ResponseParamDTO> allSPCLwithrels = spclService.getAllSPCLwithrels(domainname, domainusergroupname);
		for(ResponseParamDTO relationResp : allSPCLwithrels) {
			if(relationResp.getName().equals(spclname)) {
				if(relationResp.isStatus()) {
					String attachedState = relationResp.getLabel3();
					Iterator<TransactionUsers> it = 
							currUnit.getTransactedUsers().iterator();
					while (it.hasNext()) {
						TransactionUsers previous = it.next();
						if(previous.getThisState().equals(attachedState) && previous.getUserName().equals(owningUser)) {
							return true;
						}
					}
					return false;
				} else {
					return true;
				}
			}
		}
		
		return false;
	}
	@Transactional(readOnly = true)
    public Unit findwithDomainByName(String name) {
		Unit result = unitRepository.findwithDomainByName(name);
		System.out.println("Unit fetched by name is " + result.getName() + " domain is" + 
		result.getAnInstanceOf().getDomain().getName());
        return result;
    }

	public Unit findByID(Long id) {
		return unitRepository.findById(id).get();
	}
}
