package org.crystalslayer.nodes;

import org.crystalslayer.SlayerTask;
import org.crystalslayer.states;
import org.framework.basicnode.Node;
import simple.api.ClientContext;
import simple.api.actions.SimpleItemActions;
import simple.api.actions.SimpleNpcActions;
import simple.api.actions.SimpleObjectActions;
import simple.api.filters.SimpleSkills;
import simple.api.wrappers.SimpleGameObject;
import simple.api.wrappers.SimpleNpc;

import java.io.*;
import static org.crystalslayer.helpers.*;


public class GetTask extends Node {
    public GetTask(ClientContext ctx) {
        super(ctx);
    }

    public boolean fullStats(){
        int p1 = ctx.skills.getLevel(SimpleSkills.Skill.PRAYER);
        int p2 = ctx.skills.getRealLevel(SimpleSkills.Skill.PRAYER);
        int h1 = ctx.skills.getLevel(SimpleSkills.Skill.HITPOINTS);
        int h2 = ctx.skills.getRealLevel(SimpleSkills.Skill.HITPOINTS);
        return p1 == p2 && h1 == h2;
    }

    @Override
    public void process() {
        if(ctx.npcs.populate().filter(n -> n.getName().contains("Elf Tracker")).population() == 0){
            states.cannon = null;
            states.premiumCavern = false;
            states.useCannon = true;
            ctx.magic.castHomeTeleport();
            ctx.sleepCondition(() -> ctx.npcs.populate().filter(n -> n.getName().contains("Elf Tracker")).population() > 0, 5000);
        }else if(!fullStats()){
            ctx.log("Trying to heal!");
            SimpleGameObject box = (SimpleGameObject) ctx.objects.populate().filter(23709).nearest().next();
            if(box != null){
                ctx.log("Clicking box!");
                box.interact(SimpleObjectActions.FIRST);
                ctx.onCondition(() -> fullStats(), 1000, 5);
            }
        }else if(ctx.inventory.getFreeSlots() != 19){
            if(!ctx.bank.bankOpen()){
                SimpleGameObject bank = (SimpleGameObject) ctx.objects.populate().filter(25808).nearest().next();
                if(bank != null){
                    bank.interact(SimpleObjectActions.FIRST);
                    ctx.sleepCondition(() -> ctx.bank.bankOpen(), 5000);
                }
            }else{
                ctx.log("here..");
                ctx.menuActions.sendAction(646, 1135, 67, 18947, 1135);
                ctx.sleepCondition(() -> ctx.client.getOpenInterfaceId() == 21553, 5000);
                ctx.menuActions.sendAction(1701, 385, 8, 21593, 385);
                ctx.sleepCondition(() -> ctx.widgets.populate().filter(21565).next().getText().contains("Crystal"), 5000);
                ctx.menuActions.sendAction(315, 2577, 0, 21566, 2577);
                ctx.sleepCondition(() -> ctx.inventory.getFreeSlots() == 19, 5000);
            }
        }else if(ctx.client.getBackDialogId() == -1){
            SimpleNpc elf = (ctx.npcs.populate().filter(n -> n.getName().contains("Elf Tracker"))).next();
            elf.interact(SimpleNpcActions.TALK_TO);
            ctx.sleepCondition(() -> ctx.client.getBackDialogId() == 4887, 5000);
        }else if(ctx.client.getBackDialogId() == 4887){
            ctx.menuActions.sendAction(679, 391, 9, 4892, 391);
            ctx.sleepCondition(() -> ctx.client.getBackDialogId() == 2459, 5000);
        }else if(ctx.client.getBackDialogId() == 2459){
            ctx.menuActions.sendAction(315, 3299, 303, 2461, 3299);
            ctx.sleepCondition(() -> getSlayerTask() != SlayerTask.NONE, 5000);
        }
    }

    @Override
    public boolean canProcess() {
        return getSlayerTask() == SlayerTask.NONE && states.finishedTask;
    }
}
