package net.sparkmuse.user;

import net.sparkmuse.data.entity.UserVO;

import java.util.Arrays;
import java.security.SecureRandom;
import java.security.NoSuchAlgorithmException;
import java.security.MessageDigest;
import java.io.UnsupportedEncodingException;
import java.io.IOException;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

/**
 * @author neteller
 * @created: Mar 8, 2011
 */
public class SaltedPassword {

  private String salt;
  private String digest; //salted

  public SaltedPassword(String salt, String digest) {
    this.salt = salt;
    this.digest = digest;
  }

  public String getSalt() {
    return salt;
  }

  public String getDigest() {
    return digest;
  }

  public void setSalt(String salt) {
    this.salt = salt;
  }

  public void setDigest(String digest) {
    this.digest = digest;
  }

  public boolean verifyPassword(String password) {
    byte[] bDigest = base64ToByte(this.getDigest());
    byte[] bSalt = base64ToByte(this.getSalt());

    // Compute the new DIGEST
    byte[] proposedDigest = getHash(password, bSalt);

    return Arrays.equals(proposedDigest, bDigest);
  }

  public static SaltedPassword newPassword(String password) {
    byte[] bSalt = randomSalt();
    // Digest computation
    byte[] bDigest = getHash(password, bSalt);
    String sDigest = byteToBase64(bDigest);
    String sSalt = byteToBase64(bSalt);
    return new SaltedPassword(sSalt, sDigest);
  }

  private static byte[] randomSalt() {
    try {
      SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
      // Salt generation 64 bits long
      byte[] bSalt = new byte[8];
      random.nextBytes(bSalt);
      return bSalt;
    } catch (NoSuchAlgorithmException e) {
      throw new RuntimeException(e);
    }
  }

  private static byte[] getHash(String password, byte[] salt) {
    try {
      MessageDigest digest = MessageDigest.getInstance("SHA-1");
      digest.reset();
      digest.update(salt);
      byte[] input = digest.digest(password.getBytes("UTF-8"));
      return input;
    } catch (UnsupportedEncodingException e) {
      throw new RuntimeException(e);
    } catch (NoSuchAlgorithmException e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * From a base 64 representation, returns the corresponding byte[]
   *
   * @param data String The base64 representation
   * @return byte[]
   * @throws java.io.IOException
   */
  private static byte[] base64ToByte(String data) {
    try {
      BASE64Decoder decoder = new BASE64Decoder();
      return decoder.decodeBuffer(data);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * From a byte[] returns a base 64 representation
   *
   * @param data byte[]
   * @return String
   * @throws IOException
   */
  private static String byteToBase64(byte[] data) {
    BASE64Encoder endecoder = new BASE64Encoder();
    return endecoder.encode(data);
  }
}
