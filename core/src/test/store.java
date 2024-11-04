//import cn.treeh.Util.Configure;
//import com.badlogic.gdx.Gdx;
//import com.badlogic.gdx.graphics.Color;
//import com.badlogic.gdx.graphics.Texture;
//import com.badlogic.gdx.graphics.g2d.BitmapFont;
//import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
//import com.badlogic.gdx.scenes.scene2d.Actor;
//import com.badlogic.gdx.scenes.scene2d.InputEvent;
//import com.badlogic.gdx.scenes.scene2d.Stage;
//import com.badlogic.gdx.scenes.scene2d.ui.Label;
//import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
//
//public class store {
//    //sub\[(\"[a-zA-Z]*\")\]
//    public static void main(String[] args) {
//        FreeTypeFontGenerator.setMaxTextureSize(10000);
//        FreeTypeFontGenerator normFont = new FreeTypeFontGenerator(Gdx.files.internal(Configure.FONT_LIGHT));
//        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
//        parameter.characters = Gdx.files.internal("charset.txt").readString("UTF8");
//        parameter.minFilter = com.badlogic.gdx.graphics.Texture.TextureFilter.Linear;
//        parameter.magFilter = Texture.TextureFilter.Linear;
//        parameter.size = 11;
//        BitmapFont bf = normFont.generateFont(parameter);
//        Label label;
//        Label.LabelStyle sp = new Label.LabelStyle(bf, Color.BLUE);
//        Stage stage = new Stage();
//        Gdx.input.setInputProcessor(stage);
//        label = new Label("Fuck", sp);
//        label.setPosition(100, 100);
//        label.addListener(new ClickListener(){
//            @Override
//            public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
//                System.out.println("test2");
//            }
//
//            @Override
//            public void enter(InputEvent event, float x, float y, int pointer, Actor toActor) {
//                System.out.println("test");
//            }
//        });
//        stage.addActor(label);
//    }
//}
