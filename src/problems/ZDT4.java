package problems;


public class ZDT4 extends AProblem{

	
	private static ZDT4 instance;
	
	private ZDT4() {
		genesDimesion = 10;
		objectiveDimesion = 2;
		range = new int[genesDimesion][objectiveDimesion];
		for(int i = 0; i < genesDimesion; i ++){
			if(0 == i){
				range[i][0] = 0;
				range[i][1] = 1;
			}
			range[i][0] = -5;
			range[i][1] = 5;
		}
	}
		

	public void evaluate(double[] genes,double[] objValue) {
		objValue[0]  = genes[0];
		double g = g(genes);
		objValue[1] = g * (1-Math.sqrt(objValue[0]/g));
	}

	private double g(double[] genes) {
		double s = 0;
		if(genesDimesion != genes.length)
			throw new IllegalArgumentException("genes dimesion wrong! check genesDimesion!!!");
		int n = genesDimesion;
		for(int i = 1; i < n; i ++)
			s += Math.pow(genes[i],2) - 10 * Math.cos(4* Math.PI * genes[i]);
		s = 1 + 10 * ( n - 1) + s;
		return s;
	}

	public static ZDT4 getInstance() {
		if(instance == null)
			instance = new ZDT4();
		return instance;
	}

}
