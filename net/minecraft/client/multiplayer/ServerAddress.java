/*     */ package net.minecraft.client.multiplayer;
/*     */ 
/*     */ import java.net.IDN;
/*     */ import java.util.Hashtable;
/*     */ import javax.naming.directory.Attributes;
/*     */ import javax.naming.directory.DirContext;
/*     */ import javax.naming.directory.InitialDirContext;
/*     */ 
/*     */ 
/*     */ public class ServerAddress
/*     */ {
/*     */   private final String ipAddress;
/*     */   private final int serverPort;
/*     */   
/*     */   private ServerAddress(String address, int port) {
/*  16 */     this.ipAddress = address;
/*  17 */     this.serverPort = port;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getIP() {
/*  22 */     return IDN.toASCII(this.ipAddress);
/*     */   }
/*     */ 
/*     */   
/*     */   public int getPort() {
/*  27 */     return this.serverPort;
/*     */   }
/*     */ 
/*     */   
/*     */   public static ServerAddress fromString(String p_78860_0_) {
/*  32 */     if (p_78860_0_ == null)
/*     */     {
/*  34 */       return null;
/*     */     }
/*     */ 
/*     */     
/*  38 */     String[] astring = p_78860_0_.split(":");
/*     */     
/*  40 */     if (p_78860_0_.startsWith("[")) {
/*     */       
/*  42 */       int i = p_78860_0_.indexOf("]");
/*     */       
/*  44 */       if (i > 0) {
/*     */         
/*  46 */         String s = p_78860_0_.substring(1, i);
/*  47 */         String s1 = p_78860_0_.substring(i + 1).trim();
/*     */         
/*  49 */         if (s1.startsWith(":") && s1.length() > 0) {
/*     */           
/*  51 */           s1 = s1.substring(1);
/*  52 */           astring = new String[] { s, s1 };
/*     */         }
/*     */         else {
/*     */           
/*  56 */           astring = new String[] { s };
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/*  61 */     if (astring.length > 2)
/*     */     {
/*  63 */       astring = new String[] { p_78860_0_ };
/*     */     }
/*     */     
/*  66 */     String s2 = astring[0];
/*  67 */     int j = (astring.length > 1) ? parseIntWithDefault(astring[1], 25565) : 25565;
/*     */     
/*  69 */     if (j == 25565) {
/*     */       
/*  71 */       String[] astring1 = getServerAddress(s2);
/*  72 */       s2 = astring1[0];
/*  73 */       j = parseIntWithDefault(astring1[1], 25565);
/*     */     } 
/*     */     
/*  76 */     return new ServerAddress(s2, j);
/*     */   }
/*     */ 
/*     */   
/*     */   public static ServerAddress resolveAddress(String serverIP) {
/*  81 */     if (serverIP == null)
/*  82 */       return null; 
/*  83 */     String[] astring = serverIP.split(":");
/*     */     int i;
/*  85 */     if (serverIP.startsWith("[") && (i = serverIP.indexOf("]")) > 0) {
/*  86 */       String s = serverIP.substring(1, i);
/*  87 */       String s1 = serverIP.substring(i + 1).trim();
/*  88 */       if (s1.startsWith(":") && s1.length() > 0) {
/*  89 */         s1 = s1.substring(1);
/*  90 */         astring = new String[] { s, s1 };
/*     */       } else {
/*  92 */         astring = new String[] { s };
/*     */       } 
/*     */     } 
/*  95 */     if (astring.length > 2)
/*  96 */       astring = new String[] { serverIP }; 
/*  97 */     String s2 = astring[0];
/*  98 */     int j = (astring.length > 1) ? getInt(astring[1], 25565) : 25565, n = j;
/*  99 */     if (j == 25565) {
/* 100 */       String[] astring1 = getServerAddress(s2);
/* 101 */       s2 = astring1[0];
/* 102 */       j = getInt(astring1[1], 25565);
/*     */     } 
/* 104 */     return new ServerAddress(s2, j);
/*     */   }
/*     */   
/*     */   private static int getInt(String value, int defaultValue) {
/*     */     try {
/* 109 */       return Integer.parseInt(value.trim());
/* 110 */     } catch (Exception var3) {
/* 111 */       return defaultValue;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static String[] getServerAddress(String p_78863_0_) {
/*     */     try {
/* 119 */       String s = "com.sun.jndi.dns.DnsContextFactory";
/* 120 */       Class.forName("com.sun.jndi.dns.DnsContextFactory");
/* 121 */       Hashtable<Object, Object> hashtable = new Hashtable<>();
/* 122 */       hashtable.put("java.naming.factory.initial", "com.sun.jndi.dns.DnsContextFactory");
/* 123 */       hashtable.put("java.naming.provider.url", "dns:");
/* 124 */       hashtable.put("com.sun.jndi.dns.timeout.retries", "1");
/* 125 */       DirContext dircontext = new InitialDirContext(hashtable);
/* 126 */       Attributes attributes = dircontext.getAttributes("_minecraft._tcp." + p_78863_0_, new String[] { "SRV" });
/* 127 */       String[] astring = attributes.get("srv").get().toString().split(" ", 4);
/* 128 */       return new String[] { astring[3], astring[2] };
/*     */     }
/* 130 */     catch (Throwable var6) {
/*     */       
/* 132 */       return new String[] { p_78863_0_, Integer.toString(25565) };
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static int parseIntWithDefault(String p_78862_0_, int p_78862_1_) {
/*     */     try {
/* 140 */       return Integer.parseInt(p_78862_0_.trim());
/*     */     }
/* 142 */     catch (Exception var3) {
/*     */       
/* 144 */       return p_78862_1_;
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\client\multiplayer\ServerAddress.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */