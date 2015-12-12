package mr;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Iterator;

import problems.DTLZ1;
import problems.AProblem;

import org.apache.hadoop.io.*;
import org.apache.hadoop.mapred.*;

import utilities.StringJoin;

import mop.MoChromosome;
import mop.CMoChromosome;

public class ReduceClass extends MapReduceBase implements Reducer<Text, Text, NullWritable, Text> {
	private Text result = new Text();

	public void reduce(Text key, Iterator<Text> values, OutputCollector<NullWritable, Text> output, Reporter reporter)
			throws IOException{
		String value = null;
		String tmp = null;
		AProblem problem = DTLZ1.getInstance();
		double[] idealPoint = new double[problem.objectiveDimesion];
		double[] weight = null;
		String neighbourTableStr = null;
		List<MoChromosome> chromosomes = new ArrayList<MoChromosome>(10);
		for(int i = 0 ; i < idealPoint.length; i ++) idealPoint[i] = 1e+5;
		boolean flag = false;
		while(values.hasNext()){
			tmp = values.next().toString();
			if (!"111111111".equals(key.toString())) {
				String[] ss = tmp.split(" ");
				String[] weightStr = ss[0].split(",");	
				weight = new double[weightStr.length];
				for (int i = 0; i < weightStr.length; i++)  weight[i] = Double.parseDouble(weightStr[i]);
				MoChromosome m = CMoChromosome.createChromosome();
				String[] chromosomeStr = ss[1].split(",");
				for (int i = 0; i < chromosomeStr.length; i++) m.genes[i] = Double.parseDouble(chromosomeStr[i]);
				String[] objectiveValueStr = ss[2].split(",");
				for (int i = 0; i < objectiveValueStr.length; i++) m.objectiveValue[i] = Double.parseDouble(objectiveValueStr[i]);
				neighbourTableStr = ss[3];
				String[] idealPointStr = ss[5].split(",");
				for(int j = 0; j < idealPointStr.length; j ++) {
					double num = Double.parseDouble(idealPointStr[j]);
					if(num < idealPoint[j]) idealPoint[j] = num;
				}
				chromosomes.add(m);
			} else {
				String[] ss = tmp.split("#");
				String[] tmpIdealPoint = ss[0].split(",");
				for(int j = 0 ;j < idealPoint.length; j ++) {
					double num = Double.parseDouble(tmpIdealPoint[j]);
					if(num < idealPoint[j]) idealPoint[j] = num;
				}
				tmp = ss[1];
				flag = true;
			}
		}
		if(flag) {
			value = "111111111 " + StringJoin.join(",",idealPoint) + "#" + tmp;
		} else {
			MoChromosome m = chromosomes.get(0);
			for(int i = 1; i < chromosomes.size(); i ++) {
				m = update(m,chromosomes.get(i),weight,idealPoint);
			}
			for(int j = 0 ; j < idealPoint.length; j ++) m.idealPoint[j] = idealPoint[j];
			value = StringJoin.join(",",weight) + " " + StringJoin.join(",",m.genes) + " " + StringJoin.join(",",m.objectiveValue) + " " + neighbourTableStr + " " + m.fitnessValue + " " + StringJoin.join(",",m.idealPoint);
		}
		NullWritable nullw = null;
		result.set(value); 
		output.collect(nullw, result); 
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


	public double line2FitnessValue(String line) {
		String[] lineSplit = line.split(" ");
		String fitnessValueStr = lineSplit[4];
		return Double.parseDouble(fitnessValueStr);
	}
}
