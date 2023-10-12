/*    */ package net.minecraft.entity.ai;
/*    */ 
/*    */ import net.minecraft.block.state.IBlockState;
/*    */ import net.minecraft.entity.Entity;
/*    */ import net.minecraft.entity.item.EntityMinecart;
/*    */ import net.minecraft.init.Blocks;
/*    */ import net.minecraft.nbt.NBTTagCompound;
/*    */ import net.minecraft.tileentity.MobSpawnerBaseLogic;
/*    */ import net.minecraft.util.BlockPos;
/*    */ import net.minecraft.world.World;
/*    */ 
/*    */ public class EntityMinecartMobSpawner extends EntityMinecart {
/* 13 */   private final MobSpawnerBaseLogic mobSpawnerLogic = new MobSpawnerBaseLogic()
/*    */     {
/*    */       public void func_98267_a(int id)
/*    */       {
/* 17 */         EntityMinecartMobSpawner.this.worldObj.setEntityState((Entity)EntityMinecartMobSpawner.this, (byte)id);
/*    */       }
/*    */       
/*    */       public World getSpawnerWorld() {
/* 21 */         return EntityMinecartMobSpawner.this.worldObj;
/*    */       }
/*    */       
/*    */       public BlockPos getSpawnerPosition() {
/* 25 */         return new BlockPos((Entity)EntityMinecartMobSpawner.this);
/*    */       }
/*    */     };
/*    */ 
/*    */   
/*    */   public EntityMinecartMobSpawner(World worldIn) {
/* 31 */     super(worldIn);
/*    */   }
/*    */ 
/*    */   
/*    */   public EntityMinecartMobSpawner(World worldIn, double p_i1726_2_, double p_i1726_4_, double p_i1726_6_) {
/* 36 */     super(worldIn, p_i1726_2_, p_i1726_4_, p_i1726_6_);
/*    */   }
/*    */ 
/*    */   
/*    */   public EntityMinecart.EnumMinecartType getMinecartType() {
/* 41 */     return EntityMinecart.EnumMinecartType.SPAWNER;
/*    */   }
/*    */ 
/*    */   
/*    */   public IBlockState getDefaultDisplayTile() {
/* 46 */     return Blocks.mob_spawner.getDefaultState();
/*    */   }
/*    */ 
/*    */   
/*    */   protected void readEntityFromNBT(NBTTagCompound tagCompund) {
/* 51 */     super.readEntityFromNBT(tagCompund);
/* 52 */     this.mobSpawnerLogic.readFromNBT(tagCompund);
/*    */   }
/*    */ 
/*    */   
/*    */   protected void writeEntityToNBT(NBTTagCompound tagCompound) {
/* 57 */     super.writeEntityToNBT(tagCompound);
/* 58 */     this.mobSpawnerLogic.writeToNBT(tagCompound);
/*    */   }
/*    */ 
/*    */   
/*    */   public void handleStatusUpdate(byte id) {
/* 63 */     this.mobSpawnerLogic.setDelayToMin(id);
/*    */   }
/*    */ 
/*    */   
/*    */   public void onUpdate() {
/* 68 */     super.onUpdate();
/* 69 */     this.mobSpawnerLogic.updateSpawner();
/*    */   }
/*    */ 
/*    */   
/*    */   public MobSpawnerBaseLogic func_98039_d() {
/* 74 */     return this.mobSpawnerLogic;
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\entity\ai\EntityMinecartMobSpawner.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */