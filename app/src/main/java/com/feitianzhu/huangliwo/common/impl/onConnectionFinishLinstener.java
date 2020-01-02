package com.feitianzhu.huangliwo.common.impl;

public interface onConnectionFinishLinstener {
  void onSuccess(int code, Object result);

  void onFail(int code, String result);
}