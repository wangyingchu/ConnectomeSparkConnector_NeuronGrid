package com.viewfunction.neuronGrid.connectome.businessLogic.businessOptionsProcessor

import java.util.Date

import com.viewfunction.neuronGrid.connectome.connectionNodeDataStore.{ConnectomeDataStoreInvoker, NeuronGridDataStore}
import com.viewfunction.neuronGrid.connectome.consoleCommandHandler.BaseCommandProcessor
import org.apache.ignite.Ignite

import scala.io.Source

class LoadLPDataCommandProcessor(nodeIgnite:Ignite) extends BaseCommandProcessor{

  override def processCommand(commandContent: String): Unit = {
    val connectomeDataStoreInvoker:ConnectomeDataStoreInvoker = new ConnectomeDataStoreInvoker
    connectomeDataStoreInvoker.setDataAccessSession(nodeIgnite)
    connectomeDataStoreInvoker.eraseDataStore("LP_DATASTORE")
    var targetDataStore = connectomeDataStoreInvoker.getGridDataStore("LP_DATASTORE")
    if(targetDataStore==null){
      targetDataStore=connectomeDataStoreInvoker.createGridSingletonDataStore("LP_DATASTORE",classOf[String],classOf[LPData])
    }else{
      targetDataStore.emptyDataStore
    }
    println("Operation start at "+new Date().toString)
    println("Operation......")
    val lpCSVData=Source.fromFile("/home/wangychu/Desktop/LP.csv","UTF-8").getLines.drop(1).take(100000)
    lpCSVData.zipWithIndex.foreach{case(lpData,index) => insertLPData("LPDATA_"+ index,lpData,targetDataStore.asInstanceOf[NeuronGridDataStore[String,LPData]])}
    //lpCSVData.toArray.par.zipWithIndex.foreach{case(lpData,index) => insertLPData("LPDATA_"+ index,lpData,targetDataStore.asInstanceOf[NeuronGridDataStore[String,LPData]])}
    println()

    println("Operation finish at "+new Date().toString)
  }

  def insertLPData(lpDataKey:String,lpData:String,targetDataStore:NeuronGridDataStore[String,LPData]): Unit ={
    val currentLPData=new LPData(lpData)
    targetDataStore.addDataIfNotExist(lpDataKey,currentLPData)
  }
}
