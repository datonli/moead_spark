package test;

import java.util.ArrayList;
import java.util.List;

public class AddNullList {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		List<String> l = new ArrayList<String>();
		l.add("Str");
		l.add(null);
		l.add("tee");
		for(int i = 0; i < l.size(); i ++)
			System.out.println(l.get(i));
	}

}
