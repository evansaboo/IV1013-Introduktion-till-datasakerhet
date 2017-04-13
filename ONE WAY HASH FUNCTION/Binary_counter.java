import java.util.*;

import java.io.*;

public class Binary_counter {
	public static void main(String[] args){
		String t1= "c189c8e4aae86bcb152d70a41b1303d4b624cda1cdf4b71143c89d1fad6a1b97";
		String t2= "0d624fd2a0592068408adc696762d033ad144b318c5f4748efd11e0d4bae8dbf";
		int count = 0;

		for (int i = 0; i < t1.length(); i++)
		{
		long a = Character.digit(t1.charAt(i), 16);
		long b = Character.digit(t2.charAt(i), 16);
		for(int j = 0; j < 4; j++){
		  if((0x01&(a>>j)) == (0x01&(b>>j))){
		  	count++;
		  }
		}
	}
		System.out.println(count);
	}
}//7+9