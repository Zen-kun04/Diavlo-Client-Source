/*     */ package rip.diavlo.base.commands;
/*     */ 
/*     */ import com.google.gson.JsonObject;
/*     */ import com.google.gson.JsonParser;
/*     */ import es.diavlo.api.data.UserData;
/*     */ import java.io.IOException;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import java.util.concurrent.CompletableFuture;
/*     */ import java.util.concurrent.ExecutorService;
/*     */ import java.util.concurrent.Executors;
/*     */ import org.apache.http.HttpEntity;
/*     */ import org.apache.http.HttpResponse;
/*     */ import org.apache.http.client.methods.HttpGet;
/*     */ import org.apache.http.client.methods.HttpUriRequest;
/*     */ import org.apache.http.impl.client.CloseableHttpClient;
/*     */ import org.apache.http.impl.client.HttpClients;
/*     */ import org.apache.http.util.EntityUtils;
/*     */ import rip.diavlo.base.Client;
/*     */ import rip.diavlo.base.api.command.CommandExecutionException;
/*     */ import rip.diavlo.base.api.command.CommandVIP;
/*     */ import rip.diavlo.base.utils.ChatUtil;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class InfoIPCommand
/*     */   implements CommandVIP
/*     */ {
/*     */   private UserData diavloUser;
/*  32 */   private static final Map<String, String> COUNTRYCODETOCOUNTRY = new HashMap<String, String>()
/*     */     {
/*     */     
/*     */     };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String[] getAliases() {
/*  67 */     return new String[] { "infoip" };
/*     */   }
/*     */ 
/*     */   
/*     */   public void execute(String[] arguments) throws CommandExecutionException {
/*  72 */     this.diavloUser = UserData.getInstance();
/*     */ 
/*     */     
/*  75 */     if (this.diavloUser != null) {
/*  76 */       if (arguments.length != 2) {
/*  77 */         ChatUtil.print(".infoip <ip>", true);
/*     */         
/*     */         return;
/*     */       } 
/*  81 */       String ip = arguments[1];
/*     */ 
/*     */       
/*  84 */       ChatUtil.print("§8-----------------------------------------", false);
/*  85 */       ChatUtil.print("§7Obteniendo información de: §c§l" + ip + "§7...", true);
/*  86 */       ChatUtil.print("", false);
/*     */ 
/*     */ 
/*     */       
/*  90 */       ExecutorService executor = Executors.newFixedThreadPool(1);
/*     */       
/*  92 */       CompletableFuture.supplyAsync(() -> {
/*     */             try {
/*     */               return obtenerInfoIPAsync(ip, this.diavloUser.getToken()).get();
/*  95 */             } catch (Exception e) {
/*     */               return "";
/*     */             } 
/*  98 */           }executor).thenAccept(data -> {
/*     */             if (data.isEmpty()) {
/*     */               ChatUtil.print("§4§lERROR: §c§lIP Inválida.", true);
/*     */               
/*     */               ChatUtil.print("§8-----------------------------------------", false);
/*     */             } else {
/*     */               try {
/*     */                 JsonParser jsonParser = new JsonParser();
/*     */                 
/*     */                 JsonObject jsonObject = jsonParser.parse(data).getAsJsonObject();
/*     */                 
/*     */                 String city = jsonObject.get("city").getAsString();
/*     */                 
/*     */                 String region = jsonObject.get("region").getAsString();
/*     */                 
/*     */                 String country = jsonObject.get("country").getAsString();
/*     */                 
/*     */                 String location = jsonObject.get("loc").getAsString();
/*     */                 
/*     */                 String org = jsonObject.get("org").getAsString();
/*     */                 
/*     */                 String postalcode = jsonObject.get("postal").getAsString();
/*     */                 
/*     */                 String hostname = "";
/*     */                 
/*     */                 if (jsonObject.has("hostname")) {
/*     */                   hostname = jsonObject.get("hostname").getAsString();
/*     */                   
/*     */                   ChatUtil.print("§f- §c§lHostname§f: " + hostname + " §c[ §f" + org + "§c ]", false);
/*     */                   
/*     */                   ChatUtil.print("", false);
/*     */                 } 
/*     */                 ChatUtil.print("§f- §c§lCiudad§f: " + city + " §c[ §f" + region + " §c]", false);
/*     */                 ChatUtil.print("", false);
/*     */                 ChatUtil.print("§f- §c§lUbicacion§f: §c§l[ §f" + location + "§c§l ] §c§lCodigo Postal: [ §f" + postalcode + " §c]", false);
/*     */                 ChatUtil.print("", false);
/*     */                 ChatUtil.print("§f- §c§lPais§f: " + (String)COUNTRYCODETOCOUNTRY.getOrDefault(country, country), false);
/*     */                 ChatUtil.print("§8-----------------------------------------", false);
/* 136 */               } catch (Exception e) {
/*     */                 throw new RuntimeException(e);
/*     */               } 
/*     */             } 
/*     */           });
/*     */ 
/*     */ 
/*     */       
/* 144 */       executor.shutdown();
/*     */     } else {
/* 146 */       ChatUtil.print("§4§lERROR: §c§lTienes que logearte en Diavlo para realizar este comando.", true);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private CompletableFuture<String> obtenerInfoIPAsync(String ip, String diavloToken) {
/* 152 */     return CompletableFuture.supplyAsync(() -> {
/*     */           try {
/*     */             CloseableHttpClient closeableHttpClient = HttpClients.createDefault();
/*     */             
/*     */             String diavloDomain = Client.getInstance().getDiavloDomain();
/*     */             
/*     */             HttpGet get = new HttpGet("http://" + diavloDomain + "/api/v3/mcclient/infoip/" + ip);
/*     */             
/*     */             get.setHeader("Accept", "application/json");
/*     */             
/*     */             get.setHeader("Content-type", "application/json");
/*     */             
/*     */             get.setHeader("Authorization", "Bearer " + diavloToken);
/*     */             
/*     */             HttpResponse response = closeableHttpClient.execute((HttpUriRequest)get);
/*     */             
/*     */             int statusCode = response.getStatusLine().getStatusCode();
/*     */             
/*     */             if (statusCode == 200) {
/*     */               HttpEntity responseEntity = response.getEntity();
/*     */               
/*     */               return EntityUtils.toString(responseEntity);
/*     */             } 
/*     */             
/*     */             throw new IOException("Request failed with status code: " + statusCode);
/* 177 */           } catch (Exception e) {
/*     */             throw new RuntimeException(e);
/*     */           } 
/*     */         });
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String getUsage() {
/* 186 */     return "<ip> || Muestra información sobre la direccion IP.";
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\rip\diavlo\base\commands\InfoIPCommand.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */