package com.cm.android.doubleclick.plugin.utils


import com.cm.android.doubleclick.plugin.bean.AnalyticsMethodCell
import jdk.internal.org.objectweb.asm.Opcodes
import org.objectweb.asm.AnnotationVisitor
import org.objectweb.asm.MethodVisitor
import org.objectweb.asm.Type

import static org.objectweb.asm.Opcodes.ACC_PRIVATE
import static org.objectweb.asm.Opcodes.ACC_PUBLIC
import static org.objectweb.asm.Opcodes.ACC_STATIC
import static org.objectweb.asm.Opcodes.ACC_SYNTHETIC

class MethodHookMap {


    public static
    final String agentClassName = "com/github/susan/clickdebounce/java/ClickDebounceHandler";
    public static
    final String trackAnnoClassName = "Lcom/github/susan/clickdebounce/java/ClickDebounceMark;"
    public static
    final String extraAnnoClassName = "Lcom/github/susan/clickdebounce/java/ClickDebounceExtra;"

    //    String name
//    String desc
//    String parent
//    String agentName
//    String agentDesc
//    String paramsStart
//    String paramsCount
//    List[] opcodes

    /*
    ('I',Opcodes.ILOAD);// I: int , retrieve integer from local variable
    ('Z',Opcodes.ILOAD);// Z: bool , retrieve integer from local variable
    ('J',Opcodes.LLOAD);// J: long , retrieve long from local variable
    ('F',Opcodes.FLOAD);// F: float , retrieve float from local variable
    ('D',Opcodes.DLOAD);// D: double , retrieve double from local variable
    */

    /**
     * interface中的方法
     */
    public final static HashMap<String, AnalyticsMethodCell> sInterfaceMethods = new HashMap<>()
    static {
        sInterfaceMethods.put('onClick(Landroid/view/View;)V', new AnalyticsMethodCell(
                'onClick',
                '(Landroid/view/View;)V',
                'android/view/View$OnClickListener',
                'trackViewOnClick',
                '(Landroid/view/View;)V',
                1, 1,
                [Opcodes.ALOAD]))
        sInterfaceMethods.put('onCheckedChanged(Landroid/widget/CompoundButton;Z)V', new AnalyticsMethodCell(
                'onCheckedChanged',
                '(Landroid/widget/CompoundButton;Z)V',
                'android/widget/CompoundButton$OnCheckedChangeListener',
                'trackViewOnClick',
                '(Landroid/view/View;)V',
                1, 1,
                [Opcodes.ALOAD]))
        sInterfaceMethods.put('onRatingChanged(Landroid/widget/RatingBar;FZ)V', new AnalyticsMethodCell(
                'onRatingChanged',
                '(Landroid/widget/RatingBar;FZ)V',
                'android/widget/RatingBar$OnRatingBarChangeListener',
                'trackViewOnClick',
                '(Landroid/view/View;)V',
                1, 1,
                [Opcodes.ALOAD]))
        sInterfaceMethods.put('onStopTrackingTouch(Landroid/widget/SeekBar;)V', new AnalyticsMethodCell(
                'onStopTrackingTouch',
                '(Landroid/widget/SeekBar;)V',
                'android/widget/SeekBar$OnSeekBarChangeListener',
                'trackViewOnClick',
                '(Landroid/view/View;)V',
                1, 1,
                [Opcodes.ALOAD]))
        sInterfaceMethods.put('onCheckedChanged(Landroid/widget/RadioGroup;I)V', new AnalyticsMethodCell(
                'onCheckedChanged',
                '(Landroid/widget/RadioGroup;I)V',
                'android/widget/RadioGroup$OnCheckedChangeListener',
                'trackRadioGroup',
                '(Landroid/widget/RadioGroup;I)V',
                1, 2,
                [Opcodes.ALOAD, Opcodes.ILOAD]))
        sInterfaceMethods.put('onClick(Landroid/content/DialogInterface;I)V', new AnalyticsMethodCell(
                'onClick',
                '(Landroid/content/DialogInterface;I)V',
                'android/content/DialogInterface$OnClickListener',
                'trackDialog',
                '(Landroid/content/DialogInterface;I)V',
                1, 2,
                [Opcodes.ALOAD, Opcodes.ILOAD]))
        sInterfaceMethods.put('onItemClick(Landroid/widget/AdapterView;Landroid/view/View;IJ)V', new AnalyticsMethodCell(
                'onItemClick',
                '(Landroid/widget/AdapterView;Landroid/view/View;IJ)V',
                'android/widget/AdapterView$OnItemClickListener',
                'trackListView',
                '(Landroid/widget/AdapterView;Landroid/view/View;I)V',
                1, 3,
                [Opcodes.ALOAD, Opcodes.ALOAD, Opcodes.ILOAD]))
        sInterfaceMethods.put('onItemSelected(Landroid/widget/AdapterView;Landroid/view/View;IJ)V', new AnalyticsMethodCell(
                'onItemSelected',
                '(Landroid/widget/AdapterView;Landroid/view/View;IJ)V',
                'android/widget/AdapterView$OnItemSelectedListener',
                'trackListView',
                '(Landroid/widget/AdapterView;Landroid/view/View;I)V',
                1, 3,
                [Opcodes.ALOAD, Opcodes.ALOAD, Opcodes.ILOAD]))
        sInterfaceMethods.put('onGroupClick(Landroid/widget/ExpandableListView;Landroid/view/View;IJ)Z', new AnalyticsMethodCell(
                'onGroupClick',
                '(Landroid/widget/ExpandableListView;Landroid/view/View;IJ)Z',
                'android/widget/ExpandableListView$OnGroupClickListener',
                'trackExpandableListViewOnGroupClick',
                '(Landroid/widget/ExpandableListView;Landroid/view/View;I)V',
                1, 3,
                [Opcodes.ALOAD, Opcodes.ALOAD, Opcodes.ILOAD]))
        sInterfaceMethods.put('onChildClick(Landroid/widget/ExpandableListView;Landroid/view/View;IIJ)Z', new AnalyticsMethodCell(
                'onChildClick',
                '(Landroid/widget/ExpandableListView;Landroid/view/View;IIJ)Z',
                'android/widget/ExpandableListView$OnChildClickListener',
                'trackExpandableListViewOnChildClick',
                '(Landroid/widget/ExpandableListView;Landroid/view/View;II)V',
                1, 4,
                [Opcodes.ALOAD, Opcodes.ALOAD, Opcodes.ILOAD, Opcodes.ILOAD]))
        sInterfaceMethods.put('onTabChanged(Ljava/lang/String;)V', new AnalyticsMethodCell(
                'onTabChanged',
                '(Ljava/lang/String;)V',
                'android/widget/TabHost$OnTabChangeListener',
                'trackTabHost',
                '(Ljava/lang/String;)V',
                1, 1,
                [Opcodes.ALOAD]))

        sInterfaceMethods.put('onTabSelected(Landroid/support/design/widget/TabLayout$Tab;)V', new AnalyticsMethodCell(
                'onTabSelected',
                '(Landroid/support/design/widget/TabLayout$Tab;)V',
                'android/support/design/widget/TabLayout$OnTabSelectedListener',
                'trackTabLayoutSelected',
                '(Ljava/lang/Object;Ljava/lang/Object;)V',
                0, 2,
                [Opcodes.ALOAD, Opcodes.ALOAD]))

        sInterfaceMethods.put('onTabSelected(Lcom/google/android/material/tabs/TabLayout$Tab;)V', new AnalyticsMethodCell(
                'onTabSelected',
                '(Lcom/google/android/material/tabs/TabLayout$Tab;)V',
                'com/google/android/material/tabs/TabLayout$OnTabSelectedListener',
                'trackTabLayoutSelected',
                '(Ljava/lang/Object;Ljava/lang/Object;)V',
                0, 2,
                [Opcodes.ALOAD, Opcodes.ALOAD]))

        // Todo: 扩展
    }

    /**
     * Fragment中的方法
     */
    public final static HashMap<String, AnalyticsMethodCell> sFragmentMethods = new HashMap<>()
    static {
        sFragmentMethods.put('onResume()V', new AnalyticsMethodCell(
                'onResume',
                '()V',
                '',// parent省略，均为 android/app/Fragment 或 android/support/v4/app/Fragment
                'trackFragmentResume',
                '(Ljava/lang/Object;)V',
                0, 1,
                [Opcodes.ALOAD]))
        sFragmentMethods.put('setUserVisibleHint(Z)V', new AnalyticsMethodCell(
                'setUserVisibleHint',
                '(Z)V',
                '',// parent省略，均为 android/app/Fragment 或 android/support/v4/app/Fragment
                'trackFragmentSetUserVisibleHint',
                '(Ljava/lang/Object;Z)V',
                0, 2,
                [Opcodes.ALOAD, Opcodes.ILOAD]))
        sFragmentMethods.put('onHiddenChanged(Z)V', new AnalyticsMethodCell(
                'onHiddenChanged',
                '(Z)V',
                '',
                'trackOnHiddenChanged',
                '(Ljava/lang/Object;Z)V',
                0, 2,
                [Opcodes.ALOAD, Opcodes.ILOAD]))
        sFragmentMethods.put('onViewCreated(Landroid/view/View;Landroid/os/Bundle;)V', new AnalyticsMethodCell(
                'onViewCreated',
                '(Landroid/view/View;Landroid/os/Bundle;)V',
                '',
                'onFragmentViewCreated',
                '(Ljava/lang/Object;Landroid/view/View;Landroid/os/Bundle;)V',
                0, 3,
                [Opcodes.ALOAD, Opcodes.ALOAD, Opcodes.ALOAD]))
    }

    /**
     * android.gradle 3.2.1 版本中，针对 Lambda 表达式处理
     */
    public final static HashMap<String, AnalyticsMethodCell> sLambdaMethods = new HashMap<>()
    static {
        sLambdaMethods.put('(Landroid/view/View;)V', new AnalyticsMethodCell(
                'onClick',
                '(Landroid/view/View;)V',
                'android/view/View$OnClickListener',
                'trackViewOnClick',
                '(Landroid/view/View;)V',
                1, 1,
                [Opcodes.ALOAD]))
        sLambdaMethods.put('(Landroid/widget/CompoundButton;Z)V', new AnalyticsMethodCell(
                'onCheckedChanged',
                '(Landroid/widget/CompoundButton;Z)V',
                'android/widget/CompoundButton$OnCheckedChangeListener',
                'trackViewOnClick',
                '(Landroid/view/View;)V',
                1, 1,
                [Opcodes.ALOAD]))
        sLambdaMethods.put('(Landroid/widget/RatingBar;FZ)V', new AnalyticsMethodCell(
                'onRatingChanged',
                '(Landroid/widget/RatingBar;FZ)V',
                'android/widget/RatingBar$OnRatingBarChangeListener',
                'trackViewOnClick',
                '(Landroid/view/View;)V',
                1, 1,
                [Opcodes.ALOAD]))
        sLambdaMethods.put('(Landroid/widget/SeekBar;)V', new AnalyticsMethodCell(
                'onStopTrackingTouch',
                '(Landroid/widget/SeekBar;)V',
                'android/widget/SeekBar$OnSeekBarChangeListener',
                'trackViewOnClick',
                '(Landroid/view/View;)V',
                1, 1,
                [Opcodes.ALOAD]))
        sLambdaMethods.put('(Landroid/widget/RadioGroup;I)V', new AnalyticsMethodCell(
                'onCheckedChanged',
                '(Landroid/widget/RadioGroup;I)V',
                'android/widget/RadioGroup$OnCheckedChangeListener',
                'trackRadioGroup',
                '(Landroid/widget/RadioGroup;I)V',
                1, 2,
                [Opcodes.ALOAD, Opcodes.ILOAD]))
        sLambdaMethods.put('(Landroid/content/DialogInterface;I)V', new AnalyticsMethodCell(
                'onClick',
                '(Landroid/content/DialogInterface;I)V',
                'android/content/DialogInterface$OnClickListener',
                'trackDialog',
                '(Landroid/content/DialogInterface;I)V',
                1, 2,
                [Opcodes.ALOAD, Opcodes.ILOAD]))
        sLambdaMethods.put('(Landroid/widget/AdapterView;Landroid/view/View;IJ)V', new AnalyticsMethodCell(
                'onItemClick',
                '(Landroid/widget/AdapterView;Landroid/view/View;IJ)V',
                'android/widget/AdapterView$OnItemClickListener',
                'trackListView',
                '(Landroid/widget/AdapterView;Landroid/view/View;I)V',
                1, 3,
                [Opcodes.ALOAD, Opcodes.ALOAD, Opcodes.ILOAD]))
        sLambdaMethods.put('(Landroid/widget/AdapterView;Landroid/view/View;IJ)V', new AnalyticsMethodCell(
                'onItemSelected',
                '(Landroid/widget/AdapterView;Landroid/view/View;IJ)V',
                'android/widget/AdapterView$OnItemSelectedListener',
                'trackListView',
                '(Landroid/widget/AdapterView;Landroid/view/View;I)V',
                1, 3,
                [Opcodes.ALOAD, Opcodes.ALOAD, Opcodes.ILOAD]))
        sLambdaMethods.put('(Landroid/widget/ExpandableListView;Landroid/view/View;IJ)Z', new AnalyticsMethodCell(
                'onGroupClick',
                '(Landroid/widget/ExpandableListView;Landroid/view/View;IJ)Z',
                'android/widget/ExpandableListView$OnGroupClickListener',
                'trackExpandableListViewOnGroupClick',
                '(Landroid/widget/ExpandableListView;Landroid/view/View;I)V',
                1, 3,
                [Opcodes.ALOAD, Opcodes.ALOAD, Opcodes.ILOAD]))
        sLambdaMethods.put('(Landroid/widget/ExpandableListView;Landroid/view/View;IIJ)Z', new AnalyticsMethodCell(
                'onChildClick',
                '(Landroid/widget/ExpandableListView;Landroid/view/View;IIJ)Z',
                'android/widget/ExpandableListView$OnChildClickListener',
                'trackExpandableListViewOnChildClick',
                '(Landroid/widget/ExpandableListView;Landroid/view/View;II)V',
                1, 4,
                [Opcodes.ALOAD, Opcodes.ALOAD, Opcodes.ILOAD, Opcodes.ILOAD]))
        sLambdaMethods.put('(Ljava/lang/String;)V', new AnalyticsMethodCell(
                'onTabChanged',
                '(Ljava/lang/String;)V',
                'android/widget/TabHost$OnTabChangeListener',
                'trackTabHost',
                '(Ljava/lang/String;)V',
                1, 1,
                [Opcodes.ALOAD]))

        sLambdaMethods.put('(Landroid/view/MenuItem;)Z', new AnalyticsMethodCell(
                'onNavigationItemSelected',
                '(Landroid/view/MenuItem;)Z',
                'android/support/design/widget/NavigationView$OnNavigationItemSelectedListener',
                'trackMenuItem',
                '(Ljava/lang/Object;Landroid/view/MenuItem;)V',
                0, 2,
                [Opcodes.ALOAD, Opcodes.ALOAD]))

        //static
        sLambdaMethods.put('(Landroid/view/MenuItem;)Z2', new AnalyticsMethodCell(
                'onNavigationItemSelected',
                '(Landroid/view/MenuItem;)Z',
                'android/support/design/widget/NavigationView$OnNavigationItemSelectedListener',
                'trackMenuItem',
                '(Landroid/view/MenuItem;)V',
                0, 1,
                [Opcodes.ALOAD]))

        // Todo: 扩展
    }

    private static final HashSet<String> targetFragmentClass = new HashSet()

    static {
        /**
         * Fragment
         */
        targetFragmentClass.add('android/support/v4/app/Fragment')
        targetFragmentClass.add('android/support/v4/app/ListFragment')
        targetFragmentClass.add('android/support/v4/app/DialogFragment')

        /**
         * For AndroidX Fragment
         */
        targetFragmentClass.add('androidx/fragment/app/Fragment')
        targetFragmentClass.add('androidx/fragment/app/ListFragment')
        targetFragmentClass.add('androidx/fragment/app/DialogFragment')
    }

    private static final HashSet<String> targetMenuMethodDesc = new HashSet()
    /**
     * Menu
     */
    static {
        targetMenuMethodDesc.add("onContextItemSelected(Landroid/view/MenuItem;)Z")
        targetMenuMethodDesc.add("onOptionsItemSelected(Landroid/view/MenuItem;)Z")
        targetMenuMethodDesc.add("onNavigationItemSelected(Landroid/view/MenuItem;)Z")
    }


    static void addAnno(MethodVisitor mv) {
        AnnotationVisitor annotationVisitor =
                mv.visitAnnotation(trackAnnoClassName, true);
        annotationVisitor.visitEnd();
    }

    static boolean isTargetMenuMethodDesc(String nameDesc) {
        return targetMenuMethodDesc.contains(nameDesc)
    }

    static boolean isInstanceOfFragment(String superName) {
        return targetFragmentClass.contains(superName)
    }

    static boolean isPrivate(int access) {
        return (access & ACC_PRIVATE) != 0;
    }

    static boolean isSynthetic(int access) {
        return (access & ACC_SYNTHETIC) != 0;
    }

    static boolean isPublic(int access) {
        return (access & ACC_PUBLIC) != 0;
    }

    static boolean isStatic(int access) {
        return (access & ACC_STATIC) != 0;
    }

    static String convertSignature(String name, String desc) {
        Type method = Type.getType(desc);
        StringBuilder sb = new StringBuilder();
        sb.append(method.getReturnType().getClassName()).append(" ").append(name);
        sb.append("(");
        for (int i = 0; i < method.getArgumentTypes().length; i++) {
            sb.append(method.getArgumentTypes()[i].getClassName());
            if (i != method.getArgumentTypes().length - 1) {
                sb.append(",");
            }
        }
        sb.append(")");
        return sb.toString();
    }

    static void visitMethodWithLoadedParams(MethodVisitor methodVisitor, int opcode, String owner, String methodName, String methodDesc, int start, int count, List<Integer> paramOpcodes) {
        for (int i = start; i < start + count; i++) {
            methodVisitor.visitVarInsn(paramOpcodes.get(i - start), i);
        }

//        println "2-opcode " + opcode
//        println "2-owner " + owner
//        println "2-methodName " + methodName
//        println "2-methodDesc " + methodDesc

        methodVisitor.visitMethodInsn(opcode, owner, methodName, methodDesc, false);
    }


}
