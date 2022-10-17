package com.tpro.config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import com.tpro.security.AuthTokenFilter;
import lombok.AllArgsConstructor;
@AllArgsConstructor
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
	@Autowired // TODO AllArgs yazıldığı için cons injection olmuş oldu
	private UserDetailsService userDetailsService;
	
	
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.csrf().disable().
		sessionManagement().
		sessionCreationPolicy(SessionCreationPolicy.STATELESS).// TODO rest mimarı stateless
		and().
		authorizeRequests().
		antMatchers("/register","/login").permitAll().
		anyRequest().authenticated();
		// TODO Filter içinde Tokeni valide etmem lazım,
	   // TODO kendi yazdığım filtırı authTokenFilter 'ı buraya ekliyorum
		http.addFilterBefore(authTokenFilter(), UsernamePasswordAuthenticationFilter.class);
		
	}
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		// TODO dün yaptığımız kodun kısa hali  .. provider'ın kısa hali
		auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
	}
	
	//TODO kendim oluşturduğum filtremi bean olarak ekliyorum
	@Bean
	public AuthTokenFilter authTokenFilter() {
		return new AuthTokenFilter();
	}
	
	
	@Bean
	protected AuthenticationManager authenticationManager()throws Exception{
		return super.authenticationManager();
	}
	
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
}