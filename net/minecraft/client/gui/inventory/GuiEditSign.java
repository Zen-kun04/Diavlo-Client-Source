/*     */ package net.minecraft.client.gui.inventory;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import net.minecraft.block.Block;
/*     */ import net.minecraft.client.gui.GuiButton;
/*     */ import net.minecraft.client.gui.GuiScreen;
/*     */ import net.minecraft.client.network.NetHandlerPlayClient;
/*     */ import net.minecraft.client.renderer.GlStateManager;
/*     */ import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
/*     */ import net.minecraft.client.resources.I18n;
/*     */ import net.minecraft.init.Blocks;
/*     */ import net.minecraft.network.Packet;
/*     */ import net.minecraft.network.play.client.C12PacketUpdateSign;
/*     */ import net.minecraft.tileentity.TileEntity;
/*     */ import net.minecraft.tileentity.TileEntitySign;
/*     */ import net.minecraft.util.ChatAllowedCharacters;
/*     */ import net.minecraft.util.ChatComponentText;
/*     */ import net.minecraft.util.IChatComponent;
/*     */ import org.lwjgl.input.Keyboard;
/*     */ 
/*     */ public class GuiEditSign
/*     */   extends GuiScreen {
/*     */   private TileEntitySign tileSign;
/*     */   private int updateCounter;
/*     */   
/*     */   public GuiEditSign(TileEntitySign teSign) {
/*  27 */     this.tileSign = teSign;
/*     */   }
/*     */   private int editLine; private GuiButton doneBtn;
/*     */   
/*     */   public void initGui() {
/*  32 */     this.buttonList.clear();
/*  33 */     Keyboard.enableRepeatEvents(true);
/*  34 */     this.buttonList.add(this.doneBtn = new GuiButton(0, this.width / 2 - 100, this.height / 4 + 120, I18n.format("gui.done", new Object[0])));
/*  35 */     this.tileSign.setEditable(false);
/*     */   }
/*     */ 
/*     */   
/*     */   public void onGuiClosed() {
/*  40 */     Keyboard.enableRepeatEvents(false);
/*  41 */     NetHandlerPlayClient nethandlerplayclient = this.mc.getNetHandler();
/*     */     
/*  43 */     if (nethandlerplayclient != null)
/*     */     {
/*  45 */       nethandlerplayclient.addToSendQueue((Packet)new C12PacketUpdateSign(this.tileSign.getPos(), this.tileSign.signText));
/*     */     }
/*     */     
/*  48 */     this.tileSign.setEditable(true);
/*     */   }
/*     */ 
/*     */   
/*     */   public void updateScreen() {
/*  53 */     this.updateCounter++;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void actionPerformed(GuiButton button) throws IOException {
/*  58 */     if (button.enabled)
/*     */     {
/*  60 */       if (button.id == 0) {
/*     */         
/*  62 */         this.tileSign.markDirty();
/*  63 */         this.mc.displayGuiScreen((GuiScreen)null);
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   protected void keyTyped(char typedChar, int keyCode) throws IOException {
/*  70 */     if (keyCode == 200)
/*     */     {
/*  72 */       this.editLine = this.editLine - 1 & 0x3;
/*     */     }
/*     */     
/*  75 */     if (keyCode == 208 || keyCode == 28 || keyCode == 156)
/*     */     {
/*  77 */       this.editLine = this.editLine + 1 & 0x3;
/*     */     }
/*     */     
/*  80 */     String s = this.tileSign.signText[this.editLine].getUnformattedText();
/*     */     
/*  82 */     if (keyCode == 14 && s.length() > 0)
/*     */     {
/*  84 */       s = s.substring(0, s.length() - 1);
/*     */     }
/*     */     
/*  87 */     if (ChatAllowedCharacters.isAllowedCharacter(typedChar) && this.fontRendererObj.getStringWidth(s + typedChar) <= 90)
/*     */     {
/*  89 */       s = s + typedChar;
/*     */     }
/*     */     
/*  92 */     this.tileSign.signText[this.editLine] = (IChatComponent)new ChatComponentText(s);
/*     */     
/*  94 */     if (keyCode == 1)
/*     */     {
/*  96 */       actionPerformed(this.doneBtn);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void drawScreen(int mouseX, int mouseY, float partialTicks) {
/* 102 */     drawDefaultBackground();
/* 103 */     drawCenteredString(this.fontRendererObj, I18n.format("sign.edit", new Object[0]), this.width / 2, 40, 16777215);
/* 104 */     GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
/* 105 */     GlStateManager.pushMatrix();
/* 106 */     GlStateManager.translate((this.width / 2), 0.0F, 50.0F);
/* 107 */     float f = 93.75F;
/* 108 */     GlStateManager.scale(-f, -f, -f);
/* 109 */     GlStateManager.rotate(180.0F, 0.0F, 1.0F, 0.0F);
/* 110 */     Block block = this.tileSign.getBlockType();
/*     */     
/* 112 */     if (block == Blocks.standing_sign) {
/*     */       
/* 114 */       float f1 = (this.tileSign.getBlockMetadata() * 360) / 16.0F;
/* 115 */       GlStateManager.rotate(f1, 0.0F, 1.0F, 0.0F);
/* 116 */       GlStateManager.translate(0.0F, -1.0625F, 0.0F);
/*     */     }
/*     */     else {
/*     */       
/* 120 */       int i = this.tileSign.getBlockMetadata();
/* 121 */       float f2 = 0.0F;
/*     */       
/* 123 */       if (i == 2)
/*     */       {
/* 125 */         f2 = 180.0F;
/*     */       }
/*     */       
/* 128 */       if (i == 4)
/*     */       {
/* 130 */         f2 = 90.0F;
/*     */       }
/*     */       
/* 133 */       if (i == 5)
/*     */       {
/* 135 */         f2 = -90.0F;
/*     */       }
/*     */       
/* 138 */       GlStateManager.rotate(f2, 0.0F, 1.0F, 0.0F);
/* 139 */       GlStateManager.translate(0.0F, -1.0625F, 0.0F);
/*     */     } 
/*     */     
/* 142 */     if (this.updateCounter / 6 % 2 == 0)
/*     */     {
/* 144 */       this.tileSign.lineBeingEdited = this.editLine;
/*     */     }
/*     */     
/* 147 */     TileEntityRendererDispatcher.instance.renderTileEntityAt((TileEntity)this.tileSign, -0.5D, -0.75D, -0.5D, 0.0F);
/* 148 */     this.tileSign.lineBeingEdited = -1;
/* 149 */     GlStateManager.popMatrix();
/* 150 */     super.drawScreen(mouseX, mouseY, partialTicks);
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\client\gui\inventory\GuiEditSign.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */