package com.easyfun.eclipse.performance.appframe.monitor.mon.po;

import java.io.Serializable;
import java.util.Date;

public class MonAccess
  implements Serializable
{
  private long accessId;
  private String className;
  private String methodName;
  private Date createDate;
  private String state;

  public void setAccessId(long accessId)
  {
    this.accessId = accessId;
  }

  public void setClassName(String className) {
    this.className = className;
  }

  public void setMethodName(String methodName) {
    this.methodName = methodName;
  }

  public void setCreateDate(Date createDate) {
    this.createDate = createDate;
  }

  public void setState(String state) {
    this.state = state;
  }

  public long getAccessId() {
    return this.accessId;
  }

  public String getClassName() {
    return this.className;
  }

  public String getMethodName() {
    return this.methodName;
  }

  public Date getCreateDate() {
    return this.createDate;
  }

  public String getState() {
    return this.state;
  }
}