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
		col.add("111111111 " + StringJoin.join(",", mop.idealPoint));
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

	// oh,Oct 30  this function deal with reduce output file transfer to map input file format,I need this function input the whole file and generate mop, then I could use write file function .
	/*
  public void reduceFile2mop(String line) throws WrongRemindException {
    String[] lineSplit = line.split(" ");
    if ("111111111".equals(lineSplit[0])) {
      String[] idealPoint = lineSplit[1].split(",");
      if (idealPoint.length != mop.idealPoint.length) {
        throw new WrongRemindException(
            "idealPoint length isn't match. Data transfer error!");
      }
      for (int i = 0; i < idealPoint.length; i++) {
        mop.idealPoint[i] = Double.parseDouble(idealPoint[i]);
      }
    } else {
      String[] weightStr = lineSplit[0].split(",");
      double[] weight = new double[mop.objectiveDimesion];
      if (weightStr.length != weight.length) {
        throw new WrongRemindException(
            "weight length isn't match. Data transfer error!\n"
                + "weightStr.lenght is : " + weightStr.length
                + "\nweight.length is :" + weight.length);
      }
      for (int i = 0; i < weightStr.length; i++) {
        weight[i] = Double.parseDouble(weightStr[i]);
      }
      mop.weights.add(weight);

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
      mop.chromosomes.add(chromosome);

      String[] neighbourStr = lineSplit[3].split(",");
      int[] neighbour = new int[mop.neighbourSize];
      if (neighbourStr.length != neighbour.length) {
        throw new WrongRemindException(   
            "neighbour length isn't match. Data transfer error!");
      }
      for (int i = 0; i < neighbourStr.length; i++) {
        neighbour[i] = Integer.parseInt(neighbourStr[i]);
      }
      mop.neighbourTable.add(neighbour);
    }
  }   
	*/


	@Override
	public void line2mop(String line) throws WrongRemindException {
		// Oct. 30, A line stands for the pops. So split "_" will lead to mutli lines , and each l in lines stand for a individual
		String[] lines = line.split(DELIMITER);
		for(int l = 0 ; l < lines.length ; l ++){
			String[] lineSplit = lines[l].split(" ");
			if ("111111111".equals(lineSplit[0])) {
				String[] idealPoint = lineSplit[1].split(",");
				if (idealPoint.length != mop.idealPoint.length) {
					throw new WrongRemindException(
						"idealPoint length isn't match. Data transfer error!");
				}
				for (int i = 0; i < idealPoint.length; i++) {
						mop.idealPoint[i] = Double.parseDouble(idealPoint[i]);
				}
		  } else {	

			String[] weightStr = lineSplit[0].split(",");
			double[] weight = new double[mop.objectiveDimesion];
			if (weightStr.length != weight.length) {
				throw new WrongRemindException(
						"weight length isn't match. Data transfer error!\n"
								+ "weightStr.lenght is : " + weightStr.length
								+ "\nweight.length is :" + weight.length);
			}
			for (int i = 0; i < weightStr.length; i++) {
				weight[i] = Double.parseDouble(weightStr[i]);
			}
			mop.weights.add(weight);

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
			mop.chromosomes.add(chromosome);

			String[] neighbourStr = lineSplit[3].split(",");
			int[] neighbour = new int[mop.neighbourSize];
			if (neighbourStr.length != neighbour.length) {
				throw new WrongRemindException(
						"neighbour length isn't match. Data transfer error!");
			}
			for (int i = 0; i < neighbourStr.length; i++) {
				neighbour[i] = Integer.parseInt(neighbourStr[i]);
			}
			mop.neighbourTable.add(neighbour);
		}
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
