/*    */ package rip.diavlo.base.rpc;
/*    */ 
/*    */ import es.diavlo.api.data.UserData;
/*    */ import java.util.Date;
/*    */ import net.arikia.dev.drpc.DiscordEventHandlers;
/*    */ import net.arikia.dev.drpc.DiscordRPC;
/*    */ import net.arikia.dev.drpc.DiscordRichPresence;
/*    */ import net.arikia.dev.drpc.DiscordUser;
/*    */ import net.minecraft.client.Minecraft;
/*    */ 
/*    */ 
/*    */ public class RPC
/*    */ {
/*    */   private DiscordEventHandlers handlers;
/*    */   private final Minecraft mc;
/*    */   private UserData diavlo;
/*    */   private DiscordRichPresence presence;
/*    */   
/*    */   public RPC(Minecraft mc) {
/* 20 */     this.mc = mc;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void startRPC() {
/* 27 */     DiscordEventHandlers handlers = (new DiscordEventHandlers.Builder()).setReadyEventHandler(user -> System.out.println("DiavloClient's RPC is ready!")).build();
/* 28 */     DiscordRPC.discordInitialize("1149683740994457631", handlers, true);
/* 29 */     this.presence = (new DiscordRichPresence.Builder("Status: Free 💤")).setDetails("The Best Griefing Client").setBigImage("logotipo", "Made with 🧡 (.gg/programadores)").setSmallImage("minecraft-cube", "Diavlo Client Version: 1.0").setStartTimestamps((new Date()).getTime()).build();
/* 30 */     DiscordRPC.discordUpdatePresence(this.presence);
/*    */     
/* 32 */     (new Thread(() -> {
/*    */           while (true) {
/*    */             updateRPC();
/*    */             try {
/*    */               Thread.sleep(5000L);
/* 37 */             } catch (InterruptedException e) {
/*    */               throw new RuntimeException(e);
/*    */             } 
/*    */           } 
/* 41 */         })).start();
/*    */   }
/*    */ 
/*    */   
/*    */   public void updateRPC() {
/* 46 */     this.diavlo = UserData.getInstance();
/*    */     
/* 48 */     this.presence.state = "Status: " + ((this.diavlo == null) ? "Free 💤" : ((this.diavlo.getRank().equalsIgnoreCase("owner") ? "OWNER 👑" : "VIP 💎") + " [ " + this.diavlo.getNickname() + " ] "));
/* 49 */     DiscordRPC.discordUpdatePresence(this.presence);
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\rip\diavlo\base\rpc\RPC.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */