package org.crystalslayer.nodes;

import org.crystalslayer.SlayerTask;
import org.crystalslayer.states;
import org.framework.basicnode.Node;
import simple.api.ClientContext;
import simple.api.actions.SimpleItemActions;
import simple.api.actions.SimpleObjectActions;
import simple.api.queries.SimpleEntityQuery;
import simple.api.wrappers.SimpleGameObject;
import simple.api.wrappers.SimpleGroundItem;
import simple.api.wrappers.SimpleItem;
import simple.api.wrappers.SimpleNpc;

import java.util.Timer;

import static org.crystalslayer.helpers.getSlayerTask;

public class DoTask extends Node {
    public DoTask(ClientContext ctx) {
        super(ctx);
    }

    long startTime = System.currentTimeMillis();
    private static final int[] items = {23971, 23975, 23979, 989, 23951, 30000, 2677, 2364, 1392, 1215, 4087, 2362, 1516, 392, 454, 1127, 23877, 1305, 452, 1514, 1079, 2801, 1163, 560, 1201, 1347, 1748, 4585,  1308, 10832, 10833, 10835, 561, 565, 562, 554};
    private static final int[] alchItems = {1215, 1127, 1163, 1079, 1347, 4585, 1201, 1305, 4087};

    @Override
    public void process() {
        ctx.log("DOTask");
        states.finishedTask = false;
        SlayerTask task = getSlayerTask();

        if(states.useCannon){
            if(ctx.inventory.populate().filter(6).population() > 0 && SlayerTask.NONE != task){
                ctx.log("Placing cannon.");
                if(ctx.inventory.filter(6).next().interact(74)){
                    ctx.sleepCondition(() -> ctx.inventory.populate().filter(6).population() == 0, 5000);
                }
                if(ctx.inventory.populate().filter(6).population() == 0){
                    SimpleGameObject cannon = (SimpleGameObject) ctx.objects.populate().filter(6).nearest().next();
                    if(cannon != null){
                        startTime = System.currentTimeMillis();
                        states.cannon = cannon;
                    }
                }
            }else if(states.cannon == null && task != SlayerTask.NONE){
                ctx.log("Checking cannon state a.");
                SimpleGameObject cannon = (SimpleGameObject) ctx.objects.populate().filter(6).nearest().next();
                if(cannon != null){
                    ctx.log("Setting cannon state.");
                    startTime = System.currentTimeMillis();
                    states.cannon = cannon;
                }
            }else if(task == SlayerTask.NONE && states.cannon != null && states.useCannon){
                ctx.log("Checking cannon state b.");
                states.cannon.interact(SimpleObjectActions.SECOND);
                ctx.onCondition(() -> ctx.inventory.populate().filter(6).population() > 0, 500, 10);
                if(ctx.inventory.populate().filter(6).population() > 0){
                    ctx.log("Nulling cannon state.");
                    states.cannon = null;
                }
            }else if(states.cannon != null){
                long currTime = System.currentTimeMillis();
                if((currTime - startTime) > 15000){
                    ctx.log("Reloading cannon!");
                    int ballCount = ctx.inventory.populate().filter(2).next().getQuantity();
                    states.cannon.interact(SimpleObjectActions.FIRST);
                    ctx.sleepCondition(() ->  ctx.inventory.populate().filter(2).next().getQuantity() < ballCount, 5000);
                    startTime = System.currentTimeMillis();
                }
            }
        }

        int inventoryPopulation = ctx.inventory.populate().filter(alchItems).population();
        int groundPopulation = ctx.groundItems.populate().filter(items).population();
        if(inventoryPopulation > 0){
            ctx.log("Alching!");
            ctx.magic.castSpellOnItem(1178, ctx.inventory.populate().filter(alchItems).next());
            ctx.sleep(1600);
            ctx.sleepCondition(() -> inventoryPopulation > ctx.inventory.populate().filter(alchItems).population(), 5000);
        }else if(groundPopulation > 0 && ctx.inventory.getFreeSlots() > 0){
            ctx.log("Taking item");
            SimpleGroundItem loot = ctx.groundItems.populate().filter(items).nearest().next();
            loot.interact();
            ctx.sleep(250);
        }else if(task != SlayerTask.NONE && (!ctx.players.getLocal().inCombat() || ctx.players.getLocal().getInteracting() == null)){
            SimpleNpc fm = npcs(task.getNpc()).filter((n) -> n.getInteracting() != null && n.getInteracting().equals(ctx.players.getLocal()) && n.inCombat()).nearest().next();
            SimpleNpc npc = fm != null ? fm : npcs(task.getNpc()).nearest().next();
            if(npc != null){
                ctx.log("Attacking!");
                npc.interact("attack");
                ctx.onCondition(() -> ctx.players.getLocal().inCombat(), 250, 12);
            }
        }else if(task == SlayerTask.NONE){
            ctx.log("Finishing task!");
            if(states.useCannon && states.cannon == null){
                states.finishedTask = true;
            }else if(!states.useCannon){
                states.finishedTask = true;
            }
        }
    }

    @Override
    public boolean canProcess() {
        SlayerTask task = getSlayerTask();
        return ((task == SlayerTask.NONE && !states.finishedTask) ||
                (task != SlayerTask.NONE && ctx.pathing.inArea(task.getArea()))) ;
    }

    public final SimpleEntityQuery<SimpleNpc> npcs(String name) {
        return ctx.npcs.populate().filter(n -> n.getName().toLowerCase().contains(name)).filter(n -> {
            if (n == null) {
                return false;
            }
            try{
                if (!getSlayerTask().getArea().containsPoint(n.getLocation())){
                    return false;
                }
            }catch(Exception exception){
                return false;
            }
            if (n.getId() == 10) return false;
            if (n.getLocation().distanceTo(ctx.players.getLocal().getLocation()) > 15) {
                return false;
            }
            if (n.inCombat() && n.getInteracting() != null && !n.getInteracting().equals(ctx.players.getLocal())) {
                return false;
            }
            if (n.isDead()) {
                return false;
            }
            return true;
        });
    }
}
