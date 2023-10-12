/*     */ package rip.diavlo.base.api.ui.griefing;
/*     */ 
/*     */ import com.mojang.authlib.GameProfile;
/*     */ import java.io.BufferedReader;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStreamReader;
/*     */ import java.net.HttpURLConnection;
/*     */ import java.net.URL;
/*     */ import java.util.stream.Collectors;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.gui.GuiButton;
/*     */ import net.minecraft.client.gui.GuiScreen;
/*     */ import net.minecraft.client.gui.GuiTextField;
/*     */ import net.minecraft.util.Session;
/*     */ import org.json.JSONObject;
/*     */ import rip.diavlo.base.Client;
/*     */ import rip.diavlo.base.api.ui.alt.AltLoginThread;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class GuiUUIDSpoof
/*     */   extends GuiScreen
/*     */ {
/*     */   protected GuiTextField fakeNickField;
/*     */   protected GuiTextField realNickField;
/*     */   protected GuiScreen prevScreen;
/*     */   private AltLoginThread thread;
/*     */   private String Report;
/*     */   
/*     */   public GuiUUIDSpoof(GuiScreen screen) {
/*  33 */     this.prevScreen = screen;
/*     */   }
/*     */   
/*     */   public void initGui() {
/*  37 */     this.buttonList.clear();
/*     */     
/*  39 */     this.buttonList.add(new GuiButton(1, this.width / 2 - 100, this.height / 4 + 130 + 20 + 8, 200, 20, "Spoof"));
/*     */     
/*  41 */     this.buttonList.add(new GuiButton(2, this.width / 2 - 100, this.height / 4 + 130 + 20 + 30, 200, 20, "Back"));
/*     */ 
/*     */     
/*  44 */     this.buttonList.add(new GuiButton(222, this.width / 2 - 100, this.height / 4 + 130 + 20 + 30 + 22, 200, 20, "§3Luck§fPerms §cOld Exploit"));
/*     */ 
/*     */     
/*  47 */     this.buttonList.add(new GuiButton(4, this.width / 2 - 100, this.height / 4 + 130, 200, 20, 
/*  48 */           PremiumUUID()));
/*  49 */     this.fakeNickField = new GuiTextField(1, this.fontRendererObj, this.width / 2 - 100, this.height / 4 + 60, 200, 20);
/*     */     
/*  51 */     this.realNickField = new GuiTextField(2, this.fontRendererObj, this.width / 2 - 100, this.height / 4 + 20, 200, 20);
/*     */     
/*  53 */     this.fakeNickField.setText(Client.getInstance().getSpoofingUtil().getFakeNick());
/*  54 */     GameProfile profile = Minecraft.getMinecraft().getSession().getProfile();
/*  55 */     this.realNickField.setText(profile.getName());
/*     */   }
/*     */   
/*     */   private String PremiumUUID() {
/*  59 */     return Client.getInstance().getSpoofingUtil().isPremiumUUID() ? "Premium UUID: §aSi" : "Premium UUID: §cNo";
/*     */   }
/*     */   
/*     */   public void handleMouseInput() throws IOException {
/*  63 */     super.handleMouseInput();
/*     */   }
/*     */   
/*     */   protected void actionPerformed(GuiButton button) throws IOException {
/*  67 */     if (button.id == 1) {
/*  68 */       if (!Client.getInstance().getSpoofingUtil().isPremiumUUID()) {
/*  69 */         Session realSession = this.mc.getSession();
/*  70 */         Session copiedSession = new Session(this.realNickField.getText(), realSession.getPlayerID(), realSession.getToken(), Session.Type.LEGACY.name());
/*  71 */         Client.getInstance().getSpoofingUtil().setSession(copiedSession);
/*  72 */         Client.getInstance().getSpoofingUtil().setFakeNick(this.fakeNickField.getText());
/*  73 */         this.thread = new AltLoginThread(this.realNickField.getText(), "");
/*  74 */         this.thread.start();
/*  75 */         Client.getInstance().getSpoofingUtil().setPremiumSession(false);
/*  76 */         this.mc.displayGuiScreen(this.prevScreen);
/*     */       } else {
/*     */         try {
/*  79 */           Client.getInstance().getSpoofingUtil().setPreUUID(fetchUUID(this.realNickField.getText()));
/*  80 */           Session realSession = this.mc.getSession();
/*  81 */           Session copiedSession = new Session(this.realNickField.getText(), realSession.getPlayerID(), realSession.getToken(), Session.Type.LEGACY.name());
/*  82 */           Client.getInstance().getSpoofingUtil().setSession(copiedSession);
/*  83 */           Client.getInstance().getSpoofingUtil().setFakeNick(this.fakeNickField.getText());
/*  84 */           this.thread = new AltLoginThread(this.realNickField.getText(), "");
/*  85 */           this.thread.start();
/*  86 */           Client.getInstance().getSpoofingUtil().setPremiumSession(false);
/*  87 */           this.Report = "§aSucessfully spoofed Premium UUID of " + this.fakeNickField.getText();
/*  88 */         } catch (Exception ex) {
/*  89 */           this.Report = "§cNick: " + this.fakeNickField.getText() + " no es premium!";
/*     */         } 
/*     */       } 
/*  92 */     } else if (button.id == 2) {
/*  93 */       this.mc.displayGuiScreen(this.prevScreen);
/*     */     } 
/*  95 */     if (button.id == 4) {
/*  96 */       Client.getInstance().getSpoofingUtil().setPremiumUUID(!Client.getInstance().getSpoofingUtil().isPremiumUUID());
/*  97 */       button.displayString = PremiumUUID();
/*     */     } 
/*  99 */     if (button.id == 222) {
/* 100 */       this.fakeNickField.setText("00000000000000000000000000000000");
/*     */     }
/*     */   }
/*     */   
/*     */   protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
/* 105 */     this.fakeNickField.mouseClicked(mouseX, mouseY, mouseButton);
/* 106 */     this.realNickField.mouseClicked(mouseX, mouseY, mouseButton);
/* 107 */     super.mouseClicked(mouseX, mouseY, mouseButton);
/*     */   }
/*     */   
/*     */   protected void keyTyped(char typedChar, int keyCode) throws IOException {
/* 111 */     if (keyCode == 1) {
/* 112 */       this.mc.displayGuiScreen(this.prevScreen);
/*     */       return;
/*     */     } 
/* 115 */     if (keyCode == 15)
/* 116 */       if (this.realNickField.isFocused()) {
/* 117 */         this.realNickField.setFocused(false);
/* 118 */         this.fakeNickField.setFocused(true);
/* 119 */       } else if (this.fakeNickField.isFocused()) {
/* 120 */         this.fakeNickField.setFocused(false);
/*     */       }  
/* 122 */     if (this.fakeNickField.isFocused())
/* 123 */       this.fakeNickField.textboxKeyTyped(typedChar, keyCode); 
/* 124 */     if (this.realNickField.isFocused())
/* 125 */       this.realNickField.textboxKeyTyped(typedChar, keyCode); 
/*     */   }
/*     */   
/*     */   public void drawScreen(int mouseX, int mouseY, float partialTicks) {
/* 129 */     drawDefaultBackground();
/* 130 */     drawCenteredString(this.fontRendererObj, this.Report, this.width / 2, 50, 16777215);
/* 131 */     drawCenteredString(this.fontRendererObj, "Your Nickname", this.width / 2, this.realNickField.yPosition - 15, 16777215);
/* 132 */     drawCenteredString(this.fontRendererObj, "Fake Nick", this.width / 2, this.fakeNickField.yPosition - 15, 16777215);
/* 133 */     this.fakeNickField.drawTextBox();
/* 134 */     this.realNickField.drawTextBox();
/* 135 */     super.drawScreen(mouseX, mouseY, partialTicks);
/*     */   }
/*     */   
/*     */   public static String fetchUUID(String nome) {
/*     */     try {
/* 140 */       URL link2 = new URL("https://api.mojang.com/users/profiles/minecraft/" + nome);
/* 141 */       HttpURLConnection connected2 = (HttpURLConnection)link2.openConnection();
/* 142 */       connected2.setRequestMethod("GET");
/* 143 */       connected2.setRequestProperty("Accept", "application/json");
/* 144 */       if (connected2.getResponseCode() == 200) {
/* 145 */         BufferedReader read_string = new BufferedReader(new InputStreamReader(connected2.getInputStream()));
/* 146 */         String pageText = read_string.lines().collect(Collectors.joining("\n"));
/* 147 */         JSONObject object = new JSONObject(pageText);
/* 148 */         read_string.close();
/* 149 */         connected2.disconnect();
/* 150 */         String uuid = object.getString("id").toString();
/* 151 */         return uuid;
/*     */       } 
/* 153 */     } catch (Exception e) {
/* 154 */       e.getMessage();
/*     */     } 
/* 156 */     return "UNKNOWN_UUID";
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\rip\diavlo\base\ap\\ui\griefing\GuiUUIDSpoof.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */