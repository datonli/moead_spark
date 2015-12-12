./auto_serial.sh
sed -i -e 's|DTLZ1|DTLZ2|' moead/MOEAD.java
./auto_serial.sh
sed -i -e 's|DTLZ2|DTLZ3|' moead/MOEAD.java
./auto_serial.sh
sed -i -e 's|DTLZ3|DTLZ4|' moead/MOEAD.java
./auto_serial.sh
sed -i -e 's|DTLZ4|DTLZ1|' moead/MOEAD.java
