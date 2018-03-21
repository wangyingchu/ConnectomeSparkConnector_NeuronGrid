package com.viewfunction.neuronGrid.connectome.consoleCommandHandler

import org.apache.ignite.Ignite

class ShowHelpInfoCommandProcessor(nodeIgnite:Ignite) extends BaseCommandProcessor {
  override def processCommand(s: String): Unit = {
    val appInfoStringBuffer = new StringBuffer
    appInfoStringBuffer.append("\n\r")
    appInfoStringBuffer.append("================================================================")
    appInfoStringBuffer.append("\n\r")
    appInfoStringBuffer.append("Available Options : ")
    appInfoStringBuffer.append("\n\r")
    appInfoStringBuffer.append("-------------------------------------------------------------")
    appInfoStringBuffer.append("\n\r")
    appInfoStringBuffer.append("loadlp    " + "load ppdai data")
    appInfoStringBuffer.append("\n\r")
    appInfoStringBuffer.append("samplelp  " + "sample 100 ppdai data record")
    appInfoStringBuffer.append("\n\r")
    appInfoStringBuffer.append("help:     " + "Show available option list.")
    appInfoStringBuffer.append("\n\r")
    appInfoStringBuffer.append("================================================================")
    println(appInfoStringBuffer.toString)
  }
}
