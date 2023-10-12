/*    */ package net.minecraft.item;
/*    */ import java.util.List;
/*    */ import java.util.Random;
/*    */ import net.minecraft.creativetab.CreativeTabs;
/*    */ import net.minecraft.entity.Entity;
/*    */ import net.minecraft.entity.item.EntityArmorStand;
/*    */ import net.minecraft.entity.player.EntityPlayer;
/*    */ import net.minecraft.nbt.NBTTagCompound;
/*    */ import net.minecraft.util.AxisAlignedBB;
/*    */ import net.minecraft.util.BlockPos;
/*    */ import net.minecraft.util.EnumFacing;
/*    */ import net.minecraft.util.MathHelper;
/*    */ import net.minecraft.util.Rotations;
/*    */ import net.minecraft.world.World;
/*    */ 
/*    */ public class ItemArmorStand extends Item {
/*    */   public ItemArmorStand() {
/* 18 */     setCreativeTab(CreativeTabs.tabDecorations);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean onItemUse(ItemStack stack, EntityPlayer playerIn, World worldIn, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ) {
/* 23 */     if (side == EnumFacing.DOWN)
/*    */     {
/* 25 */       return false;
/*    */     }
/*    */ 
/*    */     
/* 29 */     boolean flag = worldIn.getBlockState(pos).getBlock().isReplaceable(worldIn, pos);
/* 30 */     BlockPos blockpos = flag ? pos : pos.offset(side);
/*    */     
/* 32 */     if (!playerIn.canPlayerEdit(blockpos, side, stack))
/*    */     {
/* 34 */       return false;
/*    */     }
/*    */ 
/*    */     
/* 38 */     BlockPos blockpos1 = blockpos.up();
/* 39 */     boolean flag1 = (!worldIn.isAirBlock(blockpos) && !worldIn.getBlockState(blockpos).getBlock().isReplaceable(worldIn, blockpos));
/* 40 */     int i = flag1 | ((!worldIn.isAirBlock(blockpos1) && !worldIn.getBlockState(blockpos1).getBlock().isReplaceable(worldIn, blockpos1)) ? 1 : 0);
/*    */     
/* 42 */     if (i != 0)
/*    */     {
/* 44 */       return false;
/*    */     }
/*    */ 
/*    */     
/* 48 */     double d0 = blockpos.getX();
/* 49 */     double d1 = blockpos.getY();
/* 50 */     double d2 = blockpos.getZ();
/* 51 */     List<Entity> list = worldIn.getEntitiesWithinAABBExcludingEntity((Entity)null, AxisAlignedBB.fromBounds(d0, d1, d2, d0 + 1.0D, d1 + 2.0D, d2 + 1.0D));
/*    */     
/* 53 */     if (list.size() > 0)
/*    */     {
/* 55 */       return false;
/*    */     }
/*    */ 
/*    */     
/* 59 */     if (!worldIn.isRemote) {
/*    */       
/* 61 */       worldIn.setBlockToAir(blockpos);
/* 62 */       worldIn.setBlockToAir(blockpos1);
/* 63 */       EntityArmorStand entityarmorstand = new EntityArmorStand(worldIn, d0 + 0.5D, d1, d2 + 0.5D);
/* 64 */       float f = MathHelper.floor_float((MathHelper.wrapAngleTo180_float(playerIn.rotationYaw - 180.0F) + 22.5F) / 45.0F) * 45.0F;
/* 65 */       entityarmorstand.setLocationAndAngles(d0 + 0.5D, d1, d2 + 0.5D, f, 0.0F);
/* 66 */       applyRandomRotations(entityarmorstand, worldIn.rand);
/* 67 */       NBTTagCompound nbttagcompound = stack.getTagCompound();
/*    */       
/* 69 */       if (nbttagcompound != null && nbttagcompound.hasKey("EntityTag", 10)) {
/*    */         
/* 71 */         NBTTagCompound nbttagcompound1 = new NBTTagCompound();
/* 72 */         entityarmorstand.writeToNBTOptional(nbttagcompound1);
/* 73 */         nbttagcompound1.merge(nbttagcompound.getCompoundTag("EntityTag"));
/* 74 */         entityarmorstand.readFromNBT(nbttagcompound1);
/*    */       } 
/*    */       
/* 77 */       worldIn.spawnEntityInWorld((Entity)entityarmorstand);
/*    */     } 
/*    */     
/* 80 */     stack.stackSize--;
/* 81 */     return true;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private void applyRandomRotations(EntityArmorStand armorStand, Random rand) {
/* 90 */     Rotations rotations = armorStand.getHeadRotation();
/* 91 */     float f = rand.nextFloat() * 5.0F;
/* 92 */     float f1 = rand.nextFloat() * 20.0F - 10.0F;
/* 93 */     Rotations rotations1 = new Rotations(rotations.getX() + f, rotations.getY() + f1, rotations.getZ());
/* 94 */     armorStand.setHeadRotation(rotations1);
/* 95 */     rotations = armorStand.getBodyRotation();
/* 96 */     f = rand.nextFloat() * 10.0F - 5.0F;
/* 97 */     rotations1 = new Rotations(rotations.getX(), rotations.getY() + f, rotations.getZ());
/* 98 */     armorStand.setBodyRotation(rotations1);
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\item\ItemArmorStand.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */