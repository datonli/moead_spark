package sp;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Iterator;

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
import problems.DTLZ1;
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
import org.apache.spark.api.java.function.PairFlatMapFunction;
import scala.Tuple2;


public class MoeadSp {

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
		int iterations = 400;
		int writeTime = 4;
		int innerLoop = 1;
		int loopTime = iterations / (writeTime * innerLoop);
		AProblem problem = DTLZ1.getInstance();
		AMOP mop = CMOP.getInstance(popSize, neighbourSize, problem);

		mop.initial();
		// comment original code for running serial.
		// MOEAD.moead(mop,iterations);
		
		
		// Oct 30  writing another mopData to use as multi pop in one file.
		//MopData mopData = new MopData(mop);
		
		HdfsOper hdfsOper = new HdfsOper();
		hdfsOper.rm("spark/");

		MopDataPop mopData = new MopDataPop(mop);
		String mopStr = mopData.mop2Str();

		SparkConf sparkConf = new SparkConf().setAppName("moead spark");
		JavaSparkContext cxt = new JavaSparkContext(sparkConf);

				//JavaRDD<String> pop = cxt.parallelize(Arrays.asList(mopStr));

		long startTime = System.currentTimeMillis();
		List<String> pStr = new ArrayList<String>();
		List<String> mopList = new ArrayList<String>();
		System.out.println("Timer start!!!");
		for (int i = 0; i < loopTime; i++) {
			System.out.println("The " + i + "th time!");
			Thread.sleep(2500);
			pStr.clear();
			for(int j = 0; j < writeTime; j ++)
					pStr.add(mopStr);
			JavaRDD<String> p = cxt.parallelize(pStr,writeTime);
			System.out.println("after union");
			JavaPairRDD<String,String> mopPair = p.mapPartitionsToPair(
													new PairFlatMapFunction<Iterator<String>,String,String>() {
															public Iterable<Tuple2<String,String>> call(Iterator<String> s) throws WrongRemindException{
																int popSize = 406;
																int neighbourSize = 30;
																AProblem aProblem = DTLZ1.getInstance();
																AMOP aMop = CMOP.getInstance(popSize, neighbourSize, aProblem);
																MopDataPop mmop = new MopDataPop(aMop);
																mmop.line2mop(s.next());
																MOEAD.moead(mmop.mop,innerLoop);
																List<Tuple2<String,String>> lt = new ArrayList<Tuple2<String,String>>();
																for (int k = 0; k < mmop.mop.chromosomes.size(); k ++) {
																		lt.add(new Tuple2<String,String>(StringJoin.join(",",mmop.mop.weights.get(k)),mmop.mop2Line(k)));
																}
																lt.add(new Tuple2<String,String>("111111111","111111111 " + StringJoin.join(",",mmop.mop.idealPoint)));
																mmop.clear();
																return lt;
															}
													}
											);
			List<Tuple2<String, String>> output = mopPair.collect();
			if(i == loopTime-1 )
			for(Tuple2<?,?> t : output)
					System.out.println(t._1() + "##############" + t._2());

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
																				}
																				return "111111111 " + StringJoin.join(",",s1_idealPoint);
																		} else {
																				String s1_fv = s1split[4];
																				String s2_fv = s2split[4];
																				// Nov 8
																				// s1_fv[4] is fitnessvalue, their order is : weights, chromosomes genes, chromosomes objective value, neighbour table, fitness value
																				if(Double.parseDouble(s1_fv) > Double.parseDouble(s2_fv) )
																								s1 = s2;
																				return s1;
																		}
																}
														}
											);
			System.out.println("after reduceByKey!");
			output = mopPop.collect();
			mopList.clear();
			for(Tuple2<?,?> t : output) {
				if(i == loopTime -1 )
					System.out.println(t._1() + "#############" + t._2());
					mopList.add(t._2().toString());
			}
			mopStr = StringJoin.join("_",mopList);

			//JavaRDD<String> mopValue = mopPop.values();
			// Nov. 3  need to add a function let all recoreds merge to one population.
			// and make it cycle

			System.out.println("After map");
			//pop = p;
			if(i == loopTime -1){
					hdfsOper.mkdir("spark/");
					hdfsOper.createFile("spark/spark_moead.txt", StringJoin.join("\n",mopList));
			}
		}

		System.out.println("Out of loop");
		cxt.stop();
		System.out.println("Running time is : " + (System.currentTimeMillis() - startTime));
    BufferedReader br = new BufferedReader(hdfsOper.open("spark/spark_moead.txt"));
    String line = null;
    String content = null;
    List<String> col = new ArrayList<String>();
    while ((line = br.readLine()) != null && line.split(" ").length > 2) {
       col.add(StringJoin.join(" ",mopData.line2ObjValue(line)));          
    }
    content = StringJoin.join("\n", col);
    mopData.write2File("/home/laboratory/workspace/moead_parallel/experiments/parallel/spark_moead.txt",content);
	}

}
