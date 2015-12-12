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
import mop.MopDataPop;
import mop.IGD;
import mop.MoChromosome;
import mop.CMoChromosome;

import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.*;
import org.apache.hadoop.mapred.*;
import org.apache.hadoop.mapred.lib.NLineInputFormat;

import problems.AProblem;
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


public class MoeadSp {

	/**
	 * @param args
	 * @throws IOException
	 * @throws InterruptedException
	 * @throws ClassNotFoundException
	 */
	public static void main(String[] args) throws IOException,
			ClassNotFoundException, InterruptedException, WrongRemindException {

		int popSize = 105;
		int neighbourSize = 20;
		int iterations = 400;
		int writeTime = 2;
		int innerLoop = 20;
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

		long startTime = System.currentTimeMillis();
		List<String> pStr = new ArrayList<String>();
		List<String> mopList = new ArrayList<String>();

        IGD igdOper = new IGD(1500);
        String filename = "/home/laboratory/workspace/TestData/PF_Real/DTLZ1(3).dat";
        try {
            igdOper.ps = igdOper.loadPfront(filename);
        } catch (IOException e) {}
	
		System.out.println("Timer start!!!");
		long igdTime = 0;
		for (int i = 0; i < loopTime; i++) {
			System.out.println("The " + i + "th time!");
			Thread.sleep(2500);
			pStr.clear();
            long igdStartTime = System.currentTimeMillis();
			mopData.clear();
			mopData.setDelimiter("_");
			mopData.line2mop(mopStr);
			mopData.mop.initPartition(1);
			mopStr = mopData.mop2Str();
            List<double[]> real = new ArrayList<double[]>(mop.chromosomes.size()); 
           	for(int j = 0; j < mop.chromosomes.size(); j ++) {
               real.add(mop.chromosomes.get(j).objectiveValue);
            }
            double[] genDisIGD = new double[2];
            genDisIGD[0] = i*innerLoop;
            genDisIGD[1] = igdOper.calcIGD(real);
            igdOper.igd.add(genDisIGD);
			igdTime += System.currentTimeMillis() - igdStartTime ;

			for(int j = 0; j < writeTime; j ++)
					pStr.add(mopStr);
			JavaRDD<String> p = cxt.parallelize(pStr,writeTime);
			System.out.println("after union");
			JavaPairRDD<String,String> mopPair = p.mapPartitionsToPair(
													new PairFlatMapFunction<Iterator<String>,String,String>() {
															public Iterable<Tuple2<String,String>> call(Iterator<String> s) throws WrongRemindException{
																int popSize = 105;
																int neighbourSize = 20;
																AProblem aProblem = DTLZ1.getInstance();
																AMOP aMop = CMOP.getInstance(popSize, neighbourSize, aProblem);
																MopDataPop mmop = new MopDataPop(aMop);
																mmop.line2mop(s.next());
																MOEAD.moead(mmop.mop,innerLoop);
																List<Tuple2<String,String>> lt = new ArrayList<Tuple2<String,String>>();
																for (int k = 0; k < mmop.mop.chromosomes.size(); k ++) {
																		lt.add(new Tuple2<String,String>(StringJoin.join(",",mmop.mop.weights.get(k)),mmop.mop2Line(k)));
																}
                                                                List<String> cc = new ArrayList<String>(2);
                                                                cc.add(StringJoin.join(",", mmop.mop.idealPoint));
                                                                cc.add(StringJoin.join(",", mmop.mop.partitionArr));
                                                                lt.add(new Tuple2<String,String>("111111111","111111111 " + StringJoin.join("#",cc)));
                                                                mmop.clear();
                                                                return lt;
															}
													}
											);

			JavaPairRDD<String,String> mopPop = mopPair.reduceByKey(
														new Function2<String,String,String>() {
                                                                public String call(String s1, String s2) {
                                                                        String[] s1split = s1.split(" ");
                                                                        String[] s2split = s2.split(" ");
                                                                        if("111111111".equals(s1split[0])) {
                                                                            //System.out.println(s1 + "," + s2);
                                                                            String[] ss1 = s1split[1].split("#");
                                                                            String[] ss2 = s2split[1].split("#");
                                                                            String[] s1_idealPoint = ss1[0].split(",");
                                                                            String[] s2_idealPoint = ss2[0].split(",");
                                                                            for (int i = 0; i < s1_idealPoint.length; i ++) {
                                                                                    if ( Double.parseDouble(s1_idealPoint[i]) > Double.parseDouble(s2_idealPoint[i]) )
                                                                                            s1_idealPoint[i] = s2_idealPoint[i];
                                                                            }
                                                                            return "111111111 " + StringJoin.join(",",s1_idealPoint) + "#" + ss1[1];
                                                                        } else {
                                                                                // Nov 8
                                                                                // order is : weights, chromosomes genes, chromosomes objective value, neighbour table, fitness value, idealPoint

                                                                                AProblem aProblem = DTLZ1.getInstance();

                                                                                String[] weightStr = s1split[0].split(",");
                                                                                double[] weight = new double[weightStr.length];
                                                                                for (int i = 0; i < weightStr.length; i++) {
                                                                                    weight[i] = Double.parseDouble(weightStr[i]);
                                                                                }

                                                                                MoChromosome m1 = CMoChromosome.createChromosome();
                                                                                MoChromosome m2 = CMoChromosome.createChromosome();

                                                                                String[] chromosomeStr = s1split[1].split(",");
                                                                                for (int i = 0; i < chromosomeStr.length; i++) {
                                                                                     m1.genes[i] = Double.parseDouble(chromosomeStr[i]);
                                                                                }
                                                                                chromosomeStr = s2split[1].split(",");
                                                                                for (int i = 0; i < chromosomeStr.length; i++) {
                                                                                     m2.genes[i] = Double.parseDouble(chromosomeStr[i]);
                                                                                }

                                                                                String[] objectiveValueStr = s1split[2].split(",");
                                                                                for (int i = 0; i < objectiveValueStr.length; i++) {
                                                                                    m1.objectiveValue[i] = Double.parseDouble(objectiveValueStr[i]);
                                                                                }
                                                                                objectiveValueStr = s2split[2].split(",");
                                                                                for (int i = 0; i < objectiveValueStr.length; i++) {
                                                                                    m2.objectiveValue[i] = Double.parseDouble(objectiveValueStr[i]);
                                                                                }

                                                                                String[] idealPointStr = s1split[5].split(",");
                                                                                double[] idealPoint = new double[idealPointStr.length];
                                                                                for(int j= 0 ; j < idealPoint.length; j ++) idealPoint[j] = 1e+5;
                                                                                for(int j = 0; j < idealPointStr.length; j ++) {    
                                                                                    double num = Double.parseDouble(idealPointStr[j]);
                                                                                    if(num < idealPoint[j]) idealPoint[j] = num;
                                                                                }
                                                                                idealPointStr = s2split[5].split(",");
                                                                                for(int j = 0; j < idealPointStr.length; j ++) {    
                                                                                    double num = Double.parseDouble(idealPointStr[j]);
                                                                                    if(num < idealPoint[j]) idealPoint[j] = num;
                                                                                }
                                                                                MoChromosome m = update(m1,m2,weight,idealPoint);
                                                                                for(int j = 0 ; j < idealPoint.length; j ++) m.idealPoint[j] = idealPoint[j];
                                                                                return s1split[0] + " " + StringJoin.join(",",m.genes) + " " + StringJoin.join(",",m.objectiveValue) + " " + s1split[3] + " " + m.fitnessValue + " " + StringJoin.join(",",m.idealPoint);
                                                                        }

                                                                }

                                                                private MoChromosome update(MoChromosome m1, MoChromosome m2,double[] weight, double[] idealPoint) {
                                                                    double o = scalarOptimization(weight,idealPoint,m1);                                                   
                                                                    double n = scalarOptimization(weight,idealPoint,m2);                                           
                                                                    if(o < n){                                                                                                 
                                                                        m1.copyTo(m2);                                                                   
                                                                    }                                                                                                          

                                                                    return m1;
                                                                }                                                                                                                  
                                                                                                                       
                                                                private double scalarOptimization(double[] weight ,double[] idealPoint , MoChromosome chrom) {               
                                                                    return techScalarObj(weight,idealPoint, chrom);                                                                            
                                                                }                                                                                                                  
                                                                                                                       
                                                                private double techScalarObj(double[] namda,double[] idealPoint,  MoChromosome chrom) {                                                 
                                                                    double max_fun = -1 * Double.MAX_VALUE;                                                                        
                                                                    for(int n = 0; n < idealPoint.length; n ++){                                                                   
                                                                        double val = Math.abs(chrom.objectiveValue[n] - idealPoint[n]);                                            
                                                                        if(0 == namda[n])                                                                                          
                                                                            val *= 0.00001;                                                                                        
                                                                        else                                                                                                       
                                                                            val *= namda[n];                                                                                       
                                                                        if(val > max_fun)                                                                                          
                                                                            max_fun = val;                                                                                         
                                                                    }                                  
                                                                    chrom.fitnessValue = max_fun;                                                                                  
                                                                    return max_fun;                                                                                                
                                                                } 
                                                        }


															/*
																public String call(String s1, String s2) {

																		String[] s1split = s1.split(" ");
																		String[] s2split = s2.split(" ");
																		if("111111111".equals(s1split[0])) {
                                                                            String[] ss1 = s1split[1].split("#");
                                                                            String[] ss2 = s2split[1].split("#");
                                                                            String[] s1_idealPoint = ss1[0].split(",");
                                                                            String[] s2_idealPoint = ss2[0].split(",");
                                                                            for (int i = 0; i < s1_idealPoint.length; i ++) {
                                                                                    if ( Double.parseDouble(s1_idealPoint[i]) > Double.parseDouble(s2_idealPoint[i]) )
                                                                                            s1_idealPoint[i] = s2_idealPoint[i];
                                                                            }
                                                                            return "111111111 " + StringJoin.join(",",s1_idealPoint) + "#" + ss1[1];
																		} else {
																				String s1_fv = s1split[4];
																				String s2_fv = s2split[4];
																				// Nov 8
																				// s1_fv[4] is fitnessvalue, their order is : weights, chromosomes genes, chromosomes objective value, neighbour table, fitness value, idealPoint
																				if(Double.parseDouble(s1_fv) > Double.parseDouble(s2_fv) )
																								s1 = s2;
																				return s1;
																		}
																}
															*/
											);
			System.out.println("after reduceByKey!");
			List<Tuple2<String, String>> output = mopPop.collect();
			mopList.clear();
			for(Tuple2<?,?> t : output) {
				mopList.add(t._2().toString());
			}
			mopStr = StringJoin.join("_",mopList);
			System.out.println("After map");
			if(i == loopTime -1){
					hdfsOper.mkdir("spark/");
					hdfsOper.createFile("spark/spark_moead.txt", StringJoin.join("\n",mopList));
			}
		}

		System.out.println("Out of loop");
		cxt.stop();
		long recordTime = System.currentTimeMillis()-startTime - igdTime;
		System.out.println("Running time is : " + recordTime);

		mopData.recordTimeFile("/home/laboratory/workspace/moead_parallel/experiments/recordTime.txt","\nMoeadSp ,writeTime_2,recordTime is " + recordTime);

        filename = "/home/laboratory/workspace/moead_parallel/experiments/DTLZ1/writeTime_2_spark_moead_sp.txt";
		mopData.mop.write2File(filename);

	    BufferedReader br = new BufferedReader(hdfsOper.open("spark/spark_moead.txt"));
    	String line = null;
	    String content = null;
    	List<String> col = new ArrayList<String>();
	    while ((line = br.readLine()) != null && line.split(" ").length > 2) {
    	   col.add(StringJoin.join(" ",mopData.line2ObjValue(line)));          
	    }
    	content = StringJoin.join("\n", col);
	    mopData.write2File("/home/laboratory/workspace/moead_parallel/experiments/DTLZ1/writeTime_2_spark_moead_all_sp.txt",content);

        filename = "/home/laboratory/workspace/moead_parallel/experiments/DTLZ1/writeTime_2_MOEAD_SP_IGD_DTLZ2_3.txt";
        try {
            igdOper.saveIGD(filename);
        } catch (IOException e) {}


	}

    
}
