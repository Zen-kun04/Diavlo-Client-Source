/*     */ package rip.diavlo.base.utils.scanner;
/*     */ 
/*     */ import com.mcping.MinecraftPing;
/*     */ import com.mcping.MinecraftPingOptions;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.concurrent.ExecutorService;
/*     */ import java.util.concurrent.Executors;
/*     */ import java.util.concurrent.Future;
/*     */ import java.util.stream.Collector;
/*     */ import java.util.stream.Collectors;
/*     */ import java.util.stream.IntStream;
/*     */ 
/*     */ public class Scanner {
/*     */   private static final int MAX_THREADS = 20;
/*     */   
/*     */   private static ArrayList<Integer> extractPorts(String portRange) {
/*  21 */     ArrayList<Integer> ports = new ArrayList<>();
/*  22 */     if (!portRange.contains(",") && !portRange.contains("-")) {
/*  23 */       ports.add(Integer.valueOf(Integer.parseInt(portRange)));
/*  24 */     } else if (!portRange.contains(",")) {
/*  25 */       int startPort = Integer.parseInt(portRange.split("-")[0]);
/*  26 */       int endPort = Integer.parseInt(portRange.split("-")[1]);
/*  27 */       ports.addAll(IntStream.rangeClosed(startPort, endPort)
/*  28 */           .boxed()
/*  29 */           .collect((Collector)Collectors.toList()));
/*     */     } else {
/*  31 */       String[] subPorts = portRange.split(",");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*  37 */       Collection<Integer> puertosSueltos = (Collection<Integer>)Arrays.<String>stream(subPorts).filter(p -> !p.contains("-")).map(Integer::parseInt).collect(Collectors.toList());
/*     */       
/*  39 */       ports.addAll(puertosSueltos);
/*     */       
/*  41 */       Arrays.<String>stream(subPorts)
/*  42 */         .filter(p -> p.contains("-"))
/*  43 */         .forEach(pr -> {
/*     */             int startPort = Integer.parseInt(pr.split("-")[0]);
/*     */ 
/*     */             
/*     */             int endPort = Integer.parseInt(pr.split("-")[1]);
/*     */             
/*     */             ports.addAll(IntStream.rangeClosed(startPort, endPort).boxed().collect((Collector)Collectors.toList()));
/*     */           });
/*     */     } 
/*     */     
/*  53 */     return ports;
/*     */   }
/*     */   
/*     */   public static HashMap<Integer, ServerData> startScanner(String IP, String PortRange) {
/*  57 */     HashMap<Integer, ServerData> resultado = new HashMap<>();
/*  58 */     ArrayList<Integer> ports = extractPorts(PortRange);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  64 */     ExecutorService executor = Executors.newFixedThreadPool(Math.min(ports.size(), (ports.size() < 200) ? 20 : 200));
/*     */     
/*  66 */     List<Future<Void>> futures = new ArrayList<>();
/*     */     
/*  68 */     for (Integer port : ports) {
/*  69 */       Future<Void> future = executor.submit(() -> {
/*     */             MinecraftPingOptions options = new MinecraftPingOptions();
/*     */             
/*     */             options.setHostname(IP);
/*     */             options.setPort(port.intValue());
/*     */             try {
/*     */               String[] result = (new MinecraftPing()).getPingString(options);
/*     */               ServerData resultMC = new ServerData(result);
/*     */               synchronized (resultado) {
/*     */                 resultado.put(port, resultMC);
/*     */               } 
/*  80 */             } catch (Exception exception) {}
/*     */             
/*     */             return null;
/*     */           });
/*     */       
/*  85 */       futures.add(future);
/*     */     } 
/*     */ 
/*     */     
/*  89 */     for (Future<Void> future : futures) {
/*     */       try {
/*  91 */         future.get();
/*  92 */       } catch (InterruptedException|java.util.concurrent.ExecutionException interruptedException) {}
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  98 */     executor.shutdown();
/*     */     
/* 100 */     return resultado;
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\rip\diavlo\bas\\utils\scanner\Scanner.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */