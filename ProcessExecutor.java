package com.hmhs.member.util.common;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.openqa.selenium.os.WindowsUtils;
import org.testng.TestNGException;

import com.hmhs.member.constants.CommandLineConstants;
import com.hmhs.member.constants.DeviceConstants;
import com.hmhs.member.session.SynchronizationUtil;
import com.hmhs.member.util.common.OSutil.OSType;

public class ProcessExecutor {

  public static String runCommand(String[] commands) {

    ProcessBuilder processBuilder = new ProcessBuilder(commands);
    processBuilder.redirectErrorStream(true);
    StringBuilder processOutput = null;

    Process process;
    try {
      process = processBuilder.start();

      processOutput = new StringBuilder();

      BufferedReader processOutputReader =
          new BufferedReader(new InputStreamReader(process.getInputStream()));

      String readLine;

      while ((readLine = processOutputReader.readLine()) != null) {
        processOutput.append(readLine + System.lineSeparator());
      }

      process.waitFor();
      process.destroy();

    } catch (Exception e) {
      throw new TestNGException("Unable to process commands :: " + e);
    }
    return processOutput.toString().trim();
  }

  public static Process processVedioCommand(String[] commands) throws IOException {

    ProcessBuilder processBuilder = new ProcessBuilder(commands);
    processBuilder.redirectErrorStream(true);
    return processBuilder.start();
  }

  public static Process startAndroidVideo(String vedioFilename) throws IOException {
    String[] vedioCommand = {
      DeviceConstants.ADB_PATH, "shell", "screenrecord", "/sdcard/" + vedioFilename + ".mp4"
    };
    return ProcessExecutor.processVedioCommand(vedioCommand);
  }

  public static void stopAndroidVedio(Process process, String vedioFilename)
      throws IOException, InterruptedException {
    if (process != null) {
      process.destroy();
    }
    String[] vedioCommand = {DeviceConstants.ADB_PATH, "pull", "/sdcard/" + vedioFilename + ".mp4"};
    ProcessBuilder processBuilder = new ProcessBuilder(vedioCommand);
    SynchronizationUtil.sleep(6);
    processBuilder.redirectErrorStream(true);

    Process process1 = processBuilder.start();
    StringBuilder processOutput = new StringBuilder();

    try (BufferedReader processOutputReader =
        new BufferedReader(new InputStreamReader(process1.getInputStream())); ) {
      String readLine;

      while ((readLine = processOutputReader.readLine()) != null) {
        processOutput.append(readLine + System.lineSeparator());
      }

      process1.waitFor();
      process1.destroy();
    }
  }

  public static void killProcessByPort(String processPort) {
    if (OSutil.getOStype() == OSType.LINUX || OSutil.getOStype() == OSType.MAC) {
      String[] commands = {CommandLineConstants.KILL_COMMAND, getProcessId(processPort)};
      runCommand(commands);
    }
  }

  public static void killProcessByName(String processName) {
    if (OSutil.getOStype() == OSType.LINUX || OSutil.getOStype() == OSType.MAC) {
      String[] commands = {CommandLineConstants.PKILL_COMMAND, "-i", processName};
      runCommand(commands);
    } else if (OSutil.getOStype() == OSType.WINDOWS) {
      WindowsUtils.killByName(processName + ".exe");
    }
  }

  public static void killAll(String processName) {
	  if (OSutil.getOStype() == OSType.LINUX || OSutil.getOStype() == OSType.MAC) {
		  String[] commands = {CommandLineConstants.KILLALL_COMMAND, processName};
	      runCommand(commands);
	  }
  }
  private static String getProcessId(String portNumber) {
    String[] pidCommands = {CommandLineConstants.LSOF_COMMAND, "-i", ":" + portNumber + ""};
    String processOutput = null;
    processOutput = runCommand(pidCommands);
    String[] outPut = processOutput.split("\\n");

    for (int i = 0; i < outPut.length; i++) {
      int pidCount = 0;
      if (outPut[i].contains("PID")) {
        String[] outPutSplit = outPut[i].split("\\s+");
        for (int j = 0; j < outPutSplit.length; j++) {
          if (outPutSplit[j].contains("PID")) {
            pidCount = j;
            break;
          }
        }
        String[] split1 = outPut[i + 1].split("\\s+");

        return split1[pidCount].trim();
      }
    }
    return "";
  }

  public static void main(String[] args) {
    killProcessByName("geckodriver");
  }
}
