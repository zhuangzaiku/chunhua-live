package com.quootta.mdate.plugin;

import java.util.ArrayList;
import java.util.List;

import io.rong.imkit.DefaultExtensionModule;
import io.rong.imkit.plugin.IPluginModule;
import io.rong.imkit.plugin.ImagePlugin;
import io.rong.imlib.model.Conversation;

public class MyExtensionModule extends DefaultExtensionModule {


    @Override
    public List<IPluginModule> getPluginModules(Conversation.ConversationType conversationType) {


        //List<IPluginModule> pluginModules=super.getPluginModules(conversationType);
//        for (int i=0;i<pluginModules.size();i++){
//
//            if (pluginModules.get(i).obtainTitle(RongContext.getInstance()).equals("语音聊天") ){
//
//                pluginModules.remove(pluginModules.get(i));
//            }
//        }
        List<IPluginModule> pluginModules=new ArrayList<>();
        pluginModules.add(new ImagePlugin());
        pluginModules.add(new MyAudioPlugin());
        pluginModules.add(new MyVideoPlugin());
        pluginModules.add(new MyGiftPlugin());
       // pluginModules.add(new FilePlugin());

        return pluginModules;
    }
}
