/*    */ package org.yaml.snakeyaml.util;
/*    */ 
/*    */ import java.io.UnsupportedEncodingException;
/*    */ import java.net.URLDecoder;
/*    */ import java.nio.ByteBuffer;
/*    */ import java.nio.CharBuffer;
/*    */ import java.nio.charset.CharacterCodingException;
/*    */ import java.nio.charset.CharsetDecoder;
/*    */ import java.nio.charset.CodingErrorAction;
/*    */ import java.nio.charset.StandardCharsets;
/*    */ import org.yaml.snakeyaml.error.YAMLException;
/*    */ import org.yaml.snakeyaml.external.com.google.gdata.util.common.base.Escaper;
/*    */ import org.yaml.snakeyaml.external.com.google.gdata.util.common.base.PercentEscaper;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public abstract class UriEncoder
/*    */ {
/* 30 */   private static final CharsetDecoder UTF8Decoder = StandardCharsets.UTF_8
/* 31 */     .newDecoder().onMalformedInput(CodingErrorAction.REPORT);
/*    */ 
/*    */   
/*    */   private static final String SAFE_CHARS = "-_.!~*'()@:$&,;=[]/";
/*    */   
/* 36 */   private static final Escaper escaper = (Escaper)new PercentEscaper("-_.!~*'()@:$&,;=[]/", false);
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static String encode(String uri) {
/* 45 */     return escaper.escape(uri);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static String decode(ByteBuffer buff) throws CharacterCodingException {
/* 56 */     CharBuffer chars = UTF8Decoder.decode(buff);
/* 57 */     return chars.toString();
/*    */   }
/*    */   
/*    */   public static String decode(String buff) {
/*    */     try {
/* 62 */       return URLDecoder.decode(buff, "UTF-8");
/* 63 */     } catch (UnsupportedEncodingException e) {
/* 64 */       throw new YAMLException(e);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\org\yaml\snakeyam\\util\UriEncoder.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */