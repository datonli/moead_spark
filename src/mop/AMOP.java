package mop;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import problems.AProblem;

public abstract class AMOP{
	
	protected static int popSize;
	protected static int neighbourSize;
	protected static int objectiveDimesion ;
	protected static AProblem problem;
	protected static AMOP instance;
	
	public double[] idealPoint;
	public List<double[]> weights;
	public List<int[]> neighbourTable;
	public List<MoChromosome> chromosomes;
	
	public void clearAll(){
		idealPoint = null;
		weights = null;
		neighbourTable = null;
		chromosomes = null;
		
	}
	
	public void allocateAll(){
		idealPoint = new double[objectiveDimesion];
		weights = new ArrayList<double[]>();
		neighbourTable = new ArrayList<int[]>(popSize);
		chromosomes = new ArrayList<MoChromosome>(popSize);
	}
	
	public abstract void initial();
	abstract void generateInitialPop();
	public abstract void updatePop();
	public void setProblem(AProblem problem){
		this.problem = problem;
	}
	public abstract void write2File(String fileName) throws IOException ;

	public static int getPopSize() {
		return popSize;
	}

	public static void setPopSize(int popSize) {
		AMOP.popSize = popSize;
	}

	public static int getNeighbourSize() {
		return neighbourSize;
	}

	public static void setNeighbourSize(int neighbourSize) {
		AMOP.neighbourSize = neighbourSize;
	}

	public static int getObjectiveDimesion() {
		return objectiveDimesion;
	}

	public static void setObjectiveDimesion(int objectiveDimesion) {
		AMOP.objectiveDimesion = objectiveDimesion;
	}


	public double[] getIdealPoint() {
		return idealPoint;
	}

	public void setIdealPoint(double[] idealPoint) {
		this.idealPoint = idealPoint;
	}

	public List<double[]> getWeights() {
		return weights;
	}

	public void setWeights(List<double[]> weights) {
		this.weights = weights;
	}

	public List<int[]> getNeighbourTable() {
		return neighbourTable;
	}

	public void setNeighbourTable(List<int[]> neighbourTable) {
		this.neighbourTable = neighbourTable;
	}

	public List<MoChromosome> getChromosomes() {
		return chromosomes;
	}

	public void setChromosomes(List<MoChromosome> chromosomes) {
		this.chromosomes = chromosomes;
	}

	public static AProblem getProblem() {
		return problem;
	}
	
	
	
}
