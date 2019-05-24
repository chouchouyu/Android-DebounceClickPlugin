package com.cm.android.doubleclick.plugin.utils

import com.cm.android.doubleclick.plugin.asm.CompactClassWriter
import com.cm.android.doubleclick.plugin.asm.ModifyClassAdapter
import com.cm.android.doubleclick.plugin.bean.TracedClass
import com.google.common.io.Files
import org.apache.commons.io.IOUtils
import groovy.transform.PackageScope
import org.gradle.api.GradleException
import org.objectweb.asm.ClassReader
import org.objectweb.asm.ClassWriter
import org.gradle.api.Project
import java.util.zip.ZipEntry
import java.util.zip.ZipInputStream
import java.util.zip.ZipOutputStream
import com.cm.android.doubleclick.plugin.DoubleClickExtension

class Processor {

    @PackageScope
    static void transformJar(Project project, File inputJar, File outputJar,
                             List<TracedClass> tracedClass, DoubleClickExtension extension) throws IOException {
        Files.createParentDirs(outputJar)

        new ZipOutputStream(new FileOutputStream(outputJar)).withCloseable { outputStream ->

            new ZipInputStream(new FileInputStream(inputJar)).withCloseable { inputStream ->

                ZipEntry entry
                while ((entry = inputStream.nextEntry) != null) {
                    if (!entry.isDirectory()) {
                        byte[] newContent = modifyClass(project, entry.name, IOUtils.toByteArray(inputStream), tracedClass, extension)

                        outputStream.putNextEntry(new ZipEntry(entry.name))
                        outputStream.write(newContent)
                        outputStream.closeEntry()
                    }
                }
            }
        }
    }

    static transformFile(Project project, File inputFile, File outputFile, List<TracedClass> tracedClasses, DoubleClickExtension extension) {

        Files.createParentDirs(outputFile)

        byte[] newContent = modifyClass(project, inputFile.path, inputFile.bytes, tracedClasses, extension)

        outputFile.withOutputStream {
            it.write(newContent)
        }
    }


    /**
     * 真正修改类中方法字节码
     */
    @PackageScope
    static byte[] modifyClass(Project project, String name, byte[] bytes, List<TracedClass> tracedClasses, DoubleClickExtension extension) {
        def weavedBytes = bytes

        if (Utils.isMatchCondition(project, extension, name)) {
            ClassReader classReader = new ClassReader(bytes)
            ClassWriter classWriter =
                    new CompactClassWriter(classReader,
                            ClassWriter.COMPUTE_FRAMES | ClassWriter.COMPUTE_MAXS)

            ModifyClassAdapter classAdapter = new ModifyClassAdapter(project,name,classWriter)
            try {
                classReader.accept(classAdapter, ClassReader.EXPAND_FRAMES)
                weavedBytes = classWriter.toByteArray()
                tracedClasses.add(classAdapter.getTracedClass())
            } catch (Exception e) {
                new GradleException("Exception occurred when modifyClass ${name} \n " + e.printStackTrace())
            }
        }

       return weavedBytes

    }


}
