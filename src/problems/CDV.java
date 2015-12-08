package problems;

public class CDV extends AProblem{

	
	private static CDV instance;
	
	private CDV(){
		genesDimesion = 5;
		objectiveDimesion = 3;
		//objectiveDimesion = 2;
		limit = 2;
		range = new int[genesDimesion][limit];
		for(int i = 0; i < genesDimesion; i ++){
			range[i][0] = 1;
			range[i][1] = 3;
		}
	} 
	
	public void evaluate(double[] genes, double[] objValue) {
		objValue[0] = 1640.2823 + 2.3573285*genes[0] + 2.3220035*genes[1] + 4.5688768*genes[2] + 7.7213633*genes[3] + 4.4559504*genes[4];
		objValue[1] = 6.5856 + 1.15*genes[0] - 1.0427*genes[1] + 0.9738*genes[2] + 0.8364*genes[3] - 0.3695*genes[0]*genes[3] + 0.0861*genes[0]*genes[4] + 0.3628*genes[1]*genes[3] - 0.1106*Math.pow(genes[0],2.0) - 0.3437*Math.pow(genes[2],2.0) + 0.1764*Math.pow(genes[3],2.0);
		objValue[2] = -0.0551 + 0.0181*genes[0] + 0.1024*genes[1] + 0.0421*genes[2] - 0.0073*genes[0]*genes[1] + 0.024*genes[1]*genes[2] - 0.0118*genes[1]*genes[3] - 0.0204*genes[2]*genes[3] - 0.008*genes[2]*genes[4] - 0.0241*Math.pow(genes[1],2.0) + 0.0109*Math.pow(genes[3],2.0);
	}
	
	public static CDV getInstance() {
		if(instance == null)
			instance = new CDV();
		return instance;
	}
	
}
