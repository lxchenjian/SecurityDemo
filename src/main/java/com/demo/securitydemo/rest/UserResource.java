package com.demo.securitydemo.rest;/**
 * @auther chen
 * @date 2022-10-13 21:58
 */

import lombok.Data;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("/greeting")
    public String makeGreeting(@RequestParam String name,@RequestBody Profile profile){

        return "Hello World"+name+"!\n"+profile.gender;
    }

    @PutMapping("/putgreeting/{name}")
    public String putGreeting(@PathVariable String name){
        return "Hello World"+name;
    }




    @Data
    static class Profile{
        private String gender;
        private String idNo;
    }

}
