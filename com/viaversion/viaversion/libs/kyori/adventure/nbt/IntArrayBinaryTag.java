/*    */ package com.viaversion.viaversion.libs.kyori.adventure.nbt;
/*    */ 
/*    */ import java.util.Iterator;
/*    */ import java.util.PrimitiveIterator;
/*    */ import java.util.Spliterator;
/*    */ import java.util.function.IntConsumer;
/*    */ import java.util.stream.IntStream;
/*    */ import org.jetbrains.annotations.ApiStatus.ScheduledForRemoval;
/*    */ import org.jetbrains.annotations.NotNull;
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
/*    */ public interface IntArrayBinaryTag
/*    */   extends ArrayBinaryTag, Iterable<Integer>
/*    */ {
/*    */   @NotNull
/*    */   static IntArrayBinaryTag intArrayBinaryTag(int... value) {
/* 48 */     return new IntArrayBinaryTagImpl(value);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   @Deprecated
/*    */   @ScheduledForRemoval(inVersion = "5.0.0")
/*    */   @NotNull
/*    */   static IntArrayBinaryTag of(int... value) {
/* 62 */     return new IntArrayBinaryTagImpl(value);
/*    */   }
/*    */   
/*    */   @NotNull
/*    */   default BinaryTagType<IntArrayBinaryTag> type() {
/* 67 */     return BinaryTagTypes.INT_ARRAY;
/*    */   }
/*    */   
/*    */   int[] value();
/*    */   
/*    */   int size();
/*    */   
/*    */   int get(int paramInt);
/*    */   
/*    */   PrimitiveIterator.OfInt iterator();
/*    */   
/*    */   Spliterator.OfInt spliterator();
/*    */   
/*    */   @NotNull
/*    */   IntStream stream();
/*    */   
/*    */   void forEachInt(@NotNull IntConsumer paramIntConsumer);
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\com\viaversion\viaversion\libs\kyori\adventure\nbt\IntArrayBinaryTag.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */