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

//        if ((!MethodHookMap.isPublic(access)) && name.equals("showMessage") && desc.equals("(Ljava/lang/String;)V")) {
//            println "wwwwwwww" + name + desc
//            methodVisitor = new View$OnClickListenerMethodAdapter(methodVisitor);
//            tracedClass.addTracedMethod(MethodHookMap.convertSignature(name, desc));
//        }


        /**
         * Method 描述信息
         */
        String methodNameDesc = name + desc

//        if (interfaces != null && interfaces.length > 0) {
//            AnalyticsMethodCell cell = MethodHookMap.sInterfaceMethods.get(methodNameDesc)
//            if (cell != null && interfaces.contains(cell.parent)) {
//                methodVisitor = new Common$MethodAdapter(cell, methodVisitor);
//                tracedClass.addTracedMethod(MethodHookMap.convertSignature(name, desc));
//            }
//        }
//
//
        if (Utils.isViewOnclickMethod(access,name,desc)) {
            methodVisitor = new OnClick$MethodAdapter(methodVisitor);
            tracedClass.addTracedMethod(MethodHookMap.convertSignature(name, desc));
        }
//
//
//        if (mClassName == 'android/databinding/generated/callback/OnClickListener') {
//            if (desc == 'onClick(Landroid/view/View;)V') {
//                methodVisitor = new OnClick$MethodAdapter(methodVisitor);
//                tracedClass.addTracedMethod(MethodHookMap.convertSignature(name, desc));
//            }
//        }
//
//
//        if (desc == 'onDrawerOpened(Landroid/view/View;)V') {
//            methodVisitor = new Drawer$MethodAdapter(name, methodVisitor);
//            tracedClass.addTracedMethod(MethodHookMap.convertSignature(name, desc));
//        } else if (desc == 'onDrawerClosed(Landroid/view/View;)V') {
//            methodVisitor = new Drawer$MethodAdapter(name, methodVisitor);
//            tracedClass.addTracedMethod(MethodHookMap.convertSignature(name, desc));
//        }
//
//        /**
//         * Menu
//         * 目前支持 onContextItemSelected(MenuItem item)、onOptionsItemSelected(MenuItem item)
//         */
//        if (MethodHookMap.isTargetMenuMethodDesc(desc)) {
//            if (MethodHookMap.isStatic(access)) {
//                methodVisitor = new TrackMenuItem$MethodAdapter(true, methodVisitor);
//            } else {
//                methodVisitor = new TrackMenuItem$MethodAdapter(false, methodVisitor);
//            }
//            tracedClass.addTracedMethod(MethodHookMap.convertSignature(name, desc));
//        }
//
//        /**
//         * Fragment
//         * 目前支持 android/support/v4/app/ListFragment 和 android/support/v4/app/Fragment
//         */
//        if (MethodHookMap.isInstanceOfFragment(superName)) {
//            AnalyticsMethodCell cell = MethodHookMap.sFragmentMethods.get(methodNameDesc)
//            if (cell != null) {
//                methodVisitor = new Common$MethodAdapter(cell, methodVisitor);
//                tracedClass.addTracedMethod(MethodHookMap.convertSignature(name, desc));
//            }
//        }
//
//
//        /**
//         * 处理 ViewPager
//         */
//        if (mClassName == 'android/support/v4/view/ViewPager' || mClassName == 'androidx/viewpager/widget/ViewPager') {
//            if (desc == 'dispatchOnPageSelected(I)V') {
//                methodVisitor = new ViewPager$MethodAdapter(name, methodVisitor);
//                tracedClass.addTracedMethod(MethodHookMap.convertSignature(name, desc));
//            }
//        }
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
