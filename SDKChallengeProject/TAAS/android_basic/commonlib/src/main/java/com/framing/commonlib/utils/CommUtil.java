package com.framing.commonlib.utils;

import android.text.Editable;
import android.text.Selection;
import android.widget.EditText;

public class CommUtil {

    public static void cursorToEnd(EditText et){
        // 把光标移动到最后
        Editable etext = et.getText();
        Selection.setSelection(etext, etext.length());
    }
}
