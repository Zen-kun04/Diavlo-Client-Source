/*    */ package net.minecraft.item;
/*    */ 
/*    */ import net.minecraft.entity.player.EntityPlayer;
/*    */ import net.minecraft.nbt.NBTTagCompound;
/*    */ import net.minecraft.nbt.NBTTagList;
/*    */ import net.minecraft.stats.StatList;
/*    */ import net.minecraft.world.World;
/*    */ 
/*    */ public class ItemWritableBook
/*    */   extends Item
/*    */ {
/*    */   public ItemWritableBook() {
/* 13 */     setMaxStackSize(1);
/*    */   }
/*    */ 
/*    */   
/*    */   public ItemStack onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn) {
/* 18 */     playerIn.displayGUIBook(itemStackIn);
/* 19 */     playerIn.triggerAchievement(StatList.objectUseStats[Item.getIdFromItem(this)]);
/* 20 */     return itemStackIn;
/*    */   }
/*    */ 
/*    */   
/*    */   public static boolean isNBTValid(NBTTagCompound nbt) {
/* 25 */     if (nbt == null)
/*    */     {
/* 27 */       return false;
/*    */     }
/* 29 */     if (!nbt.hasKey("pages", 9))
/*    */     {
/* 31 */       return false;
/*    */     }
/*    */ 
/*    */     
/* 35 */     NBTTagList nbttaglist = nbt.getTagList("pages", 8);
/*    */     
/* 37 */     for (int i = 0; i < nbttaglist.tagCount(); i++) {
/*    */       
/* 39 */       String s = nbttaglist.getStringTagAt(i);
/*    */       
/* 41 */       if (s == null)
/*    */       {
/* 43 */         return false;
/*    */       }
/*    */       
/* 46 */       if (s.length() > 32767)
/*    */       {
/* 48 */         return false;
/*    */       }
/*    */     } 
/*    */     
/* 52 */     return true;
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\item\ItemWritableBook.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */