/*    */ package net.minecraft.item;
/*    */ 
/*    */ import net.minecraft.block.Block;
/*    */ 
/*    */ public class ItemCloth
/*    */   extends ItemBlock
/*    */ {
/*    */   public ItemCloth(Block block) {
/*  9 */     super(block);
/* 10 */     setMaxDamage(0);
/* 11 */     setHasSubtypes(true);
/*    */   }
/*    */ 
/*    */   
/*    */   public int getMetadata(int damage) {
/* 16 */     return damage;
/*    */   }
/*    */ 
/*    */   
/*    */   public String getUnlocalizedName(ItemStack stack) {
/* 21 */     return getUnlocalizedName() + "." + EnumDyeColor.byMetadata(stack.getMetadata()).getUnlocalizedName();
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\item\ItemCloth.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */