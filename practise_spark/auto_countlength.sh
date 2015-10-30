hdfs dfs -rm -f -R /result/

javac -cp /home/laboratory/spark-1.5.1-bin-hadoop2.6/lib/spark-assembly-1.5.1-hadoop2.6.0.jar CountLength.java -d .

jar -cvf  CountLength.jar practise

export HADOOP_CONF_DIR=/home/laboratory/hadoop-2.7.1/etc/hadoop
#spark-submit --master yarn-client --name JavaWordCount --class practise.JavaWordCount --executor-memory 1G --total-executor-cores 2 ./JavaWordCount.jar hdfs://192.168.1.102/input/
spark-submit --master spark://192.168.1.102:7077 --name CountLength --class practise.CountLength --executor-memory 1G --total-executor-cores 2 ./CountLength.jar hdfs://192.168.1.102/input/

#hdfs dfs -mkdir practise
#hdfs dfs -put ./practise/* practise

#spark-class \
#org.apache.spark.deploy.yarn.Client \
#--jar /home/laboratory/spark-1.5.1-bin-hadoop2.6/lib/spark-assembly-1.5.1-hadoop2.6.0.jar  \
#--class practise.JavaWordCount \
#--arg yarn-standalone \
#--arg /input/
