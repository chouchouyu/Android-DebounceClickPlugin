package com.cm.android.doubleclick.plugin.asm

import com.cm.android.doubleclick.plugin.utils.MethodHookMap
import com.sun.org.apache.bcel.internal.generic.ALOAD
import com.sun.org.apache.bcel.internal.generic.ILOAD
import org.objectweb.asm.AnnotationVisitor
import org.objectweb.asm.MethodVisitor
import org.objectweb.asm.Opcodes

import static com.cm.android.doubleclick.plugin.utils.MethodHookMap.addInforsAnno
import static com.cm.android.doubleclick.plugin.utils.MethodHookMap.trackAnnoClassName

class Item$MethodAdapter extends MethodVisitor {

    private boolean traced
    private MethodVisitor methodVisitor

    Item$MethodAdapter(MethodVisitor methodVisitor) {
        super(Opcodes.ASM5, methodVisitor);
        this.methodVisitor = methodVisitor
    }

    @Override
    public void visitCode() {
        super.visitCode();

        if (traced) return;

        addInforsAnno(mv);


        methodVisitor.visitVarInsn(ALOAD, 1)
        methodVisitor.visitVarInsn(ALOAD, 2)
        methodVisitor.visitVarInsn(ILOAD, 3)
        methodVisitor.visitMethodInsn(Opcodes.INVOKESTATIC, MethodHookMap.agentClassName, "trackListView", "(Landroid/widget/AdapterView;Landroid/view/View;I)V", false)


//        MethodHookMap.visitMethodWithLoadedParams(methodVisitor, Opcodes.INVOKESTATIC, MethodHookMap.agentClassName
//                , cell.agentName, cell.agentDesc, cell.paramsStart, cell.paramsCount, cell.opcodes)

    }

    @Override
    public AnnotationVisitor visitAnnotation(String desc, boolean visible) {
        traced = desc.equals(trackAnnoClassName);
        return super.visitAnnotation(desc, visible);
    }
}