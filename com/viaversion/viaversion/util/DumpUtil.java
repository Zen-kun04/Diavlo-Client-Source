/*     */ package com.viaversion.viaversion.util;
/*     */ 
/*     */ import com.google.common.io.CharStreams;
/*     */ import com.viaversion.viaversion.api.Via;
/*     */ import com.viaversion.viaversion.api.connection.UserConnection;
/*     */ import com.viaversion.viaversion.api.protocol.version.ProtocolVersion;
/*     */ import com.viaversion.viaversion.dump.DumpTemplate;
/*     */ import com.viaversion.viaversion.dump.VersionInfo;
/*     */ import com.viaversion.viaversion.libs.gson.GsonBuilder;
/*     */ import com.viaversion.viaversion.libs.gson.JsonArray;
/*     */ import com.viaversion.viaversion.libs.gson.JsonElement;
/*     */ import com.viaversion.viaversion.libs.gson.JsonObject;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.InputStreamReader;
/*     */ import java.io.InvalidObjectException;
/*     */ import java.io.OutputStream;
/*     */ import java.net.HttpURLConnection;
/*     */ import java.net.URL;
/*     */ import java.nio.charset.StandardCharsets;
/*     */ import java.util.HashSet;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.TreeMap;
/*     */ import java.util.UUID;
/*     */ import java.util.concurrent.CompletableFuture;
/*     */ import java.util.logging.Level;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class DumpUtil
/*     */ {
/*     */   public static CompletableFuture<String> postDump(UUID playerToSample) {
/*  66 */     VersionInfo version = new VersionInfo(System.getProperty("java.version"), System.getProperty("os.name"), Via.getAPI().getServerVersion().lowestSupportedVersion(), Via.getManager().getProtocolManager().getSupportedVersions(), Via.getPlatform().getPlatformName(), Via.getPlatform().getPlatformVersion(), Via.getPlatform().getPluginVersion(), VersionInfo.getImplementationVersion(), Via.getManager().getSubPlatforms());
/*     */     
/*  68 */     Map<String, Object> configuration = Via.getPlatform().getConfigurationProvider().getValues();
/*  69 */     DumpTemplate template = new DumpTemplate(version, configuration, Via.getPlatform().getDump(), Via.getManager().getInjector().getDump(), getPlayerSample(playerToSample));
/*  70 */     CompletableFuture<String> result = new CompletableFuture<>();
/*  71 */     Via.getPlatform().runAsync(() -> {
/*     */           HttpURLConnection con;
/*     */           try {
/*     */             con = (HttpURLConnection)(new URL("https://dump.viaversion.com/documents")).openConnection();
/*  75 */           } catch (IOException e) {
/*     */             Via.getPlatform().getLogger().log(Level.SEVERE, "Error when opening connection to ViaVersion dump service", e);
/*     */             
/*     */             result.completeExceptionally(new DumpException(DumpErrorType.CONNECTION, e));
/*     */             
/*     */             return;
/*     */           } 
/*     */           
/*     */           try {
/*     */             String rawOutput;
/*     */             
/*     */             con.setRequestProperty("Content-Type", "application/json");
/*     */             
/*     */             con.addRequestProperty("User-Agent", "ViaVersion-" + Via.getPlatform().getPlatformName() + "/" + version.getPluginVersion());
/*     */             
/*     */             con.setRequestMethod("POST");
/*     */             con.setDoOutput(true);
/*     */             try (OutputStream out = con.getOutputStream()) {
/*     */               out.write((new GsonBuilder()).setPrettyPrinting().create().toJson(template).getBytes(StandardCharsets.UTF_8));
/*     */             } 
/*     */             if (con.getResponseCode() == 429) {
/*     */               result.completeExceptionally(new DumpException(DumpErrorType.RATE_LIMITED));
/*     */               return;
/*     */             } 
/*     */             try (InputStream inputStream = con.getInputStream()) {
/*     */               rawOutput = CharStreams.toString(new InputStreamReader(inputStream));
/*     */             } 
/*     */             JsonObject output = (JsonObject)GsonUtil.getGson().fromJson(rawOutput, JsonObject.class);
/*     */             if (!output.has("key")) {
/*     */               throw new InvalidObjectException("Key is not given in Hastebin output");
/*     */             }
/*     */             result.complete(urlForId(output.get("key").getAsString()));
/* 107 */           } catch (Exception e) {
/*     */             String rawOutput; Via.getPlatform().getLogger().log(Level.SEVERE, "Error when posting ViaVersion dump", (Throwable)rawOutput);
/*     */             result.completeExceptionally(new DumpException(DumpErrorType.POST, (Throwable)rawOutput));
/*     */             printFailureInfo(con);
/*     */           } 
/*     */         });
/* 113 */     return result;
/*     */   }
/*     */   
/*     */   private static void printFailureInfo(HttpURLConnection connection) {
/*     */     try {
/* 118 */       if (connection.getResponseCode() < 200 || connection.getResponseCode() > 400) {
/* 119 */         try (InputStream errorStream = connection.getErrorStream()) {
/* 120 */           String rawOutput = CharStreams.toString(new InputStreamReader(errorStream));
/* 121 */           Via.getPlatform().getLogger().log(Level.SEVERE, "Page returned: " + rawOutput);
/*     */         } 
/*     */       }
/* 124 */     } catch (IOException e) {
/* 125 */       Via.getPlatform().getLogger().log(Level.SEVERE, "Failed to capture further info", e);
/*     */     } 
/*     */   }
/*     */   
/*     */   public static String urlForId(String id) {
/* 130 */     return String.format("https://dump.viaversion.com/%s", new Object[] { id });
/*     */   }
/*     */   
/*     */   private static JsonObject getPlayerSample(UUID uuid) {
/* 134 */     JsonObject playerSample = new JsonObject();
/*     */     
/* 136 */     JsonObject versions = new JsonObject();
/* 137 */     playerSample.add("versions", (JsonElement)versions);
/* 138 */     Map<ProtocolVersion, Integer> playerVersions = new TreeMap<>((o1, o2) -> ProtocolVersion.getIndex(o2) - ProtocolVersion.getIndex(o1));
/* 139 */     for (UserConnection connection : Via.getManager().getConnectionManager().getConnections()) {
/* 140 */       ProtocolVersion protocolVersion = ProtocolVersion.getProtocol(connection.getProtocolInfo().getProtocolVersion());
/* 141 */       playerVersions.compute(protocolVersion, (v, num) -> Integer.valueOf((num != null) ? (num.intValue() + 1) : 1));
/*     */     } 
/* 143 */     for (Map.Entry<ProtocolVersion, Integer> entry : playerVersions.entrySet()) {
/* 144 */       versions.addProperty(((ProtocolVersion)entry.getKey()).getName(), entry.getValue());
/*     */     }
/*     */     
/* 147 */     Set<List<String>> pipelines = new HashSet<>();
/* 148 */     if (uuid != null) {
/*     */       
/* 150 */       UserConnection senderConnection = Via.getAPI().getConnection(uuid);
/* 151 */       if (senderConnection != null && senderConnection.getChannel() != null) {
/* 152 */         pipelines.add(senderConnection.getChannel().pipeline().names());
/*     */       }
/*     */     } 
/*     */ 
/*     */     
/* 157 */     for (UserConnection connection : Via.getManager().getConnectionManager().getConnections()) {
/* 158 */       if (connection.getChannel() == null) {
/*     */         continue;
/*     */       }
/*     */       
/* 162 */       List<String> names = connection.getChannel().pipeline().names();
/* 163 */       if (pipelines.add(names) && pipelines.size() == 3) {
/*     */         break;
/*     */       }
/*     */     } 
/*     */     
/* 168 */     int i = 0;
/* 169 */     for (List<String> pipeline : pipelines) {
/* 170 */       JsonArray senderPipeline = new JsonArray(pipeline.size());
/* 171 */       for (String name : pipeline) {
/* 172 */         senderPipeline.add(name);
/*     */       }
/*     */       
/* 175 */       playerSample.add("pipeline-" + i++, (JsonElement)senderPipeline);
/*     */     } 
/*     */     
/* 178 */     return playerSample;
/*     */   }
/*     */   
/*     */   public static final class DumpException extends RuntimeException {
/*     */     private final DumpUtil.DumpErrorType errorType;
/*     */     
/*     */     private DumpException(DumpUtil.DumpErrorType errorType, Throwable cause) {
/* 185 */       super(errorType.message(), cause);
/* 186 */       this.errorType = errorType;
/*     */     }
/*     */     
/*     */     private DumpException(DumpUtil.DumpErrorType errorType) {
/* 190 */       super(errorType.message());
/* 191 */       this.errorType = errorType;
/*     */     }
/*     */     
/*     */     public DumpUtil.DumpErrorType errorType() {
/* 195 */       return this.errorType;
/*     */     }
/*     */   }
/*     */   
/*     */   public enum DumpErrorType
/*     */   {
/* 201 */     CONNECTION("Failed to dump, please check the console for more information"),
/* 202 */     RATE_LIMITED("Please wait before creating another dump"),
/* 203 */     POST("Failed to dump, please check the console for more information");
/*     */     
/*     */     private final String message;
/*     */     
/*     */     DumpErrorType(String message) {
/* 208 */       this.message = message;
/*     */     }
/*     */     
/*     */     public String message() {
/* 212 */       return this.message;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\com\viaversion\viaversio\\util\DumpUtil.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */