/*    */ package net.minecraft.block;
/*    */ 
/*    */ import net.minecraft.block.material.Material;
/*    */ import net.minecraft.block.properties.IProperty;
/*    */ import net.minecraft.block.properties.PropertyEnum;
/*    */ import net.minecraft.block.state.IBlockState;
/*    */ import net.minecraft.creativetab.CreativeTabs;
/*    */ import net.minecraft.entity.EntityLivingBase;
/*    */ import net.minecraft.util.BlockPos;
/*    */ import net.minecraft.util.EnumFacing;
/*    */ import net.minecraft.util.IStringSerializable;
/*    */ import net.minecraft.world.World;
/*    */ 
/*    */ public abstract class BlockLog extends BlockRotatedPillar {
/* 15 */   public static final PropertyEnum<EnumAxis> LOG_AXIS = PropertyEnum.create("axis", EnumAxis.class);
/*    */ 
/*    */   
/*    */   public BlockLog() {
/* 19 */     super(Material.wood);
/* 20 */     setCreativeTab(CreativeTabs.tabBlock);
/* 21 */     setHardness(2.0F);
/* 22 */     setStepSound(soundTypeWood);
/*    */   }
/*    */ 
/*    */   
/*    */   public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
/* 27 */     int i = 4;
/* 28 */     int j = i + 1;
/*    */     
/* 30 */     if (worldIn.isAreaLoaded(pos.add(-j, -j, -j), pos.add(j, j, j)))
/*    */     {
/* 32 */       for (BlockPos blockpos : BlockPos.getAllInBox(pos.add(-i, -i, -i), pos.add(i, i, i))) {
/*    */         
/* 34 */         IBlockState iblockstate = worldIn.getBlockState(blockpos);
/*    */         
/* 36 */         if (iblockstate.getBlock().getMaterial() == Material.leaves && !((Boolean)iblockstate.getValue((IProperty)BlockLeaves.CHECK_DECAY)).booleanValue())
/*    */         {
/* 38 */           worldIn.setBlockState(blockpos, iblockstate.withProperty((IProperty)BlockLeaves.CHECK_DECAY, Boolean.valueOf(true)), 4);
/*    */         }
/*    */       } 
/*    */     }
/*    */   }
/*    */ 
/*    */   
/*    */   public IBlockState onBlockPlaced(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
/* 46 */     return super.onBlockPlaced(worldIn, pos, facing, hitX, hitY, hitZ, meta, placer).withProperty((IProperty)LOG_AXIS, EnumAxis.fromFacingAxis(facing.getAxis()));
/*    */   }
/*    */   
/*    */   public enum EnumAxis
/*    */     implements IStringSerializable {
/* 51 */     X("x"),
/* 52 */     Y("y"),
/* 53 */     Z("z"),
/* 54 */     NONE("none");
/*    */     
/*    */     private final String name;
/*    */ 
/*    */     
/*    */     EnumAxis(String name) {
/* 60 */       this.name = name;
/*    */     }
/*    */ 
/*    */     
/*    */     public String toString() {
/* 65 */       return this.name;
/*    */     }
/*    */ 
/*    */     
/*    */     public static EnumAxis fromFacingAxis(EnumFacing.Axis axis) {
/* 70 */       switch (axis) {
/*    */         
/*    */         case X:
/* 73 */           return X;
/*    */         
/*    */         case Y:
/* 76 */           return Y;
/*    */         
/*    */         case Z:
/* 79 */           return Z;
/*    */       } 
/*    */       
/* 82 */       return NONE;
/*    */     }
/*    */ 
/*    */ 
/*    */     
/*    */     public String getName() {
/* 88 */       return this.name;
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\block\BlockLog.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */