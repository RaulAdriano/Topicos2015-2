package br.grupointegrado.SpaceInvaders;

import com.badlogic.gdx.Screen;

/**
 * Created by Raul on 03/08/2015.
 * Todas as Telas do Jogo vao derivar desta telaBase
 */
public abstract class TelaBase implements Screen {

    protected MainGame game;

    public TelaBase(MainGame game){
        this.game = game;
    }


    @Override
    public void hide() {
        dispose();
    }
}
