/*     */ package net.minecraft.block;
/*     */ 
/*     */ import java.util.Random;
/*     */ import net.minecraft.block.material.Material;
/*     */ import net.minecraft.block.state.IBlockState;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.init.Blocks;
/*     */ import net.minecraft.init.Items;
/*     */ import net.minecraft.item.Item;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.util.EnumFacing;
/*     */ import net.minecraft.util.EnumParticleTypes;
/*     */ import net.minecraft.world.World;
/*     */ 
/*     */ public class BlockRedstoneOre
/*     */   extends Block
/*     */ {
/*     */   private final boolean isOn;
/*     */   
/*     */   public BlockRedstoneOre(boolean isOn) {
/*  23 */     super(Material.rock);
/*     */     
/*  25 */     if (isOn)
/*     */     {
/*  27 */       setTickRandomly(true);
/*     */     }
/*     */     
/*  30 */     this.isOn = isOn;
/*     */   }
/*     */ 
/*     */   
/*     */   public int tickRate(World worldIn) {
/*  35 */     return 30;
/*     */   }
/*     */ 
/*     */   
/*     */   public void onBlockClicked(World worldIn, BlockPos pos, EntityPlayer playerIn) {
/*  40 */     activate(worldIn, pos);
/*  41 */     super.onBlockClicked(worldIn, pos, playerIn);
/*     */   }
/*     */ 
/*     */   
/*     */   public void onEntityCollidedWithBlock(World worldIn, BlockPos pos, Entity entityIn) {
/*  46 */     activate(worldIn, pos);
/*  47 */     super.onEntityCollidedWithBlock(worldIn, pos, entityIn);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumFacing side, float hitX, float hitY, float hitZ) {
/*  52 */     activate(worldIn, pos);
/*  53 */     return super.onBlockActivated(worldIn, pos, state, playerIn, side, hitX, hitY, hitZ);
/*     */   }
/*     */ 
/*     */   
/*     */   private void activate(World worldIn, BlockPos pos) {
/*  58 */     spawnParticles(worldIn, pos);
/*     */     
/*  60 */     if (this == Blocks.redstone_ore)
/*     */     {
/*  62 */       worldIn.setBlockState(pos, Blocks.lit_redstone_ore.getDefaultState());
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
/*  68 */     if (this == Blocks.lit_redstone_ore)
/*     */     {
/*  70 */       worldIn.setBlockState(pos, Blocks.redstone_ore.getDefaultState());
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public Item getItemDropped(IBlockState state, Random rand, int fortune) {
/*  76 */     return Items.redstone;
/*     */   }
/*     */ 
/*     */   
/*     */   public int quantityDroppedWithBonus(int fortune, Random random) {
/*  81 */     return quantityDropped(random) + random.nextInt(fortune + 1);
/*     */   }
/*     */ 
/*     */   
/*     */   public int quantityDropped(Random random) {
/*  86 */     return 4 + random.nextInt(2);
/*     */   }
/*     */ 
/*     */   
/*     */   public void dropBlockAsItemWithChance(World worldIn, BlockPos pos, IBlockState state, float chance, int fortune) {
/*  91 */     super.dropBlockAsItemWithChance(worldIn, pos, state, chance, fortune);
/*     */     
/*  93 */     if (getItemDropped(state, worldIn.rand, fortune) != Item.getItemFromBlock(this)) {
/*     */       
/*  95 */       int i = 1 + worldIn.rand.nextInt(5);
/*  96 */       dropXpOnBlockBreak(worldIn, pos, i);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void randomDisplayTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
/* 102 */     if (this.isOn)
/*     */     {
/* 104 */       spawnParticles(worldIn, pos);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   private void spawnParticles(World worldIn, BlockPos pos) {
/* 110 */     Random random = worldIn.rand;
/* 111 */     double d0 = 0.0625D;
/*     */     
/* 113 */     for (int i = 0; i < 6; i++) {
/*     */       
/* 115 */       double d1 = (pos.getX() + random.nextFloat());
/* 116 */       double d2 = (pos.getY() + random.nextFloat());
/* 117 */       double d3 = (pos.getZ() + random.nextFloat());
/*     */       
/* 119 */       if (i == 0 && !worldIn.getBlockState(pos.up()).getBlock().isOpaqueCube())
/*     */       {
/* 121 */         d2 = pos.getY() + d0 + 1.0D;
/*     */       }
/*     */       
/* 124 */       if (i == 1 && !worldIn.getBlockState(pos.down()).getBlock().isOpaqueCube())
/*     */       {
/* 126 */         d2 = pos.getY() - d0;
/*     */       }
/*     */       
/* 129 */       if (i == 2 && !worldIn.getBlockState(pos.south()).getBlock().isOpaqueCube())
/*     */       {
/* 131 */         d3 = pos.getZ() + d0 + 1.0D;
/*     */       }
/*     */       
/* 134 */       if (i == 3 && !worldIn.getBlockState(pos.north()).getBlock().isOpaqueCube())
/*     */       {
/* 136 */         d3 = pos.getZ() - d0;
/*     */       }
/*     */       
/* 139 */       if (i == 4 && !worldIn.getBlockState(pos.east()).getBlock().isOpaqueCube())
/*     */       {
/* 141 */         d1 = pos.getX() + d0 + 1.0D;
/*     */       }
/*     */       
/* 144 */       if (i == 5 && !worldIn.getBlockState(pos.west()).getBlock().isOpaqueCube())
/*     */       {
/* 146 */         d1 = pos.getX() - d0;
/*     */       }
/*     */       
/* 149 */       if (d1 < pos.getX() || d1 > (pos.getX() + 1) || d2 < 0.0D || d2 > (pos.getY() + 1) || d3 < pos.getZ() || d3 > (pos.getZ() + 1))
/*     */       {
/* 151 */         worldIn.spawnParticle(EnumParticleTypes.REDSTONE, d1, d2, d3, 0.0D, 0.0D, 0.0D, new int[0]);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected ItemStack createStackedBlock(IBlockState state) {
/* 158 */     return new ItemStack(Blocks.redstone_ore);
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\block\BlockRedstoneOre.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */