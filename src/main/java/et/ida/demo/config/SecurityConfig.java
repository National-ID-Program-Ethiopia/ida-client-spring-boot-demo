package et.ida.demo.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()   // Disable CSRF
                .httpBasic().disable()  // Disable HTTP Basic Authentication
                .formLogin().disable()  // Disable form login
                .authorizeRequests()
                .antMatchers("/**").permitAll()  // Allow all requests (no authentication)
                .anyRequest().permitAll();
    }
}

