package org.framework.treescript;

import simple.api.script.Script;

public abstract class TreeScript extends Script {

    public abstract NodeBranch getRootBranch();


    @Override
    public void onProcess() {
        getRootBranch().onProcess(ctx);
    }
}