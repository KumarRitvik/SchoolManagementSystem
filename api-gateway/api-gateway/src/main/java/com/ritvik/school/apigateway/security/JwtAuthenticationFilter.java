package com.ritvik.school.apigateway.security;

import io.jsonwebtoken.Claims;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

@Component
public class JwtAuthenticationFilter implements WebFilter {

    private final JwtUtil jwtUtil;

    public JwtAuthenticationFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    public Mono<Void> filter( // Mono<Void>:- It is the standard return type for gateway filters
            ServerWebExchange exchange,
            WebFilterChain chain) {
        //The response is available through ServerWebExchange,
        // which holds both the request and response. which means
        // we do {ServerHttpResponse response = exchange.getResponse()}
        //to get response & we do{ServerHttpRequest = exchange.
        // getRequest();} to get Request...
        //WebFilterChain:- This represents the remaining WebFlux filters through which the request must pass.
        //(just like FilterChain in Spring MVC, WebFilterChain is used in Spring WebFlux
        // to represent the chain of filters that a request must pass through before reaching
        // the final destination (e.g., a controller or handler)).

        String path = exchange.getRequest().getURI().getPath();

        // Public endpoints
        if (path.equals("/auth/login")
                || path.equals("/auth/register")
                || path.equals("/actuator/health")) {
            return chain.filter(exchange);
        }

        String authHeader = exchange.getRequest()
                .getHeaders()
                .getFirst(HttpHeaders.AUTHORIZATION);
        //HTTP header"s" can have multiple values under the same name.
        //Spring's HttpHeaders represents headers as a collection of
        //names and their associated values.
        // .getFirst(HttpHeaders.AUTHORIZATION) retrieves the first
        // value of the Authorization header.

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
            //return(with response) if no token is there.
        }

        String token = authHeader.substring(7);//extracted token

        try {
            Claims claims = jwtUtil.extractClaims(token);//extracted claims from token

            String username = claims.getSubject();
            String role = claims.get("role", String.class);
            //extracted username and role from claims

            if (username == null || username.isBlank()) {
                exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                return exchange.getResponse().setComplete();
                //return(with response) if no username in token is there.
            }

            List<SimpleGrantedAuthority> authorities = new ArrayList<>();

            if (role != null && !role.isBlank()){ //if role notnull or notblank)

                if (!role.startsWith("ROLE_")) { //if Role notStarts with Role_
                    role = "ROLE_" + role;
                }

                authorities.add(new SimpleGrantedAuthority(role));
            }

            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(username, null, authorities);
                                                        //password null coz we are not validating
                                                        // again as we already validated in login &
                                                        // this is a

            return chain.filter(exchange)
                    .contextWrite(ReactiveSecurityContextHolder.withAuthentication(authentication));
               //                                 |
              //                                  V
             //"Your code continues the request and ensures that downstream reactive-Spring-Security
            // components can recognize the user authenticated by your JWT filter." just like we set
            // authentication object in SecurityContextHolder, here we set it in ReactiveSecurityContextHolder.
            //(1) chain.filter(exchange):- Continue processing the request to next filter downstream in the
            //WebFlux filter chain & it returns Mono<Void>, representing the asynchronous processing of the request.
            //(2).contextWrite(ReactiveSecurityContextHolder.withAuthentication(authentication)):-
            // This is a reactive way to set the authentication object in the security context for the current request.
            // It ensures that downstream components in the reactive chain can access the authenticated user information.
            //(3).contextWrite(...) is a method that allows you to modify the context of the reactive stream. In this case,
            // it sets the authentication object in the ReactiveSecurityContextHolder, which is a reactive equivalent of the
            // traditional SecurityContextHolder used in Spring Security.
            //(4)The combination of chain.filter(exchange).contextWrite(...) allows you to continue processing the
            //request while also ensuring that the authentication information is available for subsequent components in the reactive pipeline.

        } catch (io.jsonwebtoken.JwtException | IllegalArgumentException ex) {
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }
    }
}
//1. The three filter types
//A. WebFilter {your JWT filter}:-A Spring WebFlux filter. It can intercept web
// requests at the WebFlux layer,including requests that aren't ultimately routed
// to a downstream service.Your class: JwtAuthenticationFilter implements WebFilter
//
//B. GlobalFilter:-A Spring Cloud Gateway filter that applies across routed Gateway
// requests. Common uses include request timing, logging and adding headers.

//C. GatewayFilter:-A Gateway filter that is applied to a particular route,
// often through route configuration or a route-specific filter factory. For example, modifying the path of requests to the Student Service.