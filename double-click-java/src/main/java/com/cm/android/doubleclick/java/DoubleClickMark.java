package com.cm.android.doubleclick.java;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.RetentionPolicy.CLASS;

@Target({ElementType.METHOD})
@Retention(CLASS)
public @interface DoubleClickMark {
}
