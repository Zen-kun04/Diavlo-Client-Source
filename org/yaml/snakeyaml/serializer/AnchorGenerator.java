package org.yaml.snakeyaml.serializer;

import org.yaml.snakeyaml.nodes.Node;

public interface AnchorGenerator {
  String nextAnchor(Node paramNode);
}


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\org\yaml\snakeyaml\serializer\AnchorGenerator.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */