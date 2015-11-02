package test;

import mop.PRNG;

public class PRNGTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		for(int i = 0 ;i < 10; i ++)
			System.out.println(PRNG.nextDouble(0,1));
	}

}
