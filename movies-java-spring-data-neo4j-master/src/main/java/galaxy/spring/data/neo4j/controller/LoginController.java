package galaxy.spring.data.neo4j.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import galaxy.spring.data.neo4j.domain.DomainUser;
import galaxy.spring.data.neo4j.services.DomainService;
import galaxy.spring.data.neo4j.services.PermittedService;
import galaxy.spring.data.neo4j.services.StateService;
import galaxy.spring.data.neo4j.services.UserGroupService;
import galaxy.spring.data.neo4j.services.UserService;

@RestController
@RequestMapping("/")
public class LoginController {
	
	private final UserService userService;
	private final UserGroupService usergroupService;
	
	public LoginController(UserService userService,UserGroupService usergroupServ) {
		this.userService = userService;
		this.usergroupService = usergroupServ;
	}

    @GetMapping(value="/galaxy/appuser/login")
    @ResponseBody
    public String currentUserName(Authentication authentication,
    		HttpServletRequest request, HttpServletResponse response) {

    	System.out.println("Login request for user " + authentication.getName());
        if (authentication != null) {
    		SecurityContextHolder.clearContext();
        	HttpSession session= request.getSession(false);
			if(session != null) {
				session.invalidate();
			}
			return findUserbyUername (authentication.getName());
		}
        return "";
	}
    
    @GetMapping(value="/galaxy/appuser/logout")
    @ResponseBody
    public String logoutuser(Authentication authentication,
    		HttpServletRequest request, HttpServletResponse response) {

    	System.out.println("Logout request for user " + authentication.getName());
        if (authentication != null) {
    		SecurityContextHolder.clearContext();
        	HttpSession session= request.getSession(false);
			if(session != null) {
				session.invalidate();
			}
		}
        return "";
	}
    private String findUserbyUername(String username) {
	    
		String rolename =  
				usergroupService.findByName(userService.findByName(username).getDusergroup()).getAuthorityname();
		return new String("{ \"ROLE\" :\"" + rolename + "\"}");
				
	    
	}
}