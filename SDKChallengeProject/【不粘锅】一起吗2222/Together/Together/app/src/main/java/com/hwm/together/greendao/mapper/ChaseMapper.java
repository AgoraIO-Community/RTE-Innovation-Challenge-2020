package com.hwm.together.greendao.mapper;

import android.content.Context;

import com.hwm.together.MyApplication;
import com.hwm.together.greendao.ChaseDao;
import com.hwm.together.greendao.entity.Chase;

import java.util.List;

public class ChaseMapper {

    private Context mContext;
    private ChaseDao chaseDao = MyApplication.getInstance().getDaoSession().getChaseDao();

    private ChaseMapper(Context context){
        mContext = context;
    }

    private static volatile ChaseMapper instance = null;

    public static ChaseMapper getInstance(Context context){
        if (instance == null){
            synchronized (ChaseMapper.class){
                instance = new ChaseMapper(context);
            }
        }
        return instance;
    }

    //添加回答
    public void insertAnswer(Chase chase){
        chaseDao.insert(chase);
    }

    //删除回答
    public void deleteAnswer(Chase chase){
        //        answerDao.deleteByKey(questionId);
        chaseDao.delete(chase);
    }

    //修改
    public void updateAnswer(Chase chase){
        chaseDao.update(chase);
    }

    /*
    //根据问题id查询回答
    public Chase selectById(Long questionId,Long choiceId){
        //        return answerDao.queryBuilder().and(AnswerDao.Properties.QuestionId.eq(questionId),AnswerDao.Properties.ChoiceId.eq(choiceId));
        return chaseDao.queryBuilder()
                .where(chaseDao.queryBuilder()
                        .and(ChaseDao.Properties.Id.eq(questionId),ChaseDao.Properties.ChoiceId.eq(choiceId))).unique();
    }
     */

    //查询所有
    public List<Chase> queryAllAnswer(){
        return chaseDao.queryBuilder().build().list();
    }
}
