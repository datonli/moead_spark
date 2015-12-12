./auto_spark_moead.sh
sed -i -e 's|DTLZ1|DTLZ2|' sp/MoeadSp.java
./auto_spark_moead.sh
sed -i -e 's|DTLZ2|DTLZ3|' sp/MoeadSp.java
./auto_spark_moead.sh
sed -i -e 's|DTLZ3|DTLZ4|' sp/MoeadSp.java
./auto_spark_moead.sh
sed -i -e 's|DTLZ4|DTLZ1|' sp/MoeadSp.java
sed -i -e 's|writeTime_2|writeTime_4|' sp/MoeadSp.java
sed -i -e 's|writeTime = 2|writeTime = 4|' sp/MoeadSp.java
./auto_spark_moead.sh
sed -i -e 's|DTLZ1|DTLZ2|' sp/MoeadSp.java
./auto_spark_moead.sh
sed -i -e 's|DTLZ2|DTLZ3|' sp/MoeadSp.java
./auto_spark_moead.sh
sed -i -e 's|DTLZ3|DTLZ4|' sp/MoeadSp.java
./auto_spark_moead.sh
sed -i -e 's|DTLZ4|DTLZ1|' sp/MoeadSp.java
sed -i -e 's|writeTime_4|writeTime_8|' sp/MoeadSp.java
sed -i -e 's|writeTime = 4|writeTime = 8|' sp/MoeadSp.java
./auto_spark_moead.sh
sed -i -e 's|DTLZ1|DTLZ2|' sp/MoeadSp.java
./auto_spark_moead.sh
sed -i -e 's|DTLZ2|DTLZ3|' sp/MoeadSp.java
./auto_spark_moead.sh
sed -i -e 's|DTLZ3|DTLZ4|' sp/MoeadSp.java
./auto_spark_moead.sh
sed -i -e 's|DTLZ4|DTLZ1|' sp/MoeadSp.java
sed -i -e 's|writeTime_8|writeTime_2|' sp/MoeadSp.java
sed -i -e 's|writeTime = 8|writeTime = 2|' sp/MoeadSp.java
