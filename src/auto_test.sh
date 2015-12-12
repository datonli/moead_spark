hdfs dfs -rm -f -R /Spark/

javac -cp /home/laboratory/hadoop-2.7.1/share/hadoop/common/hadoop-common-2.7.1.jar:/home/laboratory/hadoop-2.7.1/share/hadoop/mapreduce/hadoop-mapreduce-client-core-2.7.1.jar:/home/laboratory/hadoop-2.7.1/share/hadoop/mapreduce/hadoop-mapreduce-client-common-2.7.1.jar:/home/laboratory/hadoop-2.7.1/share/hadoop/common/lib/commons-logging-1.1.3.jar:/home/laboratory/workspace/moead_parallel/apache-commons-lang.jar:/home/laboratory/workspace/moead_parallel/commons-math-2.2-sources.jar:./:/home/laboratory/spark-1.5.1-bin-hadoop2.6/lib/spark-assembly-1.5.1-hadoop2.6.0.jar sp/TestPartition.java -d .

jar -cf TestPartition.jar . 

export HADOOP_CONF_DIR=/home/laboratory/hadoop-2.7.1/etc/hadoop
spark-submit --master spark://master:7077 --jars /home/laboratory/spark-1.5.1-bin-hadoop2.6/lib/spark-assembly-1.5.1-hadoop2.6.0.jar --name TestPartition --class sp.TestPartition --executor-memory 1G --total-executor-cores 1 ./TestPartition.jar 

