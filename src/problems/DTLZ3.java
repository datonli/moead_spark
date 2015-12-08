package problems;

public class DTLZ3 extends AProblem{

	
	private static DTLZ3 instance;
	
	private DTLZ3(){
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
		double g = 0 ;
		int k = genesDimesion - objectiveDimesion + 1;
		for(int i = genesDimesion - k; i < genesDimesion; i ++) {
			g += Math.pow(genes[i] - 0.5, 2.0) - Math.cos(20.0 * Math.PI * (genes[i] - 0.5));
		}
		g = 100.0 * (k + g);
		for(int i = 0 ; i < objectiveDimesion; i ++) {
			objValue[i] = 1.0 + g;
			for(int j = 0; j < objectiveDimesion - i - 1; j ++) {
				objValue[i] *= Math.cos(0.5 * Math.PI * genes[j]);
			}
			 
			if(i != 0) {
				objValue[i] *= Math.sin(0.5 * Math.PI * genes[objectiveDimesion - i - 1]);
			}
		}
	}

	public static DTLZ3 getInstance() {
		if(instance == null)
			instance = new DTLZ3();
		return instance;
	}
	
}
