/*    */ package rip.diavlo.base.utils.host;public class ServerData {
/*    */   public String ip;
/*    */   public int max;
/*    */   public int online;
/*    */   
/*    */   public ServerData(String ip, int max, int online, String description, String version_name, String version_protocol) {
/*  7 */     this.ip = ip; this.max = max; this.online = online; this.description = description; this.version_name = version_name; this.version_protocol = version_protocol;
/*    */   } public String description; public String version_name; public String version_protocol; public void setIp(String ip) {
/*  9 */     this.ip = ip; } public void setMax(int max) { this.max = max; } public void setOnline(int online) { this.online = online; } public void setDescription(String description) { this.description = description; } public void setVersion_name(String version_name) { this.version_name = version_name; } public void setVersion_protocol(String version_protocol) { this.version_protocol = version_protocol; }
/*    */ 
/*    */   
/* 12 */   public String getIp() { return this.ip; }
/* 13 */   public int getMax() { return this.max; } public int getOnline() { return this.online; }
/* 14 */   public String getDescription() { return this.description; } public String getVersion_name() { return this.version_name; } public String getVersion_protocol() { return this.version_protocol; }
/*    */ 
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\rip\diavlo\bas\\utils\host\ServerData.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */