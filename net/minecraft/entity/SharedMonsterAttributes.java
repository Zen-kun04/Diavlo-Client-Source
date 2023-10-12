/*     */ package net.minecraft.entity;
/*     */ 
/*     */ import java.util.Collection;
/*     */ import java.util.UUID;
/*     */ import net.minecraft.entity.ai.attributes.AttributeModifier;
/*     */ import net.minecraft.entity.ai.attributes.BaseAttributeMap;
/*     */ import net.minecraft.entity.ai.attributes.IAttribute;
/*     */ import net.minecraft.entity.ai.attributes.IAttributeInstance;
/*     */ import net.minecraft.entity.ai.attributes.RangedAttribute;
/*     */ import net.minecraft.nbt.NBTBase;
/*     */ import net.minecraft.nbt.NBTTagCompound;
/*     */ import net.minecraft.nbt.NBTTagList;
/*     */ import org.apache.logging.log4j.LogManager;
/*     */ import org.apache.logging.log4j.Logger;
/*     */ 
/*     */ public class SharedMonsterAttributes {
/*  17 */   private static final Logger logger = LogManager.getLogger();
/*  18 */   public static final IAttribute maxHealth = (IAttribute)(new RangedAttribute((IAttribute)null, "generic.maxHealth", 20.0D, 0.0D, 1024.0D)).setDescription("Max Health").setShouldWatch(true);
/*  19 */   public static final IAttribute followRange = (IAttribute)(new RangedAttribute((IAttribute)null, "generic.followRange", 32.0D, 0.0D, 2048.0D)).setDescription("Follow Range");
/*  20 */   public static final IAttribute knockbackResistance = (IAttribute)(new RangedAttribute((IAttribute)null, "generic.knockbackResistance", 0.0D, 0.0D, 1.0D)).setDescription("Knockback Resistance");
/*  21 */   public static final IAttribute movementSpeed = (IAttribute)(new RangedAttribute((IAttribute)null, "generic.movementSpeed", 0.699999988079071D, 0.0D, 1024.0D)).setDescription("Movement Speed").setShouldWatch(true);
/*  22 */   public static final IAttribute attackDamage = (IAttribute)new RangedAttribute((IAttribute)null, "generic.attackDamage", 2.0D, 0.0D, 2048.0D);
/*     */ 
/*     */   
/*     */   public static NBTTagList writeBaseAttributeMapToNBT(BaseAttributeMap map) {
/*  26 */     NBTTagList nbttaglist = new NBTTagList();
/*     */     
/*  28 */     for (IAttributeInstance iattributeinstance : map.getAllAttributes())
/*     */     {
/*  30 */       nbttaglist.appendTag((NBTBase)writeAttributeInstanceToNBT(iattributeinstance));
/*     */     }
/*     */     
/*  33 */     return nbttaglist;
/*     */   }
/*     */ 
/*     */   
/*     */   private static NBTTagCompound writeAttributeInstanceToNBT(IAttributeInstance instance) {
/*  38 */     NBTTagCompound nbttagcompound = new NBTTagCompound();
/*  39 */     IAttribute iattribute = instance.getAttribute();
/*  40 */     nbttagcompound.setString("Name", iattribute.getAttributeUnlocalizedName());
/*  41 */     nbttagcompound.setDouble("Base", instance.getBaseValue());
/*  42 */     Collection<AttributeModifier> collection = instance.func_111122_c();
/*     */     
/*  44 */     if (collection != null && !collection.isEmpty()) {
/*     */       
/*  46 */       NBTTagList nbttaglist = new NBTTagList();
/*     */       
/*  48 */       for (AttributeModifier attributemodifier : collection) {
/*     */         
/*  50 */         if (attributemodifier.isSaved())
/*     */         {
/*  52 */           nbttaglist.appendTag((NBTBase)writeAttributeModifierToNBT(attributemodifier));
/*     */         }
/*     */       } 
/*     */       
/*  56 */       nbttagcompound.setTag("Modifiers", (NBTBase)nbttaglist);
/*     */     } 
/*     */     
/*  59 */     return nbttagcompound;
/*     */   }
/*     */ 
/*     */   
/*     */   private static NBTTagCompound writeAttributeModifierToNBT(AttributeModifier modifier) {
/*  64 */     NBTTagCompound nbttagcompound = new NBTTagCompound();
/*  65 */     nbttagcompound.setString("Name", modifier.getName());
/*  66 */     nbttagcompound.setDouble("Amount", modifier.getAmount());
/*  67 */     nbttagcompound.setInteger("Operation", modifier.getOperation());
/*  68 */     nbttagcompound.setLong("UUIDMost", modifier.getID().getMostSignificantBits());
/*  69 */     nbttagcompound.setLong("UUIDLeast", modifier.getID().getLeastSignificantBits());
/*  70 */     return nbttagcompound;
/*     */   }
/*     */ 
/*     */   
/*     */   public static void setAttributeModifiers(BaseAttributeMap map, NBTTagList list) {
/*  75 */     for (int i = 0; i < list.tagCount(); i++) {
/*     */       
/*  77 */       NBTTagCompound nbttagcompound = list.getCompoundTagAt(i);
/*  78 */       IAttributeInstance iattributeinstance = map.getAttributeInstanceByName(nbttagcompound.getString("Name"));
/*     */       
/*  80 */       if (iattributeinstance != null) {
/*     */         
/*  82 */         applyModifiersToAttributeInstance(iattributeinstance, nbttagcompound);
/*     */       }
/*     */       else {
/*     */         
/*  86 */         logger.warn("Ignoring unknown attribute '" + nbttagcompound.getString("Name") + "'");
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private static void applyModifiersToAttributeInstance(IAttributeInstance instance, NBTTagCompound compound) {
/*  93 */     instance.setBaseValue(compound.getDouble("Base"));
/*     */     
/*  95 */     if (compound.hasKey("Modifiers", 9)) {
/*     */       
/*  97 */       NBTTagList nbttaglist = compound.getTagList("Modifiers", 10);
/*     */       
/*  99 */       for (int i = 0; i < nbttaglist.tagCount(); i++) {
/*     */         
/* 101 */         AttributeModifier attributemodifier = readAttributeModifierFromNBT(nbttaglist.getCompoundTagAt(i));
/*     */         
/* 103 */         if (attributemodifier != null) {
/*     */           
/* 105 */           AttributeModifier attributemodifier1 = instance.getModifier(attributemodifier.getID());
/*     */           
/* 107 */           if (attributemodifier1 != null)
/*     */           {
/* 109 */             instance.removeModifier(attributemodifier1);
/*     */           }
/*     */           
/* 112 */           instance.applyModifier(attributemodifier);
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public static AttributeModifier readAttributeModifierFromNBT(NBTTagCompound compound) {
/* 120 */     UUID uuid = new UUID(compound.getLong("UUIDMost"), compound.getLong("UUIDLeast"));
/*     */ 
/*     */     
/*     */     try {
/* 124 */       return new AttributeModifier(uuid, compound.getString("Name"), compound.getDouble("Amount"), compound.getInteger("Operation"));
/*     */     }
/* 126 */     catch (Exception exception) {
/*     */       
/* 128 */       logger.warn("Unable to create attribute: " + exception.getMessage());
/* 129 */       return null;
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\entity\SharedMonsterAttributes.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */