/*     */ package com.viaversion.viaversion.libs.kyori.adventure.translation;
/*     */ 
/*     */ import com.viaversion.viaversion.libs.kyori.adventure.key.Key;
/*     */ import com.viaversion.viaversion.libs.kyori.adventure.text.Component;
/*     */ import com.viaversion.viaversion.libs.kyori.adventure.text.TranslatableComponent;
/*     */ import java.text.MessageFormat;
/*     */ import java.util.Locale;
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
/*     */ public interface Translator
/*     */ {
/*     */   @Nullable
/*     */   static Locale parseLocale(@NotNull String string) {
/*  59 */     String[] segments = string.split("_", 3);
/*  60 */     int length = segments.length;
/*  61 */     if (length == 1)
/*  62 */       return new Locale(string); 
/*  63 */     if (length == 2)
/*  64 */       return new Locale(segments[0], segments[1]); 
/*  65 */     if (length == 3) {
/*  66 */       return new Locale(segments[0], segments[1], segments[2]);
/*     */     }
/*  68 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @NotNull
/*     */   Key name();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   MessageFormat translate(@NotNull String paramString, @NotNull Locale paramLocale);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   default Component translate(@NotNull TranslatableComponent component, @NotNull Locale locale) {
/* 103 */     return null;
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\com\viaversion\viaversion\libs\kyori\adventure\translation\Translator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */