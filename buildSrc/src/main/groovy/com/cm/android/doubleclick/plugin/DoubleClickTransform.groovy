package com.cm.android.doubleclick.plugin

import com.android.build.api.transform.QualifiedContent
import com.android.build.api.transform.Transform
import com.android.build.api.transform.TransformException
import com.android.build.api.transform.TransformInvocation
import com.android.build.api.transform.TransformOutputProvider
import com.android.build.gradle.internal.pipeline.TransformManager
import com.google.common.base.Joiner
import com.google.common.collect.ImmutableList
import org.apache.commons.io.FileUtils
import org.gradle.api.Project

import static com.android.builder.model.AndroidProject.FD_OUTPUTS
import static com.google.common.base.Preconditions.checkNotNull

class DoubleClickTransform extends Transform {

    Project project
    DoubleClickExtension extention
    Map<String, List<WeavedClass>> weavedVariantClassesMap
    File debounceOutDir

    DoubleClickTransform(Project project,
                         Map<String, List<WeavedClass>> weavedVariantClassesMap) {
        this.project = project
        this.extention = project."${Constant.EXTENTION}"
        this.weavedVariantClassesMap = weavedVariantClassesMap
        this.debounceOutDir = new File(Joiner.on(File.separatorChar).join(project.buildDir,
                FD_OUTPUTS,
                Constant.EXTENTION,
                'logs'))
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
//        def weavedClassesContainer = []
//        weavedVariantClassesMap[invocation.context.variantName] = weavedClassesContainer

//        TransformOutputProvider outputProvider = checkNotNull(invocation.getOutputProvider(),
//                "Missing output object for run " + getName())
//        if (!invocation.isIncremental()) outputProvider.deleteAll()

//        File changedFiles = new File(debounceOutDir,
//                Joiner.on(File.separatorChar).join(invocation.context.variantName, 'files.txt'))
//        FileUtils.touch(changedFiles)

//        PrintWriter writer = PrintWriterUtil.createPrintWriterOut(changedFiles)
    }
}
