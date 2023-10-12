/*    */ package net.minecraft.block;
/*    */ 
/*    */ import java.util.Random;
/*    */ import net.minecraft.block.material.Material;
/*    */ import net.minecraft.block.state.IBlockState;
/*    */ import net.minecraft.item.Item;
/*    */ import net.minecraft.tileentity.TileEntity;
/*    */ import net.minecraft.tileentity.TileEntityMobSpawner;
/*    */ import net.minecraft.util.BlockPos;
/*    */ import net.minecraft.util.EnumWorldBlockLayer;
/*    */ import net.minecraft.world.World;
/*    */ 
/*    */ public class BlockMobSpawner
/*    */   extends BlockContainer
/*    */ {
/*    */   protected BlockMobSpawner() {
/* 17 */     super(Material.rock);
/*    */   }
/*    */ 
/*    */   
/*    */   public TileEntity createNewTileEntity(World worldIn, int meta) {
/* 22 */     return (TileEntity)new TileEntityMobSpawner();
/*    */   }
/*    */ 
/*    */   
/*    */   public Item getItemDropped(IBlockState state, Random rand, int fortune) {
/* 27 */     return null;
/*    */   }
/*    */ 
/*    */   
/*    */   public int quantityDropped(Random random) {
/* 32 */     return 0;
/*    */   }
/*    */ 
/*    */   
/*    */   public void dropBlockAsItemWithChance(World worldIn, BlockPos pos, IBlockState state, float chance, int fortune) {
/* 37 */     super.dropBlockAsItemWithChance(worldIn, pos, state, chance, fortune);
/* 38 */     int i = 15 + worldIn.rand.nextInt(15) + worldIn.rand.nextInt(15);
/* 39 */     dropXpOnBlockBreak(worldIn, pos, i);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isOpaqueCube() {
/* 44 */     return false;
/*    */   }
/*    */ 
/*    */   
/*    */   public int getRenderType() {
/* 49 */     return 3;
/*    */   }
/*    */ 
/*    */   
/*    */   public EnumWorldBlockLayer getBlockLayer() {
/* 54 */     return EnumWorldBlockLayer.CUTOUT;
/*    */   }
/*    */ 
/*    */   
/*    */   public Item getItem(World worldIn, BlockPos pos) {
/* 59 */     return null;
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\block\BlockMobSpawner.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */