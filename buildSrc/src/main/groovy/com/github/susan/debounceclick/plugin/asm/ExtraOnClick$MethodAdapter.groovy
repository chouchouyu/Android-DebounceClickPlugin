package com.github.susan.debounceclick.plugin.asm

import com.github.susan.debounceclick.plugin.utils.Constant
import com.github.susan.debounceclick.plugin.utils.Logger
import org.objectweb.asm.AnnotationVisitor
import org.objectweb.asm.Label
import org.objectweb.asm.MethodVisitor
import org.objectweb.asm.Opcodes

import static com.github.susan.debounceclick.plugin.utils.Utils.addAnno

class ExtraOnClick$MethodAdapter extends MethodVisitor implements Opcodes {

    private boolean traced
    private MethodVisitor methodVisitor
    private boolean needTrace

    ExtraOnClick$MethodAdapter(MethodVisitor methodVisitor) {
        super(Opcodes.ASM5, methodVisitor);
        this.methodVisitor = methodVisitor
    }

    boolean getNeedTrace() {
        return needTrace
    }

    @Override
    void visitCode() {
        super.visitCode()
        if (traced) return

        if (needTrace) {
            addAnno(mv)
            methodVisitor.visitVarInsn(ALOAD, 1)
            methodVisitor.visitMethodInsn(Opcodes.INVOKESTATIC, Constant.agentClassName, "shouldDoClick", "(Landroid/view/View;)Z", false)

            Label label = new Label()
            methodVisitor.visitJumpInsn(IFNE, label)
            methodVisitor.visitInsn(RETURN)
            methodVisitor.visitLabel(label)
        }

    }

    @Override
    AnnotationVisitor visitAnnotation(String desc, boolean visible) {
        Logger.error("ExtraOnClickMethodAdapte---------- - " + desc + " || " + desc.equals(Constant.extraAnnoClassName))
        traced = desc.equals(Constant.trackAnnoClassName)
        needTrace = desc.equals(Constant.extraAnnoClassName)
        return super.visitAnnotation(desc, visible)
    }
}