package com.game;

import android.util.Log;

import plia.manager.SpriteFontManager;
import plia.node.group.Layer;
import plia.node.group.Scene;
import plia.node.leaf.Text;

/**
 * Created by Wirune on 1/9/2556.
 */
public class Scene1 extends Scene
{
    private Layer layer;
    private Text text;

    public Scene1()
    {
        layer = new Layer();
        text = new Text();
        text.setSpriteFont(SpriteFontManager.getInstance().getSpriteFont("nexa_light"));
        text.setText("askjdnaksjdnakjhdbhwqbdwioquehqirqiwuheqiwuheqiwuehqiwuehqiweghskjdnaksjdnakjhdbhwqbdwioquehqirqiwuheqiwuheqiwuehqiwuehqiweghskjdnaksjdnakjhdbhwqbdwioquehqirqiwuheqiwuheqiwuehqiwuehqiweghskjdnaksjdnakjhdbhwqbdwioquehqirqiwuheqiwuheqiwuehqiwuehqiweghskjdnaksjdnakjhdbhwqbdwioquehqirqiwuheqiwuheqiwuehqiwuehqiweghskjdnaksjdnakjhdbhwqbdwioquehqirqiwuheqiwuheqiwuehqiwuehqiweghskjdnaksjdnakjhdbhwqbdwioquehqirqiwuheqiwuheqiwuehqiwuehqiweghskjdnaksjdnakjhdbhwqbdwioquehqirqiwuheqiwuheqiwuehqiwuehqiweghskjdnaksjdnakjhdbhwqbdwioquehqirqiwuheqiwuheqiwuehqiwuehqiweghskjdnaksjdnakjhdbhwqbdwioquehqirqiwuheqiwuheqiwuehqiwuehqiweghskjdnaksjdnakjhdbhwqbdwioquehqirqiwuheqiwuheqiwuehqiwuehqiweghskjdnaksjdnakjhdbhwqbdwioquehqirqiwuheqiwuheqiwuehqiwuehqiweghskjdnaksjdnakjhdbhwqbdwioquehqirqiwuheqiwuheqiwuehqiwuehqiweghskjdnaksjdnakjhdbhwqbdwioquehqirqiwuheqiwuheqiwuehqiwuehqiweghskjdnaksjdnakjhdbhwqbdwioquehqirqiwuheqiwuheqiwuehqiwuehqiweghskjdnaksjdnakjhdbhwqbdwioquehqirqiwuheqiwuheqiwuehqiwuehqiweghskjdnaksjdnakjhdbhwqbdwioquehqirqiwuheqiwuheqiwuehqiwuehqiweghskjdnaksjdnakjhdbhwqbdwioquehqirqiwuheqiwuheqiwuehqiwuehqiweghskjdnaksjdnakjhdbhwqbdwioquehqirqiwuheqiwuheqiwuehqiwuehqiweghskjdnaksjdnakjhdbhwqbdwioquehqirqiwuheqiwuheqiwuehqiwuehqiweghskjdnaksjdnakjhdbhwqbdwioquehqirqiwuheqiwuheqiwuehqiwuehqiweghskjdnaksjdnakjhdbhwqbdwioquehqirqiwuheqiwuheqiwuehqiwuehqiwegh");
        text.setPosition(0.15f, 0.75f);
        text.setSize(0.7f, 0.2f);text.setColor(1, 1, 1);
        text.setFontSize(32);

        layer.addChild(text);
        addChild(layer);
    }

    int count = 0;
    @Override
    public void update(float elapsedTime)
    {
        super.update(elapsedTime);
        Log.println(Log.ASSERT, "FPS", (1000f / elapsedTime) + "");

        if(count++ > 100)
        {
            text.scrollDown();
            count = 0;
        }
    }
}
