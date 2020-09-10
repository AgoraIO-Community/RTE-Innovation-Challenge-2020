package org.daigua.crushonmeetings.controller;

import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.util.Map;

@RestController
@RequestMapping("/cloud")
public class CloudMessagesController {

    private final Logger LOG = LoggerFactory.getLogger(this.getClass());

    @PostMapping(value = "/messagePipe", consumes = "application/json;charset=utf-8", produces = "application/json;charset=utf-8")
    public Object register(@RequestBody Map<String, Object> req) {
        Gson gson = new Gson();
        LOG.info(gson.toJson(req));
        return "noticed";
    }

    @GetMapping("/hello")
    public Object hello() {
        return "hello";
    }


}
