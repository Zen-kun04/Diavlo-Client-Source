/*     */ package rip.diavlo.base.commands;
/*     */ 
/*     */ import com.google.gson.Gson;
/*     */ import com.google.gson.JsonArray;
/*     */ import com.google.gson.JsonElement;
/*     */ import com.google.gson.JsonObject;
/*     */ import com.google.gson.JsonParser;
/*     */ import com.google.gson.reflect.TypeToken;
/*     */ import es.diavlo.api.data.UserData;
/*     */ import java.io.IOException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
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
/*     */ public final class PasswordsCommand implements CommandVIP {
/*     */   public String[] getAliases() {
/*  34 */     return new String[] { "getpasswords" };
/*     */   }
/*     */   private UserData diavloUser;
/*     */   private void showResults(Set<String> passwords) {
/*  38 */     if (passwords == null || passwords.size() == 0) {
/*  39 */       ChatUtil.print("§4§lERROR: §c§lNo se han encontrado contraseñas de este usuario.", true);
/*     */     } else {
/*     */       
/*  42 */       ChatUtil.print("§2§l¡§6§l" + passwords.size() + " §2§lContraseñas obtenidas!", true);
/*  43 */       ChatUtil.print("", false);
/*     */       
/*  45 */       for (String p : passwords) {
/*  46 */         ChatUtil.print("            §6§l- §a§l" + p, false);
/*  47 */         ChatUtil.print("", false);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void execute(String[] arguments) throws CommandExecutionException {
/*  54 */     this.diavloUser = UserData.getInstance();
/*  55 */     if (arguments.length < 2) {
/*  56 */       ChatUtil.print(".getpasswords <usuario>", true);
/*     */     } else {
/*  58 */       String user = String.join(" ", Arrays.<CharSequence>copyOfRange((CharSequence[])arguments, 1, arguments.length)).toLowerCase();
/*  59 */       if (this.diavloUser == null) {
/*  60 */         ChatUtil.print("§4§lERROR: §c§lTienes que logearte en Diavlo para realizar este comando.", true);
/*     */       } else {
/*  62 */         ChatUtil.print("§7Obteniendo contraseñas del usuario: §c§l" + user + "§7...", true);
/*  63 */         Map<String, Set<String>> passwordCache = Client.getInstance().getPasswordCache();
/*  64 */         Set<String> passwordsFound = passwordCache.getOrDefault(user, null);
/*  65 */         if (passwordsFound == null) {
/*  66 */           ExecutorService executor = Executors.newFixedThreadPool(1);
/*  67 */           CompletableFuture.supplyAsync(() -> {
/*     */                 try {
/*     */                   return obtenerPasswordsAsync(user, this.diavloUser.getToken()).get();
/*  70 */                 } catch (Exception e) {
/*     */                   return new ArrayList();
/*     */                 } 
/*  73 */               }executor).thenAccept(passwords -> {
/*     */                 showResults((Set<String>)passwords);
/*     */                 Client.getInstance().addPasswordstoCache(user, (Set)passwords);
/*     */               });
/*  77 */           executor.shutdown();
/*     */         } else {
/*  79 */           showResults(passwordsFound);
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private CompletableFuture<Set<String>> obtenerPasswordsAsync(String username, String diavloToken) {
/*  86 */     return CompletableFuture.supplyAsync(() -> {
/*     */           try {
/*     */             String diavloDomain = Client.getInstance().getDiavloDomain();
/*     */             
/*     */             CloseableHttpClient closeableHttpClient = HttpClients.createDefault();
/*     */             
/*     */             HttpGet get = new HttpGet("http://" + diavloDomain + "/api/v3/mcclient/passwords/" + username);
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
/* 105 */           } catch (IOException e) {
/*     */             System.err.println("An error occurred: " + e.getMessage());
/*     */             throw new CompletionException(e);
/*     */           } 
/*     */         });
/*     */   }
/*     */   
/*     */   private Set<String> parseJsonResponse(String json) {
/* 113 */     JsonParser jsonParser = new JsonParser();
/* 114 */     JsonObject jsonObject = jsonParser.parse(json).getAsJsonObject();
/* 115 */     JsonArray resultsArray = jsonObject.getAsJsonArray("results");
/*     */     
/* 117 */     TypeToken<Set<String>> typeToken = new TypeToken<Set<String>>() {  }
/*     */       ;
/* 119 */     return (Set<String>)(new Gson()).fromJson((JsonElement)resultsArray, typeToken.getType());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String getUsage() {
/* 125 */     return "<usuario> || Obten contraseñas de un usuario.";
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\rip\diavlo\base\commands\PasswordsCommand.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */