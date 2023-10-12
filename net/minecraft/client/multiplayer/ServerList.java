/*     */ package net.minecraft.client.multiplayer;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import java.io.File;
/*     */ import java.util.List;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.nbt.CompressedStreamTools;
/*     */ import net.minecraft.nbt.NBTBase;
/*     */ import net.minecraft.nbt.NBTTagCompound;
/*     */ import net.minecraft.nbt.NBTTagList;
/*     */ import org.apache.logging.log4j.LogManager;
/*     */ import org.apache.logging.log4j.Logger;
/*     */ 
/*     */ public class ServerList {
/*  15 */   private static final Logger logger = LogManager.getLogger();
/*     */   private final Minecraft mc;
/*  17 */   private final List<ServerData> servers = Lists.newArrayList();
/*     */ 
/*     */   
/*     */   public ServerList(Minecraft mcIn) {
/*  21 */     this.mc = mcIn;
/*  22 */     loadServerList();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void loadServerList() {
/*     */     try {
/*  29 */       this.servers.clear();
/*  30 */       NBTTagCompound nbttagcompound = CompressedStreamTools.read(new File(this.mc.mcDataDir, "servers.dat"));
/*     */       
/*  32 */       if (nbttagcompound == null) {
/*     */         return;
/*     */       }
/*     */ 
/*     */       
/*  37 */       NBTTagList nbttaglist = nbttagcompound.getTagList("servers", 10);
/*     */       
/*  39 */       for (int i = 0; i < nbttaglist.tagCount(); i++)
/*     */       {
/*  41 */         this.servers.add(ServerData.getServerDataFromNBTCompound(nbttaglist.getCompoundTagAt(i)));
/*     */       }
/*     */     }
/*  44 */     catch (Exception exception) {
/*     */       
/*  46 */       logger.error("Couldn't load server list", exception);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void saveServerList() {
/*     */     try {
/*  54 */       NBTTagList nbttaglist = new NBTTagList();
/*     */       
/*  56 */       for (ServerData serverdata : this.servers)
/*     */       {
/*  58 */         nbttaglist.appendTag((NBTBase)serverdata.getNBTCompound());
/*     */       }
/*     */       
/*  61 */       NBTTagCompound nbttagcompound = new NBTTagCompound();
/*  62 */       nbttagcompound.setTag("servers", (NBTBase)nbttaglist);
/*  63 */       CompressedStreamTools.safeWrite(nbttagcompound, new File(this.mc.mcDataDir, "servers.dat"));
/*     */     }
/*  65 */     catch (Exception exception) {
/*     */       
/*  67 */       logger.error("Couldn't save server list", exception);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public ServerData getServerData(int index) {
/*  73 */     return this.servers.get(index);
/*     */   }
/*     */ 
/*     */   
/*     */   public void removeServerData(int index) {
/*  78 */     this.servers.remove(index);
/*     */   }
/*     */ 
/*     */   
/*     */   public void addServerData(ServerData server) {
/*  83 */     this.servers.add(server);
/*     */   }
/*     */ 
/*     */   
/*     */   public int countServers() {
/*  88 */     return this.servers.size();
/*     */   }
/*     */ 
/*     */   
/*     */   public void swapServers(int p_78857_1_, int p_78857_2_) {
/*  93 */     ServerData serverdata = getServerData(p_78857_1_);
/*  94 */     this.servers.set(p_78857_1_, getServerData(p_78857_2_));
/*  95 */     this.servers.set(p_78857_2_, serverdata);
/*  96 */     saveServerList();
/*     */   }
/*     */ 
/*     */   
/*     */   public void func_147413_a(int index, ServerData server) {
/* 101 */     this.servers.set(index, server);
/*     */   }
/*     */ 
/*     */   
/*     */   public static void func_147414_b(ServerData p_147414_0_) {
/* 106 */     ServerList serverlist = new ServerList(Minecraft.getMinecraft());
/* 107 */     serverlist.loadServerList();
/*     */     
/* 109 */     for (int i = 0; i < serverlist.countServers(); i++) {
/*     */       
/* 111 */       ServerData serverdata = serverlist.getServerData(i);
/*     */       
/* 113 */       if (serverdata.serverName.equals(p_147414_0_.serverName) && serverdata.serverIP.equals(p_147414_0_.serverIP)) {
/*     */         
/* 115 */         serverlist.func_147413_a(i, p_147414_0_);
/*     */         
/*     */         break;
/*     */       } 
/*     */     } 
/* 120 */     serverlist.saveServerList();
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\client\multiplayer\ServerList.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */