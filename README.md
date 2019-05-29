#debounceclick 
>
* 是一个致力于解决应用中解决onClick(View view)被多次调用问题的插件。
* 并当前Android最常使用的三分库：butterknife,databanding,rxbinding,以及lambda表达式提供了解决方案。
* 基于ASM 在编译期自动在代码中添加暴力点击事件判断（下毒）。
## 效果
###接入前
[见图]
###接入后
[见图]

关键代码-> DebounceClickHandler

## 用法
1. 在项目根目录下的build.gradle 添加
```
    dependencies {
       ...
        classpath 'com.github.susan:debounceclick-plugin:1.0.0'
         ...
         }
```
1. 部分项目中配置插件

```
// 在 apply plugin: 'com.android.application'
//或者 apply plugin: 'com.android.library'

apply plugin: 'com.github.susan.debounceclick'
DebounceClick {
    includePackages = []
    excludePackages = []
    debug = true
}
```

* 默认情况下，扫描文件的目录来自于module下的AndroidManifest.xml下package所声明的目录
[见图]
* includePackages(添加修改文件路径)/excludePackages(排除修改文件路径)

* debug = true 显示编译过程中详细日志

[ 见图]

2. 如果编译通过。更改过了信息会显示在module/build/outputs/DebounceClick/mapping下
[见图]

> 一般如果文件中有内容就说明插件生效，相反，如果没有内容，就说明没本插件没生效。没生效时app运行和没接入之前一样。
如果发现没生效，欢迎跟给我提issue。:-D



# butterknife 
考虑butterknife自带有DebouncingOnClickListener(但是不是通过毫秒判断的)，如果任然希望通过毫秒来判断事件点击，includePackages添加如下内容。
```
apply plugin: 'com.github.susan.debounceclick'

DebounceClick {
    includePackages = ['butterknife.internal.DebouncingOnClickListener']
    excludePackages=[]
    debug = true
}
```
## lambda
如果项目gradle里android节点有声明jackOptions，将使得本插件失效。
```
android {
   //不要这么写
//        jackOptions {
//            enabled true
//        }
}
```
不用jackOptions也能lambda的解决方案，见demo.
# rxbinding
1. rxbinding和butterknife一起使用时，操作和butterknife操作一致，
2. rxbinding单独使用时，includePackages添加如下内容。
```
apply plugin: 'com.github.susan.debounceclick'

DebounceClick {
    //if your project contains rxbinding and without butterKnife
    includePackages = ['com.jakewharton.rxbinding.view.ViewClickOnSubscribe']
    excludePackages=[]
    debug = true
}
```

 ## databanding
 databanding会引入的点击事件并非是View.OnClickListener -> onClick(View view)的方法，所以本插件添加注解@DebounceClickExtra专门处理方法不是onClick的方法。
 [见图]
 如上图所示onXXClick方法会被处理，而没有注释的onwithoutClick不会被处理。
  ## 不想处理的方法
  添加@DebounceClickMark即可
  [见图]
 ## 修改间隔时间
 在项目编译成功，并生成Mapping文件后，在Application项目下做全局设置
 ```
      public class MyApp extends Application {
      
          @Override
          public void onCreate() {
              super.onCreate();
              DebounceClickHandler.FROZEN_WINDOW_MILLIS = 700L;
        
          }
         ```
 