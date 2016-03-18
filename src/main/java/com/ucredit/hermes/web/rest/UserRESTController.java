package com.ucredit.hermes.web.rest;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.ucredit.hermes.model.User;
import com.ucredit.hermes.service.UserService;

@Controller
@RequestMapping("/rest/users")
public class UserRESTController {
    @Autowired
    private UserService service;

    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public void addUser(@Valid @RequestBody User user) {
        this.service.addUser(user);
    }
}
