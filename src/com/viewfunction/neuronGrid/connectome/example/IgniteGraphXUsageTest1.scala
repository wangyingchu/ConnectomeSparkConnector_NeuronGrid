package com.viewfunction.neuronGrid.connectome.example

import com.viewfunction.neuronGrid.connectome.memorySpark.IgniteSparkSession

import org.apache.log4j.{Level, Logger}
import org.apache.spark.graphx.{Edge, Graph, VertexId}
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.SQLContext
import org.apache.spark.{SparkConf, SparkContext}

import scala.util.hashing.MurmurHash3

object IgniteGraphXUsageTest1 extends App{

  // Adjust the logger to exclude the logs of no interest.
  Logger.getRootLogger.setLevel(Level.ERROR)
  Logger.getLogger("org.apache.ignite").setLevel(Level.ERROR)
  Logger.getLogger("org.apache.spark").setLevel(Level.ERROR)

  val sparkConf = new SparkConf().setMaster("local").setAppName("My App")
  val sparkContext = new SparkContext(sparkConf)
  val memorySession=new IgniteSparkSession(sparkContext)
  try{
    val sqlContext = new SQLContext(sparkContext)
    import sqlContext.implicits._

    //airportsIgniteRDD:RDD[(VertexId, (String))]
    //flightsInnerIgniteRDD:RDD[(IgniteUuid,Edge[Int])]
    val airportsIgniteRDD=memorySession.getPairRDD[VertexId, (String)]("airportsIgniteRDD",classOf[VertexId],classOf[(String)])
    val flightsInnerIgniteRDD=memorySession.getValueRDD("flightsIgniteRDD",classOf[Edge[Int]])

    if(airportsIgniteRDD.count()==0||flightsInnerIgniteRDD.count()==0){
      //there is no data in memory data store,load it from disk
      airportsIgniteRDD.clear()
      flightsInnerIgniteRDD.clear()

      val df_1 = sqlContext.read.format("com.databricks.spark.csv").option("header", "true").load("extData/2008.csv")

      val airportCodes = df_1.select("Origin", "Dest").flatMap(x => Iterable(x(0).toString, x(1).toString))
      val airportVertices: RDD[(VertexId, String)] = airportCodes.distinct().map(x => (MurmurHash3.stringHash(x).asInstanceOf[VertexId], x)).rdd
      airportsIgniteRDD.savePairs(airportVertices)

      val flightsFromTo = df_1.select("Origin","Dest")
      val flightEdges = flightsFromTo.map(x =>
        ((MurmurHash3.stringHash(x(0).toString),MurmurHash3.stringHash(x(1).toString)), 1)).rdd.reduceByKey(_+_).map(x => Edge(x._1._1, x._1._2,x._2))
      flightsInnerIgniteRDD.saveValues(flightEdges)
    }

    val flightsIgniteRDD:RDD[Edge[Int]]=flightsInnerIgniteRDD.map(_._2)

    val defaultAirport = ("Missing")
    //val graph = Graph(airportVertices, flightEdges, defaultAirport)
    val graph = Graph(airportsIgniteRDD, flightsIgniteRDD, defaultAirport)
    graph.persist()
    println(graph.numVertices) // 305
    println(graph.numEdges) // 5366
    graph.triplets.sortBy(_.attr, ascending=false).map(triplet =>
      "There were " + triplet.attr.toString + " flights from " + triplet.srcAttr + " to " + triplet.dstAttr + ".").take(10).foreach(println(_))
    graph.triplets.sortBy(_.attr).map(triplet =>
      "There were " + triplet.attr.toString + " flights from " + triplet.srcAttr + " to " + triplet.dstAttr + ".").take(10).foreach(println(_))
    graph.inDegrees.join(airportsIgniteRDD).sortBy(_._2._1, ascending=false).take(1).foreach(println(_))
    graph.outDegrees.join(airportsIgniteRDD).sortBy(_._2._1, ascending=false).take(1).foreach(println(_))
    val ranks = graph.pageRank(0.0001).vertices
    val ranksAndAirports = ranks.join(airportsIgniteRDD).sortBy(_._2._1, ascending=false).map(_._2._2)
    ranksAndAirports.take(10).foreach(println(_))

  }catch{
    case e:Exception=> {
    e.printStackTrace()
  }
  }finally memorySession.closeSession(true)
}
