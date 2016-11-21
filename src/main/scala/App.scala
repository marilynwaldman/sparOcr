package com.example.simplesbt

import org.apache.spark.SparkContext
import org.apache.spark.SparkConf
import java.io.{File, FileInputStream, InputStream, StringWriter}

//Tika

import org.apache.tika.metadata.Metadata
import org.apache.tika.parser.{AutoDetectParser, Parser, ParseContext}
import org.apache.tika.sax.BodyContentHandler

import scala.util.Try

import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.fs.{Path, FileSystem}


object App {

  def extractInfo(fp:String): (String, String, Boolean) = {
    val ext = fp.substring(fp.lastIndexOf(".") + 1).toLowerCase
    val p:Parser = new AutoDetectParser()
    var fs = FileSystem.get(new Configuration())
    val inputPath:Path = new Path(fp)
    val is:InputStream = fs.open(inputPath)
    val handler:BodyContentHandler = new BodyContentHandler(-1)
    p.parse(is, handler, new Metadata(), new ParseContext())
    (ext, handler.toString,  false)
  }

  def extractinfo(filePath: String): Try[(String, String, Boolean)] = Try(extractInfo(filePath))

  def extract(filePath: String) : (String, String, String) = {
    val (fileext, content, has_exception) = extractinfo(filePath).getOrElse(("", "", true  ))
    (fileext.toString, content, has_exception.toString)
  }

  def main(args: Array[String]) {

    val spark_conf = new SparkConf().setAppName("Spark Tika HDFS")
    val sc = new SparkContext(spark_conf)

    val distData = sc.textFile("./src/main/resources/pdfs.txt")
    distData.map( line => extract(line.trim)).map(x => x._2).saveAsTextFile("./src/main/resources/save")


  }
}
