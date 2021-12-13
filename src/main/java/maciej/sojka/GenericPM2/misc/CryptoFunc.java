package maciej.sojka.GenericPM2.misc;

import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Formatter;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.Cipher;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

public class CryptoFunc {

    public static final String PEPPER = "sYFIk7I55q0mVbL9Cx5X";
    private static final String ALGO = "AES";
    private static final String HMAC_SHA512 = "HmacSHA512";

    public static String calculateSHA512(String text){

        try {

            MessageDigest md = MessageDigest.getInstance("SHA-512");
            byte[] messageDigest = md.digest(text.getBytes());
            BigInteger no = new BigInteger(1, messageDigest);
            String hashtext = no.toString(16);

            while (hashtext.length() < 32) {
                hashtext = "0" + hashtext;
            }

            return hashtext;

        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }

    }

    private static String toHexString(byte[] bytes) {

        Formatter formatter = new Formatter();
        for (byte b : bytes) formatter.format("%02x", b);
        return formatter.toString();

    }

    public static String calculateHMAC(String data, String key){

        SecretKeySpec secretKeySpec =
                new SecretKeySpec(key.getBytes(), HMAC_SHA512);
        Mac mac = null;

        try { mac = Mac.getInstance(HMAC_SHA512); }
        catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(
                    CryptoFunc.class.getName()).log(Level.SEVERE, null, ex);
        }

        try { mac.init(secretKeySpec); }
        catch (InvalidKeyException ex) {
            Logger.getLogger(
                    CryptoFunc.class.getName()).log(Level.SEVERE, null, ex);
        }

        return toHexString(mac.doFinal(data.getBytes()));

    }

    public static byte[] calculateMD5(String text) {

        try {

            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] messageDigest = md.digest(text.getBytes());
            return messageDigest;

        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }

    }

    public static String encrypt(String data, Key key) throws Exception {

        Cipher c = Cipher.getInstance(ALGO);
        c.init(Cipher.ENCRYPT_MODE, key);
        byte[] encVal = c.doFinal(data.getBytes());
        return Base64.getEncoder().encodeToString(encVal);

    }

    public static String decrypt(String encryptedData, Key key) throws Exception {

        Cipher c = Cipher.getInstance(ALGO);
        c.init(Cipher.DECRYPT_MODE, key);
        byte[] decodedValue = Base64.getDecoder().decode(encryptedData);
        byte[] decValue = c.doFinal(decodedValue);
        return new String(decValue);

    }

    public static Key generateKey(String password) throws Exception {
        return new SecretKeySpec(calculateMD5(password), ALGO);
    }

}