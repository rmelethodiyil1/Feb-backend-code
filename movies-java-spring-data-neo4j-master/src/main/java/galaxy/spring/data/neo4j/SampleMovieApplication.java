package galaxy.spring.data.neo4j;

import java.util.Arrays;
import java.util.Collections;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletResponse;

import org.neo4j.ogm.model.Result;
import org.neo4j.ogm.session.Session;
import org.neo4j.ogm.session.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.data.neo4j.repository.config.EnableNeo4jRepositories;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CommonsRequestLoggingFilter;

import galaxy.spring.data.neo4j.config.CustomBasicAuthenticationEntryPoint;
import galaxy.spring.data.neo4j.config.UserDetailsServiceImp;
import galaxy.spring.data.neo4j.services.UserService;

/**
 * @author Michael Hunger
 * @author Mark Angrish
 */
@SpringBootApplication(exclude = { SecurityAutoConfiguration.class })
@EnableNeo4jRepositories("galaxy.spring.data.neo4j.repositories")
@EnableWebSecurity
public class SampleMovieApplication extends WebSecurityConfigurerAdapter {

	public static String REALM = "REALM";
	
    public static void main(String[] args) {
        ConfigurableApplicationContext configContext = 
        		SpringApplication.run(SampleMovieApplication.class, args);
        configContext.getBean(RepoInit.class).fillWithTestdata();
      
    }
    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.cors().and()
                .authorizeRequests().anyRequest().fullyAuthenticated()
                .antMatchers("/login**").permitAll()
                .antMatchers("/galaxy/appuser/**").hasAnyRole("ADMIN","USER")
                .antMatchers("/galaxy/appadmin/**").hasRole("ADMIN")
                .and()
                .logout()
                .logoutUrl("/logout").permitAll().
                 invalidateHttpSession(true).logoutSuccessHandler((httpServletRequest, httpServletResponse, authentication) -> {
                    httpServletResponse.setStatus(HttpServletResponse.SC_OK);
                }).and().csrf().disable()
                .httpBasic().realmName("REALM").authenticationEntryPoint(getBasicAuthEntryPoint());

    }
    
    
    @Bean
    public CustomBasicAuthenticationEntryPoint getBasicAuthEntryPoint() {
        return new CustomBasicAuthenticationEntryPoint();
    }
    
    @Autowired
    public void configureGlobalSecurity( AuthenticationManagerBuilder auth) throws Exception {
    	System.out.println("Calling authenticator");
        auth.userDetailsService(userDetailsService()).passwordEncoder(passwordEncoder());
    }
    
  /*  @Override   
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
    	System.out.println("Calling authenticator");
      auth.userDetailsService(userDetailsService()).passwordEncoder(passwordEncoder());
    }*/
    
    @Bean
    public UserDetailsService userDetailsService() {
      return new UserDetailsServiceImp();
    };
    
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
      return new BCryptPasswordEncoder();
    };
    
    @Bean
    public CommonsRequestLoggingFilter requestLoggingFilter() {
         System.out.println("inside logging filter");
        CommonsRequestLoggingFilter loggingFilter = new CommonsRequestLoggingFilter();
        loggingFilter.setIncludeClientInfo(true);
        loggingFilter.setIncludeQueryString(true);
        loggingFilter.setIncludePayload(true);
        loggingFilter.setIncludeHeaders(true);
        return loggingFilter;
    }
    
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("*"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("authorization", "content-type", "x-auth-token"));
        configuration.setExposedHeaders(Arrays.asList("x-auth-token"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
    
    @Autowired
    private SessionFactory sessionFactory;

    @PostConstruct
    public void createIndexesAndConstraints() {
        Session session = sessionFactory.openSession();
        Result result = session.query("CREATE INDEX ON :State(name)", Collections.EMPTY_MAP);
    }
}