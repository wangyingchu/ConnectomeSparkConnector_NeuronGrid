package com.viewfunction.neuronGrid.connectome.sparkConnector

import com.viewfunction.neuronGrid.connectome.exception.DataRestraintException
import com.viewfunction.neuronGrid.connectome.example.GraphFrameOrientDBConnectorTest.{edgeDataFrame, vertexDataFrame}
import org.apache.spark.SparkContext
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.types.StructType
import org.apache.spark.sql.{DataFrame, Row, SQLContext, SaveMode}
import org.graphframes.GraphFrame

class GraphFrameOrientDBConnector (private val sparkContext:SparkContext,
                                   private val dburl:String,
                                   private val connectionId:String,
                                   private val connectionPWD:String){
  val sqlContext = new SQLContext(sparkContext)
  import sqlContext.implicits._

  def retrieveVertexDataFrame(vertexType:String,querySQL:String):DataFrame={
    if(vertexType=="V") throw new DataRestraintException()
    val dataFrameReader=sqlContext.read.
      format("org.apache.spark.orientdb.graphs")
      .option("dburl", dburl)
      .option("user", connectionId)
      .option("password", connectionPWD)
      .option("vertextype", vertexType)
    if(querySQL!=null){
      dataFrameReader.option("query", querySQL)
    }
    dataFrameReader.load()
  }

  def retrieveEdgeDataFrame(edgeType:String,querySQL:String):DataFrame={
    if(edgeType=="E") throw new DataRestraintException()
    val dataFrameReader=sqlContext.read.
      format("org.apache.spark.orientdb.graphs")
      .option("dburl", dburl)
      .option("user", connectionId)
      .option("password", connectionPWD)
      .option("edgetype", edgeType)
    if(querySQL!=null){
      dataFrameReader.option("query", querySQL)
    }
    dataFrameReader.load()
  }

  def saveVertexDataFrame(vertexType:String,dataRDD:RDD[Row],dataStructType:StructType,dataSaveMode:SaveMode): Unit ={
    if(vertexType=="V") throw new DataRestraintException()
    sqlContext.createDataFrame(dataRDD,dataStructType)
      .write.format("org.apache.spark.orientdb.graphs")
      .option("dburl",dburl)
      .option("user", connectionId)
      .option("password", connectionPWD)
      .option("vertextype", vertexType)
      .mode(dataSaveMode)
      .save()
  }

  def saveEdgeDataFrame(edgeType:String,vertexType:String,dataRDD:RDD[Row],dataStructType:StructType,dataSaveMode:SaveMode): Unit ={
    if(edgeType=="E") throw new DataRestraintException()
    if(vertexType=="V") throw new DataRestraintException()
    sqlContext.createDataFrame(dataRDD,dataStructType)
      .write.format("org.apache.spark.orientdb.graphs")
      .option("dburl", dburl)
      .option("user", connectionId)
      .option("password", connectionPWD)
      .option("vertextype", vertexType)
      .option("edgetype", edgeType)
      .mode(dataSaveMode)
      .save()
  }

  def retrieveGraphFrame(vertexType:String,vertexQuerySQL:String,edgeType:String,edgeQuerySQL:String):GraphFrame={
    val vertexDataFrame=retrieveVertexDataFrame(vertexType,vertexQuerySQL)
    val edgeDataFrame=retrieveEdgeDataFrame(edgeType,edgeQuerySQL)
    GraphFrame(vertexDataFrame, edgeDataFrame)
  }
}
