/*    */ package rip.diavlo.base.api.ui.alt;
/*    */ 
/*    */ import com.mojang.authlib.Agent;
/*    */ import com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService;
/*    */ import com.mojang.authlib.yggdrasil.YggdrasilUserAuthentication;
/*    */ import java.net.Proxy;
/*    */ import net.minecraft.client.Minecraft;
/*    */ import net.minecraft.util.EnumChatFormatting;
/*    */ import net.minecraft.util.Session;
/*    */ 
/*    */ public class AltLoginThread
/*    */   extends Thread
/*    */ {
/*    */   private final String password;
/*    */   private String status;
/*    */   private final String username;
/* 17 */   private final Minecraft mc = Minecraft.getMinecraft();
/*    */   
/*    */   public AltLoginThread(String username, String password) {
/* 20 */     super("Alt Login Thread");
/* 21 */     this.username = username;
/* 22 */     this.password = password;
/* 23 */     this.status = EnumChatFormatting.GRAY + "Waiting";
/*    */   }
/*    */   
/*    */   private Session createSession(String username, String password) {
/* 27 */     YggdrasilAuthenticationService service = new YggdrasilAuthenticationService(Proxy.NO_PROXY, "");
/* 28 */     YggdrasilUserAuthentication auth = (YggdrasilUserAuthentication)service.createUserAuthentication(Agent.MINECRAFT);
/* 29 */     auth.setUsername(username);
/* 30 */     auth.setPassword(password);
/*    */     try {
/* 32 */       auth.logIn();
/* 33 */       return new Session(auth.getSelectedProfile().getName(), auth.getSelectedProfile().getId().toString(), auth.getAuthenticatedToken(), "mojang");
/* 34 */     } catch (Exception exception) {
/* 35 */       exception.printStackTrace();
/*    */       
/* 37 */       return null;
/*    */     } 
/*    */   }
/*    */   public String getStatus() {
/* 41 */     return this.status;
/*    */   }
/*    */ 
/*    */   
/*    */   public void run() {
/* 46 */     if (this.password.equals("")) {
/* 47 */       this.mc.session = new Session(this.username.replace("&", "§"), "", "", "mojang");
/* 48 */       this.status = EnumChatFormatting.GREEN + "Set username to " + this.username;
/*    */       return;
/*    */     } 
/* 51 */     this.status = EnumChatFormatting.AQUA + "Authenticating...";
/* 52 */     Session auth = createSession(this.username, this.password);
/* 53 */     if (auth == null) {
/* 54 */       this.status = EnumChatFormatting.RED + "Failed";
/*    */     } else {
/* 56 */       this.status = EnumChatFormatting.GREEN + "Logged into " + auth.getUsername();
/* 57 */       this.mc.session = auth;
/*    */     } 
/*    */   }
/*    */   
/*    */   public void setStatus(String status) {
/* 62 */     this.status = status;
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\rip\diavlo\base\ap\\ui\alt\AltLoginThread.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */