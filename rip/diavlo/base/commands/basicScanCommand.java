/*    */ package rip.diavlo.base.commands;
/*    */ import java.util.ArrayList;
/*    */ import java.util.Comparator;
/*    */ import java.util.HashMap;
/*    */ import java.util.List;
/*    */ import java.util.Map;
/*    */ import java.util.concurrent.CompletableFuture;
/*    */ import java.util.concurrent.ExecutorService;
/*    */ import java.util.concurrent.Executors;
/*    */ import rip.diavlo.base.api.command.Command;
/*    */ import rip.diavlo.base.api.command.CommandExecutionException;
/*    */ import rip.diavlo.base.utils.ChatUtil;
/*    */ import rip.diavlo.base.utils.scanner.Scanner;
/*    */ import rip.diavlo.base.utils.scanner.ServerData;
/*    */ 
/*    */ public class basicScanCommand implements Command {
/*    */   public String[] getAliases() {
/* 18 */     return new String[] { "basicscan" };
/*    */   }
/*    */ 
/*    */   
/*    */   public void execute(String[] arguments) throws CommandExecutionException {
/* 23 */     if (arguments.length != 2) {
/* 24 */       ChatUtil.print(".basicscan <ip>", true);
/*    */       
/*    */       return;
/*    */     } 
/* 28 */     String fuckoff = arguments[1];
/* 29 */     ChatUtil.print("§8-----------------------------------------", false);
/* 30 */     ChatUtil.print("§7Realizando un Escaneo §aBásico§7 de la IP: §c§l" + fuckoff + "§7...", true);
/* 31 */     ChatUtil.print("", false);
/*    */     
/* 33 */     ExecutorService executor = Executors.newFixedThreadPool(1);
/* 34 */     CompletableFuture.supplyAsync(() -> {
/*    */           try {
/*    */             return startScan(fuckoff).get();
/* 37 */           } catch (Exception e) {
/*    */             return null;
/*    */           } 
/* 40 */         }executor).thenAccept(data -> {
/*    */           System.out.println(data.toString());
/*    */           
/*    */           if (data.isEmpty()) {
/*    */             ChatUtil.print("§4§lERROR: §fNo se han encontrado puertos abiertos.", false);
/*    */             
/*    */             ChatUtil.print("", false);
/*    */             ChatUtil.print("§8-----------------------------------------", false);
/*    */             ChatUtil.print("", false);
/*    */           } else {
/*    */             ChatUtil.print("§c§lResultados:", false);
/*    */             ChatUtil.print("", false);
/*    */             List<Map.Entry<Integer, ServerData>> sortedEntries = new ArrayList<>(data.entrySet());
/*    */             sortedEntries.sort((Comparator)Map.Entry.comparingByKey());
/*    */             for (Map.Entry<Integer, ServerData> entry : sortedEntries) {
/*    */               Integer key = entry.getKey();
/*    */               ServerData value = entry.getValue();
/*    */               ChatUtil.print("§a§lPuerto: " + key + " §f: " + value, false);
/*    */               ChatUtil.print("", false);
/*    */             } 
/*    */             ChatUtil.print("§8-----------------------------------------", false);
/*    */           } 
/*    */         });
/* 63 */     executor.shutdown();
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   private CompletableFuture<HashMap<Integer, ServerData>> startScan(String ip) {
/* 69 */     return CompletableFuture.supplyAsync(() -> Scanner.startScanner(ip, "25550-25570"));
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public String getUsage() {
/* 75 */     return "<ip> || Realiza un escaneo de puertos básico sobre una IP.";
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\rip\diavlo\base\commands\basicScanCommand.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */