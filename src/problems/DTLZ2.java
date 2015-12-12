package problems;


public class DTLZ2 extends AProblem{

	
	private static DTLZ2 instance;
	
	private DTLZ2() {
		genesDimesion = 10;
		objectiveDimesion = 3;
		limit = 2;
		range = new int[genesDimesion][limit];
		for(int i = 0; i < genesDimesion; i ++){
			range[i][0] = 0;
			range[i][1] = 1;
		}
	}

	public void evaluate(double[] genes,double[] objValue) {
		int k = genesDimesion - objectiveDimesion +  1;
		double g = 0;
		for(int i = genesDimesion - k; i < genesDimesion; i ++){
			g += Math.pow(genes[i] - 0.5, 2.0);
		}
		for(int i = 0 ; i  < objectiveDimesion; i ++){
			objValue[i] = 1.0 + g;
			for(int j = 0; j < objectiveDimesion - i - 1; j ++){
				objValue[i] *= Math.cos(0.5 * Math.PI * genes[j]);
			}
			if(i != 0){
				objValue[i] *= Math.sin(0.5 * Math.PI * genes[objectiveDimesion - i - 1]);
			}
		}
	}

	public static DTLZ2 getInstance() {
		if(instance == null)
			instance = new DTLZ2();
		return instance;
	}

}
