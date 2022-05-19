package cn.treeh.Graphics;


import org.lwjgl.BufferUtils;

import java.awt.*;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL.createCapabilities;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.system.MemoryUtil.NULL;

public class GraphicsGL {
    static int attribute_coord, attribute_color, uniform_texture, uniform_atlassize,
            uniform_screensize, uniform_yoffset, uniform_fontregion, program;
    static GraphicsGL graphicsGL;
    static IntBuffer vbo, atlas;
    static int 	ATLASW = 8192, ATLASH = 8192, MINLOSIZE = 32;
    Point fontBorder;
    public static GraphicsGL get() {
        if (graphicsGL == null)
            graphicsGL = new GraphicsGL();
        return graphicsGL;
    }

    public void init() {
        createCapabilities();//这句话不执行是不行的 必须执行才能连接gl函数
        int vs = glCreateShader(GL_VERTEX_SHADER);
        String vs_source =
                "#version 120\n" +
                        "attribute vec4 coord;\n " +
                        "attribute vec4 color;\n" +
                        "varying vec2 texpos;\n" +
                        "varying vec4 colormod;\n" +
                        "uniform vec2 screensize;\n" +
                        "uniform int yoffset;\n" +
                        "void main(void) {\n" +
                        "float x = -1.0 + coord.x * 2.0 / screensize.x;\n" +
                        "float y = 1.0 - (coord.y + yoffset) * 2.0 / screensize.y;\n" +
                        "gl_Position = vec4(x, y, 0.0, 1.0);\n" +
                        "texpos = coord.zw;\n" +
                        "colormod = color;}";
        glShaderSource(vs, vs_source);
        glCompileShader(vs);
        int[] result = new int[1];
        glGetShaderiv(vs, GL_COMPILE_STATUS, result);
        if (result[0] <= 0) {
            throw new RuntimeException("Shader compile error");
        }
        int fs = glCreateShader(GL_FRAGMENT_SHADER);
        String fs_source =
                "#version 120\n" +
                        "varying vec2 texpos;\n" +
                        "varying vec4 colormod;\n" +
                        "uniform sampler2D texture;\n" +
                        "uniform vec2 atlassize;\n" +
                        "uniform int fontregion;\n" +
                        "void main(void) {\n" +
                        "if (texpos.y == 0) {\n" +
                        "gl_FragColor = colormod;\n" +
                        "} else if (texpos.y <= fontregion) {\n" +
                        "gl_FragColor = vec4(1, 1, 1, texture2D(texture, texpos / atlassize).r) * colormod;\n" +
                        "} else {\n" +
                        "gl_FragColor = texture2D(texture, texpos / atlassize) * colormod;\n" +
                        "}}";
        glShaderSource(fs, fs_source);
        glCompileShader(fs);
        result = new int[1];
        glGetShaderiv(vs, GL_COMPILE_STATUS, result);
        if (result[0] <= 0) {
            throw new RuntimeException("Shader compile error");
        }
        program = glCreateProgram();
        glAttachShader(program, vs);
        glAttachShader(program, fs);
        glLinkProgram(program);
        glGetProgramiv(program, GL_LINK_STATUS, result);
        if (result[0] <= 0)
            throw new RuntimeException("Shader link error");
        attribute_coord = glGetAttribLocation(program, "coord");
        attribute_color = glGetAttribLocation(program, "color");
        uniform_texture = glGetUniformLocation(program, "texture");
        uniform_atlassize = glGetUniformLocation(program, "atlassize");
        uniform_screensize = glGetUniformLocation(program, "screensize");
        uniform_yoffset = glGetUniformLocation(program, "yoffset");
        uniform_fontregion = glGetUniformLocation(program, "fontregion");
        if (attribute_coord == -1 || attribute_color == -1 || uniform_texture == -1
                || uniform_atlassize == -1 || uniform_yoffset == -1 || uniform_screensize == -1)
            throw new RuntimeException("Shader variable");
        //创建一个buffer
        //不过我不清楚为什么设置这个大小为1
        vbo = BufferUtils.createIntBuffer(1);
        glGenBuffers(vbo);
        atlas = BufferUtils.createIntBuffer(1);
        glGenTextures(atlas);
        glBindTexture(GL_TEXTURE_2D, atlas.get(0));
        //对齐像素字节函数
        glPixelStorei(GL_UNPACK_ALIGNMENT, 1);
        //纹理参数
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, ATLASW, ATLASH, 0,
                GL_RGBA, GL_UNSIGNED_BYTE, NULL);
        fontBorder = new Point(0, 1);
        //字体有待完成 我感觉好奇怪 为啥非要用gl的纹理绘制，难道不可以直接生成字符串的位图然后显示吗
        //

    }
//字体这个地方，我暂时先不用原来的写法，我觉得有问题，因为汉字会比较复杂，如果直接生成bitmap会浪费太多时间
    //完全可以需要的时候进行绘制就好了
//    boolean addfont(String name, Text.Font id, int pixelw, int pixelh)
//    {
//        FT_Face face;
//        if (FT_New_Face(ftlibrary, name, 0, &face))
//        return false;
//
//        if (FT_Set_Pixel_Sizes(face, pixelw, pixelh))
//            return false;
//
//        FT_GlyphSlot g = face->glyph;
//
//        GLshort width = 0;
//        GLshort height = 0;
//        for (uint8_t c = 32; c < 128; c++)
//        {
//            if (FT_Load_Char(face, c, FT_LOAD_RENDER))
//                continue;
//
//            GLshort w = static_cast<GLshort>(g->bitmap.width);
//            GLshort h = static_cast<GLshort>(g->bitmap.rows);
//
//            width += w;
//            if (h > height)
//                height = h;
//        }
//
//        if (fontborder.x() + width > ATLASW)
//        {
//            fontborder.set_x(0);
//            fontborder.set_y(fontymax);
//            fontymax = 0;
//        }
//
//        GLshort x = fontborder.x();
//        GLshort y = fontborder.y();
//
//        fontborder.shift_x(width);
//        if (height > fontymax)
//            fontymax = height;
//
//        fonts[id] = Font(width, height);
//
//        GLshort ox = x;
//        GLshort oy = y;
//        for (uint8_t c = 32; c < 128; c++)
//        {
//            if (FT_Load_Char(face, c, FT_LOAD_RENDER))
//                continue;
//
//            GLshort ax = static_cast<GLshort>(g->advance.x >> 6);
//            GLshort ay = static_cast<GLshort>(g->advance.y >> 6);
//            GLshort l = static_cast<GLshort>(g->bitmap_left);
//            GLshort t = static_cast<GLshort>(g->bitmap_top);
//            GLshort w = static_cast<GLshort>(g->bitmap.width);
//            GLshort h = static_cast<GLshort>(g->bitmap.rows);
//
//            glTexSubImage2D(GL_TEXTURE_2D, 0, ox, oy, w, h,
//                    GL_RED, GL_UNSIGNED_BYTE, g->bitmap.buffer);
//
//            Offset offset = Offset(ox, oy, w, h);
//            fonts[id].chars[c] = { ax, ay, w, h, l, t, offset };
//
//            ox += w;
//        }
//
//        return true;
//    }
//Font font = Font.createFont(Font.TRUETYPE_FONT, new File("")).deriveFont(Font.BOLD, 12);
//    BufferedImage image = new BufferedImage(1,1,BufferedImage.TYPE_INT_ARGB);
//    Graphics2D graphics2D = image.createGraphics();
//        graphics2D.setFont(font);
//    FontMetrics metrics = graphics2D.getFontMetrics();
//    image = new BufferedImage(1,1,BufferedImage.TYPE_INT_ARGB);
//    graphics2D = image.createGraphics();
//        graphics2D.setFont(font);
//        graphics2D.setColor(Color.WHITE);
//        graphics2D.drawString("A", 1, 1);
//        for (char i = 0; i < font.getNumGlyphs(); i++) {
//        if (font.canDisplay(i)) {
//            Glyph glyph = characterMap.get(i);
//            glyph.calculateTextureCoordinates(width,height);
//            graphics2D.drawString(Character.toString(i), glyph.x,glyph.y);
//        }
//    }
}
