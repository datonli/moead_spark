javac -cp ./ moead/MOEAD.java -d .
jar -cf MOEAD.jar .
java -cp MOEAD.jar:./:/home/laboratory/workspace/moead_parallel/commons-math-2.2.jar:/home/laboratory/workspace/moead_parallel/apache-commons-lang.jar moead.MOEAD
