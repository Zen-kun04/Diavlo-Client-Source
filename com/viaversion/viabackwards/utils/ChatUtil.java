/*     */ package com.viaversion.viabackwards.utils;
/*     */ 
/*     */ import java.util.HashSet;
/*     */ import java.util.Objects;
/*     */ import java.util.Set;
/*     */ import java.util.regex.Pattern;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ChatUtil
/*     */ {
/*  26 */   private static final Pattern UNUSED_COLOR_PATTERN = Pattern.compile("(?>(?>§[0-fk-or])*(§r|\\Z))|(?>(?>§[0-f])*(§[0-f]))");
/*  27 */   private static final Pattern UNUSED_COLOR_PATTERN_PREFIX = Pattern.compile("(?>(?>§[0-fk-or])*(§r))|(?>(?>§[0-f])*(§[0-f]))");
/*     */   
/*     */   public static String removeUnusedColor(String legacy, char defaultColor) {
/*  30 */     return removeUnusedColor(legacy, defaultColor, false);
/*     */   }
/*     */   
/*     */   private static class ChatFormattingState {
/*     */     private final Set<Character> formatting;
/*     */     private final char defaultColor;
/*     */     private char color;
/*     */     
/*     */     private ChatFormattingState(char defaultColor) {
/*  39 */       this(new HashSet<>(), defaultColor, defaultColor);
/*     */     }
/*     */     
/*     */     public ChatFormattingState(Set<Character> formatting, char defaultColor, char color) {
/*  43 */       this.formatting = formatting;
/*  44 */       this.defaultColor = defaultColor;
/*  45 */       this.color = color;
/*     */     }
/*     */     
/*     */     private void setColor(char newColor) {
/*  49 */       this.formatting.clear();
/*  50 */       this.color = newColor;
/*     */     }
/*     */     
/*     */     public ChatFormattingState copy() {
/*  54 */       return new ChatFormattingState(new HashSet<>(this.formatting), this.defaultColor, this.color);
/*     */     }
/*     */     
/*     */     public void appendTo(StringBuilder builder) {
/*  58 */       builder.append('§').append(this.color);
/*  59 */       for (Character formatCharacter : this.formatting) {
/*  60 */         builder.append('§').append(formatCharacter);
/*     */       }
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean equals(Object o) {
/*  66 */       if (this == o) {
/*  67 */         return true;
/*     */       }
/*  69 */       if (o == null || getClass() != o.getClass()) {
/*  70 */         return false;
/*     */       }
/*  72 */       ChatFormattingState that = (ChatFormattingState)o;
/*  73 */       return (this.defaultColor == that.defaultColor && this.color == that.color && 
/*     */         
/*  75 */         Objects.equals(this.formatting, that.formatting));
/*     */     }
/*     */ 
/*     */     
/*     */     public int hashCode() {
/*  80 */       return Objects.hash(new Object[] { this.formatting, Character.valueOf(this.defaultColor), Character.valueOf(this.color) });
/*     */     }
/*     */     
/*     */     public void processNextControlChar(char controlChar) {
/*  84 */       if (controlChar == 'r') {
/*  85 */         setColor(this.defaultColor);
/*     */         return;
/*     */       } 
/*  88 */       if (controlChar == 'l' || controlChar == 'm' || controlChar == 'n' || controlChar == 'o') {
/*  89 */         this.formatting.add(Character.valueOf(controlChar));
/*     */         return;
/*     */       } 
/*  92 */       setColor(controlChar);
/*     */     }
/*     */   }
/*     */   
/*     */   public static String fromLegacy(String legacy, char defaultColor, int limit) {
/*  97 */     return fromLegacy(legacy, defaultColor, limit, false);
/*     */   }
/*     */   
/*     */   public static String fromLegacyPrefix(String legacy, char defaultColor, int limit) {
/* 101 */     return fromLegacy(legacy, defaultColor, limit, true);
/*     */   }
/*     */   
/*     */   public static String fromLegacy(String legacy, char defaultColor, int limit, boolean isPrefix) {
/* 105 */     legacy = removeUnusedColor(legacy, defaultColor, isPrefix);
/* 106 */     if (legacy.length() > limit) legacy = legacy.substring(0, limit); 
/* 107 */     if (legacy.endsWith("§")) legacy = legacy.substring(0, legacy.length() - 1); 
/* 108 */     return legacy;
/*     */   }
/*     */   
/*     */   public static String removeUnusedColor(String legacy, char defaultColor, boolean isPrefix) {
/* 112 */     if (legacy == null) return null; 
/* 113 */     Pattern pattern = isPrefix ? UNUSED_COLOR_PATTERN_PREFIX : UNUSED_COLOR_PATTERN;
/* 114 */     legacy = pattern.matcher(legacy).replaceAll("$1$2");
/* 115 */     StringBuilder builder = new StringBuilder();
/* 116 */     ChatFormattingState builderState = new ChatFormattingState(defaultColor);
/* 117 */     ChatFormattingState lastState = new ChatFormattingState(defaultColor);
/* 118 */     for (int i = 0; i < legacy.length(); i++) {
/* 119 */       char current = legacy.charAt(i);
/* 120 */       if (current != '§' || i == legacy.length() - 1) {
/* 121 */         if (!lastState.equals(builderState)) {
/* 122 */           lastState.appendTo(builder);
/* 123 */           builderState = lastState.copy();
/*     */         } 
/* 125 */         builder.append(current);
/*     */       } else {
/*     */         
/* 128 */         current = legacy.charAt(++i);
/* 129 */         lastState.processNextControlChar(current);
/*     */       } 
/* 131 */     }  return builder.toString();
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\com\viaversion\viabackward\\utils\ChatUtil.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */