/*     */ package rip.diavlo.base.modules.movement;
/*     */ 
/*     */ import com.google.common.eventbus.Subscribe;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.util.Timer;
/*     */ import rip.diavlo.base.api.module.Category;
/*     */ import rip.diavlo.base.api.module.Module;
/*     */ import rip.diavlo.base.api.value.impl.ModeValue;
/*     */ import rip.diavlo.base.events.player.UpdateEvent;
/*     */ import rip.diavlo.base.utils.MSTimer;
/*     */ import rip.diavlo.base.utils.McUtils;
/*     */ 
/*     */ public class Speed
/*     */   extends Module {
/*  15 */   public final ModeValue forward = new ModeValue("Vanilla Speed", this, new String[] { "0.5", "1", "1.5", "2", "2.5", "3", "3.5", "4", "4.5", "5", "5.5", "6", "6.5", "7", "7.5", "8", "8.5", "9", "9.5", "10" });
/*  16 */   public ModeValue mode = new ModeValue("Mode", this, new String[] { "Legit", "Yport", "Bhop", "Slowhop", "Vanilla" });
/*     */   
/*     */   public Speed() {
/*  19 */     super("Speed", 45, Category.MOVEMENT);
/*  20 */     getValues().add(this.mode);
/*  21 */     getValues().add(this.forward);
/*  22 */     this.mode.setValue("Vanilla");
/*  23 */     this.forward.setValue("2");
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @Subscribe
/*     */   public void onUpdate(UpdateEvent event) {
/*  30 */     switch (((String)this.mode.get()).toLowerCase()) {
/*     */       
/*     */       case "legit":
/*  33 */         if (mc.thePlayer.onGround) {
/*  34 */           mc.thePlayer.jump();
/*     */         }
/*     */         break;
/*     */       
/*     */       case "yport":
/*  39 */         if (mc.thePlayer.onGround && mc.thePlayer.moveForward > 0.0F) {
/*  40 */           mc.thePlayer.moveStrafing = (float)(mc.thePlayer.moveStrafing + 0.4D);
/*  41 */           mc.thePlayer.posY += 0.41999998688697815D;
/*  42 */           MSTimer t = new MSTimer(); do {
/*     */           
/*  44 */           } while (!t.hasTimePassed(1000L));
/*     */ 
/*     */ 
/*     */           
/*  48 */           mc.thePlayer.jump();
/*  49 */           mc.thePlayer.posY--;
/*  50 */           mc.thePlayer.moveStrafing = (float)(mc.thePlayer.moveStrafing - 0.4D);
/*     */         } 
/*     */         break;
/*     */       
/*     */       case "bhop":
/*  55 */         if (mc.thePlayer.onGround) Timer.timerSpeed = 1.0F; 
/*  56 */         if (mc.thePlayer.onGround && mc.thePlayer.moveForward > 0.0F) {
/*  57 */           mc.thePlayer.jump();
/*  58 */           Timer.timerSpeed = 1.0F;
/*  59 */           mc.thePlayer.motionY = 0.419973D;
/*     */         } 
/*  61 */         if (mc.thePlayer.motionY > 0.0D && !mc.thePlayer.onGround) {
/*  62 */           mc.thePlayer.motionY -= 7.991E-4D;
/*  63 */           mc.thePlayer.jumpMovementFactor = 0.0201465F;
/*  64 */           Timer.timerSpeed = 1.15F;
/*     */         } else {
/*  66 */           mc.thePlayer.motionY -= 7.4775E-4D;
/*  67 */           mc.thePlayer.jumpMovementFactor = 0.0201519F;
/*  68 */           Timer.timerSpeed = 0.8F;
/*     */         } 
/*  70 */         if (mc.thePlayer.fallDistance > 2.0F) Timer.timerSpeed = 1.0F; 
/*  71 */         if (!mc.theWorld.getCollidingBoundingBoxes((Entity)mc.thePlayer, mc.thePlayer.getEntityBoundingBox().offset(0.0D, 0.201D, 0.0D).expand(0.0D, 0.0D, 0.0D)).isEmpty() && 
/*  72 */           mc.theWorld.getCollidingBoundingBoxes((Entity)mc.thePlayer, mc.thePlayer.getEntityBoundingBox().offset(0.0D, 0.199D, 0.0D).expand(0.0D, 0.0D, 0.0D)).isEmpty() && 
/*  73 */           mc.thePlayer.onGround) Timer.timerSpeed = 2.0F;
/*     */         
/*     */         break;
/*     */ 
/*     */       
/*     */       case "slowhop":
/*  79 */         if (!mc.thePlayer.isInWater()) {
/*  80 */           if (mc.thePlayer.moveForward > 0.0F) {
/*  81 */             if (mc.thePlayer.onGround) {
/*  82 */               mc.thePlayer.jump(); break;
/*     */             } 
/*  84 */             float speed = (float)Math.sqrt(mc.thePlayer.motionX * mc.thePlayer.motionX + mc.thePlayer.motionZ * mc.thePlayer.motionZ);
/*  85 */             McUtils.strafe(speed * 1.011F);
/*     */             break;
/*     */           } 
/*  88 */           mc.thePlayer.motionX = 0.0D;
/*  89 */           mc.thePlayer.motionZ = 0.0D;
/*     */         } 
/*     */         break;
/*     */ 
/*     */       
/*     */       case "vanilla":
/*  95 */         if (mc.thePlayer.moveForward > 0.0F) {
/*  96 */           McUtils.strafe(Integer.parseInt((String)this.forward.get()));
/*     */         }
/*     */         break;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void onDisable() {
/* 105 */     super.onDisable();
/* 106 */     mc.thePlayer.moveForward = 0.0F;
/* 107 */     mc.thePlayer.moveStrafing = 0.0F;
/* 108 */     Timer.timerSpeed = 1.0F;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getSuffix() {
/* 113 */     return (String)this.mode.get();
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\rip\diavlo\base\modules\movement\Speed.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */