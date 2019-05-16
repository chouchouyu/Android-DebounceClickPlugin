package com.cm.android.doubleclick.plugin.temp

class WeavedClass implements Serializable {

    public String className;
    public Set<String> debouncedMethods = new LinkedHashSet<>();

    public WeavedClass(String className) {
        this.className = className;
    }

    public void addDebouncedMethod(String methodSignature) {
        debouncedMethods.add(methodSignature);
    }

    public boolean hasDebouncedMethod() {
        return debouncedMethods.size() > 0;
    }
}
