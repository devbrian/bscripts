package org.crystalslayer.nodes;

import org.crystalslayer.SlayerTask;
import org.framework.basicnode.Node;
import simple.api.ClientContext;
import simple.api.actions.SimpleObjectActions;
import simple.api.coords.WorldPoint;
import simple.api.wrappers.SimpleGameObject;
import simple.api.wrappers.SimpleSceneObject;

import java.util.Arrays;

import static org.crystalslayer.helpers.getSlayerTask;

public class GetToTask extends Node {
    public GetToTask(ClientContext ctx) {
        super(ctx);
    }

    @Override
    public void process() {
        ctx.log("Get to task!");
        ctx.log(getSlayerTask().name());
        if(Arrays.stream(ctx.client.getMapRegions()).filter(i -> i == 12994).count() > 0){
            ctx.log("a");
            SlayerTask task = getSlayerTask();
            if(!ctx.pathing.inArea(task.getArea())){
                ctx.log("walking..");
                WorldPoint i = task.getArea().randomTile();
                if(ctx.pathing.walkToTile(i)){
                    ctx.sleepCondition(() -> ctx.pathing.inArea(task.getArea()), 5000);
                }
            }
        }else{
            ctx.log("b");
            if(ctx.objects.populate().filter(36556).population() == 0){
                ctx.log("c");
                ctx.menuActions.sendAction(315, 395, 0, 1164, 395);
                ctx.sleepCondition(() -> ctx.client.getOpenInterfaceId() == 39700, 5000);
                ctx.menuActions.sendAction(315, 457, 0, 44814, 457);
                ctx.sleepCondition(() -> ctx.objects.populate().filter(36556).population() > 0, 5000);
            }else{
                ctx.log("d");
                SimpleGameObject cave = (SimpleGameObject) ctx.objects.populate().filter(36556).nearest().next();
                if(ctx.client.getBackDialogId() == -1){
                    cave.interact(SimpleObjectActions.FIRST);
                    ctx.sleepCondition(() -> ctx.client.getBackDialogId() == 4882, 5000);
                }else if(ctx.client.getBackDialogId() == 4882){
                    ctx.menuActions.sendAction(679, 0, 45, 4886, 0);
                    ctx.sleepCondition(() -> ctx.client.getBackDialogId() == 2469, 5000);
                }else if(ctx.client.getBackDialogId() == 2469){
                    ctx.menuActions.sendAction(315, 0, 53, 2471, 0);
                    ctx.sleepCondition(() -> Arrays.stream(ctx.client.getMapRegions()).filter(i -> i == 12994).count() > 0, 5000);
                }
            }
        }
        ctx.log("e");
    }

    @Override
    public boolean canProcess() {
        SlayerTask task = getSlayerTask();
        return task != SlayerTask.NONE &&
                !ctx.pathing.inArea(task.getArea());
    }
}
