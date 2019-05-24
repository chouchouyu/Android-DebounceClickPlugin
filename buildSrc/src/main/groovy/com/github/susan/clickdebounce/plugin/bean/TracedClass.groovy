package com.github.susan.clickdebounce.plugin.bean;

import java.io.Serializable;
import java.util.LinkedHashSet;
import java.util.Set;

public class TracedClass implements Serializable {

    private String className;
    private Set<String> tracedMethods = new LinkedHashSet<>();

    TracedClass(String className) {
        this.className = className;
    }

    void addTracedMethod(String methodSignature) {
        tracedMethods.add(methodSignature);
    }

    public String getClassName() {
        return className;
    }

    public Set<String> getTracedMethods() {
        return tracedMethods;
    }

    public boolean hasTracedMethod() {
        return tracedMethods.size() > 0;
    }
}
