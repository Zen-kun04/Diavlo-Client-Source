/*     */ package rip.diavlo.base.commands;
/*     */ import com.google.gson.Gson;
/*     */ import com.google.gson.JsonArray;
/*     */ import com.google.gson.JsonElement;
/*     */ import com.google.gson.JsonObject;
/*     */ import com.google.gson.JsonParser;
/*     */ import com.google.gson.reflect.TypeToken;
/*     */ import es.diavlo.api.data.UserData;
/*     */ import java.io.IOException;
/*     */ import java.util.Arrays;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.TreeSet;
/*     */ import java.util.concurrent.CompletableFuture;
/*     */ import java.util.concurrent.CompletionException;
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
/*     */ public final class GetIPCommand implements CommandVIP {
/*     */   public String[] getAliases() {
/*  32 */     return new String[] { "getips" };
/*     */   }
/*     */   private UserData diavloUser;
/*     */   private void showResults(Set<String> ips) {
/*  36 */     if (ips == null || ips.size() == 0) {
/*  37 */       ChatUtil.print("§4§lERROR: §c§lNo se han encontrado IPs de este usuario.", true);
/*     */     } else {
/*  39 */       ChatUtil.print("§2§l¡§6§l" + ips.size() + " §2§lIPs obtenidas!", true);
/*  40 */       ChatUtil.print("", false);
/*  41 */       for (String p : ips) {
/*  42 */         ChatUtil.print("            §6§l- §a§l" + p, false);
/*  43 */         ChatUtil.print("", false);
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   public void execute(String[] arguments) throws CommandExecutionException {
/*  49 */     this.diavloUser = UserData.getInstance();
/*  50 */     if (arguments.length < 2) {
/*  51 */       ChatUtil.print(".getips <usuario>", true);
/*     */     } else {
/*  53 */       String user = String.join(" ", Arrays.<CharSequence>copyOfRange((CharSequence[])arguments, 1, arguments.length)).toLowerCase();
/*  54 */       if (this.diavloUser != null) {
/*  55 */         ChatUtil.print("§7Obteniendo Direcciones IP del usuario: §c§l" + user + "§7...", true);
/*  56 */         Map<String, Set<String>> ipCache = Client.getInstance().getIpCache();
/*  57 */         Set<String> ipsFound = ipCache.getOrDefault(user, null);
/*  58 */         if (ipsFound == null) {
/*  59 */           ExecutorService executor = Executors.newFixedThreadPool(1);
/*  60 */           CompletableFuture.supplyAsync(() -> {
/*     */                 try {
/*     */                   return obtenerDireccionesAsync(user, this.diavloUser.getToken()).get();
/*  63 */                 } catch (Exception e) {
/*     */                   return new TreeSet();
/*     */                 } 
/*  66 */               }executor).thenAccept(ips -> {
/*     */                 showResults(ips);
/*     */                 Client.getInstance().addIPstoCache(user, ips);
/*     */               });
/*  70 */           executor.shutdown();
/*     */         } else {
/*  72 */           showResults(ipsFound);
/*     */         } 
/*     */       } else {
/*  75 */         ChatUtil.print("§4§lERROR: §c§lTienes que logearte en Diavlo para realizar este comando.", true);
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private CompletableFuture<Set<String>> obtenerDireccionesAsync(String username, String diavloToken) {
/*  81 */     return CompletableFuture.supplyAsync(() -> {
/*     */           try {
/*     */             String diavloDomain = Client.getInstance().getDiavloDomain();
/*     */             
/*     */             CloseableHttpClient closeableHttpClient = HttpClients.createDefault();
/*     */             HttpGet get = new HttpGet("http://" + diavloDomain + "/api/v3/mcclient/ips/" + username);
/*     */             get.setHeader("Accept", "application/json");
/*     */             get.setHeader("Content-type", "application/json");
/*     */             get.setHeader("Authorization", "Bearer " + diavloToken);
/*     */             HttpResponse response = closeableHttpClient.execute((HttpUriRequest)get);
/*     */             int statusCode = response.getStatusLine().getStatusCode();
/*     */             if (statusCode == 200) {
/*     */               HttpEntity responseEntity = response.getEntity();
/*     */               String res = EntityUtils.toString(responseEntity);
/*     */               return parseJsonResponse(res);
/*     */             } 
/*     */             System.err.println("Request failed with status code: " + statusCode);
/*     */             throw new IOException("Request failed with status code: " + statusCode);
/*  99 */           } catch (IOException e) {
/*     */             System.err.println("An error occurred: " + e.getMessage());
/*     */             throw new CompletionException(e);
/*     */           } 
/*     */         });
/*     */   }
/*     */   
/*     */   private Set<String> parseJsonResponse(String json) {
/* 107 */     JsonParser jsonParser = new JsonParser();
/* 108 */     JsonObject jsonObject = jsonParser.parse(json).getAsJsonObject();
/* 109 */     JsonArray resultsArray = jsonObject.getAsJsonArray("results");
/*     */     
/* 111 */     TypeToken<Set<String>> typeToken = new TypeToken<Set<String>>() {  };
/* 112 */     return (Set<String>)(new Gson()).fromJson((JsonElement)resultsArray, typeToken.getType());
/*     */   }
/*     */ 
/*     */   
/*     */   public String getUsage() {
/* 117 */     return "<usuario> || Obten direcciones IP de un usuario.";
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\rip\diavlo\base\commands\GetIPCommand.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */