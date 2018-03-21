package com.viewfunction.consoleApplication

import com.viewfunction.consoleApplication.exception.ApplicationInitException
import com.viewfunction.consoleApplication.util.{ApplicationFactory, ApplicationLauncherUtil}

import scala.io.StdIn

object ApplicationLaunchpad {

  var applicationRunningFlag=true
  var applicationAbnormalCloseFlag=true
  val consoleApplication=ApplicationFactory.createConsoleApplication()

  def main(args:Array[String]): Unit ={
    try{
      sys.addShutdownHook(cleanApplicationResource)
      if(consoleApplication.initApplication){
        ApplicationLauncherUtil.printApplicationConsoleBanner()
        while(applicationRunningFlag){
          handleConsoleInputCommands
        }
      }else{
        throw new ApplicationInitException
      }
    }catch{
      case e:Exception=>
        val finalException=new ApplicationInitException
        finalException.initCause(e)
        throw finalException
    }
  }

  private def handleConsoleInputCommands(): Unit = {
    print(">_")
    val command  =  StdIn.readLine()
    println("Processing [ " + command + " ]......")
    if (command == ApplicationLauncherUtil.getApplicationExitCommand) {
      stopApplication
    }else{
      consoleApplication.executeConsoleCommand(command)
    }
  }

  private def stopApplication(): Unit ={
    applicationAbnormalCloseFlag=false
    if(!consoleApplication.shutdownApplication()) println("Clean Application Resource Error")
    applicationRunningFlag=false
  }

  private def cleanApplicationResource(): Unit ={
    if (applicationAbnormalCloseFlag) {
      if(!consoleApplication.shutdownApplication()) println("Clean Application Resource Error")
    }
  }
}
