./auto_mr_moead_part.sh
sed -i -e 's|DTLZ1|DTLZ2|' mr/MoeadMrPartition.java
sed -i -e 's|DTLZ1|DTLZ2|' mr/MapClass.java
sed -i -e 's|DTLZ1|DTLZ2|' mr/ReduceClass.java
./auto_mr_moead_part.sh
sed -i -e 's|DTLZ2|DTLZ3|' mr/MoeadMrPartition.java
sed -i -e 's|DTLZ2|DTLZ3|' mr/MapClass.java
sed -i -e 's|DTLZ2|DTLZ3|' mr/ReduceClass.java
./auto_mr_moead_part.sh
sed -i -e 's|DTLZ3|DTLZ4|' mr/MoeadMrPartition.java
sed -i -e 's|DTLZ3|DTLZ4|' mr/MapClass.java
sed -i -e 's|DTLZ3|DTLZ4|' mr/ReduceClass.java
./auto_mr_moead_part.sh
sed -i -e 's|DTLZ4|DTLZ1|' mr/MoeadMrPartition.java
sed -i -e 's|DTLZ4|DTLZ1|' mr/MapClass.java
sed -i -e 's|DTLZ4|DTLZ1|' mr/ReduceClass.java
sed -i -e 's|partitionNum_2|partitionNum_4|' mr/MoeadMrPartition.java
sed -i -e 's|partitionNum = 2|partitionNum = 4|' mr/MoeadMrPartition.java
./auto_mr_moead_part.sh
sed -i -e 's|DTLZ1|DTLZ2|' mr/MoeadMrPartition.java
sed -i -e 's|DTLZ1|DTLZ2|' mr/MapClass.java
sed -i -e 's|DTLZ1|DTLZ2|' mr/ReduceClass.java
./auto_mr_moead_part.sh
sed -i -e 's|DTLZ2|DTLZ3|' mr/MoeadMrPartition.java
sed -i -e 's|DTLZ2|DTLZ3|' mr/MapClass.java
sed -i -e 's|DTLZ2|DTLZ3|' mr/ReduceClass.java
./auto_mr_moead_part.sh
sed -i -e 's|DTLZ3|DTLZ4|' mr/MoeadMrPartition.java
sed -i -e 's|DTLZ3|DTLZ4|' mr/MapClass.java
sed -i -e 's|DTLZ3|DTLZ4|' mr/ReduceClass.java
./auto_mr_moead_part.sh
sed -i -e 's|DTLZ4|DTLZ1|' mr/MoeadMrPartition.java
sed -i -e 's|DTLZ4|DTLZ1|' mr/MapClass.java
sed -i -e 's|DTLZ4|DTLZ1|' mr/ReduceClass.java
sed -i -e 's|partitionNum_4|partitionNum_8|' mr/MoeadMrPartition.java
sed -i -e 's|partitionNum = 4|partitionNum = 8|' mr/MoeadMrPartition.java
./auto_mr_moead_part.sh
sed -i -e 's|DTLZ1|DTLZ2|' mr/MoeadMrPartition.java
sed -i -e 's|DTLZ1|DTLZ2|' mr/MapClass.java
sed -i -e 's|DTLZ1|DTLZ2|' mr/ReduceClass.java
./auto_mr_moead_part.sh
sed -i -e 's|DTLZ2|DTLZ3|' mr/MoeadMrPartition.java
sed -i -e 's|DTLZ2|DTLZ3|' mr/MapClass.java
sed -i -e 's|DTLZ2|DTLZ3|' mr/ReduceClass.java
./auto_mr_moead_part.sh
sed -i -e 's|DTLZ3|DTLZ4|' mr/MoeadMrPartition.java
sed -i -e 's|DTLZ3|DTLZ4|' mr/MapClass.java
sed -i -e 's|DTLZ3|DTLZ4|' mr/ReduceClass.java
./auto_mr_moead_part.sh
sed -i -e 's|DTLZ4|DTLZ1|' mr/MoeadMrPartition.java
sed -i -e 's|DTLZ4|DTLZ1|' mr/MapClass.java
sed -i -e 's|DTLZ4|DTLZ1|' mr/ReduceClass.java
sed -i -e 's|partitionNum_8|partitionNum_2|' mr/MoeadMrPartition.java
sed -i -e 's|partitionNum = 8|partitionNum = 2|' mr/MoeadMrPartition.java
