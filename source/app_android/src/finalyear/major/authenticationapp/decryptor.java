/**
 * This class is used to decrypt the AES encrypted values (Strings) .. 
 * 
 * Methods :
 * public static String decrypt(String)
 * public static String decrypt(String ,String )
 * private static Key generateKey(String) //converts key to byte[] (array) from string ..
 * 
 */




package finalyear.major.authenticationapp;

import java.security.*;
import java.security.spec.InvalidKeySpecException;
import javax.crypto.spec.SecretKeySpec;
import javax.crypto.*;

import android.util.Base64;

public class decryptor{

	private static final String ALGO = "AES";
	private static final byte[] keyValue = new byte[] { 'T', 'h', 'e', 'B', 'e', 's', 't','S', 'e', 'c', 'r','e', 't', 'K', 'e', 'y' };
	

	public static String decrypt(String encryptedData) throws Exception 
	{
        	Key key = generateKey();
       		Cipher c = Cipher.getInstance(ALGO);
       	 	c.init(Cipher.DECRYPT_MODE, key);
       	    byte[] data1 = Base64.decode(encryptedData, Base64.DEFAULT);
        	byte[] decValue = c.doFinal(data1);
        	String decryptedValue = new String(decValue);
        	return decryptedValue;
	}	
	
	public static String decrypt(String encryptedData,String keyv) throws Exception 
	{
        	Key key = generateKey(keyv);
       		Cipher c = Cipher.getInstance(ALGO);
       	 	c.init(Cipher.DECRYPT_MODE, key);
       	    byte[] data1 = Base64.decode(encryptedData, Base64.DEFAULT);
        	byte[] decValue = c.doFinal(data1);
        	String decryptedValue = new String(decValue);
        	return decryptedValue;
	}	
	
	private static Key generateKey(String key1) throws Exception {
				int i ;
        		byte[] keyv = new byte[16];
        
        		for(i=0;i<16;i++)
        		{
            			keyv[i]=(byte) key1.charAt(i);
				
        		}
        		for(i=0;i<16;i++)
        		{
        			System.out.println((char)keyv[i]);
        		}
        		Key key = new SecretKeySpec(keyv, ALGO);
        		return key;
}
	
	private static Key generateKey() throws Exception {
        Key key = new SecretKeySpec(keyValue, ALGO);
        return key;

}
}
