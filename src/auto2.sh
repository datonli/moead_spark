javac -cp /home/laboratory/hadoop-2.7.1/share/hadoop/common/hadoop-common-2.7.1.jar:/home/laboratory/hadoop-2.7.1/share/hadoop/mapreduce/hadoop-mapreduce-client-core-2.7.1.jar:/home/laboratory/hadoop-2.7.1/share/hadoop/mapreduce/hadoop-mapreduce-client-common-2.7.1.jar:/home/laboratory/hadoop-2.7.1/share/hadoop/common/lib/commons-logging-1.1.3.jar:/home/laboratory/workspace/moead_parallel/apache-commons-lang.jar:/home/laboratory/workspace/moead_parallel/commons-math-2.2-sources.jar:./ mr/MoeadMr.java -d .

#javac -cp /home/laboratory/hadoop-2.7.1/share/hadoop/common/hadoop-common-2.7.1.jar:/home/laboratory/hadoop-2.7.1/share/hadoop/mapreduce/hadoop-mapreduce-client-core-2.7.1.jar:/home/laboratory/hadoop-2.7.1/share/hadoop/mapreduce/hadoop-mapreduce-client-common-2.7.1.jar utilities/StringJoin.java -d .

#javac -cp /home/laboratory/hadoop-2.7.1/share/hadoop/common/hadoop-common-2.7.1.jar:/home/laboratory/hadoop-2.7.1/share/hadoop/mapreduce/hadoop-mapreduce-client-core-2.7.1.jar:/home/laboratory/hadoop-2.7.1/share/hadoop/mapreduce/hadoop-mapreduce-client-common-2.7.1.jar:/home/laboratory/workspace/moead_parallel/commons-math-2.2-sources.jar:./ mop/CMoChromosome.java -d .

#javac -cp /home/laboratory/hadoop-2.7.1/share/hadoop/common/hadoop-common-2.7.1.jar:/home/laboratory/hadoop-2.7.1/share/hadoop/mapreduce/hadoop-mapreduce-client-core-2.7.1.jar:/home/laboratory/hadoop-2.7.1/share/hadoop/mapreduce/hadoop-mapreduce-client-common-2.7.1.jar mop/Sorting.java -d .

#javac -cp /home/laboratory/hadoop-2.7.1/share/hadoop/common/hadoop-common-2.7.1.jar:/home/laboratory/hadoop-2.7.1/share/hadoop/mapreduce/hadoop-mapreduce-client-core-2.7.1.jar:/home/laboratory/hadoop-2.7.1/share/hadoop/mapreduce/hadoop-mapreduce-client-common-2.7.1.jar:/home/laboratory/hadoop-2.7.1/share/hadoop/common/lib/commons-logging-1.1.3.jar:/home/laboratory/workspace/moead_parallel/apache-commons-lang.jar:/home/laboratory/workspace/moead_parallel/commons-math-2.2-sources.jar:./ mr/MyFileRecordReader2.java -d .

jar -cvf MoeadMr.jar .

hadoop jar MoeadMr.jar mr.MoeadMr 
