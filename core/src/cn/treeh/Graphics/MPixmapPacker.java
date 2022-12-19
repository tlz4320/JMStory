package cn.treeh.Graphics;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Blending;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.GdxRuntimeException;

import java.util.Comparator;
import java.util.regex.Pattern;


public class MPixmapPacker implements Disposable {
    boolean packToTexture;
    boolean disposed;
    int pageWidth, pageHeight;
    Format pageFormat;
    int padding;
    boolean duplicateBorder;
    boolean stripWhitespaceX, stripWhitespaceY;
    int alphaThreshold;
    Color transparentColor = new Color(0f, 0f, 0f, 0f);
    final Array<Page> pages = new Array();
    PackStrategy packStrategy;

    static Pattern indexPattern = Pattern.compile("(.+)_(\\d+)$");

    /** Uses {@link GuillotineStrategy}.
     * @see MPixmapPacker#MPixmapPacker(int, int, Format, int, boolean, boolean, boolean, PackStrategy) */
    public MPixmapPacker (int pageWidth, int pageHeight, Format pageFormat, int padding, boolean duplicateBorder) {
        this(pageWidth, pageHeight, pageFormat, padding, duplicateBorder, false, false, new GuillotineStrategy());
    }


    public MPixmapPacker (int pageWidth, int pageHeight, Format pageFormat, int padding, boolean duplicateBorder,
                         boolean stripWhitespaceX, boolean stripWhitespaceY, PackStrategy packStrategy) {
        this.pageWidth = pageWidth;
        this.pageHeight = pageHeight;
        this.pageFormat = pageFormat;
        this.padding = padding;
        this.duplicateBorder = duplicateBorder;
        this.stripWhitespaceX = stripWhitespaceX;
        this.stripWhitespaceY = stripWhitespaceY;
        this.packStrategy = packStrategy;
    }

    public synchronized TextureRegion pack (Pixmap image) {
        return pack(null, image);
    }

    public synchronized TextureRegion pack (String name, Pixmap image) {
        if (disposed) return null;
        PixmapPackerRectangle rect;
        Pixmap pixmapToDispose = null;

        if (stripWhitespaceX || stripWhitespaceY) {
            int originalWidth = image.getWidth();
            int originalHeight = image.getHeight();
            // Strip whitespace, manipulate the pixmap and return corrected Rect
            int top = 0;
            int bottom = image.getHeight();
            if (stripWhitespaceY) {
                outer:
                for (int y = 0; y < image.getHeight(); y++) {
                    for (int x = 0; x < image.getWidth(); x++) {
                        int pixel = image.getPixel(x, y);
                        int alpha = ((pixel & 0x000000ff));
                        if (alpha > alphaThreshold) break outer;
                    }
                    top++;
                }
                outer:
                for (int y = image.getHeight(); --y >= top;) {
                    for (int x = 0; x < image.getWidth(); x++) {
                        int pixel = image.getPixel(x, y);
                        int alpha = ((pixel & 0x000000ff));
                        if (alpha > alphaThreshold) break outer;
                    }
                    bottom--;
                }
            }
            int left = 0;
            int right = image.getWidth();
            if (stripWhitespaceX) {
                outer:
                for (int x = 0; x < image.getWidth(); x++) {
                    for (int y = top; y < bottom; y++) {
                        int pixel = image.getPixel(x, y);
                        int alpha = ((pixel & 0x000000ff));
                        if (alpha > alphaThreshold) break outer;
                    }
                    left++;
                }
                outer:
                for (int x = image.getWidth(); --x >= left;) {
                    for (int y = top; y < bottom; y++) {
                        int pixel = image.getPixel(x, y);
                        int alpha = ((pixel & 0x000000ff));
                        if (alpha > alphaThreshold) break outer;
                    }
                    right--;
                }
            }

            int newWidth = right - left;
            int newHeight = bottom - top;

            pixmapToDispose = new Pixmap(newWidth, newHeight, image.getFormat());
            pixmapToDispose.setBlending(Blending.None);
            pixmapToDispose.drawPixmap(image, 0, 0, left, top, newWidth, newHeight);
            image = pixmapToDispose;

            rect = new PixmapPackerRectangle(0, 0, newWidth, newHeight, left, top, originalWidth, originalHeight);
        } else {
            rect = new PixmapPackerRectangle(0, 0, image.getWidth(), image.getHeight());
        }

        if (rect.getWidth() > pageWidth || rect.getHeight() > pageHeight) {
            if (name == null) throw new GdxRuntimeException("Page size too small for pixmap.");
            throw new GdxRuntimeException("Page size too small for pixmap: " + name);
        }

        Page page = packStrategy.pack(this, name, rect);

        int rectX = (int)rect.x, rectY = (int)rect.y, rectWidth = (int)rect.width, rectHeight = (int)rect.height;

        if (!duplicateBorder && page.texture != null && !page.dirty) {
            page.texture.bind();
            Gdx.gl.glTexSubImage2D(page.texture.glTarget, 0, rectX, rectY, rectWidth, rectHeight, image.getGLFormat(),
                    image.getGLType(), image.getPixels());
        } else
            page.dirty = true;


        if (pixmapToDispose != null) {
            pixmapToDispose.dispose();
        }
        TextureAtlas.AtlasRegion region = new TextureAtlas.AtlasRegion(page.texture, (int)rect.x, (int)rect.y,
                (int)rect.width, (int)rect.height);

        if (rect.splits != null) {
            region.names = new String[] {"split", "pad"};
            region.values = new int[][] {rect.splits, rect.pads};
        }

        int imageIndex = -1;
        region.name = name;
        region.index = imageIndex;
        region.offsetX = rect.offsetX;
        region.offsetY = (int)(rect.originalHeight - rect.height - rect.offsetY);
        region.originalWidth = rect.originalWidth;
        region.originalHeight = rect.originalHeight;
        region.setRegion(region.getU(), region.getV2(), region.getU2(), region.getV());
        return region;
//        return rect;
    }

