package org.daigua.crushonmeetings.controller;

import org.daigua.crushonmeetings.model.NoteModel;
import org.daigua.crushonmeetings.repo.NoteRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/note")
public class NoteController {

    private final Logger LOG = LoggerFactory.getLogger(this.getClass());

    @Autowired
    NoteRepo noteRepo;

    @PostMapping("/noteUpdate")
    public Object uploadNote(@RequestParam(value = "file") MultipartFile file, String userId, String room, String time) throws IOException {
        Map<String, String> result = new HashMap<>();
        String fileName = userId + "-" + room + "-" + time + ".png";
        if (UploadOSS.uploadByInputStream(file.getInputStream(), fileName)) {
            NoteModel model = new NoteModel();
            model.setRid(room);
            model.setUid(userId);
            model.setNote(fileName);
            model.setTime(time);
            result.put("file", noteRepo.saveAndFlush(model).getNote());
        } else {

        }
        return result;
    }

    @GetMapping("/getUserNote")
    public Object getUserNote(String userId,String room){
        Map<String, Object> result = new HashMap<>();
        List<Map<String,String>> list = new ArrayList<>();
        List<NoteModel> query = noteRepo.findAllByRidAndUidOrderByTime(room,userId);
        for (NoteModel model:query){
            Map<String, String> map = new HashMap<>();
            map.put("note", model.getNote());
            map.put("time", model.getTime());
            list.add(map);
        }
        result.put("data", list);
        return result;
    }
}
