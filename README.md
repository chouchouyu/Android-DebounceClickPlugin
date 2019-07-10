# Android-DebounceClickPlugin  ![[logo]](https://raw.githubusercontent.com/chouchouyu/Android-DebounceClickPlugin/master/files/logo.png)
```
public abstract class NoDoubleClickListener implements View.OnClickListener {

    public static final int MIN_CLICK_DELAY_TIME = 1000;
    private long lastClickTime = 0;

    @Override
    public void onClick(View v) {
        long currentTime = SystemClock.elapsedRealtime();
        if (currentTime - lastClickTime > MIN_CLICK_DELAY_TIME) {
            lastClickTime = currentTime;
            onNoDoubleClick(v);
        }
    }

    protected abstract void onNoDoubleClick(View view);
}

submitButton.setOnClickListener(new NoDoubleClickListener() {  
        @Override  
        public void onNoDoubleClick(View v) {  
            submitOrder();  
        }  
}); 
```
>
 * 这段代码是不是很熟悉....
 * 项目大了，一个个click的数手酸不酸...
 * 能不能把`butterknife`,`databanding`,`rxbinding`,`lambda表达式`,里的点击一并处理了...

##### --------------------其实我是看到没有人写才弄了这个库的--------------------
# You need Android-DebounceClickPlugin!


## 效果
### 接入前
![[before]](https://raw.githubusercontent.com/chouchouyu/Android-DebounceClickPlugin/master/files/before.png)
### 接入后
![[after]](https://raw.githubusercontent.com/chouchouyu/Android-DebounceClickPlugin/master/files/after.png)
##### 它将在`gradle build `时，自动在每个onClick(View)添加if语句判断 



## 用法
[ ![Download](https://api.bintray.com/packages/wusanm/maven/debounceclick-plugin/images/download.svg?version=1.0.2) ](https://bintray.com/wusanm/maven/debounceclick-plugin/1.0.2/link)
1. 在项目根目录下的build.gradle 添加
```
    dependencies {
       ...
        classpath 'com.github.susan:debounceclick-plugin:1.0.1'
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
![[packageName]](https://raw.githubusercontent.com/chouchouyu/Android-DebounceClickPlugin/master/files/packageName.png)
* includePackages(添加修改文件路径)/excludePackages(排除修改文件路径)

* debug = true 显示编译过程中详细日志

![[logger]](https://raw.githubusercontent.com/chouchouyu/Android-DebounceClickPlugin/master/files/logger.png)

2. 如果编译通过。更改过了信息会显示在`module/build/outputs/DebounceClick/mapping`下
![[Mapping]](https://raw.githubusercontent.com/chouchouyu/Android-DebounceClickPlugin/master/files/Mapping.png)

一般如果文件中有内容就说明插件生效，

相反，如果没有内容，就说明没本插件没生效。

没生效时app运行和没接入之前一样。

**如果发现没生效，欢迎跟给我提issue。:-D**

## 不想处理的方法
  添加@DebounceClickMark即可
![[passMethod]](https://raw.githubusercontent.com/chouchouyu/Android-DebounceClickPlugin/master/files/passMethod.png)
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

# butterknife 
考虑butterknife自带有DebouncingOnClickListener(但是不是通过毫秒判断的)，

如果任然希望通过毫秒来判断事件点击，在includePackages添加如下内容。
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
 databanding会引入的点击事件并非是`View.OnClickListener -> onClick(View view)`的方法，所以本插件添加注解`@DebounceClickExtra`专门处理方法不是`onClick`的方法。
![[extrClickMethod]](https://raw.githubusercontent.com/chouchouyu/Android-DebounceClickPlugin/master/files/extrClickMethod.png)
 如上图所示`onXXClick方`法会被处理，而没有注释的`onwithoutClick`不会被处理。
 
## Logcat(1.0.2+)
可以通过Logcat `DebounceClickHandler` 查看插件运行情况
![[logcat]](https://raw.githubusercontent.com/chouchouyu/Android-DebounceClickPlugin/master/files/logcat.png)


# Thanks to
* [SmartDengg/sug-debounce](https://github.com/SmartDengg/sug-debounce)
* [JieYuShi/Luffy](https://github.com/JieYuShi/Luffy)
* [sa-sdk-android-plugin2](https://github.com/sensorsdata/sa-sdk-android-plugin2)
* [Qihoo360/RePlugin](https://github.com/Qihoo360/RePlugin)
         
 
