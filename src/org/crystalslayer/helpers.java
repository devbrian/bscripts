package org.crystalslayer;

import simple.api.ClientContext;
import simple.api.wrappers.SimpleWidget;

public class helpers {
    public static ClientContext ctx;

    public static SlayerTask getSlayerTask(){
        SimpleWidget taskWidget = ctx.widgets.populate().filter(w -> w.getText() != null && w.getText().contains("Slayer Task") && !w.getText().contains("Complete")).next();
        if(taskWidget == null){
            ctx.log("ERROR - no task widget.");
            return SlayerTask.NONE;
        }else{
            String slayerText = taskWidget.getText();
            if(slayerText.toLowerCase().contains("bats")){
                return SlayerTask.BATS;
            }else if(slayerText.toLowerCase().contains("spiders")){
                return SlayerTask.SPIDERS;}
            else if(slayerText.toLowerCase().contains("rats")){
                return SlayerTask.RATS;
            }else if(slayerText.toLowerCase().contains("none")){
                return SlayerTask.NONE;
            }
        }
        return SlayerTask.NONE;
    }
}

