package br.grupointegrado.SpaceInvaders;


/**
 * Created by Raul on 22/09/2015.
 */

import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGeneratorLoader;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageTextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.sun.org.apache.xpath.internal.operations.Or;


public class TelaMenu extends TelaBase {

    private OrthographicCamera camera;
    private Stage palco;
    private ImageTextButton btIniciar;
    private Label lbTitulo;
    private Label lbPontuacao;
    private Label lbNome;
    private BitmapFont fonteBotoes;
    private BitmapFont fontTitulo;
    private BitmapFont fontBotoes;
    private  BitmapFont fonteNome;


    private Texture texturaBotao;

    private  Texture texturaBotaoPressionado;




    public TelaMenu(MainGame game) {
        super(game);
    }

    @Override
    public void show() {
        camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        palco = new Stage( new FillViewport(camera.viewportWidth,camera
                .viewportHeight,camera));
        Gdx.input.setInputProcessor(palco);// define o palco como processador de entradas

        initFonts();
        InitLabel();
        initBotoes();

    }

    private void initBotoes() {
        texturaBotao = new Texture("buttons/button.png");
        texturaBotaoPressionado = new Texture("buttons/button-down.png");
        ImageTextButton.ImageTextButtonStyle estilo = new ImageTextButton.ImageTextButtonStyle();

        estilo.font = fonteBotoes;
        estilo.up = new SpriteDrawable(new Sprite(texturaBotao));
        estilo.down = new SpriteDrawable( new Sprite(texturaBotaoPressionado));

        btIniciar = new ImageTextButton("  Iniciar Jogo  ", estilo);
        palco.addActor(btIniciar);

        btIniciar.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new TelaJogo(game));
            }
        });

    }

    private void InitLabel() {

        Label.LabelStyle estilo = new Label.LabelStyle();
        estilo.font = fontTitulo;

        lbTitulo = new Label("Space Invaders ", estilo);
        palco.addActor(lbTitulo);

        Preferences preferencias = Gdx.app.getPreferences("SpaceInvaders");
        int  PontuacaoMaxima = preferencias.getInteger("pontuacaoMaxima",0);

        estilo = new Label.LabelStyle();
        estilo.font = fontBotoes;


        lbPontuacao = new Label("Pontuação : " + PontuacaoMaxima + " pontos" , estilo);
        palco.addActor(lbPontuacao);

        estilo = new Label.LabelStyle();
        estilo.font = fonteNome;
        lbNome = new Label("Desenvolvido por Raul Adriano Ramos",estilo);
        palco.addActor(lbNome);
    }

    private void initFonts() {

        FreeTypeFontGenerator gerador =
                new FreeTypeFontGenerator(Gdx.files.internal("fonts/roboto.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter params =
                new FreeTypeFontGenerator.FreeTypeFontParameter();
        params.size = 48 ;
        params.color = new Color(255,255,255,1);
        params.shadowOffsetX = 2;
        params.shadowOffsetY = 2;
        params.shadowColor = Color.BLACK;

        fontTitulo = gerador.generateFont(params);


        fontBotoes = gerador.generateFont(params);

        params = new FreeTypeFontGenerator.FreeTypeFontParameter();
        params.size = 32;
        params.color = Color.WHITE;

        fonteBotoes =  gerador.generateFont(params);

        params.size = 25;
        fonteNome = gerador.generateFont(params);

        gerador.dispose();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        atulizarLabels();
        atualizarBotoes();

        palco.act(delta);
        palco.draw();

    }

    private void atualizarBotoes() {
        float x  = camera.viewportWidth/2 - btIniciar.getPrefWidth()/2;
        float y = camera.viewportHeight/2 - btIniciar.getPrefHeight()/2;

        btIniciar.setPosition(x,y);

    }

    private void atulizarLabels() {
        float x = camera.viewportWidth / 2 - lbTitulo.getPrefWidth() / 2;
        float y = camera.viewportHeight - 100;

        lbTitulo.setPosition(x,y);

        x = camera.viewportWidth/2 - lbPontuacao.getPrefWidth()/2;
        y= 100;

        lbPontuacao.setPosition(x,y);



    }

    @Override
    public void resize(int width, int height) {

        camera.setToOrtho( false, width,height);
        camera.update();

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void dispose() {
        palco.dispose();
        fontTitulo.dispose();
        texturaBotao.dispose();
        texturaBotaoPressionado.dispose();

    }
}