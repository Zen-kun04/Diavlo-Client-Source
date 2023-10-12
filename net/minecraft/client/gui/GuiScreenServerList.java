/*     */ package net.minecraft.client.gui;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.net.InetAddress;
/*     */ import java.net.UnknownHostException;
/*     */ import java.util.ArrayList;
/*     */ import net.minecraft.client.multiplayer.ServerAddress;
/*     */ import net.minecraft.client.multiplayer.ServerData;
/*     */ import net.minecraft.client.network.OldServerPinger;
/*     */ import net.minecraft.client.resources.I18n;
/*     */ import org.lwjgl.input.Keyboard;
/*     */ import rip.diavlo.base.Client;
/*     */ import rip.diavlo.base.api.font.CustomFontRenderer;
/*     */ import rip.diavlo.base.utils.ColorUtil;
/*     */ import rip.diavlo.base.utils.host.GeoUtil;
/*     */ import rip.diavlo.base.utils.host.ServerUtil;
/*     */ 
/*     */ 
/*     */ 
/*     */ public class GuiScreenServerList
/*     */   extends GuiScreen
/*     */ {
/*     */   private final GuiScreen field_146303_a;
/*     */   private final ServerData field_146301_f;
/*     */   private static GuiTextField field_146302_g;
/*     */   private final ServerListEntryNormal entry;
/*  27 */   private static final OldServerPinger oldServerPinger = new OldServerPinger();
/*     */   
/*     */   private boolean changedIP = false;
/*     */   private long tick;
/*     */   private volatile String addressPort;
/*  32 */   private String lastAddress = "Pinging...";
/*     */   
/*     */   private static boolean tcpshield = false;
/*     */   
/*     */   public GuiScreenServerList(GuiScreen p_i1031_1_, ServerData p_i1031_2_) {
/*  37 */     this.field_146303_a = p_i1031_1_;
/*  38 */     this.field_146301_f = p_i1031_2_;
/*  39 */     this.entry = new ServerListEntryNormal((GuiMultiplayer)p_i1031_1_, p_i1031_2_);
/*     */   }
/*     */ 
/*     */   
/*     */   public void updateScreen() {
/*  44 */     field_146302_g.updateCursorCounter();
/*     */   }
/*     */ 
/*     */   
/*     */   public void initGui() {
/*  49 */     Keyboard.enableRepeatEvents(true);
/*  50 */     this.buttonList.clear();
/*  51 */     this.buttonList.add(new GuiButton(0, this.width / 2 - 100, this.height / 4 + 96 + 12, I18n.format("selectServer.select", new Object[0])));
/*  52 */     this.buttonList.add(new GuiButton(1, this.width / 2 - 100, this.height / 4 + 120 + 12, I18n.format("gui.cancel", new Object[0])));
/*  53 */     field_146302_g = new GuiTextField(2, this.fontRendererObj, this.width / 2 - 100, 116, 200, 20);
/*  54 */     field_146302_g.setMaxStringLength(128);
/*  55 */     field_146302_g.setFocused(true);
/*  56 */     field_146302_g.setText(this.mc.gameSettings.lastServer);
/*  57 */     ((GuiButton)this.buttonList.get(0)).enabled = (field_146302_g.getText().length() > 0 && (field_146302_g.getText().split(":")).length > 0);
/*     */   }
/*     */ 
/*     */   
/*     */   public void onGuiClosed() {
/*  62 */     Keyboard.enableRepeatEvents(false);
/*  63 */     this.mc.gameSettings.lastServer = field_146302_g.getText();
/*  64 */     this.mc.gameSettings.saveOptions();
/*     */   }
/*     */ 
/*     */   
/*     */   protected void actionPerformed(GuiButton button) throws IOException {
/*  69 */     if (button.enabled)
/*     */     {
/*  71 */       if (button.id == 1) {
/*     */         
/*  73 */         this.field_146303_a.confirmClicked(false, 0);
/*     */       }
/*  75 */       else if (button.id == 0) {
/*     */         
/*  77 */         this.field_146301_f.serverIP = field_146302_g.getText();
/*  78 */         this.field_146303_a.confirmClicked(true, 0);
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   protected void keyTyped(char typedChar, int keyCode) throws IOException {
/*  85 */     if (field_146302_g.textboxKeyTyped(typedChar, keyCode)) {
/*     */       
/*  87 */       ((GuiButton)this.buttonList.get(0)).enabled = (field_146302_g.getText().length() > 0 && (field_146302_g.getText().split(":")).length > 0);
/*     */     }
/*  89 */     else if (keyCode == 28 || keyCode == 156) {
/*     */       
/*  91 */       actionPerformed(this.buttonList.get(0));
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
/*  97 */     super.mouseClicked(mouseX, mouseY, mouseButton);
/*  98 */     field_146302_g.mouseClicked(mouseX, mouseY, mouseButton);
/*     */   }
/*     */   
/*     */   public boolean hasTimePassed(long ticks) {
/* 102 */     return (this.tick >= ticks);
/*     */   }
/*     */   
/*     */   public static void checkTCPShield() {
/*     */     try {
/* 107 */       InetAddress address = InetAddress.getByName(ServerAddress.fromString(field_146302_g.getText()).getIP());
/* 108 */       String ip = address.getHostAddress();
/* 109 */       ip = ip + ":" + ServerAddress.fromString(field_146302_g.getText()).getPort();
/*     */       
/*     */       try {
/* 112 */         oldServerPinger.ping(new ServerData(field_146302_g.getText(), field_146302_g.getText(), true));
/* 113 */       } catch (UnknownHostException var2) {
/* 114 */         tcpshield = false;
/*     */         return;
/* 116 */       } catch (Exception exception) {}
/*     */       
/* 118 */       ArrayList<String> server = ServerUtil.pingServer(ip);
/*     */       
/* 120 */       if (server.isEmpty()) tcpshield = true; 
/* 121 */     } catch (Exception e) {
/* 122 */       tcpshield = false;
/*     */     } 
/*     */   }
/*     */   
/*     */   public void drawScreen(int mouseX, int mouseY, float partialTicks) {
/* 127 */     CustomFontRenderer font = Client.getInstance().getFontManager().getDefaultFont().size(18);
/*     */     
/* 129 */     drawDefaultBackground();
/*     */     
/* 131 */     this.field_146301_f.serverIP = field_146302_g.getText();
/* 132 */     this.field_146301_f.serverName = field_146302_g.getText();
/*     */     
/* 134 */     this.tick++;
/*     */     
/* 136 */     long max = 166L;
/* 137 */     int x = this.width / 2 - 150;
/* 138 */     int w = 275;
/* 139 */     int y = 85;
/* 140 */     int h = 1;
/*     */     
/* 142 */     drawRect(x, y, (x + (int)(this.tick / max * w)), (y + h), ColorUtil.rainbow(50));
/*     */     
/* 144 */     if (hasTimePassed(max)) {
/* 145 */       this.entry.setServer(this.field_146301_f);
/* 146 */       this.entry.ping();
/* 147 */       this.tick = 0L;
/*     */     } 
/*     */     
/* 150 */     ServerData serverData = this.field_146301_f;
/* 151 */     int maxHeight = 100;
/* 152 */     int maxWidth = this.width / 2 + 120;
/* 153 */     int adder = 12;
/*     */     
/* 155 */     (new Thread(() -> {
/*     */           try {
/*     */             ServerAddress serverAddress = ServerAddress.resolveAddress(this.field_146301_f.serverIP);
/*     */             
/*     */             String address = InetAddress.getByName(serverAddress.getIP()).getHostAddress();
/*     */             if (!this.lastAddress.equals(address)) {
/*     */               this.lastAddress = address;
/*     */               new GeoUtil(address);
/*     */               this.addressPort = InetAddress.getByName(serverAddress.getIP()).getHostAddress() + " " + serverAddress.getPort();
/*     */             } 
/* 165 */           } catch (Exception exception) {}
/* 166 */         }"PingThread-")).start();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     try {
/* 179 */       if (serverData.pingToServer < 0L) {
/* 180 */         font.drawStringWithShadow("§fIP: §7Pinging...", maxWidth, (maxHeight += adder), -1);
/*     */       } else {
/* 182 */         font.drawStringWithShadow("§fIP: §7" + this.addressPort.replace("null", "").replaceAll("127.0.0.1 25565", "Pinging..."), maxWidth, (maxHeight += adder), -1);
/*     */       } 
/* 184 */       if (serverData.pingToServer < 0L) {
/* 185 */         font.drawStringWithShadow("§fOrganization: §7Pinging...", maxWidth, (maxHeight += adder), -1);
/*     */       } else {
/* 187 */         font.drawStringWithShadow("§fOrganization: §7" + GeoUtil.getInstance().getOrganization().replaceAll("- Connecting your World!", "").replaceAll("Cloud ", "").replaceAll("- DDoS-Protected Gameservers and more", "").replaceAll("www.", "").replaceAll("Corp.", "").replaceAll("(haftungsbeschraenkt) & Co. KG", "").replaceAll("trading as Gericke KG", ""), maxWidth, (maxHeight += adder), -1);
/*     */       } 
/* 189 */       if (serverData.pingToServer < 0L) {
/* 190 */         font.drawStringWithShadow("§fAS: §7Pinging...", maxWidth, (maxHeight += adder), -1);
/*     */       } else {
/* 192 */         font.drawStringWithShadow("§fAS: §7" + GeoUtil.getInstance().getAS().replaceAll("Center ", "").replaceAll("oration", "").replaceAll("Waldecker trading as LUMASERV Systems", "").replaceAll("GmbH", "").replaceAll(". Inc.", "").replaceAll(", LLC", "").replaceAll("Corp.", "").replaceAll("TeleHost", "Tele").replaceAll("e-commerce", "").replaceAll("IT Services & Consulting", "").replaceAll("UG (haftungsbeschrankt)", "").replaceAll("is trading as SYNLINQ", ""), maxWidth, (maxHeight += adder), -1);
/*     */       } 
/* 194 */       if (serverData.pingToServer < 0L) {
/* 195 */         font.drawStringWithShadow("§fCountry: §7Pinging,,,", maxWidth, (maxHeight += adder), -1);
/*     */       } else {
/* 197 */         font.drawStringWithShadow("§fCountry: §7" + GeoUtil.getInstance().getCountry(), maxWidth, (maxHeight += adder), -1);
/*     */       } 
/* 199 */       if (serverData.pingToServer < 0L) {
/* 200 */         font.drawStringWithShadow("§fCity: §7Pinging...", maxWidth, (maxHeight += adder), -1);
/*     */       } else {
/* 202 */         font.drawStringWithShadow("§fCity: §7" + GeoUtil.getInstance().getCITY(), maxWidth, (maxHeight += adder), -1);
/*     */       } 
/* 204 */       if (serverData.pingToServer < 0L) {
/* 205 */         font.drawStringWithShadow("§fRegion: §7Pinging...", maxWidth, (maxHeight += adder), -1);
/*     */       } else {
/* 207 */         font.drawStringWithShadow("§fRegion: §7" + GeoUtil.getInstance().getRegion(), maxWidth, (maxHeight += adder), -1);
/*     */       } 
/* 209 */       if (serverData.pingToServer < 0L) {
/* 210 */         font.drawStringWithShadow("§fISP: §7Pinging...", maxWidth, (maxHeight += adder), -1);
/*     */       } else {
/* 212 */         font.drawStringWithShadow("§fISP: §7" + GeoUtil.getInstance().getISP().replaceAll("is trading as \"SYNLINQ\"", ""), maxWidth, (maxHeight += adder), -1);
/*     */       } 
/* 214 */       if (serverData.pingToServer < 0L) {
/* 215 */         font.drawStringWithShadow("§fProxy: §7Pinging...", maxWidth, (maxHeight += adder), -1);
/*     */       } else {
/* 217 */         font.drawStringWithShadow("§fProxy: §7" + GeoUtil.getInstance().getProxy(), maxWidth, (maxHeight += adder), -1);
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*     */       }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     }
/* 229 */     catch (Exception exception) {}
/*     */     
/* 231 */     this.entry.drawEntry(0, (int)((this.width / 2) - 137.5D), 50, 275, 35, 0, 0, false);
/* 232 */     if (this.changedIP) {
/* 233 */       this.changedIP = false;
/*     */     }
/* 235 */     drawCenteredString(this.fontRendererObj, I18n.format("selectServer.direct", new Object[0]), this.width / 2, 20, 16777215);
/* 236 */     drawString(this.fontRendererObj, I18n.format("addServer.enterIp", new Object[0]), this.width / 2 - 100, 100, 10526880);
/* 237 */     field_146302_g.drawTextBox();
/* 238 */     super.drawScreen(mouseX, mouseY, partialTicks);
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\client\gui\GuiScreenServerList.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */