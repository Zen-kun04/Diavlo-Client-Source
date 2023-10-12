/*     */ package com.viaversion.viaversion.libs.kyori.adventure.text;
/*     */ 
/*     */ import com.viaversion.viaversion.libs.kyori.adventure.translation.Translatable;
/*     */ import com.viaversion.viaversion.libs.kyori.examination.ExaminableProperty;
/*     */ import java.util.List;
/*     */ import java.util.Objects;
/*     */ import java.util.stream.Stream;
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
/*     */ public interface TranslatableComponent
/*     */   extends BuildableComponent<TranslatableComponent, TranslatableComponent.Builder>, ScopedComponent<TranslatableComponent>
/*     */ {
/*     */   @NotNull
/*     */   String key();
/*     */   
/*     */   @Contract(pure = true)
/*     */   @NotNull
/*     */   default TranslatableComponent key(@NotNull Translatable translatable) {
/*  82 */     return key(((Translatable)Objects.<Translatable>requireNonNull(translatable, "translatable")).translationKey());
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
/*     */   TranslatableComponent key(@NotNull String paramString);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @NotNull
/*     */   List<Component> args();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Contract(pure = true)
/*     */   @NotNull
/*     */   TranslatableComponent args(@NotNull ComponentLike... paramVarArgs);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Contract(pure = true)
/*     */   @NotNull
/*     */   TranslatableComponent args(@NotNull List<? extends ComponentLike> paramList);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Nullable
/*     */   String fallback();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Contract(pure = true)
/*     */   @NotNull
/*     */   TranslatableComponent fallback(@Nullable String paramString);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @NotNull
/*     */   default Stream<? extends ExaminableProperty> examinableProperties() {
/* 149 */     return Stream.concat(
/* 150 */         Stream.of(new ExaminableProperty[] {
/* 151 */             ExaminableProperty.of("key", key()), 
/* 152 */             ExaminableProperty.of("args", args()), 
/* 153 */             ExaminableProperty.of("fallback", fallback())
/*     */           
/* 155 */           }), super.examinableProperties());
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
/*     */   public static interface Builder
/*     */     extends ComponentBuilder<TranslatableComponent, Builder>
/*     */   {
/*     */     @Contract(pure = true)
/*     */     @NotNull
/*     */     default Builder key(@NotNull Translatable translatable) {
/* 174 */       return key(((Translatable)Objects.<Translatable>requireNonNull(translatable, "translatable")).translationKey());
/*     */     }
/*     */     
/*     */     @Contract("_ -> this")
/*     */     @NotNull
/*     */     Builder key(@NotNull String param1String);
/*     */     
/*     */     @Contract("_ -> this")
/*     */     @NotNull
/*     */     Builder args(@NotNull ComponentBuilder<?, ?> param1ComponentBuilder);
/*     */     
/*     */     @Contract("_ -> this")
/*     */     @NotNull
/*     */     Builder args(@NotNull ComponentBuilder<?, ?>... param1VarArgs);
/*     */     
/*     */     @Contract("_ -> this")
/*     */     @NotNull
/*     */     Builder args(@NotNull Component param1Component);
/*     */     
/*     */     @Contract("_ -> this")
/*     */     @NotNull
/*     */     Builder args(@NotNull ComponentLike... param1VarArgs);
/*     */     
/*     */     @Contract("_ -> this")
/*     */     @NotNull
/*     */     Builder args(@NotNull List<? extends ComponentLike> param1List);
/*     */     
/*     */     @Contract("_ -> this")
/*     */     @NotNull
/*     */     Builder fallback(@Nullable String param1String);
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\com\viaversion\viaversion\libs\kyori\adventure\text\TranslatableComponent.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */