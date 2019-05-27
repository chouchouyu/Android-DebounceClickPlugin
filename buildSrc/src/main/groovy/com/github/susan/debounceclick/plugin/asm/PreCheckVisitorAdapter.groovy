package com.github.susan.debounceclick.plugin.asm;

import com.github.susan.debounceclick.plugin.bean.MethodDelegate;
import com.github.susan.debounceclick.plugin.utils.Constant
import com.github.susan.debounceclick.plugin.utils.Logger;
import com.github.susan.debounceclick.plugin.utils.Utils;
import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.MethodNode;


class PreCheckVisitorAdapter extends ClassVisitor implements Opcodes {

    private String className;
    private Map<String, List<MethodDelegate>> unWeavedClassMap = new HashMap<>();

    PreCheckVisitorAdapter() {
        super(Opcodes.ASM5)
    }

    @Override
    void visit(int version, int access, String name, String signature, String superName,
               String[] interfaces) {
        super.visit(version, access, name, signature, superName, interfaces)
        this.className = name
    }

    @Override
    MethodVisitor visitMethod(int access, String name, String desc, String signature,
                              String[] exceptions) {


        if ((Utils.isPublic(access) && !Utils.isStatic(access)) &&
                desc.equals("(Landroid/view/View;)V")) {
            return new MethodNodeAdapter(api, access, name, desc, signature, exceptions, className,
                    unWeavedClassMap)
        }

        return super.visitMethod(access, name, desc, signature, exceptions);
    }

    Map<String, List<MethodDelegate>> getUnWeavedClassMap() {
        return unWeavedClassMap;
    }

    static class MethodNodeAdapter extends MethodNode {

        private String className;
        private Map<String, List<MethodDelegate>> map;
        private boolean needTrace;

        MethodNodeAdapter(int api, int access, String name, String desc, String signature,
                          String[] exceptions, String className, Map<String, List<MethodDelegate>> map) {
            super(api, access, name, desc, signature, exceptions)
            this.className = className
            this.map = map
        }

        @Override
        void visitEnd() {
            if (needTrace) {
                List<MethodDelegate> methodDelegates = map.get(className)
                if (methodDelegates == null) methodDelegates = new ArrayList<>()
                methodDelegates.add(new MethodDelegate(access, name, desc))
                map.put(className, methodDelegates)
            }
        }


        @Override
        AnnotationVisitor visitAnnotation(String desc, boolean visible) {
            needTrace = desc.equals(Constant.extraAnnoClassName)
            return super.visitAnnotation(desc, visible);
        }
    }
}
