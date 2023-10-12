/*     */ package net.minecraft.command;
/*     */ 
/*     */ import com.google.common.collect.Maps;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import net.minecraft.block.Block;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.init.Blocks;
/*     */ import net.minecraft.inventory.IInventory;
/*     */ import net.minecraft.item.Item;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import net.minecraft.nbt.JsonToNBT;
/*     */ import net.minecraft.nbt.NBTException;
/*     */ import net.minecraft.server.MinecraftServer;
/*     */ import net.minecraft.tileentity.TileEntity;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.world.World;
/*     */ 
/*     */ public class CommandReplaceItem
/*     */   extends CommandBase {
/*  22 */   private static final Map<String, Integer> SHORTCUTS = Maps.newHashMap();
/*     */ 
/*     */   
/*     */   public String getCommandName() {
/*  26 */     return "replaceitem";
/*     */   }
/*     */ 
/*     */   
/*     */   public int getRequiredPermissionLevel() {
/*  31 */     return 2;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getCommandUsage(ICommandSender sender) {
/*  36 */     return "commands.replaceitem.usage";
/*     */   } public void processCommand(ICommandSender sender, String[] args) throws CommandException {
/*     */     boolean flag;
/*     */     int i;
/*     */     Item item;
/*  41 */     if (args.length < 1)
/*     */     {
/*  43 */       throw new WrongUsageException("commands.replaceitem.usage", new Object[0]);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  49 */     if (args[0].equals("entity")) {
/*     */       
/*  51 */       flag = false;
/*     */     }
/*     */     else {
/*     */       
/*  55 */       if (!args[0].equals("block"))
/*     */       {
/*  57 */         throw new WrongUsageException("commands.replaceitem.usage", new Object[0]);
/*     */       }
/*     */       
/*  60 */       flag = true;
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/*  65 */     if (flag) {
/*     */       
/*  67 */       if (args.length < 6)
/*     */       {
/*  69 */         throw new WrongUsageException("commands.replaceitem.block.usage", new Object[0]);
/*     */       }
/*     */       
/*  72 */       i = 4;
/*     */     }
/*     */     else {
/*     */       
/*  76 */       if (args.length < 4)
/*     */       {
/*  78 */         throw new WrongUsageException("commands.replaceitem.entity.usage", new Object[0]);
/*     */       }
/*     */       
/*  81 */       i = 2;
/*     */     } 
/*     */     
/*  84 */     int j = getSlotForShortcut(args[i++]);
/*     */ 
/*     */ 
/*     */     
/*     */     try {
/*  89 */       item = getItemByText(sender, args[i]);
/*     */     }
/*  91 */     catch (NumberInvalidException numberinvalidexception) {
/*     */       
/*  93 */       if (Block.getBlockFromName(args[i]) != Blocks.air)
/*     */       {
/*  95 */         throw numberinvalidexception;
/*     */       }
/*     */       
/*  98 */       item = null;
/*     */     } 
/*     */     
/* 101 */     i++;
/* 102 */     int k = (args.length > i) ? parseInt(args[i++], 1, 64) : 1;
/* 103 */     int l = (args.length > i) ? parseInt(args[i++]) : 0;
/* 104 */     ItemStack itemstack = new ItemStack(item, k, l);
/*     */     
/* 106 */     if (args.length > i) {
/*     */       
/* 108 */       String s = getChatComponentFromNthArg(sender, args, i).getUnformattedText();
/*     */ 
/*     */       
/*     */       try {
/* 112 */         itemstack.setTagCompound(JsonToNBT.getTagFromJson(s));
/*     */       }
/* 114 */       catch (NBTException nbtexception) {
/*     */         
/* 116 */         throw new CommandException("commands.replaceitem.tagError", new Object[] { nbtexception.getMessage() });
/*     */       } 
/*     */     } 
/*     */     
/* 120 */     if (itemstack.getItem() == null)
/*     */     {
/* 122 */       itemstack = null;
/*     */     }
/*     */     
/* 125 */     if (flag) {
/*     */       
/* 127 */       sender.setCommandStat(CommandResultStats.Type.AFFECTED_ITEMS, 0);
/* 128 */       BlockPos blockpos = parseBlockPos(sender, args, 1, false);
/* 129 */       World world = sender.getEntityWorld();
/* 130 */       TileEntity tileentity = world.getTileEntity(blockpos);
/*     */       
/* 132 */       if (tileentity == null || !(tileentity instanceof IInventory))
/*     */       {
/* 134 */         throw new CommandException("commands.replaceitem.noContainer", new Object[] { Integer.valueOf(blockpos.getX()), Integer.valueOf(blockpos.getY()), Integer.valueOf(blockpos.getZ()) });
/*     */       }
/*     */       
/* 137 */       IInventory iinventory = (IInventory)tileentity;
/*     */       
/* 139 */       if (j >= 0 && j < iinventory.getSizeInventory())
/*     */       {
/* 141 */         iinventory.setInventorySlotContents(j, itemstack);
/*     */       }
/*     */     }
/*     */     else {
/*     */       
/* 146 */       Entity entity = getEntity(sender, args[1]);
/* 147 */       sender.setCommandStat(CommandResultStats.Type.AFFECTED_ITEMS, 0);
/*     */       
/* 149 */       if (entity instanceof EntityPlayer)
/*     */       {
/* 151 */         ((EntityPlayer)entity).inventoryContainer.detectAndSendChanges();
/*     */       }
/*     */       
/* 154 */       if (!entity.replaceItemInInventory(j, itemstack))
/*     */       {
/* 156 */         throw new CommandException("commands.replaceitem.failed", new Object[] { Integer.valueOf(j), Integer.valueOf(k), (itemstack == null) ? "Air" : itemstack.getChatComponent() });
/*     */       }
/*     */       
/* 159 */       if (entity instanceof EntityPlayer)
/*     */       {
/* 161 */         ((EntityPlayer)entity).inventoryContainer.detectAndSendChanges();
/*     */       }
/*     */     } 
/*     */     
/* 165 */     sender.setCommandStat(CommandResultStats.Type.AFFECTED_ITEMS, k);
/* 166 */     notifyOperators(sender, this, "commands.replaceitem.success", new Object[] { Integer.valueOf(j), Integer.valueOf(k), (itemstack == null) ? "Air" : itemstack.getChatComponent() });
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private int getSlotForShortcut(String shortcut) throws CommandException {
/* 172 */     if (!SHORTCUTS.containsKey(shortcut))
/*     */     {
/* 174 */       throw new CommandException("commands.generic.parameter.invalid", new Object[] { shortcut });
/*     */     }
/*     */ 
/*     */     
/* 178 */     return ((Integer)SHORTCUTS.get(shortcut)).intValue();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public List<String> addTabCompletionOptions(ICommandSender sender, String[] args, BlockPos pos) {
/* 184 */     return (args.length == 1) ? getListOfStringsMatchingLastWord(args, new String[] { "entity", "block" }) : ((args.length == 2 && args[0].equals("entity")) ? getListOfStringsMatchingLastWord(args, getUsernames()) : ((args.length >= 2 && args.length <= 4 && args[0].equals("block")) ? func_175771_a(args, 1, pos) : (((args.length != 3 || !args[0].equals("entity")) && (args.length != 5 || !args[0].equals("block"))) ? (((args.length != 4 || !args[0].equals("entity")) && (args.length != 6 || !args[0].equals("block"))) ? null : getListOfStringsMatchingLastWord(args, Item.itemRegistry.getKeys())) : getListOfStringsMatchingLastWord(args, SHORTCUTS.keySet()))));
/*     */   }
/*     */ 
/*     */   
/*     */   protected String[] getUsernames() {
/* 189 */     return MinecraftServer.getServer().getAllUsernames();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isUsernameIndex(String[] args, int index) {
/* 194 */     return (args.length > 0 && args[0].equals("entity") && index == 1);
/*     */   }
/*     */ 
/*     */   
/*     */   static {
/* 199 */     for (int i = 0; i < 54; i++)
/*     */     {
/* 201 */       SHORTCUTS.put("slot.container." + i, Integer.valueOf(i));
/*     */     }
/*     */     
/* 204 */     for (int j = 0; j < 9; j++)
/*     */     {
/* 206 */       SHORTCUTS.put("slot.hotbar." + j, Integer.valueOf(j));
/*     */     }
/*     */     
/* 209 */     for (int k = 0; k < 27; k++)
/*     */     {
/* 211 */       SHORTCUTS.put("slot.inventory." + k, Integer.valueOf(9 + k));
/*     */     }
/*     */     
/* 214 */     for (int l = 0; l < 27; l++)
/*     */     {
/* 216 */       SHORTCUTS.put("slot.enderchest." + l, Integer.valueOf(200 + l));
/*     */     }
/*     */     
/* 219 */     for (int i1 = 0; i1 < 8; i1++)
/*     */     {
/* 221 */       SHORTCUTS.put("slot.villager." + i1, Integer.valueOf(300 + i1));
/*     */     }
/*     */     
/* 224 */     for (int j1 = 0; j1 < 15; j1++)
/*     */     {
/* 226 */       SHORTCUTS.put("slot.horse." + j1, Integer.valueOf(500 + j1));
/*     */     }
/*     */     
/* 229 */     SHORTCUTS.put("slot.weapon", Integer.valueOf(99));
/* 230 */     SHORTCUTS.put("slot.armor.head", Integer.valueOf(103));
/* 231 */     SHORTCUTS.put("slot.armor.chest", Integer.valueOf(102));
/* 232 */     SHORTCUTS.put("slot.armor.legs", Integer.valueOf(101));
/* 233 */     SHORTCUTS.put("slot.armor.feet", Integer.valueOf(100));
/* 234 */     SHORTCUTS.put("slot.horse.saddle", Integer.valueOf(400));
/* 235 */     SHORTCUTS.put("slot.horse.armor", Integer.valueOf(401));
/* 236 */     SHORTCUTS.put("slot.horse.chest", Integer.valueOf(499));
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\command\CommandReplaceItem.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */