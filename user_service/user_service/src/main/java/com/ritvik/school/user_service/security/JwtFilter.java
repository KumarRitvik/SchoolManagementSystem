package com.ritvik.school.user_service.security;

import com.ritvik.school.user_service.service.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
//After user got its token, now every request from user will pass through this class(JWTFilter) as this clss
//inherited(extends) OncePerRequestFilter class..
public class JwtFilter extends OncePerRequestFilter {
    private final JwtUtil jwtUtil;
    private final UserService userService;

    public JwtFilter(JwtUtil jwtUtil, UserService userService) {
        this.jwtUtil = jwtUtil;
        this.userService = userService;
    }

    //Before your controller executes, Spring Security runs chain of filters.
    // & this class "JwtFilter" is part of one of many filters in FilterChain
    //So whenever any endpoint is hit method(doFilterInternal) to call hoga.
    // Then at last when all the filters in filter chain executes, control goes to The controller)
    @Override
    protected void doFilterInternal(HttpServletRequest request,HttpServletResponse response,
                                              FilterChain filterChain) throws ServletException, IOException {
        String path = request.getRequestURI();

        if (path.equals("/auth/login") || path.equals("/auth/register"))//when register,login request id hit
        {
            filterChain.doFilter(request, response);//doFilterInternal() method control ko filter chain ke current
            //filter(JWT Filter) se next filter par bhej deta hai(in username password login) yahi is method ka
            // kaam hai, aur hamne Securityconfig me mention kiya hai ki is filter(JwtFilter) ke bad
            // UsernamePasswordAuthenticationFilter run karega( .addFilterBefore
            // (jwtFilter,UsernamePasswordAuthenticationFilter.class)
            // in the SecurityConfig.(matlam ab yaha se control UsernamePasswordAuthenticationFilter.class wale
            //filter par control gaya) aur jab return hoga is point par to wapas client par response return ho jayega
            //because next line is just return..!!
            return;
        }

        //agar register, login endpoint nahi hua to control if ko skip karke yaha ayega
        String authHeader = request.getHeader("Authorization");//Request se ham Header extract
        // karenge with Spring constant representing the header name "Authorization".
        String token = null;     //   |-----> token and username initially initialized as null...
        String username = null;  //   |

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            //AuthHeader me Bearer_(6letter ka +1 space) ke baad
            //jo bhi hota hai wo token hota hai i.e; 7th letter k baad se jo bhi hai wo wo bearer...!
            token = authHeader.substring(7);
            try {
                username = jwtUtil.extractUsername(token);//token se username nikal liya..

            }
            catch (Exception e) {
                System.out.println("Invalid JWT: " + e.getMessage());
            }
        }

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            //"Has somebody already authenticated this request?"
            //If there is already an Authentication object in SecurityContextHolder, your filter doesn't need
            // to create another one.
            try {
                UserDetails userDetails = userService.loadUserByUsername(username);// jo username token se nikla
                //usi username ka user db se load kar liya in UserDetails' object(parent of AppUserDetails)

                if (jwtUtil.isTokenValid(token, userDetails.getUsername())) {

                    UsernamePasswordAuthenticationToken authentication =
                            new UsernamePasswordAuthenticationToken(userDetails, null,
                                    userDetails.getAuthorities());
                    // jaise UsernamePasswordAuthenticationToken object banate hai login k time,
                    // waise hi yaha bhi bana liya - diference is waha username and password pass kiye the, yaha
                    // userDetails, null(credentials) pass kiya hai(because userDetails me username and password
                    // dono hai), and roles pass kiya..!
                    // Therefore Spring Security:- "This request has been authenticated as ADMIN/Given user."
                    //So har new request ke liye ab username,password se login/authentication nahi karna hoga
                    //coz authentication ka object hi bana diya :)

                    SecurityContextHolder.getContext().setAuthentication(authentication);
                    //"For THIS request, this is the authenticated user."
                    // Spring Security can now ask: SecurityContextHolder.getContext().getAuthentication();
                   // and discover Who is the user?, What authorities/roles does he have?, Is he authenticated?
                    //                                                                         |
                    //                                                                         |
                    // CURRENT HTTP REQUEST                                                    |
                          // │                                                                 |
                           //▼                                                                 |
                           //SecurityContextHolder                                             |
                           //│                                                                 |
                           //└── Authentication Object                                         |
                           //     ├── principal = user                                         |
                            //    ├── authorities = ROLE_ADMIN                                 |
                            //    └── authenticated = "true". <---------------------------------

                    // Har("EVERY") protected API request ke saath JWT bheja jata hai.
                    // Har("EVERY") Request me JwtFilter JWT ko extract aur validate karta hai,
                    // Har("EVERY") Request UserDetails/authorities ke basis par Authentication object banata hai,
                    // aur Har("EVERY") Request, authentication ko SecurityContextHolder me set karta hai.

                    //Important:- "Your JWT filter primarily establishes authentication. Then Spring Security
                    // uses that authentication information(object) to make authorization decisions (Authorization =
                    // "Are you allowed to do this?")" matlab springSecurity isi authorization object ko use karke
                    // ye check karega ki user is request ko access kar sakta hai ya nahi.
                }

            }
            catch (Exception e) {
                System.out.println("JWT authentication failed: " + e.getMessage());
            }
        }

        filterChain.doFilter(request, response);//means send control to next filter(UsernamePassword
        // AuthenticationFilter as discussed already above) in the filter chain
        // (along with request and response) and when all the filters in filter chain executes,
        // control goes to The controller)
    }
}