package com.cm.android.doubleclick.plugin.asm

import com.cm.android.doubleclick.plugin.utils.MethodHookMap
import org.objectweb.asm.AnnotationVisitor
import org.objectweb.asm.Label
import org.objectweb.asm.MethodVisitor
import org.objectweb.asm.Opcodes

import static com.cm.android.doubleclick.plugin.utils.MethodHookMap.addInforsAnno
import static com.cm.android.doubleclick.plugin.utils.MethodHookMap.trackAnnoClassName

class ListView$OnItemClickListenerMethodAdapter extends MethodVisitor implements Opcodes {
    private boolean traced
    private MethodVisitor methodVisitor

    ListView$OnItemClickListenerMethodAdapter(MethodVisitor methodVisitor) {
        super(Opcodes.ASM5, methodVisitor);
        this.methodVisitor = methodVisitor
    }

    @Override
      void visitCode() {
        super.visitCode();

        if (traced) return;

        addInforsAnno(mv);

        methodVisitor.visitVarInsn(ALOAD, 2)
        methodVisitor.visitMethodInsn(Opcodes.INVOKESTATIC, MethodHookMap.agentClassName, "shouldDoClick", "(Landroid/view/View;)Z", false)

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