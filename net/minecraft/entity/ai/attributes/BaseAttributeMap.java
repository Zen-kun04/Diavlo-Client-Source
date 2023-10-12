/*    */ package net.minecraft.entity.ai.attributes;
/*    */ 
/*    */ import com.google.common.collect.HashMultimap;
/*    */ import com.google.common.collect.Maps;
/*    */ import com.google.common.collect.Multimap;
/*    */ import java.util.Collection;
/*    */ import java.util.Map;
/*    */ import net.minecraft.server.management.LowerStringMap;
/*    */ 
/*    */ 
/*    */ public abstract class BaseAttributeMap
/*    */ {
/* 13 */   protected final Map<IAttribute, IAttributeInstance> attributes = Maps.newHashMap();
/* 14 */   protected final Map<String, IAttributeInstance> attributesByName = (Map<String, IAttributeInstance>)new LowerStringMap();
/* 15 */   protected final Multimap<IAttribute, IAttribute> field_180377_c = (Multimap<IAttribute, IAttribute>)HashMultimap.create();
/*    */ 
/*    */   
/*    */   public IAttributeInstance getAttributeInstance(IAttribute attribute) {
/* 19 */     return this.attributes.get(attribute);
/*    */   }
/*    */ 
/*    */   
/*    */   public IAttributeInstance getAttributeInstanceByName(String attributeName) {
/* 24 */     return this.attributesByName.get(attributeName);
/*    */   }
/*    */ 
/*    */   
/*    */   public IAttributeInstance registerAttribute(IAttribute attribute) {
/* 29 */     if (this.attributesByName.containsKey(attribute.getAttributeUnlocalizedName()))
/*    */     {
/* 31 */       throw new IllegalArgumentException("Attribute is already registered!");
/*    */     }
/*    */ 
/*    */     
/* 35 */     IAttributeInstance iattributeinstance = func_180376_c(attribute);
/* 36 */     this.attributesByName.put(attribute.getAttributeUnlocalizedName(), iattributeinstance);
/* 37 */     this.attributes.put(attribute, iattributeinstance);
/*    */     
/* 39 */     for (IAttribute iattribute = attribute.func_180372_d(); iattribute != null; iattribute = iattribute.func_180372_d())
/*    */     {
/* 41 */       this.field_180377_c.put(iattribute, attribute);
/*    */     }
/*    */     
/* 44 */     return iattributeinstance;
/*    */   }
/*    */ 
/*    */   
/*    */   protected abstract IAttributeInstance func_180376_c(IAttribute paramIAttribute);
/*    */ 
/*    */   
/*    */   public Collection<IAttributeInstance> getAllAttributes() {
/* 52 */     return this.attributesByName.values();
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void func_180794_a(IAttributeInstance instance) {}
/*    */ 
/*    */   
/*    */   public void removeAttributeModifiers(Multimap<String, AttributeModifier> modifiers) {
/* 61 */     for (Map.Entry<String, AttributeModifier> entry : (Iterable<Map.Entry<String, AttributeModifier>>)modifiers.entries()) {
/*    */       
/* 63 */       IAttributeInstance iattributeinstance = getAttributeInstanceByName(entry.getKey());
/*    */       
/* 65 */       if (iattributeinstance != null)
/*    */       {
/* 67 */         iattributeinstance.removeModifier(entry.getValue());
/*    */       }
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public void applyAttributeModifiers(Multimap<String, AttributeModifier> modifiers) {
/* 74 */     for (Map.Entry<String, AttributeModifier> entry : (Iterable<Map.Entry<String, AttributeModifier>>)modifiers.entries()) {
/*    */       
/* 76 */       IAttributeInstance iattributeinstance = getAttributeInstanceByName(entry.getKey());
/*    */       
/* 78 */       if (iattributeinstance != null) {
/*    */         
/* 80 */         iattributeinstance.removeModifier(entry.getValue());
/* 81 */         iattributeinstance.applyModifier(entry.getValue());
/*    */       } 
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\entity\ai\attributes\BaseAttributeMap.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */