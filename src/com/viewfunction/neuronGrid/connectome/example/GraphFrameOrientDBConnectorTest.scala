package com.viewfunction.neuronGrid.connectome.example

import com.viewfunction.neuronGrid.connectome.sparkConnector.GraphFrameOrientDBConnector
import org.apache.log4j.{Level, Logger}
import org.apache.spark.sql.types.{IntegerType, StringType, StructField, StructType}
import org.apache.spark.sql.{Row, SaveMode}
import org.apache.spark.{SparkConf, SparkContext}
import org.graphframes.GraphFrame

object GraphFrameOrientDBConnectorTest extends App{
  val sparkConf = new SparkConf().setMaster("local").setAppName("My App")
  val sc = new SparkContext(sparkConf)

  // Adjust the logger to exclude the logs of no interest.
  Logger.getRootLogger.setLevel(Level.ERROR)
  Logger.getLogger("org.apache.spark").setLevel(Level.ERROR)
  val doConnector=new GraphFrameOrientDBConnector(sc,"remote:localhost/sparktest","root","wyc")

  val vertexRDD=sc.parallelize(Array(Row(1), Row(2), Row(3), Row(4), Row(5)))
  val vertexRDDStructType=StructType(Seq(
    StructField("id", IntegerType)
  ))
  doConnector.saveVertexDataFrame("People",vertexRDD,vertexRDDStructType,SaveMode.Overwrite)

  val edgeRDD=sc.parallelize(Seq(
    Row(1, 2, "friends"),
    Row(2, 3, "enemy"),
    Row(3, 4, "friends"),
    Row(4, 1, "enemy")
  ))
  val edgeRDDStructType=StructType(Seq(
    StructField("src", IntegerType),
    StructField("dst", IntegerType),
    StructField("relationship", StringType)
  ))
  doConnector.saveEdgeDataFrame("RelatedTo","People",edgeRDD,edgeRDDStructType,SaveMode.Overwrite)

  val vertexDataFrame=doConnector.retrieveVertexDataFrame("People",null)
  val edgeDataFrame=doConnector.retrieveEdgeDataFrame("RelatedTo",null)
  //println(vertexDataFrame)
  //println(edgeDataFrame)
  val g = GraphFrame(vertexDataFrame, edgeDataFrame)
  println(g.triangleCount.run().count())
  g.inDegrees.show()
  g.triplets.show()

  val g1 =doConnector.retrieveGraphFrame("People",null,"RelatedTo",null)
  println(g1)
  g1.inDegrees.show()
  g1.triplets.show()
}