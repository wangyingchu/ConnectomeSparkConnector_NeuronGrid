package com.viewfunction.neuronGrid.connectome.businessLogic.businessOptionsProcessor

import com.viewfunction.neuronGrid.connectome.connectionNodeDataStore.{ConnectomeDataStoreInvoker, NeuronGridDataStore}
import com.viewfunction.neuronGrid.connectome.consoleCommandHandler.BaseCommandProcessor
import org.apache.ignite.Ignite

class SampleLPDataCommandProcessor (nodeIgnite:Ignite) extends BaseCommandProcessor{

  override def processCommand(commandContent: String): Unit = {
    val connectomeDataStoreInvoker:ConnectomeDataStoreInvoker = new ConnectomeDataStoreInvoker
    connectomeDataStoreInvoker.setDataAccessSession(nodeIgnite)
    val targetDataStore = connectomeDataStoreInvoker.getGridDataStore("LP_DATASTORE")
    if(targetDataStore==null){
      println("LP_DATASTORE not exist")
    }else{
      var currentData:LPData=null
      for( i<-0 until 100){
        currentData=targetDataStore.asInstanceOf[NeuronGridDataStore[String,LPData]].getData("LPDATA_"+ i)
        if(currentData!=null){
          println("LPDATA_"+ i+": "+currentData.listingId+"|"+currentData.recordDate+"|"+currentData.到期日期+"|"+
            currentData.剩余利息+"|"+currentData.剩余本金+"|"+currentData.应还利息+"|"+currentData.应还本金+"|"+
            currentData.期数+"|"+currentData.还款日期+"|"+currentData.还款状态)
        }
      }
    }
  }
}
