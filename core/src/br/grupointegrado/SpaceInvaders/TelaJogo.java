package br.grupointegrado.SpaceInvaders;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.viewport.FillViewport;

/**
 * Created by Raul on 03/08/2015.
 * Tela do jogo Principal
 */
public class TelaJogo extends TelaBase {

    //objeto de camera do jogo - Camera ortografica 2D
    private OrthographicCamera camera;
    //SpriteBatch - desenha as imagens do Jogo
    private SpriteBatch batch;

    private Stage palco;
    private BitmapFont fonte;
    private Label lbPontuacao;





    /**
     * Construtor padrao da tela de jogo
     * @param game - referencia para a classe principal
     */
    public  TelaJogo(MainGame game){
        super(game);
    }


    /**
     * Chamado quando a tela é exibida
     */
    @Override
    public void show() {
        camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());  //inicia a camera com os tamanhos de altura e largura da tela do jogo.
        batch = new SpriteBatch();
        palco = new Stage(new FillViewport(camera.viewportWidth, camera.viewportHeight, camera));

        initFont();
        initInformacoes();


    }

    /**
     * inicia todas as informacoes do jogo
     */
    private void initInformacoes() {
        Label.LabelStyle lbEstilo = new Label.LabelStyle();
        lbEstilo.font = fonte;
        lbEstilo.fontColor = Color.WHITE;

        lbPontuacao = new Label("0 pontos",lbEstilo);
        palco.addActor(lbPontuacao);
    }

    /**
     * e chamado para iniciar a fonte do jogo
     */
    private void initFont() {

        fonte = new BitmapFont();
    }


    /**
     * Chamado a todo quadro de atualização do jogo ( FPS )
     * @param delta - tempo um quadro e outro (em segundos)
     */
    @Override
    public void render(float delta) {
        //LIMPAR A TELA
        Gdx.gl.glClearColor(.15f,.15f, .25f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        lbPontuacao.setPosition(10,camera.viewportHeight-40);
        palco.act(delta);
        palco.draw();



    }

    /**
     * é chamado sempre que há uma alteração no tamanho da tela
     * @param width -  novo valor de largura da tela
     * @param height-  novo valor de altura da tela
     */
    @Override
    public void resize(int width, int height) {
        camera.setToOrtho(false,width,height);
        camera.update();
    }


    /**
     * é chamado sempre que o jogo foi minimizado
     */
    @Override
    public void pause() {

    }

    /**
     * é chamado sempre que o jogo voltar para o primeiro plano
     */
    @Override
    public void resume() {

    }

    /**
     * é chamado quando a tela for destruida
     */
    @Override
    public void dispose() {
        batch.dispose();
        palco.dispose();
        fonte.dispose();

    }
}
