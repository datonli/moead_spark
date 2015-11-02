package problems;


public class ZDT3 extends AProblem{

	
	private static ZDT3 instance;
	
	private ZDT3() {
		genesDimesion = 30;
		objectiveDimesion = 2;
		range = new int[genesDimesion][objectiveDimesion];
		for(int i = 0; i < genesDimesion; i ++){
			range[i][0] = 0;
			range[i][1] = 1;
		}
	}

	public void evaluate(double[] genes,double[] objValue) {
		objValue[0]  = genes[0];
		double g = g(genes);
		objValue[1] = g*(1-Math.sqrt(objValue[0]/g)-Math.sin(10*Math.PI*objValue[0])*objValue[0]/g);
	}

	private double g(double[] genes) {
		double s = 0;
		if(genesDimesion != genes.length)
			throw new IllegalArgumentException("genes dimesion wrong! check genesDimesion!!!");
		int n = genesDimesion;
		for(int i = 1; i < n; i ++)
			s += genes[i];
		s = 1 +  9*s/(n-1);
		return s;
	}

	public static ZDT3 getInstance() {
		if(instance == null)
			instance = new ZDT3();
		return instance;
	}

}
