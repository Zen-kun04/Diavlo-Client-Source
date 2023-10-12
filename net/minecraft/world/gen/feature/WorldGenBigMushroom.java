/*     */ package net.minecraft.world.gen.feature;
/*     */ 
/*     */ import java.util.Random;
/*     */ import net.minecraft.block.Block;
/*     */ import net.minecraft.block.BlockHugeMushroom;
/*     */ import net.minecraft.block.material.Material;
/*     */ import net.minecraft.block.properties.IProperty;
/*     */ import net.minecraft.init.Blocks;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.world.World;
/*     */ 
/*     */ public class WorldGenBigMushroom
/*     */   extends WorldGenerator {
/*     */   private Block mushroomType;
/*     */   
/*     */   public WorldGenBigMushroom(Block p_i46449_1_) {
/*  17 */     super(true);
/*  18 */     this.mushroomType = p_i46449_1_;
/*     */   }
/*     */ 
/*     */   
/*     */   public WorldGenBigMushroom() {
/*  23 */     super(false);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean generate(World worldIn, Random rand, BlockPos position) {
/*  28 */     if (this.mushroomType == null)
/*     */     {
/*  30 */       this.mushroomType = rand.nextBoolean() ? Blocks.brown_mushroom_block : Blocks.red_mushroom_block;
/*     */     }
/*     */     
/*  33 */     int i = rand.nextInt(3) + 4;
/*  34 */     boolean flag = true;
/*     */     
/*  36 */     if (position.getY() >= 1 && position.getY() + i + 1 < 256) {
/*     */       
/*  38 */       for (int j = position.getY(); j <= position.getY() + 1 + i; j++) {
/*     */         
/*  40 */         int k = 3;
/*     */         
/*  42 */         if (j <= position.getY() + 3)
/*     */         {
/*  44 */           k = 0;
/*     */         }
/*     */         
/*  47 */         BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();
/*     */         
/*  49 */         for (int l = position.getX() - k; l <= position.getX() + k && flag; l++) {
/*     */           
/*  51 */           for (int i1 = position.getZ() - k; i1 <= position.getZ() + k && flag; i1++) {
/*     */             
/*  53 */             if (j >= 0 && j < 256) {
/*     */               
/*  55 */               Block block = worldIn.getBlockState((BlockPos)blockpos$mutableblockpos.set(l, j, i1)).getBlock();
/*     */               
/*  57 */               if (block.getMaterial() != Material.air && block.getMaterial() != Material.leaves)
/*     */               {
/*  59 */                 flag = false;
/*     */               }
/*     */             }
/*     */             else {
/*     */               
/*  64 */               flag = false;
/*     */             } 
/*     */           } 
/*     */         } 
/*     */       } 
/*     */       
/*  70 */       if (!flag)
/*     */       {
/*  72 */         return false;
/*     */       }
/*     */ 
/*     */       
/*  76 */       Block block1 = worldIn.getBlockState(position.down()).getBlock();
/*     */       
/*  78 */       if (block1 != Blocks.dirt && block1 != Blocks.grass && block1 != Blocks.mycelium)
/*     */       {
/*  80 */         return false;
/*     */       }
/*     */ 
/*     */       
/*  84 */       int k2 = position.getY() + i;
/*     */       
/*  86 */       if (this.mushroomType == Blocks.red_mushroom_block)
/*     */       {
/*  88 */         k2 = position.getY() + i - 3;
/*     */       }
/*     */       
/*  91 */       for (int l2 = k2; l2 <= position.getY() + i; l2++) {
/*     */         
/*  93 */         int j3 = 1;
/*     */         
/*  95 */         if (l2 < position.getY() + i)
/*     */         {
/*  97 */           j3++;
/*     */         }
/*     */         
/* 100 */         if (this.mushroomType == Blocks.brown_mushroom_block)
/*     */         {
/* 102 */           j3 = 3;
/*     */         }
/*     */         
/* 105 */         int k3 = position.getX() - j3;
/* 106 */         int l3 = position.getX() + j3;
/* 107 */         int j1 = position.getZ() - j3;
/* 108 */         int k1 = position.getZ() + j3;
/*     */         
/* 110 */         for (int l1 = k3; l1 <= l3; l1++) {
/*     */           
/* 112 */           for (int i2 = j1; i2 <= k1; i2++) {
/*     */             
/* 114 */             int j2 = 5;
/*     */             
/* 116 */             if (l1 == k3) {
/*     */               
/* 118 */               j2--;
/*     */             }
/* 120 */             else if (l1 == l3) {
/*     */               
/* 122 */               j2++;
/*     */             } 
/*     */             
/* 125 */             if (i2 == j1) {
/*     */               
/* 127 */               j2 -= 3;
/*     */             }
/* 129 */             else if (i2 == k1) {
/*     */               
/* 131 */               j2 += 3;
/*     */             } 
/*     */             
/* 134 */             BlockHugeMushroom.EnumType blockhugemushroom$enumtype = BlockHugeMushroom.EnumType.byMetadata(j2);
/*     */             
/* 136 */             if (this.mushroomType == Blocks.brown_mushroom_block || l2 < position.getY() + i) {
/*     */               
/* 138 */               if ((l1 == k3 || l1 == l3) && (i2 == j1 || i2 == k1)) {
/*     */                 continue;
/*     */               }
/*     */ 
/*     */               
/* 143 */               if (l1 == position.getX() - j3 - 1 && i2 == j1)
/*     */               {
/* 145 */                 blockhugemushroom$enumtype = BlockHugeMushroom.EnumType.NORTH_WEST;
/*     */               }
/*     */               
/* 148 */               if (l1 == k3 && i2 == position.getZ() - j3 - 1)
/*     */               {
/* 150 */                 blockhugemushroom$enumtype = BlockHugeMushroom.EnumType.NORTH_WEST;
/*     */               }
/*     */               
/* 153 */               if (l1 == position.getX() + j3 - 1 && i2 == j1)
/*     */               {
/* 155 */                 blockhugemushroom$enumtype = BlockHugeMushroom.EnumType.NORTH_EAST;
/*     */               }
/*     */               
/* 158 */               if (l1 == l3 && i2 == position.getZ() - j3 - 1)
/*     */               {
/* 160 */                 blockhugemushroom$enumtype = BlockHugeMushroom.EnumType.NORTH_EAST;
/*     */               }
/*     */               
/* 163 */               if (l1 == position.getX() - j3 - 1 && i2 == k1)
/*     */               {
/* 165 */                 blockhugemushroom$enumtype = BlockHugeMushroom.EnumType.SOUTH_WEST;
/*     */               }
/*     */               
/* 168 */               if (l1 == k3 && i2 == position.getZ() + j3 - 1)
/*     */               {
/* 170 */                 blockhugemushroom$enumtype = BlockHugeMushroom.EnumType.SOUTH_WEST;
/*     */               }
/*     */               
/* 173 */               if (l1 == position.getX() + j3 - 1 && i2 == k1)
/*     */               {
/* 175 */                 blockhugemushroom$enumtype = BlockHugeMushroom.EnumType.SOUTH_EAST;
/*     */               }
/*     */               
/* 178 */               if (l1 == l3 && i2 == position.getZ() + j3 - 1)
/*     */               {
/* 180 */                 blockhugemushroom$enumtype = BlockHugeMushroom.EnumType.SOUTH_EAST;
/*     */               }
/*     */             } 
/*     */             
/* 184 */             if (blockhugemushroom$enumtype == BlockHugeMushroom.EnumType.CENTER && l2 < position.getY() + i)
/*     */             {
/* 186 */               blockhugemushroom$enumtype = BlockHugeMushroom.EnumType.ALL_INSIDE;
/*     */             }
/*     */             
/* 189 */             if (position.getY() >= position.getY() + i - 1 || blockhugemushroom$enumtype != BlockHugeMushroom.EnumType.ALL_INSIDE) {
/*     */               
/* 191 */               BlockPos blockpos = new BlockPos(l1, l2, i2);
/*     */               
/* 193 */               if (!worldIn.getBlockState(blockpos).getBlock().isFullBlock())
/*     */               {
/* 195 */                 setBlockAndNotifyAdequately(worldIn, blockpos, this.mushroomType.getDefaultState().withProperty((IProperty)BlockHugeMushroom.VARIANT, (Comparable)blockhugemushroom$enumtype));
/*     */               }
/*     */             } 
/*     */             continue;
/*     */           } 
/*     */         } 
/*     */       } 
/* 202 */       for (int i3 = 0; i3 < i; i3++) {
/*     */         
/* 204 */         Block block2 = worldIn.getBlockState(position.up(i3)).getBlock();
/*     */         
/* 206 */         if (!block2.isFullBlock())
/*     */         {
/* 208 */           setBlockAndNotifyAdequately(worldIn, position.up(i3), this.mushroomType.getDefaultState().withProperty((IProperty)BlockHugeMushroom.VARIANT, (Comparable)BlockHugeMushroom.EnumType.STEM));
/*     */         }
/*     */       } 
/*     */       
/* 212 */       return true;
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 218 */     return false;
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\world\gen\feature\WorldGenBigMushroom.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */