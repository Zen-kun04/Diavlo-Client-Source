/*     */ package net.minecraft.tileentity;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import java.util.List;
/*     */ import net.minecraft.block.Block;
/*     */ import net.minecraft.block.BlockFlower;
/*     */ import net.minecraft.init.Blocks;
/*     */ import net.minecraft.init.Items;
/*     */ import net.minecraft.item.EnumDyeColor;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import net.minecraft.nbt.NBTBase;
/*     */ import net.minecraft.nbt.NBTTagCompound;
/*     */ import net.minecraft.nbt.NBTTagList;
/*     */ import net.minecraft.network.Packet;
/*     */ import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
/*     */ 
/*     */ public class TileEntityBanner extends TileEntity {
/*     */   private int baseColor;
/*     */   private NBTTagList patterns;
/*     */   private boolean field_175119_g;
/*     */   private List<EnumBannerPattern> patternList;
/*     */   private List<EnumDyeColor> colorList;
/*     */   private String patternResourceLocation;
/*     */   
/*     */   public void setItemValues(ItemStack stack) {
/*  26 */     this.patterns = null;
/*     */     
/*  28 */     if (stack.hasTagCompound() && stack.getTagCompound().hasKey("BlockEntityTag", 10)) {
/*     */       
/*  30 */       NBTTagCompound nbttagcompound = stack.getTagCompound().getCompoundTag("BlockEntityTag");
/*     */       
/*  32 */       if (nbttagcompound.hasKey("Patterns"))
/*     */       {
/*  34 */         this.patterns = (NBTTagList)nbttagcompound.getTagList("Patterns", 10).copy();
/*     */       }
/*     */       
/*  37 */       if (nbttagcompound.hasKey("Base", 99))
/*     */       {
/*  39 */         this.baseColor = nbttagcompound.getInteger("Base");
/*     */       }
/*     */       else
/*     */       {
/*  43 */         this.baseColor = stack.getMetadata() & 0xF;
/*     */       }
/*     */     
/*     */     } else {
/*     */       
/*  48 */       this.baseColor = stack.getMetadata() & 0xF;
/*     */     } 
/*     */     
/*  51 */     this.patternList = null;
/*  52 */     this.colorList = null;
/*  53 */     this.patternResourceLocation = "";
/*  54 */     this.field_175119_g = true;
/*     */   }
/*     */ 
/*     */   
/*     */   public void writeToNBT(NBTTagCompound compound) {
/*  59 */     super.writeToNBT(compound);
/*  60 */     setBaseColorAndPatterns(compound, this.baseColor, this.patterns);
/*     */   }
/*     */ 
/*     */   
/*     */   public static void setBaseColorAndPatterns(NBTTagCompound compound, int baseColorIn, NBTTagList patternsIn) {
/*  65 */     compound.setInteger("Base", baseColorIn);
/*     */     
/*  67 */     if (patternsIn != null)
/*     */     {
/*  69 */       compound.setTag("Patterns", (NBTBase)patternsIn);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void readFromNBT(NBTTagCompound compound) {
/*  75 */     super.readFromNBT(compound);
/*  76 */     this.baseColor = compound.getInteger("Base");
/*  77 */     this.patterns = compound.getTagList("Patterns", 10);
/*  78 */     this.patternList = null;
/*  79 */     this.colorList = null;
/*  80 */     this.patternResourceLocation = null;
/*  81 */     this.field_175119_g = true;
/*     */   }
/*     */ 
/*     */   
/*     */   public Packet getDescriptionPacket() {
/*  86 */     NBTTagCompound nbttagcompound = new NBTTagCompound();
/*  87 */     writeToNBT(nbttagcompound);
/*  88 */     return (Packet)new S35PacketUpdateTileEntity(this.pos, 6, nbttagcompound);
/*     */   }
/*     */ 
/*     */   
/*     */   public int getBaseColor() {
/*  93 */     return this.baseColor;
/*     */   }
/*     */ 
/*     */   
/*     */   public static int getBaseColor(ItemStack stack) {
/*  98 */     NBTTagCompound nbttagcompound = stack.getSubCompound("BlockEntityTag", false);
/*  99 */     return (nbttagcompound != null && nbttagcompound.hasKey("Base")) ? nbttagcompound.getInteger("Base") : stack.getMetadata();
/*     */   }
/*     */ 
/*     */   
/*     */   public static int getPatterns(ItemStack stack) {
/* 104 */     NBTTagCompound nbttagcompound = stack.getSubCompound("BlockEntityTag", false);
/* 105 */     return (nbttagcompound != null && nbttagcompound.hasKey("Patterns")) ? nbttagcompound.getTagList("Patterns", 10).tagCount() : 0;
/*     */   }
/*     */ 
/*     */   
/*     */   public List<EnumBannerPattern> getPatternList() {
/* 110 */     initializeBannerData();
/* 111 */     return this.patternList;
/*     */   }
/*     */ 
/*     */   
/*     */   public NBTTagList getPatterns() {
/* 116 */     return this.patterns;
/*     */   }
/*     */ 
/*     */   
/*     */   public List<EnumDyeColor> getColorList() {
/* 121 */     initializeBannerData();
/* 122 */     return this.colorList;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getPatternResourceLocation() {
/* 127 */     initializeBannerData();
/* 128 */     return this.patternResourceLocation;
/*     */   }
/*     */ 
/*     */   
/*     */   private void initializeBannerData() {
/* 133 */     if (this.patternList == null || this.colorList == null || this.patternResourceLocation == null)
/*     */     {
/* 135 */       if (!this.field_175119_g) {
/*     */         
/* 137 */         this.patternResourceLocation = "";
/*     */       }
/*     */       else {
/*     */         
/* 141 */         this.patternList = Lists.newArrayList();
/* 142 */         this.colorList = Lists.newArrayList();
/* 143 */         this.patternList.add(EnumBannerPattern.BASE);
/* 144 */         this.colorList.add(EnumDyeColor.byDyeDamage(this.baseColor));
/* 145 */         this.patternResourceLocation = "b" + this.baseColor;
/*     */         
/* 147 */         if (this.patterns != null)
/*     */         {
/* 149 */           for (int i = 0; i < this.patterns.tagCount(); i++) {
/*     */             
/* 151 */             NBTTagCompound nbttagcompound = this.patterns.getCompoundTagAt(i);
/* 152 */             EnumBannerPattern tileentitybanner$enumbannerpattern = EnumBannerPattern.getPatternByID(nbttagcompound.getString("Pattern"));
/*     */             
/* 154 */             if (tileentitybanner$enumbannerpattern != null) {
/*     */               
/* 156 */               this.patternList.add(tileentitybanner$enumbannerpattern);
/* 157 */               int j = nbttagcompound.getInteger("Color");
/* 158 */               this.colorList.add(EnumDyeColor.byDyeDamage(j));
/* 159 */               this.patternResourceLocation += tileentitybanner$enumbannerpattern.getPatternID() + j;
/*     */             } 
/*     */           } 
/*     */         }
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public static void removeBannerData(ItemStack stack) {
/* 169 */     NBTTagCompound nbttagcompound = stack.getSubCompound("BlockEntityTag", false);
/*     */     
/* 171 */     if (nbttagcompound != null && nbttagcompound.hasKey("Patterns", 9)) {
/*     */       
/* 173 */       NBTTagList nbttaglist = nbttagcompound.getTagList("Patterns", 10);
/*     */       
/* 175 */       if (nbttaglist.tagCount() > 0) {
/*     */         
/* 177 */         nbttaglist.removeTag(nbttaglist.tagCount() - 1);
/*     */         
/* 179 */         if (nbttaglist.hasNoTags()) {
/*     */           
/* 181 */           stack.getTagCompound().removeTag("BlockEntityTag");
/*     */           
/* 183 */           if (stack.getTagCompound().hasNoTags())
/*     */           {
/* 185 */             stack.setTagCompound((NBTTagCompound)null);
/*     */           }
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   public enum EnumBannerPattern
/*     */   {
/* 194 */     BASE("base", "b"),
/* 195 */     SQUARE_BOTTOM_LEFT("square_bottom_left", "bl", "   ", "   ", "#  "),
/* 196 */     SQUARE_BOTTOM_RIGHT("square_bottom_right", "br", "   ", "   ", "  #"),
/* 197 */     SQUARE_TOP_LEFT("square_top_left", "tl", "#  ", "   ", "   "),
/* 198 */     SQUARE_TOP_RIGHT("square_top_right", "tr", "  #", "   ", "   "),
/* 199 */     STRIPE_BOTTOM("stripe_bottom", "bs", "   ", "   ", "###"),
/* 200 */     STRIPE_TOP("stripe_top", "ts", "###", "   ", "   "),
/* 201 */     STRIPE_LEFT("stripe_left", "ls", "#  ", "#  ", "#  "),
/* 202 */     STRIPE_RIGHT("stripe_right", "rs", "  #", "  #", "  #"),
/* 203 */     STRIPE_CENTER("stripe_center", "cs", " # ", " # ", " # "),
/* 204 */     STRIPE_MIDDLE("stripe_middle", "ms", "   ", "###", "   "),
/* 205 */     STRIPE_DOWNRIGHT("stripe_downright", "drs", "#  ", " # ", "  #"),
/* 206 */     STRIPE_DOWNLEFT("stripe_downleft", "dls", "  #", " # ", "#  "),
/* 207 */     STRIPE_SMALL("small_stripes", "ss", "# #", "# #", "   "),
/* 208 */     CROSS("cross", "cr", "# #", " # ", "# #"),
/* 209 */     STRAIGHT_CROSS("straight_cross", "sc", " # ", "###", " # "),
/* 210 */     TRIANGLE_BOTTOM("triangle_bottom", "bt", "   ", " # ", "# #"),
/* 211 */     TRIANGLE_TOP("triangle_top", "tt", "# #", " # ", "   "),
/* 212 */     TRIANGLES_BOTTOM("triangles_bottom", "bts", "   ", "# #", " # "),
/* 213 */     TRIANGLES_TOP("triangles_top", "tts", " # ", "# #", "   "),
/* 214 */     DIAGONAL_LEFT("diagonal_left", "ld", "## ", "#  ", "   "),
/* 215 */     DIAGONAL_RIGHT("diagonal_up_right", "rd", "   ", "  #", " ##"),
/* 216 */     DIAGONAL_LEFT_MIRROR("diagonal_up_left", "lud", "   ", "#  ", "## "),
/* 217 */     DIAGONAL_RIGHT_MIRROR("diagonal_right", "rud", " ##", "  #", "   "),
/* 218 */     CIRCLE_MIDDLE("circle", "mc", "   ", " # ", "   "),
/* 219 */     RHOMBUS_MIDDLE("rhombus", "mr", " # ", "# #", " # "),
/* 220 */     HALF_VERTICAL("half_vertical", "vh", "## ", "## ", "## "),
/* 221 */     HALF_HORIZONTAL("half_horizontal", "hh", "###", "###", "   "),
/* 222 */     HALF_VERTICAL_MIRROR("half_vertical_right", "vhr", " ##", " ##", " ##"),
/* 223 */     HALF_HORIZONTAL_MIRROR("half_horizontal_bottom", "hhb", "   ", "###", "###"),
/* 224 */     BORDER("border", "bo", "###", "# #", "###"),
/* 225 */     CURLY_BORDER("curly_border", "cbo", (String)new ItemStack(Blocks.vine)),
/* 226 */     CREEPER("creeper", "cre", (String)new ItemStack(Items.skull, 1, 4)),
/* 227 */     GRADIENT("gradient", "gra", "# #", " # ", " # "),
/* 228 */     GRADIENT_UP("gradient_up", "gru", " # ", " # ", "# #"),
/* 229 */     BRICKS("bricks", "bri", (String)new ItemStack(Blocks.brick_block)),
/* 230 */     SKULL("skull", "sku", (String)new ItemStack(Items.skull, 1, 1)),
/* 231 */     FLOWER("flower", "flo", (String)new ItemStack((Block)Blocks.red_flower, 1, BlockFlower.EnumFlowerType.OXEYE_DAISY.getMeta())),
/* 232 */     MOJANG("mojang", "moj", (String)new ItemStack(Items.golden_apple, 1, 1));
/*     */     
/*     */     private String patternName;
/*     */     
/*     */     private String patternID;
/*     */     private String[] craftingLayers;
/*     */     private ItemStack patternCraftingStack;
/*     */     
/*     */     EnumBannerPattern(String name, String id) {
/* 241 */       this.craftingLayers = new String[3];
/* 242 */       this.patternName = name;
/* 243 */       this.patternID = id;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     EnumBannerPattern(String name, String id, ItemStack craftingItem) {
/* 249 */       this.patternCraftingStack = craftingItem;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     EnumBannerPattern(String name, String id, String craftingTop, String craftingMid, String craftingBot) {
/* 255 */       this.craftingLayers[0] = craftingTop;
/* 256 */       this.craftingLayers[1] = craftingMid;
/* 257 */       this.craftingLayers[2] = craftingBot;
/*     */     }
/*     */ 
/*     */     
/*     */     public String getPatternName() {
/* 262 */       return this.patternName;
/*     */     }
/*     */ 
/*     */     
/*     */     public String getPatternID() {
/* 267 */       return this.patternID;
/*     */     }
/*     */ 
/*     */     
/*     */     public String[] getCraftingLayers() {
/* 272 */       return this.craftingLayers;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean hasValidCrafting() {
/* 277 */       return (this.patternCraftingStack != null || this.craftingLayers[0] != null);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean hasCraftingStack() {
/* 282 */       return (this.patternCraftingStack != null);
/*     */     }
/*     */ 
/*     */     
/*     */     public ItemStack getCraftingStack() {
/* 287 */       return this.patternCraftingStack;
/*     */     }
/*     */ 
/*     */     
/*     */     public static EnumBannerPattern getPatternByID(String id) {
/* 292 */       for (EnumBannerPattern tileentitybanner$enumbannerpattern : values()) {
/*     */         
/* 294 */         if (tileentitybanner$enumbannerpattern.patternID.equals(id))
/*     */         {
/* 296 */           return tileentitybanner$enumbannerpattern;
/*     */         }
/*     */       } 
/*     */       
/* 300 */       return null;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\tileentity\TileEntityBanner.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */