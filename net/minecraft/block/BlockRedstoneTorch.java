/*     */ package net.minecraft.block;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import com.google.common.collect.Maps;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Random;
/*     */ import net.minecraft.block.properties.IProperty;
/*     */ import net.minecraft.block.state.IBlockState;
/*     */ import net.minecraft.creativetab.CreativeTabs;
/*     */ import net.minecraft.init.Blocks;
/*     */ import net.minecraft.item.Item;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.util.EnumFacing;
/*     */ import net.minecraft.util.EnumParticleTypes;
/*     */ import net.minecraft.world.IBlockAccess;
/*     */ import net.minecraft.world.World;
/*     */ 
/*     */ public class BlockRedstoneTorch extends BlockTorch {
/*  20 */   private static Map<World, List<Toggle>> toggles = Maps.newHashMap();
/*     */   
/*     */   private final boolean isOn;
/*     */   
/*     */   private boolean isBurnedOut(World worldIn, BlockPos pos, boolean turnOff) {
/*  25 */     if (!toggles.containsKey(worldIn))
/*     */     {
/*  27 */       toggles.put(worldIn, Lists.newArrayList());
/*     */     }
/*     */     
/*  30 */     List<Toggle> list = toggles.get(worldIn);
/*     */     
/*  32 */     if (turnOff)
/*     */     {
/*  34 */       list.add(new Toggle(pos, worldIn.getTotalWorldTime()));
/*     */     }
/*     */     
/*  37 */     int i = 0;
/*     */     
/*  39 */     for (int j = 0; j < list.size(); j++) {
/*     */       
/*  41 */       Toggle blockredstonetorch$toggle = list.get(j);
/*     */       
/*  43 */       if (blockredstonetorch$toggle.pos.equals(pos)) {
/*     */         
/*  45 */         i++;
/*     */         
/*  47 */         if (i >= 8)
/*     */         {
/*  49 */           return true;
/*     */         }
/*     */       } 
/*     */     } 
/*     */     
/*  54 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   protected BlockRedstoneTorch(boolean isOn) {
/*  59 */     this.isOn = isOn;
/*  60 */     setTickRandomly(true);
/*  61 */     setCreativeTab((CreativeTabs)null);
/*     */   }
/*     */ 
/*     */   
/*     */   public int tickRate(World worldIn) {
/*  66 */     return 2;
/*     */   }
/*     */ 
/*     */   
/*     */   public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state) {
/*  71 */     if (this.isOn)
/*     */     {
/*  73 */       for (EnumFacing enumfacing : EnumFacing.values())
/*     */       {
/*  75 */         worldIn.notifyNeighborsOfStateChange(pos.offset(enumfacing), this);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
/*  82 */     if (this.isOn)
/*     */     {
/*  84 */       for (EnumFacing enumfacing : EnumFacing.values())
/*     */       {
/*  86 */         worldIn.notifyNeighborsOfStateChange(pos.offset(enumfacing), this);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public int getWeakPower(IBlockAccess worldIn, BlockPos pos, IBlockState state, EnumFacing side) {
/*  93 */     return (this.isOn && state.getValue((IProperty)FACING) != side) ? 15 : 0;
/*     */   }
/*     */ 
/*     */   
/*     */   private boolean shouldBeOff(World worldIn, BlockPos pos, IBlockState state) {
/*  98 */     EnumFacing enumfacing = ((EnumFacing)state.getValue((IProperty)FACING)).getOpposite();
/*  99 */     return worldIn.isSidePowered(pos.offset(enumfacing), enumfacing);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void randomTick(World worldIn, BlockPos pos, IBlockState state, Random random) {}
/*     */ 
/*     */   
/*     */   public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
/* 108 */     boolean flag = shouldBeOff(worldIn, pos, state);
/* 109 */     List<Toggle> list = toggles.get(worldIn);
/*     */     
/* 111 */     while (list != null && !list.isEmpty() && worldIn.getTotalWorldTime() - ((Toggle)list.get(0)).time > 60L)
/*     */     {
/* 113 */       list.remove(0);
/*     */     }
/*     */     
/* 116 */     if (this.isOn) {
/*     */       
/* 118 */       if (flag) {
/*     */         
/* 120 */         worldIn.setBlockState(pos, Blocks.unlit_redstone_torch.getDefaultState().withProperty((IProperty)FACING, state.getValue((IProperty)FACING)), 3);
/*     */         
/* 122 */         if (isBurnedOut(worldIn, pos, true))
/*     */         {
/* 124 */           worldIn.playSoundEffect((pos.getX() + 0.5F), (pos.getY() + 0.5F), (pos.getZ() + 0.5F), "random.fizz", 0.5F, 2.6F + (worldIn.rand.nextFloat() - worldIn.rand.nextFloat()) * 0.8F);
/*     */           
/* 126 */           for (int i = 0; i < 5; i++) {
/*     */             
/* 128 */             double d0 = pos.getX() + rand.nextDouble() * 0.6D + 0.2D;
/* 129 */             double d1 = pos.getY() + rand.nextDouble() * 0.6D + 0.2D;
/* 130 */             double d2 = pos.getZ() + rand.nextDouble() * 0.6D + 0.2D;
/* 131 */             worldIn.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, d0, d1, d2, 0.0D, 0.0D, 0.0D, new int[0]);
/*     */           } 
/*     */           
/* 134 */           worldIn.scheduleUpdate(pos, worldIn.getBlockState(pos).getBlock(), 160);
/*     */         }
/*     */       
/*     */       } 
/* 138 */     } else if (!flag && !isBurnedOut(worldIn, pos, false)) {
/*     */       
/* 140 */       worldIn.setBlockState(pos, Blocks.redstone_torch.getDefaultState().withProperty((IProperty)FACING, state.getValue((IProperty)FACING)), 3);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void onNeighborBlockChange(World worldIn, BlockPos pos, IBlockState state, Block neighborBlock) {
/* 146 */     if (!onNeighborChangeInternal(worldIn, pos, state))
/*     */     {
/* 148 */       if (this.isOn == shouldBeOff(worldIn, pos, state))
/*     */       {
/* 150 */         worldIn.scheduleUpdate(pos, this, tickRate(worldIn));
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public int getStrongPower(IBlockAccess worldIn, BlockPos pos, IBlockState state, EnumFacing side) {
/* 157 */     return (side == EnumFacing.DOWN) ? getWeakPower(worldIn, pos, state, side) : 0;
/*     */   }
/*     */ 
/*     */   
/*     */   public Item getItemDropped(IBlockState state, Random rand, int fortune) {
/* 162 */     return Item.getItemFromBlock(Blocks.redstone_torch);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canProvidePower() {
/* 167 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public void randomDisplayTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
/* 172 */     if (this.isOn) {
/*     */       
/* 174 */       double d0 = pos.getX() + 0.5D + (rand.nextDouble() - 0.5D) * 0.2D;
/* 175 */       double d1 = pos.getY() + 0.7D + (rand.nextDouble() - 0.5D) * 0.2D;
/* 176 */       double d2 = pos.getZ() + 0.5D + (rand.nextDouble() - 0.5D) * 0.2D;
/* 177 */       EnumFacing enumfacing = (EnumFacing)state.getValue((IProperty)FACING);
/*     */       
/* 179 */       if (enumfacing.getAxis().isHorizontal()) {
/*     */         
/* 181 */         EnumFacing enumfacing1 = enumfacing.getOpposite();
/* 182 */         double d3 = 0.27D;
/* 183 */         d0 += 0.27D * enumfacing1.getFrontOffsetX();
/* 184 */         d1 += 0.22D;
/* 185 */         d2 += 0.27D * enumfacing1.getFrontOffsetZ();
/*     */       } 
/*     */       
/* 188 */       worldIn.spawnParticle(EnumParticleTypes.REDSTONE, d0, d1, d2, 0.0D, 0.0D, 0.0D, new int[0]);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public Item getItem(World worldIn, BlockPos pos) {
/* 194 */     return Item.getItemFromBlock(Blocks.redstone_torch);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isAssociatedBlock(Block other) {
/* 199 */     return (other == Blocks.unlit_redstone_torch || other == Blocks.redstone_torch);
/*     */   }
/*     */ 
/*     */   
/*     */   static class Toggle
/*     */   {
/*     */     BlockPos pos;
/*     */     long time;
/*     */     
/*     */     public Toggle(BlockPos pos, long time) {
/* 209 */       this.pos = pos;
/* 210 */       this.time = time;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\block\BlockRedstoneTorch.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */