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

public class ReduceClass extends MapReduceBase implements Reducer<Text, Text, NullWritable, Text> {
	private Text result = new Text();

	public void reduce(Text key, Iterator<Text> values, OutputCollector<NullWritable, Text> output, Reporter reporter)
			throws IOException{
		String value = null;
		String tmp = null;
		double tmpFitnessValue = 1e+5;
		double maxFitnessValue = 1e+5;
		AProblem problem = DTLZ1.getInstance();
		double[] idealPointValue = new double[problem.objectiveDimesion];
		for(int i = 0 ; i < idealPointValue.length; i ++) idealPointValue[i] = 1e+5;
		boolean flag = false;
		while(values.hasNext()){
			tmp = values.next().toString();
			if (!"111111111".equals(key.toString())) {
				if(null == value) value = tmp;
				tmpFitnessValue = line2FitnessValue(tmp);
				if (tmpFitnessValue < maxFitnessValue) {
					maxFitnessValue = tmpFitnessValue;
					value = tmp;
				}
			} else {
				String[] ts = tmp.split("#");
				String[] idealPointValueNew = ts[0].split(",");
				for(int i = 0 ; i < idealPointValueNew.length; i ++) {
					if(idealPointValue[i] > Double.parseDouble(idealPointValueNew[i]) )
									idealPointValue[i] = Double.parseDouble(idealPointValueNew[i]);
				}
				flag = true;
			}
		}
		if(flag) {
			int[] arr = {1,1,1};
			value ="111111111 " +  StringJoin.join(",",idealPointValue) + "#" + StringJoin.join(",",arr);
		}
		NullWritable nullw = null;
		result.set(value);
		output.collect(nullw, result);
	}

	public double line2FitnessValue(String line) {
		String[] lineSplit = line.split(" ");
		String fitnessValueStr = lineSplit[4];
		return Double.parseDouble(fitnessValueStr);
	}
}
