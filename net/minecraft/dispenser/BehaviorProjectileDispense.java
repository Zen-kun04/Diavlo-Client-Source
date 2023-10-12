/*    */ package net.minecraft.dispenser;
/*    */ 
/*    */ import net.minecraft.block.BlockDispenser;
/*    */ import net.minecraft.entity.Entity;
/*    */ import net.minecraft.entity.IProjectile;
/*    */ import net.minecraft.item.ItemStack;
/*    */ import net.minecraft.util.EnumFacing;
/*    */ import net.minecraft.world.World;
/*    */ 
/*    */ public abstract class BehaviorProjectileDispense
/*    */   extends BehaviorDefaultDispenseItem
/*    */ {
/*    */   public ItemStack dispenseStack(IBlockSource source, ItemStack stack) {
/* 14 */     World world = source.getWorld();
/* 15 */     IPosition iposition = BlockDispenser.getDispensePosition(source);
/* 16 */     EnumFacing enumfacing = BlockDispenser.getFacing(source.getBlockMetadata());
/* 17 */     IProjectile iprojectile = getProjectileEntity(world, iposition);
/* 18 */     iprojectile.setThrowableHeading(enumfacing.getFrontOffsetX(), (enumfacing.getFrontOffsetY() + 0.1F), enumfacing.getFrontOffsetZ(), func_82500_b(), func_82498_a());
/* 19 */     world.spawnEntityInWorld((Entity)iprojectile);
/* 20 */     stack.splitStack(1);
/* 21 */     return stack;
/*    */   }
/*    */ 
/*    */   
/*    */   protected void playDispenseSound(IBlockSource source) {
/* 26 */     source.getWorld().playAuxSFX(1002, source.getBlockPos(), 0);
/*    */   }
/*    */ 
/*    */   
/*    */   protected abstract IProjectile getProjectileEntity(World paramWorld, IPosition paramIPosition);
/*    */   
/*    */   protected float func_82498_a() {
/* 33 */     return 6.0F;
/*    */   }
/*    */ 
/*    */   
/*    */   protected float func_82500_b() {
/* 38 */     return 1.1F;
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\dispenser\BehaviorProjectileDispense.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */