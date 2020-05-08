package com.hmhs.member.constants;

public class CommandLineConstants {

  private CommandLineConstants() {}

  public static final String ADB_PATH_MAC =
      System.getProperty("user.home") + "/Library/Android/sdk/platform-tools/adb";
  
  public static final String SHELL = "shell";
  
  public static final String GETPROP = "getprop";
  
  public static final String HYPHEN_S = "-s";
  
  public static final String ANDROID_VERSION = "ro.build.version.release";
  
  public static final String ANDROID_DEVICENAME = "ro.product.model";
  
  public static final String OS_NAME = System.getProperty("os.name");
  
  public static final String KILL_COMMAND = "kill";

  public static final String PKILL_COMMAND = "pkill";
  public static final String KILLALL_COMMAND = "killall";

  public static final String MAC_BROWSERSTACK_PROCESSNAME = "BrowserSt";

  public static final String WINDOWS_BROWSERSTACK_PROCESSNAME = "BrowserStackLocal";

  public static final String LSOF_COMMAND = "lsof";
 
}
