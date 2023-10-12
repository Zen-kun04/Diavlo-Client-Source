/*     */ package net.minecraft.item;
/*     */ import java.util.List;
/*     */ import net.minecraft.creativetab.CreativeTabs;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.entity.item.EntityBoat;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.init.Blocks;
/*     */ import net.minecraft.stats.StatList;
/*     */ import net.minecraft.util.AxisAlignedBB;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.util.MathHelper;
/*     */ import net.minecraft.util.MovingObjectPosition;
/*     */ import net.minecraft.util.Vec3;
/*     */ import net.minecraft.world.World;
/*     */ 
/*     */ public class ItemBoat extends Item {
/*     */   public ItemBoat() {
/*  18 */     this.maxStackSize = 1;
/*  19 */     setCreativeTab(CreativeTabs.tabTransport);
/*     */   }
/*     */ 
/*     */   
/*     */   public ItemStack onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn) {
/*  24 */     float f = 1.0F;
/*  25 */     float f1 = playerIn.prevRotationPitch + (playerIn.rotationPitch - playerIn.prevRotationPitch) * f;
/*  26 */     float f2 = playerIn.prevRotationYaw + (playerIn.rotationYaw - playerIn.prevRotationYaw) * f;
/*  27 */     double d0 = playerIn.prevPosX + (playerIn.posX - playerIn.prevPosX) * f;
/*  28 */     double d1 = playerIn.prevPosY + (playerIn.posY - playerIn.prevPosY) * f + playerIn.getEyeHeight();
/*  29 */     double d2 = playerIn.prevPosZ + (playerIn.posZ - playerIn.prevPosZ) * f;
/*  30 */     Vec3 vec3 = new Vec3(d0, d1, d2);
/*  31 */     float f3 = MathHelper.cos(-f2 * 0.017453292F - 3.1415927F);
/*  32 */     float f4 = MathHelper.sin(-f2 * 0.017453292F - 3.1415927F);
/*  33 */     float f5 = -MathHelper.cos(-f1 * 0.017453292F);
/*  34 */     float f6 = MathHelper.sin(-f1 * 0.017453292F);
/*  35 */     float f7 = f4 * f5;
/*  36 */     float f8 = f3 * f5;
/*  37 */     double d3 = 5.0D;
/*  38 */     Vec3 vec31 = vec3.addVector(f7 * d3, f6 * d3, f8 * d3);
/*  39 */     MovingObjectPosition movingobjectposition = worldIn.rayTraceBlocks(vec3, vec31, true);
/*     */     
/*  41 */     if (movingobjectposition == null)
/*     */     {
/*  43 */       return itemStackIn;
/*     */     }
/*     */ 
/*     */     
/*  47 */     Vec3 vec32 = playerIn.getLook(f);
/*  48 */     boolean flag = false;
/*  49 */     float f9 = 1.0F;
/*  50 */     List<Entity> list = worldIn.getEntitiesWithinAABBExcludingEntity((Entity)playerIn, playerIn.getEntityBoundingBox().addCoord(vec32.xCoord * d3, vec32.yCoord * d3, vec32.zCoord * d3).expand(f9, f9, f9));
/*     */     
/*  52 */     for (int i = 0; i < list.size(); i++) {
/*     */       
/*  54 */       Entity entity = list.get(i);
/*     */       
/*  56 */       if (entity.canBeCollidedWith()) {
/*     */         
/*  58 */         float f10 = entity.getCollisionBorderSize();
/*  59 */         AxisAlignedBB axisalignedbb = entity.getEntityBoundingBox().expand(f10, f10, f10);
/*     */         
/*  61 */         if (axisalignedbb.isVecInside(vec3))
/*     */         {
/*  63 */           flag = true;
/*     */         }
/*     */       } 
/*     */     } 
/*     */     
/*  68 */     if (flag)
/*     */     {
/*  70 */       return itemStackIn;
/*     */     }
/*     */ 
/*     */     
/*  74 */     if (movingobjectposition.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK) {
/*     */       
/*  76 */       BlockPos blockpos = movingobjectposition.getBlockPos();
/*     */       
/*  78 */       if (worldIn.getBlockState(blockpos).getBlock() == Blocks.snow_layer)
/*     */       {
/*  80 */         blockpos = blockpos.down();
/*     */       }
/*     */       
/*  83 */       EntityBoat entityboat = new EntityBoat(worldIn, (blockpos.getX() + 0.5F), (blockpos.getY() + 1.0F), (blockpos.getZ() + 0.5F));
/*  84 */       entityboat.rotationYaw = (((MathHelper.floor_double((playerIn.rotationYaw * 4.0F / 360.0F) + 0.5D) & 0x3) - 1) * 90);
/*     */       
/*  86 */       if (!worldIn.getCollidingBoundingBoxes((Entity)entityboat, entityboat.getEntityBoundingBox().expand(-0.1D, -0.1D, -0.1D)).isEmpty())
/*     */       {
/*  88 */         return itemStackIn;
/*     */       }
/*     */       
/*  91 */       if (!worldIn.isRemote)
/*     */       {
/*  93 */         worldIn.spawnEntityInWorld((Entity)entityboat);
/*     */       }
/*     */       
/*  96 */       if (!playerIn.capabilities.isCreativeMode)
/*     */       {
/*  98 */         itemStackIn.stackSize--;
/*     */       }
/*     */       
/* 101 */       playerIn.triggerAchievement(StatList.objectUseStats[Item.getIdFromItem(this)]);
/*     */     } 
/*     */     
/* 104 */     return itemStackIn;
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\item\ItemBoat.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */