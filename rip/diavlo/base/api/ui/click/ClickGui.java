/*     */ package rip.diavlo.base.api.ui.click;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.util.ArrayList;
/*     */ import net.minecraft.client.gui.GuiScreen;
/*     */ import org.lwjgl.input.Mouse;
/*     */ import rip.diavlo.base.api.module.Category;
/*     */ import rip.diavlo.base.api.ui.click.component.Component;
/*     */ import rip.diavlo.base.api.ui.click.component.Frame;
/*     */ import rip.diavlo.base.utils.ColorUtil;
/*     */ 
/*     */ 
/*     */ public class ClickGui
/*     */   extends GuiScreen
/*     */ {
/*  16 */   private static ArrayList<Frame> frames = new ArrayList<>();
/*  17 */   public static int color = ColorUtil.redRainbow(50);
/*     */   
/*     */   public static int offset;
/*     */   
/*     */   public ClickGui() {
/*  22 */     frames = new ArrayList<>();
/*     */     
/*  24 */     int frameX = 5;
/*  25 */     for (Category category : Category.values()) {
/*  26 */       Frame frame = new Frame(category);
/*  27 */       frame.setX(frameX);
/*  28 */       frames.add(frame);
/*  29 */       frameX += frame.getWidth() + 1;
/*     */     } 
/*     */   }
/*     */   public static int var;
/*     */   public static boolean disableScroll;
/*     */   
/*     */   public void initGui() {}
/*     */   
/*     */   public void drawScreen(int mouseX, int mouseY, float partialTicks) {
/*  38 */     drawDefaultBackground();
/*     */     
/*  40 */     for (Frame frame : frames) {
/*  41 */       frame.renderFrame(this.fontRendererObj);
/*  42 */       frame.updatePosition(mouseX, mouseY);
/*  43 */       frame.getComponents().forEach(comp -> comp.updateComponent(mouseX, mouseY));
/*     */     } 
/*     */     
/*  46 */     if (Mouse.hasWheel() && !disableScroll) {
/*  47 */       int wheel = Mouse.getDWheel();
/*  48 */       if (wheel < 0) {
/*  49 */         offset += 12;
/*  50 */         var++;
/*  51 */         if (offset < 0) {
/*  52 */           offset = 0;
/*     */         }
/*  54 */       } else if (wheel > 0) {
/*  55 */         offset -= 12;
/*  56 */         var--;
/*  57 */         if (offset < 0) {
/*  58 */           offset = 0;
/*     */         }
/*     */       } 
/*  61 */       if (var < 0) {
/*  62 */         var = 0;
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
/*  69 */     for (Frame frame : frames) {
/*  70 */       if (frame.isWithinHeader(mouseX, mouseY) && mouseButton == 0) {
/*  71 */         frame.setDragging(true);
/*  72 */         frame.dragX = mouseX - frame.getX();
/*  73 */         frame.dragY = mouseY - frame.getY();
/*     */       } 
/*  75 */       if (frame.isWithinHeader(mouseX, mouseY) && mouseButton == 1) {
/*  76 */         frame.setOpen(!frame.isOpen());
/*     */       }
/*  78 */       if (frame.isOpen() && 
/*  79 */         !frame.getComponents().isEmpty()) {
/*  80 */         frame.getComponents().forEach(comp -> comp.mouseClicked(mouseX, mouseY, mouseButton));
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void keyTyped(char typedChar, int keyCode) {
/*  88 */     for (Frame frame : frames) {
/*  89 */       if (frame.isOpen() && keyCode != 1 && 
/*  90 */         !frame.getComponents().isEmpty()) {
/*  91 */         frame.getComponents().forEach(comp -> comp.keyTyped(typedChar, keyCode));
/*     */       }
/*     */     } 
/*     */     
/*  95 */     if (keyCode == 1) this.mc.displayGuiScreen(null);
/*     */   
/*     */   }
/*     */   
/*     */   protected void mouseReleased(int mouseX, int mouseY, int state) {
/* 100 */     frames.forEach(frame -> frame.setDragging(false));
/*     */     
/* 102 */     for (Frame frame : frames) {
/* 103 */       if (frame.isOpen() && 
/* 104 */         !frame.getComponents().isEmpty()) {
/* 105 */         frame.getComponents().forEach(comp -> comp.mouseReleased(mouseX, mouseY, state));
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean doesGuiPauseGame() {
/* 113 */     return true;
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\rip\diavlo\base\ap\\ui\click\ClickGui.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */