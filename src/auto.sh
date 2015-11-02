javac -cp /home/laboratory/hadoop-1.2.1/hadoop-core-1.2.1.jar:/home/laboratory/hadoop-1.2.1/lib/commons-cli-1.2.jar:../apache-commons-lang.jar:/home/laboratory/hadoop-1.2.1/lib/commons-logging-1.1.1.jar:../commons-math-2.2.jar:./ ./mr/MoeadMr.java -d .

javac -cp /home/laboratory/hadoop-1.2.1/hadoop-core-1.2.1.jar:/home/laboratory/hadoop-1.2.1/lib/commons-cli-1.2.jar:../apache-commons-lang.jar:/home/laboratory/hadoop-1.2.1/lib/commons-logging-1.1.1.jar:../commons-math-2.2.jar:./ mop/CMoChromosome.java -d .

javac -cp /home/laboratory/hadoop-1.2.1/hadoop-core-1.2.1.jar:/home/laboratory/hadoop-1.2.1/lib/commons-cli-1.2.jar:../apache-commons-lang.jar:/home/laboratory/hadoop-1.2.1/lib/commons-logging-1.1.1.jar:../commons-math-2.2.jar:./ mop/Sorting.java -d .

jar -cvf MoeadMr.jar .


hadoop jar MoeadMr.jar mr.MoeadMr 

