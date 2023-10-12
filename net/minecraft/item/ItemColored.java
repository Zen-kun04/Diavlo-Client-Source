/*    */ package net.minecraft.item;
/*    */ 
/*    */ import net.minecraft.block.Block;
/*    */ 
/*    */ public class ItemColored
/*    */   extends ItemBlock
/*    */ {
/*    */   private final Block coloredBlock;
/*    */   private String[] subtypeNames;
/*    */   
/*    */   public ItemColored(Block block, boolean hasSubtypes) {
/* 12 */     super(block);
/* 13 */     this.coloredBlock = block;
/*    */     
/* 15 */     if (hasSubtypes) {
/*    */       
/* 17 */       setMaxDamage(0);
/* 18 */       setHasSubtypes(true);
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public int getColorFromItemStack(ItemStack stack, int renderPass) {
/* 24 */     return this.coloredBlock.getRenderColor(this.coloredBlock.getStateFromMeta(stack.getMetadata()));
/*    */   }
/*    */ 
/*    */   
/*    */   public int getMetadata(int damage) {
/* 29 */     return damage;
/*    */   }
/*    */ 
/*    */   
/*    */   public ItemColored setSubtypeNames(String[] names) {
/* 34 */     this.subtypeNames = names;
/* 35 */     return this;
/*    */   }
/*    */ 
/*    */   
/*    */   public String getUnlocalizedName(ItemStack stack) {
/* 40 */     if (this.subtypeNames == null)
/*    */     {
/* 42 */       return super.getUnlocalizedName(stack);
/*    */     }
/*    */ 
/*    */     
/* 46 */     int i = stack.getMetadata();
/* 47 */     return (i >= 0 && i < this.subtypeNames.length) ? (super.getUnlocalizedName(stack) + "." + this.subtypeNames[i]) : super.getUnlocalizedName(stack);
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\item\ItemColored.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */