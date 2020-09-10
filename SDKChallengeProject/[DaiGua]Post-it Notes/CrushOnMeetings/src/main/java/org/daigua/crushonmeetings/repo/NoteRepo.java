package org.daigua.crushonmeetings.repo;

import org.daigua.crushonmeetings.controller.NoteController;
import org.daigua.crushonmeetings.model.NoteModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NoteRepo extends JpaRepository<NoteModel, String> {

    NoteModel saveAndFlush(NoteModel model);

    List<NoteModel> findAllByRidAndUidOrderByTime(String rid, String uid);
}
