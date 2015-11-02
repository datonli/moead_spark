package utilities;

import java.util.Collection;

public class StringJoin {

	public static <T> String join(String delimiter,Collection<T> col){
		StringBuilder str = new StringBuilder();
		int count = 0;
		for(T c : col){
			if(0 == count)
				str.append(c);
			else{
				str.append(delimiter);
				str.append(c);
			}
			count ++;
		}
		return str.toString();
	}
	
	public static <T>  String join(String delimiter,T[] arr){
		StringBuilder str = new StringBuilder();
		for(int i = 0; i < arr.length; i ++){
			if(0 == i){
				str.append(arr[0]);
			}
			else{
				str.append(delimiter);
				str.append(arr[i]);
			}
		}
		return str.toString();
	}
	
	public static String join(String delimiter,int[] arr){
		StringBuilder str = new StringBuilder();
		for(int i = 0; i < arr.length; i ++){
			if(0 == i){
				str.append(arr[0]);
			}
			else{
				str.append(delimiter);
				str.append(arr[i]);
			}
		}
		return str.toString();
	}
	
	public static String join(String delimiter,double[] arr){
		StringBuilder str = new StringBuilder();
		for(int i = 0; i < arr.length; i ++){
			if(0 == i){
				str.append(arr[0]);
			}
			else{
				str.append(delimiter);
				str.append(arr[i]);
			}
		}
		return str.toString();
	}
}
