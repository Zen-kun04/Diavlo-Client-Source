/*    */ package rip.diavlo.base.ui;
/*    */ 
/*    */ import java.awt.Color;
/*    */ import net.minecraft.client.Minecraft;
/*    */ import net.minecraft.client.gui.ScaledResolution;
/*    */ import rip.diavlo.base.Client;
/*    */ import rip.diavlo.base.api.font.CustomFontRenderer;
/*    */ import rip.diavlo.base.utils.MSTimer;
/*    */ import rip.diavlo.base.utils.render.RenderUtil;
/*    */ 
/*    */ 
/*    */ public class Notification
/*    */ {
/* 14 */   private Minecraft mc = Minecraft.getMinecraft();
/*    */   
/* 16 */   private ScaledResolution sr = new ScaledResolution(this.mc);
/*    */   
/* 18 */   private int currentX = this.sr.getScaledWidth();
/* 19 */   private int finishX = this.sr.getScaledWidth() - 100;
/*    */   
/*    */   private boolean isReturning = false;
/*    */   public boolean hasFinished = false;
/*    */   private MSTimer timer;
/*    */   private String s;
/*    */   
/*    */   public Notification(String s) {
/* 27 */     this.hasFinished = false;
/* 28 */     this.s = s;
/* 29 */     this.timer = new MSTimer();
/* 30 */     this.timer.reset();
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void drawNotification() {
/* 36 */     if (!this.hasFinished) {
/* 37 */       if (this.mc.ingameGUI != null) {
/*    */ 
/*    */         
/* 40 */         CustomFontRenderer font = Client.getInstance().getFontManager().getDefaultFont().size(22);
/* 41 */         CustomFontRenderer font2 = Client.getInstance().getFontManager().getDefaultFont().size(14);
/* 42 */         RenderUtil.roundedRectangle(this.currentX, (this.sr.getScaledHeight() - 60), 100.0D, 40.0D, 2.0D, new Color(60, 63, 65, 170));
/* 43 */         font.drawStringWithShadow("Diavlo v1.0", (this.currentX + 5), (this.sr.getScaledHeight() - 55), (new Color(253, 84, 85)).getRGB());
/* 44 */         font2.drawStringWithShadow(this.s, (this.currentX + 5), (this.sr.getScaledHeight() - 40), (new Color(255, 255, 255)).getRGB());
/*    */       } 
/*    */ 
/*    */       
/* 48 */       if (this.currentX > this.finishX && 
/* 49 */         !this.isReturning) {
/* 50 */         this.currentX -= 3;
/*    */       }
/*    */ 
/*    */       
/* 54 */       if (this.timer.hasTimePassed(2000L)) {
/* 55 */         this.isReturning = true;
/* 56 */         this.currentX += 3;
/*    */       } 
/*    */       
/* 59 */       if (this.timer.hasTimePassed(6000L)) {
/*    */         
/* 61 */         this.hasFinished = true;
/* 62 */         this.timer.reset();
/*    */       } 
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\rip\diavlo\bas\\ui\Notification.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */