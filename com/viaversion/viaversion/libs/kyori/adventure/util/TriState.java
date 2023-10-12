/*     */ package com.viaversion.viaversion.libs.kyori.adventure.util;
/*     */ 
/*     */ import java.util.function.BooleanSupplier;
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
/*     */ public enum TriState
/*     */ {
/*  41 */   NOT_SET,
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  47 */   FALSE,
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  53 */   TRUE;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   public Boolean toBoolean() {
/*  62 */     switch (this) { case TRUE:
/*  63 */         return Boolean.TRUE;
/*  64 */       case FALSE: return Boolean.FALSE; }
/*  65 */      return null;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean toBooleanOrElse(boolean other) {
/*  82 */     switch (this) { case TRUE:
/*  83 */         return true;
/*  84 */       case FALSE: return false; }
/*  85 */      return other;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean toBooleanOrElseGet(@NotNull BooleanSupplier supplier) {
/* 102 */     switch (this) { case TRUE:
/* 103 */         return true;
/* 104 */       case FALSE: return false; }
/* 105 */      return supplier.getAsBoolean();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @NotNull
/*     */   public static TriState byBoolean(boolean value) {
/* 117 */     return value ? TRUE : FALSE;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @NotNull
/*     */   public static TriState byBoolean(@Nullable Boolean value) {
/* 128 */     return (value == null) ? NOT_SET : byBoolean(value.booleanValue());
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\com\viaversion\viaversion\libs\kyori\adventur\\util\TriState.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */