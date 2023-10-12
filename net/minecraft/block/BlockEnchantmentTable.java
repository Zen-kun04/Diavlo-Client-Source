/*     */ package net.minecraft.block;
/*     */ 
/*     */ import java.util.Random;
/*     */ import net.minecraft.block.material.MapColor;
/*     */ import net.minecraft.block.material.Material;
/*     */ import net.minecraft.block.state.IBlockState;
/*     */ import net.minecraft.creativetab.CreativeTabs;
/*     */ import net.minecraft.entity.EntityLivingBase;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.init.Blocks;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import net.minecraft.tileentity.TileEntity;
/*     */ import net.minecraft.tileentity.TileEntityEnchantmentTable;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.util.EnumFacing;
/*     */ import net.minecraft.util.EnumParticleTypes;
/*     */ import net.minecraft.world.IInteractionObject;
/*     */ import net.minecraft.world.World;
/*     */ 
/*     */ public class BlockEnchantmentTable
/*     */   extends BlockContainer {
/*     */   protected BlockEnchantmentTable() {
/*  23 */     super(Material.rock, MapColor.redColor);
/*  24 */     setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.75F, 1.0F);
/*  25 */     setLightOpacity(0);
/*  26 */     setCreativeTab(CreativeTabs.tabDecorations);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isFullCube() {
/*  31 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public void randomDisplayTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
/*  36 */     super.randomDisplayTick(worldIn, pos, state, rand);
/*     */     
/*  38 */     for (int i = -2; i <= 2; i++) {
/*     */       
/*  40 */       for (int j = -2; j <= 2; j++) {
/*     */         
/*  42 */         if (i > -2 && i < 2 && j == -1)
/*     */         {
/*  44 */           j = 2;
/*     */         }
/*     */         
/*  47 */         if (rand.nextInt(16) == 0)
/*     */         {
/*  49 */           for (int k = 0; k <= 1; k++) {
/*     */             
/*  51 */             BlockPos blockpos = pos.add(i, k, j);
/*     */             
/*  53 */             if (worldIn.getBlockState(blockpos).getBlock() == Blocks.bookshelf) {
/*     */               
/*  55 */               if (!worldIn.isAirBlock(pos.add(i / 2, 0, j / 2))) {
/*     */                 break;
/*     */               }
/*     */ 
/*     */               
/*  60 */               worldIn.spawnParticle(EnumParticleTypes.ENCHANTMENT_TABLE, pos.getX() + 0.5D, pos.getY() + 2.0D, pos.getZ() + 0.5D, (i + rand.nextFloat()) - 0.5D, (k - rand.nextFloat() - 1.0F), (j + rand.nextFloat()) - 0.5D, new int[0]);
/*     */             } 
/*     */           } 
/*     */         }
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isOpaqueCube() {
/*  70 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getRenderType() {
/*  75 */     return 3;
/*     */   }
/*     */ 
/*     */   
/*     */   public TileEntity createNewTileEntity(World worldIn, int meta) {
/*  80 */     return (TileEntity)new TileEntityEnchantmentTable();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumFacing side, float hitX, float hitY, float hitZ) {
/*  85 */     if (worldIn.isRemote)
/*     */     {
/*  87 */       return true;
/*     */     }
/*     */ 
/*     */     
/*  91 */     TileEntity tileentity = worldIn.getTileEntity(pos);
/*     */     
/*  93 */     if (tileentity instanceof TileEntityEnchantmentTable)
/*     */     {
/*  95 */       playerIn.displayGui((IInteractionObject)tileentity);
/*     */     }
/*     */     
/*  98 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
/* 104 */     super.onBlockPlacedBy(worldIn, pos, state, placer, stack);
/*     */     
/* 106 */     if (stack.hasDisplayName()) {
/*     */       
/* 108 */       TileEntity tileentity = worldIn.getTileEntity(pos);
/*     */       
/* 110 */       if (tileentity instanceof TileEntityEnchantmentTable)
/*     */       {
/* 112 */         ((TileEntityEnchantmentTable)tileentity).setCustomName(stack.getDisplayName());
/*     */       }
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\block\BlockEnchantmentTable.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */