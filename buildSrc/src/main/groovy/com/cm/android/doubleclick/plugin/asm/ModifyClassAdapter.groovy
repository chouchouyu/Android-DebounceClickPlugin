package com.cm.android.doubleclick.plugin.asm

import com.cm.android.doubleclick.plugin.bean.AnalyticsMethodCell
import com.cm.android.doubleclick.plugin.bean.TracedClass
import com.cm.android.doubleclick.plugin.utils.Utils
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes
import com.cm.android.doubleclick.plugin.utils.MethodHookMap

/**
 * 创建时间: 2018/03/21 23:00 <br>
 * 作者: dengwei <br>
 * 描述:
 */
public class ModifyClassAdapter extends ClassVisitor implements Opcodes {

    private TracedClass tracedClass
    private String superName
    int access
    String[] interfaces
    private String mClassName

    ModifyClassAdapter(String className, ClassVisitor classVisitor) {
        super(Opcodes.ASM5, classVisitor);
        this.mClassName = className
    }

    @Override
    void visit(int version, int access, String name, String signature, String superName,
               String[] interfaces) {
        super.visit(version, access, name, signature, superName, interfaces);
        tracedClass = new TracedClass(name);
        this.superName = superName
        this.access = access
        this.interfaces = interfaces
    }


    @Override
    MethodVisitor visitMethod(int access, final String name, String desc, String signature,
                              String[] exceptions) {

        MethodVisitor methodVisitor = cv.visitMethod(access, name, desc, signature, exceptions);

        // android.view.View.OnClickListener.onClick(android.view.View)
        if ((Utils.isPublic(access) && !Utils.isStatic(access)) && //
                name.equals("onClick") && //
                desc.equals("(Landroid/view/View;)V")) {
            methodVisitor = new OnClick$MethodAdapter(methodVisitor);
            tracedClass.addTracedMethod(MethodHookMap.convertSignature(name, desc));
        }


        // android.widget.AdapterView.OnItemClickListener.onItemClick(android.widget.AdapterView,android.view.View,int,long)
        if ((Utils.isPublic(access) && !Utils.isStatic(access)) && //
                name.equals("onItemClick") && //
                desc.equals("(Landroid/widget/AdapterView;Landroid/view/View;IJ)V")) {
            methodVisitor = new ListView$OnItemClickListenerMethodAdapter(methodVisitor);
            tracedClass.addTracedMethod(MethodHookMap.convertSignature(name, desc));
        }

//
//        if (name.trim().startsWith('lambda$') && MethodHookMap.isPrivate(access) && MethodHookMap.isSynthetic(access)) {
//            if (desc == '(Landroid/view/MenuItem;)Z' && MethodHookMap.isStatic(access)) {
//                AnalyticsMethodCell cell = MethodHookMap.sLambdaMethods.get(desc + '2')
//                if (cell != null) {
//                    methodVisitor = new Common$MethodAdapter(cell, methodVisitor);
//                    tracedClass.addTracedMethod(MethodHookMap.convertSignature(name, desc));
//                }
//            } else {
//                AnalyticsMethodCell cell = MethodHookMap.sLambdaMethods.get(desc)
//                if (cell != null) {
//                    int paramStart = cell.paramsStart
//                    if (MethodHookMap.isStatic(access)) {
//                        paramStart = paramStart - 1
//                    }
//                    methodVisitor = new Common$MethodAdapter(cell, methodVisitor);
//                    tracedClass.addTracedMethod(MethodHookMap.convertSignature(name, desc));
//                }
//            }
//        }


        return methodVisitor;
    }

    TracedClass getTracedClass() {
        return tracedClass;
    }
}
