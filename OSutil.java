package com.hmhs.member.util.common;

import com.hmhs.member.exception.ApplicationException;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Locale;
import org.apache.commons.io.IOUtils;

public class OSutil {

  private static final String WINDOWS_DRIVER = "webdrivers/windows/";
  private static final String MAC_DRIVER = "webdrivers/mac/";
  private static final String LINUX_DRIVER = "webdrivers/linux/";

  public enum OSType {
    WINDOWS,
    MAC,
    LINUX,
    OTHER
  }

  protected static OSType detectedOS;

  /**
   * detect the operating system from the os.name System property and cache the result.
   *
   * @returns - the operating system detected
   */
  public static OSType getOStype() {
    if (detectedOS == null) {

      String os = System.getProperty("os.name", "generic").toLowerCase(Locale.ENGLISH);

      if ((os.indexOf("mac") >= 0) || (os.indexOf("darwin") >= 0)) {
        detectedOS = OSType.MAC;
      } else if (os.indexOf("win") >= 0) {
        detectedOS = OSType.WINDOWS;
      } else if (os.indexOf("nux") >= 0) {
        detectedOS = OSType.LINUX;
      } else {
        detectedOS = OSType.OTHER;
      }
    }
    return detectedOS;
  }
  /**
   *
   *
   * <pre>get the driver exe path based on OS.</pre>
   *
   * @param driver driver name (chromedriver, geckodriver)
   * @return exe or binary path
   */
  public static String getDriverPath(String driver) {
    boolean isRunningOnJar = MiscUtil.isRunningJar();
    switch (getOStype()) {
      case WINDOWS:
        if (isRunningOnJar) return getWebDriverPath(WINDOWS_DRIVER, driver);
        else
          return new ResourceLoader()
              .getResource(WINDOWS_DRIVER + driver + ".exe", OSutil.class)
              .getPath();
      case MAC:
        if (isRunningOnJar) return getWebDriverPath(MAC_DRIVER, driver);
        else return new ResourceLoader().getResource(MAC_DRIVER + driver, OSutil.class).getPath();
      case LINUX:
        if (isRunningOnJar) return getWebDriverPath(LINUX_DRIVER, driver);
        else return new ResourceLoader().getResource(LINUX_DRIVER + driver, OSutil.class).getPath();
      default:
        throw new ApplicationException("OS Type is not allowed to run tests " + getOStype());
    }
  }

  private static String getWebDriverPath(String osConstant, String driver) {
    File outPutDirectory = FileUtil.makeUniqueDirectory(MiscUtil.TEMP_DIR, "drivers");
    InputStream in = new ResourceLoader().getResourceAsStream(osConstant + driver);
    OutputStream out;
    String outPath = outPutDirectory.getPath() + MiscUtil.FILE_SEPERATOR + driver;
    try {
      out = new FileOutputStream(outPath);
      IOUtils.copy(in, out);

      File fileObject = new File(outPath);
      if (!fileObject.canExecute()) fileObject.setExecutable(true);
      fileObject.deleteOnExit();
      outPutDirectory.deleteOnExit();
    } catch (IOException e) {
      throw new RuntimeException("Unable copy Drivers from jar :: ", e);
    }
    return outPath;
  }
  
  public static String getWebdriverTargetPath() {
	 // if (MiscUtil.isRunningJar())
		  return  MiscUtil.TEMP_DIR;
	 // else
		//  return new ResourceLoader().getResource("webdrivers", OSutil.class).getPath();
  }
}
