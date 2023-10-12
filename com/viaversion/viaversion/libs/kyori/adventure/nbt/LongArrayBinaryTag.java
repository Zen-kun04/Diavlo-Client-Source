/*    */ package com.viaversion.viaversion.libs.kyori.adventure.nbt;
/*    */ 
/*    */ import java.util.Iterator;
/*    */ import java.util.PrimitiveIterator;
/*    */ import java.util.Spliterator;
/*    */ import java.util.function.LongConsumer;
/*    */ import java.util.stream.LongStream;
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
/*    */ public interface LongArrayBinaryTag
/*    */   extends ArrayBinaryTag, Iterable<Long>
/*    */ {
/*    */   @NotNull
/*    */   static LongArrayBinaryTag longArrayBinaryTag(long... value) {
/* 48 */     return new LongArrayBinaryTagImpl(value);
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
/*    */   static LongArrayBinaryTag of(long... value) {
/* 62 */     return new LongArrayBinaryTagImpl(value);
/*    */   }
/*    */   
/*    */   @NotNull
/*    */   default BinaryTagType<LongArrayBinaryTag> type() {
/* 67 */     return BinaryTagTypes.LONG_ARRAY;
/*    */   }
/*    */   
/*    */   long[] value();
/*    */   
/*    */   int size();
/*    */   
/*    */   long get(int paramInt);
/*    */   
/*    */   PrimitiveIterator.OfLong iterator();
/*    */   
/*    */   Spliterator.OfLong spliterator();
/*    */   
/*    */   @NotNull
/*    */   LongStream stream();
/*    */   
/*    */   void forEachLong(@NotNull LongConsumer paramLongConsumer);
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\com\viaversion\viaversion\libs\kyori\adventure\nbt\LongArrayBinaryTag.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */