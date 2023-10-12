/*    */ package net.minecraft.block;
/*    */ 
/*    */ import net.minecraft.util.BlockPos;
/*    */ 
/*    */ 
/*    */ public class BlockEventData
/*    */ {
/*    */   private BlockPos position;
/*    */   private Block blockType;
/*    */   private int eventID;
/*    */   private int eventParameter;
/*    */   
/*    */   public BlockEventData(BlockPos pos, Block blockType, int eventId, int p_i45756_4_) {
/* 14 */     this.position = pos;
/* 15 */     this.eventID = eventId;
/* 16 */     this.eventParameter = p_i45756_4_;
/* 17 */     this.blockType = blockType;
/*    */   }
/*    */ 
/*    */   
/*    */   public BlockPos getPosition() {
/* 22 */     return this.position;
/*    */   }
/*    */ 
/*    */   
/*    */   public int getEventID() {
/* 27 */     return this.eventID;
/*    */   }
/*    */ 
/*    */   
/*    */   public int getEventParameter() {
/* 32 */     return this.eventParameter;
/*    */   }
/*    */ 
/*    */   
/*    */   public Block getBlock() {
/* 37 */     return this.blockType;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean equals(Object p_equals_1_) {
/* 42 */     if (!(p_equals_1_ instanceof BlockEventData))
/*    */     {
/* 44 */       return false;
/*    */     }
/*    */ 
/*    */     
/* 48 */     BlockEventData blockeventdata = (BlockEventData)p_equals_1_;
/* 49 */     return (this.position.equals(blockeventdata.position) && this.eventID == blockeventdata.eventID && this.eventParameter == blockeventdata.eventParameter && this.blockType == blockeventdata.blockType);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public String toString() {
/* 55 */     return "TE(" + this.position + ")," + this.eventID + "," + this.eventParameter + "," + this.blockType;
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\block\BlockEventData.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */