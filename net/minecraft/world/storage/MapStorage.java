/*     */ package net.minecraft.world.storage;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import com.google.common.collect.Maps;
/*     */ import java.io.DataInputStream;
/*     */ import java.io.DataOutputStream;
/*     */ import java.io.File;
/*     */ import java.io.FileInputStream;
/*     */ import java.io.FileOutputStream;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import net.minecraft.nbt.CompressedStreamTools;
/*     */ import net.minecraft.nbt.NBTBase;
/*     */ import net.minecraft.nbt.NBTTagCompound;
/*     */ import net.minecraft.nbt.NBTTagShort;
/*     */ import net.minecraft.world.WorldSavedData;
/*     */ 
/*     */ public class MapStorage
/*     */ {
/*     */   private ISaveHandler saveHandler;
/*  21 */   protected Map<String, WorldSavedData> loadedDataMap = Maps.newHashMap();
/*  22 */   private List<WorldSavedData> loadedDataList = Lists.newArrayList();
/*  23 */   private Map<String, Short> idCounts = Maps.newHashMap();
/*     */ 
/*     */   
/*     */   public MapStorage(ISaveHandler saveHandlerIn) {
/*  27 */     this.saveHandler = saveHandlerIn;
/*  28 */     loadIdCounts();
/*     */   }
/*     */ 
/*     */   
/*     */   public WorldSavedData loadData(Class<? extends WorldSavedData> clazz, String dataIdentifier) {
/*  33 */     WorldSavedData worldsaveddata = this.loadedDataMap.get(dataIdentifier);
/*     */     
/*  35 */     if (worldsaveddata != null)
/*     */     {
/*  37 */       return worldsaveddata;
/*     */     }
/*     */ 
/*     */     
/*  41 */     if (this.saveHandler != null) {
/*     */       
/*     */       try {
/*     */         
/*  45 */         File file1 = this.saveHandler.getMapFileFromName(dataIdentifier);
/*     */         
/*  47 */         if (file1 != null && file1.exists())
/*     */         {
/*     */           
/*     */           try {
/*  51 */             worldsaveddata = clazz.getConstructor(new Class[] { String.class }).newInstance(new Object[] { dataIdentifier });
/*     */           }
/*  53 */           catch (Exception exception) {
/*     */             
/*  55 */             throw new RuntimeException("Failed to instantiate " + clazz.toString(), exception);
/*     */           } 
/*     */           
/*  58 */           FileInputStream fileinputstream = new FileInputStream(file1);
/*  59 */           NBTTagCompound nbttagcompound = CompressedStreamTools.readCompressed(fileinputstream);
/*  60 */           fileinputstream.close();
/*  61 */           worldsaveddata.readFromNBT(nbttagcompound.getCompoundTag("data"));
/*     */         }
/*     */       
/*  64 */       } catch (Exception exception1) {
/*     */         
/*  66 */         exception1.printStackTrace();
/*     */       } 
/*     */     }
/*     */     
/*  70 */     if (worldsaveddata != null) {
/*     */       
/*  72 */       this.loadedDataMap.put(dataIdentifier, worldsaveddata);
/*  73 */       this.loadedDataList.add(worldsaveddata);
/*     */     } 
/*     */     
/*  76 */     return worldsaveddata;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setData(String dataIdentifier, WorldSavedData data) {
/*  82 */     if (this.loadedDataMap.containsKey(dataIdentifier))
/*     */     {
/*  84 */       this.loadedDataList.remove(this.loadedDataMap.remove(dataIdentifier));
/*     */     }
/*     */     
/*  87 */     this.loadedDataMap.put(dataIdentifier, data);
/*  88 */     this.loadedDataList.add(data);
/*     */   }
/*     */ 
/*     */   
/*     */   public void saveAllData() {
/*  93 */     for (int i = 0; i < this.loadedDataList.size(); i++) {
/*     */       
/*  95 */       WorldSavedData worldsaveddata = this.loadedDataList.get(i);
/*     */       
/*  97 */       if (worldsaveddata.isDirty()) {
/*     */         
/*  99 */         saveData(worldsaveddata);
/* 100 */         worldsaveddata.setDirty(false);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private void saveData(WorldSavedData p_75747_1_) {
/* 107 */     if (this.saveHandler != null) {
/*     */       
/*     */       try {
/*     */         
/* 111 */         File file1 = this.saveHandler.getMapFileFromName(p_75747_1_.mapName);
/*     */         
/* 113 */         if (file1 != null)
/*     */         {
/* 115 */           NBTTagCompound nbttagcompound = new NBTTagCompound();
/* 116 */           p_75747_1_.writeToNBT(nbttagcompound);
/* 117 */           NBTTagCompound nbttagcompound1 = new NBTTagCompound();
/* 118 */           nbttagcompound1.setTag("data", (NBTBase)nbttagcompound);
/* 119 */           FileOutputStream fileoutputstream = new FileOutputStream(file1);
/* 120 */           CompressedStreamTools.writeCompressed(nbttagcompound1, fileoutputstream);
/* 121 */           fileoutputstream.close();
/*     */         }
/*     */       
/* 124 */       } catch (Exception exception) {
/*     */         
/* 126 */         exception.printStackTrace();
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void loadIdCounts() {
/*     */     try {
/* 135 */       this.idCounts.clear();
/*     */       
/* 137 */       if (this.saveHandler == null) {
/*     */         return;
/*     */       }
/*     */ 
/*     */       
/* 142 */       File file1 = this.saveHandler.getMapFileFromName("idcounts");
/*     */       
/* 144 */       if (file1 != null && file1.exists()) {
/*     */         
/* 146 */         DataInputStream datainputstream = new DataInputStream(new FileInputStream(file1));
/* 147 */         NBTTagCompound nbttagcompound = CompressedStreamTools.read(datainputstream);
/* 148 */         datainputstream.close();
/*     */         
/* 150 */         for (String s : nbttagcompound.getKeySet()) {
/*     */           
/* 152 */           NBTBase nbtbase = nbttagcompound.getTag(s);
/*     */           
/* 154 */           if (nbtbase instanceof NBTTagShort)
/*     */           {
/* 156 */             NBTTagShort nbttagshort = (NBTTagShort)nbtbase;
/* 157 */             short short1 = nbttagshort.getShort();
/* 158 */             this.idCounts.put(s, Short.valueOf(short1));
/*     */           }
/*     */         
/*     */         } 
/*     */       } 
/* 163 */     } catch (Exception exception) {
/*     */       
/* 165 */       exception.printStackTrace();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public int getUniqueDataId(String key) {
/* 171 */     Short oshort = this.idCounts.get(key);
/*     */     
/* 173 */     if (oshort == null) {
/*     */       
/* 175 */       oshort = Short.valueOf((short)0);
/*     */     }
/*     */     else {
/*     */       
/* 179 */       oshort = Short.valueOf((short)(oshort.shortValue() + 1));
/*     */     } 
/*     */     
/* 182 */     this.idCounts.put(key, oshort);
/*     */     
/* 184 */     if (this.saveHandler == null)
/*     */     {
/* 186 */       return oshort.shortValue();
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     try {
/* 192 */       File file1 = this.saveHandler.getMapFileFromName("idcounts");
/*     */       
/* 194 */       if (file1 != null)
/*     */       {
/* 196 */         NBTTagCompound nbttagcompound = new NBTTagCompound();
/*     */         
/* 198 */         for (String s : this.idCounts.keySet()) {
/*     */           
/* 200 */           short short1 = ((Short)this.idCounts.get(s)).shortValue();
/* 201 */           nbttagcompound.setShort(s, short1);
/*     */         } 
/*     */         
/* 204 */         DataOutputStream dataoutputstream = new DataOutputStream(new FileOutputStream(file1));
/* 205 */         CompressedStreamTools.write(nbttagcompound, dataoutputstream);
/* 206 */         dataoutputstream.close();
/*     */       }
/*     */     
/* 209 */     } catch (Exception exception) {
/*     */       
/* 211 */       exception.printStackTrace();
/*     */     } 
/*     */     
/* 214 */     return oshort.shortValue();
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\world\storage\MapStorage.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */