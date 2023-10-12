/*    */ package net.minecraft.inventory;
/*    */ 
/*    */ import java.util.Random;
/*    */ import net.minecraft.entity.Entity;
/*    */ import net.minecraft.entity.item.EntityItem;
/*    */ import net.minecraft.item.ItemStack;
/*    */ import net.minecraft.nbt.NBTTagCompound;
/*    */ import net.minecraft.util.BlockPos;
/*    */ import net.minecraft.world.World;
/*    */ 
/*    */ public class InventoryHelper
/*    */ {
/* 13 */   private static final Random RANDOM = new Random();
/*    */ 
/*    */   
/*    */   public static void dropInventoryItems(World worldIn, BlockPos pos, IInventory inventory) {
/* 17 */     dropInventoryItems(worldIn, pos.getX(), pos.getY(), pos.getZ(), inventory);
/*    */   }
/*    */ 
/*    */   
/*    */   public static void dropInventoryItems(World worldIn, Entity entityAt, IInventory inventory) {
/* 22 */     dropInventoryItems(worldIn, entityAt.posX, entityAt.posY, entityAt.posZ, inventory);
/*    */   }
/*    */ 
/*    */   
/*    */   private static void dropInventoryItems(World worldIn, double x, double y, double z, IInventory inventory) {
/* 27 */     for (int i = 0; i < inventory.getSizeInventory(); i++) {
/*    */       
/* 29 */       ItemStack itemstack = inventory.getStackInSlot(i);
/*    */       
/* 31 */       if (itemstack != null)
/*    */       {
/* 33 */         spawnItemStack(worldIn, x, y, z, itemstack);
/*    */       }
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   private static void spawnItemStack(World worldIn, double x, double y, double z, ItemStack stack) {
/* 40 */     float f = RANDOM.nextFloat() * 0.8F + 0.1F;
/* 41 */     float f1 = RANDOM.nextFloat() * 0.8F + 0.1F;
/* 42 */     float f2 = RANDOM.nextFloat() * 0.8F + 0.1F;
/*    */     
/* 44 */     while (stack.stackSize > 0) {
/*    */       
/* 46 */       int i = RANDOM.nextInt(21) + 10;
/*    */       
/* 48 */       if (i > stack.stackSize)
/*    */       {
/* 50 */         i = stack.stackSize;
/*    */       }
/*    */       
/* 53 */       stack.stackSize -= i;
/* 54 */       EntityItem entityitem = new EntityItem(worldIn, x + f, y + f1, z + f2, new ItemStack(stack.getItem(), i, stack.getMetadata()));
/*    */       
/* 56 */       if (stack.hasTagCompound())
/*    */       {
/* 58 */         entityitem.getEntityItem().setTagCompound((NBTTagCompound)stack.getTagCompound().copy());
/*    */       }
/*    */       
/* 61 */       float f3 = 0.05F;
/* 62 */       entityitem.motionX = RANDOM.nextGaussian() * f3;
/* 63 */       entityitem.motionY = RANDOM.nextGaussian() * f3 + 0.20000000298023224D;
/* 64 */       entityitem.motionZ = RANDOM.nextGaussian() * f3;
/* 65 */       worldIn.spawnEntityInWorld((Entity)entityitem);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\inventory\InventoryHelper.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */