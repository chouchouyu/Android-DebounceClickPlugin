package com.cm.android.doubleclick.plugin

import com.android.build.api.transform.DirectoryInput
import com.android.build.api.transform.Format
import com.android.build.api.transform.JarInput
import com.android.build.api.transform.QualifiedContent
import com.android.build.api.transform.QualifiedContent.Scope
import com.android.build.api.transform.QualifiedContent.ContentType
import com.android.build.api.transform.Transform
import com.android.build.api.transform.TransformException
import com.android.build.api.transform.TransformInput
import com.android.build.api.transform.TransformInvocation
import com.android.build.api.transform.TransformOutputProvider
import com.android.build.api.transform.Status
import com.android.build.gradle.internal.pipeline.TransformManager
import com.cm.android.doubleclick.plugin.bean.TracedClass
import com.cm.android.doubleclick.plugin.utils.Processor
import org.apache.commons.io.IOUtils
import org.gradle.api.Project
import static com.google.common.base.Preconditions.checkNotNull
import com.android.utils.FileUtils
import com.cm.android.doubleclick.plugin.utils.Utils


class InforsTransform extends Transform {

    Project project
    Map<String, List<TracedClass>> tracedClassesMap
    InforsExtension extension
    private Status status

    InforsTransform(p, tracedClassesMap, extension) {
        this.project = p
        this.tracedClassesMap = tracedClassesMap
        this.extension = extension
    }


    @Override
    String getName() {
        return InforsTransform.simpleName
    }

    @Override
    Set<QualifiedContent.ContentType> getInputTypes() {
        return TransformManager.CONTENT_CLASS
    }

    @Override
    Set<? super QualifiedContent.Scope> getScopes() {
        return TransformManager.SCOPE_FULL_PROJECT
    }

    @Override
    boolean isIncremental() {
        return true
    }

    @Override
    void transform(TransformInvocation invocation) throws TransformException, InterruptedException, IOException {
//        welcome()

        def tracedClassesContainer = []
        tracedClassesMap[invocation.context.variantName] = tracedClassesContainer

        TransformOutputProvider outputProvider = checkNotNull(invocation.getOutputProvider(),
                "Missing output object for transform " + getName())
        if (!invocation.isIncremental()) outputProvider.deleteAll()

        invocation.inputs.each { inputs ->

            inputs.jarInputs.each { jarInput ->

                File inputJar = jarInput.file
                File outputJar = outputProvider.getContentLocation(//
                        jarInput.name,
                        jarInput.contentTypes,
                        jarInput.scopes,
                        Format.JAR)

                project.logger.error('input jar = ' + inputJar.path)
                project.logger.error('output jar = ' + outputJar.path)

                if (invocation.isIncremental()) {

                    status = jarInput.status

                    if (status != Status.NOTCHANGED) {
                        project.logger.error('changed jar =  $ { jarInput.name } : $ { status } ')
                    }

                    switch (status) {
                        case Status.NOTCHANGED:
                            break
                        case Status.ADDED:
                        case Status.CHANGED:
                            Processor.transformJar(project, inputJar, outputJar, tracedClassesContainer,extension)
                            break
                        case Status.REMOVED:
                            FileUtils.delete(outputJar)
                            break
                    }
                } else {
                    Processor.transformJar(project, inputJar, outputJar, tracedClassesContainer,extension)
                }
            }

            inputs.directoryInputs.each { directoryInput ->

                File inputDir = directoryInput.file
                File outputDir = outputProvider.getContentLocation(//
                        directoryInput.name,
                        directoryInput.contentTypes,
                        directoryInput.scopes,
                        Format.DIRECTORY)

                project.logger.error('input directory = ' + inputDir.path)
                project.logger.error('output directory = ' + outputDir.path)


                if (invocation.isIncremental()) {
                    directoryInput.changedFiles.each { File inputFile, Status status ->

                        project.logger.error(" changed file =  ${inputFile.name} : ${status}")

                        switch (status) {
                            case Status.NOTCHANGED:
                                break
                            case Status.ADDED:
                            case Status.CHANGED:
                                if (!inputFile.isDirectory()) {
                                    File outputFile = Utils.toOutputFile(outputDir, inputDir, inputFile)
                                    Processor.transformFile(project, inputFile, outputFile, tracedClassesContainer,extension)
                                }
                                break
                            case Status.REMOVED:
                                File outputFile = Utils.toOutputFile(outputDir, inputDir, inputFile)
                                FileUtils.deleteIfExists(outputFile)
                                break
                        }
                    }
                } else {
                    for (File inputFile : FileUtils.getAllFiles(inputDir)) {
                        File outputFile = Utils.toOutputFile(outputDir, inputDir, inputFile)
                        Processor.transformFile(project, inputFile, outputFile, tracedClassesContainer,extension)
                    }
                }
            }
        }

    }

    def welcome() {
        def stream = InforsTransform.class.getClassLoader().getResourceAsStream('helpContent.groovy')
        def helpContent = new String(IOUtils.toByteArray(stream), 'UTF-8')
        println helpContent
    }
}