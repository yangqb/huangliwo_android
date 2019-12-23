package com.feitianzhu.fu700.common.impl;

public interface onConnectionFinishLinstener {
  void onSuccess(int code, Object result);

  void onFail(int code, String result);
}