/*    */ package net.minecraft.block;
/*    */ 
/*    */ import java.util.List;
/*    */ import net.minecraft.block.material.Material;
/*    */ import net.minecraft.block.properties.IProperty;
/*    */ import net.minecraft.block.properties.PropertyBool;
/*    */ import net.minecraft.block.state.BlockState;
/*    */ import net.minecraft.block.state.IBlockState;
/*    */ import net.minecraft.entity.Entity;
/*    */ import net.minecraft.entity.EntityLivingBase;
/*    */ import net.minecraft.util.AxisAlignedBB;
/*    */ import net.minecraft.util.BlockPos;
/*    */ import net.minecraft.world.World;
/*    */ 
/*    */ public class BlockPressurePlate
/*    */   extends BlockBasePressurePlate {
/* 17 */   public static final PropertyBool POWERED = PropertyBool.create("powered");
/*    */   
/*    */   private final Sensitivity sensitivity;
/*    */   
/*    */   protected BlockPressurePlate(Material materialIn, Sensitivity sensitivityIn) {
/* 22 */     super(materialIn);
/* 23 */     setDefaultState(this.blockState.getBaseState().withProperty((IProperty)POWERED, Boolean.valueOf(false)));
/* 24 */     this.sensitivity = sensitivityIn;
/*    */   }
/*    */ 
/*    */   
/*    */   protected int getRedstoneStrength(IBlockState state) {
/* 29 */     return ((Boolean)state.getValue((IProperty)POWERED)).booleanValue() ? 15 : 0;
/*    */   }
/*    */ 
/*    */   
/*    */   protected IBlockState setRedstoneStrength(IBlockState state, int strength) {
/* 34 */     return state.withProperty((IProperty)POWERED, Boolean.valueOf((strength > 0)));
/*    */   }
/*    */   
/*    */   protected int computeRedstoneStrength(World worldIn, BlockPos pos) {
/*    */     List<? extends Entity> list;
/* 39 */     AxisAlignedBB axisalignedbb = getSensitiveAABB(pos);
/*    */ 
/*    */     
/* 42 */     switch (this.sensitivity) {
/*    */       
/*    */       case EVERYTHING:
/* 45 */         list = worldIn.getEntitiesWithinAABBExcludingEntity((Entity)null, axisalignedbb);
/*    */         break;
/*    */       
/*    */       case MOBS:
/* 49 */         list = worldIn.getEntitiesWithinAABB(EntityLivingBase.class, axisalignedbb);
/*    */         break;
/*    */       
/*    */       default:
/* 53 */         return 0;
/*    */     } 
/*    */     
/* 56 */     if (!list.isEmpty())
/*    */     {
/* 58 */       for (Entity entity : list) {
/*    */         
/* 60 */         if (!entity.doesEntityNotTriggerPressurePlate())
/*    */         {
/* 62 */           return 15;
/*    */         }
/*    */       } 
/*    */     }
/*    */     
/* 67 */     return 0;
/*    */   }
/*    */ 
/*    */   
/*    */   public IBlockState getStateFromMeta(int meta) {
/* 72 */     return getDefaultState().withProperty((IProperty)POWERED, Boolean.valueOf((meta == 1)));
/*    */   }
/*    */ 
/*    */   
/*    */   public int getMetaFromState(IBlockState state) {
/* 77 */     return ((Boolean)state.getValue((IProperty)POWERED)).booleanValue() ? 1 : 0;
/*    */   }
/*    */ 
/*    */   
/*    */   protected BlockState createBlockState() {
/* 82 */     return new BlockState(this, new IProperty[] { (IProperty)POWERED });
/*    */   }
/*    */   
/*    */   public enum Sensitivity
/*    */   {
/* 87 */     EVERYTHING,
/* 88 */     MOBS;
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\block\BlockPressurePlate.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */