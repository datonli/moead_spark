javac -cp /home/laboratory/hadoop-2.7.1/share/hadoop/common/hadoop-common-2.7.1.jar:/home/laboratory/hadoop-2.7.1/share/hadoop/mapreduce/hadoop-mapreduce-client-core-2.7.1.jar:/home/laboratory/hadoop-2.7.1/share/hadoop/mapreduce/hadoop-mapreduce-client-common-2.7.1.jar:/home/laboratory/hadoop-2.7.1/share/hadoop/common/lib/commons-logging-1.1.3.jar:/home/laboratory/workspace/moead_parallel/apache-commons-lang.jar:/home/laboratory/workspace/moead_parallel/commons-math-2.2-sources.jar:./ ./mr/HdfsOper.java -d .
jar -cvf HdfsOper.jar .
hadoop jar HdfsOper.jar mr.HdfsOper
