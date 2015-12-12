./auto_spark_moead_part.sh
sed -i -e 's|DTLZ1|DTLZ2|' sp/MoeadSpPartition.java
./auto_spark_moead_part.sh
sed -i -e 's|DTLZ2|DTLZ3|' sp/MoeadSpPartition.java
./auto_spark_moead_part.sh
sed -i -e 's|DTLZ3|DTLZ4|' sp/MoeadSpPartition.java
./auto_spark_moead_part.sh
sed -i -e 's|DTLZ4|DTLZ1|' sp/MoeadSpPartition.java
sed -i -e 's|partitionNum_2|partitionNum_4|' sp/MoeadSpPartition.java
sed -i -e 's|partitionNum = 2|partitionNum = 4|' sp/MoeadSpPartition.java
./auto_spark_moead_part.sh
sed -i -e 's|DTLZ1|DTLZ2|' sp/MoeadSpPartition.java
./auto_spark_moead_part.sh
sed -i -e 's|DTLZ2|DTLZ3|' sp/MoeadSpPartition.java
./auto_spark_moead_part.sh
sed -i -e 's|DTLZ3|DTLZ4|' sp/MoeadSpPartition.java
./auto_spark_moead_part.sh
sed -i -e 's|DTLZ4|DTLZ1|' sp/MoeadSpPartition.java
sed -i -e 's|partitionNum_4|partitionNum_8|' sp/MoeadSpPartition.java
sed -i -e 's|partitionNum = 4|partitionNum = 8|' sp/MoeadSpPartition.java
./auto_spark_moead_part.sh
sed -i -e 's|DTLZ1|DTLZ2|' sp/MoeadSpPartition.java
./auto_spark_moead_part.sh
sed -i -e 's|DTLZ2|DTLZ3|' sp/MoeadSpPartition.java
./auto_spark_moead_part.sh
sed -i -e 's|DTLZ3|DTLZ4|' sp/MoeadSpPartition.java
./auto_spark_moead_part.sh
sed -i -e 's|DTLZ4|DTLZ1|' sp/MoeadSpPartition.java
sed -i -e 's|partitionNum_8|partitionNum_2|' sp/MoeadSpPartition.java
sed -i -e 's|partitionNum = 8|partitionNum = 2|' sp/MoeadSpPartition.java
