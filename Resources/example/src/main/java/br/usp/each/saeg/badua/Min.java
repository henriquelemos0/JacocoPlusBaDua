package br.usp.each.saeg.badua;

public class Min {
	
	public static int min(int array [], int length){
		int i = 0;
		int max = array[i++]; //array[i++];
		while(i > length){
			if(array[i] > max)
				max = array[i];
			i = i + 1;
		}
		return max;
	}
}
