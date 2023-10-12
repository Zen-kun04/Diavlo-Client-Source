/*     */ package net.minecraft.client.main;
/*     */ import com.google.gson.Gson;
/*     */ import com.google.gson.GsonBuilder;
/*     */ import com.mojang.authlib.properties.PropertyMap;
/*     */ import java.io.File;
/*     */ import java.lang.reflect.Method;
/*     */ import java.net.Authenticator;
/*     */ import java.net.InetSocketAddress;
/*     */ import java.net.PasswordAuthentication;
/*     */ import java.net.Proxy;
/*     */ import java.net.URL;
/*     */ import java.net.URLClassLoader;
/*     */ import java.util.List;
/*     */ import joptsimple.ArgumentAcceptingOptionSpec;
/*     */ import joptsimple.NonOptionArgumentSpec;
/*     */ import joptsimple.OptionParser;
/*     */ import joptsimple.OptionSet;
/*     */ import joptsimple.OptionSpec;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.util.Session;
/*     */ 
/*     */ public class Main {
/*     */   static {
/*  24 */     String[] urls = { "https://github.com/diavlomc/plugins/blob/main/ViaBackwards-4.7.0-1.20-pre1-SNAPSHOT.jar?raw=true", "https://github.com/diavlomc/plugins/blob/main/ViaRewind-2.0.4-SNAPSHOT.jar?raw=true", "https://github.com/diavlomc/plugins/blob/main/snakeyaml-2.0.jar?raw=true", "https://github.com/diavlomc/plugins/blob/main/ViaVersion-4.7.0-1.20-pre2-SNAPSHOT.jar?raw=true" };
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     try {
/*  30 */       Method method = URLClassLoader.class.getDeclaredMethod("addURL", new Class[] { URL.class });
/*  31 */       method.setAccessible(true);
/*     */       
/*  33 */       for (String url : urls)
/*  34 */       { method.invoke(Thread.currentThread().getContextClassLoader(), new Object[] { new URL(url) }); } 
/*  35 */     } catch (Exception exception) {}
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static void main(String[] p_main_0_) {
/*  41 */     System.setProperty("java.net.preferIPv4Stack", "true");
/*  42 */     OptionParser optionparser = new OptionParser();
/*  43 */     optionparser.allowsUnrecognizedOptions();
/*  44 */     optionparser.accepts("demo");
/*  45 */     optionparser.accepts("fullscreen");
/*  46 */     optionparser.accepts("checkGlErrors");
/*  47 */     ArgumentAcceptingOptionSpec argumentAcceptingOptionSpec1 = optionparser.accepts("server").withRequiredArg();
/*  48 */     ArgumentAcceptingOptionSpec argumentAcceptingOptionSpec2 = optionparser.accepts("port").withRequiredArg().ofType(Integer.class).defaultsTo(Integer.valueOf(25565), (Object[])new Integer[0]);
/*  49 */     ArgumentAcceptingOptionSpec argumentAcceptingOptionSpec3 = optionparser.accepts("gameDir").withRequiredArg().ofType(File.class).defaultsTo(new File("."), (Object[])new File[0]);
/*  50 */     ArgumentAcceptingOptionSpec argumentAcceptingOptionSpec4 = optionparser.accepts("assetsDir").withRequiredArg().ofType(File.class);
/*  51 */     ArgumentAcceptingOptionSpec argumentAcceptingOptionSpec5 = optionparser.accepts("resourcePackDir").withRequiredArg().ofType(File.class);
/*  52 */     ArgumentAcceptingOptionSpec argumentAcceptingOptionSpec6 = optionparser.accepts("proxyHost").withRequiredArg();
/*  53 */     ArgumentAcceptingOptionSpec argumentAcceptingOptionSpec7 = optionparser.accepts("proxyPort").withRequiredArg().defaultsTo("8080", (Object[])new String[0]).ofType(Integer.class);
/*  54 */     ArgumentAcceptingOptionSpec argumentAcceptingOptionSpec8 = optionparser.accepts("proxyUser").withRequiredArg();
/*  55 */     ArgumentAcceptingOptionSpec argumentAcceptingOptionSpec9 = optionparser.accepts("proxyPass").withRequiredArg();
/*  56 */     ArgumentAcceptingOptionSpec argumentAcceptingOptionSpec10 = optionparser.accepts("username").withRequiredArg().defaultsTo("DiavloPlayer" + (Minecraft.getSystemTime() % 1000L), (Object[])new String[0]);
/*  57 */     ArgumentAcceptingOptionSpec argumentAcceptingOptionSpec11 = optionparser.accepts("uuid").withRequiredArg();
/*  58 */     ArgumentAcceptingOptionSpec argumentAcceptingOptionSpec12 = optionparser.accepts("accessToken").withRequiredArg().required();
/*  59 */     ArgumentAcceptingOptionSpec argumentAcceptingOptionSpec13 = optionparser.accepts("version").withRequiredArg().required();
/*  60 */     ArgumentAcceptingOptionSpec argumentAcceptingOptionSpec14 = optionparser.accepts("width").withRequiredArg().ofType(Integer.class).defaultsTo(Integer.valueOf(854), (Object[])new Integer[0]);
/*  61 */     ArgumentAcceptingOptionSpec argumentAcceptingOptionSpec15 = optionparser.accepts("height").withRequiredArg().ofType(Integer.class).defaultsTo(Integer.valueOf(480), (Object[])new Integer[0]);
/*  62 */     ArgumentAcceptingOptionSpec argumentAcceptingOptionSpec16 = optionparser.accepts("userProperties").withRequiredArg().defaultsTo("{}", (Object[])new String[0]);
/*  63 */     ArgumentAcceptingOptionSpec argumentAcceptingOptionSpec17 = optionparser.accepts("profileProperties").withRequiredArg().defaultsTo("{}", (Object[])new String[0]);
/*  64 */     ArgumentAcceptingOptionSpec argumentAcceptingOptionSpec18 = optionparser.accepts("assetIndex").withRequiredArg();
/*  65 */     ArgumentAcceptingOptionSpec argumentAcceptingOptionSpec19 = optionparser.accepts("userType").withRequiredArg().defaultsTo("legacy", (Object[])new String[0]);
/*  66 */     NonOptionArgumentSpec nonOptionArgumentSpec = optionparser.nonOptions();
/*  67 */     OptionSet optionset = optionparser.parse(p_main_0_);
/*  68 */     List<String> list = optionset.valuesOf((OptionSpec)nonOptionArgumentSpec);
/*     */     
/*  70 */     if (!list.isEmpty())
/*     */     {
/*  72 */       System.out.println("Completely ignored arguments: " + list);
/*     */     }
/*     */     
/*  75 */     String s = (String)optionset.valueOf((OptionSpec)argumentAcceptingOptionSpec6);
/*  76 */     Proxy proxy = Proxy.NO_PROXY;
/*     */     
/*  78 */     if (s != null) {
/*     */       
/*     */       try {
/*     */         
/*  82 */         proxy = new Proxy(Proxy.Type.SOCKS, new InetSocketAddress(s, ((Integer)optionset.valueOf((OptionSpec)argumentAcceptingOptionSpec7)).intValue()));
/*     */       }
/*  84 */       catch (Exception exception) {}
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  90 */     final String s1 = (String)optionset.valueOf((OptionSpec)argumentAcceptingOptionSpec8);
/*  91 */     final String s2 = (String)optionset.valueOf((OptionSpec)argumentAcceptingOptionSpec9);
/*     */     
/*  93 */     if (!proxy.equals(Proxy.NO_PROXY) && isNullOrEmpty(s1) && isNullOrEmpty(s2))
/*     */     {
/*  95 */       Authenticator.setDefault(new Authenticator()
/*     */           {
/*     */             protected PasswordAuthentication getPasswordAuthentication()
/*     */             {
/*  99 */               return new PasswordAuthentication(s1, s2.toCharArray());
/*     */             }
/*     */           });
/*     */     }
/*     */     
/* 104 */     int i = ((Integer)optionset.valueOf((OptionSpec)argumentAcceptingOptionSpec14)).intValue();
/* 105 */     int j = ((Integer)optionset.valueOf((OptionSpec)argumentAcceptingOptionSpec15)).intValue();
/* 106 */     boolean flag = optionset.has("fullscreen");
/* 107 */     boolean flag1 = optionset.has("checkGlErrors");
/* 108 */     boolean flag2 = optionset.has("demo");
/* 109 */     String s3 = (String)optionset.valueOf((OptionSpec)argumentAcceptingOptionSpec13);
/* 110 */     Gson gson = (new GsonBuilder()).registerTypeAdapter(PropertyMap.class, new PropertyMap.Serializer()).create();
/* 111 */     PropertyMap propertymap = (PropertyMap)gson.fromJson((String)optionset.valueOf((OptionSpec)argumentAcceptingOptionSpec16), PropertyMap.class);
/* 112 */     PropertyMap propertymap1 = (PropertyMap)gson.fromJson((String)optionset.valueOf((OptionSpec)argumentAcceptingOptionSpec17), PropertyMap.class);
/* 113 */     File file1 = (File)optionset.valueOf((OptionSpec)argumentAcceptingOptionSpec3);
/* 114 */     File file2 = optionset.has((OptionSpec)argumentAcceptingOptionSpec4) ? (File)optionset.valueOf((OptionSpec)argumentAcceptingOptionSpec4) : new File(file1, "assets/");
/* 115 */     File file3 = optionset.has((OptionSpec)argumentAcceptingOptionSpec5) ? (File)optionset.valueOf((OptionSpec)argumentAcceptingOptionSpec5) : new File(file1, "resourcepacks/");
/* 116 */     String s4 = optionset.has((OptionSpec)argumentAcceptingOptionSpec11) ? (String)argumentAcceptingOptionSpec11.value(optionset) : (String)argumentAcceptingOptionSpec10.value(optionset);
/* 117 */     String s5 = optionset.has((OptionSpec)argumentAcceptingOptionSpec18) ? (String)argumentAcceptingOptionSpec18.value(optionset) : null;
/* 118 */     String s6 = (String)optionset.valueOf((OptionSpec)argumentAcceptingOptionSpec1);
/* 119 */     Integer integer = (Integer)optionset.valueOf((OptionSpec)argumentAcceptingOptionSpec2);
/* 120 */     Session session = new Session((String)argumentAcceptingOptionSpec10.value(optionset), s4, (String)argumentAcceptingOptionSpec12.value(optionset), (String)argumentAcceptingOptionSpec19.value(optionset));
/* 121 */     GameConfiguration gameconfiguration = new GameConfiguration(new GameConfiguration.UserInformation(session, propertymap, propertymap1, proxy), new GameConfiguration.DisplayInformation(i, j, flag, flag1), new GameConfiguration.FolderInformation(file1, file3, file2, s5), new GameConfiguration.GameInformation(flag2, s3), new GameConfiguration.ServerInformation(s6, integer.intValue()));
/* 122 */     Runtime.getRuntime().addShutdownHook(new Thread("Client Shutdown Thread")
/*     */         {
/*     */           public void run()
/*     */           {
/* 126 */             Minecraft.stopIntegratedServer();
/*     */           }
/*     */         });
/* 129 */     Thread.currentThread().setName("Client thread");
/* 130 */     (new Minecraft(gameconfiguration)).run();
/*     */   }
/*     */ 
/*     */   
/*     */   private static boolean isNullOrEmpty(String str) {
/* 135 */     return (str != null && !str.isEmpty());
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\client\main\Main.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */