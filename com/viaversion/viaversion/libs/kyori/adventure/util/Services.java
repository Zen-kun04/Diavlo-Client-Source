/*     */ package com.viaversion.viaversion.libs.kyori.adventure.util;
/*     */ 
/*     */ import com.viaversion.viaversion.libs.kyori.adventure.internal.properties.AdventureProperties;
/*     */ import java.util.Iterator;
/*     */ import java.util.Optional;
/*     */ import java.util.ServiceLoader;
/*     */ import org.jetbrains.annotations.NotNull;
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
/*     */ public final class Services
/*     */ {
/*  38 */   private static final boolean SERVICE_LOAD_FAILURES_ARE_FATAL = Boolean.TRUE.equals(AdventureProperties.SERVICE_LOAD_FAILURES_ARE_FATAL.value());
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
/*     */   @NotNull
/*     */   public static <P> Optional<P> service(@NotNull Class<P> type) {
/*  52 */     ServiceLoader<P> loader = Services0.loader(type);
/*  53 */     Iterator<P> it = loader.iterator();
/*  54 */     while (it.hasNext()) {
/*     */       P instance;
/*     */       try {
/*  57 */         instance = it.next();
/*  58 */       } catch (Throwable t) {
/*  59 */         if (SERVICE_LOAD_FAILURES_ARE_FATAL) {
/*  60 */           throw new IllegalStateException("Encountered an exception loading service " + type, t);
/*     */         }
/*     */         
/*     */         continue;
/*     */       } 
/*  65 */       if (it.hasNext()) {
/*  66 */         throw new IllegalStateException("Expected to find one service " + type + ", found multiple");
/*     */       }
/*  68 */       return Optional.of(instance);
/*     */     } 
/*  70 */     return Optional.empty();
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @NotNull
/*     */   public static <P> Optional<P> serviceWithFallback(@NotNull Class<P> type) {
/*  96 */     ServiceLoader<P> loader = Services0.loader(type);
/*  97 */     Iterator<P> it = loader.iterator();
/*  98 */     P firstFallback = null;
/*     */     
/* 100 */     while (it.hasNext()) {
/*     */       P instance;
/*     */       
/*     */       try {
/* 104 */         instance = it.next();
/* 105 */       } catch (Throwable t) {
/* 106 */         if (SERVICE_LOAD_FAILURES_ARE_FATAL) {
/* 107 */           throw new IllegalStateException("Encountered an exception loading service " + type, t);
/*     */         }
/*     */         
/*     */         continue;
/*     */       } 
/*     */       
/* 113 */       if (instance instanceof Fallback) {
/* 114 */         if (firstFallback == null)
/* 115 */           firstFallback = instance; 
/*     */         continue;
/*     */       } 
/* 118 */       return Optional.of(instance);
/*     */     } 
/*     */ 
/*     */     
/* 122 */     return Optional.ofNullable(firstFallback);
/*     */   }
/*     */   
/*     */   public static interface Fallback {}
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\com\viaversion\viaversion\libs\kyori\adventur\\util\Services.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */