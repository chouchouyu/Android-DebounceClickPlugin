package com.cm.android.doubleclick.plugin

import jdk.internal.org.objectweb.asm.Opcodes
import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.MethodVisitor

class PreCheckVisitorAdapter extends ClassVisitor implements Opcodes {

    PreCheckVisitorAdapter() {
        super(org.objectweb.asm.Opcodes.ASM6);
    }

    private String className;

    @Override
    void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
        super.visit(version, access, name, signature, superName, interfaces)
        this.className = name;
    }

    @Override
    MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
        return super.visitMethod(access, name, desc, signature, exceptions)
    }
}
