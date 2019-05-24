package com.cm.android.doubleclick.plugin.asm


import com.cm.android.doubleclick.plugin.utils.MethodHookMap
import org.objectweb.asm.AnnotationVisitor
import org.objectweb.asm.MethodVisitor
import org.objectweb.asm.Opcodes

import static com.cm.android.doubleclick.plugin.utils.MethodHookMap.addAnno
import static com.cm.android.doubleclick.plugin.utils.MethodHookMap.trackAnnoClassName

class Drawer$MethodAdapter extends MethodVisitor {

    private boolean traced
    private MethodVisitor methodVisitor
    private String methodName

    Drawer$MethodAdapter(String methodName, MethodVisitor methodVisitor) {
        super(Opcodes.ASM5, methodVisitor);
        this.methodVisitor = methodVisitor
        this.methodName = methodName
    }

    @Override
    public void visitCode() {
        super.visitCode();

        if (traced) return;

        addAnno(mv);



        methodVisitor.visitVarInsn(ALOAD, 1)
        if (methodName.contains("onDrawerOpened")) {
            methodVisitor.visitMethodInsn(Opcodes.INVOKESTATIC, MethodHookMap.agentClassName, "trackDrawerOpened", "(Landroid/view/View;)V", false)
        } else if (methodName.contains("onDrawerClosed")) {
            methodVisitor.visitMethodInsn(Opcodes.INVOKESTATIC, MethodHookMap.agentClassName, "trackDrawerClosed", "(Landroid/view/View;)V", false)
        }

    }

    @Override
    public AnnotationVisitor visitAnnotation(String desc, boolean visible) {
        traced = desc.equals(trackAnnoClassName);
        return super.visitAnnotation(desc, visible);
    }
}