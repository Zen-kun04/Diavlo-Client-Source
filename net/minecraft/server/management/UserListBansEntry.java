/*    */ package net.minecraft.server.management;
/*    */ 
/*    */ import com.google.gson.JsonObject;
/*    */ import com.mojang.authlib.GameProfile;
/*    */ import java.util.Date;
/*    */ import java.util.UUID;
/*    */ 
/*    */ public class UserListBansEntry
/*    */   extends BanEntry<GameProfile>
/*    */ {
/*    */   public UserListBansEntry(GameProfile profile) {
/* 12 */     this(profile, (Date)null, (String)null, (Date)null, (String)null);
/*    */   }
/*    */ 
/*    */   
/*    */   public UserListBansEntry(GameProfile profile, Date startDate, String banner, Date endDate, String banReason) {
/* 17 */     super(profile, endDate, banner, endDate, banReason);
/*    */   }
/*    */ 
/*    */   
/*    */   public UserListBansEntry(JsonObject json) {
/* 22 */     super(toGameProfile(json), json);
/*    */   }
/*    */ 
/*    */   
/*    */   protected void onSerialization(JsonObject data) {
/* 27 */     if (getValue() != null) {
/*    */       
/* 29 */       data.addProperty("uuid", (getValue().getId() == null) ? "" : getValue().getId().toString());
/* 30 */       data.addProperty("name", getValue().getName());
/* 31 */       super.onSerialization(data);
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   private static GameProfile toGameProfile(JsonObject json) {
/* 37 */     if (json.has("uuid") && json.has("name")) {
/*    */       UUID uuid;
/* 39 */       String s = json.get("uuid").getAsString();
/*    */ 
/*    */ 
/*    */       
/*    */       try {
/* 44 */         uuid = UUID.fromString(s);
/*    */       }
/* 46 */       catch (Throwable var4) {
/*    */         
/* 48 */         return null;
/*    */       } 
/*    */       
/* 51 */       return new GameProfile(uuid, json.get("name").getAsString());
/*    */     } 
/*    */ 
/*    */     
/* 55 */     return null;
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\server\management\UserListBansEntry.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */