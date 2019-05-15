package com.cm.android.doubleclick.plugin

import com.google.common.collect.ImmutableMap
import com.google.common.collect.Iterables
import groovy.transform.PackageScope

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

    @PackageScope
    static void run(Path input, Path output, List<WeavedClass> weavedClasses,
                    FileType fileType) throws IOException {

        switch (fileType) {

            case FileType.JAR:
                processJar(input, output, weavedClasses)
                break

            case FileType.FILE:
                processFile(input, output, weavedClasses)
                break
        }
    }

    private static void processJar(Path input, Path output, List<WeavedClass> weavedClasses) {

        Map<String, String> env = ImmutableMap.of('create', 'true')
        URI inputUri = URI.create("jar:file:$input")
        URI outputUri = URI.create("jar:file:$output")

        FileSystems.newFileSystem(inputUri, env).withCloseable { inputFileSystem ->
            FileSystems.newFileSystem(outputUri, env).withCloseable { outputFileSystem ->
                Path inputRoot = Iterables.getOnlyElement(inputFileSystem.rootDirectories)
                Path outputRoot = Iterables.getOnlyElement(outputFileSystem.rootDirectories)
                processFile(inputRoot, outputRoot, weavedClasses)
            }
        }
    }

    private static void processFile(Path input, Path output, List<WeavedClass> weavedClasses) {

        Files.walkFileTree(input, new SimpleFileVisitor<Path>() {
            @Override
            FileVisitResult visitFile(Path inputPath, BasicFileAttributes attrs) throws IOException {
                Path outputPath = Utils.toOutputPath(output, input, inputPath)
                directRun(inputPath, outputPath, weavedClasses)
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

    @PackageScope
    static void directRun(Path input, Path output,
                          List<WeavedClass> weavedClasses) {
//        if (Utils.isMatchCondition(input.toString())) {
//            byte[] inputBytes = Files.readAllBytes(input)
//            byte[] outputBytes = visitAndReturnBytecode(inputBytes, weavedClasses)
//            Files.write(output, outputBytes)
//        } else {
        Files.copy(input, output)
//        }
    }
}
