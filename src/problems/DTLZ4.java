package problems;


public class DTLZ4 extends AProblem{

	
	private static DTLZ4 instance;
	
	private DTLZ4() {
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
		double alpha = 100.0;
		int k = genesDimesion - objectiveDimesion +  1;
		double g = 0;
		for(int i = genesDimesion - k; i < genesDimesion; i ++){
			g += Math.pow(genes[i] - 0.5, 2.0);
		}
		for(int i = 0 ; i  < objectiveDimesion; i ++){
			objValue[i] = 1.0 + g;
			for(int j = 0; j < objectiveDimesion - i - 1; j ++){
				objValue[j] *= Math.cos(0.5 * Math.PI * Math.pow(genes[j],alpha));
			}
			if(i != 0){
				objValue[i] *= Math.sin(0.5 * Math.PI * Math.pow(genes[objectiveDimesion - i - 1],alpha));
			}
		}
	}

	public static DTLZ4 getInstance() {
		if(instance == null)
			instance = new DTLZ4();
		return instance;
	}

}