    public synchronized void dispose () {
        disposed = true;
    }


    public int getPageWidth () {
        return pageWidth;
    }

    public void setPageWidth (int pageWidth) {
        this.pageWidth = pageWidth;
    }

    public int getPageHeight () {
        return pageHeight;
    }

    public void setPageHeight (int pageHeight) {
        this.pageHeight = pageHeight;
    }

    public Format getPageFormat () {
        return pageFormat;
    }

    public void setPageFormat (Format pageFormat) {
        this.pageFormat = pageFormat;
    }

    public int getPadding () {
        return padding;
    }

    public void setPadding (int padding) {
        this.padding = padding;
    }

    public boolean getDuplicateBorder () {
        return duplicateBorder;
    }

    public void setDuplicateBorder (boolean duplicateBorder) {
        this.duplicateBorder = duplicateBorder;
    }

    public boolean getPackToTexture () {
        return packToTexture;
    }

    /** If true, when a pixmap is packed to a page that has a texture, the portion of the texture where the pixmap was packed is
     * updated using glTexSubImage2D. Note if packing many pixmaps, this may be slower than reuploading the whole texture. This
     * setting is ignored if {@link #getDuplicateBorder()} is true. */
    public void setPackToTexture (boolean packToTexture) {
        this.packToTexture = packToTexture;
    }

    /** @author mzechner
     * @author Nathan Sweet
     * @author Rob Rendell */
    static public class Page {
        Texture texture;
        boolean dirty;
        MPixmapPacker packer;
        
        public Page (MPixmapPacker packer) {
            this.packer = packer;
        }

        /** Returns the texture for this page, or null if the texture has not been created.
        public Texture getTexture () {
            return texture;
        }

        /** Creates the texture if it has not been created, else reuploads the entire page pixmap to the texture if the pixmap has
         * changed since this method was last called.
         * @return true if the texture was created or reuploaded. */
        public boolean updateTexture (TextureFilter minFilter, TextureFilter magFilter) {

            texture = new Texture(packer.pageWidth, packer.pageHeight, packer.pageFormat);
            texture.setFilter(minFilter, magFilter);

            dirty = false;
            return true;
        }
    }

    /** Choose the page and location for each rectangle.
     * @author Nathan Sweet */
    static public interface PackStrategy {
        public void sort (Array<Pixmap> images);

        
        public Page pack (MPixmapPacker packer, String name, Rectangle rect);
    }

    /** Does bin packing by inserting to the right or below previously packed rectangles. This is good at packing arbitrarily sized
     * images.
     * @author mzechner
     * @author Nathan Sweet
     * @author Rob Rendell */
    static public class GuillotineStrategy implements PackStrategy {
        Comparator<Pixmap> comparator;

        public void sort (Array<Pixmap> pixmaps) {
            if (comparator == null) {
                comparator = new Comparator<Pixmap>() {
                    public int compare (Pixmap o1, Pixmap o2) {
                        return Math.max(o1.getWidth(), o1.getHeight()) - Math.max(o2.getWidth(), o2.getHeight());
                    }
                };
            }
            pixmaps.sort(comparator);
        }

