package com.cm.android.doubleclick.plugin

import com.android.utils.FileUtils
import com.android.build.gradle.AppExtension
import com.android.build.gradle.AppPlugin
import com.android.build.gradle.api.BaseVariant
import com.cm.android.doubleclick.plugin.bean.TracedClass
import com.cm.android.doubleclick.plugin.utils.Constant
import com.cm.android.doubleclick.plugin.utils.Utils
import org.gradle.api.GradleException
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.Task
import com.android.builder.model.AndroidProject

import java.util.concurrent.TimeUnit

class DoubleClickPlugin implements Plugin<Project> {

    @Override
    void apply(Project project) {

        /* Extensions */
        InforsExtension extension = project.extensions.create(Constant.USER_CONFIG, InforsExtension)

        project.logger.error("   Welcome to ${Constant.TAG}  !");


        /*
            project.repositories.maven {
                url "https://jitpack.io"
            }

            //    project.configurations.implementation.dependencies.add(
            //        project.dependencies.create(project.rootProject.findProject("click-debounce-runtime")))

            project.configurations.implementation.dependencies.add(
                    project.dependencies.create('com.github.SmartDengg:asm-clickdebounce-runtime:1.0.0'))
          */


        final def variants
        def hasApp = project.plugins.withType(AppPlugin)
        if (!hasApp) {
            throw new GradleException("'com.android.application' plugin required.")
        } else {
            variants = project.android.applicationVariants
        }

//        addOkHttp3Aspect(project)


        def tracedClassesMap = new LinkedHashMap<String, List<TracedClass>>()
        AppExtension android = project.extensions.getByType(AppExtension)
        android.registerTransform(new InforsTransform(project, tracedClassesMap, extension))


        project.afterEvaluate {

            Utils.forExtension(android) { variant ->

//                JavaCompile javaCompile = variant.getJavaCompiler()
//                javaCompile.doLast {
//                    println("Aspactj manipulate")
//                    String[] args = ["-showWeaveInfo",
//                                     "-1.8",
//                                     "-inpath", javaCompile.destinationDir.toString(),
//                                     "-aspectpath", javaCompile.classpath.asPath,
//                                     "-d", javaCompile.destinationDir.toString(),
//                                     "-classpath", javaCompile.classpath.asPath,
//                                     "-bootclasspath", project.android.bootClasspath.join(File.pathSeparator)]
//
//                    println("ajc args: " + Arrays.toString(args))
//
//                    MessageHandler handler = new MessageHandler(true);
//                    new Main().run(args, handler)
//
//                    def log = project.logger
//                    for (IMessage message : handler.getMessages(null, true)) {
//                        switch (message.getKind()) {
//                            case IMessage.ABORT:
//                            case IMessage.ERROR:
//                            case IMessage.FAIL:
//                                log.error message.message, message.thrown
//                                break;
//                            case IMessage.WARNING:
//                            case IMessage.INFO:
//                                log.info message.message, message.thrown
//                                break;
//                            case IMessage.DEBUG:
//                                log.debug message.message, message.thrown
//                                break;
//                        }
//                    }
//                }

                createWriteMappingTask(project, variant, tracedClassesMap)
            }
        }
    }

    void createWriteMappingTask(Project project, BaseVariant variant,
                                Map<String, List<TracedClass>> tracedClassesMap) {


        def mappingTaskName = "outputMappingFor${variant.name.capitalize()}"

        //click-debounce-lib-android:  transformClassesWithInforsForRelease
        Task inforsTask = project.tasks["transformClassesWithInforsTransformFor${variant.name.capitalize()}"]

//        SaveCfgTask saveCfgTask = project.tasks.create("infors${variant.name.capitalize()}saveCfg", SaveCfgTask)

//        variant.outputs.first().processResources.dependsOn saveCfgTask

        inforsTask.configure {
            def startTime
            doFirst {
                startTime = System.nanoTime()
            }
            doLast {
                println()
                println " --> COST: ${TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startTime)} ms"
                println()
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
        inforsTask.finalizedBy(outputMappingTask)
        outputMappingTask.onlyIf { inforsTask.didWork }
        outputMappingTask.dependsOn(inforsTask)

    }


}
