/**
*Compile: javac -classpath .:Jama.jar HillKeys.java
*Execute: java -classpath .:Jama.jar HillKeys <radix> <blocksize> <keyfile> <invkeyfile>
**/

import java.util.*;
import java.io.*;
import Jama.Matrix;
import static java.lang.Math.*;

public class HillKeys {

	public static void main(String[] args){

		if(args.length < 4 || args.length > 4){
			errorMsg("usage: <radix> <blocksize> <keyfile> <invkeyfile>");
		}
		int radix = 0;
		int blocksize = 0;
		try{
			radix = Integer.parseInt(args[0]);
			blocksize = Integer.parseInt(args[1]);
		} catch(NumberFormatException e){
			errorMsg("Your input <radix> or <blocksize> is not an Integer.");
		}
		if(radix < 2 || radix > 256) errorMsg("Max value on radix is 256 and min value is 2");
		if(blocksize < 2 || blocksize > 8) errorMsg("Max value on blocksize is 8 and min value is 2");
		String keyfile = args[2];
		String invkeyfile = args[3];
		if(keyfile.equals(invkeyfile)) errorMsg("Keyfile and InverseKeyFile can't have the same name.");
		String fileError = "";

		double[][] eMatrix = new double[blocksize][blocksize];
		double[][] dMatrix = new double[blocksize][blocksize];
		double det = 0;
		double invDet = -1;

		while(invDet == -1){
			eMatrix = genMatrix(blocksize, radix);
		    Matrix A = new Matrix(eMatrix);
	      	det = (double)Math.round(A.det());

	      	if(det == 0) continue;
	      	invDet = inverseDet(det, radix);
	      	if(invDet == -1) continue;

			Matrix B = A.inverse();
	      	dMatrix = B.getArray();
		}

		for(int i = 0; i < blocksize; i++){
			for(int j = 0; j < blocksize; j++){
				dMatrix[i][j] = mod((Math.round(dMatrix[i][j] * det * invDet)), radix);
			}
		}

		try{
			fileError = keyfile;
			FileWriter writeKey = new FileWriter(keyfile);
			String line = "";
			for(int i = 0; i < blocksize; i++){
				line = "";
				for(int j = 0; j < blocksize; j++){
					line += (int)eMatrix[i][j] + " ";
				}
				writeKey.write(line + "\n");
			}
			writeKey.close();
			fileError = invkeyfile;
			FileWriter writeInvkey = new FileWriter(invkeyfile);
			for(int i = 0; i < blocksize; i++){
				line = "";
				for(int j = 0; j < blocksize; j++){
					line += (int)dMatrix[i][j] + " ";
				}
				writeInvkey.write(line + "\n");
			}
			writeInvkey.close();

		}catch(IOException ex){
			System.out.println("ERROR: Something went wrong while reading or writing to " + fileError);
		}
		System.out.println("Key matrix (saved in file "+keyfile+") and inverse key matrix (saved in file "+invkeyfile+") have been generated successfully");
	}

	/**
	*Prints out a specified message and exits the program.
	*/
	public static void errorMsg(String msg){
			System.out.println("ERROR: " +msg);
			System.out.println("Exiting program...");
			System.exit(1);
	}

	/**
	*Generates a random matrix with specified square size and limit
	*/
 	public static double [][] genMatrix(int size, int radix){
 		double [][] matrix = new double [size][size];
 		Random rand = new Random();
		for(int i = 0; i < size; i++){
			for(int j = 0; j < size; j++){
				matrix[i][j] = rand.nextInt(9999999) % radix;
			}
		}
 		return matrix;
	}

	/**
	*Get inverse of determenant.
	*Return -1 if the inverse is not found.
	*/
	public static int inverseDet(double det, int radix){
		for(int i = 0; i < radix; i++){
			if((mod((det * i),  radix)) == 1){
				return i;
			}
		}
		return -1;
	}
	/**
	*A modified version off modulos which can take calculate positive and negative value.
	*/
	public static double mod(double val, int radix){
		if(val >= 0){
			val = val % radix;
		} else{
			val = (radix - (abs(val) % radix)) % radix;
		}
		return  val;
	}
}