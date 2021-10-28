package org.framework.treescript;

import simple.api.ClientContext;

public abstract class Node {

    public abstract void onProcess(ClientContext ctx);
    public abstract boolean validate(ClientContext ctx);
}