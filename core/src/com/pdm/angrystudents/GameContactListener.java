package com.pdm.angrystudents;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;

/**
 * Created by Bob on 4/10/2015.
 */
public class GameContactListener implements ContactListener {

    @Override
    public void beginContact(Contact contact) {
        Gdx.app.log("beginContact", contact.getFixtureA().toString() + " | " + contact.getFixtureB().toString());
    }

    @Override
    public void endContact(Contact contact) {
        Gdx.app.log("endContact", contact.getFixtureA().toString() + " | " + contact.getFixtureB().toString());
    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }
}
