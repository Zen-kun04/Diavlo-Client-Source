/*     */ package net.minecraft.nbt;
/*     */ 
/*     */ import com.mojang.authlib.GameProfile;
/*     */ import com.mojang.authlib.properties.Property;
/*     */ import java.util.UUID;
/*     */ import net.minecraft.util.StringUtils;
/*     */ 
/*     */ 
/*     */ public final class NBTUtil
/*     */ {
/*     */   public static GameProfile readGameProfileFromNBT(NBTTagCompound compound) {
/*     */     UUID uuid;
/*  13 */     String s = null;
/*  14 */     String s1 = null;
/*     */     
/*  16 */     if (compound.hasKey("Name", 8))
/*     */     {
/*  18 */       s = compound.getString("Name");
/*     */     }
/*     */     
/*  21 */     if (compound.hasKey("Id", 8))
/*     */     {
/*  23 */       s1 = compound.getString("Id");
/*     */     }
/*     */     
/*  26 */     if (StringUtils.isNullOrEmpty(s) && StringUtils.isNullOrEmpty(s1))
/*     */     {
/*  28 */       return null;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     try {
/*  36 */       uuid = UUID.fromString(s1);
/*     */     }
/*  38 */     catch (Throwable var12) {
/*     */       
/*  40 */       uuid = null;
/*     */     } 
/*     */     
/*  43 */     GameProfile gameprofile = new GameProfile(uuid, s);
/*     */     
/*  45 */     if (compound.hasKey("Properties", 10)) {
/*     */       
/*  47 */       NBTTagCompound nbttagcompound = compound.getCompoundTag("Properties");
/*     */       
/*  49 */       for (String s2 : nbttagcompound.getKeySet()) {
/*     */         
/*  51 */         NBTTagList nbttaglist = nbttagcompound.getTagList(s2, 10);
/*     */         
/*  53 */         for (int i = 0; i < nbttaglist.tagCount(); i++) {
/*     */           
/*  55 */           NBTTagCompound nbttagcompound1 = nbttaglist.getCompoundTagAt(i);
/*  56 */           String s3 = nbttagcompound1.getString("Value");
/*     */           
/*  58 */           if (nbttagcompound1.hasKey("Signature", 8)) {
/*     */             
/*  60 */             gameprofile.getProperties().put(s2, new Property(s2, s3, nbttagcompound1.getString("Signature")));
/*     */           }
/*     */           else {
/*     */             
/*  64 */             gameprofile.getProperties().put(s2, new Property(s2, s3));
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/*  70 */     return gameprofile;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static NBTTagCompound writeGameProfile(NBTTagCompound tagCompound, GameProfile profile) {
/*  76 */     if (!StringUtils.isNullOrEmpty(profile.getName()))
/*     */     {
/*  78 */       tagCompound.setString("Name", profile.getName());
/*     */     }
/*     */     
/*  81 */     if (profile.getId() != null)
/*     */     {
/*  83 */       tagCompound.setString("Id", profile.getId().toString());
/*     */     }
/*     */     
/*  86 */     if (!profile.getProperties().isEmpty()) {
/*     */       
/*  88 */       NBTTagCompound nbttagcompound = new NBTTagCompound();
/*     */       
/*  90 */       for (String s : profile.getProperties().keySet()) {
/*     */         
/*  92 */         NBTTagList nbttaglist = new NBTTagList();
/*     */         
/*  94 */         for (Property property : profile.getProperties().get(s)) {
/*     */           
/*  96 */           NBTTagCompound nbttagcompound1 = new NBTTagCompound();
/*  97 */           nbttagcompound1.setString("Value", property.getValue());
/*     */           
/*  99 */           if (property.hasSignature())
/*     */           {
/* 101 */             nbttagcompound1.setString("Signature", property.getSignature());
/*     */           }
/*     */           
/* 104 */           nbttaglist.appendTag(nbttagcompound1);
/*     */         } 
/*     */         
/* 107 */         nbttagcompound.setTag(s, nbttaglist);
/*     */       } 
/*     */       
/* 110 */       tagCompound.setTag("Properties", nbttagcompound);
/*     */     } 
/*     */     
/* 113 */     return tagCompound;
/*     */   }
/*     */ 
/*     */   
/*     */   public static boolean func_181123_a(NBTBase p_181123_0_, NBTBase p_181123_1_, boolean p_181123_2_) {
/* 118 */     if (p_181123_0_ == p_181123_1_)
/*     */     {
/* 120 */       return true;
/*     */     }
/* 122 */     if (p_181123_0_ == null)
/*     */     {
/* 124 */       return true;
/*     */     }
/* 126 */     if (p_181123_1_ == null)
/*     */     {
/* 128 */       return false;
/*     */     }
/* 130 */     if (!p_181123_0_.getClass().equals(p_181123_1_.getClass()))
/*     */     {
/* 132 */       return false;
/*     */     }
/* 134 */     if (p_181123_0_ instanceof NBTTagCompound) {
/*     */       
/* 136 */       NBTTagCompound nbttagcompound = (NBTTagCompound)p_181123_0_;
/* 137 */       NBTTagCompound nbttagcompound1 = (NBTTagCompound)p_181123_1_;
/*     */       
/* 139 */       for (String s : nbttagcompound.getKeySet()) {
/*     */         
/* 141 */         NBTBase nbtbase1 = nbttagcompound.getTag(s);
/*     */         
/* 143 */         if (!func_181123_a(nbtbase1, nbttagcompound1.getTag(s), p_181123_2_))
/*     */         {
/* 145 */           return false;
/*     */         }
/*     */       } 
/*     */       
/* 149 */       return true;
/*     */     } 
/* 151 */     if (p_181123_0_ instanceof NBTTagList && p_181123_2_) {
/*     */       
/* 153 */       NBTTagList nbttaglist = (NBTTagList)p_181123_0_;
/* 154 */       NBTTagList nbttaglist1 = (NBTTagList)p_181123_1_;
/*     */       
/* 156 */       if (nbttaglist.tagCount() == 0)
/*     */       {
/* 158 */         return (nbttaglist1.tagCount() == 0);
/*     */       }
/*     */ 
/*     */       
/* 162 */       for (int i = 0; i < nbttaglist.tagCount(); i++) {
/*     */         
/* 164 */         NBTBase nbtbase = nbttaglist.get(i);
/* 165 */         boolean flag = false;
/*     */         
/* 167 */         for (int j = 0; j < nbttaglist1.tagCount(); j++) {
/*     */           
/* 169 */           if (func_181123_a(nbtbase, nbttaglist1.get(j), p_181123_2_)) {
/*     */             
/* 171 */             flag = true;
/*     */             
/*     */             break;
/*     */           } 
/*     */         } 
/* 176 */         if (!flag)
/*     */         {
/* 178 */           return false;
/*     */         }
/*     */       } 
/*     */       
/* 182 */       return true;
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 187 */     return p_181123_0_.equals(p_181123_1_);
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\nbt\NBTUtil.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */