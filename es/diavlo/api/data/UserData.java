/*     */ package es.diavlo.api.data;
/*     */ 
/*     */ import com.google.gson.JsonObject;
/*     */ import com.google.gson.JsonParser;
/*     */ import es.diavlo.api.screens.InvalidDiavloLoginCredentialsException;
/*     */ import org.apache.http.HttpEntity;
/*     */ import org.apache.http.HttpResponse;
/*     */ import org.apache.http.client.methods.HttpPost;
/*     */ import org.apache.http.client.methods.HttpUriRequest;
/*     */ import org.apache.http.entity.StringEntity;
/*     */ import org.apache.http.impl.client.CloseableHttpClient;
/*     */ import org.apache.http.impl.client.HttpClients;
/*     */ import org.apache.http.util.EntityUtils;
/*     */ import rip.diavlo.base.Client;
/*     */ import rip.diavlo.base.licenze.AntiSKidders;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class UserData
/*     */ {
/*     */   public void setNickname(String nickname) {
/*  27 */     this.nickname = nickname; } public void setPassword(String password) { this.password = password; } public void setRank(String rank) { this.rank = rank; } public void setToken(String token) { this.token = token; } public void setExpiration(String expiration) { this.expiration = expiration; }
/*     */   
/*     */   private String nickname; private String password; private String rank;
/*  30 */   private static UserData instance = null; private String token; private String expiration;
/*     */   public String getNickname() {
/*  32 */     return this.nickname; } public String getPassword() { return this.password; } public String getRank() { return this.rank; } public String getToken() { return this.token; } public String getExpiration() { return this.expiration; }
/*     */   
/*     */   private UserData(String nickname, String password, String token, String rank, String expiration) {
/*  35 */     this.nickname = nickname;
/*  36 */     this.password = password;
/*  37 */     this.token = token;
/*  38 */     this.rank = rank;
/*  39 */     this.expiration = expiration;
/*     */   }
/*     */   
/*     */   public static UserData getInstance() {
/*  43 */     return instance;
/*     */   }
/*     */   
/*     */   public static UserData getInstance(String nickname, String password) throws InvalidDiavloLoginCredentialsException {
/*  47 */     if (instance == null) {
/*  48 */       instance = createInstance(nickname, password);
/*     */     }
/*  50 */     return instance;
/*     */   }
/*     */   
/*     */   private static UserData createInstance(String nickname, String password) {
/*  54 */     String userData = getUserData(nickname, password);
/*     */     
/*  56 */     if (!userData.equals("invalid")) {
/*  57 */       JsonParser jsonParser = new JsonParser();
/*  58 */       JsonObject jsonObject = jsonParser.parse(userData).getAsJsonObject();
/*     */       
/*  60 */       String tkn = jsonObject.get("access_token").getAsString();
/*  61 */       String rk = jsonObject.get("rank").getAsString();
/*  62 */       String exp = jsonObject.get("rank_expiration").getAsString();
/*  63 */       return new UserData(nickname, password, tkn, rk, exp);
/*     */     } 
/*     */     
/*  66 */     throw new InvalidDiavloLoginCredentialsException("Credenciales invalidas");
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static String getUserData(String nickname, String password) {
/*  72 */     String hwid = AntiSKidders.getLicenzeString();
/*  73 */     String jsonData = "{\"username\": \"" + nickname + "\", \"password\": \"" + password + "\",\"hwid\": \"" + hwid + "\" }";
/*     */     try {
/*  75 */       String diavloDomain = Client.getInstance().getDiavloDomain();
/*     */       
/*  77 */       CloseableHttpClient closeableHttpClient = HttpClients.createDefault();
/*  78 */       HttpPost httpPost = new HttpPost("http://" + diavloDomain + "/api/v3/mcclient/login");
/*  79 */       StringEntity entity = new StringEntity(jsonData);
/*  80 */       httpPost.setEntity((HttpEntity)entity);
/*  81 */       httpPost.setHeader("Accept", "application/json");
/*  82 */       httpPost.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/117.0.0.0 Safari/537.36");
/*  83 */       httpPost.setHeader("Content-type", "application/json");
/*  84 */       HttpResponse response = closeableHttpClient.execute((HttpUriRequest)httpPost);
/*  85 */       if (response.getStatusLine().getStatusCode() == 200) {
/*  86 */         HttpEntity responseEntity = response.getEntity();
/*  87 */         return EntityUtils.toString(responseEntity);
/*     */       } 
/*  89 */       System.err.println("Request failed with status code: " + response.getStatusLine().getStatusCode());
/*  90 */       return "invalid";
/*     */     }
/*  92 */     catch (Exception e) {
/*  93 */       e.printStackTrace();
/*  94 */       return "";
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 100 */     return "UserData{nickname='" + this.nickname + '\'' + ", password='" + this.password + '\'' + ", rank='" + this.rank + '\'' + ", token='" + this.token + '\'' + '}';
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\es\diavlo\api\data\UserData.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */