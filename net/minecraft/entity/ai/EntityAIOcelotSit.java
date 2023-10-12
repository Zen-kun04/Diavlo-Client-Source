/*    */ package net.minecraft.entity.ai;
/*    */ 
/*    */ import net.minecraft.block.Block;
/*    */ import net.minecraft.block.BlockBed;
/*    */ import net.minecraft.block.properties.IProperty;
/*    */ import net.minecraft.block.state.IBlockState;
/*    */ import net.minecraft.entity.EntityCreature;
/*    */ import net.minecraft.entity.passive.EntityOcelot;
/*    */ import net.minecraft.init.Blocks;
/*    */ import net.minecraft.tileentity.TileEntity;
/*    */ import net.minecraft.tileentity.TileEntityChest;
/*    */ import net.minecraft.util.BlockPos;
/*    */ import net.minecraft.world.World;
/*    */ 
/*    */ public class EntityAIOcelotSit extends EntityAIMoveToBlock {
/*    */   private final EntityOcelot ocelot;
/*    */   
/*    */   public EntityAIOcelotSit(EntityOcelot ocelotIn, double p_i45315_2_) {
/* 19 */     super((EntityCreature)ocelotIn, p_i45315_2_, 8);
/* 20 */     this.ocelot = ocelotIn;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean shouldExecute() {
/* 25 */     return (this.ocelot.isTamed() && !this.ocelot.isSitting() && super.shouldExecute());
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean continueExecuting() {
/* 30 */     return super.continueExecuting();
/*    */   }
/*    */ 
/*    */   
/*    */   public void startExecuting() {
/* 35 */     super.startExecuting();
/* 36 */     this.ocelot.getAISit().setSitting(false);
/*    */   }
/*    */ 
/*    */   
/*    */   public void resetTask() {
/* 41 */     super.resetTask();
/* 42 */     this.ocelot.setSitting(false);
/*    */   }
/*    */ 
/*    */   
/*    */   public void updateTask() {
/* 47 */     super.updateTask();
/* 48 */     this.ocelot.getAISit().setSitting(false);
/*    */     
/* 50 */     if (!getIsAboveDestination()) {
/*    */       
/* 52 */       this.ocelot.setSitting(false);
/*    */     }
/* 54 */     else if (!this.ocelot.isSitting()) {
/*    */       
/* 56 */       this.ocelot.setSitting(true);
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   protected boolean shouldMoveTo(World worldIn, BlockPos pos) {
/* 62 */     if (!worldIn.isAirBlock(pos.up()))
/*    */     {
/* 64 */       return false;
/*    */     }
/*    */ 
/*    */     
/* 68 */     IBlockState iblockstate = worldIn.getBlockState(pos);
/* 69 */     Block block = iblockstate.getBlock();
/*    */     
/* 71 */     if (block == Blocks.chest) {
/*    */       
/* 73 */       TileEntity tileentity = worldIn.getTileEntity(pos);
/*    */       
/* 75 */       if (tileentity instanceof TileEntityChest && ((TileEntityChest)tileentity).numPlayersUsing < 1)
/*    */       {
/* 77 */         return true;
/*    */       }
/*    */     }
/*    */     else {
/*    */       
/* 82 */       if (block == Blocks.lit_furnace)
/*    */       {
/* 84 */         return true;
/*    */       }
/*    */       
/* 87 */       if (block == Blocks.bed && iblockstate.getValue((IProperty)BlockBed.PART) != BlockBed.EnumPartType.HEAD)
/*    */       {
/* 89 */         return true;
/*    */       }
/*    */     } 
/*    */     
/* 93 */     return false;
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\entity\ai\EntityAIOcelotSit.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */