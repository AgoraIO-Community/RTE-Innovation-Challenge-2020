package org.daigua.crushonmeetings.repo;

import org.daigua.crushonmeetings.model.ClassModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ClassRepo extends JpaRepository<ClassModel, String> {

    ClassModel saveAndFlush(ClassModel model);

    ClassModel findByRid(String rid);

    List<ClassModel> findAllByStudentContainsOrderByTime(String str);
}
