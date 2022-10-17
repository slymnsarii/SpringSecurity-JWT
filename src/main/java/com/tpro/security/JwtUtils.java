package com.tpro.security;

import java.util.Date;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import com.tpro.security.service.UserDetailsImpl;
import com.tpro.security.service.UserDetailsServiceImpl;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;

@Component //JwtUtils class'ini AuthTokenFilter class'ina gondermek icin(class'i kendim olusturdugum icin @Component)
public class JwtUtils {

	private String jwtSecret="batch82";
	
	//24*60*60*1000 jwt Token'i guvenlik acisindan jwtExpirationMs(milisaniye) ile 1 gun aralikla degistiriyoruz
	private long jwtExpirationMs=86400000;
	
	//**********GENERATE -- TOKEN**********
	public String generateToken(Authentication authentication) {//Authentication'i springSecurity.core'dan aliyoruz
	//Anlik olarak login olan kullanici(nin bilgisini aliyorum) uzerine islemler yapmam gerektigi icin 
    //SpringSecurity'nin authentication data turunde bir veriyi parametre olarak gommem gerek
		
		//anlik olarak kullanicinin bilgilerini getiren method
		UserDetailsImpl userDetails=(UserDetailsImpl) authentication.getPrincipal(); 
		//Token builder() ile uretiliyor
		//Token uretilirken userName ve secret key kullaniliyor
		return Jwts.builder().setSubject(userDetails.getUsername()).
				setIssuedAt(new Date()). //token'in olusturulma anlik tarihi
				setExpiration(new Date(new Date().getTime()+jwtExpirationMs)). //setExpiration:token yasam suresi, bunun yeni bir date'i olmali bunu belirlemek icin de (suanki sure+jwtExpiration(token olusma suresi))
				signWith(SignatureAlgorithm.HS512, jwtSecret). //token olustu ama aga gondermek icin sifrelemek gerek
				compact();
	}
	
	//**************************************
	
	
	//*************VALIDE-TOKEN*************(validate edecegim icin boolen doner)
	//Jwt Token'in validate edilmesi icin secret key ve token'in kendisine ihtiyac var
	public boolean validateToken(String token) {
		try {
			Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token);
			//parser:validasyonu saglar, setSigningKey:validate edilmesi icin parseClaimsJws:sifrelenmis token'i cozmek icin
			return true;
		} catch (ExpiredJwtException e) { //parseClaimsJws tokenle ilgili sikintilar icin bazi exceptionlar firlatiyor bunlari handle ettik
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedJwtException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MalformedJwtException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SignatureException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
	
	
	
	
	
	
	//**************************************
	
	
	
	//*******Jwt Token'den userName'i alalim
	public String getUserNameFromJwtToken(String token) {
		return Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody().getSubject(); 
																		//token'in body'sine gidip, setSubject ile aldigim userName'i getirdim
		
	}
	
}
