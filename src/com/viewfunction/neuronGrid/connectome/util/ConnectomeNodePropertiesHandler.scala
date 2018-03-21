package com.viewfunction.neuronGrid.connectome.util

import java.io.{FileInputStream, FileNotFoundException, IOException}
import java.util.Properties

object ConnectomeNodePropertiesHandler {
  private var _connectomeNodeProperties =  new Properties

  def getConfigPropertyValue(resourceFileName: String): String = {
    if (_connectomeNodeProperties.isEmpty) {
      try
        _connectomeNodeProperties.load(new FileInputStream("appConfig/connectomeNodeCfg.properties"))
      catch {
        case e: FileNotFoundException =>
          e.printStackTrace()
        case e: IOException =>
          e.printStackTrace()
      }
    }
    _connectomeNodeProperties.getProperty(resourceFileName)
  }
}
