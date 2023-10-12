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
/*     */ import java.util.Collection;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.concurrent.CompletableFuture;
/*     */ import java.util.concurrent.CompletionException;
/*     */ import java.util.concurrent.ExecutorService;
/*     */ import java.util.concurrent.Executors;
/*     */ import net.minecraft.client.Minecraft;
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
/*     */ public final class myPasswordsCommand
/*     */   implements CommandVIP
/*     */ {
/*  38 */   private Minecraft mc = Minecraft.getMinecraft();
/*     */   
/*     */   private UserData diavloUser;
/*     */   
/*     */   public String[] getAliases() {
/*  43 */     return new String[] { "mypasswords" };
/*     */   }
/*     */ 
/*     */   
/*     */   private void showResults(Set<String> passwords) {
/*  48 */     if (passwords == null || (passwords != null && passwords.size() == 0)) {
/*  49 */       ChatUtil.print("§4§lERROR: §c§lNo se han encontrado contraseñas de este usuario.", true);
/*     */     } else {
/*     */       
/*  52 */       ChatUtil.print("§2§l¡§6§l" + passwords.size() + " §2§lContraseñas obtenidas!", true);
/*  53 */       ChatUtil.print("", false);
/*     */       
/*  55 */       for (String p : passwords) {
/*  56 */         ChatUtil.print("            §6§l- §a§l" + p, false);
/*  57 */         ChatUtil.print("", false);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void execute(String[] arguments) throws CommandExecutionException {
/*  64 */     this.diavloUser = UserData.getInstance();
/*     */     
/*  66 */     if (this.diavloUser != null) {
/*  67 */       ChatUtil.print("§7Obteniendo contraseñas del usuario: §c§l" + this.mc.thePlayer.getName() + "§7...", true);
/*     */       
/*  69 */       String user = this.mc.thePlayer.getName().toLowerCase();
/*     */       
/*  71 */       Map<String, Set<String>> passwordCache = Client.getInstance().getPasswordCache();
/*  72 */       Set<String> passwordsFound = passwordCache.getOrDefault(user, null);
/*  73 */       if (passwordsFound == null) {
/*  74 */         ExecutorService executor = Executors.newFixedThreadPool(1);
/*     */         
/*  76 */         CompletableFuture.supplyAsync(() -> {
/*     */               try {
/*     */                 return obtenerPasswordsAsync(this.mc.thePlayer.getName(), this.diavloUser.getToken()).get();
/*  79 */               } catch (Exception e) {
/*     */                 return new ArrayList();
/*     */               } 
/*  82 */             }executor).thenAccept(passwords -> {
/*     */               showResults((Set<String>)passwords);
/*     */               
/*     */               Client.getInstance().addPasswordstoCache(user, (Set)passwords);
/*     */             });
/*     */         
/*  88 */         executor.shutdown();
/*     */       } else {
/*  90 */         showResults(passwordsFound);
/*     */       }
/*     */     
/*     */     }
/*     */     else {
/*     */       
/*  96 */       ChatUtil.print("§4§lERROR: §c§lTienes que logearte en Diavlo para realizar este comando.", true);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private CompletableFuture<Set<String>> obtenerPasswordsAsync(String username, String diavloToken) {
/* 104 */     return CompletableFuture.supplyAsync(() -> {
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
/* 123 */           } catch (IOException e) {
/*     */             System.err.println("An error occurred: " + e.getMessage());
/*     */             throw new CompletionException(e);
/*     */           } 
/*     */         });
/*     */   }
/*     */   
/*     */   private Set<String> parseJsonResponse(String json) {
/* 131 */     JsonParser jsonParser = new JsonParser();
/* 132 */     JsonObject jsonObject = jsonParser.parse(json).getAsJsonObject();
/* 133 */     JsonArray resultsArray = jsonObject.getAsJsonArray("results");
/*     */     
/* 135 */     TypeToken<Set<String>> typeToken = new TypeToken<Set<String>>() {  };
/* 136 */     return (Set<String>)(new Gson()).fromJson((JsonElement)resultsArray, typeToken.getType());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String getUsage() {
/* 142 */     return " || Obten las posibles contraseñas del usuario actual.";
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\rip\diavlo\base\commands\myPasswordsCommand.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */