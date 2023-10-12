/*     */ package net.minecraft.item;
/*     */ import java.util.List;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.inventory.Slot;
/*     */ import net.minecraft.nbt.NBTBase;
/*     */ import net.minecraft.nbt.NBTTagCompound;
/*     */ import net.minecraft.nbt.NBTTagList;
/*     */ import net.minecraft.nbt.NBTTagString;
/*     */ import net.minecraft.network.play.server.S2FPacketSetSlot;
/*     */ import net.minecraft.util.ChatComponentProcessor;
/*     */ import net.minecraft.util.ChatComponentText;
/*     */ import net.minecraft.util.EnumChatFormatting;
/*     */ import net.minecraft.util.IChatComponent;
/*     */ import net.minecraft.util.StatCollector;
/*     */ import net.minecraft.util.StringUtils;
/*     */ import net.minecraft.world.World;
/*     */ 
/*     */ public class ItemEditableBook extends Item {
/*     */   public ItemEditableBook() {
/*  20 */     setMaxStackSize(1);
/*     */   }
/*     */ 
/*     */   
/*     */   public static boolean validBookTagContents(NBTTagCompound nbt) {
/*  25 */     if (!ItemWritableBook.isNBTValid(nbt))
/*     */     {
/*  27 */       return false;
/*     */     }
/*  29 */     if (!nbt.hasKey("title", 8))
/*     */     {
/*  31 */       return false;
/*     */     }
/*     */ 
/*     */     
/*  35 */     String s = nbt.getString("title");
/*  36 */     return (s != null && s.length() <= 32) ? nbt.hasKey("author", 8) : false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static int getGeneration(ItemStack book) {
/*  42 */     return book.getTagCompound().getInteger("generation");
/*     */   }
/*     */ 
/*     */   
/*     */   public String getItemStackDisplayName(ItemStack stack) {
/*  47 */     if (stack.hasTagCompound()) {
/*     */       
/*  49 */       NBTTagCompound nbttagcompound = stack.getTagCompound();
/*  50 */       String s = nbttagcompound.getString("title");
/*     */       
/*  52 */       if (!StringUtils.isNullOrEmpty(s))
/*     */       {
/*  54 */         return s;
/*     */       }
/*     */     } 
/*     */     
/*  58 */     return super.getItemStackDisplayName(stack);
/*     */   }
/*     */ 
/*     */   
/*     */   public void addInformation(ItemStack stack, EntityPlayer playerIn, List<String> tooltip, boolean advanced) {
/*  63 */     if (stack.hasTagCompound()) {
/*     */       
/*  65 */       NBTTagCompound nbttagcompound = stack.getTagCompound();
/*  66 */       String s = nbttagcompound.getString("author");
/*     */       
/*  68 */       if (!StringUtils.isNullOrEmpty(s))
/*     */       {
/*  70 */         tooltip.add(EnumChatFormatting.GRAY + StatCollector.translateToLocalFormatted("book.byAuthor", new Object[] { s }));
/*     */       }
/*     */       
/*  73 */       tooltip.add(EnumChatFormatting.GRAY + StatCollector.translateToLocal("book.generation." + nbttagcompound.getInteger("generation")));
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public ItemStack onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn) {
/*  79 */     if (!worldIn.isRemote)
/*     */     {
/*  81 */       resolveContents(itemStackIn, playerIn);
/*     */     }
/*     */     
/*  84 */     playerIn.displayGUIBook(itemStackIn);
/*  85 */     playerIn.triggerAchievement(StatList.objectUseStats[Item.getIdFromItem(this)]);
/*  86 */     return itemStackIn;
/*     */   }
/*     */ 
/*     */   
/*     */   private void resolveContents(ItemStack stack, EntityPlayer player) {
/*  91 */     if (stack != null && stack.getTagCompound() != null) {
/*     */       
/*  93 */       NBTTagCompound nbttagcompound = stack.getTagCompound();
/*     */       
/*  95 */       if (!nbttagcompound.getBoolean("resolved")) {
/*     */         
/*  97 */         nbttagcompound.setBoolean("resolved", true);
/*     */         
/*  99 */         if (validBookTagContents(nbttagcompound)) {
/*     */           
/* 101 */           NBTTagList nbttaglist = nbttagcompound.getTagList("pages", 8);
/*     */           
/* 103 */           for (int i = 0; i < nbttaglist.tagCount(); i++) {
/*     */             ChatComponentText chatComponentText;
/* 105 */             String s = nbttaglist.getStringTagAt(i);
/*     */ 
/*     */ 
/*     */             
/*     */             try {
/* 110 */               IChatComponent ichatcomponent = IChatComponent.Serializer.jsonToComponent(s);
/* 111 */               ichatcomponent = ChatComponentProcessor.processComponent((ICommandSender)player, ichatcomponent, (Entity)player);
/*     */             }
/* 113 */             catch (Exception var9) {
/*     */               
/* 115 */               chatComponentText = new ChatComponentText(s);
/*     */             } 
/*     */             
/* 118 */             nbttaglist.set(i, (NBTBase)new NBTTagString(IChatComponent.Serializer.componentToJson((IChatComponent)chatComponentText)));
/*     */           } 
/*     */           
/* 121 */           nbttagcompound.setTag("pages", (NBTBase)nbttaglist);
/*     */           
/* 123 */           if (player instanceof EntityPlayerMP && player.getCurrentEquippedItem() == stack) {
/*     */             
/* 125 */             Slot slot = player.openContainer.getSlotFromInventory((IInventory)player.inventory, player.inventory.currentItem);
/* 126 */             ((EntityPlayerMP)player).playerNetServerHandler.sendPacket((Packet)new S2FPacketSetSlot(0, slot.slotNumber, stack));
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean hasEffect(ItemStack stack) {
/* 135 */     return true;
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\item\ItemEditableBook.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */