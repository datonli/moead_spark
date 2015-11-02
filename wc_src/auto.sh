
hadoop dfs -rmr output

javac -cp /home/laboratory/hadoop-1.2.1/hadoop-core-1.2.1.jar:/home/laboratory/hadoop-1.2.1/lib/commons-cli-1.2.jar IntSumReducer.java -d .
javac -cp /home/laboratory/hadoop-1.2.1/hadoop-core-1.2.1.jar:/home/laboratory/hadoop-1.2.1/lib/commons-cli-1.2.jar TokenizerMapper.java -d .
javac -cp /home/laboratory/hadoop-1.2.1/hadoop-core-1.2.1.jar:/home/laboratory/hadoop-1.2.1/lib/commons-cli-1.2.jar:./ WordCount.java -d .

jar -cvf WordCount.jar org

hadoop jar WordCount.jar org.apache.hadoop.examples.WordCount input output

hadoop dfs -ls output

