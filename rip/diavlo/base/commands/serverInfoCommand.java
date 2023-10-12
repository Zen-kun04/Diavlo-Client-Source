/*     */ package rip.diavlo.base.commands;
/*     */ 
/*     */ import com.google.gson.JsonObject;
/*     */ import com.google.gson.JsonParser;
/*     */ import java.io.IOException;
/*     */ import java.io.Serializable;
/*     */ import java.util.ArrayList;
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
/*     */ import rip.diavlo.base.api.command.Command;
/*     */ import rip.diavlo.base.api.command.CommandExecutionException;
/*     */ import rip.diavlo.base.utils.ChatUtil;
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
/*     */ public class serverInfoCommand
/*     */   implements Command
/*     */ {
/*     */   public String[] getAliases() {
/*  37 */     return new String[] { "serverinfo" };
/*     */   }
/*     */ 
/*     */   
/*     */   public void execute(String[] arguments) throws CommandExecutionException {
/*     */     String serverIp;
/*  43 */     if (mc.isSingleplayer()) {
/*  44 */       ChatUtil.print(".serverinfo solo se puede usar en §c§lMULTIJUGADOR", true);
/*     */       
/*     */       return;
/*     */     } 
/*  48 */     if (arguments.length > 2) {
/*  49 */       ChatUtil.print(".serverinfo " + getUsage(), true);
/*     */       
/*     */       return;
/*     */     } 
/*     */     
/*  54 */     if (arguments.length == 1) {
/*  55 */       serverIp = (mc.getCurrentServerData()).serverIP;
/*     */     } else {
/*     */       
/*  58 */       serverIp = arguments[1];
/*     */     } 
/*     */ 
/*     */     
/*  62 */     ChatUtil.print("§8-----------------------------------------", false);
/*  63 */     ChatUtil.print("§7Obteniendo información de: §c§l" + serverIp + "§7...", true);
/*  64 */     ChatUtil.print("", false);
/*     */ 
/*     */ 
/*     */     
/*  68 */     ExecutorService executor = Executors.newFixedThreadPool(1);
/*     */     
/*  70 */     CompletableFuture.supplyAsync(() -> {
/*     */           try {
/*     */             return obtenerInfoIPAsync(serverIp).get();
/*  73 */           } catch (Exception e) {
/*     */             return new ArrayList();
/*     */           } 
/*  76 */         }executor).thenAccept(data -> {
/*     */           if (data.toString().isEmpty()) {
/*     */             ChatUtil.print("§4§lERROR: §c§lNo se ha podido obtener información..", true);
/*     */             
/*     */             ChatUtil.print("§8-----------------------------------------", false);
/*     */           } else {
/*     */             try {
/*     */               JsonParser jsonParser = new JsonParser();
/*     */               
/*     */               JsonObject jsonObject = jsonParser.parse(data.toString()).getAsJsonObject();
/*     */               
/*     */               String realIp = jsonObject.get("ip").getAsString();
/*     */               
/*     */               int realPort = jsonObject.get("port").getAsInt();
/*     */               
/*     */               int onlinePlayers = jsonObject.getAsJsonObject("players").get("online").getAsInt();
/*     */               
/*     */               int maxPlayers = jsonObject.getAsJsonObject("players").get("max").getAsInt();
/*     */               
/*     */               String version = jsonObject.get("version").getAsString();
/*     */               
/*     */               ChatUtil.print("", false);
/*     */               
/*     */               ChatUtil.print("§f- §c§lIP: §f" + realIp + " §cPuerto: §f" + realPort, false);
/*     */               
/*     */               ChatUtil.print("", false);
/*     */               
/*     */               ChatUtil.print("§f- §c§lJugadores Online: §a§l" + onlinePlayers + "§f/§c" + maxPlayers, false);
/*     */               ChatUtil.print("", false);
/*     */               if (!version.isEmpty()) {
/*     */                 ChatUtil.print("§f- §c§lVersion: §f" + version, false);
/*     */                 ChatUtil.print("", false);
/*     */               } 
/*     */               if (jsonObject.has("protocol")) {
/*     */                 int protocolNumber = jsonObject.getAsJsonObject("protocol").get("version").getAsInt();
/*     */                 if (protocolNumber != -1) {
/*     */                   String protocolName = jsonObject.getAsJsonObject("protocol").get("name").getAsString();
/*     */                   ChatUtil.print("§f- §c§lProtocolo: §f" + protocolName + " §c(§f" + protocolNumber + "§c)", false);
/*     */                   ChatUtil.print("", false);
/*     */                 } 
/*     */               } 
/*     */               ChatUtil.print("§8-----------------------------------------", false);
/* 118 */             } catch (Exception e) {
/*     */               ChatUtil.print("§4§lERROR: §c§lNo se ha podido obtener información..", true);
/*     */               ChatUtil.print("§8-----------------------------------------", false);
/*     */               throw new RuntimeException(e);
/*     */             } 
/*     */           } 
/*     */         });
/* 125 */     executor.shutdown();
/*     */   }
/*     */   
/*     */   private CompletableFuture<String> obtenerInfoIPAsync(String ip) {
/* 129 */     return CompletableFuture.supplyAsync(() -> {
/*     */           try {
/*     */             CloseableHttpClient closeableHttpClient = HttpClients.createDefault();
/*     */             
/*     */             HttpGet get = new HttpGet("https://api.mcsrvstat.us/3/" + ip);
/*     */             get.setHeader("Accept", "application/json");
/*     */             get.setHeader("Content-type", "application/json");
/*     */             HttpResponse response = closeableHttpClient.execute((HttpUriRequest)get);
/*     */             int statusCode = response.getStatusLine().getStatusCode();
/*     */             if (statusCode == 200) {
/*     */               HttpEntity responseEntity = response.getEntity();
/*     */               return EntityUtils.toString(responseEntity);
/*     */             } 
/*     */             throw new IOException("Request failed with status code: " + statusCode);
/* 143 */           } catch (Exception e) {
/*     */             throw new RuntimeException(e);
/*     */           } 
/*     */         });
/*     */   }
/*     */ 
/*     */   
/*     */   public String getUsage() {
/* 151 */     return " [ip] (Opcional) || Muestra información sobre el server actual o de una ip.";
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\rip\diavlo\base\commands\serverInfoCommand.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */