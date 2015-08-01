/**
 * This class contains the hashAlgorithm to calculate the hash (message digest of given text).
 * It uses MessageDigest class from java.security.MessageDigest package ..
 */

package finalyear.major.authenticationapp;


import java.io.FileInputStream;
import java.security.MessageDigest;


public class  hashAlgo
{
	public hashAlgo()
	{
	}
   public String execute(String arg)throws Exception
   {
       MessageDigest md = MessageDigest.getInstance("SHA-512");
	
	byte[] bytestr=arg.getBytes();
	
	md.update(bytestr);
       byte[] mdbytes = md.digest();
	
	//convert byte array to hex ..
	
	return	(Hex.encodeHexString(mdbytes)).toUpperCase();
   }
}
