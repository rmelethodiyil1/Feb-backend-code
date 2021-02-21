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

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize.Inclusion;

import galaxy.spring.data.neo4j.domain.Domain;
import galaxy.spring.data.neo4j.domain.DomainUser;
import galaxy.spring.data.neo4j.domain.DomainUserGroup;
import galaxy.spring.data.neo4j.domain.Movie;
import galaxy.spring.data.neo4j.services.UserGroupService;

/**
 * @author Mark Angrish
 * @author Michael J. Simons
 */
@RestController
@RequestMapping("/")
public class UserGroupController {

	private final UserGroupService userGroupService;
	
	public UserGroupController(UserGroupService userGroupService) {
		this.userGroupService = userGroupService;
	}

    @GetMapping("/galaxy/appadmin/usergroup/graph")
	public Map<String, Object> graph(@RequestParam(value = "limit",
	required = false) Integer limit) {
		return userGroupService.graph1(limit == null ? 100 : limit);
	}
    
    @GetMapping(value="/galaxy/appadmin/usergroup")    
    public @ResponseBody List<DomainUserGroup> findAllDomainug() {

        List<DomainUserGroup> usersgrp = userGroupService.getAllUsersGroup();        
        return usersgrp;
    }
    
    @PostMapping(value="/galaxy/appadmin/usergroup/id")    
    public @ResponseBody DomainUserGroup findaDomainug(@RequestBody 
			DomainUserGroup domainusergrp,HttpServletRequest request, HttpServletResponse response) {

        DomainUserGroup usersgrp = userGroupService.getUsersGroup(String.valueOf(domainusergrp.getId()));        
		SecurityContextHolder.clearContext();
		HttpSession session= request.getSession(false);
		if(session != null) {
		   session.invalidate();
		}
        return usersgrp;
    }
    
	@PostMapping("/galaxy/appadmin/usergroup")
	public ResponseEntity<Object> createDomainUserGroup(@RequestBody 
			DomainUserGroup domainusergrp,HttpServletRequest request, HttpServletResponse response) {
		System.out.println(" DomainUserGroup request is "+ domainusergrp.getName());
		DomainUserGroup domainc = userGroupService.save(domainusergrp);
		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").
						buildAndExpand(domainc.getId()).toUri();
		SecurityContextHolder.clearContext();
		HttpSession session= request.getSession(false);
       if(session != null) {
           session.invalidate();
       }
       
		return ResponseEntity.created(location).build();
	}
	
	@DeleteMapping("/galaxy/appadmin/usergroup/{id}")
	public ResponseEntity<?> deleteCustomer(@PathVariable String id) {

		DomainUserGroup dug = userGroupService.findByID(Long.valueOf(id));
		if (dug == null) {
			return new ResponseEntity<String>("No DomainUserGroup found for ID " + 
					id, HttpStatus.NOT_FOUND);
		}else {
			userGroupService.delete(dug.getId());
		}
		return new ResponseEntity<Long>(dug.getId(), HttpStatus.OK);
	}

	@PutMapping("/galaxy/appadmin/usergroup/{id}")
	public ResponseEntity<?> updateCustomer(@PathVariable Long id, 
			@RequestBody DomainUserGroup usergroup) {

		usergroup = userGroupService.update(id, usergroup);

		if (null == usergroup) {
			return new ResponseEntity<String>("No Customer found for ID " + id,
					HttpStatus.NOT_FOUND);
		}

		return new ResponseEntity<DomainUserGroup>(usergroup, HttpStatus.OK);
	}
}
