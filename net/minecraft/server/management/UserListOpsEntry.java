/*    */ package net.minecraft.server.management;
/*    */ 
/*    */ import com.google.gson.JsonObject;
/*    */ import com.mojang.authlib.GameProfile;
/*    */ import java.util.UUID;
/*    */ 
/*    */ public class UserListOpsEntry
/*    */   extends UserListEntry<GameProfile>
/*    */ {
/*    */   private final int permissionLevel;
/*    */   private final boolean bypassesPlayerLimit;
/*    */   
/*    */   public UserListOpsEntry(GameProfile player, int permissionLevelIn, boolean bypassesPlayerLimitIn) {
/* 14 */     super(player);
/* 15 */     this.permissionLevel = permissionLevelIn;
/* 16 */     this.bypassesPlayerLimit = bypassesPlayerLimitIn;
/*    */   }
/*    */ 
/*    */   
/*    */   public UserListOpsEntry(JsonObject p_i1150_1_) {
/* 21 */     super(constructProfile(p_i1150_1_), p_i1150_1_);
/* 22 */     this.permissionLevel = p_i1150_1_.has("level") ? p_i1150_1_.get("level").getAsInt() : 0;
/* 23 */     this.bypassesPlayerLimit = (p_i1150_1_.has("bypassesPlayerLimit") && p_i1150_1_.get("bypassesPlayerLimit").getAsBoolean());
/*    */   }
/*    */ 
/*    */   
/*    */   public int getPermissionLevel() {
/* 28 */     return this.permissionLevel;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean bypassesPlayerLimit() {
/* 33 */     return this.bypassesPlayerLimit;
/*    */   }
/*    */ 
/*    */   
/*    */   protected void onSerialization(JsonObject data) {
/* 38 */     if (getValue() != null) {
/*    */       
/* 40 */       data.addProperty("uuid", (getValue().getId() == null) ? "" : getValue().getId().toString());
/* 41 */       data.addProperty("name", getValue().getName());
/* 42 */       super.onSerialization(data);
/* 43 */       data.addProperty("level", Integer.valueOf(this.permissionLevel));
/* 44 */       data.addProperty("bypassesPlayerLimit", Boolean.valueOf(this.bypassesPlayerLimit));
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   private static GameProfile constructProfile(JsonObject p_152643_0_) {
/* 50 */     if (p_152643_0_.has("uuid") && p_152643_0_.has("name")) {
/*    */       UUID uuid;
/* 52 */       String s = p_152643_0_.get("uuid").getAsString();
/*    */ 
/*    */ 
/*    */       
/*    */       try {
/* 57 */         uuid = UUID.fromString(s);
/*    */       }
/* 59 */       catch (Throwable var4) {
/*    */         
/* 61 */         return null;
/*    */       } 
/*    */       
/* 64 */       return new GameProfile(uuid, p_152643_0_.get("name").getAsString());
/*    */     } 
/*    */ 
/*    */     
/* 68 */     return null;
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\server\management\UserListOpsEntry.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */