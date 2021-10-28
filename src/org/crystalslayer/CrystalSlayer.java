package org.crystalslayer;

import org.crystalslayer.nodes.DoTask;
import org.crystalslayer.nodes.GetTask;
import org.crystalslayer.nodes.GetToTask;
import org.framework.basicnode.Node;
import simple.api.events.GameTickEvent;
import simple.api.listeners.SimpleGameTickListener;
import simple.api.script.Category;
import simple.api.script.LoopingScript;
import simple.api.script.Script;
import simple.api.script.ScriptManifest;

import java.util.ArrayList;
import java.util.List;

@ScriptManifest(author = "Brian", category = Category.COMBAT, description = "Crystal Slayer",
        discord = "", name = "Crystal Slayer", servers = {"Xeros"}, version = "0.1")
public class CrystalSlayer extends Script implements SimpleGameTickListener, LoopingScript {
    private List<Node> nodes;

    @Override
    public void onGameTick(GameTickEvent gameTickEvent) {
        ctx.log("on tick");
    }

    @Override

    public boolean onExecute() {
        helpers.ctx = ctx;

        nodes = new ArrayList<>();
        nodes.add(new GetTask(ctx));
        nodes.add(new GetToTask(ctx));
        nodes.add(new DoTask(ctx));
        return true;
    }

    @Override
    public void onProcess() {
        for(Node node : nodes)
            if(node.canProcess())
                node.process();
    }

    @Override
    public void onTerminate() {
    }

    @Override
    public int loopDuration() {
        return 500;
    }
}
