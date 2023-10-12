/*    */ package net.minecraft.tileentity;
/*    */ 
/*    */ import net.minecraft.init.Blocks;
/*    */ import net.minecraft.nbt.NBTTagCompound;
/*    */ import net.minecraft.network.Packet;
/*    */ import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
/*    */ import net.minecraft.util.BlockPos;
/*    */ import net.minecraft.util.ITickable;
/*    */ import net.minecraft.world.World;
/*    */ 
/*    */ public class TileEntityMobSpawner
/*    */   extends TileEntity implements ITickable {
/* 13 */   private final MobSpawnerBaseLogic spawnerLogic = new MobSpawnerBaseLogic()
/*    */     {
/*    */       public void func_98267_a(int id)
/*    */       {
/* 17 */         TileEntityMobSpawner.this.worldObj.addBlockEvent(TileEntityMobSpawner.this.pos, Blocks.mob_spawner, id, 0);
/*    */       }
/*    */       
/*    */       public World getSpawnerWorld() {
/* 21 */         return TileEntityMobSpawner.this.worldObj;
/*    */       }
/*    */       
/*    */       public BlockPos getSpawnerPosition() {
/* 25 */         return TileEntityMobSpawner.this.pos;
/*    */       }
/*    */       
/*    */       public void setRandomEntity(MobSpawnerBaseLogic.WeightedRandomMinecart p_98277_1_) {
/* 29 */         super.setRandomEntity(p_98277_1_);
/*    */         
/* 31 */         if (getSpawnerWorld() != null)
/*    */         {
/* 33 */           getSpawnerWorld().markBlockForUpdate(TileEntityMobSpawner.this.pos);
/*    */         }
/*    */       }
/*    */     };
/*    */ 
/*    */   
/*    */   public void readFromNBT(NBTTagCompound compound) {
/* 40 */     super.readFromNBT(compound);
/* 41 */     this.spawnerLogic.readFromNBT(compound);
/*    */   }
/*    */ 
/*    */   
/*    */   public void writeToNBT(NBTTagCompound compound) {
/* 46 */     super.writeToNBT(compound);
/* 47 */     this.spawnerLogic.writeToNBT(compound);
/*    */   }
/*    */ 
/*    */   
/*    */   public void update() {
/* 52 */     this.spawnerLogic.updateSpawner();
/*    */   }
/*    */ 
/*    */   
/*    */   public Packet getDescriptionPacket() {
/* 57 */     NBTTagCompound nbttagcompound = new NBTTagCompound();
/* 58 */     writeToNBT(nbttagcompound);
/* 59 */     nbttagcompound.removeTag("SpawnPotentials");
/* 60 */     return (Packet)new S35PacketUpdateTileEntity(this.pos, 1, nbttagcompound);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean receiveClientEvent(int id, int type) {
/* 65 */     return this.spawnerLogic.setDelayToMin(id) ? true : super.receiveClientEvent(id, type);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean func_183000_F() {
/* 70 */     return true;
/*    */   }
/*    */ 
/*    */   
/*    */   public MobSpawnerBaseLogic getSpawnerBaseLogic() {
/* 75 */     return this.spawnerLogic;
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\tileentity\TileEntityMobSpawner.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */