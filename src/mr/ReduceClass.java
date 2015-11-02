package mr;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Iterator;

import org.apache.hadoop.io.*;
import org.apache.hadoop.mapred.*;

import utilities.StringJoin;

public class ReduceClass extends MapReduceBase implements Reducer<Text, Text, NullWritable, Text> {
	private Text result = new Text();

	public void reduce(Text key, Iterator<Text> values, OutputCollector<NullWritable, Text> output, Reporter reporter)
			throws IOException{
		String value = null;
		String tmp = null;
		double tmpFitnessValue = 10;
		double maxFitnessValue = 10;
		while(values.hasNext()){
			tmp = values.next().toString();
			if (!"111111111".equals(key.toString())) {
				tmpFitnessValue = line2FitnessValue(tmp);
				if (tmpFitnessValue < maxFitnessValue) {
					maxFitnessValue = tmpFitnessValue;
					value = tmp;
				}
			} else {
				List<String> col = new ArrayList<String>();
				col.add("111111111");
				col.add(tmp);
				value = StringJoin.join(" ",col);
			}
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
