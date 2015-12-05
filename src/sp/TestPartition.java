package sp;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.Iterator;

import moead.MOEAD;
import mop.AMOP;
import mop.CMOP;
import mop.MopData;
import mop.MopDataPop;
import mop.PRNG;

import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.*;
import org.apache.hadoop.mapred.*;
import org.apache.hadoop.mapred.lib.NLineInputFormat;

import problems.AProblem;
import problems.DTLZ2;
import problems.DTLZ1;
import utilities.StringJoin;
import utilities.WrongRemindException;

import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.api.java.function.Function2;
import org.apache.spark.api.java.function.PairFunction;
import org.apache.spark.api.java.function.PairFlatMapFunction;
import scala.Tuple2;


public class TestPartition {

	/**
	 * @param args
	 * @throws IOException
	 * @throws InterruptedException
	 * @throws ClassNotFoundException
	 */
	public static void main(String[] args) throws IOException,ClassNotFoundException, InterruptedException, WrongRemindException {
		SparkConf sparkConf = new SparkConf().setAppName("moead spark");
		JavaSparkContext cxt = new JavaSparkContext(sparkConf);
		List<String> pStr = new ArrayList<String>(2);
		int loopTime = 1;
		int partitionNum = 1;
		//String mopStr = "0.11428571428571428,0.8857142857142857##############0.11428571428571428,0.8857142857142857 0.887231062637861,0.39981830854994366,0.5000410892620885,0.49991391990917067,0.5000191741683344,0.4991579585874673,0.5996237679399219,0.3999667348118806,0.5997632840511493,0.5003875546804097 2.31093750947948,0.29372502635277803 12,13,11,14,10,9,15,16,8,17,7,18,6,5,19,20,4,21,3,2,22,23,1,24,0,25,26,27,28,29 0.2640571000498967!0.18095238095238095,0.819047619047619##############0.18095238095238095,0.819047619047619 0.8188578949309404,0.3998182626107639,0.49936337758784893,0.49991156961140304,0.4999384742262174,0.5000573483488967,0.5996403565892136,0.39996569300690343,0.49924859498100405,0.5003404924509494 1.7387542848788986,0.38463525028036005 19,20,18,21,17,16,22,15,23,24,14,13,25,12,26,27,11,10,28,9,29,30,8,31,7,6,32,5,33,34 0.3147551317333349";
		String mopStr = "111 3.2324#222 4.2214";
		for (int i = 0; i < loopTime; i++) {
			System.out.println("The " + i + "th time!");
			for(int j = 0; j < partitionNum; j ++) {
				pStr.add(mopStr);
			}
			System.out.println("pStr.size = " + pStr.size());
			JavaRDD<String> p = cxt.parallelize(pStr,partitionNum);

			JavaPairRDD<String,String> mopPair = p.mapPartitionsToPair(
													new PairFlatMapFunction<Iterator<String>,String,String>() {
															public Iterable<Tuple2<String,String>> call(Iterator<String> s) throws WrongRemindException{
																System.out.println("enter map part ");
																String[] ss = s.next().toString().split("#");
																List<Tuple2<String,String>> lt = new ArrayList<Tuple2<String,String>>();
																String[] s1 = ss[0].split(" ");
																lt.add(new Tuple2<String,String>(s1[0],s1[1]+PRNG.nextDouble()));
																String[] s2 = ss[1].split(" ");
																lt.add(new Tuple2<String,String>(s2[0],s2[1]+PRNG.nextDouble()));
																return lt;
															}
													}
											);
/*
			List<Tuple2<String, String>> output = mopPair.collect();
			Map<String,String> m1 = new HashMap<String,String>(2);
			Map<String,String> m2 = new HashMap<String,String>(2);
			for(Tuple2<?,?> t : output) {
					System.out.println(t._1() + "##############" + t._2());
					m1.put(t._1().toString(),t._2().toString());
			}
			*/
			
			JavaPairRDD<String,String> mopPop = mopPair.reduceByKey(
														new Function2<String,String,String>() {
																public String call(String s1, String s2) {
																	System.out.println("enterrrrrrr 11111111111111");
																	return s1+"~"+s2;
																}
														}
											);
			System.out.println("after reduceByKey!");
			//output = mopPop.collect();
			/*
			List<Tuple2<String, String>> output = mopPair.collect();
			for(Tuple2<?,?> t : output) {
				System.out.println(t._1() + "#############" + t._2());
				//m2.put(t._1().toString(),t._2().toString());
			}
			
			output = mopPair.collect();
			for(Tuple2<?,?> t : output) {
				System.out.println(t._1() + "#############" + t._2());
				m2.put(t._1().toString(),t._2().toString());
			}

			String m1tmp = null;
			String m2tmp = null;
			for(String key : m1.keySet()) {
				if(m2.containsKey(key)) {
					m2tmp = m2.get(key);
					m1tmp = m1.get(key);
					if(!m1tmp.equals(m2tmp)) System.out.println("key = " + key + ",m1 = " + m1tmp + ",m2 = " + m2tmp);
				}
			}
			*/
		}
	}

}
