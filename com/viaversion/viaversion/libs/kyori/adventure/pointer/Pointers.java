/*     */ package com.viaversion.viaversion.libs.kyori.adventure.pointer;
/*     */ 
/*     */ import com.viaversion.viaversion.libs.kyori.adventure.builder.AbstractBuilder;
/*     */ import com.viaversion.viaversion.libs.kyori.adventure.util.Buildable;
/*     */ import java.util.Optional;
/*     */ import java.util.function.Supplier;
/*     */ import org.jetbrains.annotations.Contract;
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
/*     */ public interface Pointers
/*     */   extends Buildable<Pointers, Pointers.Builder>
/*     */ {
/*     */   @Contract(pure = true)
/*     */   @NotNull
/*     */   static Pointers empty() {
/*  49 */     return PointersImpl.EMPTY;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Contract(pure = true)
/*     */   @NotNull
/*     */   static Builder builder() {
/*  61 */     return new PointersImpl.BuilderImpl();
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
/*     */   @NotNull
/*     */   <T> Optional<T> get(@NotNull Pointer<T> paramPointer);
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
/*     */   @Contract("_, null -> _; _, !null -> !null")
/*     */   @Nullable
/*     */   default <T> T getOrDefault(@NotNull Pointer<T> pointer, @Nullable T defaultValue) {
/*  88 */     return get(pointer).orElse(defaultValue);
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
/*     */   default <T> T getOrDefaultFrom(@NotNull Pointer<T> pointer, @NotNull Supplier<? extends T> defaultValue) {
/* 104 */     return get(pointer).orElseGet(defaultValue);
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
/*     */   <T> boolean supports(@NotNull Pointer<T> paramPointer);
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
/*     */   public static interface Builder
/*     */     extends AbstractBuilder<Pointers>, Buildable.Builder<Pointers>
/*     */   {
/*     */     @Contract("_, _ -> this")
/*     */     @NotNull
/*     */     default <T> Builder withStatic(@NotNull Pointer<T> pointer, @Nullable T value) {
/* 137 */       return withDynamic(pointer, () -> value);
/*     */     }
/*     */     
/*     */     @Contract("_, _ -> this")
/*     */     @NotNull
/*     */     <T> Builder withDynamic(@NotNull Pointer<T> param1Pointer, @NotNull Supplier<T> param1Supplier);
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\com\viaversion\viaversion\libs\kyori\adventure\pointer\Pointers.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */