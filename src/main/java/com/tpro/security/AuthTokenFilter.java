package com.tpro.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

public class AuthTokenFilter extends OncePerRequestFilter {

    @Autowired 
	private JwtUtils jwtUtils;	//enkete ediyoruz validate ve generate islemleri icin
    
    @Autowired
    private UserDetailsService userDetailsService; //SecurityService'e gecmek icin enjekte ediyoruz
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		String jwtToken = parseJwt(request); //alttaki method'u cagirdik
		// requestin icinden gelen tokeni alcam
		try {
			if(jwtToken!=null && jwtUtils.validateToken(jwtToken)) {//validate icin jwtUtils'i kullanirim
				String userName = jwtUtils.getUserNameFromJwtToken(jwtToken);
				UserDetails userDetails = userDetailsService.loadUserByUsername(userName);
				UsernamePasswordAuthenticationToken authentication = 
						new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());
				SecurityContextHolder.getContext().setAuthentication(authentication);
			}
		} catch (UsernameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		filterChain.doFilter(request, response);
		
		
	}
	
	//gelen request'in icerisinden JwtToken'i cikartacak olan method'u yaziyoruz
	private String parseJwt(HttpServletRequest request) {//JwtToken'i request'le beraber gelecek
		// requestin header kısmındaki Authorization yapisi lazim bu yapinin icinde JwtToken var
		String header = request.getHeader("Authorization");
		if(StringUtils.hasText(header)&&header.startsWith("Bearer ")) {
			//StringUtils springframework'den geliyor, hasText(header):Authorization'in ici dolu mu diye bakar(null olmamali)
			//header.startsWith:Token Bearer yazisi ile mi basliyor onu kontrol etmek icin
			//JwtToken'i bearer sdfjfsd(jwt token) seklinde geliyor
			return header.substring(7); //bearer+bosluk //7.karakter jwt token'i ile basliyor
			
		}
		return null; //eger yukaridaki donmezse null dondur
		
		
	}
	
	@Override
	protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
		AntPathMatcher antMatcher = new AntPathMatcher();
		return antMatcher.match("/register", request.getServletPath()) || 
				antMatcher.match("/login", request.getServletPath());
	}

}
