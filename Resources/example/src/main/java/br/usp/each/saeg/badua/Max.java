package br.usp.each.saeg.badua;

public class Max {

	public static void main(String[] args) {
		int[] array={1,5,7,8,4,31,5,0};
		int max = max(array,array.length);
	}
	
	public static int max(int array [], int length){
		int i = 0;
		int max = array[++i]; //array[i++];
		while(i < length){
			if(array[i] > max)
				max = array[i];
			i = i + 1;
		}
		return max;
	}
}
