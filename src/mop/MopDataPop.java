package mop;

import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import utilities.StringJoin;
import utilities.WrongRemindException;

public class MopDataPop implements DataOperator {

	public AMOP mop;

	String DELIMITER = "_";

	public void setDelimiter(String delimiter) {
		DELIMITER = delimiter;
	}

	public MopDataPop() {
		try {
			mop = (AMOP) CMOP.getInstance();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public MopDataPop(AMOP mop) {
		this.mop = mop;
	}

	public String mop2Str() {
		List<String> col = new ArrayList<String>();
		for (int i = 0; i < mop.chromosomes.size(); i++) {
			col.add(mop2Line(i));
		}
		List<String> cc = new ArrayList<String>(2);
		cc.add(StringJoin.join(",", mop.idealPoint));
		cc.add(StringJoin.join(",", mop.partitionArr));
		col.add("111111111 " + StringJoin.join("#",cc));
		return StringJoin.join("_", col);
	}

	@Override
	public String mop2Line(int i) {
		List<String> col = new ArrayList<String>();
		col.add(StringJoin.join(",", mop.weights.get(i)));
		col.add(StringJoin.join(",", mop.chromosomes.get(i).genes));
		col.add(StringJoin.join(",", mop.chromosomes.get(i).objectiveValue));
		col.add(StringJoin.join(",", mop.neighbourTable.get(i)));
		col.add(String.valueOf(mop.chromosomes.get(i).fitnessValue));
		// add idealPoint for every ind . Dec 4
		col.add(StringJoin.join(",", mop.chromosomes.get(i).idealPoint));
		return StringJoin.join(" ", col);
	}

	public String idealPoint2Line() {
		return StringJoin.join(",", mop.idealPoint);
	}

	public String weight2Line(int i) {
		return StringJoin.join(",", mop.weights.get(i));
	}
	

	public double[] line2ObjValue(String line) {
		String[] lineSplit = line.split(" ");
		String[] objectiveValueStr = lineSplit[2].split(",");
		double[] objectiveValue = new double[objectiveValueStr.length];
		for (int i = 0; i < objectiveValueStr.length; i++) {
			objectiveValue[i] = Double.parseDouble(objectiveValueStr[i]);
		}
		return objectiveValue;
	}

	private List<String> genWeight(int popSize) {
        List<String> weights = new ArrayList<String>(popSize);
        for (int i = 0; i <= popSize; i++) {
            if (mop.objectiveDimesion == 2) {
                double[] weight = new double[2];
                weight[0] = i / (double) popSize;
                weight[1] = (popSize - i) / (double) popSize;
                weights.add(StringJoin.join(",",weight));
            } else if (mop.objectiveDimesion == 3) {
                int parts_num = 0;
                for(int f = 0; f <= popSize/2; f ++){
                    if(popSize == f*(f-1)/2){
                            parts_num = f;
                            break;
                    }
                }
                for (int j = 0; j <= parts_num; j++) {
                    if (i + j <= parts_num) {
                        int k = parts_num - i - j;
                        double[] weight = new double[3];
                        weight[0] = i / (double) parts_num;
                        weight[1] = j / (double) parts_num;
                        weight[2] = k / (double) parts_num;
                		weights.add(StringJoin.join(",",weight));
                    }
                }
            }
        }
		return weights;
	}

	@Override
	public void line2mop(String line) throws WrongRemindException {
		// Oct. 30, A line stands for the pops. So split "_" will lead to mutli lines , and each l in lines stand for a individual
		String[] lines = line.split(DELIMITER);
		List<String> weightList = genWeight(mop.popSize);
		AMOP mopTmp = new CMOP(mop.popSize,mop.objectiveDimesion);
		int[] index = new int[mop.popSize];
		int cnt = 0;
		for(int l = 0 ; l < lines.length ; l ++){
			String[] lineSplit = lines[l].split(" ");
			if ("111111111".equals(lineSplit[0])) {
				String[] ss = lineSplit[1].split("#");
				String[] idealPoint = ss[0].split(",");
				String[] partitionArr = ss[1].split(",");
				if (idealPoint.length != mop.idealPoint.length) {
					throw new WrongRemindException(
						"idealPoint length isn't match. Data transfer error!");
				}
				for (int i = 0; i < idealPoint.length; i++) {
						mop.idealPoint[i] = Double.parseDouble(idealPoint[i]);
				}
				mop.partitionArr = new int[partitionArr.length];
				for (int i = 0; i < partitionArr.length; i++) {
						mop.partitionArr[i] = Integer.parseInt(partitionArr[i]);
				}

		  } else {	
			index[cnt] = weightList.indexOf(lineSplit[0]);
			cnt ++;
			String[] weightStr = lineSplit[0].split(",");
			double[] weight = new double[mop.objectiveDimesion];
			if (weightStr.length != weight.length) {
				throw new WrongRemindException(
						"weight length isn't match. Data transfer error!\n"
								+ "weightStr.length is : " + weightStr.length
								+ "\nweight.length is :" + weight.length);
			}
			for (int i = 0; i < weightStr.length; i++) {
				weight[i] = Double.parseDouble(weightStr[i]);
			}
			mopTmp.weights.add(weight);

			String[] chromosomeStr = lineSplit[1].split(",");
			String[] objectiveValueStr = lineSplit[2].split(",");
			String fitnessValueStr = lineSplit[4];
			MoChromosome chromosome = CMoChromosome.createChromosome();
			if (chromosomeStr.length != chromosome.genes.length) {
				throw new WrongRemindException(
						"chromosome length isn't match. Data transfer error!");
			}
			if (objectiveValueStr.length != chromosome.objectiveValue.length) {
				throw new WrongRemindException(
						"objectiveValue length isn't match. Data transfer error!");
			}
			for (int i = 0; i < chromosomeStr.length; i++) {
				chromosome.genes[i] = Double.parseDouble(chromosomeStr[i]);
			}
			for (int i = 0; i < objectiveValueStr.length; i++) {
				chromosome.objectiveValue[i] = Double
						.parseDouble(objectiveValueStr[i]);
			}
			chromosome.fitnessValue = Double.parseDouble(fitnessValueStr);
			
			String[] idealPointStr = lineSplit[5].split(",");
			for(int j = 0; j < mop.objectiveDimesion; j ++) {
				chromosome.idealPoint[j] = Double.parseDouble(idealPointStr[j]);
			}
			mopTmp.chromosomes.add(chromosome);

			String[] neighbourStr = lineSplit[3].split(",");
			int[] neighbour = new int[mop.neighbourSize];
			if (neighbourStr.length != neighbour.length) {
				throw new WrongRemindException(
						"neighbour length isn't match. Data transfer error!\nright neighbour length is " + neighbour.length + " , and pass length of neighbour is " + neighbourStr.length + " !!!");
			}
			for (int i = 0; i < neighbourStr.length; i++) {
				neighbour[i] = Integer.parseInt(neighbourStr[i]);
			}
			mopTmp.neighbourTable.add(neighbour);
		}
		}
		int j = 0;
		mop.weights.clear();
		mop.chromosomes.clear();
		mop.neighbourTable.clear();
		for(int i = 0 ; i < mop.popSize; i ++) {
			for(j = 0 ; j < mop.popSize; j ++) {
				if(i == index[j])
					break;
			}
			mop.weights.add(mopTmp.weights.get(j));
			mop.chromosomes.add(mopTmp.chromosomes.get(j));
			mop.neighbourTable.add(mopTmp.neighbourTable.get(j));
		}
		for(int i = 0 ;i < mop.popSize ; i ++) {
			//System.out.println("original weights 's " + i + " is : " + weightList.get(i) + "\nmop's weights 's " + i + " is : " + StringJoin.join(" ",mop.weights.get(i)));
		}
	}
	
	public void clear() {
		mop.clearAll();
		mop.allocateAll();
	}

	public boolean write2FileTime(String filename, String str, int writeTime) throws IOException {
			DataOutputStream dataOutputStream = new DataOutputStream(
				new FileOutputStream(filename));
			for(int i = 0;  i < writeTime; i ++) {
				dataOutputStream.writeBytes(str);
				dataOutputStream.writeBytes("\n");
		}
		dataOutputStream.close();
		return true;
	}
	
	public boolean write2File(String filename, String str) throws IOException {
		DataOutputStream dataOutputStream = new DataOutputStream(
				new FileOutputStream(filename));
			dataOutputStream.writeBytes(str);
			dataOutputStream.writeBytes("\n");
		dataOutputStream.close();
		return true;
	}

}
