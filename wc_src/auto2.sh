hdfs dfs -rm -r /output

javac -cp /home/laboratory/hadoop-2.7.1/share/hadoop/common/hadoop-common-2.7.1.jar:/home/laboratory/hadoop-2.7.1/share/hadoop/mapreduce/hadoop-mapreduce-client-core-2.7.1.jar:/home/laboratory/hadoop-2.7.1/share/hadoop/mapreduce/hadoop-mapreduce-client-common-2.7.1.jar WCMapper.java -d .
javac -cp /home/laboratory/hadoop-2.7.1/share/hadoop/common/hadoop-common-2.7.1.jar:/home/laboratory/hadoop-2.7.1/share/hadoop/mapreduce/hadoop-mapreduce-client-core-2.7.1.jar:/home/laboratory/hadoop-2.7.1/share/hadoop/mapreduce/hadoop-mapreduce-client-common-2.7.1.jar WCReducer.java -d .
javac -cp /home/laboratory/hadoop-2.7.1/share/hadoop/common/hadoop-common-2.7.1.jar:/home/laboratory/hadoop-2.7.1/share/hadoop/mapreduce/hadoop-mapreduce-client-core-2.7.1.jar:/home/laboratory/hadoop-2.7.1/share/hadoop/mapreduce/hadoop-mapreduce-client-common-2.7.1.jar:./ WC.java -d .

jar -cvf wc.jar org

/home/laboratory/hadoop-2.7.1/bin/hadoop jar wc.jar org.apache.hadoop.example.WC /input /output

