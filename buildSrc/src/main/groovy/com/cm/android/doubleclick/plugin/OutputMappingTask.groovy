package com.cm.android.doubleclick.plugin

import com.cm.android.doubleclick.plugin.bean.TracedClass
import com.cm.android.doubleclick.plugin.utils.Constant
import org.apache.commons.io.FileUtils
import org.gradle.api.DefaultTask
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.Internal
import org.gradle.api.tasks.OutputFile
import org.gradle.api.tasks.TaskAction

class OutputMappingTask extends DefaultTask {
    {
        group = Constant.USER_CONFIG
        description = ' mapping file for view which prevent from debounces multiple clicks posted in a short time'
    }

    @Input
    Property<String> variantName = project.objects.property(String.class)

    @OutputFile
    RegularFileProperty outputMappingFile = newOutputFile()

    @Internal
    Property<Map> classes = project.objects.property(Map.class)

    @TaskAction
    void wrireMapping() {

        def mappingFile = outputMappingFile.get().asFile

        FileUtils.touch(mappingFile)

        mappingFile.withWriter { writer ->

            classes.get()[variantName.get()].findAll { TracedClass tracedClass ->
                tracedClass.hasTracedMethod()
            }.each { touchedTracedClass ->

                String className = touchedTracedClass.className
                Set<String> debouncedMethods = touchedTracedClass.tracedMethods
                writer.writeLine "$className"

                println className

                for (def methodSignature in debouncedMethods) {
                    writer.writeLine "    \u21E2  $methodSignature"
                    println "    \u21E2  $methodSignature"
                }
            }
        }
        project.logger.error("Success wrote TXT mapping report to file://${outputMappingFile}")
    }
}