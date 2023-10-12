/*    */ package com.viaversion.viaversion.libs.kyori.adventure.translation;
/*    */ 
/*    */ import com.viaversion.viaversion.libs.kyori.adventure.key.Key;
/*    */ import com.viaversion.viaversion.libs.kyori.adventure.text.Component;
/*    */ import com.viaversion.viaversion.libs.kyori.adventure.text.TranslatableComponent;
/*    */ import com.viaversion.viaversion.libs.kyori.adventure.text.renderer.TranslatableComponentRenderer;
/*    */ import com.viaversion.viaversion.libs.kyori.examination.ExaminableProperty;
/*    */ import java.text.MessageFormat;
/*    */ import java.util.Collections;
/*    */ import java.util.Locale;
/*    */ import java.util.Objects;
/*    */ import java.util.Set;
/*    */ import java.util.concurrent.ConcurrentHashMap;
/*    */ import java.util.stream.Stream;
/*    */ import org.jetbrains.annotations.NotNull;
/*    */ import org.jetbrains.annotations.Nullable;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ final class GlobalTranslatorImpl
/*    */   implements GlobalTranslator
/*    */ {
/* 43 */   private static final Key NAME = Key.key("adventure", "global");
/* 44 */   static final GlobalTranslatorImpl INSTANCE = new GlobalTranslatorImpl();
/* 45 */   final TranslatableComponentRenderer<Locale> renderer = TranslatableComponentRenderer.usingTranslationSource(this);
/* 46 */   private final Set<Translator> sources = Collections.newSetFromMap(new ConcurrentHashMap<>());
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   @NotNull
/*    */   public Key name() {
/* 53 */     return NAME;
/*    */   }
/*    */   
/*    */   @NotNull
/*    */   public Iterable<? extends Translator> sources() {
/* 58 */     return Collections.unmodifiableSet(this.sources);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean addSource(@NotNull Translator source) {
/* 63 */     Objects.requireNonNull(source, "source");
/* 64 */     if (source == this) throw new IllegalArgumentException("GlobalTranslationSource"); 
/* 65 */     return this.sources.add(source);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean removeSource(@NotNull Translator source) {
/* 70 */     Objects.requireNonNull(source, "source");
/* 71 */     return this.sources.remove(source);
/*    */   }
/*    */   
/*    */   @Nullable
/*    */   public MessageFormat translate(@NotNull String key, @NotNull Locale locale) {
/* 76 */     Objects.requireNonNull(key, "key");
/* 77 */     Objects.requireNonNull(locale, "locale");
/* 78 */     for (Translator source : this.sources) {
/* 79 */       MessageFormat translation = source.translate(key, locale);
/* 80 */       if (translation != null) return translation; 
/*    */     } 
/* 82 */     return null;
/*    */   }
/*    */   
/*    */   @Nullable
/*    */   public Component translate(@NotNull TranslatableComponent component, @NotNull Locale locale) {
/* 87 */     Objects.requireNonNull(component, "component");
/* 88 */     Objects.requireNonNull(locale, "locale");
/* 89 */     for (Translator source : this.sources) {
/* 90 */       Component translation = source.translate(component, locale);
/* 91 */       if (translation != null) return translation; 
/*    */     } 
/* 93 */     return null;
/*    */   }
/*    */   
/*    */   @NotNull
/*    */   public Stream<? extends ExaminableProperty> examinableProperties() {
/* 98 */     return Stream.of(ExaminableProperty.of("sources", this.sources));
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\com\viaversion\viaversion\libs\kyori\adventure\translation\GlobalTranslatorImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */