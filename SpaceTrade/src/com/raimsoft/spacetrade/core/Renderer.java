package com.raimsoft.spacetrade.core;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.EdgeShape;
import com.badlogic.gdx.physics.box2d.MassData;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.raimsoft.spacetrade.Global;

public class Renderer
{
	private float FPS= 0.0f;				// 초당 프레임
	//private float LOOP= 0.0f;				// 원루프에 걸리는 시간
	
	private final float CHAR_WID= 150f;		// 캐릭터 가로값
	private final float CHAR_HEI= 200f;		// 캐릭터 세로값
	
	private final float V_JUMP= 15550f;		// 캐릭터 점프 벡터값 (Y)
	private final float V_VELOCITY= 2f;	// 캐릭터 좌우 속도 (X)
	private final int   N_DENCITY= 50;		// 캐릭터 밀도 (무게?)
	private final float V_DAMPING= 5.0f;	// 댐핑
	
	/**
	 * Box2d works best with small values. If you use pixels directly you will
	 * get weird results -- speeds and accelerations not feeling quite right.
	 * Common practice is to use a constant to convert pixels to and from
	 * "meters".
	 */
	// Box2d에서는 작은 값으로 동작하는 것이 좋다. 픽셀을 미터단위로 변환해주는 상수 
	public static final float PIXELS_PER_METER = 60.0f;
	
	
	// 마지막 렌더 시각 (frame-rate에 활용)
	private long now;
	private long lastRender;
	private GLFieldRenderer renderer;

	// 맵에서 그려지지 않은 별도의 텍스쳐들
	private Texture overallTexture;

	// 캐릭터 스프라이트
	private Sprite sprite_ball;

	// 스프라이트를 그릴 때 쓰는 도구
	private SpriteBatch spriteBatch, txtBatch;

	// box2d 에서 "container" 오브젝트.
	// 모든 "Body"는 이 안에 그려진다. 모든 오브젝트가 World를 통해 시뮬레이트된다. 
	private World world;

	// 캐릭터의 물리적 Body
	private Body body_char;
	private Body body_line1, body_line2, body_line3, body_line4;

	// Box2DDebugRenderer는 libgdx에서 테스트코드이다.
	// world의 충돌 바운더리를 만들어준다.
	// 테스트용이기 때문에 다소 느리다.
	//private Box2DDebugRenderer debugRenderer;
	// 프레임 표시용 텍스트
	private BitmapFont txt_frame;

	
	int wid, hei;
	
	public Renderer(int w, int h)
	{
		this.wid= w;
		this.hei= h;
		
		renderer = new GLFieldRenderer();
		
		//모든 텍스쳐를 불러온다.
		overallTexture = new Texture(Gdx.files.internal("data/resource1.png"));
		overallTexture.setFilter(TextureFilter.Linear, TextureFilter.Linear);

		sprite_ball = new Sprite(overallTexture, 0, 0, (int)CHAR_WID, (int)CHAR_HEI);

		spriteBatch = new SpriteBatch();
		txtBatch = new SpriteBatch();

		// world의 중력값을 이 생성자를 통해서 입력할 수 있다. (x,y 방향 입력 가능)
		world = new World(new Vector2(0.0f, Global.V_GRAVITY_Y), false);
		
		

		// 바디의 정보들을 넣어준다.
		BodyDef DEF_charBody = new BodyDef();
		DEF_charBody.type = BodyDef.BodyType.DynamicBody;	// 다이나믹바디이고
		DEF_charBody.position.set(5.0f, 2.0f);			// 처음 위치정보
		
		BodyDef DEF_line1 = new BodyDef();
		DEF_line1.type = BodyDef.BodyType.StaticBody;	// 고정바디
		DEF_line1.position.set(0.0f, 0.0f);			// 처음 위치정보
		
		BodyDef DEF_line2 = new BodyDef();
		DEF_line2.type = BodyDef.BodyType.StaticBody;	// 고정바디
		DEF_line2.position.set(0.0f, this.wid);			// 처음 위치정보

		// 생성한 정보를 Body에 대입
		body_char = world.createBody(DEF_charBody);
		body_line1= world.createBody(DEF_line1);
		body_line2= world.createBody(DEF_line2);
		
		CircleShape SHP_char= new CircleShape();
		SHP_char.setRadius(48f / (2*PIXELS_PER_METER) );

		EdgeShape SHP_line= new EdgeShape();
		SHP_line.set(0.0f, 0.0f, this.wid, 0.0f);
		
		// 캐릭터의 회전을 고정시킬 것인가?
		body_char.setFixedRotation(false);
		body_char.setGravityScale(2.5f);
		body_char.setBullet(true);
		
//		MassData mass= new MassData();
//		mass.mass= 10f;
//		body_char.setMassData(mass);
		

		
		// 캐릭터의 밀도 : 70, 실험해볼 때 가장 적합한 값.
		body_char.createFixture(SHP_char, N_DENCITY);
		SHP_char.dispose();
		
		body_line1.createFixture(SHP_line, N_DENCITY);		
		body_line2.createFixture(SHP_line, N_DENCITY);		
		SHP_line.dispose();

		body_char.setLinearVelocity(new Vector2(0.0f, 0.0f));	// 캐릭터 바디의 기본속도를 입력
		body_char.setLinearDamping(V_DAMPING);						// 이것도 비슷한 것 같다.

		

		//debugRenderer = new Box2DDebugRenderer();
		txt_frame =  new BitmapFont();
		
		lastRender = System.nanoTime();
	}
	
	
	public void Render()
	{
		now  = System.nanoTime();
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		
		this.RenderMap();
		this.RenderChar();		
		this.RenderButton();
		
		/**
		 * Draw this last, so we can see the collision boundaries on top of the
		 * sprites and map.
		 */
		// debugRenderer.render(world);

		now = System.nanoTime();
		
		this.RenderFrame();
		
		FPS=Gdx.graphics.getFramesPerSecond();
		
		
//		if (now - lastRender < 30000000)
//		{ // 30 ms, ~33FPS
//			try
//			{
//				Thread.sleep(30 - (now - lastRender) / 1000000);
//			}
//			catch (InterruptedException e)	{}
//		}

		lastRender = now;
		
	
	}	
	
	private void RenderFrame()
	{
		txtBatch.begin();
		txt_frame.draw(txtBatch, "FPS : "+Gdx.graphics.getFramesPerSecond() , 10, Gdx.graphics.getHeight()-10 );
		txtBatch.end();
	}
	
	private void RenderChar()
	{
		//Gdx.app.log("char pos", " X : "+ body_char.getPosition().x + " Y : "+body_char.getPosition().y);
		
//		Gdx.app.log("Acceleromenter", "X : "+Gdx.input.getAccelerometerX()+
//									  "Y : "+Gdx.input.getAccelerometerY()+
//									  "Z : "+Gdx.input.getAccelerometerZ() );
		
		//sprite_ball.setRotation(-7*Gdx.input.getAccelerometerY());
		sprite_ball.setRotation(body_char.getAngle());
		
		body_char.applyForce(new Vector2(-1*V_VELOCITY*FPS* Gdx.input.getAccelerometerX(), 0.0f), new Vector2(
				sprite_ball.getWidth() / (2 * PIXELS_PER_METER),
				sprite_ball.getHeight() / (2 * PIXELS_PER_METER)));
		
		if(Gdx.input.isTouched())
		{
			if(Gdx.input.getX() > 240)	// 왼쪽 터치시
			{
				body_char.applyForce(new Vector2(V_VELOCITY*FPS, 0.0f), new Vector2(
						sprite_ball.getWidth() / (2 * PIXELS_PER_METER),
						sprite_ball.getHeight() / (2 * PIXELS_PER_METER)));				
			}
			else	// 오른쪽 터치시
			{
				body_char.applyForce(new Vector2(-V_VELOCITY*FPS, 0.0f), new Vector2(
						sprite_ball.getWidth() / (2 * PIXELS_PER_METER),
						sprite_ball.getHeight() / (2 * PIXELS_PER_METER)));
			}
			//Gdx.app.log("touch", " X : "+Gdx.input.getX()+" Y : "+Gdx.input.getY());
		}
		
		


		
		/**
		 * Prepare the SpriteBatch for drawing.
		 */
		//spriteBatch.setProjectionMatrix(tiledMapHelper.getCamera().combined);
		spriteBatch.begin();

		sprite_ball.setPosition( PIXELS_PER_METER * body_char.getPosition().x - sprite_ball.getWidth() / 2,
								 PIXELS_PER_METER * body_char.getPosition().y - sprite_ball.getHeight() / 2);
		sprite_ball.draw(spriteBatch);
		
		

		/**
		 * "Flush" the sprites to screen.
		 */
		spriteBatch.end();
		
//		renderer.begin();
//		renderer.fillCircle(PIXELS_PER_METER*body_char.getPosition().x, PIXELS_PER_METER*body_char.getPosition().y, 20.0f, 255, 255, 255);
//		renderer.end();
		
		
	}
	
	private void RenderMap()
	{
		world.setGravity(new Vector2(0.0f, Global.V_GRAVITY_Y));
		
		world.step(Gdx.app.getGraphics().getDeltaTime(), 3, 3);
		
	}
	
	private void RenderButton()
	{	
	}
}
