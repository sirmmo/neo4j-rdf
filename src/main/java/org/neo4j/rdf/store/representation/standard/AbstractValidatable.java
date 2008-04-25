package org.neo4j.rdf.store.representation.standard;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.neo4j.api.core.Direction;
import org.neo4j.api.core.NeoService;
import org.neo4j.api.core.Node;
import org.neo4j.api.core.Relationship;
import org.neo4j.neometa.structure.MetaStructure;
import org.neo4j.neometa.structure.MetaStructureClass;
import org.neo4j.neometa.structure.MetaStructureRelTypes;
import org.neo4j.rdf.validation.Validatable;
import org.neo4j.util.NeoUtil;

public abstract class AbstractValidatable implements Validatable
{
    private NeoUtil neoUtil;
    private Node node;
    private MetaStructure meta;
    
    public AbstractValidatable( NeoService neo, Node node, MetaStructure meta )
    {
        this.neoUtil = new NeoUtil( neo );
        this.node = node;
        this.meta = meta;
    }
    
    protected Node node()
    {
        return this.node;
    }
    
    protected MetaStructure meta()
    {
        return this.meta;
    }
    
    protected NeoUtil neoUtil()
    {
        return this.neoUtil;
    }
    
    protected boolean isPropertyKey( String key )
    {
        return !key.contains( UriBasedExecutor.CONTEXT_DELIMITER );
    }
    
    protected boolean isPropertyRelationship( Relationship relationship )
    {
        return !relationship.getType().name().equals(
            MetaStructureRelTypes.META_IS_INSTANCE_OF.name() );
    }

    protected abstract void addSimplePropertyKeys( Set<String> set );

    protected void addComplexPropertyKeys( Set<String> set )
    {
        for ( Relationship relationship :
            node().getRelationships( Direction.OUTGOING ) )
        {
            if ( isPropertyRelationship( relationship ) )
            {
                set.add( relationship.getType().name() );
            }
        }
    }

    public String[] getAllPropertyKeys()
    {
        Set<String> set = new HashSet<String>();
        addSimplePropertyKeys( set );
        addComplexPropertyKeys( set );
        return set.toArray( new String[ set.size() ] );
    }

    public Collection<MetaStructureClass> getClasses()
    {
        return new InstanceOfCollection( meta, node );
    }
}
