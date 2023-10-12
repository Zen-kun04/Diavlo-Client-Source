/*     */ package net.minecraft.item;
/*     */ import java.util.List;
/*     */ import net.minecraft.block.state.IBlockState;
/*     */ import net.minecraft.creativetab.CreativeTabs;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.entity.EntityList;
/*     */ import net.minecraft.entity.EntityLiving;
/*     */ import net.minecraft.entity.IEntityLivingData;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.stats.StatList;
/*     */ import net.minecraft.tileentity.MobSpawnerBaseLogic;
/*     */ import net.minecraft.tileentity.TileEntity;
/*     */ import net.minecraft.tileentity.TileEntityMobSpawner;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.util.EnumFacing;
/*     */ import net.minecraft.util.MathHelper;
/*     */ import net.minecraft.util.MovingObjectPosition;
/*     */ import net.minecraft.util.StatCollector;
/*     */ import net.minecraft.world.World;
/*     */ 
/*     */ public class ItemMonsterPlacer extends Item {
/*     */   public ItemMonsterPlacer() {
/*  23 */     setHasSubtypes(true);
/*  24 */     setCreativeTab(CreativeTabs.tabMisc);
/*     */   }
/*     */ 
/*     */   
/*     */   public String getItemStackDisplayName(ItemStack stack) {
/*  29 */     String s = ("" + StatCollector.translateToLocal(getUnlocalizedName() + ".name")).trim();
/*  30 */     String s1 = EntityList.getStringFromID(stack.getMetadata());
/*     */     
/*  32 */     if (s1 != null)
/*     */     {
/*  34 */       s = s + " " + StatCollector.translateToLocal("entity." + s1 + ".name");
/*     */     }
/*     */     
/*  37 */     return s;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getColorFromItemStack(ItemStack stack, int renderPass) {
/*  42 */     EntityList.EntityEggInfo entitylist$entityegginfo = (EntityList.EntityEggInfo)EntityList.entityEggs.get(Integer.valueOf(stack.getMetadata()));
/*  43 */     return (entitylist$entityegginfo != null) ? ((renderPass == 0) ? entitylist$entityegginfo.primaryColor : entitylist$entityegginfo.secondaryColor) : 16777215;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean onItemUse(ItemStack stack, EntityPlayer playerIn, World worldIn, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ) {
/*  48 */     if (worldIn.isRemote)
/*     */     {
/*  50 */       return true;
/*     */     }
/*  52 */     if (!playerIn.canPlayerEdit(pos.offset(side), side, stack))
/*     */     {
/*  54 */       return false;
/*     */     }
/*     */ 
/*     */     
/*  58 */     IBlockState iblockstate = worldIn.getBlockState(pos);
/*     */     
/*  60 */     if (iblockstate.getBlock() == Blocks.mob_spawner) {
/*     */       
/*  62 */       TileEntity tileentity = worldIn.getTileEntity(pos);
/*     */       
/*  64 */       if (tileentity instanceof TileEntityMobSpawner) {
/*     */         
/*  66 */         MobSpawnerBaseLogic mobspawnerbaselogic = ((TileEntityMobSpawner)tileentity).getSpawnerBaseLogic();
/*  67 */         mobspawnerbaselogic.setEntityName(EntityList.getStringFromID(stack.getMetadata()));
/*  68 */         tileentity.markDirty();
/*  69 */         worldIn.markBlockForUpdate(pos);
/*     */         
/*  71 */         if (!playerIn.capabilities.isCreativeMode)
/*     */         {
/*  73 */           stack.stackSize--;
/*     */         }
/*     */         
/*  76 */         return true;
/*     */       } 
/*     */     } 
/*     */     
/*  80 */     pos = pos.offset(side);
/*  81 */     double d0 = 0.0D;
/*     */     
/*  83 */     if (side == EnumFacing.UP && iblockstate instanceof net.minecraft.block.BlockFence)
/*     */     {
/*  85 */       d0 = 0.5D;
/*     */     }
/*     */     
/*  88 */     Entity entity = spawnCreature(worldIn, stack.getMetadata(), pos.getX() + 0.5D, pos.getY() + d0, pos.getZ() + 0.5D);
/*     */     
/*  90 */     if (entity != null) {
/*     */       
/*  92 */       if (entity instanceof net.minecraft.entity.EntityLivingBase && stack.hasDisplayName())
/*     */       {
/*  94 */         entity.setCustomNameTag(stack.getDisplayName());
/*     */       }
/*     */       
/*  97 */       if (!playerIn.capabilities.isCreativeMode)
/*     */       {
/*  99 */         stack.stackSize--;
/*     */       }
/*     */     } 
/*     */     
/* 103 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public ItemStack onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn) {
/* 109 */     if (worldIn.isRemote)
/*     */     {
/* 111 */       return itemStackIn;
/*     */     }
/*     */ 
/*     */     
/* 115 */     MovingObjectPosition movingobjectposition = getMovingObjectPositionFromPlayer(worldIn, playerIn, true);
/*     */     
/* 117 */     if (movingobjectposition == null)
/*     */     {
/* 119 */       return itemStackIn;
/*     */     }
/*     */ 
/*     */     
/* 123 */     if (movingobjectposition.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK) {
/*     */       
/* 125 */       BlockPos blockpos = movingobjectposition.getBlockPos();
/*     */       
/* 127 */       if (!worldIn.isBlockModifiable(playerIn, blockpos))
/*     */       {
/* 129 */         return itemStackIn;
/*     */       }
/*     */       
/* 132 */       if (!playerIn.canPlayerEdit(blockpos, movingobjectposition.sideHit, itemStackIn))
/*     */       {
/* 134 */         return itemStackIn;
/*     */       }
/*     */       
/* 137 */       if (worldIn.getBlockState(blockpos).getBlock() instanceof net.minecraft.block.BlockLiquid) {
/*     */         
/* 139 */         Entity entity = spawnCreature(worldIn, itemStackIn.getMetadata(), blockpos.getX() + 0.5D, blockpos.getY() + 0.5D, blockpos.getZ() + 0.5D);
/*     */         
/* 141 */         if (entity != null) {
/*     */           
/* 143 */           if (entity instanceof net.minecraft.entity.EntityLivingBase && itemStackIn.hasDisplayName())
/*     */           {
/* 145 */             ((EntityLiving)entity).setCustomNameTag(itemStackIn.getDisplayName());
/*     */           }
/*     */           
/* 148 */           if (!playerIn.capabilities.isCreativeMode)
/*     */           {
/* 150 */             itemStackIn.stackSize--;
/*     */           }
/*     */           
/* 153 */           playerIn.triggerAchievement(StatList.objectUseStats[Item.getIdFromItem(this)]);
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/* 158 */     return itemStackIn;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Entity spawnCreature(World worldIn, int entityID, double x, double y, double z) {
/* 165 */     if (!EntityList.entityEggs.containsKey(Integer.valueOf(entityID)))
/*     */     {
/* 167 */       return null;
/*     */     }
/*     */ 
/*     */     
/* 171 */     Entity entity = null;
/*     */     
/* 173 */     for (int i = 0; i < 1; i++) {
/*     */       
/* 175 */       entity = EntityList.createEntityByID(entityID, worldIn);
/*     */       
/* 177 */       if (entity instanceof net.minecraft.entity.EntityLivingBase) {
/*     */         
/* 179 */         EntityLiving entityliving = (EntityLiving)entity;
/* 180 */         entity.setLocationAndAngles(x, y, z, MathHelper.wrapAngleTo180_float(worldIn.rand.nextFloat() * 360.0F), 0.0F);
/* 181 */         entityliving.rotationYawHead = entityliving.rotationYaw;
/* 182 */         entityliving.renderYawOffset = entityliving.rotationYaw;
/* 183 */         entityliving.onInitialSpawn(worldIn.getDifficultyForLocation(new BlockPos((Entity)entityliving)), (IEntityLivingData)null);
/* 184 */         worldIn.spawnEntityInWorld(entity);
/* 185 */         entityliving.playLivingSound();
/*     */       } 
/*     */     } 
/*     */     
/* 189 */     return entity;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void getSubItems(Item itemIn, CreativeTabs tab, List<ItemStack> subItems) {
/* 195 */     for (EntityList.EntityEggInfo entitylist$entityegginfo : EntityList.entityEggs.values())
/*     */     {
/* 197 */       subItems.add(new ItemStack(itemIn, 1, entitylist$entityegginfo.spawnedID));
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\item\ItemMonsterPlacer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */