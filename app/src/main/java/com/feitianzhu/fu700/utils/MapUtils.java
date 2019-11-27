package com.feitianzhu.fu700.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by dicallc on 2017/9/27 0027.
 */

public class MapUtils {
  public static boolean isAvilible(Context context, String packageName){
    //获取packagemanager
    final PackageManager packageManager = context.getPackageManager();
    //获取所有已安装程序的包信息
    List<PackageInfo> packageInfos = packageManager.getInstalledPackages(0);
    //用于存储所有已安装程序的包名
    List<String> packageNames = new ArrayList<String>();
    //从pinfo中将包名字逐一取出，压入pName list中
    if(packageInfos != null){
      for(int i = 0; i < packageInfos.size(); i++){
        String packName = packageInfos.get(i).packageName;
        packageNames.add(packName);
      }
    }
    //判断packageNames中是否有目标程序的包名，有TRUE，没有FALSE
    return packageNames.contains(packageName);
  }
}
