package com.cm.android.doubleclick.plugin.asm

import com.cm.android.doubleclick.plugin.utils.MethodHookMap
import com.sun.org.apache.bcel.internal.generic.ALOAD
import org.objectweb.asm.AnnotationVisitor
import org.objectweb.asm.MethodVisitor
import org.objectweb.asm.Opcodes

import static com.cm.android.doubleclick.plugin.utils.MethodHookMap.addInforsAnno
import static com.cm.android.doubleclick.plugin.utils.MethodHookMap.trackAnnoClassName

class TrackMenuItem$MethodAdapter extends MethodVisitor {

    private boolean traced
    private MethodVisitor methodVisitor
    private boolean isStatic

    TrackMenuItem$MethodAdapter(boolean isStatic, MethodVisitor methodVisitor) {
        super(Opcodes.ASM5, methodVisitor);
        this.methodVisitor = methodVisitor
        this.isStatic = isStatic
    }

    @Override
    public void visitCode() {
        super.visitCode();

        if (traced) return;

        addInforsAnno(mv);


        if (isStatic) {
            methodVisitor.visitVarInsn(ALOAD, 1)
            methodVisitor.visitMethodInsn(Opcodes.INVOKESTATIC, MethodHookMap.agentClassName, "trackMenuItem", "(Landroid/view/MenuItem;)V", false)
        } else {
            methodVisitor.visitVarInsn(ALOAD, 0)
            methodVisitor.visitVarInsn(ALOAD, 1)
            methodVisitor.visitMethodInsn(Opcodes.INVOKESTATIC, MethodHookMap.agentClassName, "trackMenuItem", "(Ljava/lang/Object;Landroid/view/MenuItem;)V", false)
        }
    }

    @Override
    public AnnotationVisitor visitAnnotation(String desc, boolean visible) {
        traced = desc.equals(trackAnnoClassName);
        return super.visitAnnotation(desc, visible);
    }
}