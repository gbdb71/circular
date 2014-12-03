package co.bravebunny.circular.entities.objects;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.Timer.Task;

import co.bravebunny.circular.managers.ActorTween;
import co.bravebunny.circular.managers.Assets;
import co.bravebunny.circular.managers.Particles;
import co.bravebunny.circular.managers.Positions;
import co.bravebunny.circular.screens.GameScreen;
import co.bravebunny.circular.screens.Play;
import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;
import aurelienribon.tweenengine.equations.Back;

public class Enemy extends Solid{
	private float h;
	private float angle = 0;
	private int type;
	private Image body;
	
	private TweenCallback tweenCallback = new TweenCallback()
	{
		@Override
		public void onEvent(int type, BaseTween<?> source)
		{
			if(type == TweenCallback.END) {
				dispose();
			}
		}
	};

	public void init() {
		body = Assets.getImage("level/enemy");
		actors.addActor(body);
		coll_on = false;
		type = MathUtils.random(1);
		h = 400 - type*150;
		coll_radius = 10;
		
		actors.setOrigin(actors.getWidth()/2, actors.getHeight()/2);
		actors.setScale(0);
	}
	
	public void grow(float bpm) {
		//grow to initial size
		Tween.to(actors, ActorTween.SCALE, 60/bpm).target(1 - type*0.3f)
		.ease(Back.OUT).start(GameScreen.getTweenManager());
		
        //turn on collisions
        Timer.schedule(new Task(){
            @Override
            public void run() {
                coll_on = true;
            }
        }, 60/bpm);
		
        //destroy the enemy after some time
        Timer.schedule(new Task(){
            @Override
            public void run() {
                destroy();
            }
        }, 3*60/bpm);
	}
	
	public void destroy() {
		//doesn't do much right now. should be prettier.
		dispose();
	}
	
	//enemy beat effect
	public void beat(){
		//TODO
	}

	public void explode() {
		if (actors.isVisible()) {
			Particles.create(Positions.getCenterX(actors), Positions.getCenterY(actors), "B71C1CFF");
			actors.setVisible(false);
		}
		destroy();
	}
	
	public void render(float delta) {
		//place the enemy in the game screen, and rotate it to an angle in front of the ship
		Positions.setPolarPosition(actors, h, angle);

	}
	
	@Override
	public void setRotation(float degrees) {
		super.setRotation(degrees - 90);
		this.angle = degrees;
		
	}
	
	public void dispose() {
		if (actors != null) {
			actors.remove();
		}
		actors = null;
	}
	
	public boolean isDead() {
		return actors == null;
	}
}