/*     */ package net.minecraft.item;
/*     */ 
/*     */ import net.minecraft.block.BlockEndPortalFrame;
/*     */ import net.minecraft.block.properties.IProperty;
/*     */ import net.minecraft.block.state.IBlockState;
/*     */ import net.minecraft.creativetab.CreativeTabs;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.entity.item.EntityEnderEye;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.init.Blocks;
/*     */ import net.minecraft.stats.StatList;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.util.EnumFacing;
/*     */ import net.minecraft.util.EnumParticleTypes;
/*     */ import net.minecraft.util.MovingObjectPosition;
/*     */ import net.minecraft.world.World;
/*     */ 
/*     */ public class ItemEnderEye extends Item {
/*     */   public ItemEnderEye() {
/*  20 */     setCreativeTab(CreativeTabs.tabMisc);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean onItemUse(ItemStack stack, EntityPlayer playerIn, World worldIn, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ) {
/*  25 */     IBlockState iblockstate = worldIn.getBlockState(pos);
/*     */     
/*  27 */     if (playerIn.canPlayerEdit(pos.offset(side), side, stack) && iblockstate.getBlock() == Blocks.end_portal_frame && !((Boolean)iblockstate.getValue((IProperty)BlockEndPortalFrame.EYE)).booleanValue()) {
/*     */       
/*  29 */       if (worldIn.isRemote)
/*     */       {
/*  31 */         return true;
/*     */       }
/*     */ 
/*     */       
/*  35 */       worldIn.setBlockState(pos, iblockstate.withProperty((IProperty)BlockEndPortalFrame.EYE, Boolean.valueOf(true)), 2);
/*  36 */       worldIn.updateComparatorOutputLevel(pos, Blocks.end_portal_frame);
/*  37 */       stack.stackSize--;
/*     */       
/*  39 */       for (int i = 0; i < 16; i++) {
/*     */         
/*  41 */         double d0 = (pos.getX() + (5.0F + itemRand.nextFloat() * 6.0F) / 16.0F);
/*  42 */         double d1 = (pos.getY() + 0.8125F);
/*  43 */         double d2 = (pos.getZ() + (5.0F + itemRand.nextFloat() * 6.0F) / 16.0F);
/*  44 */         double d3 = 0.0D;
/*  45 */         double d4 = 0.0D;
/*  46 */         double d5 = 0.0D;
/*  47 */         worldIn.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, d0, d1, d2, d3, d4, d5, new int[0]);
/*     */       } 
/*     */       
/*  50 */       EnumFacing enumfacing = (EnumFacing)iblockstate.getValue((IProperty)BlockEndPortalFrame.FACING);
/*  51 */       int l = 0;
/*  52 */       int j = 0;
/*  53 */       boolean flag1 = false;
/*  54 */       boolean flag = true;
/*  55 */       EnumFacing enumfacing1 = enumfacing.rotateY();
/*     */       
/*  57 */       for (int k = -2; k <= 2; k++) {
/*     */         
/*  59 */         BlockPos blockpos1 = pos.offset(enumfacing1, k);
/*  60 */         IBlockState iblockstate1 = worldIn.getBlockState(blockpos1);
/*     */         
/*  62 */         if (iblockstate1.getBlock() == Blocks.end_portal_frame) {
/*     */           
/*  64 */           if (!((Boolean)iblockstate1.getValue((IProperty)BlockEndPortalFrame.EYE)).booleanValue()) {
/*     */             
/*  66 */             flag = false;
/*     */             
/*     */             break;
/*     */           } 
/*  70 */           j = k;
/*     */           
/*  72 */           if (!flag1) {
/*     */             
/*  74 */             l = k;
/*  75 */             flag1 = true;
/*     */           } 
/*     */         } 
/*     */       } 
/*     */       
/*  80 */       if (flag && j == l + 2) {
/*     */         
/*  82 */         BlockPos blockpos = pos.offset(enumfacing, 4);
/*     */         
/*  84 */         for (int i1 = l; i1 <= j; i1++) {
/*     */           
/*  86 */           BlockPos blockpos2 = blockpos.offset(enumfacing1, i1);
/*  87 */           IBlockState iblockstate3 = worldIn.getBlockState(blockpos2);
/*     */           
/*  89 */           if (iblockstate3.getBlock() != Blocks.end_portal_frame || !((Boolean)iblockstate3.getValue((IProperty)BlockEndPortalFrame.EYE)).booleanValue()) {
/*     */             
/*  91 */             flag = false;
/*     */             
/*     */             break;
/*     */           } 
/*     */         } 
/*  96 */         for (int j1 = l - 1; j1 <= j + 1; j1 += 4) {
/*     */           
/*  98 */           blockpos = pos.offset(enumfacing1, j1);
/*     */           
/* 100 */           for (int l1 = 1; l1 <= 3; l1++) {
/*     */             
/* 102 */             BlockPos blockpos3 = blockpos.offset(enumfacing, l1);
/* 103 */             IBlockState iblockstate2 = worldIn.getBlockState(blockpos3);
/*     */             
/* 105 */             if (iblockstate2.getBlock() != Blocks.end_portal_frame || !((Boolean)iblockstate2.getValue((IProperty)BlockEndPortalFrame.EYE)).booleanValue()) {
/*     */               
/* 107 */               flag = false;
/*     */               
/*     */               break;
/*     */             } 
/*     */           } 
/*     */         } 
/* 113 */         if (flag)
/*     */         {
/* 115 */           for (int k1 = l; k1 <= j; k1++) {
/*     */             
/* 117 */             blockpos = pos.offset(enumfacing1, k1);
/*     */             
/* 119 */             for (int i2 = 1; i2 <= 3; i2++) {
/*     */               
/* 121 */               BlockPos blockpos4 = blockpos.offset(enumfacing, i2);
/* 122 */               worldIn.setBlockState(blockpos4, Blocks.end_portal.getDefaultState(), 2);
/*     */             } 
/*     */           } 
/*     */         }
/*     */       } 
/*     */       
/* 128 */       return true;
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 133 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public ItemStack onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn) {
/* 139 */     MovingObjectPosition movingobjectposition = getMovingObjectPositionFromPlayer(worldIn, playerIn, false);
/*     */     
/* 141 */     if (movingobjectposition != null && movingobjectposition.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK && worldIn.getBlockState(movingobjectposition.getBlockPos()).getBlock() == Blocks.end_portal_frame)
/*     */     {
/* 143 */       return itemStackIn;
/*     */     }
/*     */ 
/*     */     
/* 147 */     if (!worldIn.isRemote) {
/*     */       
/* 149 */       BlockPos blockpos = worldIn.getStrongholdPos("Stronghold", new BlockPos((Entity)playerIn));
/*     */       
/* 151 */       if (blockpos != null) {
/*     */         
/* 153 */         EntityEnderEye entityendereye = new EntityEnderEye(worldIn, playerIn.posX, playerIn.posY, playerIn.posZ);
/* 154 */         entityendereye.moveTowards(blockpos);
/* 155 */         worldIn.spawnEntityInWorld((Entity)entityendereye);
/* 156 */         worldIn.playSoundAtEntity((Entity)playerIn, "random.bow", 0.5F, 0.4F / (itemRand.nextFloat() * 0.4F + 0.8F));
/* 157 */         worldIn.playAuxSFXAtEntity((EntityPlayer)null, 1002, new BlockPos((Entity)playerIn), 0);
/*     */         
/* 159 */         if (!playerIn.capabilities.isCreativeMode)
/*     */         {
/* 161 */           itemStackIn.stackSize--;
/*     */         }
/*     */         
/* 164 */         playerIn.triggerAchievement(StatList.objectUseStats[Item.getIdFromItem(this)]);
/*     */       } 
/*     */     } 
/*     */     
/* 168 */     return itemStackIn;
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\item\ItemEnderEye.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */