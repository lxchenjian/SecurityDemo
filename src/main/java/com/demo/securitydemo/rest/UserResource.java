package com.demo.securitydemo.rest;/**
 * @auther chen
 * @date 2022-10-13 21:58
 */

import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @program: SecurityDemo
 * @description:
 * @author: chen
 * @create: 2022-10-13 21:58
 **/
@RestController
@RequestMapping("/api")
public class UserResource {

    @GetMapping("/greeting")
    public String greeting(){
        return "Hello World";
    }
}
