package galaxy.spring.data.neo4j.controller;

import java.net.URI;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

import galaxy.spring.data.neo4j.domain.BelongsTo;
import galaxy.spring.data.neo4j.domain.Domain;
import galaxy.spring.data.neo4j.domain.DomainUser;
import galaxy.spring.data.neo4j.domain.DomainUserGroup;
import galaxy.spring.data.neo4j.domain.Movie;
import galaxy.spring.data.neo4j.services.BelongsToService;
import galaxy.spring.data.neo4j.services.UserGroupService;
import galaxy.spring.data.neo4j.services.UserService;

/**
 * @author Mark Angrish
 * @author Michael J. Simons
 */
@RestController
@RequestMapping("/")
public class UserController {

	private final UserService userService;
	private final UserGroupService usergrpService;
	private final BelongsToService belongsToservice;
	
	public UserController(UserService userService,
		UserGroupService usergrpService,BelongsToService belongsToservice) {
		this.userService = userService;
		this.usergrpService = usergrpService;
		this.belongsToservice = belongsToservice;
	}

    @GetMapping("/galaxy/appadmin/user/graph")
	public Map<String, Object> graph(@RequestParam(value = "limit",
	required = false) Integer limit) {
		return userService.graph1(limit == null ? 100 : limit);
	}
    
    @GetMapping(value="/galaxy/appadmin/user")
    public @ResponseBody Map<String, Object> findAllDomains() {

        return userService.getAllUsers1();        
    }  
    
	@PostMapping("/galaxy/appadmin/user")
	public ResponseEntity<Object> createDomainUser(@RequestBody 
			DomainUser domainuser,HttpServletRequest request, HttpServletResponse response) {
		System.out.println(" DomainUser request is for "+ domainuser.getName() + " group " + 
			domainuser.getDusergroup());
		DomainUser domainc = userService.save(domainuser);
		DomainUserGroup usrGroup = 
				usergrpService.findByName(domainuser.getDusergroup());
		BelongsTo userBelongsTo = new BelongsTo(usrGroup,domainc);
		belongsToservice.save(userBelongsTo);
		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").
						buildAndExpand(domainc.getId()).toUri();
		SecurityContextHolder.clearContext();
		HttpSession session= request.getSession(false);
        if(session != null) {
           session.invalidate();
        }
		return ResponseEntity.created(location).build();
	}
	
	@DeleteMapping("/galaxy/appadmin/user/{id}")
	public ResponseEntity<?> deleteUser(@PathVariable String id) {

		DomainUser duser = userService.findByID(Long.valueOf(id));
		if (duser == null) {
			return new ResponseEntity<String>("No DomainUser found for ID " + 
					id, HttpStatus.NOT_FOUND);
		}else {
			userService.delete(duser.getId());
		}
		return new ResponseEntity<Long>(duser.getId(), HttpStatus.OK);

	}

	@PostMapping(value="/galaxy/appadmin/user/id")    
    public @ResponseBody DomainUser findaDomainus(@RequestBody 
			DomainUser domainuser,HttpServletRequest request, HttpServletResponse response) {

        DomainUser users = userService.getUsers((String.valueOf(domainuser.getId())));        
		SecurityContextHolder.clearContext();
		HttpSession session= request.getSession(false);
		//users.setDusergroup(null);
		if(session != null) {
		   session.invalidate();
		}
        return users;
    }
	
	@PutMapping("/galaxy/appadmin/user/{id}")
	public ResponseEntity<?> updateUser(@PathVariable Long id, 
			@RequestBody DomainUser user) {

		user = userService.update(id, user);

		if (null == user) {
			return new ResponseEntity<String>("No Customer found for ID " + id,
					HttpStatus.NOT_FOUND);
		}

		return new ResponseEntity<DomainUser>(user, HttpStatus.OK);
	}
}
