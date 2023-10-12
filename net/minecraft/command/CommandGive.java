/*     */ package net.minecraft.command;
/*     */ 
/*     */ import java.util.List;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.entity.item.EntityItem;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.entity.player.EntityPlayerMP;
/*     */ import net.minecraft.item.Item;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import net.minecraft.nbt.JsonToNBT;
/*     */ import net.minecraft.nbt.NBTException;
/*     */ import net.minecraft.server.MinecraftServer;
/*     */ import net.minecraft.util.BlockPos;
/*     */ 
/*     */ public class CommandGive extends CommandBase {
/*     */   public String getCommandName() {
/*  17 */     return "give";
/*     */   }
/*     */ 
/*     */   
/*     */   public int getRequiredPermissionLevel() {
/*  22 */     return 2;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getCommandUsage(ICommandSender sender) {
/*  27 */     return "commands.give.usage";
/*     */   }
/*     */ 
/*     */   
/*     */   public void processCommand(ICommandSender sender, String[] args) throws CommandException {
/*  32 */     if (args.length < 2)
/*     */     {
/*  34 */       throw new WrongUsageException("commands.give.usage", new Object[0]);
/*     */     }
/*     */ 
/*     */     
/*  38 */     EntityPlayerMP entityPlayerMP = getPlayer(sender, args[0]);
/*  39 */     Item item = getItemByText(sender, args[1]);
/*  40 */     int i = (args.length >= 3) ? parseInt(args[2], 1, 64) : 1;
/*  41 */     int j = (args.length >= 4) ? parseInt(args[3]) : 0;
/*  42 */     ItemStack itemstack = new ItemStack(item, i, j);
/*     */     
/*  44 */     if (args.length >= 5) {
/*     */       
/*  46 */       String s = getChatComponentFromNthArg(sender, args, 4).getUnformattedText();
/*     */ 
/*     */       
/*     */       try {
/*  50 */         itemstack.setTagCompound(JsonToNBT.getTagFromJson(s));
/*     */       }
/*  52 */       catch (NBTException nbtexception) {
/*     */         
/*  54 */         throw new CommandException("commands.give.tagError", new Object[] { nbtexception.getMessage() });
/*     */       } 
/*     */     } 
/*     */     
/*  58 */     boolean flag = ((EntityPlayer)entityPlayerMP).inventory.addItemStackToInventory(itemstack);
/*     */     
/*  60 */     if (flag) {
/*     */       
/*  62 */       ((EntityPlayer)entityPlayerMP).worldObj.playSoundAtEntity((Entity)entityPlayerMP, "random.pop", 0.2F, ((entityPlayerMP.getRNG().nextFloat() - entityPlayerMP.getRNG().nextFloat()) * 0.7F + 1.0F) * 2.0F);
/*  63 */       ((EntityPlayer)entityPlayerMP).inventoryContainer.detectAndSendChanges();
/*     */     } 
/*     */     
/*  66 */     if (flag && itemstack.stackSize <= 0) {
/*     */       
/*  68 */       itemstack.stackSize = 1;
/*  69 */       sender.setCommandStat(CommandResultStats.Type.AFFECTED_ITEMS, i);
/*  70 */       EntityItem entityitem1 = entityPlayerMP.dropPlayerItemWithRandomChoice(itemstack, false);
/*     */       
/*  72 */       if (entityitem1 != null)
/*     */       {
/*  74 */         entityitem1.func_174870_v();
/*     */       }
/*     */     }
/*     */     else {
/*     */       
/*  79 */       sender.setCommandStat(CommandResultStats.Type.AFFECTED_ITEMS, i - itemstack.stackSize);
/*  80 */       EntityItem entityitem = entityPlayerMP.dropPlayerItemWithRandomChoice(itemstack, false);
/*     */       
/*  82 */       if (entityitem != null) {
/*     */         
/*  84 */         entityitem.setNoPickupDelay();
/*  85 */         entityitem.setOwner(entityPlayerMP.getName());
/*     */       } 
/*     */     } 
/*     */     
/*  89 */     notifyOperators(sender, this, "commands.give.success", new Object[] { itemstack.getChatComponent(), Integer.valueOf(i), entityPlayerMP.getName() });
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public List<String> addTabCompletionOptions(ICommandSender sender, String[] args, BlockPos pos) {
/*  95 */     return (args.length == 1) ? getListOfStringsMatchingLastWord(args, getPlayers()) : ((args.length == 2) ? getListOfStringsMatchingLastWord(args, Item.itemRegistry.getKeys()) : null);
/*     */   }
/*     */ 
/*     */   
/*     */   protected String[] getPlayers() {
/* 100 */     return MinecraftServer.getServer().getAllUsernames();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isUsernameIndex(String[] args, int index) {
/* 105 */     return (index == 0);
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\command\CommandGive.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */