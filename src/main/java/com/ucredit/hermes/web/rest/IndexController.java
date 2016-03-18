/**
 * Copyright(c) 2011-2011 by YouCredit Inc.
 * All Rights Reserved
 */
package com.ucredit.hermes.web.rest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @author ijay
 */
@Controller
public class IndexController {
    @RequestMapping(method = RequestMethod.GET, value = "/")
    public String getIndexPage() {
        return "index";
    }

    @RequestMapping(method = RequestMethod.GET, value = "/{path}")
    public String redirect(@PathVariable String path) {
        return path;
    }
}
