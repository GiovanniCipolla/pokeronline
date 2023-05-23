package it.prova.pokeronline.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity 
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private JWTFilter jwtFilter;
	@Autowired
	private CustomUserDetailsService customUserDetailsService;

	@Autowired
	private JWTAuthEntryPoint unauthorizedHandler;

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception { 
		http.csrf().disable() // Disabling csrf
				.httpBasic().disable() // Disabling http basic
				.cors() // Enabling cors
				.and()
				
				.authorizeHttpRequests() 
				.antMatchers("/api/auth/login").permitAll()
				
				//tutti gli utenti autenticati possono richiedere le info
				.antMatchers("/api/utente/userInfo").authenticated()
				.antMatchers("/api/tavolo/private").hasAnyRole("ADMIN", "SPECIAL_PLAYER")
				.antMatchers("/api/tavolo/**").authenticated()
				.antMatchers("/api/utente/**").hasRole("ADMIN")
				.antMatchers("/**").hasAnyRole("ADMIN", "SPECIAL_PLAYER","PLAYER")
				// .antMatchers("/anonymous*").anonymous()
				.anyRequest().authenticated()
				.and()
				
				// imposto il mio custom user details service
				.userDetailsService(customUserDetailsService) 
				// quando qualcosa fallisce ho il mio handler che customizza la response
				.exceptionHandling().authenticationEntryPoint(unauthorizedHandler)
				.and()
				
				// non abbiamo bisogno di una sessione: meglio forzare a stateless
				.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS); 

		// Adding the JWT filter
		http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
		
		http.headers().frameOptions().disable();
	}

}
