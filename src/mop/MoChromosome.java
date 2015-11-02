package mop;


import problems.AProblem;


public abstract class MoChromosome {
	
	public static final double EPS = 1.2e-7;

	private static final long serialVersionUID = 1L;
	protected static int objectiveDimesion;
	protected static int genesDimesion;
	protected double[] genes;
	protected double[] objectiveValue;
	protected double fitnessValue;
	protected static int[][] range;

	protected Object clone() throws CloneNotSupportedException {
		throw new CloneNotSupportedException(
				"Chromosome cannot be cloned directly, it must be created from a pool");
	}

	public void copyTo(MoChromosome copyto) {
		copyto.fitnessValue = this.fitnessValue;
		System.arraycopy(objectiveValue, 0, copyto.objectiveValue, 0,
				objectiveValue.length);
		System.arraycopy(genes, 0, copyto.genes, 0,
				genes.length);
	}

	public abstract double parameterDistance(MoChromosome another);

	public static double objectiveDistance(MoChromosome ch1, MoChromosome ch2) {
		double sum = 0;
		for (int i = 0; i < ch1.objectiveValue.length; i++) {
			sum += Math.pow(ch1.objectiveValue[i] - ch2.objectiveValue[i], 2);
		}
		return Math.sqrt(sum);
	}
	
	public abstract void evaluate(AProblem problem);
	
	public abstract void mutate(double mutationrate);
	public abstract void diff_xover(MoChromosome ind0, MoChromosome ind1,MoChromosome ind2);
	public abstract void crossover(MoChromosome ind0, MoChromosome ind1);
	public abstract String vectorString();
	
	public abstract String getParameterString();
}
