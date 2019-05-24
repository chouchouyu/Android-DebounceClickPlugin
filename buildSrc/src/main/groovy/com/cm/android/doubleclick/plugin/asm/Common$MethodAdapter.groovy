package com.cm.android.doubleclick.plugin.asm;

import com.cm.android.doubleclick.plugin.bean.AnalyticsMethodCell
import com.cm.android.doubleclick.plugin.utils.MethodHookMap;
import org.objectweb.asm.AnnotationVisitor
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes

import static com.cm.android.doubleclick.plugin.utils.MethodHookMap.addAnno
import static com.cm.android.doubleclick.plugin.utils.MethodHookMap.trackAnnoClassName;

class Common$MethodAdapter extends MethodVisitor {

    private final AnalyticsMethodCell cell;
    private boolean traced;
    private MethodVisitor methodVisitor

    Common$MethodAdapter(AnalyticsMethodCell analyticsMethodCell, MethodVisitor methodVisitor) {
        super(Opcodes.ASM5, methodVisitor);
        this.methodVisitor = methodVisitor
        this.cell = analyticsMethodCell;
    }

    @Override
    public void visitCode() {
        super.visitCode();

        if (traced) return;

        addAnno(mv);

        MethodHookMap.visitMethodWithLoadedParams(methodVisitor, Opcodes.INVOKESTATIC, MethodHookMap.agentClassName
                , cell.agentName, cell.agentDesc, cell.paramsStart, cell.paramsCount, cell.opcodes)

    }

    @Override
    public AnnotationVisitor visitAnnotation(String desc, boolean visible) {
        traced = desc.equals(trackAnnoClassName);
        return super.visitAnnotation(desc, visible);
    }
}