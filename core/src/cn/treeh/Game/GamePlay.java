package cn.treeh.Game;

import cn.treeh.Game.Combat.Combat;
import cn.treeh.Game.MapleMap.Map;
import cn.treeh.Game.Player.Char;
import cn.treeh.Game.Player.Look.CharLook;
import cn.treeh.Game.Player.Player;
import cn.treeh.IO.Net.LookEntry;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;

public class GamePlay {
    enum State {
        INACTIVE,
        TRANSITION,
        ACTIVE
    }

    ;
    Stage stage;
    SpriteBatch batch;
    static GamePlay instance;
    Camera camera;
    Player player = new Player();
    State state;
    Map map;

    Combat combat;

    public static GamePlay getInstance() {
        return instance;
    }
    public Player getPlayer(){
        return player;
    }
    public static GamePlay createGamePlay(SpriteBatch b, Stage s) {
        instance = new GamePlay(b, s);
        return instance;
    }
    public void loadPlayer(){
        player = new Player(100,
                new CharLook(new LookEntry(0, 20402, 30027)),
                "测试");
    }
    public void init() {
        Char.init();
        map.init();
    }

    public void load(int mapid, int portalid) {
        switch (state) {
            case INACTIVE:
                load_map(mapid);
                respawn(portalid);
            case TRANSITION:
                respawn(portalid);
        }
        state = State.ACTIVE;
    }

    public void respawn(int portalid) {
        map.BGM();
        int[] spawnpoint = map.getPortalByID(portalid);
        int[] startpos = map.get_y_below(spawnpoint);
        player.respawn(startpos, map.isUnderWater());
        camera.setPos(startpos);
        camera.setView(map.getWalls(), map.getBorders());
    }

    public void setAction(){
        stage.addListener(new InputListener() {
            @Override
            public boolean keyUp(InputEvent event, int keycode) {
                if(keycode == Input.Keys.LEFT)
                    key = -1;
                if(keycode == Input.Keys.RIGHT)
                    key = -1;
                if(keycode == Input.Keys.UP)
                    key = -1;
                if(keycode == Input.Keys.DOWN)
                    key = -1;
                return true;
            }

            @Override
            public boolean keyDown(InputEvent event, int keycode) {
                if(keycode == Input.Keys.LEFT)
                    key = 0;
                if(keycode == Input.Keys.RIGHT)
                    key = 1;
                if(keycode == Input.Keys.UP)
                    key = 2;
                if(keycode == Input.Keys.DOWN)
                    key = 3;
                return true;
            }
        });
    }
    private GamePlay(SpriteBatch b, Stage s) {
        stage = s;
        batch = b;
        state = State.INACTIVE;
        map = new Map();
        camera = new Camera();
        setAction();
    }

    public void draw(float alpha) {
        if (state != State.ACTIVE)
            return;
        int[] viewpos = camera.position(alpha);
        double[] realpos = camera.realPosition(alpha);
        map.drawBackground(realpos, alpha, batch);
        for (int id = 0; id < 8; id++) {
            map.drawLayer(id, viewpos, realpos, alpha, batch);
            player.draw(id, realpos, alpha, batch);
            //map.drawDrop();
        }
        map.drawForeground(realpos, alpha, batch);
        //combat.draw();

    }

    public void dispose() {

    }

    public void load_map(int mapId) {
        map = new Map(mapId);
    }
    int[] tmppos = new int[]{-3500,300};
    int key = -1;
    public void update() {
        if (state != State.ACTIVE)
            return;
        map.update(player);
        if(key == 0)
            tmppos[0] -= 10;
        if(key == 1)
            tmppos[0] += 10;
        if(key == 2)
            tmppos[1] -= 10;
        if(key == 3)
            tmppos[1] += 10;
        camera.setPos(tmppos);
        player.update(map.getPhysics());
//        combat.update();
//        backgrounds.update();
//        effect.update();
//        tilesobjs.update();
//
//        reactors.update(physics);
//        npcs.update(physics);
//        mobs.update(physics);
//        chars.update(physics);
//        drops.update(physics);
//        player.update(physics);
//
//        portals.update(player.get_position());
//        camera.update(player.get_position());
//
//        if (!player.is_climbing() && !player.is_sitting() && !player.is_attacking())
//        {
//            if (player.is_key_down(KeyAction::Id::UP) && !player.is_key_down(KeyAction::Id::DOWN))
//            check_ladders(true);
//
//            if (player.is_key_down(KeyAction::Id::UP))
//            check_portals();
//
//            if (player.is_key_down(KeyAction::Id::DOWN))
//            check_ladders(false);
//
//            if (player.is_key_down(KeyAction::Id::SIT))
//            check_seats();
//
//            if (player.is_key_down(KeyAction::Id::ATTACK))
//            combat.use_move(0);
//
//            if (player.is_key_down(KeyAction::Id::PICKUP))
//            check_drops();
    }

//        if (player.is_invincible())
//            return;
//
//        if (int32_t oid_id = mobs.find_colliding(player.get_phobj()))
//        {
//            if (MobAttack attack = mobs.create_attack(oid_id))
//            {
//                MobAttackResult result = player.damage(attack);
//                TakeDamagePacket(result, TakeDamagePacket::From::TOUCH).dispatch();
//            }
//        }
}

