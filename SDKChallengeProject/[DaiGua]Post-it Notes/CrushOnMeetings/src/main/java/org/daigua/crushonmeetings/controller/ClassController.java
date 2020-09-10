package org.daigua.crushonmeetings.controller;

import com.google.gson.Gson;
import org.daigua.crushonmeetings.Utils.MD5;
import org.daigua.crushonmeetings.Utils.Shell;
import org.daigua.crushonmeetings.model.ClassModel;
import org.daigua.crushonmeetings.repo.ClassRepo;
import org.hibernate.dialect.AbstractHANADialect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/class")
public class ClassController {

    private static final Logger LOG = LoggerFactory.getLogger(ClassController.class.getClass());

    @Autowired
    ClassRepo classRepo;

    @GetMapping("/join")
    public Object joinClass(String rname, String room, String userId, String time) {
        Map<String, Object> result = new HashMap<>();
        if (time == null) { // join
            ClassModel model = classRepo.findByRid(room);
            String stu = model.getStudent();
            result.put("class", model);
            model.setStudent(stu + userId + ",");
            classRepo.saveAndFlush(model);
        } else { // create
            ClassModel model = new ClassModel();
            model.setRid(room);
            model.setTeacher(userId);
            model.setTime(time);
            model.setStudent(",");
            model.setRname(rname);
            classRepo.saveAndFlush(model);
            result.put("class", model);
        }
        return result;
    }

    @GetMapping("/uploadVideo")
    public Object uploadVideo(String videoName, String room) {
        Map<String, String> result = new HashMap<>();
        ClassModel model = classRepo.findByRid(room);
        model.setVideo(videoName);
        classRepo.saveAndFlush(model);
        return result;
    }

    @GetMapping("/gerUserVideoHistory")
    public Object getHistory(String userId) {
        Map<String, Object> result = new HashMap<>();
        List<ClassModel> classes = classRepo.findAllByStudentContainsOrderByTime("," + userId + ",");
        for (ClassModel classModel : classes) {
            classModel.setCloudpng(getCloudImg(classModel));
        }
        result.put("data", classes);
        return result;
    }

    @GetMapping("/getCloud")
    public Object getCloudImage(String classId) throws InterruptedException {
        Map<String, Object> result = new HashMap<>();
        ClassModel classModel = classRepo.findByRid(classId);
        if (classModel != null) {
            String queryword = classModel.getQueryword();
            String cloud = classModel.getCloudpng();
            String md5ofQueryword = MD5.MD5(queryword);
            if ("".equals(queryword)) {
                result.put("data", "no query words for class " + classId);
            } else if (cloud.contains(md5ofQueryword)) {
                result.put("data", cloud);
            } else {
                String filename = MD5.MD5(queryword) + ".png";
                String[] generateWordCloud = new String[]{
                        "python3", "/root/yuntu/mywordcloud.py", queryword, "/root/daigua/res/" + filename};
                Shell.execShell(generateWordCloud);
                UploadOSS.uploadByFile("/root/daigua/res/" + filename, filename);
                classModel.setCloudpng("https://cmz-daigua.oss-cn-beijing.aliyuncs.com/" + filename);
                classRepo.saveAndFlush(classModel);
                result.put("data", classModel.getCloudpng());
            }
        } else {
            result.put("data", "can not find class " + classId);
        }
        return result;
    }

    private String getCloudImg(ClassModel classModel) {
        String queryword = classModel.getQueryword();
        String cloud = classModel.getCloudpng();
        String md5ofQueryword = MD5.MD5(queryword);
        if ("".equals(queryword)) {
            return "";
        } else if (cloud.contains(md5ofQueryword)) {
            return cloud;
        } else {
            String filename = MD5.MD5(queryword) + ".png";
            String[] generateWordCloud = new String[]{
                    "python3", "/root/yuntu/mywordcloud.py", queryword, "/root/daigua/res/" + filename};
            Shell.execShell(generateWordCloud);
            UploadOSS.uploadByFile("/root/daigua/res/" + filename, filename);
            classModel.setCloudpng("https://cmz-daigua.oss-cn-beijing.aliyuncs.com/" + filename);
            classRepo.saveAndFlush(classModel);
            return classModel.getCloudpng();
        }
    }


}
