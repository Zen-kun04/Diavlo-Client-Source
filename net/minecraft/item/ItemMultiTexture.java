/*    */ package net.minecraft.item;
/*    */ 
/*    */ import com.google.common.base.Function;
/*    */ import net.minecraft.block.Block;
/*    */ 
/*    */ public class ItemMultiTexture
/*    */   extends ItemBlock
/*    */ {
/*    */   protected final Block theBlock;
/*    */   protected final Function<ItemStack, String> nameFunction;
/*    */   
/*    */   public ItemMultiTexture(Block block, Block block2, Function<ItemStack, String> nameFunction) {
/* 13 */     super(block);
/* 14 */     this.theBlock = block2;
/* 15 */     this.nameFunction = nameFunction;
/* 16 */     setMaxDamage(0);
/* 17 */     setHasSubtypes(true);
/*    */   }
/*    */ 
/*    */   
/*    */   public ItemMultiTexture(Block block, Block block2, String[] namesByMeta) {
/* 22 */     this(block, block2, new Function<ItemStack, String>(namesByMeta)
/*    */         {
/*    */           public String apply(ItemStack p_apply_1_)
/*    */           {
/* 26 */             int i = p_apply_1_.getMetadata();
/*    */             
/* 28 */             if (i < 0 || i >= namesByMeta.length)
/*    */             {
/* 30 */               i = 0;
/*    */             }
/*    */             
/* 33 */             return namesByMeta[i];
/*    */           }
/*    */         });
/*    */   }
/*    */ 
/*    */   
/*    */   public int getMetadata(int damage) {
/* 40 */     return damage;
/*    */   }
/*    */ 
/*    */   
/*    */   public String getUnlocalizedName(ItemStack stack) {
/* 45 */     return getUnlocalizedName() + "." + (String)this.nameFunction.apply(stack);
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\item\ItemMultiTexture.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */