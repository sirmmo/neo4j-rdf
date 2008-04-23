package org.neo4j.rdf.newmodel;

public class Wildcard implements Value
{
    private final String name;
    public Wildcard( String name )
    {
        assert name != null;
        this.name = name;
    }
    public String getVariableName()
    {
        return name;
    }
}