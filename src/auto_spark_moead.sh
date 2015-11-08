hdfs dfs -rm -f -R /Spark/

javac -cp /home/laboratory/hadoop-2.7.1/share/hadoop/common/hadoop-common-2.7.1.jar:/home/laboratory/hadoop-2.7.1/share/hadoop/mapreduce/hadoop-mapreduce-client-core-2.7.1.jar:/home/laboratory/hadoop-2.7.1/share/hadoop/mapreduce/hadoop-mapreduce-client-common-2.7.1.jar:/home/laboratory/hadoop-2.7.1/share/hadoop/common/lib/commons-logging-1.1.3.jar:/home/laboratory/workspace/moead_parallel/apache-commons-lang.jar:/home/laboratory/workspace/moead_parallel/commons-math-2.2-sources.jar:./:/home/laboratory/spark-1.5.1-bin-hadoop2.6/lib/spark-assembly-1.5.1-hadoop2.6.0.jar sp/MoeadSp.java -d .

jar -cvf MoeadSp.jar . 

export HADOOP_CONF_DIR=/home/laboratory/hadoop-2.7.1/etc/hadoop
#spark-submit --master yarn-client --name JavaWordCount --class practise.JavaWordCount --executor-memory 1G --total-executor-cores 2 ./JavaWordCount.jar hdfs://192.168.1.102/input/
spark-submit --master spark://192.168.1.102:7077 --name MoeadSp --class sp.MoeadSp --executor-memory 1G --total-executor-cores 1 ./MoeadSp.jar 

#hdfs dfs -mkdir practise
#hdfs dfs -put ./practise/* practise

#spark-class \
#org.apache.spark.deploy.yarn.Client \
#--jar /home/laboratory/spark-1.5.1-bin-hadoop2.6/lib/spark-assembly-1.5.1-hadoop2.6.0.jar  \
#--class practise.JavaWordCount \
#--arg yarn-standalone \
#--arg /input/
