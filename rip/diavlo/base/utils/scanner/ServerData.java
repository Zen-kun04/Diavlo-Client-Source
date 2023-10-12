/*    */ package rip.diavlo.base.utils.scanner;
/*    */ 
/*    */ public class ServerData
/*    */ {
/*    */   public String motd;
/*    */   public Integer currentOnline;
/*    */   public Integer maxOnline;
/*    */   public String version;
/*    */   
/*    */   public ServerData(String[] rawData) {
/* 11 */     this.motd = rawData[0].trim().replace("\\n", "");
/* 12 */     this.currentOnline = Integer.valueOf(Integer.parseInt(rawData[2]));
/* 13 */     this.maxOnline = Integer.valueOf(Integer.parseInt(rawData[1]));
/* 14 */     this.version = rawData[3];
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 19 */     return this.motd + " | Jugadores: §a§l" + this.currentOnline + "§f/" + this.maxOnline + " | Version: " + this.version;
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\rip\diavlo\bas\\utils\scanner\ServerData.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */