/*     */ package net.optifine.shaders.config;
/*     */ import java.io.BufferedReader;
/*     */ import java.io.ByteArrayInputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.StringReader;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import net.minecraft.src.Config;
/*     */ import net.optifine.shaders.Shaders;
/*     */ 
/*     */ public class MacroProcessor {
/*     */   public static InputStream process(InputStream in, String path) throws IOException {
/*  16 */     String s = Config.readInputStream(in, "ASCII");
/*  17 */     String s1 = getMacroHeader(s);
/*     */     
/*  19 */     if (!s1.isEmpty()) {
/*     */       
/*  21 */       s = s1 + s;
/*     */       
/*  23 */       if (Shaders.saveFinalShaders) {
/*     */         
/*  25 */         String s2 = path.replace(':', '/') + ".pre";
/*  26 */         Shaders.saveShader(s2, s);
/*     */       } 
/*     */       
/*  29 */       s = process(s);
/*     */     } 
/*     */     
/*  32 */     if (Shaders.saveFinalShaders) {
/*     */       
/*  34 */       String s3 = path.replace(':', '/');
/*  35 */       Shaders.saveShader(s3, s);
/*     */     } 
/*     */     
/*  38 */     byte[] abyte = s.getBytes("ASCII");
/*  39 */     ByteArrayInputStream bytearrayinputstream = new ByteArrayInputStream(abyte);
/*  40 */     return bytearrayinputstream;
/*     */   }
/*     */ 
/*     */   
/*     */   public static String process(String strIn) throws IOException {
/*  45 */     StringReader stringreader = new StringReader(strIn);
/*  46 */     BufferedReader bufferedreader = new BufferedReader(stringreader);
/*  47 */     MacroState macrostate = new MacroState();
/*  48 */     StringBuilder stringbuilder = new StringBuilder();
/*     */ 
/*     */     
/*     */     while (true) {
/*  52 */       String s = bufferedreader.readLine();
/*     */       
/*  54 */       if (s == null) {
/*     */         
/*  56 */         s = stringbuilder.toString();
/*  57 */         return s;
/*     */       } 
/*     */       
/*  60 */       if (macrostate.processLine(s) && !MacroState.isMacroLine(s)) {
/*     */         
/*  62 */         stringbuilder.append(s);
/*  63 */         stringbuilder.append("\n");
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private static String getMacroHeader(String str) throws IOException {
/*  70 */     StringBuilder stringbuilder = new StringBuilder();
/*  71 */     List<ShaderOption> list = null;
/*  72 */     List<ShaderMacro> list1 = null;
/*  73 */     StringReader stringreader = new StringReader(str);
/*  74 */     BufferedReader bufferedreader = new BufferedReader(stringreader);
/*     */ 
/*     */     
/*     */     while (true) {
/*  78 */       String s = bufferedreader.readLine();
/*     */       
/*  80 */       if (s == null)
/*     */       {
/*  82 */         return stringbuilder.toString();
/*     */       }
/*     */       
/*  85 */       if (MacroState.isMacroLine(s)) {
/*     */         
/*  87 */         if (stringbuilder.length() == 0)
/*     */         {
/*  89 */           stringbuilder.append(ShaderMacros.getFixedMacroLines());
/*     */         }
/*     */         
/*  92 */         if (list1 == null)
/*     */         {
/*  94 */           list1 = new ArrayList<>(Arrays.asList(ShaderMacros.getExtensions()));
/*     */         }
/*     */         
/*  97 */         Iterator<ShaderMacro> iterator = list1.iterator();
/*     */         
/*  99 */         while (iterator.hasNext()) {
/*     */           
/* 101 */           ShaderMacro shadermacro = iterator.next();
/*     */           
/* 103 */           if (s.contains(shadermacro.getName())) {
/*     */             
/* 105 */             stringbuilder.append(shadermacro.getSourceLine());
/* 106 */             stringbuilder.append("\n");
/* 107 */             iterator.remove();
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private static List<ShaderOption> getMacroOptions() {
/* 116 */     List<ShaderOption> list = new ArrayList<>();
/* 117 */     ShaderOption[] ashaderoption = Shaders.getShaderPackOptions();
/*     */     
/* 119 */     for (int i = 0; i < ashaderoption.length; i++) {
/*     */       
/* 121 */       ShaderOption shaderoption = ashaderoption[i];
/* 122 */       String s = shaderoption.getSourceLine();
/*     */       
/* 124 */       if (s != null && s.startsWith("#"))
/*     */       {
/* 126 */         list.add(shaderoption);
/*     */       }
/*     */     } 
/*     */     
/* 130 */     return list;
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\optifine\shaders\config\MacroProcessor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */