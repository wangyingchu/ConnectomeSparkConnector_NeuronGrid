package com.viewfunction.consoleApplication.util

import java.io.{FileInputStream, FileNotFoundException, IOException}
import java.util.{Date, Properties}

object ApplicationLauncherUtil {

  private val _applicationInfoProperties = new Properties;

  def getApplicationInfoPropertyValue(resourceFileName: String): String = {
    if (_applicationInfoProperties.isEmpty) {
      try
        _applicationInfoProperties.load(new FileInputStream("appConfig/applicationInfo.properties"))
      catch {
        case e: FileNotFoundException =>
          e.printStackTrace()
        case e: IOException =>
          e.printStackTrace()
      }
    }
    _applicationInfoProperties.getProperty(resourceFileName)
  }

  def getApplicationExitCommand: String = getApplicationInfoPropertyValue("applicationExitCommand")

  def printApplicationConsoleBanner(): Unit = {
    val bannerStringBuffer = new StringBuffer
    val echo0 = "=========================================================="
    val echo1 = "    _  __                       _____    _    __"
    val echo2 = "   / |/ /__ __ _________  ___  / ___/___(_)__/ /"
    val echo3 = "  /    / -_) // / __/ _ \\/ _ \\/ (_ / __/ / _  /"
    val echo4 = " /_/|_/\\__/\\_,_/_/  \\___/_//_/\\___/_/ /_/\\_,_/"
    val echo5 = "---------------------------------------------------------"
    bannerStringBuffer.append(echo0)
    bannerStringBuffer.append("\n\r")
    bannerStringBuffer.append(echo1)
    bannerStringBuffer.append("\n\r")
    bannerStringBuffer.append(echo2)
    bannerStringBuffer.append("\n\r")
    bannerStringBuffer.append(echo3)
    bannerStringBuffer.append("\n\r")
    bannerStringBuffer.append(echo4)
    bannerStringBuffer.append("\n\r")
    bannerStringBuffer.append("\n\r")
    bannerStringBuffer.append(echo5)
    bannerStringBuffer.append("\n\r")
    bannerStringBuffer.append(getApplicationInfoPropertyValue("applicationName") + " | " + "ver. " + getApplicationInfoPropertyValue("applicationVersion"))
    bannerStringBuffer.append("\n\r")
    val echo6 = "---------------------------------------------------------"
    bannerStringBuffer.append(echo6)
    bannerStringBuffer.append("\n\r")
    bannerStringBuffer.append("\n\r")
    val prop = System.getProperties
    val osName = prop.getProperty("os.name")
    val osVersion = prop.getProperty("os.version")
    val osArch = prop.getProperty("os.arch")
    val osInfo = osName + " " + osVersion + " " + osArch
    bannerStringBuffer.append("OS: " + osInfo)
    bannerStringBuffer.append("\n\r")
    bannerStringBuffer.append("User: " + prop.getProperty("user.name"))
    bannerStringBuffer.append("\n\r")
    val jvmVendor = prop.getProperty("java.vm.vendor")
    val jvmName = prop.getProperty("java.vm.name")
    val jvmVersion = prop.getProperty("java.vm.version")
    bannerStringBuffer.append("JVM: ver. " + prop.getProperty("java.version") + " " + jvmVendor)
    bannerStringBuffer.append("\n\r")
    bannerStringBuffer.append("     " + jvmName + " " + jvmVersion)
    bannerStringBuffer.append("\n\r")
    bannerStringBuffer.append("Started at: " + new Date + "")
    bannerStringBuffer.append("\n\r")
    val echo7 = "=========================================================="
    bannerStringBuffer.append(echo7)
    System.out.println(bannerStringBuffer.toString)
  }

}
