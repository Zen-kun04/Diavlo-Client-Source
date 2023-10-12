/*     */ package net.optifine.util;
/*     */ import java.io.File;
/*     */ import java.io.FileNotFoundException;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Enumeration;
/*     */ import java.util.List;
/*     */ import java.util.Properties;
/*     */ import java.util.Set;
/*     */ import java.util.zip.ZipEntry;
/*     */ import java.util.zip.ZipFile;
/*     */ import net.minecraft.client.resources.AbstractResourcePack;
/*     */ import net.minecraft.client.resources.IResourcePack;
/*     */ import net.minecraft.src.Config;
/*     */ import net.minecraft.util.ResourceLocation;
/*     */ 
/*     */ public class ResUtils {
/*     */   public static String[] collectFiles(String prefix, String suffix) {
/*  21 */     return collectFiles(new String[] { prefix }, new String[] { suffix });
/*     */   }
/*     */ 
/*     */   
/*     */   public static String[] collectFiles(String[] prefixes, String[] suffixes) {
/*  26 */     Set<String> set = new LinkedHashSet<>();
/*  27 */     IResourcePack[] airesourcepack = Config.getResourcePacks();
/*     */     
/*  29 */     for (int i = 0; i < airesourcepack.length; i++) {
/*     */       
/*  31 */       IResourcePack iresourcepack = airesourcepack[i];
/*  32 */       String[] astring = collectFiles(iresourcepack, prefixes, suffixes, (String[])null);
/*  33 */       set.addAll(Arrays.asList(astring));
/*     */     } 
/*     */     
/*  36 */     String[] astring1 = set.<String>toArray(new String[set.size()]);
/*  37 */     return astring1;
/*     */   }
/*     */ 
/*     */   
/*     */   public static String[] collectFiles(IResourcePack rp, String prefix, String suffix, String[] defaultPaths) {
/*  42 */     return collectFiles(rp, new String[] { prefix }, new String[] { suffix }, defaultPaths);
/*     */   }
/*     */ 
/*     */   
/*     */   public static String[] collectFiles(IResourcePack rp, String[] prefixes, String[] suffixes) {
/*  47 */     return collectFiles(rp, prefixes, suffixes, (String[])null);
/*     */   }
/*     */ 
/*     */   
/*     */   public static String[] collectFiles(IResourcePack rp, String[] prefixes, String[] suffixes, String[] defaultPaths) {
/*  52 */     if (rp instanceof net.minecraft.client.resources.DefaultResourcePack)
/*     */     {
/*  54 */       return collectFilesFixed(rp, defaultPaths);
/*     */     }
/*  56 */     if (!(rp instanceof AbstractResourcePack)) {
/*     */       
/*  58 */       Config.warn("Unknown resource pack type: " + rp);
/*  59 */       return new String[0];
/*     */     } 
/*     */ 
/*     */     
/*  63 */     AbstractResourcePack abstractresourcepack = (AbstractResourcePack)rp;
/*  64 */     File file1 = abstractresourcepack.resourcePackFile;
/*     */     
/*  66 */     if (file1 == null)
/*     */     {
/*  68 */       return new String[0];
/*     */     }
/*  70 */     if (file1.isDirectory())
/*     */     {
/*  72 */       return collectFilesFolder(file1, "", prefixes, suffixes);
/*     */     }
/*  74 */     if (file1.isFile())
/*     */     {
/*  76 */       return collectFilesZIP(file1, prefixes, suffixes);
/*     */     }
/*     */ 
/*     */     
/*  80 */     Config.warn("Unknown resource pack file: " + file1);
/*  81 */     return new String[0];
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static String[] collectFilesFixed(IResourcePack rp, String[] paths) {
/*  88 */     if (paths == null)
/*     */     {
/*  90 */       return new String[0];
/*     */     }
/*     */ 
/*     */     
/*  94 */     List<String> list = new ArrayList();
/*     */     
/*  96 */     for (int i = 0; i < paths.length; i++) {
/*     */       
/*  98 */       String s = paths[i];
/*  99 */       ResourceLocation resourcelocation = new ResourceLocation(s);
/*     */       
/* 101 */       if (rp.resourceExists(resourcelocation))
/*     */       {
/* 103 */         list.add(s);
/*     */       }
/*     */     } 
/*     */     
/* 107 */     String[] astring = list.<String>toArray(new String[list.size()]);
/* 108 */     return astring;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static String[] collectFilesFolder(File tpFile, String basePath, String[] prefixes, String[] suffixes) {
/* 114 */     List<String> list = new ArrayList();
/* 115 */     String s = "assets/minecraft/";
/* 116 */     File[] afile = tpFile.listFiles();
/*     */     
/* 118 */     if (afile == null)
/*     */     {
/* 120 */       return new String[0];
/*     */     }
/*     */ 
/*     */     
/* 124 */     for (int i = 0; i < afile.length; i++) {
/*     */       
/* 126 */       File file1 = afile[i];
/*     */       
/* 128 */       if (file1.isFile()) {
/*     */         
/* 130 */         String s3 = basePath + file1.getName();
/*     */         
/* 132 */         if (s3.startsWith(s))
/*     */         {
/* 134 */           s3 = s3.substring(s.length());
/*     */           
/* 136 */           if (StrUtils.startsWith(s3, prefixes) && StrUtils.endsWith(s3, suffixes))
/*     */           {
/* 138 */             list.add(s3);
/*     */           }
/*     */         }
/*     */       
/* 142 */       } else if (file1.isDirectory()) {
/*     */         
/* 144 */         String s1 = basePath + file1.getName() + "/";
/* 145 */         String[] astring = collectFilesFolder(file1, s1, prefixes, suffixes);
/*     */         
/* 147 */         for (int j = 0; j < astring.length; j++) {
/*     */           
/* 149 */           String s2 = astring[j];
/* 150 */           list.add(s2);
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/* 155 */     String[] astring1 = list.<String>toArray(new String[list.size()]);
/* 156 */     return astring1;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static String[] collectFilesZIP(File tpFile, String[] prefixes, String[] suffixes) {
/* 162 */     List<String> list = new ArrayList();
/* 163 */     String s = "assets/minecraft/";
/*     */ 
/*     */     
/*     */     try {
/* 167 */       ZipFile zipfile = new ZipFile(tpFile);
/* 168 */       Enumeration<? extends ZipEntry> enumeration = zipfile.entries();
/*     */       
/* 170 */       while (enumeration.hasMoreElements()) {
/*     */         
/* 172 */         ZipEntry zipentry = enumeration.nextElement();
/* 173 */         String s1 = zipentry.getName();
/*     */         
/* 175 */         if (s1.startsWith(s)) {
/*     */           
/* 177 */           s1 = s1.substring(s.length());
/*     */           
/* 179 */           if (StrUtils.startsWith(s1, prefixes) && StrUtils.endsWith(s1, suffixes))
/*     */           {
/* 181 */             list.add(s1);
/*     */           }
/*     */         } 
/*     */       } 
/*     */       
/* 186 */       zipfile.close();
/* 187 */       String[] astring = list.<String>toArray(new String[list.size()]);
/* 188 */       return astring;
/*     */     }
/* 190 */     catch (IOException ioexception) {
/*     */       
/* 192 */       ioexception.printStackTrace();
/* 193 */       return new String[0];
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private static boolean isLowercase(String str) {
/* 199 */     return str.equals(str.toLowerCase(Locale.ROOT));
/*     */   }
/*     */ 
/*     */   
/*     */   public static Properties readProperties(String path, String module) {
/* 204 */     ResourceLocation resourcelocation = new ResourceLocation(path);
/*     */ 
/*     */     
/*     */     try {
/* 208 */       InputStream inputstream = Config.getResourceStream(resourcelocation);
/*     */       
/* 210 */       if (inputstream == null)
/*     */       {
/* 212 */         return null;
/*     */       }
/*     */ 
/*     */       
/* 216 */       Properties properties = new PropertiesOrdered();
/* 217 */       properties.load(inputstream);
/* 218 */       inputstream.close();
/* 219 */       Config.dbg("" + module + ": Loading " + path);
/* 220 */       return properties;
/*     */     
/*     */     }
/* 223 */     catch (FileNotFoundException var5) {
/*     */       
/* 225 */       return null;
/*     */     }
/* 227 */     catch (IOException var6) {
/*     */       
/* 229 */       Config.warn("" + module + ": Error reading " + path);
/* 230 */       return null;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public static Properties readProperties(InputStream in, String module) {
/* 236 */     if (in == null)
/*     */     {
/* 238 */       return null;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     try {
/* 244 */       Properties properties = new PropertiesOrdered();
/* 245 */       properties.load(in);
/* 246 */       in.close();
/* 247 */       return properties;
/*     */     }
/* 249 */     catch (FileNotFoundException var3) {
/*     */       
/* 251 */       return null;
/*     */     }
/* 253 */     catch (IOException var4) {
/*     */       
/* 255 */       return null;
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\optifin\\util\ResUtils.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */