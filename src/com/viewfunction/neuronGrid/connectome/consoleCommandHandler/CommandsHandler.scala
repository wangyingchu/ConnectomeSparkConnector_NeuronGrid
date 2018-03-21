package com.viewfunction.neuronGrid.connectome.consoleCommandHandler

import com.viewfunction.neuronGrid.connectome.businessLogic.businessOptionsProcessor.{LoadLPDataCommandProcessor, SampleLPDataCommandProcessor}
import org.apache.ignite.Ignite

class CommandsHandler(nodeIgnite:Ignite) {

  private var commandProcessorMap:Map[String,BaseCommandProcessor] = Map()

  def handleCommand(command:String): Unit ={
    command match{
      case "help" => {
        if(!commandProcessorMap.contains("help")){
          commandProcessorMap = commandProcessorMap + ("help" -> new ShowHelpInfoCommandProcessor(nodeIgnite))
        }
        commandProcessorMap.get("help").get.processCommand(command)
      }
      case "loadlp" => {
        if(!commandProcessorMap.contains("loadlp")){
          commandProcessorMap = commandProcessorMap + ("loadlp" -> new LoadLPDataCommandProcessor(nodeIgnite))
        }
        commandProcessorMap.get("loadlp").get.processCommand(command)
      }
      case "samplelp" => {
        if(!commandProcessorMap.contains("samplelp")){
          commandProcessorMap = commandProcessorMap + ("samplelp" -> new SampleLPDataCommandProcessor(nodeIgnite))
        }
        commandProcessorMap.get("samplelp").get.processCommand(command)
      }
      case _ => println("Command [ " + command + " ] not supported")
    }
  }
}