        public Page pack (MPixmapPacker packer, String name, Rectangle rect) {
            GuillotinePage page;
            if (packer.pages.size == 0) {
                // Add a page if empty.
                page = new GuillotinePage(packer);
                page.updateTexture(TextureFilter.Linear, TextureFilter.Linear);
                packer.pages.add(page);
            } else {
                // Always try to pack into the last page.
                page = (GuillotinePage)packer.pages.peek();
            }

            int padding = packer.padding;
            rect.width += padding;
            rect.height += padding;
            Node node = insert(page.root, rect);
            if (node == null) {
                // Didn't fit, pack into a new page.
                page = new GuillotinePage(packer);
                page.updateTexture(TextureFilter.Linear, TextureFilter.Linear);
                packer.pages.add(page);
                node = insert(page.root, rect);
            }
            node.full = true;
            rect.set(node.rect.x, node.rect.y, node.rect.width - padding, node.rect.height - padding);
            return page;
        }

        private Node insert (Node node, Rectangle rect) {
            if (!node.full && node.leftChild != null && node.rightChild != null) {
                Node newNode = insert(node.leftChild, rect);
                if (newNode == null) newNode = insert(node.rightChild, rect);
                return newNode;
            } else {
                if (node.full) return null;
                if (node.rect.width == rect.width && node.rect.height == rect.height) return node;
                if (node.rect.width < rect.width || node.rect.height < rect.height) return null;

                node.leftChild = new Node();
                node.rightChild = new Node();

                int deltaWidth = (int)node.rect.width - (int)rect.width;
                int deltaHeight = (int)node.rect.height - (int)rect.height;
                if (deltaWidth > deltaHeight) {
                    node.leftChild.rect.x = node.rect.x;
                    node.leftChild.rect.y = node.rect.y;
                    node.leftChild.rect.width = rect.width;
                    node.leftChild.rect.height = node.rect.height;

                    node.rightChild.rect.x = node.rect.x + rect.width;
                    node.rightChild.rect.y = node.rect.y;
                    node.rightChild.rect.width = node.rect.width - rect.width;
                    node.rightChild.rect.height = node.rect.height;
                } else {
                    node.leftChild.rect.x = node.rect.x;
                    node.leftChild.rect.y = node.rect.y;
                    node.leftChild.rect.width = node.rect.width;
                    node.leftChild.rect.height = rect.height;

                    node.rightChild.rect.x = node.rect.x;
                    node.rightChild.rect.y = node.rect.y + rect.height;
                    node.rightChild.rect.width = node.rect.width;
                    node.rightChild.rect.height = node.rect.height - rect.height;
                }

                return insert(node.leftChild, rect);
            }
        }

        static final class Node {
            public Node leftChild;
            public Node rightChild;
            public final Rectangle rect = new Rectangle();
            public boolean full;
        }

        static class GuillotinePage extends Page {
            Node root;

            public GuillotinePage (MPixmapPacker packer) {
                super(packer);
                root = new Node();
                root.rect.x = packer.padding;
                root.rect.y = packer.padding;
                root.rect.width = packer.pageWidth - packer.padding * 2;
                root.rect.height = packer.pageHeight - packer.padding * 2;
            }
        }
    }

    /** Does bin packing by inserting in rows. This is good at packing images that have similar heights.
     * @author Nathan Sweet */


    private Color c = new Color();

    private int getSplitPoint (Pixmap raster, int startX, int startY, boolean startPoint, boolean xAxis) {
        int[] rgba = new int[4];

        int next = xAxis ? startX : startY;
        int end = xAxis ? raster.getWidth() : raster.getHeight();
        int breakA = startPoint ? 255 : 0;

        int x = startX;
        int y = startY;
        while (next != end) {
            if (xAxis)
                x = next;
            else
                y = next;

            int colint = raster.getPixel(x, y);
            c.set(colint);
            rgba[0] = (int)(c.r * 255);
            rgba[1] = (int)(c.g * 255);
            rgba[2] = (int)(c.b * 255);
            rgba[3] = (int)(c.a * 255);
            if (rgba[3] == breakA) return next;

            if (!startPoint && (rgba[0] != 0 || rgba[1] != 0 || rgba[2] != 0 || rgba[3] != 255))
                System.out.println(x + "  " + y + " " + rgba + " ");

            next++;
        }

        return 0;
    }

    public static class PixmapPackerRectangle extends Rectangle {
        int[] splits;
        int[] pads;
        int offsetX, offsetY;
        int originalWidth, originalHeight;

        PixmapPackerRectangle (int x, int y, int width, int height) {
            super(x, y, width, height);
            this.offsetX = 0;
            this.offsetY = 0;
            this.originalWidth = width;
            this.originalHeight = height;
        }

        PixmapPackerRectangle (int x, int y, int width, int height, int left, int top, int originalWidth, int originalHeight) {
            super(x, y, width, height);
            this.offsetX = left;
            this.offsetY = top;
            this.originalWidth = originalWidth;
            this.originalHeight = originalHeight;
        }
    }

}
