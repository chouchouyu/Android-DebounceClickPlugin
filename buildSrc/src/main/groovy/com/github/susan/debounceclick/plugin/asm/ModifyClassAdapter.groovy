package com.github.susan.debounceclick.plugin.asm

import com.github.susan.debounceclick.plugin.bean.MethodDelegate
import com.github.susan.debounceclick.plugin.bean.TracedClass
import com.github.susan.debounceclick.plugin.utils.Logger
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes
import com.github.susan.debounceclick.plugin.utils.Utils


class ModifyClassAdapter extends ClassVisitor implements Opcodes {

    private TracedClass tracedClass
    private String superName
    int access
    String[] interfaces
    private String mClassName
    Map<String, List<MethodDelegate>> extraMethodMap

    ModifyClassAdapter(ClassVisitor classVisitor, Map<String, List<MethodDelegate>> extraMethodMap) {
        super(Opcodes.ASM5, classVisitor);
        this.extraMethodMap = extraMethodMap
    }

    @Override
    void visit(int version, int access, String name, String signature, String superName,
               String[] interfaces) {
        super.visit(version, access, name, signature, superName, interfaces);
        this.mClassName = name
        tracedClass = new TracedClass(name)
        this.superName = superName
        this.access = access
        this.interfaces = interfaces
    }


    @Override
    MethodVisitor visitMethod(int access, final String name, String desc, String signature,
                              String[] exceptions) {
        MethodVisitor methodVisitor = cv.visitMethod(access, name, desc, signature, exceptions);

        if ((Utils.isPublic(access) && !Utils.isStatic(access)) &&
                desc.equals("(Landroid/view/View;)V") && name.equals("onClick")) {
            // android.view.View.OnClickListener.onClick(android.view.View)
            methodVisitor = new OnClick$MethodAdapter(methodVisitor);
            tracedClass.addTracedMethod(Utils.convertSignature(name, desc))
        }

        if (name.trim().startsWith('lambda$') && Utils.isPrivate(access) && Utils.isSynthetic(access)) {
            if (desc == '(Landroid/view/View;)V') {
                methodVisitor = new OnClick$MethodAdapter(methodVisitor)
                tracedClass.addTracedMethod(Utils.convertSignature(name, desc))
            }
        }


        for (String clazzName : extraMethodMap.keySet()) {

            if (mClassName.equals(clazzName)) {
                List<MethodDelegate> delegateList = extraMethodMap.get(clazzName)
                for (MethodDelegate item : delegateList) {
                    if (item.match(access, name, desc)) {
                        // method form like XX(View view) with anno @ClickDebounceExtra
                        methodVisitor = new ExtraOnClick$MethodAdapter(methodVisitor)
                        tracedClass.addTracedMethod(Utils.convertSignature(name, desc))
                    }
                }
            }
        }


        return methodVisitor
    }

    TracedClass getTracedClass() {
        return tracedClass;
    }

}
