package com.viewfunction.consoleApplication

abstract class BaseApplication {
  def initApplication():Boolean
  def shutdownApplication():Boolean
  def executeConsoleCommand(command:String):Unit
}
