package com.feitianzhu.huangliwo.common.impl;

public interface onNetFinishLinstenerT<T>  {
  public void onSuccess(int code, T result);

  public void onFail(int code, String result);
}