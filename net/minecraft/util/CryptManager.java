/*     */ package net.minecraft.util;
/*     */ import java.security.GeneralSecurityException;
/*     */ import java.security.InvalidKeyException;
/*     */ import java.security.Key;
/*     */ import java.security.KeyFactory;
/*     */ import java.security.KeyPairGenerator;
/*     */ import java.security.MessageDigest;
/*     */ import java.security.NoSuchAlgorithmException;
/*     */ import java.security.PrivateKey;
/*     */ import java.security.PublicKey;
/*     */ import java.security.spec.EncodedKeySpec;
/*     */ import javax.crypto.BadPaddingException;
/*     */ import javax.crypto.Cipher;
/*     */ import javax.crypto.KeyGenerator;
/*     */ import javax.crypto.SecretKey;
/*     */ 
/*     */ public class CryptManager {
/*  18 */   private static final Logger LOGGER = LogManager.getLogger();
/*     */ 
/*     */ 
/*     */   
/*     */   public static SecretKey createNewSharedKey() {
/*     */     try {
/*  24 */       KeyGenerator keygenerator = KeyGenerator.getInstance("AES");
/*  25 */       keygenerator.init(128);
/*  26 */       return keygenerator.generateKey();
/*     */     }
/*  28 */     catch (NoSuchAlgorithmException nosuchalgorithmexception) {
/*     */       
/*  30 */       throw new Error(nosuchalgorithmexception);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static KeyPair generateKeyPair() {
/*     */     try {
/*  38 */       KeyPairGenerator keypairgenerator = KeyPairGenerator.getInstance("RSA");
/*  39 */       keypairgenerator.initialize(1024);
/*  40 */       return keypairgenerator.generateKeyPair();
/*     */     }
/*  42 */     catch (NoSuchAlgorithmException nosuchalgorithmexception) {
/*     */       
/*  44 */       nosuchalgorithmexception.printStackTrace();
/*  45 */       LOGGER.error("Key pair generation failed!");
/*  46 */       return null;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static byte[] getServerIdHash(String serverId, PublicKey publicKey, SecretKey secretKey) {
/*     */     try {
/*  54 */       return digestOperation("SHA-1", new byte[][] { serverId.getBytes("ISO_8859_1"), secretKey.getEncoded(), publicKey.getEncoded() });
/*     */     }
/*  56 */     catch (UnsupportedEncodingException unsupportedencodingexception) {
/*     */       
/*  58 */       unsupportedencodingexception.printStackTrace();
/*  59 */       return null;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static byte[] digestOperation(String algorithm, byte[]... data) {
/*     */     try {
/*  67 */       MessageDigest messagedigest = MessageDigest.getInstance(algorithm);
/*     */       
/*  69 */       for (byte[] abyte : data)
/*     */       {
/*  71 */         messagedigest.update(abyte);
/*     */       }
/*     */       
/*  74 */       return messagedigest.digest();
/*     */     }
/*  76 */     catch (NoSuchAlgorithmException nosuchalgorithmexception) {
/*     */       
/*  78 */       nosuchalgorithmexception.printStackTrace();
/*  79 */       return null;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static PublicKey decodePublicKey(byte[] encodedKey) {
/*     */     try {
/*  87 */       EncodedKeySpec encodedkeyspec = new X509EncodedKeySpec(encodedKey);
/*  88 */       KeyFactory keyfactory = KeyFactory.getInstance("RSA");
/*  89 */       return keyfactory.generatePublic(encodedkeyspec);
/*     */     }
/*  91 */     catch (NoSuchAlgorithmException noSuchAlgorithmException) {
/*     */ 
/*     */     
/*     */     }
/*  95 */     catch (InvalidKeySpecException invalidKeySpecException) {}
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 100 */     LOGGER.error("Public key reconstitute failed!");
/* 101 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public static SecretKey decryptSharedKey(PrivateKey key, byte[] secretKeyEncrypted) {
/* 106 */     return new SecretKeySpec(decryptData(key, secretKeyEncrypted), "AES");
/*     */   }
/*     */ 
/*     */   
/*     */   public static byte[] encryptData(Key key, byte[] data) {
/* 111 */     return cipherOperation(1, key, data);
/*     */   }
/*     */ 
/*     */   
/*     */   public static byte[] decryptData(Key key, byte[] data) {
/* 116 */     return cipherOperation(2, key, data);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static byte[] cipherOperation(int opMode, Key key, byte[] data) {
/*     */     try {
/* 123 */       return createTheCipherInstance(opMode, key.getAlgorithm(), key).doFinal(data);
/*     */     }
/* 125 */     catch (IllegalBlockSizeException illegalblocksizeexception) {
/*     */       
/* 127 */       illegalblocksizeexception.printStackTrace();
/*     */     }
/* 129 */     catch (BadPaddingException badpaddingexception) {
/*     */       
/* 131 */       badpaddingexception.printStackTrace();
/*     */     } 
/*     */     
/* 134 */     LOGGER.error("Cipher data failed!");
/* 135 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static Cipher createTheCipherInstance(int opMode, String transformation, Key key) {
/*     */     try {
/* 142 */       Cipher cipher = Cipher.getInstance(transformation);
/* 143 */       cipher.init(opMode, key);
/* 144 */       return cipher;
/*     */     }
/* 146 */     catch (InvalidKeyException invalidkeyexception) {
/*     */       
/* 148 */       invalidkeyexception.printStackTrace();
/*     */     }
/* 150 */     catch (NoSuchAlgorithmException nosuchalgorithmexception) {
/*     */       
/* 152 */       nosuchalgorithmexception.printStackTrace();
/*     */     }
/* 154 */     catch (NoSuchPaddingException nosuchpaddingexception) {
/*     */       
/* 156 */       nosuchpaddingexception.printStackTrace();
/*     */     } 
/*     */     
/* 159 */     LOGGER.error("Cipher creation failed!");
/* 160 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static Cipher createNetCipherInstance(int opMode, Key key) {
/*     */     try {
/* 167 */       Cipher cipher = Cipher.getInstance("AES/CFB8/NoPadding");
/* 168 */       cipher.init(opMode, key, new IvParameterSpec(key.getEncoded()));
/* 169 */       return cipher;
/*     */     }
/* 171 */     catch (GeneralSecurityException generalsecurityexception) {
/*     */       
/* 173 */       throw new RuntimeException(generalsecurityexception);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraf\\util\CryptManager.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */