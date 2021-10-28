package org.framework.basicnode;

import simple.api.ClientContext;

public abstract class Node {
    protected ClientContext ctx;

    protected Node(ClientContext ctx) {
        this.ctx = ctx;
    }

    public abstract void process();
    public abstract boolean canProcess();
}