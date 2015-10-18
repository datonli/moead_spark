javac -cp /home/laboratory/spark-1.5.1-bin-hadoop2.6/lib/spark-assembly-1.5.1-hadoop2.6.0.jar JavaWordCount.java -d .

jar -cvf  JavaWordCount.jar practise

spark-submit --master spark://192.168.1.102:7077 --name JavaWordCount --class practise.JavaWordCount --executor-memory 1G --total-executor-cores 2 ./JavaWordCount.jar hdfs://192.168.1.102/input/

#hdfs dfs -mkdir practise
#hdfs dfs -put ./practise/* practise

#spark-class \
#org.apache.spark.deploy.yarn.Client \
#--jar /home/laboratory/spark-1.5.1-bin-hadoop2.6/lib/spark-assembly-1.5.1-hadoop2.6.0.jar  \
#--class practise.JavaWordCount \
#--arg yarn-standalone \
#--arg /input/
