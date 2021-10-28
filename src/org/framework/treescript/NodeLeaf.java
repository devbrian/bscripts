package org.framework.treescript;

import simple.api.ClientContext;

public abstract class NodeLeaf extends Node {

    @Override
    public boolean validate(ClientContext ctx) {
        return true;
    }
}