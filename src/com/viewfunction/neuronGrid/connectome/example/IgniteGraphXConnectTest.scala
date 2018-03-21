package com.viewfunction.neuronGrid.connectome.example

import com.viewfunction.neuronGrid.connectome.memorySpark.IgniteSparkSession
import org.apache.log4j.{Level, Logger}
import org.apache.spark.graphx.{Edge, Graph, VertexId}
import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

object IgniteGraphXConnectTest extends App{

  val sparkConf = new SparkConf().setMaster("local").setAppName("My App")
  val sparkContext = new SparkContext(sparkConf)

  // Adjust the logger to exclude the logs of no interest.
  Logger.getRootLogger.setLevel(Level.ERROR)
  Logger.getLogger("org.apache.ignite").setLevel(Level.ERROR)

  val memorySession=new IgniteSparkSession(sparkContext)

  try{
    val usersIgniteRDD=memorySession.getPairRDD[VertexId, (String, String)]("usersIgniteRDD",classOf[VertexId],classOf[(String, String)])
    println(usersIgniteRDD.count())
    println(usersIgniteRDD.getNumPartitions)

    val users: RDD[(VertexId, (String, String))] =
    sparkContext.parallelize(Array((3L, ("rxin", "student")), (7L, ("jgonzal", "postdoc")),(5L, ("franklin", "prof")), (2L, ("istoica", "prof"))))
    usersIgniteRDD.savePairs(users)

    val relationshipsIgniteRDD=memorySession.getValueRDD("relationshipsIgniteRDD",classOf[Edge[String]])
    val relationships: RDD[Edge[String]] =
    sparkContext.parallelize(Array(Edge(3L, 7L, "collab"),  Edge(5L, 3L, "advisor"), Edge(2L, 5L, "colleague"), Edge(5L, 7L, "pi")))
    relationshipsIgniteRDD.saveValues(relationships)
    val relationshipsPureDataRDD:RDD[Edge[String]]=relationshipsIgniteRDD.map(_._2)

    // Define a default user in case there are relationship with missing user
    val defaultUser = ("John Doe", "Missing")
    // Build the initial Graph
    val graph = Graph(usersIgniteRDD,relationshipsPureDataRDD, defaultUser)
    //val graph = Graph(users,relationships, defaultUser)

    println(graph.vertices.filter { case (id, (name, pos)) => pos == "prof" }.count)
    println(graph.triplets.count())

  }catch{
    case e:Exception=> {
      e.printStackTrace()
    }
  }finally memorySession.closeSession(true)
}
