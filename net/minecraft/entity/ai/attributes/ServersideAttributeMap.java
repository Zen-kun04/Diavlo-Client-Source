/*    */ package net.minecraft.entity.ai.attributes;
/*    */ 
/*    */ import com.google.common.collect.Sets;
/*    */ import java.util.Collection;
/*    */ import java.util.Map;
/*    */ import java.util.Set;
/*    */ import net.minecraft.server.management.LowerStringMap;
/*    */ 
/*    */ public class ServersideAttributeMap
/*    */   extends BaseAttributeMap {
/* 11 */   private final Set<IAttributeInstance> attributeInstanceSet = Sets.newHashSet();
/* 12 */   protected final Map<String, IAttributeInstance> descriptionToAttributeInstanceMap = (Map<String, IAttributeInstance>)new LowerStringMap();
/*    */ 
/*    */   
/*    */   public ModifiableAttributeInstance getAttributeInstance(IAttribute attribute) {
/* 16 */     return (ModifiableAttributeInstance)super.getAttributeInstance(attribute);
/*    */   }
/*    */ 
/*    */   
/*    */   public ModifiableAttributeInstance getAttributeInstanceByName(String attributeName) {
/* 21 */     IAttributeInstance iattributeinstance = super.getAttributeInstanceByName(attributeName);
/*    */     
/* 23 */     if (iattributeinstance == null)
/*    */     {
/* 25 */       iattributeinstance = this.descriptionToAttributeInstanceMap.get(attributeName);
/*    */     }
/*    */     
/* 28 */     return (ModifiableAttributeInstance)iattributeinstance;
/*    */   }
/*    */ 
/*    */   
/*    */   public IAttributeInstance registerAttribute(IAttribute attribute) {
/* 33 */     IAttributeInstance iattributeinstance = super.registerAttribute(attribute);
/*    */     
/* 35 */     if (attribute instanceof RangedAttribute && ((RangedAttribute)attribute).getDescription() != null)
/*    */     {
/* 37 */       this.descriptionToAttributeInstanceMap.put(((RangedAttribute)attribute).getDescription(), iattributeinstance);
/*    */     }
/*    */     
/* 40 */     return iattributeinstance;
/*    */   }
/*    */ 
/*    */   
/*    */   protected IAttributeInstance func_180376_c(IAttribute attribute) {
/* 45 */     return new ModifiableAttributeInstance(this, attribute);
/*    */   }
/*    */ 
/*    */   
/*    */   public void func_180794_a(IAttributeInstance instance) {
/* 50 */     if (instance.getAttribute().getShouldWatch())
/*    */     {
/* 52 */       this.attributeInstanceSet.add(instance);
/*    */     }
/*    */     
/* 55 */     for (IAttribute iattribute : this.field_180377_c.get(instance.getAttribute())) {
/*    */       
/* 57 */       ModifiableAttributeInstance modifiableattributeinstance = getAttributeInstance(iattribute);
/*    */       
/* 59 */       if (modifiableattributeinstance != null)
/*    */       {
/* 61 */         modifiableattributeinstance.flagForUpdate();
/*    */       }
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public Set<IAttributeInstance> getAttributeInstanceSet() {
/* 68 */     return this.attributeInstanceSet;
/*    */   }
/*    */ 
/*    */   
/*    */   public Collection<IAttributeInstance> getWatchedAttributes() {
/* 73 */     Set<IAttributeInstance> set = Sets.newHashSet();
/*    */     
/* 75 */     for (IAttributeInstance iattributeinstance : getAllAttributes()) {
/*    */       
/* 77 */       if (iattributeinstance.getAttribute().getShouldWatch())
/*    */       {
/* 79 */         set.add(iattributeinstance);
/*    */       }
/*    */     } 
/*    */     
/* 83 */     return set;
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\entity\ai\attributes\ServersideAttributeMap.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */