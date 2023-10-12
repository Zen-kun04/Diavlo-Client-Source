/*     */ package net.minecraft.client.resources;
/*     */ 
/*     */ import com.google.common.base.Splitter;
/*     */ import com.google.common.collect.Iterables;
/*     */ import com.google.common.collect.Maps;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.util.IllegalFormatException;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.regex.Pattern;
/*     */ import net.minecraft.util.ResourceLocation;
/*     */ import org.apache.commons.io.Charsets;
/*     */ import org.apache.commons.io.IOUtils;
/*     */ 
/*     */ public class Locale
/*     */ {
/*  18 */   private static final Splitter splitter = Splitter.on('=').limit(2);
/*  19 */   private static final Pattern pattern = Pattern.compile("%(\\d+\\$)?[\\d\\.]*[df]");
/*  20 */   Map<String, String> properties = Maps.newHashMap();
/*     */   
/*     */   private boolean unicode;
/*     */   
/*     */   public synchronized void loadLocaleDataFiles(IResourceManager resourceManager, List<String> languageList) {
/*  25 */     this.properties.clear();
/*     */     
/*  27 */     for (String s : languageList) {
/*     */       
/*  29 */       String s1 = String.format("lang/%s.lang", new Object[] { s });
/*     */       
/*  31 */       for (String s2 : resourceManager.getResourceDomains()) {
/*     */ 
/*     */         
/*     */         try {
/*  35 */           loadLocaleData(resourceManager.getAllResources(new ResourceLocation(s2, s1)));
/*     */         }
/*  37 */         catch (IOException iOException) {}
/*     */       } 
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  44 */     checkUnicode();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isUnicode() {
/*  49 */     return this.unicode;
/*     */   }
/*     */ 
/*     */   
/*     */   private void checkUnicode() {
/*  54 */     this.unicode = false;
/*  55 */     int i = 0;
/*  56 */     int j = 0;
/*     */     
/*  58 */     for (String s : this.properties.values()) {
/*     */       
/*  60 */       int k = s.length();
/*  61 */       j += k;
/*     */       
/*  63 */       for (int l = 0; l < k; l++) {
/*     */         
/*  65 */         if (s.charAt(l) >= 'Ā')
/*     */         {
/*  67 */           i++;
/*     */         }
/*     */       } 
/*     */     } 
/*     */     
/*  72 */     float f = i / j;
/*  73 */     this.unicode = (f > 0.1D);
/*     */   }
/*     */ 
/*     */   
/*     */   private void loadLocaleData(List<IResource> resourcesList) throws IOException {
/*  78 */     for (IResource iresource : resourcesList) {
/*     */       
/*  80 */       InputStream inputstream = iresource.getInputStream();
/*     */ 
/*     */       
/*     */       try {
/*  84 */         loadLocaleData(inputstream);
/*     */       }
/*     */       finally {
/*     */         
/*  88 */         IOUtils.closeQuietly(inputstream);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private void loadLocaleData(InputStream inputStreamIn) throws IOException {
/*  95 */     for (String s : IOUtils.readLines(inputStreamIn, Charsets.UTF_8)) {
/*     */       
/*  97 */       if (!s.isEmpty() && s.charAt(0) != '#') {
/*     */         
/*  99 */         String[] astring = (String[])Iterables.toArray(splitter.split(s), String.class);
/*     */         
/* 101 */         if (astring != null && astring.length == 2) {
/*     */           
/* 103 */           String s1 = astring[0];
/* 104 */           String s2 = pattern.matcher(astring[1]).replaceAll("%$1s");
/* 105 */           this.properties.put(s1, s2);
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private String translateKeyPrivate(String translateKey) {
/* 113 */     String s = this.properties.get(translateKey);
/* 114 */     return (s == null) ? translateKey : s;
/*     */   }
/*     */ 
/*     */   
/*     */   public String formatMessage(String translateKey, Object[] parameters) {
/* 119 */     String s = translateKeyPrivate(translateKey);
/*     */ 
/*     */     
/*     */     try {
/* 123 */       return String.format(s, parameters);
/*     */     }
/* 125 */     catch (IllegalFormatException var5) {
/*     */       
/* 127 */       return "Format error: " + s;
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\client\resources\Locale.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */