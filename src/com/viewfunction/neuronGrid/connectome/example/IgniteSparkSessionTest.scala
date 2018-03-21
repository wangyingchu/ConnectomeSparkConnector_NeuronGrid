package com.viewfunction.neuronGrid.connectome.example

import com.viewfunction.neuronGrid.connectome.businessLogic.businessOptionsProcessor.LPData
import com.viewfunction.neuronGrid.connectome.memorySpark.IgniteSparkSession
import org.apache.ignite.lang.IgniteUuid
import org.apache.ignite.spark.IgniteRDD
import org.apache.log4j.{Level, Logger}
import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

object IgniteSparkSessionTest extends App{
  val sparkConf = new SparkConf().setMaster("local").setAppName("My App")
  val sparkContext = new SparkContext(sparkConf)

  // Adjust the logger to exclude the logs of no interest.
  Logger.getRootLogger.setLevel(Level.ERROR)
  Logger.getLogger("org.apache.ignite").setLevel(Level.ERROR)

  val memorySession=new IgniteSparkSession(sparkContext)
  try{

    val igniteRDD1=memorySession.getPairRDD[String, Int]("igniteRDD1",classOf[String],classOf[Int])
    val dataRDD:RDD[(String,Int)]=sparkContext.parallelize(1 to 100000, 10).map(i => ("k-"+i, i))
    igniteRDD1.savePairs(dataRDD)
    igniteRDD1.clear()


    val igniteRDD2: IgniteRDD[String, LPData]=memorySession.getPairRDD[String, LPData]("LP_DATASTORE",classOf[String],classOf[LPData])
    println(igniteRDD2.count())
    igniteRDD2.sample(true,0.01,500).take(100).foreach(k => {
      println(k._1+"->"+k._2.剩余本金+" "+k._2.剩余利息+" "+k._2.到期日期.toString)
    })


    val igniteRDD3:IgniteRDD[Any,Any]=memorySession.getPairRDD("igniteRDD2")
    //val dataRDD2:RDD[(Any,Any)]=sparkContext.parallelize(1 to 10000, 10).map(i => ("k-"+i, "v"+i))
    //igniteRDD3.savePairs(dataRDD2)
    igniteRDD3.sample(true,0.01,500).take(100).foreach(k => {
      println(k._1+"->"+k._2)
    })


    val igniteRDD4:IgniteRDD[IgniteUuid,Int]=memorySession.getValueRDD("igniteRDD3",classOf[Int])
    //val dataRDD3:RDD[Int]=sparkContext.parallelize(1 to 10000, 10)
    //igniteRDD4.saveValues(dataRDD3)
    igniteRDD4.sample(true,0.01,500).take(100).foreach(k => {
      println(k._1.localId()+"->"+k._2)
    })


    val igniteRDD5:IgniteRDD[IgniteUuid,Any]=memorySession.getValueRDD("igniteRDD4")
    //val dataRDD4:RDD[Any]=sparkContext.parallelize(1 to 10000, 10)
    //igniteRDD5.saveValues(dataRDD4)
    igniteRDD5.sample(true,0.01,500).take(100).foreach(k => {
      println(k._1.localId()+"->"+k._2)
    })

  }catch{
    case e:Exception=> {
      e.printStackTrace()
    }
  }finally memorySession.closeSession(true)
}
