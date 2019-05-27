package com.github.susan.debounceclick.plugin.asm

import com.github.susan.debounceclick.plugin.bean.TracedClass
import com.github.susan.debounceclick.plugin.utils.Logger
import org.objectweb.asm.AnnotationVisitor
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

    ModifyClassAdapter(String className, ClassVisitor classVisitor) {
        super(Opcodes.ASM5, classVisitor);
        this.mClassName = className
    }

    @Override
    void visit(int version, int access, String name, String signature, String superName,
               String[] interfaces) {
        super.visit(version, access, name, signature, superName, interfaces);
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
                desc.equals("(Landroid/view/View;)V")) {
            if (name.equals("onClick")) {
                // android.view.View.OnClickListener.onClick(android.view.View)
                methodVisitor = new OnClick$MethodAdapter(methodVisitor);
                tracedClass.addTracedMethod(Utils.convertSignature(name, desc))
            } else {
                // method form like XX(View view) with anno @ClickDebounceExtra
                ExtraOnClick$MethodAdapter extraOnClick$MethodAdapter = new ExtraOnClick$MethodAdapter(methodVisitor)
                if (extraOnClick$MethodAdapter.needTrace) {
                    Logger.info("ModifyClassAdapter " + extraOnClick$MethodAdapter.needTrace+"--------"+name+desc)
                    tracedClass.addTracedMethod(Utils.convertSignature(name, desc))
                } else {
                    Logger.info("ModifyClassAdapter " + extraOnClick$MethodAdapter.needTrace+"--------"+name+desc)
                }
                methodVisitor = extraOnClick$MethodAdapter
            }

        }

        if (name.trim().startsWith('lambda$') && Utils.isPrivate(access) && Utils.isSynthetic(access)) {
            if (desc == '(Landroid/view/View;)V') {
                methodVisitor = new OnClick$MethodAdapter(methodVisitor)
                tracedClass.addTracedMethod(Utils.convertSignature(name, desc))
            }
        }


        return methodVisitor
    }

    TracedClass getTracedClass() {
        return tracedClass;
    }

}
