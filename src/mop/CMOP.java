package mop;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import problems.AProblem;
import utilities.WrongRemindException;

public class CMOP extends AMOP {

	private final double F = 0.5;
	private final double CR = 1;
	
	private CMOP(int popSize,int neighbourSize,AProblem problem){
		this.popSize = popSize;
		this.neighbourSize = neighbourSize;
		this.objectiveDimesion = AProblem.objectiveDimesion;
		this.problem = problem;
		allocateAll();
	}
	
	public CMOP(int popSize,int objectiveDimesion) {
		this.popSize = popSize;
		this.objectiveDimesion = objectiveDimesion;
		allocateAll();
	}

	public static AMOP getInstance(int popSize,int neighbourSize,AProblem problem){
		if(null == instance)
			instance = new CMOP(popSize,neighbourSize,problem);
		return instance;
	}
	
	
	public static AMOP getInstance() throws Exception{
		return new CMOP(popSize,neighbourSize,problem);
	}
	
    public List<int[]> indexRangePartition(double p,int partitionNum) {
        List<int[]> partitions = new ArrayList<int[]>(partitionNum);
        List<Integer> partList = new ArrayList<Integer>(popSize/partitionNum);
        for(int i = 0; i < partitionNum; i ++) {
            int cnt = i ;
            partList.clear();
            while(cnt < popSize) {
                partList.add(new Integer(cnt));
                cnt += partitionNum;
            }
            int[] arr = new int[partList.size()];
            for(int j = 0 ; j < partList.size(); j ++) arr[j] = partList.get(j).intValue();
            partitions.add(arr);
        }
        return partitions;
    }

	/*
    public List<int[]> indexRangePartition(double p,int partitionNum) {
        int increase =(int)(p*popSize);
        if( 1 == partitionNum ) increase = 0;
        int part = (int)(popSize/partitionNum);
        List<int[]> partitions = new ArrayList<int[]>(partitionNum);
        int index = 0 ;
        for(int i = 0; i < partitionNum; i ++) {
            if(0 == i) ;
            else if(partitionNum - 1 == i) index = popSize - (part + increase);                                                                                                                                    
            else index -= increase/2;
            int[] arr = new int[part + increase];                                                                                                                                                                  
            for(int j = 0; j < part + increase; j ++) {
                    arr[j] = index;
                    index ++;
            }
            partitions.add(arr);
        }
        return partitions;
    }
	*/
    public void initPartition(int partitionNum) {
        partitions.clear();
        partitions = indexRangePartition(0.05,partitionNum);
    }

    public void setPartitionArr(int i) {
        partitionArr = partitions.get(i);
        //System.out.println("partition's " + i + " is : " + StringJoin.join(" ",partitionArr));
    }

	
	@Override
	public void initial() {
		idealPoint = new double[objectiveDimesion];
		for(int i = 0; i < objectiveDimesion; i ++)
			idealPoint[i] = 1.0e+30;
		
		initWeight();
		initNeighbour();
		generateInitialPop();
		evaluateAfterInitial();

		partitions = indexRangePartition(0.05,1);
		partitionArr = partitions.get(0);        
	}


	private void evaluateAfterInitial() {
		for(int i = 0 ;i < chromosomes.size(); i ++){
			chromosomes.get(i).evaluate(problem);
		}
		
	}


	private void initNeighbour() {
		neighbourTable = new ArrayList<int[]>(popSize);

		double[][] distancematrix = new double[popSize][popSize];
		for (int i = 0; i < popSize; i++) {
			distancematrix[i][i] = 0;
			for (int j = i + 1; j < popSize; j++) {
				distancematrix[i][j] = distance(weights.get(i), weights.get(j));
				distancematrix[j][i] = distancematrix[i][j];
			}
		}

		for (int i = 0; i < popSize; i++) {
			int[] index = Sorting.sorting(distancematrix[i]);
			int[] array = new int[neighbourSize];
			System.arraycopy(index, 0, array, 0, neighbourSize);
			neighbourTable.add(array);
		}
	}

	private static double distance(double[] weight1, double[] weight2) {
		double sum = 0;
		for (int i = 0; i < weight1.length; i++) {
			sum += Math.pow((weight1[i] - weight2[i]), 2);
		}
		return Math.sqrt(sum);
	}

	private void initWeight() {
		weights = new ArrayList<double[]>();
		for (int i = 0; i <= popSize; i++) {
			if (objectiveDimesion == 2) {
				double[] weight = new double[2];
				weight[0] = i / (double) popSize;
				weight[1] = (popSize - i) / (double) popSize;
				weights.add(weight);
			} else if (objectiveDimesion == 3) {
				int parts_num = 0;
				for(int f = 0; f <= popSize/2; f ++){
					if(popSize == f*(f-1)/2){
							parts_num = f;
							break;
					}
				}
				for (int j = 0; j <= parts_num; j++) {
					if (i + j <= parts_num) {
						int k = parts_num - i - j;
						double[] weight = new double[3];
						weight[0] = i / (double) parts_num;
						weight[1] = j / (double) parts_num;
						weight[2] = k / (double) parts_num;
						weights.add(weight);
					}
				}
			}
		}
	}


	@Override
	void generateInitialPop() {
		chromosomes = new ArrayList<MoChromosome>(popSize);
		for(int i = 0; i < popSize; i ++)
		{
			chromosomes.add(CMoChromosome.createChromosome());
		}
	}

	private MoChromosome reproductionByCrossoverMutate(int i){
		int k, l;
		do
			k = neighbourTable.get(i)[PRNG.nextInt(0,
					neighbourSize - 1)];
		while (k == i);
		do
			l = neighbourTable.get(i)[PRNG.nextInt(0,
					neighbourSize - 1)];
		while (l == k || l == i);
		
		CMoChromosome chromosome1 =(CMoChromosome)chromosomes
				.get(k);
		CMoChromosome chromosome2 = (CMoChromosome)chromosomes
				.get(l);
		
		MoChromosome offSpring = new CMoChromosome();
		offSpring.crossover((MoChromosome)chromosome1,(MoChromosome)chromosome2);
		offSpring.mutate(1d / offSpring.genesDimesion);
		return offSpring;
	}
	
	private MoChromosome diffReproduction(int i){
		int k, l, m;
		do{
			k = neighbourTable.get(i)[PRNG.nextInt(0,
					neighbourSize - 1)];
		}
		while (k == i);
		do{
			l = neighbourTable.get(i)[PRNG.nextInt(0,
					neighbourSize - 1)];
		}
		while (l == k || l == i);
		do{
			m = neighbourTable.get(i)[PRNG.nextInt(0,
					neighbourSize - 1)];
		}
		while (m == l || m == k || m == i);

		MoChromosome chromosome1 = (MoChromosome)chromosomes
				.get(k);
		MoChromosome chromosome2 = (MoChromosome)chromosomes
				.get(l);
		MoChromosome chromosome3 = (MoChromosome)chromosomes
				.get(m);

		// generic operation crossover and mutation.
		MoChromosome offSpring = (MoChromosome)CMoChromosome.getEmptyChromosome();
		offSpring.diff_xover(chromosome1,chromosome2,chromosome3);
		offSpring.mutate(1d / offSpring.genesDimesion);
//		offSpring.mutate(1);
		return offSpring;
	}
	
