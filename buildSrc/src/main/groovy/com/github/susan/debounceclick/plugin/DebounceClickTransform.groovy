package com.github.susan.debounceclick.plugin


import com.android.build.api.transform.Format
import com.android.build.api.transform.QualifiedContent
import com.android.build.api.transform.Transform
import com.android.build.api.transform.TransformException
import com.android.build.api.transform.TransformInvocation
import com.android.build.api.transform.TransformOutputProvider
import com.android.build.api.transform.Status
import com.android.build.gradle.internal.pipeline.TransformManager
import com.github.susan.debounceclick.plugin.bean.TracedClass
import com.github.susan.debounceclick.plugin.utils.Logger
import com.github.susan.debounceclick.plugin.utils.Processor
import org.gradle.api.Project
import static com.google.common.base.Preconditions.checkNotNull
import com.android.utils.FileUtils
import com.github.susan.debounceclick.plugin.utils.Utils


class DebounceClickTransform extends Transform {

    Project project
    Map<String, List<TracedClass>> tracedClassesMap
    private Status status
    private boolean isApp

    DebounceClickTransform(p, tracedClassesMap, isApp) {
        this.project = p
        this.tracedClassesMap = tracedClassesMap
        this.isApp = isApp
    }


    @Override
    String getName() {
        return DebounceClickTransform.simpleName
    }

    @Override
    Set<QualifiedContent.ContentType> getInputTypes() {
        return TransformManager.CONTENT_CLASS
    }

    @Override
    Set<? super QualifiedContent.Scope> getScopes() {
        if (isApp) return TransformManager.SCOPE_FULL_PROJECT
        return TransformManager.SCOPE_FULL_LIBRARY
    }

    @Override
    boolean isIncremental() {
        return true
    }

    @Override
    void transform(TransformInvocation invocation) throws TransformException, InterruptedException, IOException {

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

                Logger.info('input jar = ' + inputJar.path)
                Logger.info('output jar = ' + outputJar.path)

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
                            Processor.transformJar(project, inputJar, outputJar, tracedClassesContainer)
                            break
                        case Status.REMOVED:
                            FileUtils.delete(outputJar)
                            break
                    }
                } else {
                    Processor.transformJar(project, inputJar, outputJar, tracedClassesContainer)
                }
            }

            inputs.directoryInputs.each { directoryInput ->

                File inputDir = directoryInput.file
                File outputDir = outputProvider.getContentLocation(//
                        directoryInput.name,
                        directoryInput.contentTypes,
                        directoryInput.scopes,
                        Format.DIRECTORY)

                Logger.info('input directory = ' + inputDir.path)
                Logger.info('output directory = ' + outputDir.path)


                if (invocation.isIncremental()) {
                    directoryInput.changedFiles.each { File inputFile, Status status ->

                        Logger.info(" changed file =  ${inputFile.name} : ${status}")

                        switch (status) {
                            case Status.NOTCHANGED:
                                break
                            case Status.ADDED:
                            case Status.CHANGED:
                                if (!inputFile.isDirectory()) {
                                    File outputFile = Utils.toOutputFile(outputDir, inputDir, inputFile)
                                    Processor.transformFile(project, inputFile, outputFile, tracedClassesContainer)
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
                        Processor.transformFile(project, inputFile, outputFile, tracedClassesContainer)
                    }
                }
            }
        }

    }

}
