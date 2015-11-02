package problems;


public class ZDT6 extends AProblem{

	
	private static ZDT6 instance;
	
	private ZDT6() {
		genesDimesion = 10;
		objectiveDimesion = 2;
		range = new int[genesDimesion][objectiveDimesion];
		for(int i = 0; i < genesDimesion; i ++){
			range[i][0] = 0;
			range[i][1] = 1;
		}
	}

	public void evaluate(double[] genes,double[] objValue) {
		objValue[0]  = 1- Math.exp(-4 * genes[0])*Math.pow(Math.sin(6*Math.PI*genes[0]),6);
		double g = g(genes);
		objValue[1] = g*(1-Math.pow(objValue[0]/g,2));
	}

	private double g(double[] genes) {
		double s = 0;
		if(genesDimesion != genes.length)
			throw new IllegalArgumentException("genes dimesion wrong! check genesDimesion!!!");
		int n = genesDimesion;
		for(int i = 1; i < n; i ++)
			s += genes[i];
		s = 1 +  9*Math.pow(s/(n-1),0.25);
		return s;
	}

	public static ZDT6 getInstance() {
		if(instance == null)
			instance = new ZDT6();
		return instance;
	}

}
