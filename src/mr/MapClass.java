package mr;

import java.io.IOException;

import moead.MOEAD;
import mop.MopData;
import mop.MopDataPop;
import org.apache.hadoop.io.*;
import org.apache.hadoop.mapred.*;

import utilities.WrongRemindException;

public class MapClass extends MapReduceBase implements Mapper<Object, Text, Text, Text> {

	private static int innerLoop = 1;
	
	static MopDataPop mopData = new MopDataPop();
	
	Text weightVector = new Text();
	Text indivInfo = new Text();

	public static void setInnerLoop(int innerLoopTime){
		innerLoop = innerLoopTime;
	}
	
	public void map(Object key, Text value, OutputCollector<Text, Text> output, Reporter reporter) 
			throws IOException{
		String paragraph = value.toString();
		//System.out.println("paragraph is " + paragraph);
		//MopDataPop mopData = new MopDataPop();
		System.out.println("map begin ... ");
		mopData.clear();
		try {
			mopData.line2mop(paragraph);
			
//			running moead algorithm
			//MOEAD.moead(mopData.mop,1);
			MOEAD.moead(mopData.mop,innerLoop);
			
			weightVector.set("111111111");
			indivInfo.set(mopData.idealPoint2Line());
			output.collect(weightVector, indivInfo);

			for (int i = 0; i < mopData.mop.chromosomes.size(); i++) {
				weightVector.set(mopData.weight2Line(i));
				indivInfo.set(mopData.mop2Line(i));
				output.collect(weightVector, indivInfo);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
