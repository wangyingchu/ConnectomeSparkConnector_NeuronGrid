package com.viewfunction.consoleApplication.util

import com.viewfunction.consoleApplication.BaseApplication
import com.viewfunction.neuronGrid.connectome.ConnectomeDataLoaderConsoleApplication

object ApplicationFactory {

  def createConsoleApplication():BaseApplication={
    ConnectomeDataLoaderConsoleApplication()
  }

}
