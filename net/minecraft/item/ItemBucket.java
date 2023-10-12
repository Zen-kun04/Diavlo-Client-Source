/*     */ package net.minecraft.item;
/*     */ 
/*     */ import net.minecraft.block.Block;
/*     */ import net.minecraft.block.BlockLiquid;
/*     */ import net.minecraft.block.material.Material;
/*     */ import net.minecraft.block.properties.IProperty;
/*     */ import net.minecraft.block.state.IBlockState;
/*     */ import net.minecraft.creativetab.CreativeTabs;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.init.Blocks;
/*     */ import net.minecraft.init.Items;
/*     */ import net.minecraft.stats.StatList;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.util.EnumParticleTypes;
/*     */ import net.minecraft.util.MovingObjectPosition;
/*     */ import net.minecraft.world.World;
/*     */ 
/*     */ public class ItemBucket
/*     */   extends Item {
/*     */   private Block isFull;
/*     */   
/*     */   public ItemBucket(Block containedBlock) {
/*  23 */     this.maxStackSize = 1;
/*  24 */     this.isFull = containedBlock;
/*  25 */     setCreativeTab(CreativeTabs.tabMisc);
/*     */   }
/*     */ 
/*     */   
/*     */   public ItemStack onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn) {
/*  30 */     boolean flag = (this.isFull == Blocks.air);
/*  31 */     MovingObjectPosition movingobjectposition = getMovingObjectPositionFromPlayer(worldIn, playerIn, flag);
/*     */     
/*  33 */     if (movingobjectposition == null)
/*     */     {
/*  35 */       return itemStackIn;
/*     */     }
/*     */ 
/*     */     
/*  39 */     if (movingobjectposition.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK) {
/*     */       
/*  41 */       BlockPos blockpos = movingobjectposition.getBlockPos();
/*     */       
/*  43 */       if (!worldIn.isBlockModifiable(playerIn, blockpos))
/*     */       {
/*  45 */         return itemStackIn;
/*     */       }
/*     */       
/*  48 */       if (flag) {
/*     */         
/*  50 */         if (!playerIn.canPlayerEdit(blockpos.offset(movingobjectposition.sideHit), movingobjectposition.sideHit, itemStackIn))
/*     */         {
/*  52 */           return itemStackIn;
/*     */         }
/*     */         
/*  55 */         IBlockState iblockstate = worldIn.getBlockState(blockpos);
/*  56 */         Material material = iblockstate.getBlock().getMaterial();
/*     */         
/*  58 */         if (material == Material.water && ((Integer)iblockstate.getValue((IProperty)BlockLiquid.LEVEL)).intValue() == 0) {
/*     */           
/*  60 */           worldIn.setBlockToAir(blockpos);
/*  61 */           playerIn.triggerAchievement(StatList.objectUseStats[Item.getIdFromItem(this)]);
/*  62 */           return fillBucket(itemStackIn, playerIn, Items.water_bucket);
/*     */         } 
/*     */         
/*  65 */         if (material == Material.lava && ((Integer)iblockstate.getValue((IProperty)BlockLiquid.LEVEL)).intValue() == 0)
/*     */         {
/*  67 */           worldIn.setBlockToAir(blockpos);
/*  68 */           playerIn.triggerAchievement(StatList.objectUseStats[Item.getIdFromItem(this)]);
/*  69 */           return fillBucket(itemStackIn, playerIn, Items.lava_bucket);
/*     */         }
/*     */       
/*     */       } else {
/*     */         
/*  74 */         if (this.isFull == Blocks.air)
/*     */         {
/*  76 */           return new ItemStack(Items.bucket);
/*     */         }
/*     */         
/*  79 */         BlockPos blockpos1 = blockpos.offset(movingobjectposition.sideHit);
/*     */         
/*  81 */         if (!playerIn.canPlayerEdit(blockpos1, movingobjectposition.sideHit, itemStackIn))
/*     */         {
/*  83 */           return itemStackIn;
/*     */         }
/*     */         
/*  86 */         if (tryPlaceContainedLiquid(worldIn, blockpos1) && !playerIn.capabilities.isCreativeMode) {
/*     */           
/*  88 */           playerIn.triggerAchievement(StatList.objectUseStats[Item.getIdFromItem(this)]);
/*  89 */           return new ItemStack(Items.bucket);
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/*  94 */     return itemStackIn;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private ItemStack fillBucket(ItemStack emptyBuckets, EntityPlayer player, Item fullBucket) {
/* 100 */     if (player.capabilities.isCreativeMode)
/*     */     {
/* 102 */       return emptyBuckets;
/*     */     }
/* 104 */     if (--emptyBuckets.stackSize <= 0)
/*     */     {
/* 106 */       return new ItemStack(fullBucket);
/*     */     }
/*     */ 
/*     */     
/* 110 */     if (!player.inventory.addItemStackToInventory(new ItemStack(fullBucket)))
/*     */     {
/* 112 */       player.dropPlayerItemWithRandomChoice(new ItemStack(fullBucket, 1, 0), false);
/*     */     }
/*     */     
/* 115 */     return emptyBuckets;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean tryPlaceContainedLiquid(World worldIn, BlockPos pos) {
/* 121 */     if (this.isFull == Blocks.air)
/*     */     {
/* 123 */       return false;
/*     */     }
/*     */ 
/*     */     
/* 127 */     Material material = worldIn.getBlockState(pos).getBlock().getMaterial();
/* 128 */     boolean flag = !material.isSolid();
/*     */     
/* 130 */     if (!worldIn.isAirBlock(pos) && !flag)
/*     */     {
/* 132 */       return false;
/*     */     }
/*     */ 
/*     */     
/* 136 */     if (worldIn.provider.doesWaterVaporize() && this.isFull == Blocks.flowing_water) {
/*     */       
/* 138 */       int i = pos.getX();
/* 139 */       int j = pos.getY();
/* 140 */       int k = pos.getZ();
/* 141 */       worldIn.playSoundEffect((i + 0.5F), (j + 0.5F), (k + 0.5F), "random.fizz", 0.5F, 2.6F + (worldIn.rand.nextFloat() - worldIn.rand.nextFloat()) * 0.8F);
/*     */       
/* 143 */       for (int l = 0; l < 8; l++)
/*     */       {
/* 145 */         worldIn.spawnParticle(EnumParticleTypes.SMOKE_LARGE, i + Math.random(), j + Math.random(), k + Math.random(), 0.0D, 0.0D, 0.0D, new int[0]);
/*     */       }
/*     */     }
/*     */     else {
/*     */       
/* 150 */       if (!worldIn.isRemote && flag && !material.isLiquid())
/*     */       {
/* 152 */         worldIn.destroyBlock(pos, true);
/*     */       }
/*     */       
/* 155 */       worldIn.setBlockState(pos, this.isFull.getDefaultState(), 3);
/*     */     } 
/*     */     
/* 158 */     return true;
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\item\ItemBucket.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */