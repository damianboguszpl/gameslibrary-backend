package pl.pollub.gameslibrary.Security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import pl.pollub.gameslibrary.Filters.CustomAuthenticationFilter;
import pl.pollub.gameslibrary.Filters.CustomAuthorizationFilter;

import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.POST;
import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final UserDetailsService userDetailsService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;


    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder);
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        CustomAuthenticationFilter customAuthenticationFilter = new CustomAuthenticationFilter(authenticationManagerBean(null));
        customAuthenticationFilter.setFilterProcessesUrl("/auth/login");

        http.csrf( csrf -> csrf.disable());
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http.authorizeRequests()
                .antMatchers("/auth/login/**", "/token/refresh/**").permitAll()
                .antMatchers(POST,"/user").permitAll()
                .antMatchers(GET, "/user/**").hasAnyAuthority("USER_ROLE")
                .antMatchers(GET, "/role/**").hasAnyAuthority("USER_ROLE")
                .antMatchers(GET, "/app/**").hasAnyAuthority("USER_ROLE")
                .antMatchers(GET, "/review/**").hasAnyAuthority("USER_ROLE")
                .antMatchers(GET, "/favapp/**").hasAnyAuthority("USER_ROLE")
                .antMatchers(POST, "/user/**").hasAnyAuthority("ADMIN_ROLE")
                .antMatchers(POST, "/role/**").hasAnyAuthority("ADMIN_ROLE")
                .antMatchers(POST, "/app/**").hasAnyAuthority("ADMIN_ROLE")
                .antMatchers(POST, "/review/**").hasAnyAuthority("ADMIN_ROLE")
                .antMatchers(POST, "/favapp/**").hasAnyAuthority("ADMIN_ROLE")
                .anyRequest().authenticated();
        http.addFilter(customAuthenticationFilter);
        http.addFilterBefore(new CustomAuthorizationFilter(), UsernamePasswordAuthenticationFilter.class)
             .httpBasic(Customizer.withDefaults());
        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManagerBean(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

}
