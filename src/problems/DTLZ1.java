package problems;

public class DTLZ1 extends AProblem{

	
	private static DTLZ1 instance;
	
	private DTLZ1(){
		genesDimesion = 10;
		objectiveDimesion = 3;
		limit = 2;
		range = new int[genesDimesion][limit];
		for(int i = 0; i < genesDimesion; i ++){
			range[i][0] = 0;
			range[i][1] = 1;
		}
	} 
	
	public void evaluate(double[] genes, double[] objValue) {
		int k = genes.length - objValue.length + 1;
		double g = 0.0;
		for(int i = genes.length - k; i < genes.length; i ++){
			g += Math.pow(genes[i] - 0.5, 2.0) - Math.cos(20.0 * Math.PI * (genes[i] - 0.5));
		}
		g = 100 * (k + g);
		for(int i = 0; i < objValue.length; i ++){
			objValue[i] = 0.5 * (1.0 + g);
			for(int j = 0; j < objValue.length - i - 1; j ++){
				objValue[i] *= genes[j];
			}
			if(i != 0){
				objValue[i] *= 1 - genes[objValue.length - i - 1];
			}
		}
	}
	
/*	public void evaluate(double[] genes, double[] objValue) {
		double g = g(genes);
		objValue[0] = (1+g)*genes[0]*genes[1];
		objValue[1] = (1+g)*genes[0]*(1-genes[1]);
		objValue[2] = (1+g)*(1-genes[0]); 
	}

	private double g(double[] genes) {
		double s = 0;
		if(genesDimesion != genes.length)
			throw new IllegalArgumentException("genes dimesion wrong! check genesDimesion!!!");
		int n = genesDimesion;
		for(int i = 2; i < n; i ++)
			s += Math.pow(genes[i]-0.5,2)-Math.cos(20*Math.PI*(genes[i]-0.5));
		s += 100*(n-2)+100*s;
		return s;
	}*/

	public static DTLZ1 getInstance() {
		if(instance == null)
			instance = new DTLZ1();
		return instance;
	}
	
}
