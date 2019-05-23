package com.cm.android.doubleclick.plugin.temp

class WeavedClass implements Serializable {

    public String className;
    public Set<String> debouncedMethods = new LinkedHashSet<>();

    WeavedClass(String className) {
        this.className = className;
    }

    void addDebouncedMethod(String methodSignature) {
        debouncedMethods.add(methodSignature);
    }

    boolean hasDebouncedMethod() {
        return debouncedMethods.size() > 0;
    }
}
