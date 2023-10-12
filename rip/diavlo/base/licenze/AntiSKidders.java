/*    */ package rip.diavlo.base.licenze;
/*    */ 
/*    */ import com.google.common.base.Charsets;
/*    */ import com.google.common.hash.HashCode;
/*    */ import com.google.common.hash.HashFunction;
/*    */ import com.google.common.hash.Hashing;
/*    */ import java.nio.charset.StandardCharsets;
/*    */ import java.security.MessageDigest;
/*    */ import java.security.NoSuchAlgorithmException;
/*    */ import net.minecraft.client.Minecraft;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class AntiSKidders
/*    */ {
/*    */   public static void main(String[] args) {
/* 22 */     System.out.println(getLicenzeString());
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public static String getLicenzeString() {
/* 28 */     String s = "";
/*    */     try {
/* 30 */       s = sha256(createLicenseKey(System.getenv("USERNAME") + System.getenv("PROCESSOR_IDENTIFIER") + System.getenv("PROCESSOR_LEVEL"), "Diavlo", "1.0-DEV"));
/* 31 */     } catch (Exception e) {
/* 32 */       Minecraft.getMinecraft().shutdown();
/* 33 */       System.runFinalization();
/* 34 */       System.gc();
/*    */     } 
/* 36 */     return s;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static String group(String s) {
/* 43 */     StringBuilder result = new StringBuilder();
/* 44 */     for (int i = 0; i < s.length(); i++) {
/* 45 */       if (i % 6 == 0 && i > 0) result.append('-'); 
/* 46 */       result.append(s.charAt(i));
/*    */     } 
/* 48 */     return result.toString();
/*    */   }
/*    */   
/*    */   public static String sha256(String paramString) throws NoSuchAlgorithmException {
/* 52 */     MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
/* 53 */     messageDigest.update(paramString.getBytes(StandardCharsets.UTF_8));
/* 54 */     byte[] arrayOfByte = messageDigest.digest();
/* 55 */     StringBuilder stringBuilder = new StringBuilder();
/* 56 */     int i = arrayOfByte.length;
/* 57 */     for (byte value : arrayOfByte) {
/* 58 */       stringBuilder.append(String.format("%02x", new Object[] { Byte.valueOf(value) }));
/*    */     } 
/* 60 */     return stringBuilder.toString();
/*    */   }
/*    */   
/*    */   public static String createLicenseKey(String userName, String productKey, String versionNumber) {
/* 64 */     String s = userName + "|" + productKey + "|" + versionNumber;
/* 65 */     HashFunction hashFunction = Hashing.sha1();
/* 66 */     HashCode hashCode = hashFunction.hashString(s, Charsets.UTF_8);
/* 67 */     String upper = hashCode.toString().toUpperCase();
/* 68 */     return group(upper);
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\rip\diavlo\base\licenze\AntiSKidders.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */