package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.sun.org.apache.xpath.internal.operations.Bool;
import sun.rmi.runtime.Log;

import java.util.*;
import java.util.logging.Logger;

public class MyGdxGame extends ApplicationAdapter implements InputProcessor {
	private SpriteBatch batch;
//    private Texture img;
    private Texture imgButtonX, imgButtonO, imgButtonEmpty;
//    private Image spriceGrid; // sprite, spriceButton1, spriceButton2, spriceButton3;
    private BitmapFont gameStatusFont; // font,
//    private String textStatus = "";
//	private Button button1 = new Button();
    private Stage stage;
    private Integer gridSize = 10;
    private Integer buttonSize = 120;
    private Integer buttonCount = 9;
    //    private String[] buttonStatus;
//    private Image[] imageButtons = new Image[buttonCount];
    private String buttonNamePrefix = "button_";
    //    private String gameStatus = "draw";
    private Boolean isYourTurn = true;
    private Boolean isGameEnd = false;
    private Float botThinkingTime = 1f;
    private Float botThinkingTimeCurrent = 0f;
    private Map<Integer, Integer[]> mapLineLogic = new HashMap<>();
    private Map<String, Image> mapButtonImage = new HashMap<>();

    private String gameStatusText = "";
    private Vector2[] lineWinPosition = new Vector2[2];
    private Map<String, String> mapButtonStatus = new HashMap<>();

	@Override
	public void create () {
	    // Logic
        // Horizontal
        mapLineLogic.put(0, new Integer[]{0, 1, 2});
        mapLineLogic.put(1, new Integer[]{3, 4, 5});
        mapLineLogic.put(2, new Integer[]{6, 7, 8});
        // Vertical
        mapLineLogic.put(3, new Integer[]{0, 3, 6});
        mapLineLogic.put(4, new Integer[]{1, 4, 7});
        mapLineLogic.put(5, new Integer[]{2, 5, 8});
        // Cross
        mapLineLogic.put(6, new Integer[]{0, 4, 8});
        mapLineLogic.put(7, new Integer[]{2, 4, 6});

		batch = new SpriteBatch();
//		img = new Texture("badlogic.jpg");
//		sprite = new Sprite(img);
//		sprite.setPosition(
//				Gdx.graphics.getWidth()/2 - sprite.getWidth()/2,
//				Gdx.graphics.getHeight()/2 - sprite.getHeight()/2
//		);
//		font = new BitmapFont();
//		font.setColor(Color.WHITE);
        gameStatusFont = new BitmapFont();
        gameStatusFont.setColor(Color.WHITE);

//		button1.setColor(Color.CORAL);
//		button1.setName("Holaaaa");
//		button1.setPosition(100, 100);

		stage = new Stage(new ScreenViewport());
		Group group = new Group();

		Texture imgGrid;
		imgGrid = new Texture("xo-grid.png");
		imgButtonX = new Texture("xo-button-x.png");
		imgButtonO = new Texture("xo-button-o.png");
		imgButtonEmpty = new Texture("xo-button-empty.png");

		Image spriceGrid;
		spriceGrid = new Image(imgGrid);
//		spriceGrid.setOrigin(0, 0);
		spriceGrid.setName("grid");
		spriceGrid.setPosition(0, 0);
//		spriceGrid.setPosition(spriceGrid.getWidth() / 2, spriceGrid.getHeight() / 2);
		group.addActor(spriceGrid);

//		Array<Image> imageButtons = new Array<Image>();
		int buttonCurrentCol = 0;
		int buttonCurrentRow = 0;
		for (int i = 0; i < buttonCount; i++) {
//			imageButtons[i] = new Image(imgButtonX);
//			imageButtons[i].setName(name);
//			imageButtons[i].setPosition((gridSize * buttonCurrentCol) + gridSize + (buttonSize * buttonCurrentCol), (gridSize * buttonCurrentRow) + gridSize + (buttonSize * buttonCurrentRow));
//			group.addActor(imageButtons[i]);

            String name = buttonNamePrefix + i;
            Image image = new Image(imgButtonEmpty);
			image.setName(name);
			image.setPosition((gridSize * buttonCurrentCol) + gridSize + (buttonSize * buttonCurrentCol), (gridSize * buttonCurrentRow) + gridSize + (buttonSize * buttonCurrentRow));
			group.addActor(image);

            mapButtonImage.put(name, image);
            mapButtonStatus.put(name, "");

			if (buttonCurrentCol < 2) {
				buttonCurrentCol++;
			} else {
				buttonCurrentCol = 0;
				buttonCurrentRow++;
			}

		}

//		spriceButton1 = new Image(imgButtonEmpty);
//		spriceButton1.setName("button1");
//		spriceButton1.setPosition(gridSize, gridSize);
////		spriceButton1.setOrigin(0, 0);
//		spriceButton1.setDrawable(new SpriteDrawable(new Sprite(imgButtonO)));

		group.setOrigin(0, 0);
//		group.setPosition(0, 0);
		group.setPosition(Gdx.graphics.getWidth() / 2f - spriceGrid.getWidth() / 2, Gdx.graphics.getHeight() / 2f - spriceGrid.getHeight() / 2);
//		group.addActor(spriceButton1);
		stage.addActor(group);

        randomTurn();

		Gdx.input.setInputProcessor(this);
	}

	@Override
	public void render () {

//		textStatus = "Hola";

//		if (Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
////			sprite.setPosition(Gdx.input.getX(), Gdx.graphics.getHeight() - Gdx.input.getY());
////			textStatus = "Pressed " + Gdx.input.getX() + " , " + Gdx.input.getY();
//			Gdx.app.log("click", "pressed");
//		}

//		if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
//			textStatus = "Just Pressed";
//			Gdx.app.log("click", "just pressed");
//		}

//		if (Gdx.input.isTouched()) {
////			textStatus = "Touched " + Gdx.input.getX() + " , " + Gdx.input.getY();
//		}

//		if (Gdx.input.isTouched()) {
//			System.out.println("Input occurred at x=" + Gdx.input.getX() + ", y=" + Gdx.input.getY());
//		}



        if (!isGameEnd) {
            if (isYourTurn) {
                gameStatusText = "Your Turn";
            } else {
                // Bot Play
                gameStatusText = "Wait";
                if (botThinkingTimeCurrent < botThinkingTime) {
                    botThinkingTimeCurrent += Gdx.graphics.getDeltaTime();
                } else {
                    updateButtonStatus(buttonNamePrefix + getBotAnswerButtonNum(), false);
                    botThinkingTimeCurrent = 0f;
                }
            }
        }

		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		batch.begin();
//		batch.draw(sprite, sprite.getX(), sprite.getY());
//		batch.draw(spriceButton1, spriceButton1.getX(), spriceButton1.getY());
//		font.draw(batch, textStatus, 20, 30);
        gameStatusFont.getData().setScale(2f);
        gameStatusFont.draw(batch, gameStatusText, Gdx.graphics.getWidth() / 2f - 150, Gdx.graphics.getHeight() - 70, 300, Align.center, false);

        if (isGameEnd) {
            gameStatusFont.getData().setScale(1.5f);
            gameStatusFont.draw(batch, "Tap to Play again", Gdx.graphics.getWidth() / 2f - 150, 75, 300, Align.center, false);
        }

		batch.end();

//		stage.act(Gdx.graphics.getDeltaTime());
		stage.draw();

        // Draw line win
        if (lineWinPosition[0] != null) {
            ShapeRenderer shapeRenderer = new ShapeRenderer();
            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            shapeRenderer.setColor(1, 0, 0, 1); // Red line
            shapeRenderer.rectLine(lineWinPosition[0], lineWinPosition[1], 10);
            shapeRenderer.end();
        }
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		stage.dispose();
	}

	@Override
	public boolean keyDown(int keycode) {
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
//		textStatus = "touchDown";

        if (isGameEnd) {
            restart();
        } else {
            if (isYourTurn) {
                Vector2 coords = stage.screenToStageCoordinates(new Vector2(Gdx.input.getX(), Gdx.input.getY()));
                Actor hitActor = stage.hit(coords.x, coords.y, false);

                if (hitActor != null) {
//			        hitActor .setDrawable(new SpriteDrawable(new Sprite(imgButtonO)));
//                  imageButtons[0].setDrawable(new SpriteDrawable(new Sprite(imgButtonO)));

//                textStatus = "Hit " + hitActor.getName();
                    String actorName = hitActor.getName();

                    if (actorName.contains(buttonNamePrefix)) {
                        updateButtonStatus(actorName, true);
                    }
                }
            }
        }

		return true;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		return false;
	}

    private void restart() {
        gameStatusText = "";
        lineWinPosition = new Vector2[2];

        // Clear status button
        for (int i = 0; i < mapButtonStatus.size(); i++) {
            String name = buttonNamePrefix + i;

            mapButtonImage.get(name).setDrawable(new SpriteDrawable(new Sprite(imgButtonEmpty)));
            mapButtonStatus.put(name, "");
        }

        randomTurn();
        isGameEnd = false;
    }

    private void randomTurn() {
        Random random = new Random();
        isYourTurn = random.nextBoolean();
    }

	private int getBotAnswerButtonNum() {
        List<Integer> availableButton = new ArrayList<Integer>();

//	    for (int i = 0; i < mapButtonStatus.size(); i++) {
//	        if (mapButtonStatus.get(buttonNamePrefix + i).equals("")) {
//                availableButton.add(i);
//            }
//        }

//	    @TODO Smart Random 1. Check if i has 3 point? next check player has 3 point? next check i has 2point and 1

        int result = -1;

        for (Integer i = 0; i < mapLineLogic.size(); i++) {
            Integer[] logic = mapLineLogic.get(i);

            int playerCountSame = 0;
            int BotCountSame = 0;
            int availableNumCurrent = -1;
            for (Integer num:logic) {

                if (mapButtonStatus.get(buttonNamePrefix + num).equals("o")) {
                    playerCountSame++;
                } else if (mapButtonStatus.get(buttonNamePrefix + num).equals("x")) {
                    BotCountSame++;
                } else {
                    availableNumCurrent = num;
                    availableButton.add(num);
                }
            }

            if (playerCountSame + BotCountSame < logic.length) {
                if (BotCountSame == 2) {
                    result = availableNumCurrent;
                    break;
                } else if (playerCountSame == 2) {
                    result = availableNumCurrent;
                }
            }
        }

        if (result == -1) {
            Random random = new Random();
            int randomIndex = random.nextInt(availableButton.size());
            result = availableButton.get(randomIndex);
        }

        return result;
    }

	private void checkWin(Boolean isPlayer) {
        boolean isWin = false;
        String symbol = (isPlayer) ? "o" : "x";

        for (Integer i = 0; i < mapLineLogic.size(); i++) {
            Integer[] logic = mapLineLogic.get(i);

            Integer countSame = 0;
            for (Integer num:logic) {
                if (mapButtonStatus.get(buttonNamePrefix + num).equals(symbol)) {
                    countSame++;
                }
            }

            if (countSame.equals(logic.length)) {
                isWin = true;

                // Save Position button for draw line
                Vector2 button_start = mapButtonImage.get(buttonNamePrefix + logic[0]).localToStageCoordinates(new Vector2(0, 0));
                Vector2 button_end = mapButtonImage.get(buttonNamePrefix + logic[2]).localToStageCoordinates(new Vector2(0, 0));
                lineWinPosition[0] = new Vector2(button_start.x + buttonSize / 2f, button_start.y + buttonSize / 2f);
                lineWinPosition[1] = new Vector2(button_end.x + buttonSize / 2f, button_end.y + buttonSize / 2f);
            }
        }

        if (isWin) {
            isGameEnd = true;
            isYourTurn = false;
            gameStatusText = (isPlayer) ? "YOU WIN!" : "YOU LOSE!";
        } else {
            // Check Draw
            int countClickedButton = 0;
            for (int i = 0; i < mapButtonStatus.size(); i++) {
                if (!mapButtonStatus.get(buttonNamePrefix + i).equals("")) {
                    countClickedButton++;
                }
            }

            if (countClickedButton == buttonCount) {
                isGameEnd = true;
                isYourTurn = false;
                gameStatusText = "DRAW!";
            }
        }

    }

	private void updateButtonStatus(String buttonName, Boolean isPlayer) {
        if (isButtonAvailable(buttonName)) {
            isYourTurn = !isPlayer;
            String status = "x";
            Texture texture = imgButtonX;

            if (isPlayer) {
                status = "o";
                texture = imgButtonO;
            }

            mapButtonStatus.put(buttonName, status);
            mapButtonImage.get(buttonName).setDrawable(new SpriteDrawable(new Sprite(texture)));

            checkWin(isPlayer);

//            textStatus = "Update " + buttonName;
        }
    }

	private boolean isButtonAvailable(String buttonName) {
        return mapButtonStatus.get(buttonName).equals("");
    }
}
