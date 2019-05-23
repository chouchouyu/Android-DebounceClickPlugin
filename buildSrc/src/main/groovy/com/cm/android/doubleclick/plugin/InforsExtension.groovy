package com.cm.android.doubleclick.plugin

class InforsExtension {
    def includePackages = []
    String appKey
    // when rxjava in use, ajc requires jre rt.java as its classpath. Or an error will be issued.
    def javartNeeded = false

}
