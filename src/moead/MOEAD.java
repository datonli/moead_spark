package moead;

import java.io.IOException;

//import mop.IGD;

import mop.AMOP;
import mop.CMOP;
import problems.AProblem;
import problems.DTLZ1;
import problems.DTLZ2;

public class MOEAD {

/*	
	public static void moead(AMOP mop,int iterations){
        int innerTime = 1;
        //initial IGD calc Nov 19
        IGD igdOper = new IGD(1500);
        String filename = "/home/laboratory/workspace/TestData/PF_Real/DTLZ1(3).dat";                    
        try {
            igdOper.ps = igdOper.loadPfront(filename);
        } catch (IOException e) {
        }

		for(int i = 0 ; i < iterations; i ++) {
			mop.updatePop();
            double[] genDisIGD = new double[2];
            genDisIGD[0] = gen;
            //genDisIGD[1] = igdOper.calcIGD(mop.sops);
            igdOper.igd.add(genDisIGD);
		}
        filename = "/home/laboratory/workspace/moead_parallel/experiments/MOEAD_IGD_DTLZ1_3.txt";
        try {
            igdOper.saveIGD(filename);  
        } catch (IOException e) {
        }

	}
*/
	
	public static void moead(AMOP mop,int iterations){
		for(int i = 0 ; i < iterations; i ++) {
			mop.updatePop();
		}
	}
	
	public static void main(String[] args) throws IOException {
		int popSize = 406;
		int neighbourSize = 30;
		int iterations = 800;
		
		AProblem problem = DTLZ1.getInstance();
		AMOP mop = CMOP.getInstance(popSize,neighbourSize,problem);
		mop.initial();
		long startTime = System.currentTimeMillis();
		for(int i = 0 ; i < iterations; i ++)
			mop.updatePop();
		System.out.println("Running time is : " + (System.currentTimeMillis()-startTime));
		String filename = "/home/laboratory/workspace/moead_parallel/experiments/moead_new.txt";
		mop.write2File(filename);
		System.out.println("done!");
	}
}
