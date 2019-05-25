package com.github.susan.clickdebounce.plugin.asm

import com.github.susan.clickdebounce.plugin.utils.Utils
import org.objectweb.asm.AnnotationVisitor
import org.objectweb.asm.Label
import org.objectweb.asm.MethodVisitor
import org.objectweb.asm.Opcodes

import static com.github.susan.clickdebounce.plugin.utils.Utils.addAnno
import static com.github.susan.clickdebounce.plugin.utils.Utils.trackAnnoClassName

class OnClick$MethodAdapter extends MethodVisitor implements Opcodes {

    private boolean traced
    private MethodVisitor methodVisitor

    OnClick$MethodAdapter(MethodVisitor methodVisitor) {
        super(Opcodes.ASM5, methodVisitor);
        this.methodVisitor = methodVisitor
    }

    @Override
    void visitCode() {
        super.visitCode();

        if (traced) return;

        addAnno(mv);

        methodVisitor.visitVarInsn(ALOAD, 1)
        methodVisitor.visitMethodInsn(Opcodes.INVOKESTATIC, Utils.agentClassName, "shouldDoClick", "(Landroid/view/View;)Z", false)

        Label label = new Label();
        methodVisitor.visitJumpInsn(IFNE, label);
        methodVisitor.visitInsn(RETURN);
        methodVisitor.visitLabel(label);
//        methodVisitor.visitFrame(Opcodes.F_SAME, 0, null, 0, null);
    }

    @Override
    public AnnotationVisitor visitAnnotation(String desc, boolean visible) {
        traced = desc.equals(trackAnnoClassName);
        return super.visitAnnotation(desc, visible);
    }
}