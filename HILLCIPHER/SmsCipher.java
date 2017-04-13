import java.util.*;

import java.io.*;

public class SmsCipher {

	public static void main(String[] args){

		if(args.length < 2 || args.length > 2){
			errorMsg("usage: <keyfile> <plain-textfile>");
		}

		String sms_key = args[0];
		String sms_plain = args[1];

		String line = null;

		int[][] eMatrix = new int[3][3];
		String answer = "";
		int lineLen;
		String fileError = "";

		try{
			fileError = sms_key;
			FileReader key_fileReader = new FileReader(sms_key);
			BufferedReader key_bufferedReader = new BufferedReader(key_fileReader);

			int y=0;
			while((line = key_bufferedReader.readLine()) != null){
				int j=0;
				for(String v: line.split(" ")){
					if(j > 2 || y > 2){
						errorMsg("Key matrix does not have the dimensions 3x3. Please provide a 3x3 key matrix.");
					}
					try{
						eMatrix[y][j] = Integer.parseInt(v);
					} catch(NumberFormatException e){
						errorMsg("The character or word '"+v+"' is not a number.");
					}
					if(eMatrix[y][j] < 0 || eMatrix[y][j] > 25){ errorMsg("Value '"+v+"' is out of bounds: All values in the matrix must be in range of 0 to 25.");}
					j++;
				}
				if(j < 3){
					errorMsg("Key matrix does not have the dimensions 3x3. Please provide a 3x3 key matrix.");
				}
				y++;
			}
			if(y < 3){
				errorMsg("Key matrix does not have the dimensions 3x3. Please provide a 3x3 key matrix.");
			}

			key_bufferedReader.close();

			fileError = sms_plain;
			FileReader plain_fileReader = new FileReader(sms_plain);
			BufferedReader plain_bufferedReader = new BufferedReader(plain_fileReader);

			line = plain_bufferedReader.readLine();
			lineLen = line.length() / 3;
			for(int z = 0; z < lineLen; z++){
				for(int i = 0; i < 3; i++){
					int spot = 0;
					for(int j = 0; j<3; j++){
						int letter = (line.toLowerCase()).charAt(j + 3*z) - 'a';
						if(letter < 0 || letter > 25){
							errorMsg("Wrong message input. The key '"+(line.toLowerCase()).charAt(j + 3*z)+"' is not an english letter (A-Z)");
						}
						spot += (eMatrix[i][j] * letter);
					}
					answer += Character.toString((char)(spot % 26 + 'A'));
				}
			}


			plain_bufferedReader.close();
			System.out.println("Encrypted message: " + answer);

		} catch(FileNotFoundException ex){
			System.out.println("ERROR: Unable to open file " + fileError);
		} catch(IOException ex){
			System.out.println("Error reading file " + fileError);
		}
	}

	public static void errorMsg(String msg){
			System.out.println("ERROR: " +msg);
			System.out.println("Exiting program...");
			System.exit(1);
	}
}