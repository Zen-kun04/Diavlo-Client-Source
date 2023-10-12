/*    */ package net.minecraft.item;
/*    */ 
/*    */ import net.minecraft.entity.player.EntityPlayer;
/*    */ import net.minecraft.network.Packet;
/*    */ import net.minecraft.world.World;
/*    */ 
/*    */ public class ItemMapBase
/*    */   extends Item
/*    */ {
/*    */   public boolean isMap() {
/* 11 */     return true;
/*    */   }
/*    */ 
/*    */   
/*    */   public Packet createMapDataPacket(ItemStack stack, World worldIn, EntityPlayer player) {
/* 16 */     return null;
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\item\ItemMapBase.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */