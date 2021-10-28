package org.utils;

import simple.api.ClientContext;

public class Prayer {
    public static void usePrayer(ClientContext ctx,int id){
        ctx.menuActions.sendAction(169, 444, 5, id, 1);
    };
}