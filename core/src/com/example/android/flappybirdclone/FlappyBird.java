package com.example.android.flappybirdclone;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Gdx2DPixmap;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;

import org.omg.CORBA.BAD_PARAM;

import java.util.Random;

public class FlappyBird extends ApplicationAdapter {
	SpriteBatch batch;
	Texture background;
	Texture[] bird;
	Texture topTube;
	Texture bottomTube;
	Texture gameOver;


	int spaceBetweenTubes = 500;
	Random random;
	int tubeSpeed = 5;
	int birdStateFlag = 0;
	float flyHeight;
	float fallingSpeed = 0;
	int gameStateFlag = 0;

	int tubesNumber = 5;
	float tubeX[] = new float[tubesNumber];
	float tubeshift[] = new float[tubesNumber];
	float distanceBetweenTubes;

	Circle birdCircle;
	Rectangle[] topTubesRectangle;
	Rectangle[] bottomTubesRectangle;
	BitmapFont scoreFont;

	int gameScore = 0;
	int passedTubeIndex = 0;


//	ShapeRenderer shapeRenderer;

	@Override
	public void create () {
		batch = new SpriteBatch();
		background = new Texture("background.png");
//		shapeRenderer = new ShapeRenderer();

		birdCircle = new Circle();
		topTubesRectangle = new Rectangle[tubesNumber];
		bottomTubesRectangle = new Rectangle[tubesNumber];

		bird = new Texture[2];
		bird[0] = new Texture("bird_wings_up.png");
		bird[1] = new Texture("bird_wings_down.png");

		topTube = new Texture("top_tube.png");
		bottomTube = new Texture("bottom_tube.png");
		random = new Random();
		scoreFont = new BitmapFont();
		scoreFont.setColor(Color.CYAN);
		scoreFont.getData().setScale(10);

		gameOver = new Texture("game_over.png");

		distanceBetweenTubes = Gdx.graphics.getWidth();

		initGame();

	}

	public void initGame() {
		flyHeight = Gdx.graphics.getHeight() / 2 - bird[0].getHeight() / 2;

		for (int i = 0; i < tubesNumber; i++) {
			tubeX[i] = Gdx.graphics.getWidth() / 2 -
					topTube.getWidth() / 2 +
					Gdx.graphics.getWidth() + i *
					distanceBetweenTubes;
			tubeshift[i] = (random.nextFloat() - 0.5f) * (Gdx.graphics.getHeight() - spaceBetweenTubes - 200);
			topTubesRectangle[i] = new Rectangle();
			bottomTubesRectangle[i] = new Rectangle();
		}
	}

	@Override
	public void render () {

		batch.begin();
		batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

		if (gameStateFlag == 1) {

			Gdx.app.log("score", "" + gameScore);

			if (tubeX[passedTubeIndex] < Gdx.graphics.getWidth() / 2) {
				gameScore++;

				if (passedTubeIndex < tubesNumber - 1) {
					passedTubeIndex++;
				} else {
					passedTubeIndex = 0;
				}
			}

			if (Gdx.input.justTouched()) {
				fallingSpeed = -20;
			}

			for (int i = 0; i < tubesNumber; i++) {

				if(tubeX[i] < - topTube.getWidth()) {
					tubeX[i] = tubesNumber * distanceBetweenTubes;
				} else {
					tubeX[i] -= tubeSpeed;
				}

				batch.draw(topTube, tubeX[i],
						Gdx.graphics.getHeight() / 2 + spaceBetweenTubes / 2 + tubeshift[i]);

				batch.draw(bottomTube, tubeX[i],
						Gdx.graphics.getHeight() / 2 - spaceBetweenTubes / 2 -
								bottomTube.getHeight() + tubeshift[i]);

				topTubesRectangle[i] = new Rectangle(tubeX[i],
						Gdx.graphics.getHeight() / 2 + spaceBetweenTubes / 2 + tubeshift[i],
						topTube.getWidth(),
						topTube.getHeight());

				bottomTubesRectangle[i] = new Rectangle(tubeX[i],
						Gdx.graphics.getHeight() / 2 - spaceBetweenTubes / 2 -
								bottomTube.getHeight() + tubeshift[i],
						bottomTube.getWidth(),
						bottomTube.getHeight());

			}

			if (flyHeight > 0) {
				fallingSpeed++;
				flyHeight -= fallingSpeed;
			} else {
				gameStateFlag = 2;
			}

		} else if (gameStateFlag == 0) {
		if (Gdx.input.justTouched()) {
			gameStateFlag = 1;
		}
	} else if(gameStateFlag == 2){
			batch.draw(gameOver, Gdx.graphics.getWidth() / 2 - gameOver.getWidth() / 2,
					Gdx.graphics.getHeight() / 2  - gameOver.getHeight() / 2);

			if (Gdx.input.justTouched()) {
				gameStateFlag = 1;
				initGame();
				gameScore = 0;
				passedTubeIndex = 0;
				fallingSpeed = 0;
			}
	}
		if(birdStateFlag == 0) {
			birdStateFlag = 1;
		} else {
			birdStateFlag = 0;
		}

		batch.draw(bird[birdStateFlag], Gdx.graphics.getWidth() / 2 -bird[birdStateFlag].getWidth() / 2,
				flyHeight);

		scoreFont.draw(batch, String.valueOf(gameScore), 100, 200);

		batch.end();
		birdCircle.set(Gdx.graphics.getWidth() / 2, flyHeight + bird[birdStateFlag].getHeight() / 2,
				bird[birdStateFlag].getWidth() / 2);
//		shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
//		shapeRenderer.setColor(Color.YELLOW);
//		shapeRenderer.circle(birdCircle.x, birdCircle.y, birdCircle.radius);

		for (int i = 0; i < tubesNumber; i++) {

//			shapeRenderer.rect(tubeX[i],
//					Gdx.graphics.getHeight() / 2 + spaceBetweenTubes / 2 + tubeshift[i],
//					topTube.getWidth(),
//					topTube.getHeight());
//			shapeRenderer.rect(tubeX[i],
//					Gdx.graphics.getHeight() / 2 - spaceBetweenTubes / 2 -
//							bottomTube.getHeight() + tubeshift[i], bottomTube.getWidth(), bottomTube.getHeight());

			if(Intersector.overlaps(birdCircle, topTubesRectangle[i]) ||
			Intersector.overlaps(birdCircle, bottomTubesRectangle[i])) {
				gameStateFlag = 2;

			}
		}
//		shapeRenderer.end();

	}
	
	@Override
	public void dispose () {

	}
}
