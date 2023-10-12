/*    */ package net.minecraft.command;
/*    */ 
/*    */ import net.minecraft.entity.Entity;
/*    */ import net.minecraft.nbt.JsonToNBT;
/*    */ import net.minecraft.nbt.NBTException;
/*    */ import net.minecraft.nbt.NBTTagCompound;
/*    */ 
/*    */ 
/*    */ public class CommandEntityData
/*    */   extends CommandBase
/*    */ {
/*    */   public String getCommandName() {
/* 13 */     return "entitydata";
/*    */   }
/*    */ 
/*    */   
/*    */   public int getRequiredPermissionLevel() {
/* 18 */     return 2;
/*    */   }
/*    */ 
/*    */   
/*    */   public String getCommandUsage(ICommandSender sender) {
/* 23 */     return "commands.entitydata.usage";
/*    */   }
/*    */   
/*    */   public void processCommand(ICommandSender sender, String[] args) throws CommandException {
/*    */     NBTTagCompound nbttagcompound2;
/* 28 */     if (args.length < 2)
/*    */     {
/* 30 */       throw new WrongUsageException("commands.entitydata.usage", new Object[0]);
/*    */     }
/*    */ 
/*    */     
/* 34 */     Entity entity = getEntity(sender, args[0]);
/*    */     
/* 36 */     if (entity instanceof net.minecraft.entity.player.EntityPlayer)
/*    */     {
/* 38 */       throw new CommandException("commands.entitydata.noPlayers", new Object[] { entity.getDisplayName() });
/*    */     }
/*    */ 
/*    */     
/* 42 */     NBTTagCompound nbttagcompound = new NBTTagCompound();
/* 43 */     entity.writeToNBT(nbttagcompound);
/* 44 */     NBTTagCompound nbttagcompound1 = (NBTTagCompound)nbttagcompound.copy();
/*    */ 
/*    */ 
/*    */     
/*    */     try {
/* 49 */       nbttagcompound2 = JsonToNBT.getTagFromJson(getChatComponentFromNthArg(sender, args, 1).getUnformattedText());
/*    */     }
/* 51 */     catch (NBTException nbtexception) {
/*    */       
/* 53 */       throw new CommandException("commands.entitydata.tagError", new Object[] { nbtexception.getMessage() });
/*    */     } 
/*    */     
/* 56 */     nbttagcompound2.removeTag("UUIDMost");
/* 57 */     nbttagcompound2.removeTag("UUIDLeast");
/* 58 */     nbttagcompound.merge(nbttagcompound2);
/*    */     
/* 60 */     if (nbttagcompound.equals(nbttagcompound1))
/*    */     {
/* 62 */       throw new CommandException("commands.entitydata.failed", new Object[] { nbttagcompound.toString() });
/*    */     }
/*    */ 
/*    */     
/* 66 */     entity.readFromNBT(nbttagcompound);
/* 67 */     notifyOperators(sender, this, "commands.entitydata.success", new Object[] { nbttagcompound.toString() });
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean isUsernameIndex(String[] args, int index) {
/* 75 */     return (index == 0);
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\command\CommandEntityData.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */