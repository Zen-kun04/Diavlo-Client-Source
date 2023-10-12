/*    */ package net.minecraft.item;
/*    */ 
/*    */ import com.google.common.collect.Lists;
/*    */ import java.util.List;
/*    */ import net.minecraft.entity.Entity;
/*    */ import net.minecraft.entity.item.EntityFireworkRocket;
/*    */ import net.minecraft.entity.player.EntityPlayer;
/*    */ import net.minecraft.nbt.NBTTagCompound;
/*    */ import net.minecraft.nbt.NBTTagList;
/*    */ import net.minecraft.util.BlockPos;
/*    */ import net.minecraft.util.EnumFacing;
/*    */ import net.minecraft.util.StatCollector;
/*    */ import net.minecraft.world.World;
/*    */ 
/*    */ public class ItemFirework
/*    */   extends Item
/*    */ {
/*    */   public boolean onItemUse(ItemStack stack, EntityPlayer playerIn, World worldIn, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ) {
/* 19 */     if (!worldIn.isRemote) {
/*    */       
/* 21 */       EntityFireworkRocket entityfireworkrocket = new EntityFireworkRocket(worldIn, (pos.getX() + hitX), (pos.getY() + hitY), (pos.getZ() + hitZ), stack);
/* 22 */       worldIn.spawnEntityInWorld((Entity)entityfireworkrocket);
/*    */       
/* 24 */       if (!playerIn.capabilities.isCreativeMode)
/*    */       {
/* 26 */         stack.stackSize--;
/*    */       }
/*    */       
/* 29 */       return true;
/*    */     } 
/*    */ 
/*    */     
/* 33 */     return false;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void addInformation(ItemStack stack, EntityPlayer playerIn, List<String> tooltip, boolean advanced) {
/* 39 */     if (stack.hasTagCompound()) {
/*    */       
/* 41 */       NBTTagCompound nbttagcompound = stack.getTagCompound().getCompoundTag("Fireworks");
/*    */       
/* 43 */       if (nbttagcompound != null) {
/*    */         
/* 45 */         if (nbttagcompound.hasKey("Flight", 99))
/*    */         {
/* 47 */           tooltip.add(StatCollector.translateToLocal("item.fireworks.flight") + " " + nbttagcompound.getByte("Flight"));
/*    */         }
/*    */         
/* 50 */         NBTTagList nbttaglist = nbttagcompound.getTagList("Explosions", 10);
/*    */         
/* 52 */         if (nbttaglist != null && nbttaglist.tagCount() > 0)
/*    */         {
/* 54 */           for (int i = 0; i < nbttaglist.tagCount(); i++) {
/*    */             
/* 56 */             NBTTagCompound nbttagcompound1 = nbttaglist.getCompoundTagAt(i);
/* 57 */             List<String> list = Lists.newArrayList();
/* 58 */             ItemFireworkCharge.addExplosionInfo(nbttagcompound1, list);
/*    */             
/* 60 */             if (list.size() > 0) {
/*    */               
/* 62 */               for (int j = 1; j < list.size(); j++)
/*    */               {
/* 64 */                 list.set(j, "  " + (String)list.get(j));
/*    */               }
/*    */               
/* 67 */               tooltip.addAll(list);
/*    */             } 
/*    */           } 
/*    */         }
/*    */       } 
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\item\ItemFirework.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */