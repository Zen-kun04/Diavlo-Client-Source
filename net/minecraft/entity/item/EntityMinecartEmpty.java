/*    */ package net.minecraft.entity.item;
/*    */ 
/*    */ import net.minecraft.entity.Entity;
/*    */ import net.minecraft.entity.player.EntityPlayer;
/*    */ import net.minecraft.world.World;
/*    */ 
/*    */ public class EntityMinecartEmpty
/*    */   extends EntityMinecart
/*    */ {
/*    */   public EntityMinecartEmpty(World worldIn) {
/* 11 */     super(worldIn);
/*    */   }
/*    */ 
/*    */   
/*    */   public EntityMinecartEmpty(World worldIn, double x, double y, double z) {
/* 16 */     super(worldIn, x, y, z);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean interactFirst(EntityPlayer playerIn) {
/* 21 */     if (this.riddenByEntity != null && this.riddenByEntity instanceof EntityPlayer && this.riddenByEntity != playerIn)
/*    */     {
/* 23 */       return true;
/*    */     }
/* 25 */     if (this.riddenByEntity != null && this.riddenByEntity != playerIn)
/*    */     {
/* 27 */       return false;
/*    */     }
/*    */ 
/*    */     
/* 31 */     if (!this.worldObj.isRemote)
/*    */     {
/* 33 */       playerIn.mountEntity(this);
/*    */     }
/*    */     
/* 36 */     return true;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void onActivatorRailPass(int x, int y, int z, boolean receivingPower) {
/* 42 */     if (receivingPower) {
/*    */       
/* 44 */       if (this.riddenByEntity != null)
/*    */       {
/* 46 */         this.riddenByEntity.mountEntity((Entity)null);
/*    */       }
/*    */       
/* 49 */       if (getRollingAmplitude() == 0) {
/*    */         
/* 51 */         setRollingDirection(-getRollingDirection());
/* 52 */         setRollingAmplitude(10);
/* 53 */         setDamage(50.0F);
/* 54 */         setBeenAttacked();
/*    */       } 
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public EntityMinecart.EnumMinecartType getMinecartType() {
/* 61 */     return EntityMinecart.EnumMinecartType.RIDEABLE;
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\entity\item\EntityMinecartEmpty.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */