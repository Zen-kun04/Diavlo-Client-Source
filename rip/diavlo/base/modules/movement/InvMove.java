/*    */ package rip.diavlo.base.modules.movement;
/*    */ 
/*    */ import com.google.common.eventbus.Subscribe;
/*    */ import net.minecraft.client.gui.GuiChat;
/*    */ import net.minecraft.client.settings.GameSettings;
/*    */ import rip.diavlo.base.api.module.Category;
/*    */ import rip.diavlo.base.api.module.Module;
/*    */ import rip.diavlo.base.events.player.UpdateEvent;
/*    */ 
/*    */ 
/*    */ public class InvMove
/*    */   extends Module
/*    */ {
/*    */   public InvMove() {
/* 15 */     super("InvMove", 0, Category.MOVEMENT);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   @Subscribe
/*    */   public void onUpdate(UpdateEvent event) {
/* 22 */     if (!GuiChat.chatEnabled && !(mc.currentScreen instanceof net.minecraft.client.gui.GuiIngameMenu)) {
/*    */ 
/*    */       
/* 25 */       mc.gameSettings.keyBindForward.pressed = GameSettings.isKeyDown(mc.gameSettings.keyBindForward);
/* 26 */       mc.gameSettings.keyBindBack.pressed = GameSettings.isKeyDown(mc.gameSettings.keyBindBack);
/* 27 */       mc.gameSettings.keyBindRight.pressed = GameSettings.isKeyDown(mc.gameSettings.keyBindRight);
/* 28 */       mc.gameSettings.keyBindLeft.pressed = GameSettings.isKeyDown(mc.gameSettings.keyBindLeft);
/* 29 */       mc.gameSettings.keyBindJump.pressed = GameSettings.isKeyDown(mc.gameSettings.keyBindJump);
/* 30 */       mc.gameSettings.keyBindSprint.pressed = GameSettings.isKeyDown(mc.gameSettings.keyBindSprint);
/*    */     } 
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void onDisable() {
/* 37 */     super.onDisable();
/* 38 */     if (!GameSettings.isKeyDown(mc.gameSettings.keyBindForward) || mc.currentScreen != null)
/* 39 */       mc.gameSettings.keyBindForward.pressed = false; 
/* 40 */     if (!GameSettings.isKeyDown(mc.gameSettings.keyBindBack) || mc.currentScreen != null)
/* 41 */       mc.gameSettings.keyBindBack.pressed = false; 
/* 42 */     if (!GameSettings.isKeyDown(mc.gameSettings.keyBindRight) || mc.currentScreen != null)
/* 43 */       mc.gameSettings.keyBindRight.pressed = false; 
/* 44 */     if (!GameSettings.isKeyDown(mc.gameSettings.keyBindLeft) || mc.currentScreen != null)
/* 45 */       mc.gameSettings.keyBindLeft.pressed = false; 
/* 46 */     if (!GameSettings.isKeyDown(mc.gameSettings.keyBindJump) || mc.currentScreen != null)
/* 47 */       mc.gameSettings.keyBindJump.pressed = false; 
/* 48 */     if (!GameSettings.isKeyDown(mc.gameSettings.keyBindSprint) || mc.currentScreen != null)
/* 49 */       mc.gameSettings.keyBindSprint.pressed = false; 
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\rip\diavlo\base\modules\movement\InvMove.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */