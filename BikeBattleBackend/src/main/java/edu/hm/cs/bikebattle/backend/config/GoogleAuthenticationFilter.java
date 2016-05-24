package edu.hm.cs.bikebattle.backend.config;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import edu.hm.cs.bikebattle.backend.domain.User;
import edu.hm.cs.bikebattle.backend.repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;

/**
 * Organization: HM FK07.
 * Project: BikeBattleBackend, edu.hm.cs.bikebattle.config
 * Author(s): Rene Zarwel
 * Date: 04.05.16
 * OS: MacOS 10.11
 * Java-Version: 1.8
 * System: 2,3 GHz Intel Core i7, 16 GB 1600 MHz DDR3
 */
public class GoogleAuthenticationFilter extends OncePerRequestFilter {

  Logger logger = LoggerFactory.getLogger(GoogleAuthenticationFilter.class);

  private static final String CLIENT_ID = "1005553311508-r90po1hf888rkr2u464ccoo8vvojk75u.apps.googleusercontent.com";

  private static final GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(new NetHttpTransport(), new JacksonFactory())
      .setAudience(Arrays.asList(CLIENT_ID))
      .setIssuer("https://accounts.google.com")
      .build();


  private final UserRepository userRepository;

  public GoogleAuthenticationFilter(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

    //Get Token from Header
    String token = request.getHeader("Authorization");
    if (token != null && token.matches("Bearer .*")) {
      token = token.split(" ")[1];
    } else {
      response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "No Authorization provided");
      return;
    }

    //Check Token
    GoogleIdToken idToken;
    try {
      idToken = verifier.verify(token);

    } catch (Exception e) {
      response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Verification failed.");
      return;
    }
    if (idToken == null) {
      response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid ID token.");
      return;
    }

    //------Token is valid ---

    GoogleIdToken.Payload payload = idToken.getPayload();

    // Get profile information from payload
    String email = payload.getEmail();
    String name = (String) payload.get("name");

    //Find requesting user, create user if not in db
    User userInfo = userRepository.findByEmail(email);

    if (userInfo == null) {

      userInfo = new User();
      userInfo.setEmail(email);
      userInfo.setName(name);

      userInfo = userRepository.save(userInfo);
    }

    //Set UserLocation in Header
    response.addHeader("UserLocation", String.valueOf(userInfo.getOid()));

    //Set Authentication Context
    GrantedAuthority authority = new SimpleGrantedAuthority("user");
    UsernamePasswordAuthenticationToken authenticationToken =
        new UsernamePasswordAuthenticationToken(userInfo, token, Collections.singleton(authority));
    SecurityContextHolder.getContext().setAuthentication(authenticationToken);

    //Continue
    filterChain.doFilter(request, response);

  }
}
