/*    */ package net.minecraft.command;
/*    */ 
/*    */ import java.util.List;
/*    */ import net.minecraft.entity.player.EntityPlayerMP;
/*    */ import net.minecraft.item.Item;
/*    */ import net.minecraft.nbt.JsonToNBT;
/*    */ import net.minecraft.nbt.NBTException;
/*    */ import net.minecraft.nbt.NBTTagCompound;
/*    */ import net.minecraft.server.MinecraftServer;
/*    */ import net.minecraft.util.BlockPos;
/*    */ import net.minecraft.util.ChatComponentTranslation;
/*    */ import net.minecraft.util.IChatComponent;
/*    */ 
/*    */ public class CommandClearInventory
/*    */   extends CommandBase {
/*    */   public String getCommandName() {
/* 17 */     return "clear";
/*    */   }
/*    */ 
/*    */   
/*    */   public String getCommandUsage(ICommandSender sender) {
/* 22 */     return "commands.clear.usage";
/*    */   }
/*    */ 
/*    */   
/*    */   public int getRequiredPermissionLevel() {
/* 27 */     return 2;
/*    */   }
/*    */ 
/*    */   
/*    */   public void processCommand(ICommandSender sender, String[] args) throws CommandException {
/* 32 */     EntityPlayerMP entityplayermp = (args.length == 0) ? getCommandSenderAsPlayer(sender) : getPlayer(sender, args[0]);
/* 33 */     Item item = (args.length >= 2) ? getItemByText(sender, args[1]) : null;
/* 34 */     int i = (args.length >= 3) ? parseInt(args[2], -1) : -1;
/* 35 */     int j = (args.length >= 4) ? parseInt(args[3], -1) : -1;
/* 36 */     NBTTagCompound nbttagcompound = null;
/*    */     
/* 38 */     if (args.length >= 5) {
/*    */       
/*    */       try {
/*    */         
/* 42 */         nbttagcompound = JsonToNBT.getTagFromJson(buildString(args, 4));
/*    */       }
/* 44 */       catch (NBTException nbtexception) {
/*    */         
/* 46 */         throw new CommandException("commands.clear.tagError", new Object[] { nbtexception.getMessage() });
/*    */       } 
/*    */     }
/*    */     
/* 50 */     if (args.length >= 2 && item == null)
/*    */     {
/* 52 */       throw new CommandException("commands.clear.failure", new Object[] { entityplayermp.getName() });
/*    */     }
/*    */ 
/*    */     
/* 56 */     int k = entityplayermp.inventory.clearMatchingItems(item, i, j, nbttagcompound);
/* 57 */     entityplayermp.inventoryContainer.detectAndSendChanges();
/*    */     
/* 59 */     if (!entityplayermp.capabilities.isCreativeMode)
/*    */     {
/* 61 */       entityplayermp.updateHeldItem();
/*    */     }
/*    */     
/* 64 */     sender.setCommandStat(CommandResultStats.Type.AFFECTED_ITEMS, k);
/*    */     
/* 66 */     if (k == 0)
/*    */     {
/* 68 */       throw new CommandException("commands.clear.failure", new Object[] { entityplayermp.getName() });
/*    */     }
/*    */ 
/*    */     
/* 72 */     if (j == 0) {
/*    */       
/* 74 */       sender.addChatMessage((IChatComponent)new ChatComponentTranslation("commands.clear.testing", new Object[] { entityplayermp.getName(), Integer.valueOf(k) }));
/*    */     }
/*    */     else {
/*    */       
/* 78 */       notifyOperators(sender, this, "commands.clear.success", new Object[] { entityplayermp.getName(), Integer.valueOf(k) });
/*    */     } 
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public List<String> addTabCompletionOptions(ICommandSender sender, String[] args, BlockPos pos) {
/* 86 */     return (args.length == 1) ? getListOfStringsMatchingLastWord(args, func_147209_d()) : ((args.length == 2) ? getListOfStringsMatchingLastWord(args, Item.itemRegistry.getKeys()) : null);
/*    */   }
/*    */ 
/*    */   
/*    */   protected String[] func_147209_d() {
/* 91 */     return MinecraftServer.getServer().getAllUsernames();
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isUsernameIndex(String[] args, int index) {
/* 96 */     return (index == 0);
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\command\CommandClearInventory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */