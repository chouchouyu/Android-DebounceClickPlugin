package com.cm.android.doubleclick.plugin

import com.cm.android.doubleclick.plugin.temp.CompactClassWriter
import com.cm.android.doubleclick.plugin.temp.MethodDelegate
import com.cm.android.doubleclick.plugin.temp.PreCheckVisitorAdapter
import com.cm.android.doubleclick.plugin.temp.Utils
import com.cm.android.doubleclick.plugin.temp.WeavedClass
import com.google.common.collect.ImmutableMap
import com.google.common.collect.Iterables
import org.gradle.api.GradleException
import org.gradle.api.Project
import org.objectweb.asm.ClassReader
import org.objectweb.asm.ClassWriter

import java.nio.file.FileSystems
import java.nio.file.FileVisitResult
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.SimpleFileVisitor
import java.nio.file.attribute.BasicFileAttributes

class Processor {
    enum FileType {
        JAR,
        FILE
    }

    static void run(Project project, Path input, Path output, List<WeavedClass> weavedClasses,
                    FileType fileType) throws IOException {

        switch (fileType) {
            case FileType.JAR:
                processJar(project, input, output, weavedClasses)
                break

            case FileType.FILE:
                processFile(project, input, output, weavedClasses)
                break
        }
    }

    private static void processJar(Project project, Path input, Path output, List<WeavedClass> weavedClasses) {
        Map<String, String> env = ImmutableMap.of('create', 'true')
        URI inputUri = URI.create("jar:file:$input")
        URI outputUri = URI.create("jar:file:$output")

        if (!output.toFile().exists()) {
            com.google.common.io.Files.createParentDirs(output.toFile())
        }
        FileSystems.newFileSystem(inputUri, env).withCloseable { inputFileSystem ->
            FileSystems.newFileSystem(outputUri, env).withCloseable { outputFileSystem ->
                Path inputRoot = Iterables.getOnlyElement(inputFileSystem.rootDirectories)
                Path outputRoot = Iterables.getOnlyElement(outputFileSystem.rootDirectories)

                processFile(project, inputRoot, outputRoot, weavedClasses)
            }
        }
    }

    private static void processFile(Project project, Path input, Path output, List<WeavedClass> weavedClasses) {

        Files.walkFileTree(input, new SimpleFileVisitor<Path>() {
            @Override
            FileVisitResult visitFile(Path inputPath, BasicFileAttributes attrs) throws IOException {
                Path outputPath = Utils.toOutputPath(output, input, inputPath)
                directRun(project, inputPath, outputPath, weavedClasses)
                return FileVisitResult.CONTINUE
            }

            @Override
            FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                Path outputPath = Utils.toOutputPath(output, input, dir)
                Files.createDirectories(outputPath)
                return FileVisitResult.CONTINUE
            }
        })
    }

    static void directRun(Project project, Path input, Path output,
                          List<WeavedClass> weavedClasses) {
        if (Utils.isMatchCondition(project, input.toString())) {
            project.logger.error("directRun-INPUT: ${input.toString()} ")
            byte[] inputBytes = Files.readAllBytes(input)
            byte[] outputBytes = visitAndReturnBytecode(project, inputBytes, weavedClasses)
            Files.write(output, outputBytes)
        } else {
            Files.copy(input, output)
        }
    }

    private static byte[] visitAndReturnBytecode(Project project, byte[] originBytes,
                                                 List<WeavedClass> weavedClasses) {
        ClassReader classReader = new ClassReader(originBytes)
        ClassWriter classWriter =
                new CompactClassWriter(classReader,
                        ClassWriter.COMPUTE_FRAMES | ClassWriter.COMPUTE_MAXS)

        Map<String, List<MethodDelegate>> map = preCheckAndRetrieve(originBytes)
        DebounceModifyClassAdapter classAdapter = new DebounceModifyClassAdapter(project, classWriter, map)

        try {
            classReader.accept(classAdapter, ClassReader.EXPAND_FRAMES)
            //move to visit end?
            weavedClasses.add(classAdapter.getWovenClass())
            return classWriter.toByteArray()
        } catch (Exception e) {
            new GradleException("Exception occurred when visit code \n " + e.printStackTrace())
        }
        return originBytes
    }

    private static Map<String, List<MethodDelegate>> preCheckAndRetrieve(byte[] bytes) {

        ClassReader classReader = new ClassReader(bytes)
        PreCheckVisitorAdapter preCheckVisitorAdapter = new PreCheckVisitorAdapter()
        try {
            classReader.accept(preCheckVisitorAdapter, ClassReader.SKIP_FRAMES)
        } catch (Exception e) {
            println "Exception occurred when visit code \n " + e.printStackTrace()
        }

        return preCheckVisitorAdapter.getUnWeavedClassMap()
    }
}
