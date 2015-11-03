package mr;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import moead.MOEAD;
import mop.AMOP;
import mop.CMOP;
import mop.MopData;
import mop.MopDataPop;

import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.*;
import org.apache.hadoop.mapred.*;
import org.apache.hadoop.mapred.lib.NLineInputFormat;

import problems.AProblem;
import problems.DTLZ2;
import problems.ZDT1;
import utilities.StringJoin;
import utilities.WrongRemindException;

import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.api.java.function.Function2;
import org.apache.spark.api.java.function.PairFunction;
import scala.Tuple2;


public class MoeadMr {

	/**
	 * @param args
	 * @throws IOException
	 * @throws InterruptedException
	 * @throws ClassNotFoundException
	 */
	public static void main(String[] args) throws IOException,
			ClassNotFoundException, InterruptedException, WrongRemindException {

		int popSize = 406;
		int neighbourSize = 30;
		int iterations = 800;
		int writeTime = 4;
		int innerLoop = 20;
		int loopTime = iterations / (writeTime * innerLoop);
		AProblem problem = ZDT1.getInstance();
		AMOP mop = CMOP.getInstance(popSize, neighbourSize, problem);

		mop.initial();
		// comment original code for running serial.
		// MOEAD.moead(mop,iterations);
		
		
		// Oct 30  writing another mopData to use as multi pop in one file.
		//MopData mopData = new MopData(mop);
		
		MopDataPop mopData = new MopDataPop(mop);
		String mopStr = mopData.mop2Str();

		SparkConf sparkConf = new SparkConf().setAppName("moead spark");
		JavaSparkContext cxt = new JavaSparkContext(sparkConf);
		JavaRDD<String> pop = cxt.parallelize(Arrays.asList(mopStr));

		long startTime = System.currentTimeMillis();
		System.out.println("Timer start!!!");
		//for (int i = 0; i < loopTime; i++) {
		for (int i = 0; i < 1; i++) {
			System.out.println("The " + i + "th time!");
			
			JavaRDD<String> p = pop.union(pop);
			for(int j = 0; j < writeTime-2; j ++){
					p = pop.union(p);
			}
			JavaPairRDD<String,String> mopPair = p.mapToPair(
													new PairFunction<String,String,String>() {
															public Tuple2<String,String> call(String s) throws WrongRemindException{
																AProblem aProblem = ZDT1.getInstance();
																AMOP aMop = CMOP.getInstance(popSize, neighbourSize, aProblem);
																MopDataPop mmop = new MopDataPop(aMop);
																mmop.line2mop(s);
																MOEAD.moead(mmop.mop,innerLoop);
																for (int k = 0; k < mmop.mop.chromosomes.size(); k ++) {
																		return new Tuple2<String,String>(StringJoin.join(",",mmop.mop.weights.get(k)),mmop.mop2Line(k));
																}
																return new Tuple2<String,String>("111111111","111111111 " + StringJoin.join(",",mmop.mop.idealPoint));
															}
													}
											);
			
			JavaPairRDD<String,String> mopPop = mopPair.reduceByKey(
														new Function2<String,String,String>() {
																public String call(String s1, String s2) {
																		String[] s1split = s1.split(" ");
																		String[] s2split = s2.split(" ");
																		if("111111111".equals(s1split[0])) {
																				String[] s1_idealPoint = s1split[1].split(",");
																				String[] s2_idealPoint = s2split[1].split(",");
																				for (int i = 0; i < s1_idealPoint.length; i ++) {
																						if ( Double.parseDouble(s1_idealPoint[i]) > Double.parseDouble(s2_idealPoint[i]) )
																										s1_idealPoint[i] = s2_idealPoint[i];
																						return "111111111 " + StringJoin.join(",",s1_idealPoint);
																				}
																		} else {
																				String[] s1_fv = s1split[1].split(",");
																				String[] s2_fv = s2split[1].split(",");
																				if(Double.parseDouble(s1_fv[4]) < Double.parseDouble(s2_fv[4]) )
																								s1 = s2;
																		}
																		return s1;
																}
														}
											);
			//JavaRDD<String> mopValue = mopPop.values();
			// Nov. 3  need to add a function let all recoreds merge to one population.
			// and make it cycle


			System.out.println("After map");
			//pop = p;
		mopPop.saveAsTextFile("/Spark/");
		}
		System.out.println("Out of loop");
		cxt.stop();
		System.out.println("Running time is : " + (System.currentTimeMillis() - startTime));
	}
}
