package moead;

import java.io.IOException;

//import mop.IGD;

import mop.AMOP;
import mop.CMOP;
import mop.IGD;

import java.util.List;
import java.util.ArrayList;

import problems.AProblem;
import problems.DTLZ4;
import problems.CDV;

public class MOEAD {

	public static void moead(AMOP mop,int iterations){
    	for(int i = 0 ; i < iterations; i ++){
			mop.updatePop();
       	}
   }
	
	public static void main(String[] args) throws IOException {
		int popSize = 406;
		int neighbourSize = 30;
		int iterations = 400;
		
		//AProblem problem = CDV.getInstance();
		AProblem problem = DTLZ4.getInstance();
		AMOP mop = CMOP.getInstance(popSize,neighbourSize,problem);
		mop.initial();
        IGD igdOper = new IGD(1500);
        String filename = "/home/laboratory/workspace/TestData/PF_Real/DTLZ4(3).dat";
        try {
            igdOper.ps = igdOper.loadPfront(filename);
        } catch (IOException e) {}
		long startTime = System.currentTimeMillis();
		for(int i = 0 ; i < iterations; i ++){
			mop.updatePop();
            List<double[]> real = new ArrayList<double[]>(mop.chromosomes.size());
            for(int j = 0; j < mop.chromosomes.size(); j ++) {
               real.add(mop.chromosomes.get(j).objectiveValue);
            }
            double[] genDisIGD = new double[2];
            genDisIGD[0] = i;
            genDisIGD[1] = igdOper.calcIGD(real);
            igdOper.igd.add(genDisIGD);
		}
        filename = "/home/laboratory/workspace/moead_parallel/experiments/DTLZ4/MOEAD_IGD_DTLZ4_3.txt";
        try {
            igdOper.saveIGD(filename);
        } catch (IOException e) {}
		System.out.println("Running time is : " + (System.currentTimeMillis()-startTime));
		filename = "/home/laboratory/workspace/moead_parallel/experiments/DTLZ4/moead.txt";
		mop.write2File(filename);
		filename = "/home/laboratory/workspace/moead_parallel/experiments/DTLZ4/moead_all.txt";
		mop.write2File(filename);
		System.out.println("done!");
	}
}
