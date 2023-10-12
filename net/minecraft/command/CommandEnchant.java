/*     */ package net.minecraft.command;
/*     */ 
/*     */ import java.util.List;
/*     */ import net.minecraft.enchantment.Enchantment;
/*     */ import net.minecraft.entity.player.EntityPlayerMP;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import net.minecraft.nbt.NBTTagList;
/*     */ import net.minecraft.server.MinecraftServer;
/*     */ import net.minecraft.util.BlockPos;
/*     */ 
/*     */ public class CommandEnchant
/*     */   extends CommandBase
/*     */ {
/*     */   public String getCommandName() {
/*  15 */     return "enchant";
/*     */   }
/*     */ 
/*     */   
/*     */   public int getRequiredPermissionLevel() {
/*  20 */     return 2;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getCommandUsage(ICommandSender sender) {
/*  25 */     return "commands.enchant.usage";
/*     */   }
/*     */   
/*     */   public void processCommand(ICommandSender sender, String[] args) throws CommandException {
/*     */     int i;
/*  30 */     if (args.length < 2)
/*     */     {
/*  32 */       throw new WrongUsageException("commands.enchant.usage", new Object[0]);
/*     */     }
/*     */ 
/*     */     
/*  36 */     EntityPlayerMP entityPlayerMP = getPlayer(sender, args[0]);
/*  37 */     sender.setCommandStat(CommandResultStats.Type.AFFECTED_ITEMS, 0);
/*     */ 
/*     */ 
/*     */     
/*     */     try {
/*  42 */       i = parseInt(args[1], 0);
/*     */     }
/*  44 */     catch (NumberInvalidException numberinvalidexception) {
/*     */       
/*  46 */       Enchantment enchantment = Enchantment.getEnchantmentByLocation(args[1]);
/*     */       
/*  48 */       if (enchantment == null)
/*     */       {
/*  50 */         throw numberinvalidexception;
/*     */       }
/*     */       
/*  53 */       i = enchantment.effectId;
/*     */     } 
/*     */     
/*  56 */     int j = 1;
/*  57 */     ItemStack itemstack = entityPlayerMP.getCurrentEquippedItem();
/*     */     
/*  59 */     if (itemstack == null)
/*     */     {
/*  61 */       throw new CommandException("commands.enchant.noItem", new Object[0]);
/*     */     }
/*     */ 
/*     */     
/*  65 */     Enchantment enchantment1 = Enchantment.getEnchantmentById(i);
/*     */     
/*  67 */     if (enchantment1 == null)
/*     */     {
/*  69 */       throw new NumberInvalidException("commands.enchant.notFound", new Object[] { Integer.valueOf(i) });
/*     */     }
/*  71 */     if (!enchantment1.canApply(itemstack))
/*     */     {
/*  73 */       throw new CommandException("commands.enchant.cantEnchant", new Object[0]);
/*     */     }
/*     */ 
/*     */     
/*  77 */     if (args.length >= 3)
/*     */     {
/*  79 */       j = parseInt(args[2], enchantment1.getMinLevel(), enchantment1.getMaxLevel());
/*     */     }
/*     */     
/*  82 */     if (itemstack.hasTagCompound()) {
/*     */       
/*  84 */       NBTTagList nbttaglist = itemstack.getEnchantmentTagList();
/*     */       
/*  86 */       if (nbttaglist != null)
/*     */       {
/*  88 */         for (int k = 0; k < nbttaglist.tagCount(); k++) {
/*     */           
/*  90 */           int l = nbttaglist.getCompoundTagAt(k).getShort("id");
/*     */           
/*  92 */           if (Enchantment.getEnchantmentById(l) != null) {
/*     */             
/*  94 */             Enchantment enchantment2 = Enchantment.getEnchantmentById(l);
/*     */             
/*  96 */             if (!enchantment2.canApplyTogether(enchantment1))
/*     */             {
/*  98 */               throw new CommandException("commands.enchant.cantCombine", new Object[] { enchantment1.getTranslatedName(j), enchantment2.getTranslatedName(nbttaglist.getCompoundTagAt(k).getShort("lvl")) });
/*     */             }
/*     */           } 
/*     */         } 
/*     */       }
/*     */     } 
/*     */     
/* 105 */     itemstack.addEnchantment(enchantment1, j);
/* 106 */     notifyOperators(sender, this, "commands.enchant.success", new Object[0]);
/* 107 */     sender.setCommandStat(CommandResultStats.Type.AFFECTED_ITEMS, 1);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List<String> addTabCompletionOptions(ICommandSender sender, String[] args, BlockPos pos) {
/* 115 */     return (args.length == 1) ? getListOfStringsMatchingLastWord(args, getListOfPlayers()) : ((args.length == 2) ? getListOfStringsMatchingLastWord(args, Enchantment.func_181077_c()) : null);
/*     */   }
/*     */ 
/*     */   
/*     */   protected String[] getListOfPlayers() {
/* 120 */     return MinecraftServer.getServer().getAllUsernames();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isUsernameIndex(String[] args, int index) {
/* 125 */     return (index == 0);
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\command\CommandEnchant.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */