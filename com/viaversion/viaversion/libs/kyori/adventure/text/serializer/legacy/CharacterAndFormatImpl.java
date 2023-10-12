/*     */ package com.viaversion.viaversion.libs.kyori.adventure.text.serializer.legacy;
/*     */ 
/*     */ import com.viaversion.viaversion.libs.kyori.adventure.internal.Internals;
/*     */ import com.viaversion.viaversion.libs.kyori.adventure.text.format.TextFormat;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import java.util.Objects;
/*     */ import org.jetbrains.annotations.NotNull;
/*     */ import org.jetbrains.annotations.Nullable;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ final class CharacterAndFormatImpl
/*     */   implements CharacterAndFormat
/*     */ {
/*     */   private final char character;
/*     */   private final TextFormat format;
/*     */   
/*     */   CharacterAndFormatImpl(char character, @NotNull TextFormat format) {
/*  41 */     this.character = character;
/*  42 */     this.format = Objects.<TextFormat>requireNonNull(format, "format");
/*     */   }
/*     */ 
/*     */   
/*     */   public char character() {
/*  47 */     return this.character;
/*     */   }
/*     */   
/*     */   @NotNull
/*     */   public TextFormat format() {
/*  52 */     return this.format;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(@Nullable Object other) {
/*  57 */     if (this == other) return true; 
/*  58 */     if (!(other instanceof CharacterAndFormatImpl)) return false; 
/*  59 */     CharacterAndFormatImpl that = (CharacterAndFormatImpl)other;
/*  60 */     return (this.character == that.character && this.format
/*  61 */       .equals(that.format));
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/*  66 */     int result = this.character;
/*  67 */     result = 31 * result + this.format.hashCode();
/*  68 */     return result;
/*     */   }
/*     */   
/*     */   @NotNull
/*     */   public String toString() {
/*  73 */     return Internals.toString(this);
/*     */   }
/*     */   
/*     */   static final class Defaults {
/*  77 */     static final List<CharacterAndFormat> DEFAULTS = createDefaults();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     static List<CharacterAndFormat> createDefaults() {
/*  84 */       List<CharacterAndFormat> formats = new ArrayList<>(22);
/*     */       
/*  86 */       formats.add(CharacterAndFormat.BLACK);
/*  87 */       formats.add(CharacterAndFormat.DARK_BLUE);
/*  88 */       formats.add(CharacterAndFormat.DARK_GREEN);
/*  89 */       formats.add(CharacterAndFormat.DARK_AQUA);
/*  90 */       formats.add(CharacterAndFormat.DARK_RED);
/*  91 */       formats.add(CharacterAndFormat.DARK_PURPLE);
/*  92 */       formats.add(CharacterAndFormat.GOLD);
/*  93 */       formats.add(CharacterAndFormat.GRAY);
/*  94 */       formats.add(CharacterAndFormat.DARK_GRAY);
/*  95 */       formats.add(CharacterAndFormat.BLUE);
/*  96 */       formats.add(CharacterAndFormat.GREEN);
/*  97 */       formats.add(CharacterAndFormat.AQUA);
/*  98 */       formats.add(CharacterAndFormat.RED);
/*  99 */       formats.add(CharacterAndFormat.LIGHT_PURPLE);
/* 100 */       formats.add(CharacterAndFormat.YELLOW);
/* 101 */       formats.add(CharacterAndFormat.WHITE);
/*     */       
/* 103 */       formats.add(CharacterAndFormat.OBFUSCATED);
/* 104 */       formats.add(CharacterAndFormat.BOLD);
/* 105 */       formats.add(CharacterAndFormat.STRIKETHROUGH);
/* 106 */       formats.add(CharacterAndFormat.UNDERLINED);
/* 107 */       formats.add(CharacterAndFormat.ITALIC);
/*     */       
/* 109 */       formats.add(CharacterAndFormat.RESET);
/*     */       
/* 111 */       return Collections.unmodifiableList(formats);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\com\viaversion\viaversion\libs\kyori\adventure\text\serializer\legacy\CharacterAndFormatImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */