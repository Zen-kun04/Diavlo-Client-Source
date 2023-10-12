/*     */ package net.minecraft.item.crafting;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import java.util.List;
/*     */ import net.minecraft.init.Items;
/*     */ import net.minecraft.inventory.InventoryCrafting;
/*     */ import net.minecraft.item.ItemDye;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import net.minecraft.nbt.NBTBase;
/*     */ import net.minecraft.nbt.NBTTagCompound;
/*     */ import net.minecraft.nbt.NBTTagList;
/*     */ import net.minecraft.world.World;
/*     */ 
/*     */ public class RecipeFireworks
/*     */   implements IRecipe
/*     */ {
/*     */   private ItemStack field_92102_a;
/*     */   
/*     */   public boolean matches(InventoryCrafting inv, World worldIn) {
/*  20 */     this.field_92102_a = null;
/*  21 */     int i = 0;
/*  22 */     int j = 0;
/*  23 */     int k = 0;
/*  24 */     int l = 0;
/*  25 */     int i1 = 0;
/*  26 */     int j1 = 0;
/*     */     
/*  28 */     for (int k1 = 0; k1 < inv.getSizeInventory(); k1++) {
/*     */       
/*  30 */       ItemStack itemstack = inv.getStackInSlot(k1);
/*     */       
/*  32 */       if (itemstack != null)
/*     */       {
/*  34 */         if (itemstack.getItem() == Items.gunpowder) {
/*     */           
/*  36 */           j++;
/*     */         }
/*  38 */         else if (itemstack.getItem() == Items.firework_charge) {
/*     */           
/*  40 */           l++;
/*     */         }
/*  42 */         else if (itemstack.getItem() == Items.dye) {
/*     */           
/*  44 */           k++;
/*     */         }
/*  46 */         else if (itemstack.getItem() == Items.paper) {
/*     */           
/*  48 */           i++;
/*     */         }
/*  50 */         else if (itemstack.getItem() == Items.glowstone_dust) {
/*     */           
/*  52 */           i1++;
/*     */         }
/*  54 */         else if (itemstack.getItem() == Items.diamond) {
/*     */           
/*  56 */           i1++;
/*     */         }
/*  58 */         else if (itemstack.getItem() == Items.fire_charge) {
/*     */           
/*  60 */           j1++;
/*     */         }
/*  62 */         else if (itemstack.getItem() == Items.feather) {
/*     */           
/*  64 */           j1++;
/*     */         }
/*  66 */         else if (itemstack.getItem() == Items.gold_nugget) {
/*     */           
/*  68 */           j1++;
/*     */         }
/*     */         else {
/*     */           
/*  72 */           if (itemstack.getItem() != Items.skull)
/*     */           {
/*  74 */             return false;
/*     */           }
/*     */           
/*  77 */           j1++;
/*     */         } 
/*     */       }
/*     */     } 
/*     */     
/*  82 */     i1 = i1 + k + j1;
/*     */     
/*  84 */     if (j <= 3 && i <= 1) {
/*     */       
/*  86 */       if (j >= 1 && i == 1 && i1 == 0) {
/*     */         
/*  88 */         this.field_92102_a = new ItemStack(Items.fireworks);
/*     */         
/*  90 */         if (l > 0) {
/*     */           
/*  92 */           NBTTagCompound nbttagcompound1 = new NBTTagCompound();
/*  93 */           NBTTagCompound nbttagcompound3 = new NBTTagCompound();
/*  94 */           NBTTagList nbttaglist = new NBTTagList();
/*     */           
/*  96 */           for (int k2 = 0; k2 < inv.getSizeInventory(); k2++) {
/*     */             
/*  98 */             ItemStack itemstack3 = inv.getStackInSlot(k2);
/*     */             
/* 100 */             if (itemstack3 != null && itemstack3.getItem() == Items.firework_charge && itemstack3.hasTagCompound() && itemstack3.getTagCompound().hasKey("Explosion", 10))
/*     */             {
/* 102 */               nbttaglist.appendTag((NBTBase)itemstack3.getTagCompound().getCompoundTag("Explosion"));
/*     */             }
/*     */           } 
/*     */           
/* 106 */           nbttagcompound3.setTag("Explosions", (NBTBase)nbttaglist);
/* 107 */           nbttagcompound3.setByte("Flight", (byte)j);
/* 108 */           nbttagcompound1.setTag("Fireworks", (NBTBase)nbttagcompound3);
/* 109 */           this.field_92102_a.setTagCompound(nbttagcompound1);
/*     */         } 
/*     */         
/* 112 */         return true;
/*     */       } 
/* 114 */       if (j == 1 && i == 0 && l == 0 && k > 0 && j1 <= 1) {
/*     */         
/* 116 */         this.field_92102_a = new ItemStack(Items.firework_charge);
/* 117 */         NBTTagCompound nbttagcompound = new NBTTagCompound();
/* 118 */         NBTTagCompound nbttagcompound2 = new NBTTagCompound();
/* 119 */         byte b0 = 0;
/* 120 */         List<Integer> list = Lists.newArrayList();
/*     */         
/* 122 */         for (int l1 = 0; l1 < inv.getSizeInventory(); l1++) {
/*     */           
/* 124 */           ItemStack itemstack2 = inv.getStackInSlot(l1);
/*     */           
/* 126 */           if (itemstack2 != null)
/*     */           {
/* 128 */             if (itemstack2.getItem() == Items.dye) {
/*     */               
/* 130 */               list.add(Integer.valueOf(ItemDye.dyeColors[itemstack2.getMetadata() & 0xF]));
/*     */             }
/* 132 */             else if (itemstack2.getItem() == Items.glowstone_dust) {
/*     */               
/* 134 */               nbttagcompound2.setBoolean("Flicker", true);
/*     */             }
/* 136 */             else if (itemstack2.getItem() == Items.diamond) {
/*     */               
/* 138 */               nbttagcompound2.setBoolean("Trail", true);
/*     */             }
/* 140 */             else if (itemstack2.getItem() == Items.fire_charge) {
/*     */               
/* 142 */               b0 = 1;
/*     */             }
/* 144 */             else if (itemstack2.getItem() == Items.feather) {
/*     */               
/* 146 */               b0 = 4;
/*     */             }
/* 148 */             else if (itemstack2.getItem() == Items.gold_nugget) {
/*     */               
/* 150 */               b0 = 2;
/*     */             }
/* 152 */             else if (itemstack2.getItem() == Items.skull) {
/*     */               
/* 154 */               b0 = 3;
/*     */             } 
/*     */           }
/*     */         } 
/*     */         
/* 159 */         int[] aint1 = new int[list.size()];
/*     */         
/* 161 */         for (int l2 = 0; l2 < aint1.length; l2++)
/*     */         {
/* 163 */           aint1[l2] = ((Integer)list.get(l2)).intValue();
/*     */         }
/*     */         
/* 166 */         nbttagcompound2.setIntArray("Colors", aint1);
/* 167 */         nbttagcompound2.setByte("Type", b0);
/* 168 */         nbttagcompound.setTag("Explosion", (NBTBase)nbttagcompound2);
/* 169 */         this.field_92102_a.setTagCompound(nbttagcompound);
/* 170 */         return true;
/*     */       } 
/* 172 */       if (j == 0 && i == 0 && l == 1 && k > 0 && k == i1) {
/*     */         
/* 174 */         List<Integer> list1 = Lists.newArrayList();
/*     */         
/* 176 */         for (int i2 = 0; i2 < inv.getSizeInventory(); i2++) {
/*     */           
/* 178 */           ItemStack itemstack1 = inv.getStackInSlot(i2);
/*     */           
/* 180 */           if (itemstack1 != null)
/*     */           {
/* 182 */             if (itemstack1.getItem() == Items.dye) {
/*     */               
/* 184 */               list1.add(Integer.valueOf(ItemDye.dyeColors[itemstack1.getMetadata() & 0xF]));
/*     */             }
/* 186 */             else if (itemstack1.getItem() == Items.firework_charge) {
/*     */               
/* 188 */               this.field_92102_a = itemstack1.copy();
/* 189 */               this.field_92102_a.stackSize = 1;
/*     */             } 
/*     */           }
/*     */         } 
/*     */         
/* 194 */         int[] aint = new int[list1.size()];
/*     */         
/* 196 */         for (int j2 = 0; j2 < aint.length; j2++)
/*     */         {
/* 198 */           aint[j2] = ((Integer)list1.get(j2)).intValue();
/*     */         }
/*     */         
/* 201 */         if (this.field_92102_a != null && this.field_92102_a.hasTagCompound()) {
/*     */           
/* 203 */           NBTTagCompound nbttagcompound4 = this.field_92102_a.getTagCompound().getCompoundTag("Explosion");
/*     */           
/* 205 */           if (nbttagcompound4 == null)
/*     */           {
/* 207 */             return false;
/*     */           }
/*     */ 
/*     */           
/* 211 */           nbttagcompound4.setIntArray("FadeColors", aint);
/* 212 */           return true;
/*     */         } 
/*     */ 
/*     */ 
/*     */         
/* 217 */         return false;
/*     */       } 
/*     */ 
/*     */ 
/*     */       
/* 222 */       return false;
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 227 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public ItemStack getCraftingResult(InventoryCrafting inv) {
/* 233 */     return this.field_92102_a.copy();
/*     */   }
/*     */ 
/*     */   
/*     */   public int getRecipeSize() {
/* 238 */     return 10;
/*     */   }
/*     */ 
/*     */   
/*     */   public ItemStack getRecipeOutput() {
/* 243 */     return this.field_92102_a;
/*     */   }
/*     */ 
/*     */   
/*     */   public ItemStack[] getRemainingItems(InventoryCrafting inv) {
/* 248 */     ItemStack[] aitemstack = new ItemStack[inv.getSizeInventory()];
/*     */     
/* 250 */     for (int i = 0; i < aitemstack.length; i++) {
/*     */       
/* 252 */       ItemStack itemstack = inv.getStackInSlot(i);
/*     */       
/* 254 */       if (itemstack != null && itemstack.getItem().hasContainerItem())
/*     */       {
/* 256 */         aitemstack[i] = new ItemStack(itemstack.getItem().getContainerItem());
/*     */       }
/*     */     } 
/*     */     
/* 260 */     return aitemstack;
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\item\crafting\RecipeFireworks.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */