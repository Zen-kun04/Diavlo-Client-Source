/*    */ package rip.diavlo.base.modules.combat;
/*    */ 
/*    */ import com.google.common.eventbus.Subscribe;
/*    */ import net.minecraft.client.entity.EntityPlayerSP;
/*    */ import net.minecraft.entity.Entity;
/*    */ import net.minecraft.entity.player.EntityPlayer;
/*    */ import net.minecraft.network.Packet;
/*    */ import net.minecraft.network.play.client.C02PacketUseEntity;
/*    */ import net.minecraft.util.Vec3;
/*    */ import rip.diavlo.base.api.module.Category;
/*    */ import rip.diavlo.base.api.module.Module;
/*    */ import rip.diavlo.base.api.value.impl.ModeValue;
/*    */ import rip.diavlo.base.events.player.UpdateEvent;
/*    */ import rip.diavlo.base.utils.MSTimer;
/*    */ import rip.diavlo.base.utils.McUtils;
/*    */ 
/*    */ public class Killaura extends Module {
/* 18 */   public final ModeValue aps = new ModeValue("Cps", this, new String[] { "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20" });
/* 19 */   public final ModeValue mode = new ModeValue("Mode", this, new String[] { "Single", "Multi" });
/* 20 */   public final ModeValue range = new ModeValue("Range", this, new String[] { "0.9", "1.1", "1.3", "1.5", "1.7", "1.9", "2.1", "2.3", "2.5", "2.7", "2.9", "3.1", "3.3", "3.5", "3.7", "3.9", "4.1", "4.3", "4.5", "4.7", "4.9", "5.1", "5.3", "5.5", "5.7", "5.9", "6.1", "6.3", "6.5", "6.7", "6.9", "7.1", "7.3", "7.5", "7.7", "8.0" });
/* 21 */   public final ModeValue swing = new ModeValue("Swing", this, new String[] { "Si", "No" });
/*    */   
/* 23 */   public MSTimer timer = new MSTimer();
/*    */   
/*    */   public Killaura() {
/* 26 */     super("Killaura", 19, Category.COMBAT);
/* 27 */     getValues().add(this.aps);
/* 28 */     this.aps.setValue("15");
/* 29 */     getValues().add(this.mode);
/* 30 */     getValues().add(this.range);
/* 31 */     this.range.setValue("3.9");
/* 32 */     getValues().add(this.swing);
/*    */   }
/*    */ 
/*    */   
/*    */   @Subscribe
/*    */   public void onUpdate(UpdateEvent event) {
/* 38 */     if (((String)this.mode.get()).equalsIgnoreCase("single")) {
/* 39 */       EntityPlayer p = McUtils.getClosestPlayerToEntity((Entity)mc.thePlayer, Double.parseDouble((String)this.range.get()));
/* 40 */       if (p != null && !p.isDead && p.getHealth() >= 0.0F && canEntityBeSeen((Entity)mc.thePlayer) && 
/* 41 */         this.timer.hasTimePassed(1000L / Long.valueOf(((String)this.aps.get()).toString()).longValue())) {
/* 42 */         mc.thePlayer.sendQueue.addToSendQueue((Packet)new C02PacketUseEntity((Entity)p, C02PacketUseEntity.Action.ATTACK));
/* 43 */         mc.thePlayer.swingItem();
/*    */         
/* 45 */         mc.thePlayer.setRotationYawHead(p.getRotationYawHead());
/* 46 */         this.timer.reset();
/*    */       }
/*    */     
/*    */     }
/*    */     else {
/*    */       
/* 52 */       for (EntityPlayer p : mc.theWorld.playerEntities) {
/* 53 */         if (p.getDistance(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ) <= Double.parseDouble((String)this.range.get()) && 
/* 54 */           !p.getName().equalsIgnoreCase(mc.thePlayer.getName()) && !p.isDead && p
/* 55 */           .getHealth() >= 0.0F && canEntityBeSeen((Entity)mc.thePlayer) && 
/* 56 */           this.timer.hasTimePassed(1000L / Long.valueOf(((String)this.aps.get()).toString()).longValue())) {
/* 57 */           mc.thePlayer.sendQueue.addToSendQueue((Packet)new C02PacketUseEntity((Entity)p, C02PacketUseEntity.Action.ATTACK));
/* 58 */           mc.thePlayer.swingItem();
/*    */           
/* 60 */           mc.thePlayer.setRotationYawHead(p.getRotationYawHead());
/* 61 */           this.timer.reset();
/*    */         } 
/*    */       } 
/*    */     } 
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean canEntityBeSeen(Entity entityIn) {
/* 72 */     EntityPlayerSP entityPlayerSP = mc.thePlayer;
/* 73 */     return (mc.theWorld.rayTraceBlocks(new Vec3(((EntityPlayer)entityPlayerSP).posX, ((EntityPlayer)entityPlayerSP).posY + entityPlayerSP.getEyeHeight(), ((EntityPlayer)entityPlayerSP).posZ), new Vec3(entityIn.posX, entityIn.posY + entityIn
/* 74 */           .getEyeHeight(), entityIn.posZ), false) == null);
/*    */   }
/*    */ 
/*    */   
/*    */   public void onDisable() {
/* 79 */     super.onDisable();
/*    */   }
/*    */ 
/*    */   
/*    */   public void onEnable() {
/* 84 */     super.onEnable();
/*    */   }
/*    */ 
/*    */   
/*    */   public String getSuffix() {
/* 89 */     return (String)this.mode.get();
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\rip\diavlo\base\modules\combat\Killaura.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */