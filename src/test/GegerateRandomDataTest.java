package test;

import org.apache.commons.math.random.RandomData;
import org.apache.commons.math.random.RandomDataImpl;
import org.apache.commons.math.random.RandomGenerator;

import mop.RanMT;

public class GegerateRandomDataTest {
	public static RandomData randomData;
	public static RandomGenerator randomGenerator;
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		randomGenerator = new RanMT(1);
		randomData = new RandomDataImpl(randomGenerator);
		System.out.println(randomData.nextUniform(0,1));
	}

}
