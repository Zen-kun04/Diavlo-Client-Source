/*     */ package com.viaversion.viaversion.libs.fastutil.ints;
/*     */ 
/*     */ import com.viaversion.viaversion.libs.fastutil.Function;
/*     */ import com.viaversion.viaversion.libs.fastutil.bytes.Byte2IntFunction;
/*     */ import com.viaversion.viaversion.libs.fastutil.bytes.Byte2ObjectFunction;
/*     */ import com.viaversion.viaversion.libs.fastutil.chars.Char2IntFunction;
/*     */ import com.viaversion.viaversion.libs.fastutil.chars.Char2ObjectFunction;
/*     */ import com.viaversion.viaversion.libs.fastutil.doubles.Double2IntFunction;
/*     */ import com.viaversion.viaversion.libs.fastutil.doubles.Double2ObjectFunction;
/*     */ import com.viaversion.viaversion.libs.fastutil.floats.Float2IntFunction;
/*     */ import com.viaversion.viaversion.libs.fastutil.floats.Float2ObjectFunction;
/*     */ import com.viaversion.viaversion.libs.fastutil.longs.Long2IntFunction;
/*     */ import com.viaversion.viaversion.libs.fastutil.longs.Long2ObjectFunction;
/*     */ import com.viaversion.viaversion.libs.fastutil.objects.Object2ByteFunction;
/*     */ import com.viaversion.viaversion.libs.fastutil.objects.Object2CharFunction;
/*     */ import com.viaversion.viaversion.libs.fastutil.objects.Object2DoubleFunction;
/*     */ import com.viaversion.viaversion.libs.fastutil.objects.Object2FloatFunction;
/*     */ import com.viaversion.viaversion.libs.fastutil.objects.Object2IntFunction;
/*     */ import com.viaversion.viaversion.libs.fastutil.objects.Object2LongFunction;
/*     */ import com.viaversion.viaversion.libs.fastutil.objects.Object2ObjectFunction;
/*     */ import com.viaversion.viaversion.libs.fastutil.objects.Object2ReferenceFunction;
/*     */ import com.viaversion.viaversion.libs.fastutil.objects.Object2ShortFunction;
/*     */ import com.viaversion.viaversion.libs.fastutil.objects.Reference2IntFunction;
/*     */ import com.viaversion.viaversion.libs.fastutil.objects.Reference2ObjectFunction;
/*     */ import com.viaversion.viaversion.libs.fastutil.shorts.Short2IntFunction;
/*     */ import com.viaversion.viaversion.libs.fastutil.shorts.Short2ObjectFunction;
/*     */ import java.util.function.Function;
/*     */ import java.util.function.IntFunction;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @FunctionalInterface
/*     */ public interface Int2ObjectFunction<V>
/*     */   extends Function<Integer, V>, IntFunction<V>
/*     */ {
/*     */   default V apply(int operand) {
/*  60 */     return get(operand);
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
/*     */   default V put(int key, V value) {
/*  73 */     throw new UnsupportedOperationException();
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
/*     */   default V getOrDefault(int key, V defaultValue) {
/*     */     V v;
/*  99 */     return ((v = get(key)) != defaultReturnValue() || containsKey(key)) ? v : defaultValue;
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
/*     */   default V remove(int key) {
/* 111 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   default V put(Integer key, V value) {
/* 122 */     int k = key.intValue();
/* 123 */     boolean containsKey = containsKey(k);
/* 124 */     V v = put(k, value);
/* 125 */     return containsKey ? v : null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   default V get(Object key) {
/* 136 */     if (key == null) return null; 
/* 137 */     int k = ((Integer)key).intValue(); V v; return ((
/*     */       
/* 139 */       v = get(k)) != defaultReturnValue() || containsKey(k)) ? v : null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   default V getOrDefault(Object key, V defaultValue) {
/* 150 */     if (key == null) return defaultValue; 
/* 151 */     int k = ((Integer)key).intValue();
/* 152 */     V v = get(k);
/* 153 */     return (v != defaultReturnValue() || containsKey(k)) ? v : defaultValue;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   default V remove(Object key) {
/* 164 */     if (key == null) return null; 
/* 165 */     int k = ((Integer)key).intValue();
/* 166 */     return containsKey(k) ? remove(k) : null;
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
/*     */   default boolean containsKey(int key) {
/* 181 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   default boolean containsKey(Object key) {
/* 192 */     return (key == null) ? false : containsKey(((Integer)key).intValue());
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
/*     */   default void defaultReturnValue(V rv) {
/* 206 */     throw new UnsupportedOperationException();
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
/*     */   default V defaultReturnValue() {
/* 219 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   default <T> Function<T, V> compose(Function<? super T, ? extends Integer> before) {
/* 230 */     return super.compose(before);
/*     */   }
/*     */   
/*     */   default Int2ByteFunction andThenByte(Object2ByteFunction<V> after) {
/* 234 */     return k -> after.getByte(get(k));
/*     */   }
/*     */   
/*     */   default Byte2ObjectFunction<V> composeByte(Byte2IntFunction before) {
/* 238 */     return k -> get(before.get(k));
/*     */   }
/*     */   
/*     */   default Int2ShortFunction andThenShort(Object2ShortFunction<V> after) {
/* 242 */     return k -> after.getShort(get(k));
/*     */   }
/*     */   
/*     */   default Short2ObjectFunction<V> composeShort(Short2IntFunction before) {
/* 246 */     return k -> get(before.get(k));
/*     */   }
/*     */   
/*     */   default Int2IntFunction andThenInt(Object2IntFunction<V> after) {
/* 250 */     return k -> after.getInt(get(k));
/*     */   }
/*     */   
/*     */   default Int2ObjectFunction<V> composeInt(Int2IntFunction before) {
/* 254 */     return k -> get(before.get(k));
/*     */   }
/*     */   
/*     */   default Int2LongFunction andThenLong(Object2LongFunction<V> after) {
/* 258 */     return k -> after.getLong(get(k));
/*     */   }
/*     */   
/*     */   default Long2ObjectFunction<V> composeLong(Long2IntFunction before) {
/* 262 */     return k -> get(before.get(k));
/*     */   }
/*     */   
/*     */   default Int2CharFunction andThenChar(Object2CharFunction<V> after) {
/* 266 */     return k -> after.getChar(get(k));
/*     */   }
/*     */   
/*     */   default Char2ObjectFunction<V> composeChar(Char2IntFunction before) {
/* 270 */     return k -> get(before.get(k));
/*     */   }
/*     */   
/*     */   default Int2FloatFunction andThenFloat(Object2FloatFunction<V> after) {
/* 274 */     return k -> after.getFloat(get(k));
/*     */   }
/*     */   
/*     */   default Float2ObjectFunction<V> composeFloat(Float2IntFunction before) {
/* 278 */     return k -> get(before.get(k));
/*     */   }
/*     */   
/*     */   default Int2DoubleFunction andThenDouble(Object2DoubleFunction<V> after) {
/* 282 */     return k -> after.getDouble(get(k));
/*     */   }
/*     */   
/*     */   default Double2ObjectFunction<V> composeDouble(Double2IntFunction before) {
/* 286 */     return k -> get(before.get(k));
/*     */   }
/*     */   
/*     */   default <T> Int2ObjectFunction<T> andThenObject(Object2ObjectFunction<? super V, ? extends T> after) {
/* 290 */     return k -> after.get(get(k));
/*     */   }
/*     */   
/*     */   default <T> Object2ObjectFunction<T, V> composeObject(Object2IntFunction<? super T> before) {
/* 294 */     return k -> get(before.getInt(k));
/*     */   }
/*     */   
/*     */   default <T> Int2ReferenceFunction<T> andThenReference(Object2ReferenceFunction<? super V, ? extends T> after) {
/* 298 */     return k -> after.get(get(k));
/*     */   }
/*     */   
/*     */   default <T> Reference2ObjectFunction<T, V> composeReference(Reference2IntFunction<? super T> before) {
/* 302 */     return k -> get(before.getInt(k));
/*     */   }
/*     */   
/*     */   V get(int paramInt);
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\com\viaversion\viaversion\libs\fastutil\ints\Int2ObjectFunction.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */