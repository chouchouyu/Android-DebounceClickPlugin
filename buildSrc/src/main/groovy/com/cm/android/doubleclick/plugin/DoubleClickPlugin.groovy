package com.cm.android.doubleclick.plugin

import com.android.build.gradle.AppExtension
import com.android.build.gradle.AppPlugin
import com.android.build.gradle.LibraryPlugin
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

//        final def log = project.logger
//        final def variants
//        if (hasApp) {
//            variants = project.android.applicationVariants
//        } else {
//            variants = project.android.libraryVariants
//        }

        project.extensions["${Constant.EXTENTION}"] = project.objects.newInstance(DoubleClickExtension)

//        variants.all { variant ->
//            if (!variant.buildType.isDebuggable()) {
//                log.debug("Skipping non-debuggable build type '${variant.buildType.name}'.")
//                return;
//            } else if (!project.hugo.enabled) {
//                log.debug("DoubleClickPlugin is not disabled.")
//                return;
//            }
//
//        }


        def variantWeavedClassesMap = new LinkedHashMap<String, List<WeavedClass>>()
        AppExtension appExtension = project.extensions.findByType(AppExtension.class)
        appExtension.registerTransform(new DoubleClickTransform(project, variantWeavedClassesMap, !hasApp))
    }
}