/*    */ package net.minecraft.util;
/*    */ 
/*    */ import com.google.common.base.Splitter;
/*    */ import com.google.common.collect.Iterables;
/*    */ import com.google.common.collect.Maps;
/*    */ import java.io.IOException;
/*    */ import java.io.InputStream;
/*    */ import java.util.IllegalFormatException;
/*    */ import java.util.Map;
/*    */ import java.util.regex.Pattern;
/*    */ import org.apache.commons.io.Charsets;
/*    */ import org.apache.commons.io.IOUtils;
/*    */ 
/*    */ 
/*    */ public class StringTranslate
/*    */ {
/* 17 */   private static final Pattern numericVariablePattern = Pattern.compile("%(\\d+\\$)?[\\d\\.]*[df]");
/* 18 */   private static final Splitter equalSignSplitter = Splitter.on('=').limit(2);
/* 19 */   private static StringTranslate instance = new StringTranslate();
/* 20 */   private final Map<String, String> languageList = Maps.newHashMap();
/*    */   
/*    */   private long lastUpdateTimeInMilliseconds;
/*    */ 
/*    */   
/*    */   public StringTranslate() {
/*    */     try {
/* 27 */       InputStream inputstream = StringTranslate.class.getResourceAsStream("/assets/minecraft/lang/en_US.lang");
/*    */       
/* 29 */       for (String s : IOUtils.readLines(inputstream, Charsets.UTF_8)) {
/*    */         
/* 31 */         if (!s.isEmpty() && s.charAt(0) != '#') {
/*    */           
/* 33 */           String[] astring = (String[])Iterables.toArray(equalSignSplitter.split(s), String.class);
/*    */           
/* 35 */           if (astring != null && astring.length == 2) {
/*    */             
/* 37 */             String s1 = astring[0];
/* 38 */             String s2 = numericVariablePattern.matcher(astring[1]).replaceAll("%$1s");
/* 39 */             this.languageList.put(s1, s2);
/*    */           } 
/*    */         } 
/*    */       } 
/*    */       
/* 44 */       this.lastUpdateTimeInMilliseconds = System.currentTimeMillis();
/*    */     }
/* 46 */     catch (IOException iOException) {}
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   static StringTranslate getInstance() {
/* 54 */     return instance;
/*    */   }
/*    */ 
/*    */   
/*    */   public static synchronized void replaceWith(Map<String, String> p_135063_0_) {
/* 59 */     instance.languageList.clear();
/* 60 */     instance.languageList.putAll(p_135063_0_);
/* 61 */     instance.lastUpdateTimeInMilliseconds = System.currentTimeMillis();
/*    */   }
/*    */ 
/*    */   
/*    */   public synchronized String translateKey(String key) {
/* 66 */     return tryTranslateKey(key);
/*    */   }
/*    */ 
/*    */   
/*    */   public synchronized String translateKeyFormat(String key, Object... format) {
/* 71 */     String s = tryTranslateKey(key);
/*    */ 
/*    */     
/*    */     try {
/* 75 */       return String.format(s, format);
/*    */     }
/* 77 */     catch (IllegalFormatException var5) {
/*    */       
/* 79 */       return "Format error: " + s;
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   private String tryTranslateKey(String key) {
/* 85 */     String s = this.languageList.get(key);
/* 86 */     return (s == null) ? key : s;
/*    */   }
/*    */ 
/*    */   
/*    */   public synchronized boolean isKeyTranslated(String key) {
/* 91 */     return this.languageList.containsKey(key);
/*    */   }
/*    */ 
/*    */   
/*    */   public long getLastUpdateTimeInMilliseconds() {
/* 96 */     return this.lastUpdateTimeInMilliseconds;
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraf\\util\StringTranslate.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */