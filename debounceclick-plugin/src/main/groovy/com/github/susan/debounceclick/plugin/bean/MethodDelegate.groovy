package com.github.susan.debounceclick.plugin.bean;

class MethodDelegate {

    private int access
    private String name
    private String desc

    MethodDelegate(int access, String name, String desc) {
        this.access = access
        this.name = name
        this.desc = desc
    }

    int getAccess() {
        return access
    }

    String getName() {
        return name
    }

    String getDesc() {
        return desc
    }

    boolean match(int access, String name, String desc) {
        return this.access == access &&
                Objects.equals(this.name, name) &&
                Objects.equals(this.desc, desc)
    }
}
