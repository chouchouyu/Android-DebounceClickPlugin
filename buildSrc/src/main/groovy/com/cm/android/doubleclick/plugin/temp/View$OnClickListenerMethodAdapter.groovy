package com.cm.android.doubleclick.plugin.temp;

import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes

import static com.cm.android.doubleclick.plugin.temp.Utils.addDebouncedAnno;
import static org.objectweb.asm.Opcodes.*;

/**
 * 创建时间:  2018/03/09 19:48 <br>
 * 作者:  SmartDengg <br>
 * 描述:
 */
class View$OnClickListenerMethodAdapter extends MethodVisitor {

    private boolean weaved;
    private MethodVisitor methodVisitor
    View$OnClickListenerMethodAdapter(MethodVisitor methodVisitor) {
        super(Opcodes.ASM6, methodVisitor)
        this.methodVisitor = methodVisitor
    }

    @Override
    void visitCode() {
        super.visitCode();

        if (weaved) return;

        addDebouncedAnno(mv);

        methodVisitor.visitVarInsn(ALOAD, 1);
        methodVisitor.visitMethodInsn(INVOKESTATIC, Constant.agentClassName,
                "maybe", "(Landroid/view/View;)Z", false);
//        Label label = new Label();
//        methodVisitor.visitJumpInsn(IFNE, label);
//        methodVisitor.visitInsn(RETURN);
//        methodVisitor.visitLabel(label);
//        methodVisitor.visitFrame(Opcodes.F_SAME, 0, null, 0, null);
    }

    @Override
    public AnnotationVisitor visitAnnotation(String desc, boolean visible) {

        /*Lcom/smartdengg/clickdebounce/Debounced;*/
        weaved = desc.equals(Constant.trackAnnoClassName);

        return super.visitAnnotation(desc, visible);
    }
}
