# DoubleClickPlugin
1. 加注解后就不会影响
2. Lambert ok
3， butterknife https://www.2cto.com/kf/201609/547120.html 有防暴击
4.databanding 

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