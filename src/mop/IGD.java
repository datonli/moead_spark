package mop;

import java.io.IOException;

import java.io.*;
import java.util.List;
import java.util.ArrayList;

import mr.HdfsOper;

import utilities.StringJoin;

public class IGD {

	private static String DELIMITER = " ";
	int igdNum;

	public List<double[]> ps;
	public List<double[]> igd; 
	public IGD(int igdNum) {
		igd = new ArrayList<double[]>(igdNum);
		ps = new ArrayList<double[]>(igdNum);
		this.igdNum = igdNum;
	}

	public void setSplit(String split) {
		DELIMITER = split;
	}

	public List<double[]> loadPfrontStr(String str) {
		String[] lines = str.split("\n");
		List<double[]> ps = new ArrayList<double[]>(igdNum);
		String[] ss;
		for(int l = 0; l < lines.length; l ++) {
			ss = lines[l].split(DELIMITER);
			double[] d = new double[ss.length];
			for(int i = 0; i < ss.length; i ++) {
				d[i] = Double.parseDouble(ss[i]);
			}
			ps.add(d);
		}
		return ps;
	}
	
	public double calcDistance(double[] w1,double[] w2) {
		double sum= 0.0;
		for(int i = 0; i < w1.length; i ++) {
			sum += Math.pow((w1[i] - w2[i]), 2.0);
		}
		return Math.sqrt(sum);
	}
	
	public double calcIGD(List<double[]> real) {
		double distanceIGD = 0.0;
		for(int i = 0 ; i < real.size(); i ++) {
			double minDistance = 1.0e+10;
			for(int j = 0; j < ps.size(); j ++) {
				double d = calcDistance(real.get(i),ps.get(j));
				if(d < minDistance) minDistance = d;
			}
			distanceIGD += minDistance;
		}
		distanceIGD /= real.size();
		return distanceIGD;
	}

	/*
    public double calcIGD(List<SOP> sops) {
        double distanceIGD = 0.0;
        for (int i  = 0 ; i < ps.size(); i ++) {
            double minDistance = 1.0e+10;
            for (int j = 0 ; j < sops.size(); j ++) {
            	double d = calcDistance(ps.get(i),sops.get(j).ind.objectiveValue);
            	if(d < minDistance) minDistance = d;
            }
            distanceIGD += minDistance;
        }
        distanceIGD /= sops.size();
        return distanceIGD;
    }
	*/


	public List<double[]> loadPfront(String filename) throws IOException {
		FileReader fr = new FileReader(filename);
		BufferedReader br = new BufferedReader(fr);
		String s ;
		String[] ss;
		List<double[]> ps = new ArrayList<double[]>(igdNum);
		while( null != (s = br.readLine()) ) {
			ss = s.split(DELIMITER);
			double[] d = new double[ss.length];
			for(int i = 0 ; i < ss.length; i ++) {
				d[i] = Double.parseDouble(ss[i]);
			}
			ps.add(d);
		}
		br.close();
		fr.close();
		return ps;
	}

	public String strIGD() {
		StringBuilder str = new StringBuilder();
		for(int n = 0 ; n < igd.size() ;  n ++) {
			str.append(StringJoin.join(DELIMITER,igd.get(n)));
			if(n < igd.size() - 1) str.append("\n");
		}
		return str.toString();
	}

	public void saveIGD(String fileName) throws IOException {
		File file = new File(fileName);
        if(!file.exists()){
            file.createNewFile();
        }
        FileWriter fw = new FileWriter(file.getAbsoluteFile());
        BufferedWriter bw = new BufferedWriter(fw);
		bw.write(strIGD());
        bw.close();
        fw.close();
	}

}
