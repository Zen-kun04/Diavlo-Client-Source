/*     */ package rip.diavlo.base.commands;
/*     */ 
/*     */ import es.diavlo.api.data.UserData;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.Comparator;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.concurrent.CompletableFuture;
/*     */ import java.util.concurrent.ExecutorService;
/*     */ import java.util.concurrent.Executors;
/*     */ import rip.diavlo.base.api.command.CommandExecutionException;
/*     */ import rip.diavlo.base.api.command.CommandVIP;
/*     */ import rip.diavlo.base.utils.ChatUtil;
/*     */ import rip.diavlo.base.utils.scanner.Scanner;
/*     */ import rip.diavlo.base.utils.scanner.ServerData;
/*     */ 
/*     */ public class scanCommand implements CommandVIP {
/*     */   public String[] getAliases() {
/*  21 */     return new String[] { "scan" };
/*     */   }
/*     */ 
/*     */   
/*     */   public void execute(String[] arguments) throws CommandExecutionException {
/*  26 */     this.diavloUser = UserData.getInstance();
/*     */ 
/*     */     
/*  29 */     if (this.diavloUser == null) {
/*  30 */       ChatUtil.print("§4§lERROR: §c§lTienes que logearte en Diavlo para realizar este comando.", true);
/*     */       
/*     */       return;
/*     */     } 
/*  34 */     if (arguments.length != 3) {
/*  35 */       ChatUtil.print(".scan <ip> <rango de puertos> (Ej: 1,5,25550-25580,30000-30010)", true);
/*     */       
/*     */       return;
/*     */     } 
/*     */     
/*  40 */     String ip = arguments[1];
/*  41 */     String portrange = arguments[2];
/*     */ 
/*     */ 
/*     */     
/*  45 */     ChatUtil.print("§8-----------------------------------------", false);
/*  46 */     ChatUtil.print("§7Realizando un Escaneo §6§lVIP§7 de la IP: §c§l" + ip + "§7...", true);
/*  47 */     ChatUtil.print("", false);
/*     */ 
/*     */ 
/*     */     
/*  51 */     ExecutorService executor = Executors.newFixedThreadPool(1);
/*     */     
/*  53 */     CompletableFuture.supplyAsync(() -> {
/*     */           try {
/*     */             return startScan(ip, portrange).get();
/*  56 */           } catch (Exception e) {
/*     */             return null;
/*     */           } 
/*  59 */         }executor).thenAccept(data -> {
/*     */           if (data == null) {
/*     */             ChatUtil.print("§4§lERROR: §fNo se han encontrado puertos abiertos.", false);
/*     */             
/*     */             ChatUtil.print("", false);
/*     */             
/*     */             ChatUtil.print("§8-----------------------------------------", false);
/*     */             
/*     */             ChatUtil.print("", false);
/*     */           } else {
/*     */             ChatUtil.print("§c§lResultados:", false);
/*     */             
/*     */             ChatUtil.print("", false);
/*     */             
/*     */             List<Map.Entry<Integer, ServerData>> sortedEntries = new ArrayList<>(data.entrySet());
/*     */             
/*     */             Collections.sort(sortedEntries, Comparator.comparing(Map.Entry::getKey));
/*     */             
/*     */             for (Map.Entry<Integer, ServerData> entry : sortedEntries) {
/*     */               Integer key = entry.getKey();
/*     */               
/*     */               ServerData value = entry.getValue();
/*     */               
/*     */               ChatUtil.print("§a§lPuerto: " + key + " §f: " + value, false);
/*     */               
/*     */               ChatUtil.print("", false);
/*     */             } 
/*     */             ChatUtil.print("§8-----------------------------------------", false);
/*     */             ChatUtil.print("", false);
/*     */           } 
/*     */         });
/*  90 */     executor.shutdown();
/*     */   }
/*     */   
/*     */   private UserData diavloUser;
/*     */   
/*     */   private CompletableFuture<HashMap<Integer, ServerData>> startScan(String ip, String PortRange) {
/*  96 */     return CompletableFuture.supplyAsync(() -> Scanner.startScanner(ip, PortRange));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String getUsage() {
/* 102 */     return "<ip> <rango de puertos> (Ej: 1,5,25550-25580,30000-30010) || Realiza un escaneo de puertos customizado sobre una IP.";
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\rip\diavlo\base\commands\scanCommand.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */