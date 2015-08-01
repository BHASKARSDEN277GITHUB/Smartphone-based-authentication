/**
 * @author bhaskar kalia
 */


/**
 * This class is used for encryption purpose . 
 * It can be used be both by specifying key and without key . (As it already has one ..)..
 * 
 * Methods :
 *  
 *  public static String encrypt(String ,String ) .. to encrypt encrypted data .. (for code , scroll down) ..
 *  private static Key generateKey(String ) .. to generate byte[] of  key from string ..
 * 
 * 
 * classes imported are below in imported section ..
 */


import java.security.*;
import java.security.spec.InvalidKeySpecException;
import javax.crypto.spec.SecretKeySpec;
import sun.misc.*;
import javax.crypto.*;

public class encryptor	 {
    
     private static final String ALGO = "AES";
     private static final byte[] keyValue = new byte[] { 'T', 'h', 'e', 'B', 'e', 's', 't','S', 'e', 'c', 'r','e', 't', 'K', 'e', 'y' };

	public static String encrypt(String Data) throws Exception {
        Key key = generateKey();
        Cipher c = Cipher.getInstance(ALGO);
        c.init(Cipher.ENCRYPT_MODE, key);
        byte[] encVal = c.doFinal(Data.getBytes());
        String encryptedValue = new BASE64Encoder().encode(encVal);
        return encryptedValue;
    	}
        
        public static String encrypt(String Data,String keyv) throws Exception {
        Key key = generateKey(keyv);
        Cipher c = Cipher.getInstance(ALGO);
        c.init(Cipher.ENCRYPT_MODE, key);
        byte[] encVal = c.doFinal(Data.getBytes());
        String encryptedValue = new BASE64Encoder().encode(encVal);
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
