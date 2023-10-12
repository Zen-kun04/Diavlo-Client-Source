/*    */ package net.minecraft.client.particle;
/*    */ 
/*    */ import net.minecraft.client.renderer.WorldRenderer;
/*    */ import net.minecraft.entity.Entity;
/*    */ import net.minecraft.util.EnumParticleTypes;
/*    */ import net.minecraft.world.World;
/*    */ 
/*    */ public class EntityParticleEmitter
/*    */   extends EntityFX
/*    */ {
/*    */   private Entity attachedEntity;
/*    */   private int age;
/*    */   private int lifetime;
/*    */   private EnumParticleTypes particleTypes;
/*    */   
/*    */   public EntityParticleEmitter(World worldIn, Entity p_i46279_2_, EnumParticleTypes particleTypesIn) {
/* 17 */     super(worldIn, p_i46279_2_.posX, (p_i46279_2_.getEntityBoundingBox()).minY + (p_i46279_2_.height / 2.0F), p_i46279_2_.posZ, p_i46279_2_.motionX, p_i46279_2_.motionY, p_i46279_2_.motionZ);
/* 18 */     this.attachedEntity = p_i46279_2_;
/* 19 */     this.lifetime = 3;
/* 20 */     this.particleTypes = particleTypesIn;
/* 21 */     onUpdate();
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void renderParticle(WorldRenderer worldRendererIn, Entity entityIn, float partialTicks, float rotationX, float rotationZ, float rotationYZ, float rotationXY, float rotationXZ) {}
/*    */ 
/*    */   
/*    */   public void onUpdate() {
/* 30 */     for (int i = 0; i < 16; i++) {
/*    */       
/* 32 */       double d0 = (this.rand.nextFloat() * 2.0F - 1.0F);
/* 33 */       double d1 = (this.rand.nextFloat() * 2.0F - 1.0F);
/* 34 */       double d2 = (this.rand.nextFloat() * 2.0F - 1.0F);
/*    */       
/* 36 */       if (d0 * d0 + d1 * d1 + d2 * d2 <= 1.0D) {
/*    */         
/* 38 */         double d3 = this.attachedEntity.posX + d0 * this.attachedEntity.width / 4.0D;
/* 39 */         double d4 = (this.attachedEntity.getEntityBoundingBox()).minY + (this.attachedEntity.height / 2.0F) + d1 * this.attachedEntity.height / 4.0D;
/* 40 */         double d5 = this.attachedEntity.posZ + d2 * this.attachedEntity.width / 4.0D;
/* 41 */         this.worldObj.spawnParticle(this.particleTypes, false, d3, d4, d5, d0, d1 + 0.2D, d2, new int[0]);
/*    */       } 
/*    */     } 
/*    */     
/* 45 */     this.age++;
/*    */     
/* 47 */     if (this.age >= this.lifetime)
/*    */     {
/* 49 */       setDead();
/*    */     }
/*    */   }
/*    */ 
/*    */   
/*    */   public int getFXLayer() {
/* 55 */     return 3;
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\client\particle\EntityParticleEmitter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */