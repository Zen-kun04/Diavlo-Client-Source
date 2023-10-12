/*     */ package net.minecraft.client.gui;
/*     */ 
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.audio.ISound;
/*     */ import net.minecraft.client.audio.PositionedSoundRecord;
/*     */ import net.minecraft.client.audio.SoundHandler;
/*     */ import net.minecraft.util.ResourceLocation;
/*     */ import org.lwjgl.opengl.GL11;
/*     */ import rip.diavlo.base.Client;
/*     */ import rip.diavlo.base.utils.render.RenderUtil;
/*     */ 
/*     */ public class GuiButton extends Gui {
/*  13 */   protected static final ResourceLocation buttonTextures = new ResourceLocation("textures/gui/widgets.png");
/*     */   
/*     */   protected int width;
/*     */   
/*     */   protected int height;
/*     */   public int xPosition;
/*     */   public int yPosition;
/*     */   public String displayString;
/*     */   public int id;
/*     */   public boolean enabled;
/*     */   public boolean visible;
/*     */   protected boolean hovered;
/*     */   private boolean background = true;
/*     */   
/*     */   public GuiButton(int buttonId, int x, int y, String buttonText) {
/*  28 */     this(buttonId, x, y, 200, 20, buttonText);
/*     */   }
/*     */ 
/*     */   
/*     */   public GuiButton(int buttonId, int x, int y, String buttonText, boolean background) {
/*  33 */     this(buttonId, x, y, 200, 20, buttonText);
/*  34 */     this.background = background;
/*     */   }
/*     */ 
/*     */   
/*     */   public GuiButton(int buttonId, int x, int y, int widthIn, int heightIn, String buttonText) {
/*  39 */     this.width = 200;
/*  40 */     this.height = 20;
/*  41 */     this.enabled = true;
/*  42 */     this.visible = true;
/*  43 */     this.id = buttonId;
/*  44 */     this.xPosition = x;
/*  45 */     this.yPosition = y;
/*  46 */     this.width = widthIn;
/*  47 */     this.height = heightIn;
/*  48 */     this.displayString = buttonText;
/*     */   }
/*     */ 
/*     */   
/*     */   protected int getHoverState(boolean mouseOver) {
/*  53 */     int i = 1;
/*     */     
/*  55 */     if (!this.enabled) {
/*     */       
/*  57 */       i = 0;
/*     */     }
/*  59 */     else if (mouseOver) {
/*     */       
/*  61 */       i = 2;
/*     */     } 
/*     */     
/*  64 */     return i;
/*     */   }
/*     */ 
/*     */   
/*     */   public void drawButton(Minecraft mc, int mouseX, int mouseY) {
/*  69 */     if (this.visible) {
/*  70 */       FontRenderer fontrenderer = mc.fontRendererObj;
/*  71 */       this.hovered = (mouseX >= this.xPosition && mouseY >= this.yPosition && mouseX < this.xPosition + this.width && mouseY < this.yPosition + this.height);
/*     */ 
/*     */       
/*  74 */       Gui.drawRect(this.xPosition, this.yPosition, (this.xPosition + this.width), (this.yPosition + this.height), 2013265920);
/*     */       
/*  76 */       if (this.hovered && this.enabled) {
/*  77 */         GL11.glPushMatrix();
/*  78 */         GL11.glEnable(3042);
/*  79 */         GL11.glBlendFunc(770, 771);
/*  80 */         GL11.glDisable(3553);
/*  81 */         RenderUtil.color(Client.getInstance().getClientColor());
/*  82 */         GL11.glTranslatef(this.xPosition, this.yPosition, 0.0F);
/*  83 */         GL11.glLineWidth(1.0F);
/*  84 */         GL11.glBegin(2);
/*  85 */         GL11.glVertex2f(0.0F, 0.0F);
/*  86 */         GL11.glVertex2f(this.width, 0.0F);
/*  87 */         GL11.glVertex2f(this.width, this.height);
/*  88 */         GL11.glVertex2f(0.0F, this.height);
/*  89 */         GL11.glEnd();
/*  90 */         GL11.glEnable(3553);
/*  91 */         GL11.glDisable(3042);
/*  92 */         GL11.glPopMatrix();
/*     */       } 
/*  94 */       mouseDragged(mc, mouseX, mouseY);
/*  95 */       int j = 14737632;
/*  96 */       if (!this.enabled) {
/*     */         
/*  98 */         j = 10526880;
/*     */       }
/* 100 */       else if (this.hovered) {
/*     */         
/* 102 */         j = Client.getInstance().getClientColor();
/*     */       } 
/*     */       
/* 105 */       drawCenteredString(fontrenderer, this.displayString, this.xPosition + this.width / 2, this.yPosition + (this.height - 8) / 2, j);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void mouseDragged(Minecraft mc, int mouseX, int mouseY) {}
/*     */ 
/*     */ 
/*     */   
/*     */   public void mouseReleased(int mouseX, int mouseY) {}
/*     */ 
/*     */   
/*     */   public boolean mousePressed(Minecraft mc, int mouseX, int mouseY) {
/* 119 */     return (this.enabled && this.visible && mouseX >= this.xPosition && mouseY >= this.yPosition && mouseX < this.xPosition + this.width && mouseY < this.yPosition + this.height);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isMouseOver() {
/* 124 */     return this.hovered;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void drawButtonForegroundLayer(int mouseX, int mouseY) {}
/*     */ 
/*     */   
/*     */   public void playPressSound(SoundHandler soundHandlerIn) {
/* 133 */     soundHandlerIn.playSound((ISound)PositionedSoundRecord.create(new ResourceLocation("gui.button.press"), 1.0F));
/*     */   }
/*     */ 
/*     */   
/*     */   public int getButtonWidth() {
/* 138 */     return this.width;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setWidth(int width) {
/* 143 */     this.width = width;
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\client\gui\GuiButton.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */