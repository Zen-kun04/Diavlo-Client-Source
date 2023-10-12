/*    */ package rip.diavlo.base.modules.movement;
/*    */ 
/*    */ import com.google.common.eventbus.Subscribe;
/*    */ import net.minecraft.util.Timer;
/*    */ import rip.diavlo.base.api.module.Category;
/*    */ import rip.diavlo.base.api.module.Module;
/*    */ import rip.diavlo.base.api.value.impl.ModeValue;
/*    */ import rip.diavlo.base.events.player.UpdateEvent;
/*    */ 
/*    */ 
/*    */ public class TimerHack
/*    */   extends Module
/*    */ {
/* 14 */   public final ModeValue speedValue = new ModeValue("Time", this, new String[] { "0.1", "0.2", "0.3", "0.4", "0.5", "0.6", "0.7", "0.8", "0.9", "1.0", "1.1", "1.2", "1.3", "1.4", "1.5", "1.6", "1.7", "1.8", "1.9", "2.0", "2.1", "2.2", "2.3", "2.4", "2.5", "2.6", "2.7", "2.8", "2.9", "3.0", "3.1", "3.2", "3.3", "3.4", "3.5", "3.6", "3.7", "3.8", "3.9", "4.0", "4.1", "4.2", "4.3", "4.4", "4.5", "4.6", "4.7", "4.8", "4.9", "5.0", "5.1", "5.2", "5.3", "5.4", "5.5", "5.6", "5.7", "5.8", "5.9", "6.0", "6.1", "6.2", "6.3", "6.4", "6.5", "6.6", "6.7", "6.8", "6.9", "7.0", "7.1", "7.2", "7.3", "7.4", "7.5", "7.6", "7.7", "7.8", "7.9", "8.0", "8.1", "8.2", "8.3", "8.4", "8.5", "8.6", "8.7", "8.8", "8.9", "9.0", "9.1", "9.2", "9.3", "9.4", "9.5", "9.6", "9.7", "9.8", "9.9", "10.0" });
/*    */   
/*    */   public TimerHack() {
/* 17 */     super("Timer", 0, Category.MOVEMENT);
/* 18 */     getValues().add(this.speedValue);
/*    */   }
/*    */ 
/*    */   
/*    */   public void onEnable() {
/* 23 */     super.onEnable();
/*    */   }
/*    */   
/*    */   @Subscribe
/*    */   public void onUpdate(UpdateEvent event) {
/* 28 */     Timer.setTimerSpeed(Float.parseFloat((String)this.speedValue.getValue()));
/*    */   }
/*    */ 
/*    */   
/*    */   public void onDisable() {
/* 33 */     super.onDisable();
/* 34 */     Timer.setTimerSpeed(1.0F);
/*    */   }
/*    */ 
/*    */   
/*    */   public String getSuffix() {
/* 39 */     return this.speedValue.getValueAsString();
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\rip\diavlo\base\modules\movement\TimerHack.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */