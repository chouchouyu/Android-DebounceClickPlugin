# DoubleClickPlugin
1. 加注解后就不会影响 ok 
2. Lambert ok
3， butterknife https://www.2cto.com/kf/201609/547120.html 有防暴击 ok
4.databanding  ok但是 没有成功 
rxbinding ok  , 因为和butterknife一起可以用可以去除 


avatar!

1.改名 ok 
2.改暴击方法 觉得还是原来的好
3.extra
4.Mapping文件
 

SingleClickMarked
SingleClickMark手动设置
SingleClickIgnore自动跳过功能
Mapping 文件显示
混淆

1.自动化集成
2.文档
3.单例的说法
4.发布
5.黑马群
6.lib

https://yuqirong.me/2019/02/23/%E5%88%A9%E7%94%A8AOP%E5%AF%B9%E7%82%B9%E5%87%BB%E4%BA%8B%E4%BB%B6%E4%BD%9C%E9%98%B2%E6%8A%96%E5%A4%84%E7%90%86/
https://www.jianshu.com/p/d2ac5aa6ca76
AndroidStudio对于Java8特性的支持

//    defaultConfig {
//        multiDexEnabled true
    //不要这么写
//        jackOptions {
//            enabled true
//        }
//    }


javap -p MainActivity.class
Compiled from "MainActivity.java"
public class com.cm.android.MainActivity extends android.app.Activity implements android.view.View$OnClickListener,android.widget.AdapterView$OnItemClickListener {
  private static final java.lang.String TAG;
  public com.cm.android.MainActivity();
  protected void onCreate(android.os.Bundle);
  public void onClick(android.view.View);
  public void onItemClick(android.widget.AdapterView<?>, android.view.View, int, long);
  private void lambda$onCreate$0(android.view.View);
  static {};
}

   findViewById(R.id.button2).setOnClickListener((v) -> {
            Log.d(TAG, "onClick : " + this.getClass().getName());
        });
        
        gradlew -q app:dependencies >1.txt
        
        
   https://blog.csdn.net/zhongweijian/article/details/7868644
   使用ASM4.0实现AOP的功能，监控每个方法的执行时间
   
   
   https://my.oschina.net/wensiqun/blog/117382
   ASMSupport教程2：如何查看生成的class文件和查看log文件内容 原 荐
   
   
   http://www.wangyuwei.me/2017/03/05/ASM%E5%AE%9E%E6%88%98%E7%BB%9F%E8%AE%A1%E6%96%B9%E6%B3%95%E8%80%97%E6%97%B6/
   ASM实战统计方法耗时（demo）