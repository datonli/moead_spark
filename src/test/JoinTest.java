package test;

import org.apache.commons.lang.StringUtils;

import utilities.StringJoin;

public class JoinTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Integer[] t = new Integer[]{1,3,4};
//		String[] t = {"TEst","dd"};
		System.out.println(StringJoin.join(",",t));
		System.out.println(StringUtils.join(t,","));
	}

}