	private MoChromosome reproduction(int i) {
		int k, l, m;
		do
			k = neighbourTable.get(i)[PRNG.nextInt(0,
					neighbourSize - 1)];
		while (k == i);
		do
			l = neighbourTable.get(i)[PRNG.nextInt(0,
					neighbourSize - 1)];
		while (l == k || l == i);
		do
			m = neighbourTable.get(i)[PRNG.nextInt(0,
					neighbourSize - 1)];
		while (m == l || m == k || m == i);

		CMoChromosome chromosome1 = (CMoChromosome)chromosomes
				.get(k);
		CMoChromosome chromosome2 = (CMoChromosome)chromosomes
				.get(l);
		CMoChromosome chromosome3 = (CMoChromosome)chromosomes
				.get(m);

		// generic operation crossover and mutation.
		MoChromosome offSpring = (MoChromosome)CMoChromosome.createChromosome();
		MoChromosome current = (MoChromosome)chromosomes.get(i);
		int D = offSpring.genesDimesion;
		double jrandom = Math.floor(Math.random() * D);

		for (int index = 0; index < D; index++) {
			double value = 0;
			if (Math.random() < CR || index == jrandom)
				value = chromosome1.genes[index]
						+ F
						* (chromosome2.genes[index] - chromosome3.genes[index]);
			else
				value = current.genes[index];
			// REPAIR.

			double high = 1;
			double low = 0;
			if (value > high)
				value = high;
			else if (value < low)
				value = low;

			offSpring.genes[index] = value;
		}

		offSpring.mutate(1d / offSpring.genesDimesion);
//		offSpring.mutate(1d / popSize);
		return offSpring;
	}

	
	
	
	@Override
	public void updatePop() {
		for(int i = 0 ;i < partitionArr.length; i ++){
			evolveNewInd(partitionArr[i]);
		}
		updateIdealPoint();
	}

	private void updateIdealPoint() {
		for(int i = 0 ;i < chromosomes.size(); i ++) {
			for(int j = 0 ; j < objectiveDimesion ; j  ++ ) {
				chromosomes.get(i).idealPoint[j] = idealPoint[j];
			}
		}
	}

	private void evolveNewInd(int i) {
		MoChromosome offSpring = diffReproduction(i);
		improve(i,offSpring);
		offSpring.evaluate(problem);
		updateReference(offSpring);
		updateNeighbours(i,offSpring);
	}

	private void updateReference(MoChromosome offSpring){
		for(int j = 0 ; j < offSpring.objectiveDimesion; j ++){
			if(offSpring.objectiveValue[j] < idealPoint[j]){
				idealPoint[j] = offSpring.objectiveValue[j];
			}
		}
	}
	
	private void updateNeighbours(int i, MoChromosome offSpring) {
		for(int j = 0 ; j < neighbourSize; j ++){
			int neighbourIndex = neighbourTable.get(i)[j];
			MoChromosome neighbourSolution = chromosomes.get(neighbourIndex);
			double o = scalarOptimization(neighbourIndex,offSpring);
			double n = scalarOptimization(neighbourIndex,neighbourSolution);
			if(o < n){
				offSpring.copyTo(neighbourSolution);
			}
		}
	}
	
	private double scalarOptimization(int subproblemIndex, MoChromosome chrom) {
		double[] namda = weights.get(subproblemIndex);
		
		return techScalarObj(namda, chrom);
	}

	private double techScalarObj(double[] namda, MoChromosome chrom) {
		double max_fun = -1 * Double.MAX_VALUE;
		for(int n = 0; n < objectiveDimesion; n ++){
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


	private void improve(int i,MoChromosome offSpring) {
//		do nothing
	}
	
	
	public void write2File(String fileName) throws IOException{
		File file = new File(fileName);
		if(!file.exists()){
			file.createNewFile();
		}
		FileWriter fw = new FileWriter(file.getAbsoluteFile());
		BufferedWriter bw = new BufferedWriter(fw);
		for(int n = 0 ; n < popSize; n ++){
			StringBuffer sb = new StringBuffer();
			for(int od = 0; od < objectiveDimesion; od ++){
				if(0 != od)
					sb.append(" ");
				sb.append(chromosomes.get(n).objectiveValue[od]);
			}
			if(n != popSize)
				sb.append("\n");
			bw.write(sb.toString());
		}
		bw.close();
		fw.close();
	}

}
