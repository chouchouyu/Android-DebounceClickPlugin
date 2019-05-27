package com.github.susan.debounceclick.plugin

class DebounceClickExtension {
    def includePackages = []
    def excludePackages = ['butterknife.internal.DebouncingOnClickListener']
//  ['com.jakewharton.rxbinding.view.ViewClickOnSubscribe' ,'com.facebook.react.uimanager.NativeViewHierarchyManager']
    boolean debug = false
}
