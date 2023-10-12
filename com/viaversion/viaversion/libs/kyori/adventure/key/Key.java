/*     */ package com.viaversion.viaversion.libs.kyori.adventure.key;
/*     */ 
/*     */ import com.viaversion.viaversion.libs.kyori.examination.Examinable;
/*     */ import com.viaversion.viaversion.libs.kyori.examination.ExaminableProperty;
/*     */ import java.util.Comparator;
/*     */ import java.util.OptionalInt;
/*     */ import java.util.stream.Stream;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public interface Key
/*     */   extends Comparable<Key>, Examinable, Namespaced, Keyed
/*     */ {
/*     */   public static final String MINECRAFT_NAMESPACE = "minecraft";
/*     */   public static final char DEFAULT_SEPARATOR = ':';
/*     */   
/*     */   @NotNull
/*     */   static Key key(@NotNull @KeyPattern String string) {
/*  88 */     return key(string, ':');
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
/*     */   @NotNull
/*     */   static Key key(@NotNull String string, char character) {
/* 108 */     int index = string.indexOf(character);
/* 109 */     String namespace = (index >= 1) ? string.substring(0, index) : "minecraft";
/* 110 */     String value = (index >= 0) ? string.substring(index + 1) : string;
/* 111 */     return key(namespace, value);
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
/*     */   static Key key(@NotNull Namespaced namespaced, @NotNull @Value String value) {
/* 124 */     return key(namespaced.namespace(), value);
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
/*     */   static Key key(@NotNull @Namespace String namespace, @NotNull @Value String value) {
/* 137 */     return new KeyImpl(namespace, value);
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
/*     */   static Comparator<? super Key> comparator() {
/* 149 */     return KeyImpl.COMPARATOR;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static boolean parseable(@Nullable String string) {
/* 160 */     if (string == null) {
/* 161 */       return false;
/*     */     }
/* 163 */     int index = string.indexOf(':');
/* 164 */     String namespace = (index >= 1) ? string.substring(0, index) : "minecraft";
/* 165 */     String value = (index >= 0) ? string.substring(index + 1) : string;
/* 166 */     return (parseableNamespace(namespace) && parseableValue(value));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static boolean parseableNamespace(@NotNull String namespace) {
/* 177 */     return !checkNamespace(namespace).isPresent();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @NotNull
/*     */   static OptionalInt checkNamespace(@NotNull String namespace) {
/* 188 */     for (int i = 0, length = namespace.length(); i < length; i++) {
/* 189 */       if (!allowedInNamespace(namespace.charAt(i))) {
/* 190 */         return OptionalInt.of(i);
/*     */       }
/*     */     } 
/* 193 */     return OptionalInt.empty();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static boolean parseableValue(@NotNull String value) {
/* 204 */     return !checkValue(value).isPresent();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @NotNull
/*     */   static OptionalInt checkValue(@NotNull String value) {
/* 215 */     for (int i = 0, length = value.length(); i < length; i++) {
/* 216 */       if (!allowedInValue(value.charAt(i))) {
/* 217 */         return OptionalInt.of(i);
/*     */       }
/*     */     } 
/* 220 */     return OptionalInt.empty();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static boolean allowedInNamespace(char character) {
/* 231 */     return KeyImpl.allowedInNamespace(character);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static boolean allowedInValue(char character) {
/* 242 */     return KeyImpl.allowedInValue(character);
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
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @NotNull
/*     */   default Stream<? extends ExaminableProperty> examinableProperties() {
/* 272 */     return Stream.of(new ExaminableProperty[] {
/* 273 */           ExaminableProperty.of("namespace", namespace()), 
/* 274 */           ExaminableProperty.of("value", value())
/*     */         });
/*     */   }
/*     */ 
/*     */   
/*     */   default int compareTo(@NotNull Key that) {
/* 280 */     return comparator().compare(this, that);
/*     */   }
/*     */   
/*     */   @NotNull
/*     */   default Key key() {
/* 285 */     return this;
/*     */   }
/*     */   
/*     */   @NotNull
/*     */   @Namespace
/*     */   String namespace();
/*     */   
/*     */   @NotNull
/*     */   @Value
/*     */   String value();
/*     */   
/*     */   @NotNull
/*     */   String asString();
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\com\viaversion\viaversion\libs\kyori\adventure\key\Key.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */