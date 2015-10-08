package com.pdm.angrystudents;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.JointDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJoint;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJointDef;
import com.badlogic.gdx.utils.Array;



public class AngryStudentsGame extends ApplicationAdapter {
	SpriteBatch batch;
	OrthographicCamera  camera;
	World world;
	Box2DDebugRenderer debugRenderer;
	Body nave;
	float time;
    Body circle1;
    Body circle2;

	Array<Body> primeraPila;

	Array<Body> segundaPila;

    float w;
    float h;

	Body bala;
	
	@Override
	public void create () {
		batch = new SpriteBatch();

		//camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        camera = new OrthographicCamera();
        float invRatio = Gdx.graphics.getHeight()/(float)Gdx.graphics.getWidth();
        w = 50;
        h = 50*invRatio;
        camera.setToOrtho(false, w, h);
		time = 0;

		world = new World(new Vector2(0, -10), true);
		debugRenderer = new Box2DDebugRenderer();

		world.setContactListener(new GameContactListener());

		createScene();
	}

	private void createScene()
	{
        float floorHeigth = 1.0f;
        float floorY = 0.75f;
        float topFloorY = floorY+floorHeigth/2;

		// creamos definicion del cuerpo
		BodyDef bodyDef = new BodyDef();
		// sera de tipo estatico
		bodyDef.type = BodyDef.BodyType.StaticBody;
		// establecemos la posicion
		bodyDef.position.set(w/2.0f, floorY);

		// creamos un cuerpo con esta definicion en el mundo
		Body body = world.createBody(bodyDef);

		// creamos el adorno para el cuerpo anterior
		FixtureDef fixtureDef = new FixtureDef();
		// creaamos una forma circular de radio 6
		PolygonShape suelo = new  PolygonShape();
		suelo.setAsBox(w/2.0f-1, floorHeigth/2);
		fixtureDef.shape = suelo;
		// establecemos propiedades fisicas
		fixtureDef.density = 1.0f;
		fixtureDef.friction = 0.4f;
		fixtureDef.restitution = 0.6f;

		// agregamos el adorno al cuerpo
		Fixture fixture = body.createFixture(fixtureDef);

		// liberamos la forma que creamos (ya no la necesitamos)
		suelo.dispose();

		nave = createNave(w/2.0f, h/2.0f, 2);

		primeraPila = new Array<Body>();
		for (int i=0; i < 12 ; ++i)
			primeraPila.add(createBox(35, topFloorY+1.1f*i, 1));

		segundaPila = new Array<Body>();
		for (int i=0; i < 7 ; ++i)
			segundaPila.add(createBox(45, topFloorY+1.1f*i, 1));

		for (int i=0; i < 10 ; ++i)
			createBall(MathUtils.random(4, w) - 2, MathUtils.random(h * 0.75f, h), MathUtils.random(0.5f, 2.0f));

		//for(Body b: primeraPila)
		//	b.applyLinearImpulse(new Vector2(200,200), b.getPosition(), true);

		bala = createBox(10/*x*/, topFloorY/*y*/, 2/*lado*/);

		Body box = createBox(5, topFloorY+1.5f, 3);
		circle1 = createBall(box.getPosition().x-1.5f, box.getPosition().y-1.5f, 1);
        circle2 = createBall(box.getPosition().x+1.5f, box.getPosition().y-1.5f, 1);

        RevoluteJointDef revDef = new RevoluteJointDef();
        revDef.initialize(box, circle1, box.getPosition().add(-1.5f,-1.5f));
		RevoluteJoint revolute1 = (RevoluteJoint)world.createJoint(revDef);

        revDef.initialize(box, circle2, box.getPosition().add(1.5f,-1.5f));
        RevoluteJoint revolute2 = (RevoluteJoint)world.createJoint(revDef);

	}

	private Body createBox(float x, float y, float lado)
	{
		// creamos definicion del cuerpo
		BodyDef bodyDef = new BodyDef();
		// sera de tipo dinamico
		bodyDef.type = BodyDef.BodyType.DynamicBody;
		// establecemos la posicion
		bodyDef.position.set(x, y);

		// creamos un cuerpo con esta definicion en el mundo
		Body body = world.createBody(bodyDef);

		// creamos el adorno para el cuerpo anterior
		FixtureDef fixtureDef = new FixtureDef();
		// creaamos una forma circular de radio 6
		PolygonShape caja = new  PolygonShape();
		caja.setAsBox(lado/2, lado/2);
		fixtureDef.shape = caja;
		// establecemos propiedades fisicas
		fixtureDef.density = 0.1f;
		fixtureDef.friction = 0.4f;
		fixtureDef.restitution = 0.6f;

		// agregamos el adorno al cuerpo
		Fixture fixture = body.createFixture(fixtureDef);

		// liberamos la forma que creamos (ya no la necesitamos)
		caja.dispose();

		return body;
	}

	private Body createNave(float x, float y, float ancho)
	{
		// creamos definicion del cuerpo
		BodyDef bodyDef = new BodyDef();
		// sera de tipo dinamico
		bodyDef.type = BodyDef.BodyType.KinematicBody;
		// establecemos la posicion
		bodyDef.position.set(x, y);
		// creamos un cuerpo con esta definicion en el mundo
		Body body = world.createBody(bodyDef);

		// creamos el adorno para el cuerpo anterior
		FixtureDef fixtureDef = new FixtureDef();
		// creaamos una forma circular de radio 6
		PolygonShape triangulo = new  PolygonShape();
		Vector2[] vert = new Vector2[3];
		vert[0] = new Vector2(-ancho/2.0f, -ancho/2.0f);
		vert[1] = new Vector2(ancho/2.0f, -ancho/2.0f);
		vert[2] = new Vector2(0.0f, ancho/2.0f);
		triangulo.set(vert);
		fixtureDef.shape = triangulo;
		// establecemos propiedades fisicas
		fixtureDef.density = 0.5f;
		fixtureDef.friction = 0.4f;
		fixtureDef.restitution = 0.6f;

		// agregamos el adorno al cuerpo
		Fixture fixture = body.createFixture(fixtureDef);

		// liberamos la forma que creamos (ya no la necesitamos)
		triangulo.dispose();
		return body;
	}

	private Body createBall(float x, float y, float radio)
	{
		// creamos definicion del cuerpo
		BodyDef bodyDef = new BodyDef();
		// sera de tipo dinamico
		bodyDef.type = BodyDef.BodyType.DynamicBody;
		// establecemos la posicion
		bodyDef.position.set(x, y);

		// creamos un cuerpo con esta definicion en el mundo
		Body body = world.createBody(bodyDef);

		// creamos el adorno para el cuerpo anterior
		FixtureDef fixtureDef = new FixtureDef();
		// creaamos una forma circular de radio 6
		CircleShape circle = new CircleShape();
		circle.setRadius(radio);
		fixtureDef.shape = circle;
		// establecemos propiedades fisicas
		fixtureDef.density = 0.5f;
		fixtureDef.friction = 0.4f;
		fixtureDef.restitution = 0.6f;

		// agregamos el adorno al cuerpo
		Fixture fixture = body.createFixture(fixtureDef);

		// liberamos la forma que creamos (ya no la necesitamos)
		circle.dispose();
		return body;
	}

	@Override
	public void render() {

		Vector2 applyPoint = bala.getPosition().sub(new Vector2(-1,1));

		if( Gdx.input.justTouched() )
			bala.applyLinearImpulse(new Vector2(3, 3), applyPoint, true);

		if( Gdx.input.isTouched() ) {
            circle1.applyForce(new Vector2(5, 0), circle1.getPosition(), true);
            circle2.applyForce(new Vector2(5, 0), circle2.getPosition(), true);
			//for (Body b : primeraPila)
			//	b.applyForce(new Vector2(100, 0), b.getPosition(), true);
		}


		time += Gdx.graphics.getDeltaTime();
		float x = MathUtils.lerp(5,w-5, Math.abs(MathUtils.sin(time)));
		float y = h / 2.0f;
		float ang = time*5;
		nave.setTransform(x, y, ang);

		world.step(1 / 60f, 6, 2);

		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		camera.update();

		debugRenderer.render(world, camera.combined);

		//batch.begin();

		//batch.end();
	}
}
