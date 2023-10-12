/*    */ package com.viaversion.viaversion.libs.kyori.adventure.nbt;
/*    */ 
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
/*    */ public interface DoubleBinaryTag
/*    */   extends NumberBinaryTag
/*    */ {
/*    */   @NotNull
/*    */   static DoubleBinaryTag doubleBinaryTag(double value) {
/* 43 */     return new DoubleBinaryTagImpl(value);
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
/*    */   static DoubleBinaryTag of(double value) {
/* 57 */     return new DoubleBinaryTagImpl(value);
/*    */   }
/*    */   
/*    */   @NotNull
/*    */   default BinaryTagType<DoubleBinaryTag> type() {
/* 62 */     return BinaryTagTypes.DOUBLE;
/*    */   }
/*    */   
/*    */   double value();
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\com\viaversion\viaversion\libs\kyori\adventure\nbt\DoubleBinaryTag.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */