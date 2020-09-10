package org.daigua.crushonmeetings.controller;

import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.HttpClientUtils;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.daigua.crushonmeetings.model.ClassModel;
import org.daigua.crushonmeetings.repo.ClassRepo;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/spi")
public class SpiderController {

    @Autowired
    ClassRepo classRepo;

    @GetMapping("")
    public Object getUserNote(String query, String room) {
        Map<String, String> result = new HashMap<>();
        result.put("query", query);
        String wiki = "query error";
        try {
            wiki = spiderWord(query);
        } catch (Exception e) {

        }
        result.put("wiki", wiki);

        ClassModel classModel = classRepo.findByRid(room);
        if (classModel.getQueryword()==null)
            classModel.setQueryword(classModel.getQueryword() + " " + query);
        else
            classModel.setQueryword(query);
        classRepo.saveAndFlush(classModel);

        return result;
    }

    private static String spiderWord(String word) throws IOException {
        Document doc = Jsoup.connect("https://baike.baidu.com/item/" + word).get();
        Elements attr = doc.select("meta[name=description]");
        String content = attr.get(0).attr("content");
        return content;
    }
}
