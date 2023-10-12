/*    */ package rip.diavlo.base.modules.movement;
/*    */ 
/*    */ import com.google.common.eventbus.Subscribe;
/*    */ import net.minecraft.client.Minecraft;
/*    */ import net.minecraft.network.Packet;
/*    */ import net.minecraft.network.play.client.C03PacketPlayer;
/*    */ import rip.diavlo.base.api.module.Category;
/*    */ import rip.diavlo.base.api.module.Module;
/*    */ import rip.diavlo.base.api.value.impl.ModeValue;
/*    */ import rip.diavlo.base.events.player.UpdateEvent;
/*    */ 
/*    */ public class Fly
/*    */   extends Module
/*    */ {
/* 15 */   private final Minecraft mc = Minecraft.getMinecraft();
/*    */   
/*    */   private boolean flyEnabled;
/*    */   private int flyTimer;
/* 19 */   public final ModeValue speedValue = new ModeValue("Speed", this, new String[] { "0.5", "1", "1.5", "2", "2.5", "3", "3.5", "4", "4.5", "5", "5.5", "6", "6.5", "7", "7.5", "8", "8.5", "9", "9.5", "10" });
/*    */   
/*    */   public Fly() {
/* 22 */     super("Fly", 33, Category.MOVEMENT);
/* 23 */     getValues().add(this.speedValue);
/*    */   }
/*    */ 
/*    */   
/*    */   public void onEnable() {
/* 28 */     super.onEnable();
/* 29 */     this.flyEnabled = true;
/* 30 */     this.flyTimer = 100;
/* 31 */     this.mc.thePlayer.capabilities.isFlying = true;
/*    */   }
/*    */ 
/*    */   
/*    */   public void onDisable() {
/* 36 */     this.flyEnabled = false;
/* 37 */     this.mc.thePlayer.capabilities.isFlying = false;
/* 38 */     this.mc.thePlayer.capabilities.setFlySpeed(1.0F);
/*    */   }
/*    */   
/*    */   @Subscribe
/*    */   public void onUpdate(UpdateEvent event) {
/* 43 */     if (this.flyEnabled) {
/* 44 */       this.mc.thePlayer.capabilities.isFlying = true;
/* 45 */       this.mc.thePlayer.capabilities.setFlySpeed(Float.parseFloat((String)this.speedValue.get()) / 5.0F);
/*    */       
/* 47 */       if (this.mc.thePlayer.capabilities.isCreativeMode) {
/* 48 */         this.mc.thePlayer.capabilities.allowFlying = true;
/*    */       }
/*    */       
/* 51 */       if (++this.flyTimer > 20) {
/* 52 */         C03PacketPlayer packet = new C03PacketPlayer(true);
/* 53 */         packet.y += 1.0E-5D;
/* 54 */         this.mc.thePlayer.sendQueue.addToSendQueue((Packet)packet);
/* 55 */         this.flyTimer = 0;
/*    */       } 
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public String getSuffix() {
/* 62 */     return ((String)this.speedValue.getValue()).toString();
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\rip\diavlo\base\modules\movement\Fly.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */