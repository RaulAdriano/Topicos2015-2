package br.grupointegrado.SpaceInvaders;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageTextButton;
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
    private Stage palcoInformacoes;
    private BitmapFont fonte;

    private Label lbPontuacao;
    private Label lbGameOver;
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

    private Array<Texture> texturesExplosao  = new Array<Texture>();
    private Array<Explosao> explosoes = new Array<Explosao>();



    private Sound somTiro;
    private Sound somExplosao;
    private Sound somGameOver;
    private Music  musicaFundo;


    /**
     * Construtor padrao da tela de jogo
     * @param game - referencia para a classe principal
     */
    public  TelaJogo(MainGame game){
        super(game);
    }


    /**
     * Chamado quando a tela � exibida
     */
    @Override
    public void show() {
        camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());  //inicia a camera com os tamanhos de altura e largura da tela do jogo.
        batch = new SpriteBatch();
        palco = new Stage(new FillViewport(camera.viewportWidth, camera.viewportHeight, camera));
        palcoInformacoes = new Stage(new FillViewport(camera.viewportWidth, camera.viewportHeight, camera));



        initSons();
        initTexturas();
        initFont();
        initInformacoes();
        initJogador();

    }



    private void initSons() {
        somTiro = Gdx.audio.newSound(Gdx.files.internal("sounds/shoot.mp3"));
        somExplosao = Gdx.audio.newSound(Gdx.files.internal("sounds/explosion.mp3"));
        somGameOver = Gdx.audio.newSound(Gdx.files.internal("sounds/gameOver.mp3"));
        musicaFundo = Gdx.audio.newMusic(Gdx.files.internal("sounds/background.mp3"));
        musicaFundo.setLooping(true);
    }

    private void initTexturas() {
        texturaTiro = new Texture("sprites/shot.png");
        texturaMeteoro1 = new Texture("sprites/enemie-1.png");
        texturaMeteoro2 = new Texture("sprites/enemie-2.png");

        for (int i =1; i<=17; i++){
            Texture text = new Texture("sprites/explosion-"+ i +".png");
            texturesExplosao.add(text);
        }
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
     * instancia as informacoes escritas na tela
     */
    private void initInformacoes() {

        Label.LabelStyle lbEstilo = new Label.LabelStyle();
        lbEstilo.font = fonte;
        lbEstilo.fontColor = Color.WHITE;

        lbPontuacao = new Label("0 pontos",lbEstilo);
        palcoInformacoes.addActor(lbPontuacao);

        lbGameOver = new Label("Game Over",lbEstilo);
        lbGameOver.setVisible(false);
        palcoInformacoes.addActor(lbGameOver);
    }

    /**
     * instancia os objetos de fonte
     */
    private void initFont() {

        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/roboto.ttf"));

        FreeTypeFontGenerator.FreeTypeFontParameter param = new FreeTypeFontGenerator.FreeTypeFontParameter();

        param.color = Color.WHITE;
        param.size = 25;
        param.shadowOffsetX = 2;
        param.shadowOffsetY = 2;
        param.shadowColor = Color.BLACK;

        fonte = generator.generateFont(param);


        generator.dispose();
    }


    /**
     * Chamado a todo quadro de atualiza��o do jogo ( FPS )
     * @param delta - tempo um quadro e outro (em segundos)
     */
    @Override
    public void render(float delta) {
        //LIMPAR A TELA
        Gdx.gl.glClearColor(.15f, .15f, .25f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        lbPontuacao.setPosition(10, camera.viewportHeight - 40);
        lbGameOver.setPosition(camera.viewportWidth / 2 - lbGameOver.getWidth() / 2, camera.viewportHeight / 2);
        lbGameOver.setVisible(gameOver == true);

        atualizarExplosoes(delta);
        if(gameOver==false) {
            if (!musicaFundo.isPlaying()) //se nao estiver tocando
                    musicaFundo.play();// inicia a musica

            capturaTeclas();
            atualizarJogador(delta);
            atualizarTiros(delta);
            atualizarMeteoros(delta);
            detectarColisoes(meteoros1, 10);
            detectarColisoes(meteoros2, 20);

            updatePontos();

        }else{
            if(musicaFundo.isPlaying())// se esta tocando
                musicaFundo.stop();// parar musica

            reiniciarJogo();
        }


        // Atualiza a situa��o do palco
        palco.act(delta);
        //desenha o palco na tela
        palco.draw();

        palcoInformacoes.act(delta);
        palcoInformacoes.draw();


    }

    private void reiniciarJogo() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER)){

            Preferences preferencias = Gdx.app.getPreferences("SpaceInvaders");
            int  PontuacaoMaxima = preferencias.getInteger("pontuacaoMaxima",0);

            if (pontuacao> PontuacaoMaxima){
                preferencias.putInteger("pontuacaoMaxima",pontuacao);
                preferencias.flush();
            }

            game.setScreen(new TelaMenu(game));
        }

    }

    private void atualizarExplosoes(float delta) {
        for (Explosao explosao : explosoes){
            //verifica se a explosao chegou ao fim
            if (explosao.getEstagio()>=16){
                //chegou ao fim
                explosoes.removeValue(explosao,true); //remove a explosao do array
                explosao.getAtor().remove();// remove o ator do palco
            }else{
                //ainda nao chegou ao fim
                explosao.atualizar(delta);
            }
        }
    }

    private void updatePontos() {
        lbPontuacao.setText(pontuacao + " Pontos");
    }

    private Rectangle recJogador = new Rectangle();
    private Rectangle recTiro = new Rectangle();
    private Rectangle recMeteoro = new Rectangle();
    private int pontuacao=0;
    private boolean gameOver = false;


    private void detectarColisoes(Array<Image> meteoros, int valePonto) {
        recJogador.set(jogador.getX(),jogador.getY(),jogador.getImageWidth(),jogador.getImageHeight());
        for (Image meteoro : meteoros) {
            recMeteoro.set(meteoro.getX(), meteoro.getY(), meteoro.getImageWidth(), meteoro.getImageHeight());
            for (Image tiro : tiros) {
                recTiro.set(tiro.getX(), tiro.getY(), tiro.getImageWidth(), tiro.getImageHeight());

                if (recMeteoro.overlaps(recTiro)) { // se o meteoro 1 contem as coordenadas do tiro, colidem.
                    //ocorre uma colisao entre o tiro e meteoro 1
                    pontuacao += valePonto;
                    tiro.remove();//remover do palco
                    tiros.removeValue(tiro, true); //remove da lista
                    meteoro.remove(); //remove do palco
                    meteoros.removeValue(meteoro, true); // remove da lista

                    criarExplosao(meteoro.getX(), meteoro.getY());
                    somGameOver.play();
                }
            }

            //detecta colisao com o player
            if (recJogador.overlaps(recMeteoro)){
                //ocorre colisao do jogador com meteoro 1
                gameOver = true;
            }
        }


    }

    /**
     * cria a explosao na posicao x e y
     * @param x
     * @param y
     */
    private void criarExplosao(float x, float y) {

        Image ator = new Image(texturesExplosao.get(0));
        ator.setPosition(x,y);
        ator.setPosition(x - ator.getWidth() / 2 , y - ator.getHeight()/ 2);
        palco.addActor(ator);

        Explosao explosao = new Explosao(ator, texturesExplosao);
        explosoes.add(explosao);
        somExplosao.play();

    }

    private void atualizarMeteoros(float delta) {
        int quantidadeMeteoros = meteoros1.size + meteoros2.size; //retorna quantidades de meteoros criados


        if (quantidadeMeteoros < 25) {
            int tipo = MathUtils.random(1, 6); //retorna 1 ou 2 aleat�riamente
            if (tipo == 1) {
                //cria meteoro 1
                Image meteoro = new Image(texturaMeteoro1);
                float x = MathUtils.random(0, camera.viewportWidth - meteoro.getWidth());
                float y = MathUtils.random(camera.viewportHeight, camera.viewportHeight * 2);
                meteoro.setPosition(x, y);
                meteoros1.add(meteoro);
                palco.addActor(meteoro);
            } else if(tipo==2){
                //cria meteoro 2
                Image meteoro = new Image(texturaMeteoro2);
                float x = MathUtils.random(0, camera.viewportWidth - meteoro.getWidth());
                float y = MathUtils.random(camera.viewportHeight, camera.viewportHeight * 2);
                meteoro.setPosition(x, y);
                meteoros2.add(meteoro);
                palco.addActor(meteoro);
            }
        }

        float velocidade = 150; // 200 pixels por segundo
        for (Image meteoro : meteoros1){
            float x = meteoro.getX();//atualiza a posicao do meteoro
            float y = meteoro.getY() - velocidade * delta;
            meteoro.setPosition(x,y);

            if (meteoro.getY()+meteoro.getHeight() < 0 ){
                meteoro.remove(); //remove do palco
                meteoros1.removeValue(meteoro, true); // remove da lista
                pontuacao -= 30;
            }
        }

        float velocidade2 = 200; //300 pixels por segundo
        for (Image meteoro : meteoros2){
            float x = meteoro.getX();
            float y = meteoro.getY() - velocidade2 * delta;
            meteoro.setPosition(x,y);
            if (meteoro.getY()+meteoro.getHeight() < 0 ){
                meteoro.remove(); //remove do palco
                meteoros2.removeValue(meteoro,true); // remove da lista
                pontuacao -= 60;
            }
        }

    }

    private final float MIN_INTERVALO_TIROS = 0.15f; //minimo de tempo entre os tiros
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
                somTiro.play();
            }
        }

        float velocidade = 600;//velocidade do tiro

        //percorre todos os tiros
        for (Image tiro : tiros){
            //movimenta o tiro em direcao ao topo
            float x = tiro.getX();
            float y = tiro.getY()+ velocidade*delta;
            tiro.setPosition(x, y);

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
     * verifica se as teclas est�o pressionadas
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
     * � chamado sempre que h� uma altera��o no tamanho da tela
     * @param width -  novo valor de largura da tela
     * @param height-  novo valor de altura da tela
     */
    @Override
    public void resize(int width, int height) {
        camera.setToOrtho(false,width,height);
        camera.update();
    }


    /**
     * � chamado sempre que o jogo foi minimizado
     */
    @Override
    public void pause() {

    }

    /**
     * � chamado sempre que o jogo voltar para o primeiro plano
     */
    @Override
    public void resume() {

    }

    /**
     * � chamado quando a tela for destruida
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
        somTiro.dispose();
        somExplosao.dispose();
        somGameOver.dispose();
        musicaFundo.dispose();
        texturaMeteoro2.dispose();
        for(Texture text : texturesExplosao){
            text.dispose();
        }

    }
}
