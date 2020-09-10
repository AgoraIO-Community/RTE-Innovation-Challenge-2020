package org.daigua.crushonmeetings.token;

import org.daigua.crushonmeetings.token.media.RtcTokenBuilder;
import org.daigua.crushonmeetings.token.media.RtcTokenBuilder.Role;
import org.junit.Test;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/token")
public class RtcTokenBuilderController {
    static String appCertificate = "4afd5f30bb0c4f0e941a58cc594b007e";
    static int expirationTimeInSeconds = 3600;

    @GetMapping("/getToken")
    public Object getToken(String appid, String uid, String channelName) {
        Map<String, String> map = new HashMap<>();
        RtcTokenBuilder token = new RtcTokenBuilder();
        int timestamp = (int) (System.currentTimeMillis() / 1000 + expirationTimeInSeconds);
        String result = token.buildTokenWithUid(appid, appCertificate,
                channelName, Integer.parseInt(uid), Role.Role_Publisher, timestamp);
        map.put("token", result);
        return map;
    }
}
