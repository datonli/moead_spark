

package practise;

import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.api.java.function.Function2;
import org.apache.spark.api.java.function.PairFunction;
import scala.Tuple2;
import java.util.List;

public class CountLength{
	
	public static void main(String[] args) {
		
			SparkConf sparkConf = new SparkConf().setAppName("Count length of txt file");
			JavaSparkContext ctx = new JavaSparkContext(sparkConf);
			JavaRDD<String> lines = ctx.textFile(args[0]);
			lines.cache();

			JavaRDD<Integer> lineLengths = lines.map(s->s.length());
			int totalLengths = lineLengths.reduce((a,b)->a+b);
			
			
			JavaRDD<Integer> lineCount = lines.map(
								new Function<String,Integer>() {
									public Integer call(String s){
										return 1;
									}
								}
							);
			int totalLines = lineCount.reduce(
								new Function2<Integer,Integer,Integer>(){
									public Integer call(Integer i1,Integer i2){
										return i1+i2;
									}
								}
							);

			
			// duplicate the lines 
			//lines = lines.union(lines);

			JavaPairRDD<String,Integer> lineKeyValue = lines.mapToPair(
								new PairFunction<String,String,Integer>() {
									public Tuple2<String,Integer> call(String s){
										return new Tuple2<String,Integer>(s,1);
									}
								}
							);

			
			
			JavaPairRDD<String,Integer> lineNumCount = lineKeyValue.reduceByKey(
								new Function2<Integer,Integer,Integer>(){
									public Integer call(Integer i1,Integer i2){
										return i1+i2;
									}
								}
							);

			System.out.println("totalLengths is : " + totalLengths);
			System.out.println("totalLines is : " + totalLines);
			List<Tuple2<String,Integer>> output = lineNumCount.collect();
			for (Tuple2<?,?> t : output) {
				System.out.println(t._1() +  " : " + t._2());
			}
	}
	

}
