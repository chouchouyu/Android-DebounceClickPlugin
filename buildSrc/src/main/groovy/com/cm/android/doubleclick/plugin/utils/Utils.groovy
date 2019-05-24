package com.cm.android.doubleclick.plugin.utils

import com.android.SdkConstants
import com.android.build.gradle.AppExtension
import com.android.build.gradle.BaseExtension
import com.android.build.gradle.LibraryExtension
import com.android.utils.FileUtils
import com.cm.android.doubleclick.plugin.DoubleClickExtension
import org.gradle.api.Project
import org.objectweb.asm.Opcodes

class Utils implements Opcodes {

    static def shouldExcludeFile(def filePath) {
        if (filePath.endsWith("BuildConfig.class")
                || filePath.endsWith("BuildConfig\$*.class")
                || filePath.endsWith("R.class")
                || isRIgnoreFile(filePath)
                || isBuildConfigIgnoreFile(filePath)) {
            return true
        } else {
            return false

        }
    }

    static def isRIgnoreFile(def fileName) {
        //println  ('R$mipmap.class'==~'R\\$mipmap\\.class' )
        def regex = ~'R\\$.*\\.class'
        def match = fileName =~ regex
        match.find()
    }

    static def isBuildConfigIgnoreFile(def fileName) {
        //("BuildConfig\$*.class")
        def regex = ~'BuildConfig\\$.*\\.class'
        def match = fileName =~ regex
        match.find()
    }


    static boolean isMatchCondition(Project project, DoubleClickExtension extension, String name) {
        name.endsWith(SdkConstants.DOT_CLASS) &&
                shouldModifyClass(project, extension, name) &&
                !shouldExcludeFile(name)
    }

    /**
     * 只扫描特定包下的类
     * @param className 形如 android.app.Fragment 的类名
     * @return
     */
    static def shouldModifyClass(Project project, DoubleClickExtension extension, String className) {
        def targetPackages = setIncludePackages(extension.includePackages, project)
        def pathName = path2Classname(className);
        for (i in targetPackages) {
            if (pathName.contains(i)) {
                return true
            }
            if (pathName.contains('butterknife.internal.DebouncingOnClickListener')) {
                return false
            }
        }
        return false
    }

    static def path2Classname(String entryName) {
        entryName.replace(File.separator, ".").replace(".class", "")
    }


    static def setIncludePackages(def extentPackage, Project project) {
        def includePackage = []
//        def includePackage = ['com.jakewharton.rxbinding.view.ViewClickOnSubscribe']
//                                  ,'com.facebook.react.uimanager.NativeViewHierarchyManager']
        AppExtension android = project.extensions.getByType(AppExtension)
        def appPackageName = getAppPackageName(android)
        includePackage.add(appPackageName)
        if (extentPackage) {
            includePackage.addAll(extentPackage)
        }
        includePackage
    }

    /**
     * 获取应用程序包名
     * @return
     */
    static def getAppPackageName(AppExtension android) {
        def manifestFile = android.sourceSets.main.manifest.srcFile
        def xml = new XmlSlurper().parse(manifestFile)
        def pageName = xml.@package
        return "$pageName"
    }


    static void forExtension(BaseExtension extension, Closure closure) {

        def findExtensionType
        if (closure.maximumNumberOfParameters == 3) findExtensionType = true

        if (extension instanceof AppExtension) {
            if (findExtensionType) {
                closure.call(true, false, false)
            } else {
                extension.applicationVariants.all(closure)
            }
        }
        if (extension instanceof LibraryExtension) {
            if (findExtensionType) {
                closure.call(false, true, false)
            } else {
                extension.libraryVariants.all(closure)
            }
        }
//        if (extension instanceof FeatureExtension) {
//            if (findExtensionType) {
//                closure.call(false, false, true)
//            } else {
//                extension.featureVariants.all(closure)
//            }
//        }
    }

    static File toOutputFile(File outputDir, File inputDir, File inputFile) {
        return new File(outputDir, FileUtils.relativePossiblyNonExistingPath(inputFile, inputDir))
    }

    static boolean isViewOnclickMethod(int access, String name, String desc) {
        return (Utils.isPublic(access) && !Utils.isStatic(access)) && //
                name.equals("onClick") && //
                desc.equals("(Landroid/view/View;)V");
    }

    static boolean isPrivate(int access) {
        return (access & ACC_PRIVATE) != 0;
    }

    static boolean isPublic(int access) {
        return (access & ACC_PUBLIC) != 0;
    }

    static boolean isStatic(int access) {
        return (access & ACC_STATIC) != 0;
    }
}
