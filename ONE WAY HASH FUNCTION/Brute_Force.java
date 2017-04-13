import java.util.*;
import java.io.*;
import java.security.*;

public class Brute_Force {
	private static boolean cracked = false;
	private static byte[] crackedMd;
	private static int tries = 0;

	public static void main(String[] args){
		if(args.length < 1 || args.length > 2){
			System.out.println("usage: <characters>");
			System.exit(1);
		}
		String digestAlgorithm = "SHA-256";
		String message = args[0];
		String crackedMsg;
		String textEncoding = "UTF-8";
		boolean cracked = false;


		try{
			MessageDigest mdMsg = MessageDigest.getInstance(digestAlgorithm);

			byte[] inputBytes = message.getBytes(textEncoding);
			mdMsg.update(inputBytes);
			byte[] digestMsg = mdMsg.digest();

			printDigest(message, mdMsg.getAlgorithm(), digestMsg);

			StringBuilder  msg = new StringBuilder(" ");

			msg = recurciveMd(mdMsg, msg, 0, digestMsg);
			System.out.println("");
			System.out.println("Collision Detected! Only 24 bits of the given hash could be matched..");
			printDigest(msg.toString(), mdMsg.getAlgorithm(), crackedMd);
			System.out.println("It took " + tries  + " tries until a collision was found.");


		} catch (NoSuchAlgorithmException e) {
			System.out.println("Algorithm \"" + digestAlgorithm  + "\" is not available");
		} catch (Exception e) {
			System.out.println("Exception "+e);
		}
	}
	/**
	*Prints out digest, hash function and plain text
	*/
	public static void printDigest(String inputText, String algorithm, byte[] digest) {   
		System.out.println("Digest for the message \"" + inputText +"\", using " + algorithm + " is:");       
		for (int i=0; i<digest.length; i++)           
			System.out.format("%02x", digest[i]&0xff);     
		System.out.println();   
	} 

    /**
    *A recursive method which finds the least 24 bits matching the hash we want to get a collision.
    */
    public static StringBuilder recurciveMd(MessageDigest mdMsg, StringBuilder msg, int pos, byte[] testMsg){
    	boolean check = true;
    	while(cracked == false){
    		for(int i = 0; i < 256; i++){
    			if(cracked){
    				return msg;
    			}

    			msg.setCharAt(pos, (char) i);
    			if((pos+1) < msg.length()){
    				if(msg.charAt(pos) == 0) continue;
    				msg = recurciveMd(mdMsg, msg, pos+1, testMsg);
    			}
    			if(i== 255 && pos == 0){
    				msg.setCharAt(pos, (char) 0);
    				msg.append((char)0);
    				msg = recurciveMd(mdMsg, msg, pos+1, testMsg);
    			} else if(i == 255){
    				return msg;
    			}
    			try{	
    				byte[] inputBytes = (msg.toString()).getBytes("UTF-8");
    				mdMsg.update(inputBytes);
    				crackedMd = mdMsg.digest();
    			} catch (Exception e) {
    				System.out.println("Exception "+e);

    			}
    			for(int j = 0; j < 3; j++){
    				if(crackedMd[j] == testMsg[j]){
    					cracked = true;
    				} else {
    					cracked = false;
    					break;
    				}
    			}

    			tries++;
    		}
    	}
    	return msg;
    }
}