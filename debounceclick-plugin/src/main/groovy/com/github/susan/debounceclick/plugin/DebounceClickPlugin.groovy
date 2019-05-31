package com.github.susan.debounceclick.plugin

import com.android.build.gradle.LibraryPlugin
import com.android.utils.FileUtils
import com.android.build.gradle.AppExtension
import com.android.build.gradle.AppPlugin
import com.android.build.gradle.api.BaseVariant
import com.github.susan.debounceclick.plugin.bean.TracedClass
import com.github.susan.debounceclick.plugin.utils.Constant
import com.github.susan.debounceclick.plugin.utils.Logger
import com.github.susan.debounceclick.plugin.utils.Utils
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.Task
import com.android.builder.model.AndroidProject

import java.util.concurrent.TimeUnit

class DebounceClickPlugin implements Plugin<Project> {

    @Override
    void apply(Project project) {

        /* Extensions */
        DebounceClickExtension extension = project.extensions.create(Constant.USER_CONFIG, DebounceClickExtension)

        project.logger.error("   Welcome to ${Constant.TAG}  !");



        def hasApp = project.plugins.withType(AppPlugin)
        def hasLib = project.plugins.withType(LibraryPlugin)
        if (!hasApp && !hasLib) {
            throw new IllegalStateException("'android' or 'android-library' plugin required.")
        }


        project.dependencies {
            compile 'com.github.susan:debounceclick:1.0.1'
        }

        def tracedClassesMap = new LinkedHashMap<String, List<TracedClass>>()
        AppExtension android = project.extensions.getByType(AppExtension)
        android.registerTransform(new DebounceClickTransform(project, tracedClassesMap, !hasLib))


        project.afterEvaluate {
            //logger enable
            Logger.setDebug(extension.debug)

            Utils.forExtension(android) { variant ->

                createWriteMappingTask(project, variant, tracedClassesMap)
            }
        }
    }

    void createWriteMappingTask(Project project, BaseVariant variant,
                                Map<String, List<TracedClass>> tracedClassesMap) {


        def mappingTaskName = "outputMappingFor${variant.name.capitalize()}"

        Task mappingTask = project.tasks["transformClassesWithDebounceClickTransformFor${variant.name.capitalize()}"]

        mappingTask.configure {
            def startTime
            doFirst {
                startTime = System.nanoTime()
            }
            doLast {
                Logger.info(">>>>>>>>>>>>>>>>>>>>>>>>")
                Logger.info(" --> COST: ${TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startTime)} ms")
                Logger.info("<<<<<<<<<<<<<<<<<<<<<<<<")
            }
        }


        Task outputMappingTask = project.tasks.create(//
                name: "${mappingTaskName}",
                type: OutputMappingTask) {

            classes = tracedClassesMap

            variantName = variant.name

            outputMappingFile =
                    FileUtils.join(project.buildDir, AndroidProject.FD_OUTPUTS, Constant.USER_CONFIG, 'mapping',
                            variant.name, Constant.USER_CONFIG + 'Mapping.txt')
        }
        mappingTask.finalizedBy(outputMappingTask)
        outputMappingTask.onlyIf { mappingTask.didWork }
        outputMappingTask.dependsOn(mappingTask)

    }


}
