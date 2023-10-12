/*    */ package net.minecraft.server.management;
/*    */ 
/*    */ import com.google.gson.JsonObject;
/*    */ import java.util.Date;
/*    */ 
/*    */ public class IPBanEntry
/*    */   extends BanEntry<String>
/*    */ {
/*    */   public IPBanEntry(String valueIn) {
/* 10 */     this(valueIn, (Date)null, (String)null, (Date)null, (String)null);
/*    */   }
/*    */ 
/*    */   
/*    */   public IPBanEntry(String valueIn, Date startDate, String banner, Date endDate, String banReason) {
/* 15 */     super(valueIn, startDate, banner, endDate, banReason);
/*    */   }
/*    */ 
/*    */   
/*    */   public IPBanEntry(JsonObject json) {
/* 20 */     super(getIPFromJson(json), json);
/*    */   }
/*    */ 
/*    */   
/*    */   private static String getIPFromJson(JsonObject json) {
/* 25 */     return json.has("ip") ? json.get("ip").getAsString() : null;
/*    */   }
/*    */ 
/*    */   
/*    */   protected void onSerialization(JsonObject data) {
/* 30 */     if (getValue() != null) {
/*    */       
/* 32 */       data.addProperty("ip", getValue());
/* 33 */       super.onSerialization(data);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\server\management\IPBanEntry.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */