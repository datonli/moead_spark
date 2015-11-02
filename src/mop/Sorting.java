package mop;

import java.util.Comparator;
import java.util.List;

public class Sorting {

	/**
	 * Sroting the given array in assending order, with the order returned and
	 * the array unchanged.
	 * 
	 * @param tobesorted
	 * @return
	 */
	public static int[] sorting(double[] tobesorted) {
		int[] index = new int[tobesorted.length];
		for (int i = 0; i < index.length; i++)
			index[i] = i;

		for (int i = 1; i < tobesorted.length; i++) {
			for (int j = 0; j < i; j++) {
				if (tobesorted[index[i]] < tobesorted[index[j]]) {
					// insert and break;
					int temp = index[i];
					for (int k = i - 1; k >= j; k--) {
						index[k + 1] = index[k];
					}
					index[j] = temp;
					break;
				}
			}
		}
		return index;
	}

	/**
	 * 
	 * @param <T>
	 * @param tobeSorted
	 * @param comparator
	 * @return
	 */
	public static <T> int[] sorting(T[] tobeSorted, Comparator<T> comparator) {
		int[] index = new int[tobeSorted.length];
		for (int i = 0; i < index.length; i++)
			index[i] = i;
		for (int i = 1; i < tobeSorted.length; i++) {
			for (int j = 0; j < i; j++) {
				if (comparator.compare(tobeSorted[index[i]],
						tobeSorted[index[j]]) < 0) {
					// insert and break;
					int temp = index[i];
					for (int k = i - 1; k >= j; k--) {
						index[k + 1] = index[k];
					}
					index[j] = temp;
					break;
				}
			}
		}

		for (int i = 0; i < index.length; i++) {
			T object1 = tobeSorted[index[i]];
			for (int j = i + 1; j < tobeSorted.length; j++) {
				T object2 = tobeSorted[index[j]];
				assert comparator.compare(object1, object2) <= 0;
			}
		}
		return index;
	}
	
	public static <T> int[] sorting(List<T> tobeSorted, Comparator<T> comparator) {
		int[] index = new int[tobeSorted.size()];
		for (int i = 0; i < index.length; i++)
			index[i] = i;
		for (int i = 1; i < tobeSorted.size(); i++) {
			for (int j = 0; j < i; j++) {
				if (comparator.compare(tobeSorted.get(index[i]), tobeSorted
						.get(index[j])) < 0) {
					// insert and break;
					int temp = index[i];
					for (int k = i - 1; k >= j; k--) {
						index[k + 1] = index[k];
					}
					index[j] = temp;
					break;
				}
			}
		}

		for (int i = 0; i < index.length; i++) {
			T object1 = tobeSorted.get(index[i]);
			for (int j = i + 1; j < tobeSorted.size(); j++) {
				T object2 = tobeSorted.get(index[j]);
				assert comparator.compare(object1, object2) <= 0;
			}
		}
		return index;
	}
}
