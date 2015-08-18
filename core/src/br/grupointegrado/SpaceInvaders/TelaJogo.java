package br.grupointegrado.SpaceInvaders;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.utils.Array;
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
    private Image jogador;
    private Texture texturaJogador;
    private Texture texturaJogadorDireita;
    private Texture texturaJogadorEsquerda;
    private boolean indoDireita;
    private boolean indoEsquerda;
    private boolean atirando;
    private Array<Image> tiros = new Array<Image>();
    private  Texture texturaTiro;

    private Texture texturaMeteoro1;
    private Texture texturaMeteoro2;
    private Array<Image> meteoros1 = new Array<Image>();
    private Array<Image> meteoros2 = new Array<Image>();


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

        initTexturas();
        initFont();
        initInformacoes();
        initJogador();

    }

    private void initTexturas() {
        texturaTiro = new Texture("sprites/shot.png");
        texturaMeteoro1 = new Texture("sprites/enemie-1.png");
        texturaMeteoro2 = new Texture("sprites/enemie-2.png");
    }

    /**
     * Instancia os objetos do jogador e adiciona no palco
     */
    private void initJogador() {
        texturaJogador = new Texture("sprites/player.png");
        texturaJogadorDireita = new Texture("sprites/player-right.png");
        texturaJogadorEsquerda = new Texture("sprites/player-left.png");

        jogador = new Image(texturaJogador);

        float x = camera.viewportWidth/2 - jogador.getWidth()/2;
        float y = 15;

        jogador.setPosition(x, y);

        palco.addActor(jogador);


    }

    /**
     * instancia as informações escritas na tela
     */
    private void initInformacoes() {
        Label.LabelStyle lbEstilo = new Label.LabelStyle();
        lbEstilo.font = fonte;
        lbEstilo.fontColor = Color.WHITE;

        lbPontuacao = new Label("0 pontos",lbEstilo);
        palco.addActor(lbPontuacao);
    }

    /**
     * instancia os objetos de fonte
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

        lbPontuacao.setPosition(10, camera.viewportHeight - 40);
        capturaTeclas();
        atualizarJogador(delta);
        atualizarTiros(delta);
        atualizarMeteoros(delta);


        // Atualiza a situação do palco
        palco.act(delta);
        //desenha o palco na tela
        palco.draw();



    }

    private void atualizarMeteoros(float delta) {
        int tipo = MathUtils.random(1,3);

        if (tipo == 1){
            //cria meteoro 1
            Image meteoro = new Image(texturaMeteoro1);
            float x = MathUtils.random(0,camera.viewportWidth - meteoro.getWidth());
            float y = MathUtils.random(camera.viewportHeight, camera.viewportHeight*2);
            meteoro.setPosition(x,y);
            meteoros1.add(meteoro);
            palco.addActor(meteoro);
        }else{
            //cria meteoro 2
        }

        float velocidade = 300;
        for (Image meteoro : meteoros1){
            float x = meteoro.getX();
            float y = meteoro.getY() - velocidade * delta;
            meteoro.setPosition(x,y);
        }

    }

    private final float MIN_INTERVALO_TIROS = 0.25f; //minimo de tempo entre os tiros
    private float intervaloTiros = 0; //tempo acumulado entre os tiros

    private void atualizarTiros(float delta) {
        intervaloTiros = intervaloTiros + delta;//acumula o tempo percorrido
        //cria um novo tiro se necessario
        if (atirando) {
            //verifica se o temoi minimo foi atingido
            if (intervaloTiros >= MIN_INTERVALO_TIROS) {
                Image tiro = new Image(texturaTiro);
                float x = jogador.getX() + jogador.getWidth() / 2 - tiro.getWidth()/2;
                float y = jogador.getY() + jogador.getHeight();
                tiro.setPosition(x, y);
                tiros.add(tiro);
                palco.addActor(tiro);
                intervaloTiros = 0;
            }
        }

        float velocidade = 600;//velocidade do tiro

        //percorre todos os tiros
        for (Image tiro : tiros){
            //movimenta o tiro em direcao ao topo
            float x = tiro.getX();
            float y = tiro.getY()+ velocidade*delta;
            tiro.setPosition(x,y);

            //remove os tiros que sairam da tela
            if (tiro.getY()> camera.viewportHeight){
                tiros.removeValue(tiro, true); //remove da lista
                tiro.remove();//remove do palco
            }
        }
    }

    /**
     * atualiza a velocidade do jogador
     * @param delta
     */
    private void atualizarJogador(float delta) {
        float velocidade = 400; //velocidade de movimento do jogador
        if (indoDireita){
            //verifica se o jogador esta dentro da tela
            if(jogador.getX() < camera.viewportWidth - jogador.getWidth()){
                float x = jogador.getX()+ velocidade * delta;
                float y = jogador.getY();
                jogador.setPosition(x,y);
            }

        }
        if (indoEsquerda){
            //verifica se o jogador esta dentro da tela
            if(jogador.getX() > 0) {
                float x = jogador.getX() - velocidade * delta;
                float y = jogador.getY();
                jogador.setPosition(x, y);
            }
        }

        if(indoDireita){
            jogador.setDrawable(new SpriteDrawable(new Sprite(texturaJogadorDireita)));
        }else if(indoEsquerda){
            jogador.setDrawable(new SpriteDrawable(new Sprite(texturaJogadorEsquerda)));
        }else{
            jogador.setDrawable(new SpriteDrawable(new Sprite(texturaJogador)));
        }
    }

    /**
     * verifica se as teclas estão pressionadas
     */
    private void capturaTeclas() {
        indoDireita = false;
        indoEsquerda = false;
        atirando =false;

        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)){
            indoEsquerda = true;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)){
            indoDireita = true;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.SPACE)){
            atirando = true;
        }
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
        texturaJogadorDireita.dispose();
        texturaJogadorEsquerda.dispose();
        texturaJogador.dispose();
        texturaTiro.dispose();
        texturaMeteoro1.dispose();
        texturaMeteoro2.dispose();

    }
}
