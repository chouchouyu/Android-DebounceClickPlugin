package com.cm.android.doubleclick.plugin.temp

class Constant {
    /** 版本号 */
    def static final VER = "1.0.0"

    /** 打印信息时候的前缀 */
    def static final TAG = "< DoubleClick-plugin-v${VER} >"

    /** 外部用户配置信息 */
    def static final EXTENTION = "DoubleClick"


    public static final String trackAnnoClassName = "Lcom/cm/android/doubleclick/java/DoubleClickMark";

    public static final String agentClassName = "com/cm/android/doubleclick/java/DebouncedPredictor";

}
