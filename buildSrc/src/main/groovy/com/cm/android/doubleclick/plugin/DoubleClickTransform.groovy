package com.cm.android.doubleclick.plugin

import com.android.build.api.transform.Format
import com.android.build.api.transform.QualifiedContent
import com.android.build.api.transform.Status
import com.android.build.api.transform.Transform
import com.android.build.api.transform.TransformException
import com.android.build.api.transform.TransformInvocation
import com.android.build.api.transform.TransformOutputProvider
import com.android.build.gradle.internal.pipeline.TransformManager
import com.cm.android.doubleclick.plugin.temp.Constant
import com.cm.android.doubleclick.plugin.temp.DoubleClickExtension
import com.cm.android.doubleclick.plugin.temp.Utils
import com.cm.android.doubleclick.plugin.temp.WeavedClass
import com.google.common.base.Joiner
import com.google.common.collect.ImmutableList
import org.gradle.api.Project

import java.nio.file.Files
import java.nio.file.Path

import static com.android.builder.model.AndroidProject.FD_OUTPUTS
import static com.google.common.base.Preconditions.checkNotNull

class DoubleClickTransform extends Transform {

    Project project
    Map<String, List<WeavedClass>> weavedVariantClassesMap
    File debounceOutDir
    def isliberary

    DoubleClickTransform(Project project,
                         Map<String, List<WeavedClass>> weavedVariantClassesMap, boolean isliberary) {
        this.project = project
        this.weavedVariantClassesMap = weavedVariantClassesMap
        this.isliberary = isliberary
        this.debounceOutDir = new File(Joiner.on(File.separatorChar).join(project.buildDir,
                FD_OUTPUTS,
                Constant.EXTENTION,
                'logs'))
        project.logger.error("isliberary: ${isliberary} ")
    }


    @Override
    String getName() {
        return Constant.EXTENTION
    }

    @Override
    Set<QualifiedContent.ContentType> getInputTypes() {
        return TransformManager.CONTENT_CLASS
    }

    @Override
    Set<QualifiedContent.Scope> getScopes() {
        if (isliberary) return TransformManager.PROJECT
        return TransformManager.SCOPE_FULL_PROJECT

    }

    @Override
    boolean isIncremental() {
        return true
    }

    @Override
    Collection<File> getSecondaryDirectoryOutputs() {
        return ImmutableList.of(debounceOutDir)
    }

    @Override
    void transform(TransformInvocation invocation) throws TransformException, InterruptedException, IOException {
        def weavedClassesContainer = []
        weavedVariantClassesMap[invocation.context.variantName] = weavedClassesContainer

        TransformOutputProvider outputProvider = checkNotNull(invocation.getOutputProvider(),
                "Missing output object for run " + getName())
        if (!invocation.isIncremental()) outputProvider.deleteAll()


        invocation.inputs.each { inputs ->
            inputs.jarInputs.each { jarInput ->

                Path inputPath = jarInput.file.toPath()
                Path outputPtah = outputProvider.getContentLocation(//
                        jarInput.name,
                        jarInput.contentTypes,
                        jarInput.scopes,
                        Format.JAR).toPath()

                /** *************************************************************/
                project.logger.error("INPUT: ${inputPath.toString()}")
                project.logger.error("CHANGED: ${jarInput.status} ")
                project.logger.error("OUTPUT: ${outputPtah.toString()} ")
                project.logger.error("INCREMENTAL: ${invocation.isIncremental()}")
                /** *************************************************************/

                if (invocation.isIncremental()) {

                    switch (jarInput.status) {
                        case Status.NOTCHANGED:
                            break
                        case Status.ADDED:
                        case Status.CHANGED:
                            Files.deleteIfExists(outputPtah)
                            Processor.run(inputPath, outputPtah, weavedClassesContainer, Processor.FileType.JAR)
                            break
                        case Status.REMOVED:
                            Files.deleteIfExists(outputPtah)
                            break
                    }
                } else {
                    Processor.run(project, inputPath, outputPtah, weavedClassesContainer, Processor.FileType.JAR)
                }
            }

            inputs.directoryInputs.each { directoryInput ->

                Path inputRoot = directoryInput.file.toPath()
                Path outputRoot = outputProvider.getContentLocation(//
                        directoryInput.name,
                        directoryInput.contentTypes,
                        directoryInput.scopes,
                        Format.DIRECTORY).toPath()

                /** *************************************************************/
                project.logger.error("INPUT: ${inputRoot.toString()} ")
                project.logger.error("CHANGED: ${directoryInput.changedFiles.size()} ")
                project.logger.error("OUTPUT: ${outputRoot.toString()} ")
                project.logger.error("INCREMENTAL: ${invocation.isIncremental()}")
                /** *************************************************************/

                if (invocation.isIncremental()) {
                    directoryInput.changedFiles.each { File inputFile, Status status ->

                        Path inputPath = inputFile.toPath()
                        Path outputPath = Utils.toOutputPath(outputRoot, inputRoot, inputPath)

                        switch (status) {
                            case Status.NOTCHANGED:
                                break
                            case Status.ADDED:
                            case Status.CHANGED:
                                //direct run byte code
                                Processor.directRun(project,inputPath, outputPath, weavedClassesContainer)
                                break
                            case Status.REMOVED:
                                Files.deleteIfExists(outputPath)
                                break
                        }
                    }
                } else {
                    Processor.run(project, inputRoot, outputRoot, weavedClassesContainer, Processor.FileType.FILE)
                }
            }
        }


    }
}
