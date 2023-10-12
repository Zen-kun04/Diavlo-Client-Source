/*    */ package net.minecraft.dispenser;
/*    */ 
/*    */ import net.minecraft.block.BlockDispenser;
/*    */ import net.minecraft.entity.Entity;
/*    */ import net.minecraft.entity.item.EntityItem;
/*    */ import net.minecraft.item.ItemStack;
/*    */ import net.minecraft.util.EnumFacing;
/*    */ import net.minecraft.world.World;
/*    */ 
/*    */ public class BehaviorDefaultDispenseItem
/*    */   implements IBehaviorDispenseItem {
/*    */   public final ItemStack dispense(IBlockSource source, ItemStack stack) {
/* 13 */     ItemStack itemstack = dispenseStack(source, stack);
/* 14 */     playDispenseSound(source);
/* 15 */     spawnDispenseParticles(source, BlockDispenser.getFacing(source.getBlockMetadata()));
/* 16 */     return itemstack;
/*    */   }
/*    */ 
/*    */   
/*    */   protected ItemStack dispenseStack(IBlockSource source, ItemStack stack) {
/* 21 */     EnumFacing enumfacing = BlockDispenser.getFacing(source.getBlockMetadata());
/* 22 */     IPosition iposition = BlockDispenser.getDispensePosition(source);
/* 23 */     ItemStack itemstack = stack.splitStack(1);
/* 24 */     doDispense(source.getWorld(), itemstack, 6, enumfacing, iposition);
/* 25 */     return stack;
/*    */   }
/*    */ 
/*    */   
/*    */   public static void doDispense(World worldIn, ItemStack stack, int speed, EnumFacing facing, IPosition position) {
/* 30 */     double d0 = position.getX();
/* 31 */     double d1 = position.getY();
/* 32 */     double d2 = position.getZ();
/*    */     
/* 34 */     if (facing.getAxis() == EnumFacing.Axis.Y) {
/*    */       
/* 36 */       d1 -= 0.125D;
/*    */     }
/*    */     else {
/*    */       
/* 40 */       d1 -= 0.15625D;
/*    */     } 
/*    */     
/* 43 */     EntityItem entityitem = new EntityItem(worldIn, d0, d1, d2, stack);
/* 44 */     double d3 = worldIn.rand.nextDouble() * 0.1D + 0.2D;
/* 45 */     entityitem.motionX = facing.getFrontOffsetX() * d3;
/* 46 */     entityitem.motionY = 0.20000000298023224D;
/* 47 */     entityitem.motionZ = facing.getFrontOffsetZ() * d3;
/* 48 */     entityitem.motionX += worldIn.rand.nextGaussian() * 0.007499999832361937D * speed;
/* 49 */     entityitem.motionY += worldIn.rand.nextGaussian() * 0.007499999832361937D * speed;
/* 50 */     entityitem.motionZ += worldIn.rand.nextGaussian() * 0.007499999832361937D * speed;
/* 51 */     worldIn.spawnEntityInWorld((Entity)entityitem);
/*    */   }
/*    */ 
/*    */   
/*    */   protected void playDispenseSound(IBlockSource source) {
/* 56 */     source.getWorld().playAuxSFX(1000, source.getBlockPos(), 0);
/*    */   }
/*    */ 
/*    */   
/*    */   protected void spawnDispenseParticles(IBlockSource source, EnumFacing facingIn) {
/* 61 */     source.getWorld().playAuxSFX(2000, source.getBlockPos(), func_82488_a(facingIn));
/*    */   }
/*    */ 
/*    */   
/*    */   private int func_82488_a(EnumFacing facingIn) {
/* 66 */     return facingIn.getFrontOffsetX() + 1 + (facingIn.getFrontOffsetZ() + 1) * 3;
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\dispenser\BehaviorDefaultDispenseItem.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */