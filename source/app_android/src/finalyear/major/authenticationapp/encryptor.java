/**
 * This class is used to encrypt the AES encrypted values (Strings) .. 
 * 
 * Methods :
 * public static String encrypt(String)
 * public static String encrypt(String ,String )
 * private static Key generateKey(String) //converts key to byte[] (array) from string ..
 * 
 */


package finalyear.major.authenticationapp;

import java.security.*;
import java.security.spec.InvalidKeySpecException;
import javax.crypto.spec.SecretKeySpec;
import javax.crypto.*;

import android.util.Base64;

public class encryptor	 {
    
   	  private static final String ALGO = "AES";
   	  private static final byte[] keyValue = new byte[] { 'T', 'h', 'e', 'B', 'e', 's', 't','S', 'e', 'c', 'r','e', 't', 'K', 'e', 'y' };

	public static String encrypt(String Data) throws Exception {
        Key key = generateKey();
        Cipher c = Cipher.getInstance(ALGO);
        c.init(Cipher.ENCRYPT_MODE, key);
        byte[] encVal = c.doFinal(Data.getBytes());
        String base64 = Base64.encodeToString(encVal, Base64.DEFAULT);
        return base64;
    	}
	
		
	
	public static String encrypt(String Data,String keyv) throws Exception {
        Key key = generateKey(keyv);
        Cipher c = Cipher.getInstance(ALGO);
        c.init(Cipher.ENCRYPT_MODE, key);
        byte[] encVal = c.doFinal(Data.getBytes());
        String encryptedValue = Base64.encodeToString(encVal, Base64.DEFAULT);
        return encryptedValue;
    	}


    
    	private static Key generateKey() throws Exception {
        Key key = new SecretKeySpec(keyValue, ALGO);
        return key;
	}
    	private static Key generateKey(String SS) throws Exception {
            int i =0 ;
            byte[] keyv = new byte[16];
            
            for(i=0;i<16;i++)
            {
                keyv[i]=(byte) SS.charAt(i);
            }
            Key key = new SecretKeySpec(keyv, ALGO);
            return key;
    }

}
