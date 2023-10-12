/*     */ package net.minecraft.item;
/*     */ 
/*     */ import java.util.List;
/*     */ import net.minecraft.block.Block;
/*     */ import net.minecraft.block.BlockPlanks;
/*     */ import net.minecraft.block.IGrowable;
/*     */ import net.minecraft.block.material.Material;
/*     */ import net.minecraft.block.properties.IProperty;
/*     */ import net.minecraft.block.state.IBlockState;
/*     */ import net.minecraft.creativetab.CreativeTabs;
/*     */ import net.minecraft.entity.EntityLivingBase;
/*     */ import net.minecraft.entity.passive.EntitySheep;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.init.Blocks;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.util.EnumFacing;
/*     */ import net.minecraft.util.EnumParticleTypes;
/*     */ import net.minecraft.world.IBlockAccess;
/*     */ import net.minecraft.world.World;
/*     */ 
/*     */ public class ItemDye extends Item {
/*  22 */   public static final int[] dyeColors = new int[] { 1973019, 11743532, 3887386, 5320730, 2437522, 8073150, 2651799, 11250603, 4408131, 14188952, 4312372, 14602026, 6719955, 12801229, 15435844, 15790320 };
/*     */ 
/*     */   
/*     */   public ItemDye() {
/*  26 */     setHasSubtypes(true);
/*  27 */     setMaxDamage(0);
/*  28 */     setCreativeTab(CreativeTabs.tabMaterials);
/*     */   }
/*     */ 
/*     */   
/*     */   public String getUnlocalizedName(ItemStack stack) {
/*  33 */     int i = stack.getMetadata();
/*  34 */     return getUnlocalizedName() + "." + EnumDyeColor.byDyeDamage(i).getUnlocalizedName();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean onItemUse(ItemStack stack, EntityPlayer playerIn, World worldIn, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ) {
/*  39 */     if (!playerIn.canPlayerEdit(pos.offset(side), side, stack))
/*     */     {
/*  41 */       return false;
/*     */     }
/*     */ 
/*     */     
/*  45 */     EnumDyeColor enumdyecolor = EnumDyeColor.byDyeDamage(stack.getMetadata());
/*     */     
/*  47 */     if (enumdyecolor == EnumDyeColor.WHITE) {
/*     */       
/*  49 */       if (applyBonemeal(stack, worldIn, pos))
/*     */       {
/*  51 */         if (!worldIn.isRemote)
/*     */         {
/*  53 */           worldIn.playAuxSFX(2005, pos, 0);
/*     */         }
/*     */         
/*  56 */         return true;
/*     */       }
/*     */     
/*  59 */     } else if (enumdyecolor == EnumDyeColor.BROWN) {
/*     */       
/*  61 */       IBlockState iblockstate = worldIn.getBlockState(pos);
/*  62 */       Block block = iblockstate.getBlock();
/*     */       
/*  64 */       if (block == Blocks.log && iblockstate.getValue((IProperty)BlockPlanks.VARIANT) == BlockPlanks.EnumType.JUNGLE) {
/*     */         
/*  66 */         if (side == EnumFacing.DOWN)
/*     */         {
/*  68 */           return false;
/*     */         }
/*     */         
/*  71 */         if (side == EnumFacing.UP)
/*     */         {
/*  73 */           return false;
/*     */         }
/*     */         
/*  76 */         pos = pos.offset(side);
/*     */         
/*  78 */         if (worldIn.isAirBlock(pos)) {
/*     */           
/*  80 */           IBlockState iblockstate1 = Blocks.cocoa.onBlockPlaced(worldIn, pos, side, hitX, hitY, hitZ, 0, (EntityLivingBase)playerIn);
/*  81 */           worldIn.setBlockState(pos, iblockstate1, 2);
/*     */           
/*  83 */           if (!playerIn.capabilities.isCreativeMode)
/*     */           {
/*  85 */             stack.stackSize--;
/*     */           }
/*     */         } 
/*     */         
/*  89 */         return true;
/*     */       } 
/*     */     } 
/*     */     
/*  93 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean applyBonemeal(ItemStack stack, World worldIn, BlockPos target) {
/*  99 */     IBlockState iblockstate = worldIn.getBlockState(target);
/*     */     
/* 101 */     if (iblockstate.getBlock() instanceof IGrowable) {
/*     */       
/* 103 */       IGrowable igrowable = (IGrowable)iblockstate.getBlock();
/*     */       
/* 105 */       if (igrowable.canGrow(worldIn, target, iblockstate, worldIn.isRemote)) {
/*     */         
/* 107 */         if (!worldIn.isRemote) {
/*     */           
/* 109 */           if (igrowable.canUseBonemeal(worldIn, worldIn.rand, target, iblockstate))
/*     */           {
/* 111 */             igrowable.grow(worldIn, worldIn.rand, target, iblockstate);
/*     */           }
/*     */           
/* 114 */           stack.stackSize--;
/*     */         } 
/*     */         
/* 117 */         return true;
/*     */       } 
/*     */     } 
/*     */     
/* 121 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public static void spawnBonemealParticles(World worldIn, BlockPos pos, int amount) {
/* 126 */     if (amount == 0)
/*     */     {
/* 128 */       amount = 15;
/*     */     }
/*     */     
/* 131 */     Block block = worldIn.getBlockState(pos).getBlock();
/*     */     
/* 133 */     if (block.getMaterial() != Material.air) {
/*     */       
/* 135 */       block.setBlockBoundsBasedOnState((IBlockAccess)worldIn, pos);
/*     */       
/* 137 */       for (int i = 0; i < amount; i++) {
/*     */         
/* 139 */         double d0 = itemRand.nextGaussian() * 0.02D;
/* 140 */         double d1 = itemRand.nextGaussian() * 0.02D;
/* 141 */         double d2 = itemRand.nextGaussian() * 0.02D;
/* 142 */         worldIn.spawnParticle(EnumParticleTypes.VILLAGER_HAPPY, (pos.getX() + itemRand.nextFloat()), pos.getY() + itemRand.nextFloat() * block.getBlockBoundsMaxY(), (pos.getZ() + itemRand.nextFloat()), d0, d1, d2, new int[0]);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean itemInteractionForEntity(ItemStack stack, EntityPlayer playerIn, EntityLivingBase target) {
/* 149 */     if (target instanceof EntitySheep) {
/*     */       
/* 151 */       EntitySheep entitysheep = (EntitySheep)target;
/* 152 */       EnumDyeColor enumdyecolor = EnumDyeColor.byDyeDamage(stack.getMetadata());
/*     */       
/* 154 */       if (!entitysheep.getSheared() && entitysheep.getFleeceColor() != enumdyecolor) {
/*     */         
/* 156 */         entitysheep.setFleeceColor(enumdyecolor);
/* 157 */         stack.stackSize--;
/*     */       } 
/*     */       
/* 160 */       return true;
/*     */     } 
/*     */ 
/*     */     
/* 164 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void getSubItems(Item itemIn, CreativeTabs tab, List<ItemStack> subItems) {
/* 170 */     for (int i = 0; i < 16; i++)
/*     */     {
/* 172 */       subItems.add(new ItemStack(itemIn, 1, i));
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\item\ItemDye.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */