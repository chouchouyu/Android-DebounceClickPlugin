package com.cm.android.doubleclick.plugin

import com.android.build.gradle.AppExtension
import com.android.build.gradle.AppPlugin
import com.android.build.gradle.LibraryPlugin
import com.cm.android.doubleclick.plugin.DoubleClickTransform
import com.cm.android.doubleclick.plugin.temp.Constant
import com.cm.android.doubleclick.plugin.temp.DoubleClickExtension
import com.cm.android.doubleclick.plugin.temp.WeavedClass
import org.gradle.api.Plugin
import org.gradle.api.Project

class DoubleClickPlugin implements Plugin<Project> {
    @Override
    void apply(Project project) {

        def hasApp = project.plugins.withType(AppPlugin)
        def hasLib = project.plugins.withType(LibraryPlugin)
        if (!hasApp && !hasLib) {
            throw new IllegalStateException("'android' or 'android-library' plugin required.")
        }

//        project.dependencies {
//            compile project(':double-click-java')
//        }

        project.extensions["${Constant.EXTENTION}"] = project.objects.newInstance(DoubleClickExtension)


        def variantWeavedClassesMap = new LinkedHashMap<String, List<WeavedClass>>()
        AppExtension appExtension = project.extensions.findByType(AppExtension.class)
        appExtension.registerTransform(new DoubleClickTransform(project, variantWeavedClassesMap, !hasApp))
    }
}