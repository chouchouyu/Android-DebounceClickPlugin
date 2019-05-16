package com.cm.android.doubleclick.plugin.temp;

import java.util.Objects;

/**
 * 创建时间:  2019/01/04 17:16 <br>
 * 作者:  SmartDengg <br>
 * 描述:
 */
public class MethodDelegate {

  private int access;
  private String name;
  private String desc;

  MethodDelegate(int access, String name, String desc) {
    this.access = access;
    this.name = name;
    this.desc = desc;
  }

  public boolean match(int access, String name, String desc) {
    return this.access == access &&
        Objects.equals(this.name, name) &&
        Objects.equals(this.desc, desc);
  }
}
