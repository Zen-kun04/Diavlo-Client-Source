/*    */ package net.minecraft.block;
/*    */ 
/*    */ import net.minecraft.block.material.MapColor;
/*    */ import net.minecraft.block.material.Material;
/*    */ import net.minecraft.block.properties.IProperty;
/*    */ import net.minecraft.block.state.BlockState;
/*    */ import net.minecraft.block.state.IBlockState;
/*    */ import net.minecraft.creativetab.CreativeTabs;
/*    */ import net.minecraft.entity.EntityLivingBase;
/*    */ import net.minecraft.item.Item;
/*    */ import net.minecraft.item.ItemStack;
/*    */ import net.minecraft.util.BlockPos;
/*    */ import net.minecraft.util.EnumFacing;
/*    */ import net.minecraft.world.World;
/*    */ 
/*    */ public class BlockHay
/*    */   extends BlockRotatedPillar
/*    */ {
/*    */   public BlockHay() {
/* 20 */     super(Material.grass, MapColor.yellowColor);
/* 21 */     setDefaultState(this.blockState.getBaseState().withProperty((IProperty)AXIS, (Comparable)EnumFacing.Axis.Y));
/* 22 */     setCreativeTab(CreativeTabs.tabBlock);
/*    */   }
/*    */ 
/*    */   
/*    */   public IBlockState getStateFromMeta(int meta) {
/* 27 */     EnumFacing.Axis enumfacing$axis = EnumFacing.Axis.Y;
/* 28 */     int i = meta & 0xC;
/*    */     
/* 30 */     if (i == 4) {
/*    */       
/* 32 */       enumfacing$axis = EnumFacing.Axis.X;
/*    */     }
/* 34 */     else if (i == 8) {
/*    */       
/* 36 */       enumfacing$axis = EnumFacing.Axis.Z;
/*    */     } 
/*    */     
/* 39 */     return getDefaultState().withProperty((IProperty)AXIS, (Comparable)enumfacing$axis);
/*    */   }
/*    */ 
/*    */   
/*    */   public int getMetaFromState(IBlockState state) {
/* 44 */     int i = 0;
/* 45 */     EnumFacing.Axis enumfacing$axis = (EnumFacing.Axis)state.getValue((IProperty)AXIS);
/*    */     
/* 47 */     if (enumfacing$axis == EnumFacing.Axis.X) {
/*    */       
/* 49 */       i |= 0x4;
/*    */     }
/* 51 */     else if (enumfacing$axis == EnumFacing.Axis.Z) {
/*    */       
/* 53 */       i |= 0x8;
/*    */     } 
/*    */     
/* 56 */     return i;
/*    */   }
/*    */ 
/*    */   
/*    */   protected BlockState createBlockState() {
/* 61 */     return new BlockState(this, new IProperty[] { (IProperty)AXIS });
/*    */   }
/*    */ 
/*    */   
/*    */   protected ItemStack createStackedBlock(IBlockState state) {
/* 66 */     return new ItemStack(Item.getItemFromBlock(this), 1, 0);
/*    */   }
/*    */ 
/*    */   
/*    */   public IBlockState onBlockPlaced(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
/* 71 */     return super.onBlockPlaced(worldIn, pos, facing, hitX, hitY, hitZ, meta, placer).withProperty((IProperty)AXIS, (Comparable)facing.getAxis());
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\block\BlockHay.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */