package com.cm.android.doubleclick.plugin.temp

import org.gradle.api.Project;
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

    private boolean weaved
    private MethodVisitor methodVisitor
    private Project project

    View$OnClickListenerMethodAdapter(Project project, MethodVisitor methodVisitor) {
        super(Opcodes.ASM6, methodVisitor)
        this.methodVisitor = methodVisitor
        this.project = project;
    }


    @Override
    void visitCode() {
        super.visitCode();
        project.logger.error('1---------------Anno')
        if (weaved) return;

        addDebouncedAnno(mv);
        project.logger.error('2---------------Anno')
//        methodVisitor.visitVarInsn(ALOAD, 1);
//        methodVisitor.visitMethodInsn(INVOKESTATIC, Constant.agentClassName,
//                "maybe", "(Landroid/view/View;)Z", false);
        project.logger.error('3---------------Anno')
//        Label label = new Label();
//        methodVisitor.visitJumpInsn(IFNE, label);
//        methodVisitor.visitInsn(RETURN);
//        methodVisitor.visitLabel(label);
//        methodVisitor.visitFrame(Opcodes.F_SAME, 0, null, 0, null);
    }

    @Override
    AnnotationVisitor visitAnnotation(String desc, boolean visible) {

        project.logger.error(visible+'4---------------Anno'+desc)
        /*Lcom/smartdengg/clickdebounce/Debounced;*/
//        weaved = desc.equals(Constant.trackAnnoClassName);
//        project.logger.error('5---------------Anno')
        return super.visitAnnotation(desc, visible);
    }
}
