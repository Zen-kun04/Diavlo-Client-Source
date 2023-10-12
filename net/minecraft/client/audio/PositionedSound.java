/*    */ package net.minecraft.client.audio;
/*    */ 
/*    */ import net.minecraft.util.ResourceLocation;
/*    */ 
/*    */ public abstract class PositionedSound
/*    */   implements ISound {
/*    */   protected final ResourceLocation positionedSoundLocation;
/*  8 */   protected float volume = 1.0F;
/*  9 */   protected float pitch = 1.0F;
/*    */   protected float xPosF;
/*    */   protected float yPosF;
/*    */   protected float zPosF;
/*    */   protected boolean repeat = false;
/* 14 */   protected int repeatDelay = 0;
/* 15 */   protected ISound.AttenuationType attenuationType = ISound.AttenuationType.LINEAR;
/*    */ 
/*    */   
/*    */   protected PositionedSound(ResourceLocation soundResource) {
/* 19 */     this.positionedSoundLocation = soundResource;
/*    */   }
/*    */ 
/*    */   
/*    */   public ResourceLocation getSoundLocation() {
/* 24 */     return this.positionedSoundLocation;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean canRepeat() {
/* 29 */     return this.repeat;
/*    */   }
/*    */ 
/*    */   
/*    */   public int getRepeatDelay() {
/* 34 */     return this.repeatDelay;
/*    */   }
/*    */ 
/*    */   
/*    */   public float getVolume() {
/* 39 */     return this.volume;
/*    */   }
/*    */ 
/*    */   
/*    */   public float getPitch() {
/* 44 */     return this.pitch;
/*    */   }
/*    */ 
/*    */   
/*    */   public float getXPosF() {
/* 49 */     return this.xPosF;
/*    */   }
/*    */ 
/*    */   
/*    */   public float getYPosF() {
/* 54 */     return this.yPosF;
/*    */   }
/*    */ 
/*    */   
/*    */   public float getZPosF() {
/* 59 */     return this.zPosF;
/*    */   }
/*    */ 
/*    */   
/*    */   public ISound.AttenuationType getAttenuationType() {
/* 64 */     return this.attenuationType;
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\client\audio\PositionedSound.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */