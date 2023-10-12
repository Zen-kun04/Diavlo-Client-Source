/*    */ package net.minecraft.tileentity;
/*    */ 
/*    */ public class TileEntityDropper
/*    */   extends TileEntityDispenser
/*    */ {
/*    */   public String getName() {
/*  7 */     return hasCustomName() ? this.customName : "container.dropper";
/*    */   }
/*    */ 
/*    */   
/*    */   public String getGuiID() {
/* 12 */     return "minecraft:dropper";
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\tileentity\TileEntityDropper.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */