/*     */ package net.minecraft.creativetab;
/*     */ 
/*     */ import java.util.List;
/*     */ import net.minecraft.block.Block;
/*     */ import net.minecraft.block.BlockDoublePlant;
/*     */ import net.minecraft.enchantment.Enchantment;
/*     */ import net.minecraft.enchantment.EnchantmentData;
/*     */ import net.minecraft.enchantment.EnumEnchantmentType;
/*     */ import net.minecraft.init.Blocks;
/*     */ import net.minecraft.init.Items;
/*     */ import net.minecraft.item.Item;
/*     */ import net.minecraft.item.ItemStack;
/*     */ 
/*     */ public abstract class CreativeTabs {
/*  15 */   public static final CreativeTabs[] creativeTabArray = new CreativeTabs[12];
/*  16 */   public static final CreativeTabs tabBlock = new CreativeTabs(0, "buildingBlocks")
/*     */     {
/*     */       public Item getTabIconItem()
/*     */       {
/*  20 */         return Item.getItemFromBlock(Blocks.brick_block);
/*     */       }
/*     */     };
/*  23 */   public static final CreativeTabs tabDecorations = new CreativeTabs(1, "decorations")
/*     */     {
/*     */       public Item getTabIconItem()
/*     */       {
/*  27 */         return Item.getItemFromBlock((Block)Blocks.double_plant);
/*     */       }
/*     */       
/*     */       public int getIconItemDamage() {
/*  31 */         return BlockDoublePlant.EnumPlantType.PAEONIA.getMeta();
/*     */       }
/*     */     };
/*  34 */   public static final CreativeTabs tabRedstone = new CreativeTabs(2, "redstone")
/*     */     {
/*     */       public Item getTabIconItem()
/*     */       {
/*  38 */         return Items.redstone;
/*     */       }
/*     */     };
/*  41 */   public static final CreativeTabs tabTransport = new CreativeTabs(3, "transportation")
/*     */     {
/*     */       public Item getTabIconItem()
/*     */       {
/*  45 */         return Item.getItemFromBlock(Blocks.golden_rail);
/*     */       }
/*     */     };
/*  48 */   public static final CreativeTabs tabMisc = (new CreativeTabs(4, "misc")
/*     */     {
/*     */       public Item getTabIconItem()
/*     */       {
/*  52 */         return Items.lava_bucket;
/*     */       }
/*  54 */     }).setRelevantEnchantmentTypes(new EnumEnchantmentType[] { EnumEnchantmentType.ALL });
/*  55 */   public static final CreativeTabs tabAllSearch = (new CreativeTabs(5, "search")
/*     */     {
/*     */       public Item getTabIconItem()
/*     */       {
/*  59 */         return Items.compass;
/*     */       }
/*  61 */     }).setBackgroundImageName("item_search.png");
/*  62 */   public static final CreativeTabs tabFood = new CreativeTabs(6, "food")
/*     */     {
/*     */       public Item getTabIconItem()
/*     */       {
/*  66 */         return Items.apple;
/*     */       }
/*     */     };
/*  69 */   public static final CreativeTabs tabTools = (new CreativeTabs(7, "tools")
/*     */     {
/*     */       public Item getTabIconItem()
/*     */       {
/*  73 */         return Items.iron_axe;
/*     */       }
/*  75 */     }).setRelevantEnchantmentTypes(new EnumEnchantmentType[] { EnumEnchantmentType.DIGGER, EnumEnchantmentType.FISHING_ROD, EnumEnchantmentType.BREAKABLE });
/*  76 */   public static final CreativeTabs tabCombat = (new CreativeTabs(8, "combat")
/*     */     {
/*     */       public Item getTabIconItem()
/*     */       {
/*  80 */         return Items.golden_sword;
/*     */       }
/*  82 */     }).setRelevantEnchantmentTypes(new EnumEnchantmentType[] { EnumEnchantmentType.ARMOR, EnumEnchantmentType.ARMOR_FEET, EnumEnchantmentType.ARMOR_HEAD, EnumEnchantmentType.ARMOR_LEGS, EnumEnchantmentType.ARMOR_TORSO, EnumEnchantmentType.BOW, EnumEnchantmentType.WEAPON });
/*  83 */   public static final CreativeTabs tabBrewing = new CreativeTabs(9, "brewing")
/*     */     {
/*     */       public Item getTabIconItem()
/*     */       {
/*  87 */         return (Item)Items.potionitem;
/*     */       }
/*     */     };
/*  90 */   public static final CreativeTabs tabMaterials = new CreativeTabs(10, "materials")
/*     */     {
/*     */       public Item getTabIconItem()
/*     */       {
/*  94 */         return Items.stick;
/*     */       }
/*     */     };
/*  97 */   public static final CreativeTabs tabInventory = (new CreativeTabs(11, "inventory")
/*     */     {
/*     */       public Item getTabIconItem()
/*     */       {
/* 101 */         return Item.getItemFromBlock((Block)Blocks.chest);
/*     */       }
/* 103 */     }).setBackgroundImageName("inventory.png").setNoScrollbar().setNoTitle();
/*     */   private final int tabIndex;
/*     */   private final String tabLabel;
/* 106 */   private String theTexture = "items.png";
/*     */   
/*     */   private boolean hasScrollbar = true;
/*     */   private boolean drawTitle = true;
/*     */   private EnumEnchantmentType[] enchantmentTypes;
/*     */   private ItemStack iconItemStack;
/*     */   
/*     */   public CreativeTabs(int index, String label) {
/* 114 */     this.tabIndex = index;
/* 115 */     this.tabLabel = label;
/* 116 */     creativeTabArray[index] = this;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getTabIndex() {
/* 121 */     return this.tabIndex;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getTabLabel() {
/* 126 */     return this.tabLabel;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getTranslatedTabLabel() {
/* 131 */     return "itemGroup." + getTabLabel();
/*     */   }
/*     */ 
/*     */   
/*     */   public ItemStack getIconItemStack() {
/* 136 */     if (this.iconItemStack == null)
/*     */     {
/* 138 */       this.iconItemStack = new ItemStack(getTabIconItem(), 1, getIconItemDamage());
/*     */     }
/*     */     
/* 141 */     return this.iconItemStack;
/*     */   }
/*     */ 
/*     */   
/*     */   public abstract Item getTabIconItem();
/*     */   
/*     */   public int getIconItemDamage() {
/* 148 */     return 0;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getBackgroundImageName() {
/* 153 */     return this.theTexture;
/*     */   }
/*     */ 
/*     */   
/*     */   public CreativeTabs setBackgroundImageName(String texture) {
/* 158 */     this.theTexture = texture;
/* 159 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean drawInForegroundOfTab() {
/* 164 */     return this.drawTitle;
/*     */   }
/*     */ 
/*     */   
/*     */   public CreativeTabs setNoTitle() {
/* 169 */     this.drawTitle = false;
/* 170 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean shouldHidePlayerInventory() {
/* 175 */     return this.hasScrollbar;
/*     */   }
/*     */ 
/*     */   
/*     */   public CreativeTabs setNoScrollbar() {
/* 180 */     this.hasScrollbar = false;
/* 181 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getTabColumn() {
/* 186 */     return this.tabIndex % 6;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isTabInFirstRow() {
/* 191 */     return (this.tabIndex < 6);
/*     */   }
/*     */ 
/*     */   
/*     */   public EnumEnchantmentType[] getRelevantEnchantmentTypes() {
/* 196 */     return this.enchantmentTypes;
/*     */   }
/*     */ 
/*     */   
/*     */   public CreativeTabs setRelevantEnchantmentTypes(EnumEnchantmentType... types) {
/* 201 */     this.enchantmentTypes = types;
/* 202 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean hasRelevantEnchantmentType(EnumEnchantmentType enchantmentType) {
/* 207 */     if (this.enchantmentTypes == null)
/*     */     {
/* 209 */       return false;
/*     */     }
/*     */ 
/*     */     
/* 213 */     for (EnumEnchantmentType enumenchantmenttype : this.enchantmentTypes) {
/*     */       
/* 215 */       if (enumenchantmenttype == enchantmentType)
/*     */       {
/* 217 */         return true;
/*     */       }
/*     */     } 
/*     */     
/* 221 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void displayAllReleventItems(List<ItemStack> p_78018_1_) {
/* 227 */     for (Item item : Item.itemRegistry) {
/*     */       
/* 229 */       if (item != null && item.getCreativeTab() == this)
/*     */       {
/* 231 */         item.getSubItems(item, this, p_78018_1_);
/*     */       }
/*     */     } 
/*     */     
/* 235 */     if (getRelevantEnchantmentTypes() != null)
/*     */     {
/* 237 */       addEnchantmentBooksToList(p_78018_1_, getRelevantEnchantmentTypes());
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void addEnchantmentBooksToList(List<ItemStack> itemList, EnumEnchantmentType... enchantmentType) {
/* 243 */     for (Enchantment enchantment : Enchantment.enchantmentsBookList) {
/*     */       
/* 245 */       if (enchantment != null && enchantment.type != null) {
/*     */         
/* 247 */         boolean flag = false;
/*     */         
/* 249 */         for (int i = 0; i < enchantmentType.length && !flag; i++) {
/*     */           
/* 251 */           if (enchantment.type == enchantmentType[i])
/*     */           {
/* 253 */             flag = true;
/*     */           }
/*     */         } 
/*     */         
/* 257 */         if (flag)
/*     */         {
/* 259 */           itemList.add(Items.enchanted_book.getEnchantedItemStack(new EnchantmentData(enchantment, enchantment.getMaxLevel())));
/*     */         }
/*     */       } 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\creativetab\CreativeTabs.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */