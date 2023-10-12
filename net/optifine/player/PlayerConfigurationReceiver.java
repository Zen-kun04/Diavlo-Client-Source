/*    */ package net.optifine.player;
/*    */ 
/*    */ import com.google.gson.JsonElement;
/*    */ import com.google.gson.JsonParser;
/*    */ import net.minecraft.src.Config;
/*    */ import net.optifine.http.IFileDownloadListener;
/*    */ 
/*    */ public class PlayerConfigurationReceiver
/*    */   implements IFileDownloadListener {
/* 10 */   private String player = null;
/*    */ 
/*    */   
/*    */   public PlayerConfigurationReceiver(String player) {
/* 14 */     this.player = player;
/*    */   }
/*    */ 
/*    */   
/*    */   public void fileDownloadFinished(String url, byte[] bytes, Throwable exception) {
/* 19 */     if (bytes != null)
/*    */       
/*    */       try {
/*    */         
/* 23 */         String s = new String(bytes, "ASCII");
/* 24 */         JsonParser jsonparser = new JsonParser();
/* 25 */         JsonElement jsonelement = jsonparser.parse(s);
/* 26 */         PlayerConfigurationParser playerconfigurationparser = new PlayerConfigurationParser(this.player);
/* 27 */         PlayerConfiguration playerconfiguration = playerconfigurationparser.parsePlayerConfiguration(jsonelement);
/*    */         
/* 29 */         if (playerconfiguration != null)
/*    */         {
/* 31 */           playerconfiguration.setInitialized(true);
/* 32 */           PlayerConfigurations.setPlayerConfiguration(this.player, playerconfiguration);
/*    */         }
/*    */       
/* 35 */       } catch (Exception exception1) {
/*    */         
/* 37 */         Config.dbg("Error parsing configuration: " + url + ", " + exception1.getClass().getName() + ": " + exception1.getMessage());
/*    */       }  
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\optifine\player\PlayerConfigurationReceiver.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */