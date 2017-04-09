package kr.co.hanbit.mastering.springmvc4.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * Created by Jeon on 2017-04-02.
 */
@Configuration
@Order(1)
@EnableGlobalMethodSecurity(securedEnabled = true)
public class ApiSecurityConfiguration extends WebSecurityConfigurerAdapter{
    /* 더 세밀한 보안을 위해 만든 coufiguration */

    /*
     * APISecurityConfiguration: 첫 번째 구성이며, 기본 인증으로 RESTful 엔드포인트를 보호한다.
     * WebSecurityConfiguration: 애플리케이션의 나머지 부분에 대한 로그인 폼을 구성한다.
     * */

    @Autowired
    public void configureAuth(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication()
                .withUser("user").password("user").roles("USER").and()
                .withUser("admin").password("admin").roles("USER", "ADMIN");
    /* 위의 설정은 application properties의 내용을 덮어쓴다. */
    }


    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http
                .antMatcher("/api/**")
                .httpBasic().and().csrf().disable()
                .authorizeRequests()
                .antMatchers(HttpMethod.GET).hasRole("USER")
                .antMatchers(HttpMethod.POST).hasRole("ADMIN")
                .antMatchers(HttpMethod.PUT).hasRole("ADMIN")
                .antMatchers(HttpMethod.DELETE).hasRole("ADMIN")
                .anyRequest().authenticated();
        /*
        http.httpBasic()
                .and().csrf().disable()
                .authorizeRequests()
                .antMatchers("/login", "/logout").permitAll()
                .antMatchers(HttpMethod.GET, "/api/**").hasRole("USER")
                .antMatchers(HttpMethod.POST, "/api/**").hasRole("ADMIN")
                .antMatchers(HttpMethod.PUT, "/api/**").hasRole("ADMIN")
                .antMatchers(HttpMethod.DELETE, "/api/**").hasRole("ADMIN")
                .anyRequest().authenticated();
                */
    }
}
