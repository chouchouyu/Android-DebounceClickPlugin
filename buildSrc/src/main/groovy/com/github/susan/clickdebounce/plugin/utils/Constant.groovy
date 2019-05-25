package com.github.susan.clickdebounce.plugin.utils

class Constant {
    /** 版本号 */
    def static final VER = "1.0.0"

    /** 打印信息时候的前缀 */
    def static final TAG = "< " + USER_CONFIG + "-plugin-v${VER} >"

    /** 外部用户配置信息 */
    def static final USER_CONFIG = "DebounceClick"

    public static
    final String agentClassName = "com/github/susan/clickdebounce/java/ClickDebounceHandler";

    public static
    final String trackAnnoClassName = "Lcom/github/susan/clickdebounce/java/ClickDebounceMark;"
    public static
    final String extraAnnoClassName = "Lcom/github/susan/clickdebounce/java/ClickDebounceExtra;"

}
