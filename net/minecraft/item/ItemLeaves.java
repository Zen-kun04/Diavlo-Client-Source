/*    */ package net.minecraft.item;
/*    */ 
/*    */ import net.minecraft.block.Block;
/*    */ import net.minecraft.block.BlockLeaves;
/*    */ 
/*    */ public class ItemLeaves
/*    */   extends ItemBlock {
/*    */   private final BlockLeaves leaves;
/*    */   
/*    */   public ItemLeaves(BlockLeaves block) {
/* 11 */     super((Block)block);
/* 12 */     this.leaves = block;
/* 13 */     setMaxDamage(0);
/* 14 */     setHasSubtypes(true);
/*    */   }
/*    */ 
/*    */   
/*    */   public int getMetadata(int damage) {
/* 19 */     return damage | 0x4;
/*    */   }
/*    */ 
/*    */   
/*    */   public int getColorFromItemStack(ItemStack stack, int renderPass) {
/* 24 */     return this.leaves.getRenderColor(this.leaves.getStateFromMeta(stack.getMetadata()));
/*    */   }
/*    */ 
/*    */   
/*    */   public String getUnlocalizedName(ItemStack stack) {
/* 29 */     return getUnlocalizedName() + "." + this.leaves.getWoodType(stack.getMetadata()).getUnlocalizedName();
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\item\ItemLeaves.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */