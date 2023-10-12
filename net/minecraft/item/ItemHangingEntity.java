/*    */ package net.minecraft.item;
/*    */ 
/*    */ import net.minecraft.creativetab.CreativeTabs;
/*    */ import net.minecraft.entity.Entity;
/*    */ import net.minecraft.entity.EntityHanging;
/*    */ import net.minecraft.entity.item.EntityItemFrame;
/*    */ import net.minecraft.entity.item.EntityPainting;
/*    */ import net.minecraft.entity.player.EntityPlayer;
/*    */ import net.minecraft.util.BlockPos;
/*    */ import net.minecraft.util.EnumFacing;
/*    */ import net.minecraft.world.World;
/*    */ 
/*    */ public class ItemHangingEntity
/*    */   extends Item {
/*    */   private final Class<? extends EntityHanging> hangingEntityClass;
/*    */   
/*    */   public ItemHangingEntity(Class<? extends EntityHanging> entityClass) {
/* 18 */     this.hangingEntityClass = entityClass;
/* 19 */     setCreativeTab(CreativeTabs.tabDecorations);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean onItemUse(ItemStack stack, EntityPlayer playerIn, World worldIn, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ) {
/* 24 */     if (side == EnumFacing.DOWN)
/*    */     {
/* 26 */       return false;
/*    */     }
/* 28 */     if (side == EnumFacing.UP)
/*    */     {
/* 30 */       return false;
/*    */     }
/*    */ 
/*    */     
/* 34 */     BlockPos blockpos = pos.offset(side);
/*    */     
/* 36 */     if (!playerIn.canPlayerEdit(blockpos, side, stack))
/*    */     {
/* 38 */       return false;
/*    */     }
/*    */ 
/*    */     
/* 42 */     EntityHanging entityhanging = createEntity(worldIn, blockpos, side);
/*    */     
/* 44 */     if (entityhanging != null && entityhanging.onValidSurface()) {
/*    */       
/* 46 */       if (!worldIn.isRemote)
/*    */       {
/* 48 */         worldIn.spawnEntityInWorld((Entity)entityhanging);
/*    */       }
/*    */       
/* 51 */       stack.stackSize--;
/*    */     } 
/*    */     
/* 54 */     return true;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private EntityHanging createEntity(World worldIn, BlockPos pos, EnumFacing clickedSide) {
/* 61 */     return (this.hangingEntityClass == EntityPainting.class) ? (EntityHanging)new EntityPainting(worldIn, pos, clickedSide) : ((this.hangingEntityClass == EntityItemFrame.class) ? (EntityHanging)new EntityItemFrame(worldIn, pos, clickedSide) : null);
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\item\ItemHangingEntity.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */