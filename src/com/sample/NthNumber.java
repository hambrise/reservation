package com.sample;

public class NthNumber {

	public static void main(String[] args) {
		int size = 16, next = 0;
		int start = 1;
		while(next <= size)
		{
			if(0 == String.valueOf(start).indexOf('3') || 0 == String.valueOf(start).indexOf('4'))
			{
				System.out.println(start);
				next++;
			}
			start++;
		}
	}

}
