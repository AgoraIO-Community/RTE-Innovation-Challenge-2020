package com.xylib.base.widgets.font;

import android.graphics.Typeface;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Zyj on 2016/12/23.
 */
public class FontManager {

    /**
     * iconFont
     */
    public static final String ICON_FONT = "iconfont.ttf";

    /**
     * normalFont
     */
    public static final String NORMAL_FONT = "/font/normal";

    /**
     * lightFont
     */
    public static final String LIGHT_FONT = "/font/light";

    /**
     * mediumFont
     */
    public static final String MEDIUM_FONT = "/font/medium";

    /**
     * boldFont
     */
    public static final String BOLD_FONT = "/font/bold";

    private Map<String, Typeface> typefaceMap;

    private static FontManager instance = new FontManager();
    private FontPlugin plugin;
    private boolean isInit;

    public static FontManager getInstance() {
        return instance;
    }

    private FontManager() {
        typefaceMap = new HashMap<>();
    }

    public boolean isInit() {
        return isInit;
    }

    public void init(FontPlugin fontPlugin) {
        this.plugin = fontPlugin;
        typefaceMap.put(NORMAL_FONT, plugin.getTypefaceByKey(NORMAL_FONT));
        typefaceMap.put(LIGHT_FONT, plugin.getTypefaceByKey(LIGHT_FONT));
        typefaceMap.put(MEDIUM_FONT, plugin.getTypefaceByKey(MEDIUM_FONT));
        typefaceMap.put(BOLD_FONT, plugin.getTypefaceByKey(BOLD_FONT));
        typefaceMap.put(ICON_FONT, plugin.getTypefaceByKey(ICON_FONT));
        isInit = true;
    }

    public Typeface getIconTypeface() {
        return getTypeface(ICON_FONT);
    }

    public Typeface getNormalTypeface() {
        return getTypeface(NORMAL_FONT);
    }

    public Typeface getLightTypeface() {
        return getTypeface(LIGHT_FONT);
    }

    public Typeface getMediumTypeface() {
        return getTypeface(MEDIUM_FONT);
    }

    public Typeface getBoldTypeface() {
        return getTypeface(BOLD_FONT);
    }

    private Typeface getTypeface(String key) {
        if (plugin == null)
            return null;
        if (typefaceMap == null)
            return null;
        Typeface typeface = typefaceMap.get(key);
        if (typeface == null) {
            typeface = plugin.getTypefaceByKey(key);
        }
        typefaceMap.put(key, typeface);
        return typeface;
    }

}
