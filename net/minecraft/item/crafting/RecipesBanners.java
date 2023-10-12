/*     */ package net.minecraft.item.crafting;
/*     */ 
/*     */ import net.minecraft.init.Blocks;
/*     */ import net.minecraft.init.Items;
/*     */ import net.minecraft.inventory.InventoryCrafting;
/*     */ import net.minecraft.item.EnumDyeColor;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import net.minecraft.nbt.NBTBase;
/*     */ import net.minecraft.nbt.NBTTagCompound;
/*     */ import net.minecraft.nbt.NBTTagList;
/*     */ import net.minecraft.tileentity.TileEntityBanner;
/*     */ import net.minecraft.world.World;
/*     */ 
/*     */ public class RecipesBanners
/*     */ {
/*     */   void addRecipes(CraftingManager p_179534_1_) {
/*  17 */     for (EnumDyeColor enumdyecolor : EnumDyeColor.values()) {
/*     */       
/*  19 */       p_179534_1_.addRecipe(new ItemStack(Items.banner, 1, enumdyecolor.getDyeDamage()), new Object[] { "###", "###", " | ", Character.valueOf('#'), new ItemStack(Blocks.wool, 1, enumdyecolor.getMetadata()), Character.valueOf('|'), Items.stick });
/*     */     } 
/*     */     
/*  22 */     p_179534_1_.addRecipe(new RecipeDuplicatePattern());
/*  23 */     p_179534_1_.addRecipe(new RecipeAddPattern());
/*     */   }
/*     */ 
/*     */   
/*     */   static class RecipeAddPattern
/*     */     implements IRecipe
/*     */   {
/*     */     private RecipeAddPattern() {}
/*     */ 
/*     */     
/*     */     public boolean matches(InventoryCrafting inv, World worldIn) {
/*  34 */       boolean flag = false;
/*     */       
/*  36 */       for (int i = 0; i < inv.getSizeInventory(); i++) {
/*     */         
/*  38 */         ItemStack itemstack = inv.getStackInSlot(i);
/*     */         
/*  40 */         if (itemstack != null && itemstack.getItem() == Items.banner) {
/*     */           
/*  42 */           if (flag)
/*     */           {
/*  44 */             return false;
/*     */           }
/*     */           
/*  47 */           if (TileEntityBanner.getPatterns(itemstack) >= 6)
/*     */           {
/*  49 */             return false;
/*     */           }
/*     */           
/*  52 */           flag = true;
/*     */         } 
/*     */       } 
/*     */       
/*  56 */       if (!flag)
/*     */       {
/*  58 */         return false;
/*     */       }
/*     */ 
/*     */       
/*  62 */       return (func_179533_c(inv) != null);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public ItemStack getCraftingResult(InventoryCrafting inv) {
/*  68 */       ItemStack itemstack = null;
/*     */       
/*  70 */       for (int i = 0; i < inv.getSizeInventory(); i++) {
/*     */         
/*  72 */         ItemStack itemstack1 = inv.getStackInSlot(i);
/*     */         
/*  74 */         if (itemstack1 != null && itemstack1.getItem() == Items.banner) {
/*     */           
/*  76 */           itemstack = itemstack1.copy();
/*  77 */           itemstack.stackSize = 1;
/*     */           
/*     */           break;
/*     */         } 
/*     */       } 
/*  82 */       TileEntityBanner.EnumBannerPattern tileentitybanner$enumbannerpattern = func_179533_c(inv);
/*     */       
/*  84 */       if (tileentitybanner$enumbannerpattern != null) {
/*     */         
/*  86 */         int k = 0;
/*     */         
/*  88 */         for (int j = 0; j < inv.getSizeInventory(); j++) {
/*     */           
/*  90 */           ItemStack itemstack2 = inv.getStackInSlot(j);
/*     */           
/*  92 */           if (itemstack2 != null && itemstack2.getItem() == Items.dye) {
/*     */             
/*  94 */             k = itemstack2.getMetadata();
/*     */             
/*     */             break;
/*     */           } 
/*     */         } 
/*  99 */         NBTTagCompound nbttagcompound1 = itemstack.getSubCompound("BlockEntityTag", true);
/* 100 */         NBTTagList nbttaglist = null;
/*     */         
/* 102 */         if (nbttagcompound1.hasKey("Patterns", 9)) {
/*     */           
/* 104 */           nbttaglist = nbttagcompound1.getTagList("Patterns", 10);
/*     */         }
/*     */         else {
/*     */           
/* 108 */           nbttaglist = new NBTTagList();
/* 109 */           nbttagcompound1.setTag("Patterns", (NBTBase)nbttaglist);
/*     */         } 
/*     */         
/* 112 */         NBTTagCompound nbttagcompound = new NBTTagCompound();
/* 113 */         nbttagcompound.setString("Pattern", tileentitybanner$enumbannerpattern.getPatternID());
/* 114 */         nbttagcompound.setInteger("Color", k);
/* 115 */         nbttaglist.appendTag((NBTBase)nbttagcompound);
/*     */       } 
/*     */       
/* 118 */       return itemstack;
/*     */     }
/*     */ 
/*     */     
/*     */     public int getRecipeSize() {
/* 123 */       return 10;
/*     */     }
/*     */ 
/*     */     
/*     */     public ItemStack getRecipeOutput() {
/* 128 */       return null;
/*     */     }
/*     */ 
/*     */     
/*     */     public ItemStack[] getRemainingItems(InventoryCrafting inv) {
/* 133 */       ItemStack[] aitemstack = new ItemStack[inv.getSizeInventory()];
/*     */       
/* 135 */       for (int i = 0; i < aitemstack.length; i++) {
/*     */         
/* 137 */         ItemStack itemstack = inv.getStackInSlot(i);
/*     */         
/* 139 */         if (itemstack != null && itemstack.getItem().hasContainerItem())
/*     */         {
/* 141 */           aitemstack[i] = new ItemStack(itemstack.getItem().getContainerItem());
/*     */         }
/*     */       } 
/*     */       
/* 145 */       return aitemstack;
/*     */     }
/*     */ 
/*     */     
/*     */     private TileEntityBanner.EnumBannerPattern func_179533_c(InventoryCrafting p_179533_1_) {
/* 150 */       for (TileEntityBanner.EnumBannerPattern tileentitybanner$enumbannerpattern : TileEntityBanner.EnumBannerPattern.values()) {
/*     */         
/* 152 */         if (tileentitybanner$enumbannerpattern.hasValidCrafting()) {
/*     */           
/* 154 */           boolean flag = true;
/*     */           
/* 156 */           if (tileentitybanner$enumbannerpattern.hasCraftingStack()) {
/*     */             
/* 158 */             boolean flag1 = false;
/* 159 */             boolean flag2 = false;
/*     */             
/* 161 */             for (int i = 0; i < p_179533_1_.getSizeInventory() && flag; i++) {
/*     */               
/* 163 */               ItemStack itemstack = p_179533_1_.getStackInSlot(i);
/*     */               
/* 165 */               if (itemstack != null && itemstack.getItem() != Items.banner)
/*     */               {
/* 167 */                 if (itemstack.getItem() == Items.dye) {
/*     */                   
/* 169 */                   if (flag2) {
/*     */                     
/* 171 */                     flag = false;
/*     */                     
/*     */                     break;
/*     */                   } 
/* 175 */                   flag2 = true;
/*     */                 }
/*     */                 else {
/*     */                   
/* 179 */                   if (flag1 || !itemstack.isItemEqual(tileentitybanner$enumbannerpattern.getCraftingStack())) {
/*     */                     
/* 181 */                     flag = false;
/*     */                     
/*     */                     break;
/*     */                   } 
/* 185 */                   flag1 = true;
/*     */                 } 
/*     */               }
/*     */             } 
/*     */             
/* 190 */             if (!flag1)
/*     */             {
/* 192 */               flag = false;
/*     */             }
/*     */           }
/* 195 */           else if (p_179533_1_.getSizeInventory() == (tileentitybanner$enumbannerpattern.getCraftingLayers()).length * tileentitybanner$enumbannerpattern.getCraftingLayers()[0].length()) {
/*     */             
/* 197 */             int j = -1;
/*     */             
/* 199 */             for (int k = 0; k < p_179533_1_.getSizeInventory() && flag; k++) {
/*     */               
/* 201 */               int l = k / 3;
/* 202 */               int i1 = k % 3;
/* 203 */               ItemStack itemstack1 = p_179533_1_.getStackInSlot(k);
/*     */               
/* 205 */               if (itemstack1 != null && itemstack1.getItem() != Items.banner) {
/*     */                 
/* 207 */                 if (itemstack1.getItem() != Items.dye) {
/*     */                   
/* 209 */                   flag = false;
/*     */                   
/*     */                   break;
/*     */                 } 
/* 213 */                 if (j != -1 && j != itemstack1.getMetadata()) {
/*     */                   
/* 215 */                   flag = false;
/*     */                   
/*     */                   break;
/*     */                 } 
/* 219 */                 if (tileentitybanner$enumbannerpattern.getCraftingLayers()[l].charAt(i1) == ' ') {
/*     */                   
/* 221 */                   flag = false;
/*     */                   
/*     */                   break;
/*     */                 } 
/* 225 */                 j = itemstack1.getMetadata();
/*     */               }
/* 227 */               else if (tileentitybanner$enumbannerpattern.getCraftingLayers()[l].charAt(i1) != ' ') {
/*     */                 
/* 229 */                 flag = false;
/*     */ 
/*     */                 
/*     */                 break;
/*     */               } 
/*     */             } 
/*     */           } else {
/* 236 */             flag = false;
/*     */           } 
/*     */           
/* 239 */           if (flag)
/*     */           {
/* 241 */             return tileentitybanner$enumbannerpattern;
/*     */           }
/*     */         } 
/*     */       } 
/*     */       
/* 246 */       return null;
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   static class RecipeDuplicatePattern
/*     */     implements IRecipe
/*     */   {
/*     */     private RecipeDuplicatePattern() {}
/*     */ 
/*     */     
/*     */     public boolean matches(InventoryCrafting inv, World worldIn) {
/* 258 */       ItemStack itemstack = null;
/* 259 */       ItemStack itemstack1 = null;
/*     */       
/* 261 */       for (int i = 0; i < inv.getSizeInventory(); i++) {
/*     */         
/* 263 */         ItemStack itemstack2 = inv.getStackInSlot(i);
/*     */         
/* 265 */         if (itemstack2 != null) {
/*     */           
/* 267 */           if (itemstack2.getItem() != Items.banner)
/*     */           {
/* 269 */             return false;
/*     */           }
/*     */           
/* 272 */           if (itemstack != null && itemstack1 != null)
/*     */           {
/* 274 */             return false;
/*     */           }
/*     */           
/* 277 */           int j = TileEntityBanner.getBaseColor(itemstack2);
/* 278 */           boolean flag = (TileEntityBanner.getPatterns(itemstack2) > 0);
/*     */           
/* 280 */           if (itemstack != null) {
/*     */             
/* 282 */             if (flag)
/*     */             {
/* 284 */               return false;
/*     */             }
/*     */             
/* 287 */             if (j != TileEntityBanner.getBaseColor(itemstack))
/*     */             {
/* 289 */               return false;
/*     */             }
/*     */             
/* 292 */             itemstack1 = itemstack2;
/*     */           }
/* 294 */           else if (itemstack1 != null) {
/*     */             
/* 296 */             if (!flag)
/*     */             {
/* 298 */               return false;
/*     */             }
/*     */             
/* 301 */             if (j != TileEntityBanner.getBaseColor(itemstack1))
/*     */             {
/* 303 */               return false;
/*     */             }
/*     */             
/* 306 */             itemstack = itemstack2;
/*     */           }
/* 308 */           else if (flag) {
/*     */             
/* 310 */             itemstack = itemstack2;
/*     */           }
/*     */           else {
/*     */             
/* 314 */             itemstack1 = itemstack2;
/*     */           } 
/*     */         } 
/*     */       } 
/*     */       
/* 319 */       return (itemstack != null && itemstack1 != null);
/*     */     }
/*     */ 
/*     */     
/*     */     public ItemStack getCraftingResult(InventoryCrafting inv) {
/* 324 */       for (int i = 0; i < inv.getSizeInventory(); i++) {
/*     */         
/* 326 */         ItemStack itemstack = inv.getStackInSlot(i);
/*     */         
/* 328 */         if (itemstack != null && TileEntityBanner.getPatterns(itemstack) > 0) {
/*     */           
/* 330 */           ItemStack itemstack1 = itemstack.copy();
/* 331 */           itemstack1.stackSize = 1;
/* 332 */           return itemstack1;
/*     */         } 
/*     */       } 
/*     */       
/* 336 */       return null;
/*     */     }
/*     */ 
/*     */     
/*     */     public int getRecipeSize() {
/* 341 */       return 2;
/*     */     }
/*     */ 
/*     */     
/*     */     public ItemStack getRecipeOutput() {
/* 346 */       return null;
/*     */     }
/*     */ 
/*     */     
/*     */     public ItemStack[] getRemainingItems(InventoryCrafting inv) {
/* 351 */       ItemStack[] aitemstack = new ItemStack[inv.getSizeInventory()];
/*     */       
/* 353 */       for (int i = 0; i < aitemstack.length; i++) {
/*     */         
/* 355 */         ItemStack itemstack = inv.getStackInSlot(i);
/*     */         
/* 357 */         if (itemstack != null)
/*     */         {
/* 359 */           if (itemstack.getItem().hasContainerItem()) {
/*     */             
/* 361 */             aitemstack[i] = new ItemStack(itemstack.getItem().getContainerItem());
/*     */           }
/* 363 */           else if (itemstack.hasTagCompound() && TileEntityBanner.getPatterns(itemstack) > 0) {
/*     */             
/* 365 */             aitemstack[i] = itemstack.copy();
/* 366 */             (aitemstack[i]).stackSize = 1;
/*     */           } 
/*     */         }
/*     */       } 
/*     */       
/* 371 */       return aitemstack;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\item\crafting\RecipesBanners.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */