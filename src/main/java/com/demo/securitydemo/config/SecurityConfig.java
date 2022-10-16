package com.demo.securitydemo.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


@Slf4j
@EnableWebSecurity(debug = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {


    /***
     * 配置真正路径的安全性*
     * @param http
     * @throws Exception
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
    //配置一：
        //        http
    //                .formLogin(Customizer.withDefaults())
    //            .authorizeRequests(authorizeRequests -> authorizeRequests
    //                    //There was an unexpected error (type=Forbidden, status=403).
    //                    //.antMatchers("/api/**").hasRole("ADMIN")
    //                    .antMatchers("/api/**").authenticated()
    //            );

    //配置二：
        //post请求 报错CSRF Invalid CSRF token found for
//        http.csrf(csrf ->csrf.disable()).httpBasic(Customizer.withDefaults())
//                .formLogin(form ->form.loginPage("/"));

        //配置三：
        http.authorizeHttpRequests(
                req ->req.anyRequest().authenticated())
                //.formLogin(AbstractHttpConfigurer::disable)
                .formLogin(form->form.loginPage("/login").permitAll()
                        .defaultSuccessUrl("/")
                        .successHandler((req,res,auth) ->{
                            ObjectMapper objectMapper = new ObjectMapper();
                            res.setStatus(HttpStatus.OK.value());
                            res.getWriter().println(objectMapper.writeValueAsString(auth));
                            log.debug("认证成功");
                        })
                        .failureHandler((req,res,exp) -> {
                            val objectMapper = new ObjectMapper();
                            res.setStatus(HttpStatus.UNAUTHORIZED.value());
                            res.setContentType(MediaType.APPLICATION_JSON_VALUE);
                            res.setCharacterEncoding("UTF-8");
                            Map map = new HashMap();
                            map.put("title","认证失败");
                            map.put("details",exp.getMessage());
                            val errData = map;
                            res.getWriter().println(objectMapper.writeValueAsString(errData));
                        })
                )

                .csrf(Customizer.withDefaults())
                .logout(logout ->logout.logoutUrl("/perform_logout"))
                .rememberMe(rememberMe -> rememberMe
                .key("someSecret")
                .tokenValiditySeconds(86400))
                .httpBasic(Customizer.withDefaults()); // 显示浏览器对话框，需要禁用 CSRF ，或添加路径到忽略列表

    }


    /***
     * 配置静态资源的忽略
     * * @param web
     * @throws Exception
     */
    @Override
    public void configure(WebSecurity web) throws Exception{
                //界面 安全验证
        web.ignoring().mvcMatchers("/public/**","/error")
                //解决样式问题
                .requestMatchers(PathRequest.toStaticResources().atCommonLocations());

    }
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws  Exception{
        auth.inMemoryAuthentication()
                .withUser("user")
                .password(passwordEncoder().encode("12345678"))
                .roles("USER","ADMIN");

    }

    @Bean
    PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

}
