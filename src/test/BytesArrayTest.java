package test;

import java.io.UnsupportedEncodingException;

public class BytesArrayTest {

	/**
	 * @param args
	 * @throws UnsupportedEncodingException 
	 */
	public static void main(String[] args) throws UnsupportedEncodingException {
		String s = "";
		byte[] sb = s.getBytes("UTF-8");
		 String ss = new String(sb,"UTF-8");
		System.out.println("sb length is : " + sb.length + " ; s is : " + ss);
	}

}
