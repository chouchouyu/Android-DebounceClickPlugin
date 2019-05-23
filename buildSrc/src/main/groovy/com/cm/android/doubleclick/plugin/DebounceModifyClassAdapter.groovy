package com.cm.android.doubleclick.plugin;

import com.cm.android.doubleclick.plugin.temp.MethodDelegate;
import com.cm.android.doubleclick.plugin.temp.Utils
import com.cm.android.doubleclick.plugin.temp.View$OnClickListenerMethodAdapter;
import com.cm.android.doubleclick.plugin.temp.WeavedClass;
import org.gradle.api.Project;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;


import static com.cm.android.doubleclick.plugin.temp.Utils.convertSignature;


/**
 * 创建时间: 2018/03/21 23:00 <br>
 * 作者: dengwei <br>
 * 描述:
 */
public class DebounceModifyClassAdapter extends ClassVisitor implements Opcodes {

    private String className;
    private WeavedClass weavedClass;
    private Map<String, List<MethodDelegate>> unWovenClassMap;
    private Project project;

    DebounceModifyClassAdapter(Project project, ClassVisitor classVisitor,
                               Map<String, List<MethodDelegate>> unWovenClassMap) {
        super(Opcodes.ASM6, classVisitor);
        this.unWovenClassMap = unWovenClassMap;
        this.project = project;
    }

    @Override
    public void visit(int version, int access, String name, String signature, String superName,
                      String[] interfaces) {
        super.visit(version, access, name, signature, superName, interfaces);
        this.weavedClass = new WeavedClass(className = name);
    }

    @Override
    public MethodVisitor visitMethod(int access, final String name, String desc, String signature,
                                     String[] exceptions) {

        MethodVisitor methodVisitor = cv.visitMethod(access, name, desc, signature, exceptions);


        // android.view.View.OnClickListener.onClick(android.view.View)
//        if (Utils.isViewOnclickMethod(access, name, desc) && isHit(access, name, desc)) {
//            project.logger.error(access+"-----------"+name+"---"+desc)
//            methodVisitor = new View$OnClickListenerMethodAdapter(methodVisitor);
//            weavedClass.addDebouncedMethod(convertSignature(name, desc));
//        }


        return methodVisitor;
    }

    private boolean isHit(int access, String name, String desc) {
        if (unWovenClassMap == null || unWovenClassMap.size() == 0) return false;
        boolean hitClass = unWovenClassMap.containsKey(className);
        if (hitClass) {
            List<MethodDelegate> methodDelegates = unWovenClassMap.get(weavedClass.className);
            for (int i = 0; i < methodDelegates.size(); i++) {
                boolean hitMethod = methodDelegates.get(i).match(access, name, desc);
                if (hitMethod) {
                    unWovenClassMap.remove(className);
                    return true;
                }
            }
        }
        return false;
    }


    WeavedClass getWovenClass() {
        return weavedClass;
    }
}
