package problems;

public abstract class  AProblem {
	
	public static int genesDimesion;
	public static int objectiveDimesion;
	public static int limit;
	public static int[][] range;
	public abstract void evaluate(double[] genes,double[] objValue);
}
