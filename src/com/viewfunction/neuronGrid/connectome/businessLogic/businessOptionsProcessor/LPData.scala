package com.viewfunction.neuronGrid.connectome.businessLogic.businessOptionsProcessor

import java.text.SimpleDateFormat

class LPData(private val dataContent:String) extends Serializable {
  //ListingId,期数,还款状态,应还本金,应还利息,剩余本金,剩余利息,到期日期,还款日期,recorddate
  private val contentData=dataContent.split(",")
  val listingId=contentData(0)
  val 期数:Int=contentData(1).toInt
  val 还款状态:String={
     contentData(2) match{
       case "0"=> "未还款"
       case "1"=>"已正常还款"
       case "2"=>"已逾期还款"
       case "3"=>"已提前还清该标全部欠款"
       case "4"=>"已部分还款"
     }
  }
  val 应还本金=contentData(3).toDouble
  val 应还利息=contentData(4).toDouble
  val 剩余本金=contentData(5).toDouble
  val 剩余利息=contentData(6).toDouble

  private val dateFormat = new SimpleDateFormat("yyyy-MM-dd")
  val 到期日期= dateFormat.parse(contentData(7))
  val 还款日期={
    contentData(8) match{
      case "\\N"=> null
      case _ =>dateFormat.parse(contentData(8))
    }
  }
  val recordDate=dateFormat.parse(contentData(9))
}
