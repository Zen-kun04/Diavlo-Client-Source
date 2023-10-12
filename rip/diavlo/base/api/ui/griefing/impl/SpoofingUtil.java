/*    */ package rip.diavlo.base.api.ui.griefing.impl;
/*    */ 
/*    */ import net.minecraft.util.Session;
/*    */ 
/*    */ public class SpoofingUtil
/*    */ {
/*    */   public void setIpBungeeHack(String ipBungeeHack) {
/*  8 */     this.ipBungeeHack = ipBungeeHack; } public void setFakeNick(String fakeNick) { this.fakeNick = fakeNick; } public void setPreUUID(String preUUID) { this.preUUID = preUUID; } public void setBungeeHack(boolean bungeeHack) { this.bungeeHack = bungeeHack; } public void setPremiumUUID(boolean premiumUUID) { this.premiumUUID = premiumUUID; } public void setPremiumSession(boolean premiumSession) { this.premiumSession = premiumSession; } public void setSession(Session session) { this.session = session; } public void setAutoReconnect(boolean autoReconnect) { this.autoReconnect = autoReconnect; }
/*    */   
/*    */   private String fakeNick; private String preUUID; private boolean bungeeHack; private boolean premiumUUID;
/* 11 */   private String ipBungeeHack = "127.0.0.1"; private boolean premiumSession; private Session session; private boolean autoReconnect; public String getIpBungeeHack() { return this.ipBungeeHack; }
/* 12 */   public String getFakeNick() { return this.fakeNick; } public String getPreUUID() { return this.preUUID; }
/* 13 */   public boolean isBungeeHack() { return this.bungeeHack; } public boolean isPremiumUUID() { return this.premiumUUID; } public boolean isPremiumSession() { return this.premiumSession; } public Session getSession() {
/* 14 */     return this.session;
/*    */   } public boolean isAutoReconnect() {
/* 16 */     return this.autoReconnect;
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\rip\diavlo\base\ap\\ui\griefing\impl\SpoofingUtil.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */