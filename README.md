#debounceclick 
是一个致力于解决应用中解决onClick(View view)被多次调用问题的插件。
为当前Android最常使用的三分库：butterknife,databanding,rxbinding,以及java8 的Lambert提供了解决方案。
基于ASM一键在编译期添加判断代码，支持Android全项目/liberary部分项目。

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
apply plugin: 'com.android.application'//扫描全项目
//或者 apply plugin: 'com.android.library'//只扫描liberary部分
//apply plugin: 
apply plugin: 'com.github.susan.debounceclick'

DebounceClick {
    includePackages = []
    excludePackages = []
    debug = true
}
```
* application 扫描的是全项目/library只扫描liberary部分
* includePackages/excludePackages 后续兼容三方库会介绍
* debug = true 显示编译过程中详细日志

[ 见图]

如果编译通过。更改过了信息会显示在
[见图]

* 一般如果文件中有内容就说明插件生效，相反，如果没有内容，就说明没生效，没生效的情况下，APP还是和原来一样，只是代码没有做替换。

至此基本使用就已经完成。
#butterknife 
考虑


':debounceclick-plugin'