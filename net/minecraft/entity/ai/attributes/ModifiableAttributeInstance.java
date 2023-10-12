/*     */ package net.minecraft.entity.ai.attributes;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import com.google.common.collect.Maps;
/*     */ import com.google.common.collect.Sets;
/*     */ import java.util.Collection;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.UUID;
/*     */ 
/*     */ public class ModifiableAttributeInstance
/*     */   implements IAttributeInstance {
/*     */   private final BaseAttributeMap attributeMap;
/*     */   private final IAttribute genericAttribute;
/*  15 */   private final Map<Integer, Set<AttributeModifier>> mapByOperation = Maps.newHashMap();
/*  16 */   private final Map<String, Set<AttributeModifier>> mapByName = Maps.newHashMap();
/*  17 */   private final Map<UUID, AttributeModifier> mapByUUID = Maps.newHashMap();
/*     */   
/*     */   private double baseValue;
/*     */   private boolean needsUpdate = true;
/*     */   private double cachedValue;
/*     */   
/*     */   public ModifiableAttributeInstance(BaseAttributeMap attributeMapIn, IAttribute genericAttributeIn) {
/*  24 */     this.attributeMap = attributeMapIn;
/*  25 */     this.genericAttribute = genericAttributeIn;
/*  26 */     this.baseValue = genericAttributeIn.getDefaultValue();
/*     */     
/*  28 */     for (int i = 0; i < 3; i++)
/*     */     {
/*  30 */       this.mapByOperation.put(Integer.valueOf(i), Sets.newHashSet());
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public IAttribute getAttribute() {
/*  36 */     return this.genericAttribute;
/*     */   }
/*     */ 
/*     */   
/*     */   public double getBaseValue() {
/*  41 */     return this.baseValue;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setBaseValue(double baseValue) {
/*  46 */     if (baseValue != getBaseValue()) {
/*     */       
/*  48 */       this.baseValue = baseValue;
/*  49 */       flagForUpdate();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public Collection<AttributeModifier> getModifiersByOperation(int operation) {
/*  55 */     return this.mapByOperation.get(Integer.valueOf(operation));
/*     */   }
/*     */ 
/*     */   
/*     */   public Collection<AttributeModifier> func_111122_c() {
/*  60 */     Set<AttributeModifier> set = Sets.newHashSet();
/*     */     
/*  62 */     for (int i = 0; i < 3; i++)
/*     */     {
/*  64 */       set.addAll(getModifiersByOperation(i));
/*     */     }
/*     */     
/*  67 */     return set;
/*     */   }
/*     */ 
/*     */   
/*     */   public AttributeModifier getModifier(UUID uuid) {
/*  72 */     return this.mapByUUID.get(uuid);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean hasModifier(AttributeModifier modifier) {
/*  77 */     return (this.mapByUUID.get(modifier.getID()) != null);
/*     */   }
/*     */ 
/*     */   
/*     */   public void applyModifier(AttributeModifier modifier) {
/*  82 */     if (getModifier(modifier.getID()) != null)
/*     */     {
/*  84 */       throw new IllegalArgumentException("Modifier is already applied on this attribute!");
/*     */     }
/*     */ 
/*     */     
/*  88 */     Set<AttributeModifier> set = this.mapByName.get(modifier.getName());
/*     */     
/*  90 */     if (set == null) {
/*     */       
/*  92 */       set = Sets.newHashSet();
/*  93 */       this.mapByName.put(modifier.getName(), set);
/*     */     } 
/*     */     
/*  96 */     ((Set<AttributeModifier>)this.mapByOperation.get(Integer.valueOf(modifier.getOperation()))).add(modifier);
/*  97 */     set.add(modifier);
/*  98 */     this.mapByUUID.put(modifier.getID(), modifier);
/*  99 */     flagForUpdate();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void flagForUpdate() {
/* 105 */     this.needsUpdate = true;
/* 106 */     this.attributeMap.func_180794_a(this);
/*     */   }
/*     */ 
/*     */   
/*     */   public void removeModifier(AttributeModifier modifier) {
/* 111 */     for (int i = 0; i < 3; i++) {
/*     */       
/* 113 */       Set<AttributeModifier> set = this.mapByOperation.get(Integer.valueOf(i));
/* 114 */       set.remove(modifier);
/*     */     } 
/*     */     
/* 117 */     Set<AttributeModifier> set1 = this.mapByName.get(modifier.getName());
/*     */     
/* 119 */     if (set1 != null) {
/*     */       
/* 121 */       set1.remove(modifier);
/*     */       
/* 123 */       if (set1.isEmpty())
/*     */       {
/* 125 */         this.mapByName.remove(modifier.getName());
/*     */       }
/*     */     } 
/*     */     
/* 129 */     this.mapByUUID.remove(modifier.getID());
/* 130 */     flagForUpdate();
/*     */   }
/*     */ 
/*     */   
/*     */   public void removeAllModifiers() {
/* 135 */     Collection<AttributeModifier> collection = func_111122_c();
/*     */     
/* 137 */     if (collection != null)
/*     */     {
/* 139 */       for (AttributeModifier attributemodifier : Lists.newArrayList(collection))
/*     */       {
/* 141 */         removeModifier(attributemodifier);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public double getAttributeValue() {
/* 148 */     if (this.needsUpdate) {
/*     */       
/* 150 */       this.cachedValue = computeValue();
/* 151 */       this.needsUpdate = false;
/*     */     } 
/*     */     
/* 154 */     return this.cachedValue;
/*     */   }
/*     */ 
/*     */   
/*     */   private double computeValue() {
/* 159 */     double d0 = getBaseValue();
/*     */     
/* 161 */     for (AttributeModifier attributemodifier : func_180375_b(0))
/*     */     {
/* 163 */       d0 += attributemodifier.getAmount();
/*     */     }
/*     */     
/* 166 */     double d1 = d0;
/*     */     
/* 168 */     for (AttributeModifier attributemodifier1 : func_180375_b(1))
/*     */     {
/* 170 */       d1 += d0 * attributemodifier1.getAmount();
/*     */     }
/*     */     
/* 173 */     for (AttributeModifier attributemodifier2 : func_180375_b(2))
/*     */     {
/* 175 */       d1 *= 1.0D + attributemodifier2.getAmount();
/*     */     }
/*     */     
/* 178 */     return this.genericAttribute.clampValue(d1);
/*     */   }
/*     */ 
/*     */   
/*     */   private Collection<AttributeModifier> func_180375_b(int operation) {
/* 183 */     Set<AttributeModifier> set = Sets.newHashSet(getModifiersByOperation(operation));
/*     */     
/* 185 */     for (IAttribute iattribute = this.genericAttribute.func_180372_d(); iattribute != null; iattribute = iattribute.func_180372_d()) {
/*     */       
/* 187 */       IAttributeInstance iattributeinstance = this.attributeMap.getAttributeInstance(iattribute);
/*     */       
/* 189 */       if (iattributeinstance != null)
/*     */       {
/* 191 */         set.addAll(iattributeinstance.getModifiersByOperation(operation));
/*     */       }
/*     */     } 
/*     */     
/* 195 */     return set;
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\entity\ai\attributes\ModifiableAttributeInstance.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */