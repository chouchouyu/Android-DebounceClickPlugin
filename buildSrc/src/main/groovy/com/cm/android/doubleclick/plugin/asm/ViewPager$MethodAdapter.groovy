package com.cm.android.doubleclick.plugin.asm

import com.cm.android.doubleclick.plugin.utils.MethodHookMap
import com.sun.org.apache.bcel.internal.generic.ALOAD
import org.objectweb.asm.AnnotationVisitor
import org.objectweb.asm.MethodVisitor
import org.objectweb.asm.Opcodes

import static com.cm.android.doubleclick.plugin.utils.MethodHookMap.addInforsAnno
import static com.cm.android.doubleclick.plugin.utils.MethodHookMap.trackAnnoClassName

class ViewPager$MethodAdapter extends MethodVisitor {

    private boolean traced
    private MethodVisitor methodVisitor

    ViewPager$MethodAdapter(MethodVisitor methodVisitor) {
        super(Opcodes.ASM5, methodVisitor);
        this.methodVisitor = methodVisitor
    }

    @Override
    public void visitCode() {
        super.visitCode();

        if (traced) return;

        addInforsAnno(mv);

        methodVisitor.visitVarInsn(ALOAD, 0)
        methodVisitor.visitMethodInsn(Opcodes.INVOKESTATIC, MethodHookMap.agentClassName, "trackViewOnClick", "(Landroid/view/View;)V", false)

    }

    @Override
    public AnnotationVisitor visitAnnotation(String desc, boolean visible) {
        traced = desc.equals(trackAnnoClassName);
        return super.visitAnnotation(desc, visible);
    }
}