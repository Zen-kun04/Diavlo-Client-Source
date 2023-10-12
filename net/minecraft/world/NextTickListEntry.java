/*    */ package net.minecraft.world;
/*    */ 
/*    */ import net.minecraft.block.Block;
/*    */ import net.minecraft.util.BlockPos;
/*    */ 
/*    */ public class NextTickListEntry
/*    */   implements Comparable<NextTickListEntry>
/*    */ {
/*    */   private static long nextTickEntryID;
/*    */   private final Block block;
/*    */   public final BlockPos position;
/*    */   public long scheduledTime;
/*    */   public int priority;
/*    */   private long tickEntryID;
/*    */   
/*    */   public NextTickListEntry(BlockPos positionIn, Block blockIn) {
/* 17 */     this.tickEntryID = nextTickEntryID++;
/* 18 */     this.position = positionIn;
/* 19 */     this.block = blockIn;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean equals(Object p_equals_1_) {
/* 24 */     if (!(p_equals_1_ instanceof NextTickListEntry))
/*    */     {
/* 26 */       return false;
/*    */     }
/*    */ 
/*    */     
/* 30 */     NextTickListEntry nextticklistentry = (NextTickListEntry)p_equals_1_;
/* 31 */     return (this.position.equals(nextticklistentry.position) && Block.isEqualTo(this.block, nextticklistentry.block));
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public int hashCode() {
/* 37 */     return this.position.hashCode();
/*    */   }
/*    */ 
/*    */   
/*    */   public NextTickListEntry setScheduledTime(long scheduledTimeIn) {
/* 42 */     this.scheduledTime = scheduledTimeIn;
/* 43 */     return this;
/*    */   }
/*    */ 
/*    */   
/*    */   public void setPriority(int priorityIn) {
/* 48 */     this.priority = priorityIn;
/*    */   }
/*    */ 
/*    */   
/*    */   public int compareTo(NextTickListEntry p_compareTo_1_) {
/* 53 */     return (this.scheduledTime < p_compareTo_1_.scheduledTime) ? -1 : ((this.scheduledTime > p_compareTo_1_.scheduledTime) ? 1 : ((this.priority != p_compareTo_1_.priority) ? (this.priority - p_compareTo_1_.priority) : ((this.tickEntryID < p_compareTo_1_.tickEntryID) ? -1 : ((this.tickEntryID > p_compareTo_1_.tickEntryID) ? 1 : 0))));
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 58 */     return Block.getIdFromBlock(this.block) + ": " + this.position + ", " + this.scheduledTime + ", " + this.priority + ", " + this.tickEntryID;
/*    */   }
/*    */ 
/*    */   
/*    */   public Block getBlock() {
/* 63 */     return this.block;
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\world\NextTickListEntry.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */