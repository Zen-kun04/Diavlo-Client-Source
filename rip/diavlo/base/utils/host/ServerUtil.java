/*    */ package rip.diavlo.base.utils.host;
/*    */ 
/*    */ import com.mcping.MinecraftPing;
/*    */ import com.mcping.MinecraftPingOptions;
/*    */ import java.util.ArrayList;
/*    */ 
/*    */ 
/*    */ public class ServerUtil
/*    */ {
/*    */   public static ArrayList<String> pingServer(String ip) {
/* 11 */     ArrayList<String> server = new ArrayList<>();
/* 12 */     MinecraftPing ping = new MinecraftPing();
/*    */     try {
/* 14 */       String[] pingserver = ping.getPingString((new MinecraftPingOptions()).setHostname(ip.split(":")[0]).setPort(Integer.parseInt(ip.split(":")[1])).setTimeout(5000));
/*    */       
/* 16 */       if (pingserver != null) {
/* 17 */         ServerData serverData = new ServerData(ip, Integer.parseInt(pingserver[1]), Integer.parseInt(pingserver[2]), pingserver[0], pingserver[3], pingserver[4]);
/* 18 */         server.add(serverData.getIp());
/* 19 */         server.add(serverData.getDescription());
/* 20 */         server.add(serverData.getOnline() + "/" + serverData.getMax());
/* 21 */         server.add(serverData.getVersion_name());
/* 22 */         server.add(serverData.getVersion_protocol());
/*    */       } 
/* 24 */     } catch (Exception exception) {}
/* 25 */     return server;
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\rip\diavlo\bas\\utils\host\ServerUtil.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */