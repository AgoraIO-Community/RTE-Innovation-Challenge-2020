package com.xiaoyang.poweroperation.data.basedate;

import com.xiaoyang.poweroperation.data.entity.CodeInfoEntity;
import com.xylib.base.utils.StringUtil;

import java.util.ArrayList;
import java.util.List;

public class BaseData {

    /**
     * 运行状态
     *
     * @return
     */
    public static List<CodeInfoEntity> getRunStatusData() {
        List<CodeInfoEntity> codeList = new ArrayList<>();
        CodeInfoEntity code = null;
        code = new CodeInfoEntity();
        code.setKey("未投运");
        code.setNum(0);
        codeList.add(code);
        code = new CodeInfoEntity();
        code.setKey("投运");
        code.setNum(1);
        codeList.add(code);
        return codeList;
    }


}
