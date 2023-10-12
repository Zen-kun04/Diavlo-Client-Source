/*    */ package rip.diavlo.base.utils.host;
/*    */ 
/*    */ import com.google.gson.JsonObject;
/*    */ import com.google.gson.JsonParser;
/*    */ import java.io.BufferedReader;
/*    */ import java.io.InputStreamReader;
/*    */ import java.net.URL;
/*    */ import java.util.List;
/*    */ import java.util.stream.Collector;
/*    */ import java.util.stream.Collectors;
/*    */ 
/*    */ public class GeoUtil
/*    */ {
/*    */   public static GeoUtil getInstance() {
/* 15 */     return instance;
/*    */   }
/*    */   private static GeoUtil instance;
/*    */   public JsonObject object;
/*    */   public String server;
/*    */   
/*    */   public GeoUtil(String ip) {
/* 22 */     instance = this;
/* 23 */     this.server = "http://ip-api.com/json/" + ip + "?fields=status,message,continent,continentCode,country,countryCode,region,regionName,city,district,zip,lat,lon,timezone,currency,isp,org,as,asname,reverse,mobile,proxy,query";
/*    */     
/* 25 */     this.object = (new JsonParser()).parse(websitedata(this.server)).getAsJsonObject();
/*    */   }
/*    */   
/*    */   public String getAS() {
/* 29 */     return getObjectString("as");
/*    */   }
/*    */   
/*    */   public String getCITY() {
/* 33 */     return getObjectString("city");
/*    */   }
/*    */   
/*    */   public String getCountry() {
/* 37 */     return getObjectString("country");
/*    */   }
/*    */   
/*    */   public String getCountryCode() {
/* 41 */     return getObjectString("countryCode");
/*    */   }
/*    */   
/*    */   public String getISP() {
/* 45 */     return getObjectString("isp");
/*    */   }
/*    */   
/*    */   public String getOrganization() {
/* 49 */     return getObjectString("org");
/*    */   }
/*    */   
/*    */   public String getRegion() {
/* 53 */     return getObjectString("regionName");
/*    */   }
/*    */   
/*    */   public String getTimezone() {
/* 57 */     return getObjectString("timezone");
/*    */   }
/*    */   
/*    */   public String getProxy() {
/* 61 */     return getObjectString("proxy");
/*    */   }
/*    */   
/*    */   public boolean isSuccess() {
/* 65 */     return getObjectString("status").equals("success");
/*    */   }
/*    */   
/*    */   public String getObjectString(String obj) {
/*    */     try {
/* 70 */       return this.object.get(obj).getAsString();
/* 71 */     } catch (Exception e) {
/* 72 */       return "Pinging...";
/*    */     } 
/*    */   }
/*    */   
/*    */   public String websitedata(String website) {
/*    */     try {
/* 78 */       URL url = new URL(website);
/* 79 */       BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(url.openStream()));
/* 80 */       String content = format(bufferedReader.lines().collect((Collector)Collectors.toList()));
/* 81 */       bufferedReader.close();
/* 82 */       return content;
/* 83 */     } catch (Exception e) {
/* 84 */       return null;
/*    */     } 
/*    */   }
/*    */   
/*    */   private String format(List<String> arrayList) {
/* 89 */     String out = "";
/* 90 */     for (String entry : arrayList)
/* 91 */       out = String.valueOf(out) + entry + "\n"; 
/* 92 */     return out;
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\rip\diavlo\bas\\utils\host\GeoUtil.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */