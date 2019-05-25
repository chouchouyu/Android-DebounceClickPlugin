package com.github.susan.clickdebounce.plugin.asm

import com.github.susan.clickdebounce.plugin.utils.Logger
import com.github.susan.clickdebounce.plugin.utils.MethodHookMap
import org.objectweb.asm.AnnotationVisitor
import org.objectweb.asm.Label
import org.objectweb.asm.MethodVisitor
import org.objectweb.asm.Opcodes

import static com.github.susan.clickdebounce.plugin.utils.MethodHookMap.addAnno

class ExtraOnClick$MethodAdapter extends MethodVisitor implements Opcodes {

    private boolean traced
    private MethodVisitor methodVisitor
    private boolean needTrace

    ExtraOnClick$MethodAdapter(MethodVisitor methodVisitor) {
        super(Opcodes.ASM5, methodVisitor);
        this.methodVisitor = methodVisitor
    }

    @Override
    void visitCode() {
        super.visitCode()
        if (traced) return

        if (needTrace) {
            addAnno(mv);
            methodVisitor.visitVarInsn(ALOAD, 1)
            methodVisitor.visitMethodInsn(Opcodes.INVOKESTATIC, MethodHookMap.agentClassName, "shouldDoClick", "(Landroid/view/View;)Z", false)

            Label label = new Label();
            methodVisitor.visitJumpInsn(IFNE, label);
            methodVisitor.visitInsn(RETURN);
            methodVisitor.visitLabel(label);
        }

    }

    @Override
    AnnotationVisitor visitAnnotation(String desc, boolean visible) {
        traced = desc.equals(MethodHookMap.trackAnnoClassName)
        needTrace = desc.equals(MethodHookMap.extraAnnoClassName)
        Logger.info('desc:nnnnnnnnnnnnnnnnn' + desc)
        return super.visitAnnotation(desc, visible);
    }
}