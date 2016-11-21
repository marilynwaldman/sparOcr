




/Users/marilynwaldman/spark/bin/spark-submit \
 --master local[4]   \
   --jars "./lib/tika-app-1.13.jar,./lib/jai_imageio-1.1.jar" \
   target/scala-2.11/simple-sbt_2.11-0.1.0-SNAPSHOT.jar 
