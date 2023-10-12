/*    */ package net.minecraft.tileentity;
/*    */ 
/*    */ import net.minecraft.block.BlockDaylightDetector;
/*    */ import net.minecraft.util.ITickable;
/*    */ 
/*    */ public class TileEntityDaylightDetector
/*    */   extends TileEntity
/*    */   implements ITickable {
/*    */   public void update() {
/* 10 */     if (this.worldObj != null && !this.worldObj.isRemote && this.worldObj.getTotalWorldTime() % 20L == 0L) {
/*    */       
/* 12 */       this.blockType = getBlockType();
/*    */       
/* 14 */       if (this.blockType instanceof BlockDaylightDetector)
/*    */       {
/* 16 */         ((BlockDaylightDetector)this.blockType).updatePower(this.worldObj, this.pos);
/*    */       }
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\tileentity\TileEntityDaylightDetector.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */