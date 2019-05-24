package com.github.susan.clickdebounce.plugin.utils


import org.objectweb.asm.AnnotationVisitor
import org.objectweb.asm.MethodVisitor
import org.objectweb.asm.Type

import static org.objectweb.asm.Opcodes.ACC_PRIVATE
import static org.objectweb.asm.Opcodes.ACC_PUBLIC
import static org.objectweb.asm.Opcodes.ACC_STATIC
import static org.objectweb.asm.Opcodes.ACC_SYNTHETIC

class MethodHookMap {


    public static
    final String agentClassName = "com/github/susan/clickdebounce/java/ClickDebounceHandler";
    public static
    final String trackAnnoClassName = "Lcom/github/susan/clickdebounce/java/ClickDebounceMark;"
    public static
    final String extraAnnoClassName = "Lcom/github/susan/clickdebounce/java/ClickDebounceExtra;"

    //    String name
//    String desc
//    String parent
//    String agentName
//    String agentDesc
//    String paramsStart
//    String paramsCount
//    List[] opcodes

    /*
    ('I',Opcodes.ILOAD);// I: int , retrieve integer from local variable
    ('Z',Opcodes.ILOAD);// Z: bool , retrieve integer from local variable
    ('J',Opcodes.LLOAD);// J: long , retrieve long from local variable
    ('F',Opcodes.FLOAD);// F: float , retrieve float from local variable
    ('D',Opcodes.DLOAD);// D: double , retrieve double from local variable
    */


    static void addAnno(MethodVisitor mv) {
        AnnotationVisitor annotationVisitor =
                mv.visitAnnotation(trackAnnoClassName, true);
        annotationVisitor.visitEnd();
    }


    static boolean isPrivate(int access) {
        return (access & ACC_PRIVATE) != 0;
    }

    static boolean isSynthetic(int access) {
        return (access & ACC_SYNTHETIC) != 0;
    }

    static boolean isPublic(int access) {
        return (access & ACC_PUBLIC) != 0;
    }

    static boolean isStatic(int access) {
        return (access & ACC_STATIC) != 0;
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
