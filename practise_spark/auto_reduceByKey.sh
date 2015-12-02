hdfs dfs -rm -f -R /result/

javac -cp /home/laboratory/spark-1.5.1-bin-hadoop2.6/lib/spark-assembly-1.5.1-hadoop2.6.0.jar JavaReduceByKeyTest.java -d .

jar -cvf  JavaReduceByKeyTest.jar practise

export HADOOP_CONF_DIR=/home/laboratory/hadoop-2.7.1/etc/hadoop
#spark-submit --master yarn-client --name JavaReduceByKeyTest --class practise.JavaReduceByKeyTest --executor-memory 1G --total-executor-cores 2 ./JavaReduceByKeyTest.jar hdfs://192.168.1.102/input/
#spark-submit --master spark://master:7077 --name JavaReduceByKeyTest --class practise.JavaReduceByKeyTest --executor-memory 1G --total-executor-cores 2 ./JavaReduceByKeyTest.jar hdfs://master/input/
spark-submit --master yarn-cluster --name JavaReduceByKeyTest --class practise.JavaReduceByKeyTest --executor-memory 1G --total-executor-cores 2 ./JavaReduceByKeyTest.jar hdfs://master/input/

#hdfs dfs -mkdir practise
#hdfs dfs -put ./practise/* practise

#spark-class \
#org.apache.spark.deploy.yarn.Client \
#--jar /home/laboratory/spark-1.5.1-bin-hadoop2.6/lib/spark-assembly-1.5.1-hadoop2.6.0.jar  \
#--class practise.JavaReduceByKeyTest \
#--arg yarn-standalone \
#--arg /input/
