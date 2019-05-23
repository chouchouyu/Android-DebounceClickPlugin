package com.cm.android.doubleclick.plugin.temp

import com.android.SdkConstants
import com.android.build.gradle.AppExtension
import org.gradle.api.Project
import org.objectweb.asm.AnnotationVisitor
import org.objectweb.asm.MethodVisitor
import org.objectweb.asm.Opcodes
import org.objectweb.asm.Type

import java.nio.file.Path;

class Utils implements Opcodes {

    static DoubleClickExtension getExtension(Project project) {
        return project."${Constant.EXTENTION}";
    }

    static Path toOutputPath(Path outputRoot, Path inputRoot, Path inputPath) {
        return outputRoot.resolve(inputRoot.relativize(inputPath))
    }

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


    static boolean isMatchCondition(Project project, String name) {
        name.endsWith(SdkConstants.DOT_CLASS) &&
                shouldModifyClass(project, name) &&
                !shouldExcludeFile(name)
    }

    /**
     * 只扫描特定包下的类
     * @param className 形如 android.app.Fragment 的类名
     * @return
     */
    static def shouldModifyClass(Project project, String className) {
        def targetPackages = setIncludePackages(Utils.getExtension(project).includePackages, project)
        HashSet<String> exclude = ['com.qiyukf', 'com.sensorsdata.analytics.android.sdk', 'com.cm.android.doubleclick', 'android.support', 'androidx', ' com.cm.android.infors', 'android.arch']

        def pathName = path2Classname(className);

        for (i in exclude) {
            if (pathName.contains(i)) {
//                project.logger.error("FILE--exclude " + pathName)
                return false
            }
        }

        for (i in targetPackages) {
            if (pathName.contains(i)) {
//                project.logger.error("FILE--targetPackages " + pathName)
                return true
            }

        }

        return false
    }

    static def path2Classname(String entryName) {
        entryName.replace(File.separator, ".").replace(".class", "")
    }


    static def setIncludePackages(def includePackage, Project project) {
        HashSet<String> include = ['butterknife.internal.DebouncingOnClickListener',
                                   'com.jakewharton.rxbinding.view.ViewClickOnSubscribe',
                                   'com.facebook.react.uimanager.NativeViewHierarchyManager']
        AppExtension android = project.extensions.getByType(AppExtension)
        def appPackageName = getAppPackageName(android)
        includePackage.add(appPackageName)
        if (includePackage) {
            includePackage.addAll(include)
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


    static boolean isPrivate(int access) {
        return (access & ACC_PRIVATE) != 0;
    }

    static boolean isPublic(int access) {
        return (access & ACC_PUBLIC) != 0;
    }

    static boolean isStatic(int access) {
        return (access & ACC_STATIC) != 0;
    }

    static boolean isViewOnclickMethod(int access, String name, String desc) {
        return (Utils.isPublic(access) && !Utils.isStatic(access)) && //
                name.equals("onClick") && //
                desc.equals("(Landroid/view/View;)V");
    }

    static boolean isListViewOnItemOnclickMethod(int access, String name, String desc) {
        return (Utils.isPublic(access) && !Utils.isStatic(access)) && //
                name.equals("onItemClick") && //
                desc.equals("(Landroid/widget/AdapterView;Landroid/view/View;IJ)V");
    }

    static void addDebouncedAnno(MethodVisitor mv) {
        AnnotationVisitor annotationVisitor =
                mv.visitAnnotation(Constant.trackAnnoClassName, false);
        annotationVisitor.visitEnd();
    }

    static String convertSignature(String name, String desc) {
        Type method = Type.getType(desc);
        StringBuilder sb = new StringBuilder();
        sb.append(method.getReturnType().getClassName()).append(" ").append(name);
        sb.append("(");
        for (int i = 0; i < method.getArgumentTypes().length; i++) {
            sb.append(method.getArgumentTypes()[i].getClassName());
            if (i != method.getArgumentTypes().length - 1) {
                sb.append(",");
            }
        }
        sb.append(")");
        return sb.toString();
    }


}
