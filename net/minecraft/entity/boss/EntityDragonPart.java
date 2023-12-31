/*    */ package net.minecraft.entity.boss;
/*    */ 
/*    */ import net.minecraft.entity.Entity;
/*    */ import net.minecraft.entity.IEntityMultiPart;
/*    */ import net.minecraft.nbt.NBTTagCompound;
/*    */ import net.minecraft.util.DamageSource;
/*    */ 
/*    */ public class EntityDragonPart
/*    */   extends Entity
/*    */ {
/*    */   public final IEntityMultiPart entityDragonObj;
/*    */   public final String partName;
/*    */   
/*    */   public EntityDragonPart(IEntityMultiPart parent, String partName, float base, float sizeHeight) {
/* 15 */     super(parent.getWorld());
/* 16 */     setSize(base, sizeHeight);
/* 17 */     this.entityDragonObj = parent;
/* 18 */     this.partName = partName;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   protected void entityInit() {}
/*    */ 
/*    */ 
/*    */   
/*    */   protected void readEntityFromNBT(NBTTagCompound tagCompund) {}
/*    */ 
/*    */ 
/*    */   
/*    */   protected void writeEntityToNBT(NBTTagCompound tagCompound) {}
/*    */ 
/*    */   
/*    */   public boolean canBeCollidedWith() {
/* 35 */     return true;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean attackEntityFrom(DamageSource source, float amount) {
/* 40 */     return isEntityInvulnerable(source) ? false : this.entityDragonObj.attackEntityFromPart(this, source, amount);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isEntityEqual(Entity entityIn) {
/* 45 */     return (this == entityIn || this.entityDragonObj == entityIn);
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\entity\boss\EntityDragonPart.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */