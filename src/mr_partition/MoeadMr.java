package mr;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import moead.MOEAD;
import mop.AMOP;
import mop.CMOP;
import mop.MopDataPop;
import mop.IGD;

import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.*;
import org.apache.hadoop.mapred.*;
import org.apache.hadoop.mapred.lib.NLineInputFormat;

import problems.AProblem;
import problems.DTLZ2;
import problems.DTLZ1;
import utilities.StringJoin;
import utilities.WrongRemindException;

public class MoeadMr {

	/**
	 * @param args
	 * @throws IOException
	 * @throws InterruptedException
	 * @throws ClassNotFoundException
	 */
	public static void main(String[] args) throws IOException,
			ClassNotFoundException, InterruptedException, WrongRemindException {
		int popSize = 351;
		int neighbourSize = 20;
		int iterations = 600;
		int writeTime = 4;
		int innerLoop = 10;
		int loopTime = iterations / (writeTime * innerLoop);
		AProblem problem = DTLZ1.getInstance();
		AMOP mop = CMOP.getInstance(popSize, neighbourSize, problem);

		mop.initial();
		MopDataPop mopData = new MopDataPop(mop);
		String mopStr = mopData.mop2Str();
		HdfsOper hdfsOper = new HdfsOper();
		hdfsOper.mkdir("moead/");
		for(int i = 0; i < iterations + 1; i ++)
			hdfsOper.rm("moead/" + i + "/");
		hdfsOper.mkdir("moead/0/");
		hdfsOper.createFile("moead/moead.txt", mopStr, writeTime);
		hdfsOper.cp("moead/moead.txt","moead/0/part-00000");

        IGD igdOper = new IGD(1500);
        String filename = "/home/laboratory/workspace/TestData/PF_Real/DTLZ1(3).dat";
        try {
            igdOper.ps = igdOper.loadPfront(filename);
        } catch (IOException e) {}


		long startTime = System.currentTimeMillis();
		long igdTime = 0;
		System.out.println("Timer start!!!");
		for (int i = 0; i < loopTime; i++) {
			System.out.println("The " + i + "th time!");

			//Thread.sleep(2500);
			
			mopData.clear();
			mopData.line2mop(mopStr);
            long igdStartTime = System.currentTimeMillis();
            List<double[]> real = new ArrayList<double[]>(mopData.mop.chromosomes.size()); 
            for(int j = 0; j < mopData.mop.chromosomes.size(); j ++) {
               real.add(mopData.mop.chromosomes.get(j).objectiveValue);
            }
            double[] genDisIGD = new double[2];
            genDisIGD[0] = i*innerLoop;
            genDisIGD[1] = igdOper.calcIGD(real);
            igdOper.igd.add(genDisIGD);   
            igdTime += System.currentTimeMillis() - igdStartTime ;

			JobConf jobConf = new JobConf(MoeadMr.class);
			jobConf.setJobName("moead mapreduce");
			jobConf.setNumMapTasks(writeTime);
			jobConf.setNumReduceTasks(1);

			jobConf.setJarByClass(MoeadMr.class);
			MapClass.setInnerLoop(innerLoop);
			jobConf.setInputFormat(NLineInputFormat.class);
			jobConf.setOutputFormat(TextOutputFormat.class);
			jobConf.setMapperClass(MapClass.class);
			jobConf.setReducerClass(ReduceClass.class);
			jobConf.setOutputKeyClass(Text.class);
			jobConf.setOutputValueClass(Text.class);
			
			FileInputFormat.addInputPath(jobConf,new Path(
					"hdfs://master:8020/user/root/moead/moead.txt"));
			FileOutputFormat.setOutputPath(jobConf,new Path(
					"hdfs://master:8020/user/root/moead/"
					+ (i+1)));
			System.out.println("Run job begin ... ");
			JobClient.runJob(jobConf);
			System.out.println("Run job end ... ");

			// read the output of reduce and write the pop in moead.txt
			mopData.clear();
			mopData.setDelimiter("\n");
			mopData.line2mop(hdfsOper.readWholeFile("moead/"+(i+1)+"/part-00000"));		
			mopStr = mopData.mop2Str();
			hdfsOper.rm("moead/moead.txt");
			hdfsOper.createFile("moead/moead.txt", mopStr, writeTime);
		}
		System.out.println("Running time is : " + (System.currentTimeMillis() - startTime - igdTime));
		BufferedReader br = new BufferedReader(hdfsOper.open("moead/"+(loopTime-1)+"/part-00000"));
		String line = null;
		String content = null;
		List<String> col = new ArrayList<String>();
		while ((line = br.readLine()) != null && line.split(" ").length > 2) {
			col.add(StringJoin.join(" ",mopData.line2ObjValue(line)));
		}
		content = StringJoin.join("\n", col);
		mopData.write2File("/home/laboratory/workspace/moead_parallel/experiments/parallel/mr_moead.txt",content);
		System.out.println("LoopTime is : " + loopTime + "\n");

        filename = "/home/laboratory/workspace/moead_parallel/experiments/MOEAD_MR_IGD_DTLZ1_3.txt";
        try {
            igdOper.saveIGD(filename);
        } catch (IOException e) {}

	}
}
