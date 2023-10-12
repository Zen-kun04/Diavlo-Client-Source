/*    */ package net.minecraft.client.gui;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.util.List;
/*    */ import net.minecraft.client.multiplayer.GuiConnecting;
/*    */ import net.minecraft.client.multiplayer.ServerData;
/*    */ import net.minecraft.client.resources.I18n;
/*    */ import net.minecraft.util.IChatComponent;
/*    */ import rip.diavlo.base.Client;
/*    */ import rip.diavlo.base.viaversion.viamcp.ViaMCP;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class GuiDisconnected
/*    */   extends GuiScreen
/*    */ {
/*    */   private String reason;
/*    */   private IChatComponent message;
/*    */   private List<String> multilineMessage;
/*    */   private final GuiScreen parentScreen;
/*    */   private int field_175353_i;
/*    */   private long reconnectTime;
/*    */   
/*    */   public GuiDisconnected(GuiScreen screen, String reasonLocalizationKey, IChatComponent chatComp) {
/* 25 */     this.parentScreen = screen;
/* 26 */     this.reason = I18n.format(reasonLocalizationKey, new Object[0]);
/* 27 */     this.message = chatComp;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   protected void keyTyped(char typedChar, int keyCode) throws IOException {}
/*    */ 
/*    */   
/*    */   public void initGui() {
/* 36 */     this.buttonList.clear();
/* 37 */     this.multilineMessage = this.fontRendererObj.listFormattedStringToWidth(this.message.getFormattedText(), this.width - 50);
/* 38 */     this.field_175353_i = this.multilineMessage.size() * this.fontRendererObj.FONT_HEIGHT;
/* 39 */     this.buttonList.add(new GuiButton(0, this.width / 2 - 100, this.height / 2 + this.field_175353_i / 2 + this.fontRendererObj.FONT_HEIGHT, I18n.format("gui.toMenu", new Object[0])));
/* 40 */     this.buttonList.add(new GuiButton(1, this.width / 2 - 100, this.height / 2 + this.field_175353_i / 2 + 33, "§cAuto Reconnect"));
/*    */   }
/*    */ 
/*    */   
/*    */   protected void actionPerformed(GuiButton button) throws IOException {
/* 45 */     if (button.id == 0) {
/*    */       
/* 47 */       this.mc.displayGuiScreen(this.parentScreen);
/* 48 */     } else if (button.id == 1) {
/* 49 */       Client.getInstance().getSpoofingUtil().setAutoReconnect(!Client.getInstance().getSpoofingUtil().isAutoReconnect());
/* 50 */       this.reconnectTime = System.currentTimeMillis() + 1500L;
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public void drawScreen(int mouseX, int mouseY, float partialTicks) {
/* 56 */     drawDefaultBackground();
/* 57 */     drawCenteredString(this.fontRendererObj, this.reason, this.width / 2, this.height / 2 - this.field_175353_i / 2 - this.fontRendererObj.FONT_HEIGHT * 2, 11184810);
/* 58 */     int i = this.height / 2 - this.field_175353_i / 2;
/*    */     
/* 60 */     if (this.multilineMessage != null)
/*    */     {
/* 62 */       for (String s : this.multilineMessage) {
/*    */         
/* 64 */         drawCenteredString(this.fontRendererObj, s, this.width / 2, i, 16777215);
/* 65 */         i += this.fontRendererObj.FONT_HEIGHT;
/*    */       } 
/*    */     }
/*    */     
/* 69 */     if (Client.getInstance().getSpoofingUtil().isAutoReconnect()) {
/* 70 */       drawCenteredString(this.fontRendererObj, "Relog Time: " + Math.max(this.reconnectTime - System.currentTimeMillis(), 0L) + "ms", this.width / 2, 62, -1);
/* 71 */       if (System.currentTimeMillis() >= this.reconnectTime) {
/* 72 */         this.mc.displayGuiScreen((GuiScreen)new GuiConnecting(new GuiMultiplayer(new GuiMainMenu()), this.mc, new ServerData(ViaMCP.INSTANCE.getLastServer(), ViaMCP.INSTANCE.getLastServer(), false)));
/*    */         
/*    */         return;
/*    */       } 
/*    */     } 
/* 77 */     super.drawScreen(mouseX, mouseY, partialTicks);
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\client\gui\GuiDisconnected.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */