package dev.pethaven.configs;

import dev.pethaven.auth.AuthEntryPointJwt;
import dev.pethaven.auth.AuthTokenFilter;
import dev.pethaven.enums.Role;
import dev.pethaven.mappers.*;
import dev.pethaven.services.AuthDetailsServiceImpl;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.cors.CorsConfiguration;

@EnableWebSecurity
@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    AuthDetailsServiceImpl authDetailsService;
    private final AuthEntryPointJwt unauthorizedHandler;
    @Lazy
    @Autowired
    public WebSecurityConfig(AuthDetailsServiceImpl authDetailsService, AuthEntryPointJwt unauthorizedHandler) {
        this.authDetailsService = authDetailsService;
        this.unauthorizedHandler = unauthorizedHandler;
    }

    @Bean
    public AuthTokenFilter authenticationJwtTokenFilter() {
        return new AuthTokenFilter();
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
        authenticationManagerBuilder.userDetailsService(authDetailsService).passwordEncoder(passwordEncoder());
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthDetailsServiceImpl userDetailsService() {
        return new AuthDetailsServiceImpl();
    }
    @Bean
    public PetMapper petMapper() {return Mappers.getMapper(PetMapper.class);}
    @Bean
    public UserMapper userMapper() {return Mappers.getMapper(UserMapper.class);}
    @Bean
    public OrganizationMapper organizationMapper() {return Mappers.getMapper(OrganizationMapper.class);}
    @Bean
    public ChatMapper chatMapper() {return Mappers.getMapper(ChatMapper.class);}
    @Bean
    public MessageMapper messageMapper() {return Mappers.getMapper(MessageMapper.class);}

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors().and().csrf().disable()
                .exceptionHandling().authenticationEntryPoint(unauthorizedHandler).and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                .authorizeRequests()
                .antMatchers("/api/login/**").permitAll()
                .antMatchers("/api/signup/**").permitAll()
                .antMatchers("/api/profile/organization").hasAnyAuthority(Role.ORG.name())
                .antMatchers("/api/profile/user").hasAnyAuthority(Role.USER.name(), Role.ADMIN.name())
                .antMatchers("/api/home/**").permitAll()
                .antMatchers("/api/chats/**").hasAnyAuthority(Role.ORG.name(), Role.USER.name())
                .anyRequest().authenticated();

        http.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);
        http.cors().disable();
    }
}