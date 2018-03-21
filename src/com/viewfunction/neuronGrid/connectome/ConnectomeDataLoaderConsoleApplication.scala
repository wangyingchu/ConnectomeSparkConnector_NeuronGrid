package com.viewfunction.neuronGrid.connectome

import com.viewfunction.consoleApplication.BaseApplication
import com.viewfunction.neuronGrid.connectome.consoleCommandHandler.CommandsHandler
import com.viewfunction.neuronGrid.connectome.util.{ConnectomeComponentFactory, ConnectomeNodePropertiesHandler}
import org.apache.ignite.{Ignite, Ignition}

class ConnectomeDataLoaderConsoleApplication extends BaseApplication{

  var nodeIgnite:Ignite=null
  var commandsHandler:CommandsHandler=null

  override def shutdownApplication(): Boolean = {
    nodeIgnite.close()
    true
  }

  override def initApplication(): Boolean = {
    val isClientNodeCfg=ConnectomeNodePropertiesHandler.getConfigPropertyValue("isClientNode")
    if(isClientNodeCfg.toBoolean) Ignition.setClientMode(true)
    nodeIgnite = Ignition.start(ConnectomeComponentFactory.getConnectomeNodeConfigurationFilePath)
    commandsHandler=new CommandsHandler(nodeIgnite)
    true
  }

  override def executeConsoleCommand(command: String): Unit = {
    commandsHandler.handleCommand(command)
  }
}

object ConnectomeDataLoaderConsoleApplication{
  def apply()={
    new ConnectomeDataLoaderConsoleApplication
  }
}
