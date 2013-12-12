package org.javers.model.object.graph;

import org.javers.common.collections.Function;
import org.javers.common.collections.Lists;
import org.javers.model.domain.GlobalCdoId;
import org.javers.model.mapping.Property;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
/**
 * OneToMany or ManyToMany relation
 * @author bartosz walacik
 */
public class MultiEdge extends Edge {
    protected List<ObjectNode> references;

    public MultiEdge(Property property) {
        super(property);
        references = new ArrayList<>();
    }

    public List<GlobalCdoId> getReferencedGlobalCdoIds() {
        return Lists.transform(references, new Function<ObjectNode, GlobalCdoId>() {
            @Override
            public GlobalCdoId apply(ObjectNode input) {
                return new GlobalCdoId(input.getGlobalCdoId(), input.getEntity());
            }
        });
    }

    public List<ObjectNode> getReferences(){
        return Collections.unmodifiableList(references);
    }

    /**
     * @return null if not found
     */
    public ObjectNode getReference(Object referencedCdoId){
        for (ObjectNode ref: references) {
            if (ref.getLocalCdoId().equals(referencedCdoId)) {
                return ref;
            }
        }
        return null;
    }

    public void addReferenceNode(ObjectNode objectNode) {
        references.add(objectNode);
    }

    @Override
    public void accept(GraphVisitor visitor) {
        for(ObjectNode objectNode : references) {
            objectNode.accept(visitor);
        }
    }
}
