package moead;

import java.io.IOException;
import java.io.FileWriter;


//import mop.IGD;

import mop.AMOP;
import mop.CMOP;
import mop.IGD;

import java.util.List;
import java.util.ArrayList;

import problems.AProblem;
import problems.DTLZ1;
import problems.CDV;

public class MOEAD {

	public static void moead(AMOP mop,int iterations){
    	for(int i = 0 ; i < iterations; i ++){
			mop.updatePop();
       	}
   }
	
	public static void main(String[] args) throws InterruptedException,IOException{
		int popSize = 105;
		int neighbourSize = 20;
		int iterations = 400;
		
		//AProblem problem = CDV.getInstance();
		AProblem problem = DTLZ1.getInstance();
		AMOP mop = CMOP.getInstance(popSize,neighbourSize,problem);
		mop.initial();
        IGD igdOper = new IGD(1500);
        String filename = "/home/laboratory/workspace/TestData/PF_Real/DTLZ1(3).dat";
        try {
            igdOper.ps = igdOper.loadPfront(filename);
        } catch (IOException e) {}
		long startTime = System.currentTimeMillis();
		long igdTime = 0;
		long sleepTime = 0;
		for(int i = 0 ; i < iterations; i ++){
			System.out.println("The " + i + "th iterations");
			mop.updatePop();
			long sleepTimeStart = System.currentTimeMillis();
			Thread.sleep(2500);
			sleepTime += System.currentTimeMillis() - sleepTimeStart;
			long igdStartTime = System.currentTimeMillis();
            List<double[]> real = new ArrayList<double[]>(mop.chromosomes.size());
            for(int j = 0; j < mop.chromosomes.size(); j ++) {
               real.add(mop.chromosomes.get(j).objectiveValue);
            }
            double[] genDisIGD = new double[2];
            genDisIGD[0] = i;
            genDisIGD[1] = igdOper.calcIGD(real);
            igdOper.igd.add(genDisIGD);
			igdTime += System.currentTimeMillis() - igdStartTime ;
		}

            long recordTime = System.currentTimeMillis()-startTime - igdTime;
            System.out.println("Running time is : " + recordTime);
            System.out.println("Sleep time is : " + sleepTime);
            recordTimeFile("/home/laboratory/workspace/moead_parallel/experiments/recordTime.txt","\nDTLZ1 moead serial time : " + recordTime + ",sleepTime is : " + sleepTime);


        filename = "/home/laboratory/workspace/moead_parallel/experiments/DTLZ1/MOEAD_IGD_DTLZ1_3.txt";
        try {
            igdOper.saveIGD(filename);
        } catch (IOException e) {}
		filename = "/home/laboratory/workspace/moead_parallel/experiments/DTLZ1/moead.txt";
		mop.write2File(filename);
		filename = "/home/laboratory/workspace/moead_parallel/experiments/DTLZ1/moead_all.txt";
		mop.write2File(filename);
		System.out.println("done!");
	}

          public static void recordTimeFile(String filename,String str) throws IOException {
               FileWriter writer = new FileWriter(filename,true);
               writer.write(str);
               writer.close();
          }


}
