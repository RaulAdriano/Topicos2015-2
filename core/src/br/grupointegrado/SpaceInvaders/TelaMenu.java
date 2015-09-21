package br.grupointegrado.SpaceInvaders;


/**
 * Created by Raul on 22/09/2015.
 */

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGeneratorLoader;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageTextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.sun.org.apache.xpath.internal.operations.Or;


public class TelaMenu extends TelaBase {

    private OrthographicCamera camera;
    private Stage palco;
    private ImageTextButton btIniciar;
    private Label lbTitulo;
    private Label lbPontuacao;

    private BitmapFont fontTitulo;
    private BitmapFont fontBotores;


    private Texture texturaBotao;
    private Texture texturaBotaoPressionado;


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

    }

    private void InitLabel() {
        Label.LabelStyle estilo = new Label.LabelStyle();
        estilo.font = fontTitulo;

        lbTitulo = new Label("Space Invaders ", estilo);
        palco.addActor(lbTitulo);


    }

    private void initFonts() {

        FreeTypeFontGenerator gerador =
                new FreeTypeFontGenerator(Gdx.files.internal("fonts/roboto.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter params =
                new FreeTypeFontGenerator.FreeTypeFontParameter();
        params.size = 48 ;
        params.color = new Color(.25f,.25f,.85f,1);
        params.shadowOffsetX = 2;
        params.shadowOffsetY = 2;
        params.shadowColor = Color.BLACK;

        fontTitulo = gerador.generateFont(params);

        gerador.dispose();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(1f,1f,1f,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        atulizarLabels();

        palco.act(delta);
        palco.draw();

    }

    private void atulizarLabels() {
        float x = camera.viewportWidth / 2 - lbTitulo.getPrefWidth() / 2;
        float y = camera.viewportHeight - 100;


        lbTitulo.setPosition(x,y);

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

    }
}