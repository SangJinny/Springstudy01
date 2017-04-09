package kr.co.hanbit.mastering.springmvc4.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * Created by Jeon on 2017-04-05.
 */
@Configuration
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {
    /*
     * APISecurityConfiguration: 첫 번째 구성이며, 기본 인증으로 RESTful 엔드포인트를 보호한다.
     * WebSecurityConfiguration: 애플리케이션의 나머지 부분에 대한 로그인 폼을 구성한다.
     * */
    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http
                .formLogin()
                .loginPage("/login") // 사용자정의된 로그인 페이지
                .defaultSuccessUrl("/profile")
                .and()
                .logout().logoutSuccessUrl("/login")
                .and()
                .authorizeRequests().antMatchers("/webjars/**", "/login", "/signin/**", "/signup").permitAll()
                .anyRequest().authenticated();
    }
}
