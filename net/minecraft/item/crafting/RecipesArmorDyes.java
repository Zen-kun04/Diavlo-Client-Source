/*     */ package net.minecraft.item.crafting;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import java.util.List;
/*     */ import net.minecraft.entity.passive.EntitySheep;
/*     */ import net.minecraft.init.Items;
/*     */ import net.minecraft.inventory.InventoryCrafting;
/*     */ import net.minecraft.item.EnumDyeColor;
/*     */ import net.minecraft.item.ItemArmor;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import net.minecraft.world.World;
/*     */ 
/*     */ 
/*     */ public class RecipesArmorDyes
/*     */   implements IRecipe
/*     */ {
/*     */   public boolean matches(InventoryCrafting inv, World worldIn) {
/*  18 */     ItemStack itemstack = null;
/*  19 */     List<ItemStack> list = Lists.newArrayList();
/*     */     
/*  21 */     for (int i = 0; i < inv.getSizeInventory(); i++) {
/*     */       
/*  23 */       ItemStack itemstack1 = inv.getStackInSlot(i);
/*     */       
/*  25 */       if (itemstack1 != null)
/*     */       {
/*  27 */         if (itemstack1.getItem() instanceof ItemArmor) {
/*     */           
/*  29 */           ItemArmor itemarmor = (ItemArmor)itemstack1.getItem();
/*     */           
/*  31 */           if (itemarmor.getArmorMaterial() != ItemArmor.ArmorMaterial.LEATHER || itemstack != null)
/*     */           {
/*  33 */             return false;
/*     */           }
/*     */           
/*  36 */           itemstack = itemstack1;
/*     */         }
/*     */         else {
/*     */           
/*  40 */           if (itemstack1.getItem() != Items.dye)
/*     */           {
/*  42 */             return false;
/*     */           }
/*     */           
/*  45 */           list.add(itemstack1);
/*     */         } 
/*     */       }
/*     */     } 
/*     */     
/*  50 */     return (itemstack != null && !list.isEmpty());
/*     */   }
/*     */ 
/*     */   
/*     */   public ItemStack getCraftingResult(InventoryCrafting inv) {
/*  55 */     ItemStack itemstack = null;
/*  56 */     int[] aint = new int[3];
/*  57 */     int i = 0;
/*  58 */     int j = 0;
/*  59 */     ItemArmor itemarmor = null;
/*     */     
/*  61 */     for (int k = 0; k < inv.getSizeInventory(); k++) {
/*     */       
/*  63 */       ItemStack itemstack1 = inv.getStackInSlot(k);
/*     */       
/*  65 */       if (itemstack1 != null)
/*     */       {
/*  67 */         if (itemstack1.getItem() instanceof ItemArmor) {
/*     */           
/*  69 */           itemarmor = (ItemArmor)itemstack1.getItem();
/*     */           
/*  71 */           if (itemarmor.getArmorMaterial() != ItemArmor.ArmorMaterial.LEATHER || itemstack != null)
/*     */           {
/*  73 */             return null;
/*     */           }
/*     */           
/*  76 */           itemstack = itemstack1.copy();
/*  77 */           itemstack.stackSize = 1;
/*     */           
/*  79 */           if (itemarmor.hasColor(itemstack1))
/*     */           {
/*  81 */             int l = itemarmor.getColor(itemstack);
/*  82 */             float f = (l >> 16 & 0xFF) / 255.0F;
/*  83 */             float f1 = (l >> 8 & 0xFF) / 255.0F;
/*  84 */             float f2 = (l & 0xFF) / 255.0F;
/*  85 */             i = (int)(i + Math.max(f, Math.max(f1, f2)) * 255.0F);
/*  86 */             aint[0] = (int)(aint[0] + f * 255.0F);
/*  87 */             aint[1] = (int)(aint[1] + f1 * 255.0F);
/*  88 */             aint[2] = (int)(aint[2] + f2 * 255.0F);
/*  89 */             j++;
/*     */           }
/*     */         
/*     */         } else {
/*     */           
/*  94 */           if (itemstack1.getItem() != Items.dye)
/*     */           {
/*  96 */             return null;
/*     */           }
/*     */           
/*  99 */           float[] afloat = EntitySheep.getDyeRgb(EnumDyeColor.byDyeDamage(itemstack1.getMetadata()));
/* 100 */           int l1 = (int)(afloat[0] * 255.0F);
/* 101 */           int i2 = (int)(afloat[1] * 255.0F);
/* 102 */           int j2 = (int)(afloat[2] * 255.0F);
/* 103 */           i += Math.max(l1, Math.max(i2, j2));
/* 104 */           aint[0] = aint[0] + l1;
/* 105 */           aint[1] = aint[1] + i2;
/* 106 */           aint[2] = aint[2] + j2;
/* 107 */           j++;
/*     */         } 
/*     */       }
/*     */     } 
/*     */     
/* 112 */     if (itemarmor == null)
/*     */     {
/* 114 */       return null;
/*     */     }
/*     */ 
/*     */     
/* 118 */     int i1 = aint[0] / j;
/* 119 */     int j1 = aint[1] / j;
/* 120 */     int k1 = aint[2] / j;
/* 121 */     float f3 = i / j;
/* 122 */     float f4 = Math.max(i1, Math.max(j1, k1));
/* 123 */     i1 = (int)(i1 * f3 / f4);
/* 124 */     j1 = (int)(j1 * f3 / f4);
/* 125 */     k1 = (int)(k1 * f3 / f4);
/* 126 */     int lvt_12_3_ = (i1 << 8) + j1;
/* 127 */     lvt_12_3_ = (lvt_12_3_ << 8) + k1;
/* 128 */     itemarmor.setColor(itemstack, lvt_12_3_);
/* 129 */     return itemstack;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int getRecipeSize() {
/* 135 */     return 10;
/*     */   }
/*     */ 
/*     */   
/*     */   public ItemStack getRecipeOutput() {
/* 140 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public ItemStack[] getRemainingItems(InventoryCrafting inv) {
/* 145 */     ItemStack[] aitemstack = new ItemStack[inv.getSizeInventory()];
/*     */     
/* 147 */     for (int i = 0; i < aitemstack.length; i++) {
/*     */       
/* 149 */       ItemStack itemstack = inv.getStackInSlot(i);
/*     */       
/* 151 */       if (itemstack != null && itemstack.getItem().hasContainerItem())
/*     */       {
/* 153 */         aitemstack[i] = new ItemStack(itemstack.getItem().getContainerItem());
/*     */       }
/*     */     } 
/*     */     
/* 157 */     return aitemstack;
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\item\crafting\RecipesArmorDyes.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */