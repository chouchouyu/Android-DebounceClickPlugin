package com.cm.android.doubleclick.plugin.asm

import com.cm.android.doubleclick.plugin.utils.MethodHookMap
import org.objectweb.asm.AnnotationVisitor
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes
import static com.cm.android.doubleclick.plugin.utils.MethodHookMap.addAnno;
import static com.cm.android.doubleclick.plugin.utils.MethodHookMap.trackAnnoClassName
import static org.objectweb.asm.Opcodes.ALOAD
import static org.objectweb.asm.Opcodes.INVOKESTATIC

/**
 * 创建时间:  2018/03/09 19:48 <br>
 * 作者:  SmartDengg <br>
 * 描述:
 */
class View$OnClickListenerMethodAdapter extends MethodVisitor {

    private boolean traced;

    View$OnClickListenerMethodAdapter(MethodVisitor methodVisitor) {
        super(Opcodes.ASM5, methodVisitor);
    }

    @Override
    void visitCode() {
        super.visitCode();

        if (traced) return;

        addAnno(mv);

        mv.visitVarInsn(ALOAD, 1)
        mv.visitMethodInsn(INVOKESTATIC, MethodHookMap.agentClassName, "trackViewOnClick", "(Landroid/view/View;)V", false)

//        mv.visitMethodInsn(INVOKESTATIC, MethodHookMap.agentClassName, "wsm", "()V", false);

//        mv.visitMethodInsn(INVOKESTATIC, MethodHookMap.agentClassName, "wsm", "()V", false);
//        Label label = new Label();
//        mv.visitJumpInsn(IFNE, label);
//        mv.visitInsn(RETURN);
//        mv.visitLabel(label);

//        {
//            av0 = mv.visitAnnotation("Lcom/cm/android/infors/autotrace/InforsAnnotation;", false);
//            av0.visitEnd();
//        }

//        mv.visitVarInsn(ALOAD, 1);
//        mv.visitMethodInsn(INVOKESTATIC, "com/cm/android/infors/autotrace/AutoTrackHelper", "trackViewOnClick", "(Landroid/view/View;)V", false);

//        mv.visitVarInsn(ALOAD, 1);
//        mv.visitMethodInsn(INVOKESTATIC, agentClassName,
//                "trackViewOnClick", "(Landroid/view/View;)V", false);

//        Label label = new Label();
//        mv.visitJumpInsn(IFNE, label);
//        mv.visitInsn(RETURN);
//        mv.visitLabel(label);
    }

    @Override
    public AnnotationVisitor visitAnnotation(String desc, boolean visible) {
        traced = desc.equals(trackAnnoClassName);
        return super.visitAnnotation(desc, visible);
    }
}
