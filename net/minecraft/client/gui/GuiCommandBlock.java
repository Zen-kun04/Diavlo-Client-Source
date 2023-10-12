/*     */ package net.minecraft.client.gui;
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import io.netty.buffer.Unpooled;
/*     */ import java.io.IOException;
/*     */ import net.minecraft.client.resources.I18n;
/*     */ import net.minecraft.command.server.CommandBlockLogic;
/*     */ import net.minecraft.network.Packet;
/*     */ import net.minecraft.network.PacketBuffer;
/*     */ import net.minecraft.network.play.client.C17PacketCustomPayload;
/*     */ import net.minecraft.util.IChatComponent;
/*     */ import org.apache.logging.log4j.LogManager;
/*     */ import org.apache.logging.log4j.Logger;
/*     */ import org.lwjgl.input.Keyboard;
/*     */ 
/*     */ public class GuiCommandBlock extends GuiScreen {
/*  16 */   private static final Logger field_146488_a = LogManager.getLogger();
/*     */   
/*     */   private GuiTextField commandTextField;
/*     */   private GuiTextField previousOutputTextField;
/*     */   private final CommandBlockLogic localCommandBlock;
/*     */   private GuiButton doneBtn;
/*     */   private GuiButton cancelBtn;
/*     */   private GuiButton field_175390_s;
/*     */   private boolean field_175389_t;
/*     */   
/*     */   public GuiCommandBlock(CommandBlockLogic p_i45032_1_) {
/*  27 */     this.localCommandBlock = p_i45032_1_;
/*     */   }
/*     */ 
/*     */   
/*     */   public void updateScreen() {
/*  32 */     this.commandTextField.updateCursorCounter();
/*     */   }
/*     */ 
/*     */   
/*     */   public void initGui() {
/*  37 */     Keyboard.enableRepeatEvents(true);
/*  38 */     this.buttonList.clear();
/*  39 */     this.buttonList.add(this.doneBtn = new GuiButton(0, this.width / 2 - 4 - 150, this.height / 4 + 120 + 12, 150, 20, I18n.format("gui.done", new Object[0])));
/*  40 */     this.buttonList.add(this.cancelBtn = new GuiButton(1, this.width / 2 + 4, this.height / 4 + 120 + 12, 150, 20, I18n.format("gui.cancel", new Object[0])));
/*  41 */     this.buttonList.add(this.field_175390_s = new GuiButton(4, this.width / 2 + 150 - 20, 150, 20, 20, "O"));
/*  42 */     this.commandTextField = new GuiTextField(2, this.fontRendererObj, this.width / 2 - 150, 50, 300, 20);
/*  43 */     this.commandTextField.setMaxStringLength(32767);
/*  44 */     this.commandTextField.setFocused(true);
/*  45 */     this.commandTextField.setText(this.localCommandBlock.getCommand());
/*  46 */     this.previousOutputTextField = new GuiTextField(3, this.fontRendererObj, this.width / 2 - 150, 150, 276, 20);
/*  47 */     this.previousOutputTextField.setMaxStringLength(32767);
/*  48 */     this.previousOutputTextField.setEnabled(false);
/*  49 */     this.previousOutputTextField.setText("-");
/*  50 */     this.field_175389_t = this.localCommandBlock.shouldTrackOutput();
/*  51 */     func_175388_a();
/*  52 */     this.doneBtn.enabled = (this.commandTextField.getText().trim().length() > 0);
/*     */   }
/*     */ 
/*     */   
/*     */   public void onGuiClosed() {
/*  57 */     Keyboard.enableRepeatEvents(false);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void actionPerformed(GuiButton button) throws IOException {
/*  62 */     if (button.enabled)
/*     */     {
/*  64 */       if (button.id == 1) {
/*     */         
/*  66 */         this.localCommandBlock.setTrackOutput(this.field_175389_t);
/*  67 */         this.mc.displayGuiScreen((GuiScreen)null);
/*     */       }
/*  69 */       else if (button.id == 0) {
/*     */         
/*  71 */         PacketBuffer packetbuffer = new PacketBuffer(Unpooled.buffer());
/*  72 */         packetbuffer.writeByte(this.localCommandBlock.func_145751_f());
/*  73 */         this.localCommandBlock.func_145757_a((ByteBuf)packetbuffer);
/*  74 */         packetbuffer.writeString(this.commandTextField.getText());
/*  75 */         packetbuffer.writeBoolean(this.localCommandBlock.shouldTrackOutput());
/*  76 */         this.mc.getNetHandler().addToSendQueue((Packet)new C17PacketCustomPayload("MC|AdvCdm", packetbuffer));
/*     */         
/*  78 */         if (!this.localCommandBlock.shouldTrackOutput())
/*     */         {
/*  80 */           this.localCommandBlock.setLastOutput((IChatComponent)null);
/*     */         }
/*     */         
/*  83 */         this.mc.displayGuiScreen((GuiScreen)null);
/*     */       }
/*  85 */       else if (button.id == 4) {
/*     */         
/*  87 */         this.localCommandBlock.setTrackOutput(!this.localCommandBlock.shouldTrackOutput());
/*  88 */         func_175388_a();
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   protected void keyTyped(char typedChar, int keyCode) throws IOException {
/*  95 */     this.commandTextField.textboxKeyTyped(typedChar, keyCode);
/*  96 */     this.previousOutputTextField.textboxKeyTyped(typedChar, keyCode);
/*  97 */     this.doneBtn.enabled = (this.commandTextField.getText().trim().length() > 0);
/*     */     
/*  99 */     if (keyCode != 28 && keyCode != 156) {
/*     */       
/* 101 */       if (keyCode == 1)
/*     */       {
/* 103 */         actionPerformed(this.cancelBtn);
/*     */       }
/*     */     }
/*     */     else {
/*     */       
/* 108 */       actionPerformed(this.doneBtn);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
/* 114 */     super.mouseClicked(mouseX, mouseY, mouseButton);
/* 115 */     this.commandTextField.mouseClicked(mouseX, mouseY, mouseButton);
/* 116 */     this.previousOutputTextField.mouseClicked(mouseX, mouseY, mouseButton);
/*     */   }
/*     */ 
/*     */   
/*     */   public void drawScreen(int mouseX, int mouseY, float partialTicks) {
/* 121 */     drawDefaultBackground();
/* 122 */     drawCenteredString(this.fontRendererObj, I18n.format("advMode.setCommand", new Object[0]), this.width / 2, 20, 16777215);
/* 123 */     drawString(this.fontRendererObj, I18n.format("advMode.command", new Object[0]), this.width / 2 - 150, 37, 10526880);
/* 124 */     this.commandTextField.drawTextBox();
/* 125 */     int i = 75;
/* 126 */     int j = 0;
/* 127 */     drawString(this.fontRendererObj, I18n.format("advMode.nearestPlayer", new Object[0]), this.width / 2 - 150, i + j++ * this.fontRendererObj.FONT_HEIGHT, 10526880);
/* 128 */     drawString(this.fontRendererObj, I18n.format("advMode.randomPlayer", new Object[0]), this.width / 2 - 150, i + j++ * this.fontRendererObj.FONT_HEIGHT, 10526880);
/* 129 */     drawString(this.fontRendererObj, I18n.format("advMode.allPlayers", new Object[0]), this.width / 2 - 150, i + j++ * this.fontRendererObj.FONT_HEIGHT, 10526880);
/* 130 */     drawString(this.fontRendererObj, I18n.format("advMode.allEntities", new Object[0]), this.width / 2 - 150, i + j++ * this.fontRendererObj.FONT_HEIGHT, 10526880);
/* 131 */     drawString(this.fontRendererObj, "", this.width / 2 - 150, i + j++ * this.fontRendererObj.FONT_HEIGHT, 10526880);
/*     */     
/* 133 */     if (this.previousOutputTextField.getText().length() > 0) {
/*     */       
/* 135 */       i = i + j * this.fontRendererObj.FONT_HEIGHT + 16;
/* 136 */       drawString(this.fontRendererObj, I18n.format("advMode.previousOutput", new Object[0]), this.width / 2 - 150, i, 10526880);
/* 137 */       this.previousOutputTextField.drawTextBox();
/*     */     } 
/*     */     
/* 140 */     super.drawScreen(mouseX, mouseY, partialTicks);
/*     */   }
/*     */ 
/*     */   
/*     */   private void func_175388_a() {
/* 145 */     if (this.localCommandBlock.shouldTrackOutput()) {
/*     */       
/* 147 */       this.field_175390_s.displayString = "O";
/*     */       
/* 149 */       if (this.localCommandBlock.getLastOutput() != null)
/*     */       {
/* 151 */         this.previousOutputTextField.setText(this.localCommandBlock.getLastOutput().getUnformattedText());
/*     */       }
/*     */     }
/*     */     else {
/*     */       
/* 156 */       this.field_175390_s.displayString = "X";
/* 157 */       this.previousOutputTextField.setText("-");
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\client\gui\GuiCommandBlock.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */