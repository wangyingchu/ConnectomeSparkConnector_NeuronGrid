package com.viewfunction.neuronGrid.connectome.memorySpark

import com.viewfunction.neuronGrid.connectome.connectionNodeDataStore.ConnectomeDataStoreInvoker
import org.apache.ignite.lang.IgniteUuid
import org.apache.ignite.spark.{IgniteContext, IgniteRDD}
import org.apache.spark.SparkContext

class IgniteSparkSession(private val sparkContext:SparkContext) {

  // Defines spring cache Configuration path.
  private val CONFIG = "appConfig/connectome-ignite.xml"
  // Creates Ignite context with above configuration.
  private val igniteContext = new IgniteContext(sparkContext, CONFIG, true)

  def getPairRDD[K,V](rddName:String,keyClass:Class[K],valueClass:Class[V]):IgniteRDD[K, V]={
    val connectomeDataStoreInvoker:ConnectomeDataStoreInvoker = new ConnectomeDataStoreInvoker
    connectomeDataStoreInvoker.setDataAccessSession(igniteContext.ignite())
    if(connectomeDataStoreInvoker.getGridDataStore(rddName)==null){
      connectomeDataStoreInvoker.createGridSingletonDataStore(rddName,keyClass, valueClass)
    }
    igniteContext.fromCache[K, V](rddName)
  }

  def getValueRDD[V](rddName:String,valueClass:Class[V]):IgniteRDD[IgniteUuid, V]={
    val connectomeDataStoreInvoker:ConnectomeDataStoreInvoker = new ConnectomeDataStoreInvoker
    connectomeDataStoreInvoker.setDataAccessSession(igniteContext.ignite())
    if(connectomeDataStoreInvoker.getGridDataStore(rddName)==null){
      connectomeDataStoreInvoker.createGridSingletonDataStore(rddName,classOf[Any],valueClass)
    }
    igniteContext.fromCache(rddName)
  }

  def getPairRDD(rddName:String):IgniteRDD[Any, Any]={
    val connectomeDataStoreInvoker:ConnectomeDataStoreInvoker = new ConnectomeDataStoreInvoker
    connectomeDataStoreInvoker.setDataAccessSession(igniteContext.ignite())
    if(connectomeDataStoreInvoker.getGridDataStore(rddName)==null){
      connectomeDataStoreInvoker.createGridSingletonDataStore(rddName,classOf[Any], classOf[Any])
    }
    igniteContext.fromCache[Any, Any](rddName)
  }

  def getValueRDD(rddName:String):IgniteRDD[IgniteUuid, Any]={
    val connectomeDataStoreInvoker:ConnectomeDataStoreInvoker = new ConnectomeDataStoreInvoker
    connectomeDataStoreInvoker.setDataAccessSession(igniteContext.ignite())
    if(connectomeDataStoreInvoker.getGridDataStore(rddName)==null){
      connectomeDataStoreInvoker.createGridSingletonDataStore(rddName,classOf[Any],classOf[Any])
    }
    igniteContext.fromCache(rddName)
  }

  def closeSession(stopSparkContext:Boolean):Unit={
    // Close IgniteContext on all workers.
    igniteContext.close(true)
    // Stop SparkContext.
    if(stopSparkContext) sparkContext.stop()
  }
}
