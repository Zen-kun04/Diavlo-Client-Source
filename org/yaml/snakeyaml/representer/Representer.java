/*     */ package org.yaml.snakeyaml.representer;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.TimeZone;
/*     */ import org.yaml.snakeyaml.DumperOptions;
/*     */ import org.yaml.snakeyaml.TypeDescription;
/*     */ import org.yaml.snakeyaml.introspector.Property;
/*     */ import org.yaml.snakeyaml.introspector.PropertyUtils;
/*     */ import org.yaml.snakeyaml.nodes.MappingNode;
/*     */ import org.yaml.snakeyaml.nodes.Node;
/*     */ import org.yaml.snakeyaml.nodes.NodeId;
/*     */ import org.yaml.snakeyaml.nodes.NodeTuple;
/*     */ import org.yaml.snakeyaml.nodes.ScalarNode;
/*     */ import org.yaml.snakeyaml.nodes.SequenceNode;
/*     */ import org.yaml.snakeyaml.nodes.Tag;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Representer
/*     */   extends SafeRepresenter
/*     */ {
/*  43 */   protected Map<Class<? extends Object>, TypeDescription> typeDefinitions = Collections.emptyMap();
/*     */   
/*     */   public Representer(DumperOptions options) {
/*  46 */     super(options);
/*  47 */     this.representers.put(null, new RepresentJavaBean());
/*     */   }
/*     */   
/*     */   public TypeDescription addTypeDescription(TypeDescription td) {
/*  51 */     if (Collections.EMPTY_MAP == this.typeDefinitions) {
/*  52 */       this.typeDefinitions = new HashMap<>();
/*     */     }
/*  54 */     if (td.getTag() != null) {
/*  55 */       addClassTag(td.getType(), td.getTag());
/*     */     }
/*  57 */     td.setPropertyUtils(getPropertyUtils());
/*  58 */     return this.typeDefinitions.put(td.getType(), td);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setPropertyUtils(PropertyUtils propertyUtils) {
/*  63 */     super.setPropertyUtils(propertyUtils);
/*  64 */     Collection<TypeDescription> tds = this.typeDefinitions.values();
/*  65 */     for (TypeDescription typeDescription : tds)
/*  66 */       typeDescription.setPropertyUtils(propertyUtils); 
/*     */   }
/*     */   
/*     */   protected class RepresentJavaBean
/*     */     implements Represent
/*     */   {
/*     */     public Node representData(Object data) {
/*  73 */       return (Node)Representer.this.representJavaBean(Representer.this.getProperties((Class)data.getClass()), data);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected MappingNode representJavaBean(Set<Property> properties, Object javaBean) {
/*  88 */     List<NodeTuple> value = new ArrayList<>(properties.size());
/*     */     
/*  90 */     Tag customTag = this.classTags.get(javaBean.getClass());
/*  91 */     Tag tag = (customTag != null) ? customTag : new Tag(javaBean.getClass());
/*     */     
/*  93 */     MappingNode node = new MappingNode(tag, value, DumperOptions.FlowStyle.AUTO);
/*  94 */     this.representedObjects.put(javaBean, node);
/*  95 */     DumperOptions.FlowStyle bestStyle = DumperOptions.FlowStyle.FLOW;
/*  96 */     for (Property property : properties) {
/*  97 */       Object memberValue = property.get(javaBean);
/*  98 */       Tag customPropertyTag = (memberValue == null) ? null : this.classTags.get(memberValue.getClass());
/*     */       
/* 100 */       NodeTuple tuple = representJavaBeanProperty(javaBean, property, memberValue, customPropertyTag);
/* 101 */       if (tuple == null) {
/*     */         continue;
/*     */       }
/* 104 */       if (!((ScalarNode)tuple.getKeyNode()).isPlain()) {
/* 105 */         bestStyle = DumperOptions.FlowStyle.BLOCK;
/*     */       }
/* 107 */       Node nodeValue = tuple.getValueNode();
/* 108 */       if (!(nodeValue instanceof ScalarNode) || !((ScalarNode)nodeValue).isPlain()) {
/* 109 */         bestStyle = DumperOptions.FlowStyle.BLOCK;
/*     */       }
/* 111 */       value.add(tuple);
/*     */     } 
/* 113 */     if (this.defaultFlowStyle != DumperOptions.FlowStyle.AUTO) {
/* 114 */       node.setFlowStyle(this.defaultFlowStyle);
/*     */     } else {
/* 116 */       node.setFlowStyle(bestStyle);
/*     */     } 
/* 118 */     return node;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected NodeTuple representJavaBeanProperty(Object javaBean, Property property, Object propertyValue, Tag customTag) {
/* 132 */     ScalarNode nodeKey = (ScalarNode)representData(property.getName());
/*     */     
/* 134 */     boolean hasAlias = this.representedObjects.containsKey(propertyValue);
/*     */     
/* 136 */     Node nodeValue = representData(propertyValue);
/*     */     
/* 138 */     if (propertyValue != null && !hasAlias) {
/* 139 */       NodeId nodeId = nodeValue.getNodeId();
/* 140 */       if (customTag == null) {
/* 141 */         if (nodeId == NodeId.scalar) {
/*     */           
/* 143 */           if (property.getType() != Enum.class && 
/* 144 */             propertyValue instanceof Enum) {
/* 145 */             nodeValue.setTag(Tag.STR);
/*     */           }
/*     */         } else {
/*     */           
/* 149 */           if (nodeId == NodeId.mapping && 
/* 150 */             property.getType() == propertyValue.getClass() && 
/* 151 */             !(propertyValue instanceof Map) && 
/* 152 */             !nodeValue.getTag().equals(Tag.SET)) {
/* 153 */             nodeValue.setTag(Tag.MAP);
/*     */           }
/*     */ 
/*     */ 
/*     */           
/* 158 */           checkGlobalTag(property, nodeValue, propertyValue);
/*     */         } 
/*     */       }
/*     */     } 
/*     */     
/* 163 */     return new NodeTuple((Node)nodeKey, nodeValue);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void checkGlobalTag(Property property, Node node, Object object) {
/* 177 */     if (object.getClass().isArray() && object.getClass().getComponentType().isPrimitive()) {
/*     */       return;
/*     */     }
/*     */     
/* 181 */     Class<?>[] arguments = property.getActualTypeArguments();
/* 182 */     if (arguments != null) {
/* 183 */       if (node.getNodeId() == NodeId.sequence) {
/*     */         
/* 185 */         Class<? extends Object> t = (Class)arguments[0];
/* 186 */         SequenceNode snode = (SequenceNode)node;
/* 187 */         Iterable<Object> memberList = Collections.EMPTY_LIST;
/* 188 */         if (object.getClass().isArray()) {
/* 189 */           memberList = Arrays.asList((Object[])object);
/* 190 */         } else if (object instanceof Iterable) {
/*     */           
/* 192 */           memberList = (Iterable<Object>)object;
/*     */         } 
/* 194 */         Iterator<Object> iter = memberList.iterator();
/* 195 */         if (iter.hasNext()) {
/* 196 */           for (Node childNode : snode.getValue()) {
/* 197 */             Object member = iter.next();
/* 198 */             if (member != null && 
/* 199 */               t.equals(member.getClass()) && 
/* 200 */               childNode.getNodeId() == NodeId.mapping) {
/* 201 */               childNode.setTag(Tag.MAP);
/*     */             }
/*     */           }
/*     */         
/*     */         }
/*     */       }
/* 207 */       else if (object instanceof Set) {
/* 208 */         Class<?> t = arguments[0];
/* 209 */         MappingNode mnode = (MappingNode)node;
/* 210 */         Iterator<NodeTuple> iter = mnode.getValue().iterator();
/* 211 */         Set<?> set = (Set)object;
/* 212 */         for (Object member : set) {
/* 213 */           NodeTuple tuple = iter.next();
/* 214 */           Node keyNode = tuple.getKeyNode();
/* 215 */           if (t.equals(member.getClass()) && 
/* 216 */             keyNode.getNodeId() == NodeId.mapping) {
/* 217 */             keyNode.setTag(Tag.MAP);
/*     */           }
/*     */         }
/*     */       
/* 221 */       } else if (object instanceof Map) {
/* 222 */         Class<?> keyType = arguments[0];
/* 223 */         Class<?> valueType = arguments[1];
/* 224 */         MappingNode mnode = (MappingNode)node;
/* 225 */         for (NodeTuple tuple : mnode.getValue()) {
/* 226 */           resetTag((Class)keyType, tuple.getKeyNode());
/* 227 */           resetTag((Class)valueType, tuple.getValueNode());
/*     */         } 
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void resetTag(Class<? extends Object> type, Node node) {
/* 237 */     Tag tag = node.getTag();
/* 238 */     if (tag.matches(type)) {
/* 239 */       if (Enum.class.isAssignableFrom(type)) {
/* 240 */         node.setTag(Tag.STR);
/*     */       } else {
/* 242 */         node.setTag(Tag.MAP);
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Set<Property> getProperties(Class<? extends Object> type) {
/* 255 */     if (this.typeDefinitions.containsKey(type)) {
/* 256 */       return ((TypeDescription)this.typeDefinitions.get(type)).getProperties();
/*     */     }
/* 258 */     return getPropertyUtils().getProperties(type);
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\org\yaml\snakeyaml\representer\Representer.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */