package com.sparkOcr

//Spark Libraries

import org.apache.spark.SparkContext
import org.apache.spark.SparkConf
import java.io.{File, FileInputStream, InputStream, StringWriter}

//Tika Libraries

import org.apache.tika.metadata.Metadata
import org.apache.tika.parser.{AutoDetectParser, Parser, ParseContext}
import org.apache.tika.sax.BodyContentHandler

import scala.util.Try

import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.fs.{Path, FileSystem}

/**
 *Demo project for running OCR on pdf files with Apache Spark.  
 * 
 * @params - a text file with a list of pdf files
 * @return - text files
 * 
 * Master reads list of pds and sends to workers pdf's to be converted to
 * text via OCR.  OCR Library is Tika and will be loaded on each worker 
 * node.
 * 
 * To run open a terminal window and issue:
 * 
 * <YOUR SPARK LIBRARY>/bin/spark-submit \
 *  --master local[4] \ 
 *  --jars "./lib/tika-app-1.13.jar,./lib/jai_imageio-1.1.jar" \
 *   target/scala-2.11/simple-sbt_2.11-0.1.0-SNAPSHOT.jar
 */

object App {
  
  /**
 * extractInfo - calls TIKA, parses and returns contents 
 * 
 * param filePath:  full path file name ie ./src/main/resources/SeagateEngineer.pdf
 * return 3 tuple (String, String, String)
 *        - file extention .pdf
 *        - content - generated text from OCR
 *        - is an exception or not (true or false)
 */

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

/**
 * extractinfo - TRY - can catch exception from TIKO
 * 
 * param filePath:  full path file name ie ./src/main/resources/SeagateEngineer.pdf
 * return 3 tuple (String, String, String)
 *        - file extention .pdf
 *        - content - generated text from OCR
 *        - is an exception or not (true or false)
 */

  def extractinfo(filePath: String): Try[(String, String, Boolean)] = Try(extractInfo(filePath))

 /**
 * extract entry 
 * 
 * param filePath:  full path file name ie ./src/main/resources/SeagateEngineer.pdf
 * return 3 tuple (String, String, String)
 *        - file extention .pdf
 *        - content - generated text from OCR
 *        - is an exception or not (true or false)
 */

  def extract(filePath: String) : (String, String, String) = {
    val (fileext, content, has_exception) = extractinfo(filePath).getOrElse(("", "", true  ))
    (fileext.toString, content, has_exception.toString)
  }
  
  /**
 * Main entry 
 * 
 * param args:  Not used
 * return textfiles (pdf's converted to text) in Resources/save as HDFS files.
 */

  def main(args: Array[String]) {

 // Create Spark Context
    
    val spark_conf = new SparkConf().setAppName("Spark Tika HDFS")
    val sc = new SparkContext(spark_conf)
    
//  Read list of pdf's
    
    val distData = sc.textFile("./src/main/resources/pdfs.txt")
    
 // Run ocr (TIKA) on each file and save to HDFS
    
    distData.map( line => extract(line.trim)).map(x => x._2).saveAsTextFile("./src/main/resources/save")


  }
}
