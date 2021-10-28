package org.framework.treescript;

import simple.api.ClientContext;

public abstract class NodeBranch extends Node {
    public abstract Node isTrue();
    public abstract Node isFalse();

    @Override
    public void onProcess(ClientContext ctx) {
        Node left = isTrue();
        Node right = isFalse();
        if (validate(ctx)) {
            left.onProcess(ctx);
        } else {
            right.onProcess(ctx);
        }
    }

}
