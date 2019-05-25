package com.github.susan.clickdebounce.plugin.bean;


class TracedClass implements Serializable {

    private String className
    private Set<String> tracedMethods = new LinkedHashSet<>()

    TracedClass(String className) {
        this.className = className
    }

    void addTracedMethod(String methodSignature) {
        tracedMethods.add(methodSignature)
    }

    String getClassName() {
        return className
    }

    Set<String> getTracedMethods() {
        return tracedMethods
    }

    boolean hasTracedMethod() {
        return tracedMethods.size() > 0
    }
}
