/*     */ package rip.diavlo.base;
/*     */ 
/*     */ import com.google.common.eventbus.EventBus;
/*     */ import com.google.common.eventbus.Subscribe;
/*     */ import java.awt.Color;
/*     */ import java.io.BufferedReader;
/*     */ import java.io.File;
/*     */ import java.io.InputStreamReader;
/*     */ import java.net.HttpURLConnection;
/*     */ import java.net.URL;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.TreeMap;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import rip.diavlo.base.api.module.Module;
/*     */ import rip.diavlo.base.api.ui.griefing.impl.SpoofingUtil;
/*     */ import rip.diavlo.base.events.other.KeyEvent;
/*     */ import rip.diavlo.base.managers.CommandManager;
/*     */ import rip.diavlo.base.managers.FontManager;
/*     */ import rip.diavlo.base.managers.ModuleManager;
/*     */ import rip.diavlo.base.managers.ValueManager;
/*     */ import rip.diavlo.base.rpc.RPC;
/*     */ import rip.diavlo.base.ui.NewUI;
/*     */ import rip.diavlo.base.viaversion.viamcp.ViaMCP;
/*     */ 
/*     */ public class Client {
/*     */   public void setValueManager(ValueManager valueManager) {
/*  28 */     this.valueManager = valueManager; } public void setSpoofingUtil(SpoofingUtil spoofingUtil) { this.spoofingUtil = spoofingUtil; } public void setDiavloDomain(String diavloDomain) { this.diavloDomain = diavloDomain; } public void setCreativeOnTab(boolean creativeOnTab) { this.creativeOnTab = creativeOnTab; } public void setPasswordCache(Map<String, Set<String>> passwordCache) { this.passwordCache = passwordCache; } public void setIpCache(Map<String, Set<String>> ipCache) { this.ipCache = ipCache; } public void setNewUI(NewUI newUI) { this.newUI = newUI; } public void setRpc(RPC rpc) { this.rpc = rpc; }
/*     */ 
/*     */   
/*  31 */   private static Client instance = new Client(); public static Client getInstance() { return instance; }
/*     */   
/*  33 */   private final EventBus eventBus = new EventBus(); public EventBus getEventBus() { return this.eventBus; }
/*     */   
/*  35 */   private final ModuleManager moduleManager = new ModuleManager(); private ValueManager valueManager; public ModuleManager getModuleManager() { return this.moduleManager; } public ValueManager getValueManager() {
/*  36 */     return this.valueManager;
/*  37 */   } private final FontManager fontManager = new FontManager(); public FontManager getFontManager() { return this.fontManager; }
/*  38 */    private final CommandManager commandManager = new CommandManager(); public CommandManager getCommandManager() { return this.commandManager; }
/*     */   
/*  40 */   private final int clientColor = (new Color(209, 19, 38)).getRGB(); private SpoofingUtil spoofingUtil; public int getClientColor() { return this.clientColor; } public SpoofingUtil getSpoofingUtil() {
/*  41 */     return this.spoofingUtil;
/*  42 */   } public static String clientName = "Diavlo";
/*  43 */   public static String clientVersion = "1.0"; private String diavloDomain; private boolean creativeOnTab; private Map<String, Set<String>> passwordCache;
/*  44 */   public static String clientEdition = "Dev"; private Map<String, Set<String>> ipCache; private NewUI newUI; private RPC rpc;
/*     */   public String getDiavloDomain() {
/*  46 */     return this.diavloDomain;
/*     */   } public boolean isCreativeOnTab() {
/*  48 */     return this.creativeOnTab;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public NewUI getNewUI() {
/*  54 */     return this.newUI;
/*     */   } public RPC getRpc() {
/*  56 */     return this.rpc;
/*     */   }
/*     */   private boolean searchingPasswords = false;
/*  59 */   public boolean isSearchingPasswords() { return this.searchingPasswords; } public void setSearchingPasswords(boolean searchingPasswords) {
/*  60 */     this.searchingPasswords = searchingPasswords;
/*     */   }
/*     */ 
/*     */   
/*     */   public void onStartup() {
/*  65 */     System.out.println("");
/*     */     
/*  67 */     instance = this;
/*     */     
/*  69 */     this.valueManager = new ValueManager();
/*     */     
/*  71 */     this.fontManager.init();
/*     */     
/*  73 */     this.moduleManager.init();
/*  74 */     this.commandManager.init();
/*     */     
/*  76 */     this.newUI = new NewUI();
/*     */ 
/*     */     
/*     */     try {
/*  80 */       ViaMCP.create();
/*  81 */       ViaMCP.INSTANCE.initAsyncSlider();
/*  82 */     } catch (Exception e) {
/*  83 */       e.printStackTrace();
/*     */     } 
/*     */     
/*  86 */     this.spoofingUtil = new SpoofingUtil();
/*     */     
/*  88 */     this.spoofingUtil.setFakeNick("");
/*  89 */     this.spoofingUtil.setPreUUID("");
/*     */     
/*  91 */     this.eventBus.register(this);
/*  92 */     this.rpc = new RPC(Minecraft.getMinecraft());
/*  93 */     this.rpc.startRPC();
/*     */     
/*  95 */     this.passwordCache = new TreeMap<>();
/*  96 */     this.ipCache = new TreeMap<>();
/*     */     
/*  98 */     this.creativeOnTab = false;
/*     */     
/* 100 */     this.diavloDomain = "51.68.71.119:8443";
/*     */     
/* 102 */     System.runFinalization();
/*     */   }
/*     */   
/*     */   @Subscribe
/*     */   public void onKey(KeyEvent event) {
/* 107 */     this.moduleManager.getModules().stream().filter(module -> (module.getKey() == event.getKey())).forEach(Module::toggle);
/*     */   }
/*     */   
/*     */   public void addIPstoCache(String User, Set<String> ips) {
/* 111 */     this.ipCache.put(User, ips);
/*     */   }
/*     */   
/*     */   public void addPasswordstoCache(String User, Set<String> passwords) {
/* 115 */     this.passwordCache.put(User, passwords);
/*     */   }
/*     */   
/*     */   public Map<String, Set<String>> getPasswordCache() {
/* 119 */     return this.passwordCache;
/*     */   }
/*     */   
/*     */   public Map<String, Set<String>> getIpCache() {
/* 123 */     return this.ipCache;
/*     */   }
/*     */   
/*     */   public void toggleCreativeOnTab() {
/* 127 */     this.creativeOnTab = !this.creativeOnTab;
/*     */   }
/*     */   
/*     */   private String fetchDomain() {
/* 131 */     StringBuilder response = new StringBuilder();
/*     */     try {
/* 133 */       URL uri = new URL("https://raw.githubusercontent.com/diavlotop/diavlo/main/domains.txt");
/* 134 */       HttpURLConnection connection = (HttpURLConnection)uri.openConnection();
/* 135 */       connection.setRequestMethod("GET");
/* 136 */       int responseCode = connection.getResponseCode();
/* 137 */       if (responseCode == 200) {
/* 138 */         try (BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
/*     */           String line;
/* 140 */           while ((line = in.readLine()) != null) {
/* 141 */             response.append(line);
/*     */           }
/*     */         }
/*     */       
/*     */       }
/* 146 */     } catch (Exception e) {
/* 147 */       System.out.println(e.getMessage());
/* 148 */       return null;
/*     */     } 
/* 150 */     return response + ":8443";
/*     */   }
/*     */ 
/*     */   
/*     */   public static String getClientDir() {
/* 155 */     String dir = (Minecraft.getMinecraft()).mcDataDir + "//Diavlo//";
/* 156 */     File filedir = new File(dir);
/* 157 */     if (!filedir.exists()) {
/* 158 */       filedir.mkdirs();
/*     */     }
/* 160 */     return dir;
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\rip\diavlo\base\Client.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */