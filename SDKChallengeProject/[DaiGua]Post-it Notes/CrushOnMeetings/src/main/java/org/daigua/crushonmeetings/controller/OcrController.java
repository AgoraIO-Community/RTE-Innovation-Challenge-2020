package org.daigua.crushonmeetings.controller;

import org.daigua.crushonmeetings.Utils.Shell;
import org.daigua.crushonmeetings.model.NoteModel;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.*;
import java.util.regex.Pattern;

@RestController
@RequestMapping("/ocr")
public class OcrController {

    @PostMapping("")
    public Object uploadOCR(@RequestParam(value = "file") MultipartFile file) throws IOException {
        Map<String, Object> resultMap = new HashMap<>();
        try {
            String fileExt = file.getOriginalFilename()
                    .substring(file.getOriginalFilename().lastIndexOf(".") + 1)
                    .toLowerCase();
            String pikId = UUID.randomUUID().toString().replaceAll("-", "");
            String newFileName = pikId + "." + fileExt;

            String savePaths = "/root/daigua/res";

            File fileSave = new File(savePaths, newFileName);
            file.transferTo(fileSave);

            String fileInServer = savePaths + "/" + newFileName;
            String fileInContainer = "/home/res/" + newFileName;
            String transToContainer = "docker cp " + fileInServer + " d5f66aea0fd9:" + fileInContainer;
            Shell.execShell(transToContainer);

            String content = sendGet("http://127.0.0.1:8002/ocr", "file=" + fileInContainer);

            resultMap.put("data", deserialize(content));

            return resultMap;

        } catch (Exception e) {
            e.printStackTrace();
            return resultMap;
        }
    }

    public static String sendGet(String url, String param) {
        String result = "";
        BufferedReader in = null;
        try {
            String urlNameString = url + "?" + param;
            URL realUrl = new URL(urlNameString);
            URLConnection connection = realUrl.openConnection();
            connection.setRequestProperty("accept", "*/*");
            connection.setRequestProperty("connection", "Keep-Alive");
            connection.setRequestProperty("user-agent",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            connection.connect();
            Map<String, List<String>> map = connection.getHeaderFields();
            for (String key : map.keySet()) {
                System.out.println(key + "--->" + map.get(key));
            }
            in = new BufferedReader(new InputStreamReader(
                    connection.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            System.out.println("error" + e);
            e.printStackTrace();
        }
        finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        return result;
    }

    private List<Map<String, String>> deserialize(String content) {
        List<Map<String, String>> data = new ArrayList<>();
        Map<String, String> subcontent = null;
        char[] contentArray = content.toCharArray();

        for (int i = 0; i < contentArray.length; i++) {
            if (contentArray[i] == '(') {
                if (subcontent != null) data.add(subcontent);
                subcontent = new HashMap<>();
            } else if (contentArray[i] == '[') {
                i++;
                int match = 1;
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append('[');
                while (match > 0) {
                    if (contentArray[i] == '[') match++;
                    else if (contentArray[i] == ']') match--;
                    stringBuilder.append(contentArray[i++]);
                }
                subcontent.put("pos", stringBuilder.toString());
            } else if (contentArray[i] == '\'') {
                i++;
                StringBuilder stringBuilder = new StringBuilder();
                while (contentArray[i] != '\'') {
                    stringBuilder.append(contentArray[i++]);
                }
                subcontent.put("word", stringBuilder.toString());
            } else if (contentArray[i] == '.' && contentArray[i - 1] == '0') {
                i++;
                StringBuilder stringBuilder = new StringBuilder();
                while (contentArray[i] != ')') {
                    stringBuilder.append(contentArray[i++]);
                }
                subcontent.put("score", "0." + stringBuilder.toString());
            }
        }
        if (subcontent != null) data.add(subcontent);
        return data;
    }
}
