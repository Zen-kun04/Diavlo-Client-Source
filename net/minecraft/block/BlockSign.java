/*    */ package net.minecraft.block;
/*    */ 
/*    */ import java.util.Random;
/*    */ import net.minecraft.block.material.Material;
/*    */ import net.minecraft.block.state.IBlockState;
/*    */ import net.minecraft.entity.player.EntityPlayer;
/*    */ import net.minecraft.init.Items;
/*    */ import net.minecraft.item.Item;
/*    */ import net.minecraft.tileentity.TileEntity;
/*    */ import net.minecraft.tileentity.TileEntitySign;
/*    */ import net.minecraft.util.AxisAlignedBB;
/*    */ import net.minecraft.util.BlockPos;
/*    */ import net.minecraft.util.EnumFacing;
/*    */ import net.minecraft.world.IBlockAccess;
/*    */ import net.minecraft.world.World;
/*    */ 
/*    */ public class BlockSign
/*    */   extends BlockContainer
/*    */ {
/*    */   protected BlockSign() {
/* 21 */     super(Material.wood);
/* 22 */     float f = 0.25F;
/* 23 */     float f1 = 1.0F;
/* 24 */     setBlockBounds(0.5F - f, 0.0F, 0.5F - f, 0.5F + f, f1, 0.5F + f);
/*    */   }
/*    */ 
/*    */   
/*    */   public AxisAlignedBB getCollisionBoundingBox(World worldIn, BlockPos pos, IBlockState state) {
/* 29 */     return null;
/*    */   }
/*    */ 
/*    */   
/*    */   public AxisAlignedBB getSelectedBoundingBox(World worldIn, BlockPos pos) {
/* 34 */     setBlockBoundsBasedOnState((IBlockAccess)worldIn, pos);
/* 35 */     return super.getSelectedBoundingBox(worldIn, pos);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isFullCube() {
/* 40 */     return false;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isPassable(IBlockAccess worldIn, BlockPos pos) {
/* 45 */     return true;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isOpaqueCube() {
/* 50 */     return false;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean canSpawnInBlock() {
/* 55 */     return true;
/*    */   }
/*    */ 
/*    */   
/*    */   public TileEntity createNewTileEntity(World worldIn, int meta) {
/* 60 */     return (TileEntity)new TileEntitySign();
/*    */   }
/*    */ 
/*    */   
/*    */   public Item getItemDropped(IBlockState state, Random rand, int fortune) {
/* 65 */     return Items.sign;
/*    */   }
/*    */ 
/*    */   
/*    */   public Item getItem(World worldIn, BlockPos pos) {
/* 70 */     return Items.sign;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumFacing side, float hitX, float hitY, float hitZ) {
/* 75 */     if (worldIn.isRemote)
/*    */     {
/* 77 */       return true;
/*    */     }
/*    */ 
/*    */     
/* 81 */     TileEntity tileentity = worldIn.getTileEntity(pos);
/* 82 */     return (tileentity instanceof TileEntitySign) ? ((TileEntitySign)tileentity).executeCommand(playerIn) : false;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean canPlaceBlockAt(World worldIn, BlockPos pos) {
/* 88 */     return (!hasInvalidNeighbor(worldIn, pos) && super.canPlaceBlockAt(worldIn, pos));
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\block\BlockSign.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */