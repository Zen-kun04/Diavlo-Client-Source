/*     */ package net.minecraft.item;
/*     */ 
/*     */ import java.util.List;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.nbt.NBTBase;
/*     */ import net.minecraft.nbt.NBTTagCompound;
/*     */ import net.minecraft.nbt.NBTTagIntArray;
/*     */ import net.minecraft.util.StatCollector;
/*     */ 
/*     */ 
/*     */ public class ItemFireworkCharge
/*     */   extends Item
/*     */ {
/*     */   public int getColorFromItemStack(ItemStack stack, int renderPass) {
/*  15 */     if (renderPass != 1)
/*     */     {
/*  17 */       return super.getColorFromItemStack(stack, renderPass);
/*     */     }
/*     */ 
/*     */     
/*  21 */     NBTBase nbtbase = getExplosionTag(stack, "Colors");
/*     */     
/*  23 */     if (!(nbtbase instanceof NBTTagIntArray))
/*     */     {
/*  25 */       return 9079434;
/*     */     }
/*     */ 
/*     */     
/*  29 */     NBTTagIntArray nbttagintarray = (NBTTagIntArray)nbtbase;
/*  30 */     int[] aint = nbttagintarray.getIntArray();
/*     */     
/*  32 */     if (aint.length == 1)
/*     */     {
/*  34 */       return aint[0];
/*     */     }
/*     */ 
/*     */     
/*  38 */     int i = 0;
/*  39 */     int j = 0;
/*  40 */     int k = 0;
/*     */     
/*  42 */     for (int l : aint) {
/*     */       
/*  44 */       i += (l & 0xFF0000) >> 16;
/*  45 */       j += (l & 0xFF00) >> 8;
/*  46 */       k += (l & 0xFF) >> 0;
/*     */     } 
/*     */     
/*  49 */     i /= aint.length;
/*  50 */     j /= aint.length;
/*  51 */     k /= aint.length;
/*  52 */     return i << 16 | j << 8 | k;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static NBTBase getExplosionTag(ItemStack stack, String key) {
/*  60 */     if (stack.hasTagCompound()) {
/*     */       
/*  62 */       NBTTagCompound nbttagcompound = stack.getTagCompound().getCompoundTag("Explosion");
/*     */       
/*  64 */       if (nbttagcompound != null)
/*     */       {
/*  66 */         return nbttagcompound.getTag(key);
/*     */       }
/*     */     } 
/*     */     
/*  70 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public void addInformation(ItemStack stack, EntityPlayer playerIn, List<String> tooltip, boolean advanced) {
/*  75 */     if (stack.hasTagCompound()) {
/*     */       
/*  77 */       NBTTagCompound nbttagcompound = stack.getTagCompound().getCompoundTag("Explosion");
/*     */       
/*  79 */       if (nbttagcompound != null)
/*     */       {
/*  81 */         addExplosionInfo(nbttagcompound, tooltip);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public static void addExplosionInfo(NBTTagCompound nbt, List<String> tooltip) {
/*  88 */     byte b0 = nbt.getByte("Type");
/*     */     
/*  90 */     if (b0 >= 0 && b0 <= 4) {
/*     */       
/*  92 */       tooltip.add(StatCollector.translateToLocal("item.fireworksCharge.type." + b0).trim());
/*     */     }
/*     */     else {
/*     */       
/*  96 */       tooltip.add(StatCollector.translateToLocal("item.fireworksCharge.type").trim());
/*     */     } 
/*     */     
/*  99 */     int[] aint = nbt.getIntArray("Colors");
/*     */     
/* 101 */     if (aint.length > 0) {
/*     */       
/* 103 */       boolean flag = true;
/* 104 */       String s = "";
/*     */       
/* 106 */       for (int i : aint) {
/*     */         
/* 108 */         if (!flag)
/*     */         {
/* 110 */           s = s + ", ";
/*     */         }
/*     */         
/* 113 */         flag = false;
/* 114 */         boolean flag1 = false;
/*     */         
/* 116 */         for (int j = 0; j < ItemDye.dyeColors.length; j++) {
/*     */           
/* 118 */           if (i == ItemDye.dyeColors[j]) {
/*     */             
/* 120 */             flag1 = true;
/* 121 */             s = s + StatCollector.translateToLocal("item.fireworksCharge." + EnumDyeColor.byDyeDamage(j).getUnlocalizedName());
/*     */             
/*     */             break;
/*     */           } 
/*     */         } 
/* 126 */         if (!flag1)
/*     */         {
/* 128 */           s = s + StatCollector.translateToLocal("item.fireworksCharge.customColor");
/*     */         }
/*     */       } 
/*     */       
/* 132 */       tooltip.add(s);
/*     */     } 
/*     */     
/* 135 */     int[] aint1 = nbt.getIntArray("FadeColors");
/*     */     
/* 137 */     if (aint1.length > 0) {
/*     */       
/* 139 */       boolean flag2 = true;
/* 140 */       String s1 = StatCollector.translateToLocal("item.fireworksCharge.fadeTo") + " ";
/*     */       
/* 142 */       for (int l : aint1) {
/*     */         
/* 144 */         if (!flag2)
/*     */         {
/* 146 */           s1 = s1 + ", ";
/*     */         }
/*     */         
/* 149 */         flag2 = false;
/* 150 */         boolean flag5 = false;
/*     */         
/* 152 */         for (int k = 0; k < 16; k++) {
/*     */           
/* 154 */           if (l == ItemDye.dyeColors[k]) {
/*     */             
/* 156 */             flag5 = true;
/* 157 */             s1 = s1 + StatCollector.translateToLocal("item.fireworksCharge." + EnumDyeColor.byDyeDamage(k).getUnlocalizedName());
/*     */             
/*     */             break;
/*     */           } 
/*     */         } 
/* 162 */         if (!flag5)
/*     */         {
/* 164 */           s1 = s1 + StatCollector.translateToLocal("item.fireworksCharge.customColor");
/*     */         }
/*     */       } 
/*     */       
/* 168 */       tooltip.add(s1);
/*     */     } 
/*     */     
/* 171 */     boolean flag3 = nbt.getBoolean("Trail");
/*     */     
/* 173 */     if (flag3)
/*     */     {
/* 175 */       tooltip.add(StatCollector.translateToLocal("item.fireworksCharge.trail"));
/*     */     }
/*     */     
/* 178 */     boolean flag4 = nbt.getBoolean("Flicker");
/*     */     
/* 180 */     if (flag4)
/*     */     {
/* 182 */       tooltip.add(StatCollector.translateToLocal("item.fireworksCharge.flicker"));
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\item\ItemFireworkCharge.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */