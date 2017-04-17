Example code for running TIKA on Apache Spark.  The application will convert PDF's to a text stream and place the content in a "save" directory found in resources.  Please note the directory your Spark application is downloaded to.

This code is a variation of a project recently completed.  It follows from :

     https://github.com/scotthaleen/spark-hdfs-tika/blob/master/src/main/scala/Driver.scala

This repo has stripped out customer customizations from the client application and is for demonstration purposes only.


open a terminal then:

1. $git clone https://github.com/marilynwaldman/sparkOcr


2. change directories to the application

   $cd sparkOcr

3. Enter sbt then compile and package

   $sbt
      eclipse
      compile
      package
      exit

3. On the command line issue the Spark submit command:

   Replace "Users/marilynwaldman/spark" below with the prefix to your Spark library running locally:

   $/Users/marilynwaldman/spark/bin/spark-submit \
      --class com.sparkOcr.App \
     --jars "./lib/tika-app-1.13.jar,./lib/jai_imageio-1.1.jar" \
     ./target/scala-2.11/sparkocr_2.11-0.1-SNAPSHOT.jar



    Note that --jars loads TIKA on all worker nodes

5. You can now import the code into the Eclipse Scala IDE:  http://scala-ide.org.





