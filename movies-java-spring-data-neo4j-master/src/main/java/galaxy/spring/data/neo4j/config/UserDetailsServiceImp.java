package galaxy.spring.data.neo4j.config;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import galaxy.spring.data.neo4j.domain.DomainUser;
import galaxy.spring.data.neo4j.domain.DomainUserGroup;
import galaxy.spring.data.neo4j.services.UserGroupService;
import galaxy.spring.data.neo4j.services.UserService;

@Service
public class UserDetailsServiceImp implements UserDetailsService {
	 
	@Autowired
	private UserService userService;
	@Autowired
	private UserGroupService usergService;
	
	public UserDetailsServiceImp() {
		
	}
	
	public UserDetailsServiceImp(UserService userService) {
		this.userService = userService;
	}
		
	/*@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
	
		   
		DomainUser user = findUserbyUername(username);
	
		UserBuilder builder = null;
		if (user != null) {
		  builder = org.springframework.security.core.userdetails.User.withUsername(username);
		  builder.password(new BCryptPasswordEncoder().encode(user.getPassword()));
		  builder.roles("ROLE_" + user.getBelongsTo().get(0).getDomainUserGroup().getAuthorityname());
		} else {
		  throw new UsernameNotFoundException("User not found.");
		}
		
		return builder.build();
	} */
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		    
		System.out.println("called load method");
		DomainUser user = null;
		Set<GrantedAuthority> grantedAuthorities = null;
        try
        {
            user = findUserbyUername(username);
            if(user == null)
                throw new UsernameNotFoundException("User " + username  + " not available");

            grantedAuthorities = new HashSet<>();
           
            grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_" + 
            		findUserGroupbyUername(user.getDusergroup()).getAuthorityname()));
            
        }
        catch(Exception exp) {
            exp.printStackTrace();
        }
        System.out.println("Returning new userdetails");
        return new 
        org.springframework.security.core.userdetails.User(user.getName(), user.getPassword(), grantedAuthorities);
	}
		
	private DomainUser findUserbyUername(String username) {
	    
		return userService.findByName(username);
	    
	}
	
	private DomainUserGroup findUserGroupbyUername(String usergrpname) {
	    
		return usergService.findByName(usergrpname);
	    
	}
}