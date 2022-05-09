package cn.treeh.NX;

public class JSquish {
    public enum CompressionType {

        DXT1(8),
        DXT3(16),
        DXT5(16);

        public final int blockSize;
        public final int blockOffset;

        CompressionType(final int blockSize) {
            this.blockSize = blockSize;
            this.blockOffset = blockSize - 8;
        }
    }
    private static final int[] Cremapped = new int[16];

    private static final int[] Cindices = new int[16];

    private static final int[] Ccodes = new int[16];
    public static byte[] decompressImage(byte[] rgba, final int width, final int height, final byte[] blocks, final CompressionType type) {
        rgba = checkDecompressInput(rgba, width, height, blocks, type);

        final byte[] targetRGBA = new byte[16 * 4];

        // loop over blocks
        int sourceBlock = 0;
        for ( int y = 0; y < height; y += 4 ) {
            for ( int x = 0; x < width; x += 4 ) {
                // decompress the block
                decompress(targetRGBA, blocks, sourceBlock, type);

                // write the decompressed pixels to the correct image locations
                int sourcePixel = 0;
                for ( int py = 0; py < 4; ++py ) {
                    for ( int px = 0; px < 4; ++px ) {
                        // get the target location
                        int sx = x + px;
                        int sy = y + py;
                        if ( sx < width && sy < height ) {
                            // copy the rgba value
                            int targetPixel = 4 * (width * sy + sx);
                            for ( int i = 0; i < 4; ++i )
                                rgba[targetPixel++] = targetRGBA[sourcePixel++];
                        } else {
                            // skip this pixel as its outside the image
                            sourcePixel += 4;
                        }
                    }
                }

                // advance
                sourceBlock += type.blockSize;
            }
        }

        return rgba;
    }
    public static int getStorageRequirements(final int width, final int height, final CompressionType type) {
        if ( width <= 0 || height <= 0 )
            throw new IllegalArgumentException("Invalid image dimensions specified: " + width + " x " + height);

        final int blockcount = ((width + 3) / 4) * ((height + 3) / 4);

        return blockcount * type.blockSize;
    }
    private static byte[] checkDecompressInput(byte[] rgba, final int width, final int height, final byte[] blocks, final CompressionType type) {
        final int storageSize = getStorageRequirements(width, height, type);

        if ( blocks == null || blocks.length < storageSize )
            throw new IllegalArgumentException("Invalid source image data specified.");

        if ( rgba == null || rgba.length < (width * height * 4) )
            rgba = new byte[(width * height * 4)];

        return rgba;
    }
    static void decompressColour(final byte[] rgba, final byte[] block, final int offset, final boolean isDXT1) {
        // unpack the endpoints
        final int[] codes = Ccodes;

        final int a = unpack565(block, offset, codes, 0);
        final int b = unpack565(block, offset + 2, codes, 4);

        // generate the midpoints
        for ( int i = 0; i < 3; ++i ) {
            final int c = codes[i];
            final int d = codes[4 + i];

            if ( isDXT1 && a <= b ) {
                codes[8 + i] = (c + d) / 2;
                codes[12 + i] = 0;
            } else {
                codes[8 + i] = (2 * c + d) / 3;
                codes[12 + i] = (c + 2 * d) / 3;
            }
        }

        // fill in alpha for the intermediate values
        codes[8 + 3] = 255;
        codes[12 + 3] = (isDXT1 && a <= b) ? 0 : 255;

        // unpack the indices
        final int[] indices = Cindices;

        for ( int i = 0; i < 4; ++i ) {
            final int index = 4 * i;
            final int packed = (block[offset + 4 + i] & 0xFF);

            indices[index] = packed & 0x3;
            indices[index + 1] = (packed >> 2) & 0x3;
            indices[index + 2] = (packed >> 4) & 0x3;
            indices[index + 3] = (packed >> 6) & 0x3;
        }

        // store out the colours
        for ( int i = 0; i < 16; ++i ) {
            final int index = 4 * indices[i];
            for ( int j = 0; j < 4; ++j )
                rgba[4 * i + j] = (byte)codes[index + j];
        }
    }

    private static int unpack565(final byte[] packed, final int pOffset, final int[] colour, final int cOffset) {
        // build the packed value
        int value = (packed[pOffset + 0] & 0xff) | ((packed[pOffset + 1] & 0xff) << 8);

        // get the components in the stored range
        int red = (value >> 11) & 0x1f;
        int green = (value >> 5) & 0x3f;
        int blue = value & 0x1f;

        // scale up to 8 bits
        colour[cOffset + 0] = (red << 3) | (red >> 2);
        colour[cOffset + 1] = (green << 2) | (green >> 4);
        colour[cOffset + 2] = (blue << 3) | (blue >> 2);
        colour[cOffset + 3] = 255;

        // return the value
        return value;
    }
    private static void decompress(final byte[] rgba, final byte[] block, final int offset, final CompressionType type) {
        // get the block locations
        final int colourBlock = offset + type.blockOffset;
        final int alphaBock = offset;

        // decompress colour
        decompressColour(rgba, block, colourBlock, type == CompressionType.DXT1);

        // decompress alpha separately if necessary
        if ( type == CompressionType.DXT3 )
            decompressAlphaDxt3(rgba, block, alphaBock);
        else if ( type == CompressionType.DXT5 )
            decompressAlphaDxt5(rgba, block, alphaBock);
    }



    private static final int[] Aswapped = new int[16];

    private static final int[] Acodes5 = new int[8];
    private static final int[] Acodes7 = new int[8];

    private static final int[] indices5 = new int[16];
    private static final int[] indices7 = new int[16];

    private static final int[] Acodes = new int[8];
    private static final int[] Aindices = new int[16];


    static void compressAlphaDxt3(final byte[] rgba, final int mask, final byte[] block, final int offset) {
        // quantise and pack the alpha values pairwise
        for ( int i = 0; i < 8; ++i ) {
            // quantise down to 4 bits
            final float alpha1 = (rgba[8 * i + 3] & 0xFF) * (15.0f / 255.0f);
            final float alpha2 = (rgba[8 * i + 7] & 0xFF) * (15.0f / 255.0f);
            int quant1 = Math.round(alpha1);
            int quant2 = Math.round(alpha2);

            // set alpha to zero where masked
            final int bit1 = 1 << (2 * i);
            final int bit2 = 1 << (2 * i + 1);
            if ( (mask & bit1) == 0 )
                quant1 = 0;
            if ( (mask & bit2) == 0 )
                quant2 = 0;

            // pack into the byte
            block[offset + i] = (byte)(quant1 | (quant2 << 4));
        }
    }

    static void decompressAlphaDxt3(final byte[] rgba, final byte[] block, final int offset) {
        // unpack the alpha values pairwise
        for ( int i = 0; i < 8; ++i ) {
            // quantise down to 4 bits
            final int quant = (block[offset + i] & 0xFF);

            // unpack the values
            int lo = quant & 0x0f;
            int hi = quant & 0xf0;

            // convert back up to bytes
            rgba[8 * i + 3] = (byte)(lo | (lo << 4));
            rgba[8 * i + 7] = (byte)(hi | (hi >> 4));
        }
    }

    private static int fitCodes(final byte[] rgba, final int mask, final int[] codes, final int[] indices) {
        // fit each alpha value to the codebook
        int err = 0;
        for ( int i = 0; i < 16; ++i ) {
            // check this pixel is valid
            final int bit = 1 << i;
            if ( (mask & bit) == 0 ) {
                // use the first code
                indices[i] = 0;
                continue;
            }

            // find the least error and corresponding index
            final int value = (rgba[4 * i + 3] & 0xFF);
            int least = Integer.MAX_VALUE;
            int index = 0;
            for ( int j = 0; j < 8; ++j ) {
                // get the squared error from this code
                int dist = value - codes[j];
                dist *= dist;

                // compare with the best so far
                if ( dist < least ) {
                    least = dist;
                    index = j;
                }
            }

            // save this index and accumulate the error
            indices[i] = index;
            err += least;
        }

        // return the total error
        return err;
    }

    private static void writeAlphaBlock(final int alpha0, final int alpha1, final int[] indices, final byte[] block, final int offset) {
        // write the first two bytes
        block[offset + 0] = (byte)alpha0;
        block[offset + 1] = (byte)alpha1;

        // pack the indices with 3 bits each
        int src = 0;
        int dest = 2;
        for ( int i = 0; i < 2; ++i ) {
            // pack 8 3-bit values
            int value = 0;
            for ( int j = 0; j < 8; ++j ) {
                final int index = indices[src++];
                value |= (index << 3 * j);
            }

            // store in 3 bytes
            for ( int j = 0; j < 3; ++j )
                block[offset + dest++] = (byte)((value >> 8 * j) & 0xff);
        }
    }

    private static void writeAlphaBlock5(final int alpha0, final int alpha1, final int[] indices, final byte[] block, final int offset) {
        // check the relative values of the endpoints
        final int[] swapped = Aswapped;

        if ( alpha0 > alpha1 ) {
            // swap the indices
            for ( int i = 0; i < 16; ++i ) {
                int index = indices[i];
                if ( index == 0 )
                    swapped[i] = 1;
                else if ( index == 1 )
                    swapped[i] = 0;
                else if ( index <= 5 )
                    swapped[i] = 7 - index;
                else
                    swapped[i] = index;
            }

            // write the block
            writeAlphaBlock(alpha1, alpha0, swapped, block, offset);
        } else {
            // write the block
            writeAlphaBlock(alpha0, alpha1, indices, block, offset);
        }
    }

    private static void writeAlphaBlock7(final int alpha0, final int alpha1, final int[] indices, final byte[] block, final int offset) {
        // check the relative values of the endpoints
        final int[] swapped = Aswapped;

        if ( alpha0 < alpha1 ) {
            // swap the indices
            for ( int i = 0; i < 16; ++i ) {
                int index = indices[i];
                if ( index == 0 )
                    swapped[i] = 1;
                else if ( index == 1 )
                    swapped[i] = 0;
                else
                    swapped[i] = 9 - index;
            }

            // write the block
            writeAlphaBlock(alpha1, alpha0, swapped, block, offset);
        } else {
            // write the block
            writeAlphaBlock(alpha0, alpha1, indices, block, offset);
        }
    }

    static void compressAlphaDxt5(final byte[] rgba, final int mask, final byte[] block, final int offset) {
        // get the range for 5-alpha and 7-alpha interpolation
        int min5 = 255;
        int max5 = 0;
        int min7 = 255;
        int max7 = 0;
        for ( int i = 0; i < 16; ++i ) {
            // check this pixel is valid
            final int bit = 1 << i;
            if ( (mask & bit) == 0 )
                continue;

            // incorporate into the min/max
            final int value = (rgba[4 * i + 3] & 0xFF);
            if ( value < min7 )
                min7 = value;
            if ( value > max7 )
                max7 = value;
            if ( value != 0 && value < min5 )
                min5 = value;
            if ( value != 255 && value > max5 )
                max5 = value;
        }

        // handle the case that no valid range was found
        if ( min5 > max5 )
            min5 = max5;
        if ( min7 > max7 )
            min7 = max7;

        // fix the range to be the minimum in each case
        if ( max5 - min5 < 5 )
            max5 = Math.min(min5 + 5, 255);
        if ( max5 - min5 < 5 )
            min5 = Math.max(0, max5 - 5);

        if ( max7 - min7 < 7 )
            max7 = Math.min(min7 + 7, 255);
        if ( max7 - min7 < 7 )
            min7 = Math.max(0, max7 - 7);

        // set up the 5-alpha code book
        final int[] codes5 = Acodes5;

        codes5[0] = min5;
        codes5[1] = max5;
        for ( int i = 1; i < 5; ++i )
            codes5[1 + i] = ((5 - i) * min5 + i * max5) / 5;
        codes5[6] = 0;
        codes5[7] = 255;

        // set up the 7-alpha code book
        final int[] codes7 = Acodes7;

        codes7[0] = min7;
        codes7[1] = max7;
        for ( int i = 1; i < 7; ++i )
            codes7[1 + i] = ((7 - i) * min7 + i * max7) / 7;

        // fit the data to both code books
        int err5 = fitCodes(rgba, mask, codes5, indices5);
        int err7 = fitCodes(rgba, mask, codes7, indices7);

        // save the block with least error
        if ( err5 <= err7 )
            writeAlphaBlock5(min5, max5, indices5, block, offset);
        else
            writeAlphaBlock7(min7, max7, indices7, block, offset);
    }

    static void decompressAlphaDxt5(final byte[] rgba, final byte[] block, final int offset) {
        // get the two alpha values
        final int alpha0 = (block[offset + 0] & 0xFF);
        final int alpha1 = (block[offset + 1] & 0xFF);

        // compare the values to build the codebook
        final int[] codes = Acodes;

        codes[0] = alpha0;
        codes[1] = alpha1;
        if ( alpha0 <= alpha1 ) {
            // use 5-alpha codebook
            for ( int i = 1; i < 5; ++i )
                codes[1 + i] = ((5 - i) * alpha0 + i * alpha1) / 5;
            codes[6] = 0;
            codes[7] = 255;
        } else {
            // use 7-alpha codebook
            for ( int i = 1; i < 7; ++i )
                codes[1 + i] = ((7 - i) * alpha0 + i * alpha1) / 7;
        }

        // decode the indices
        final int[] indices = Aindices;

        int src = 2;
        int dest = 0;
        for ( int i = 0; i < 2; ++i ) {
            // grab 3 bytes
            int value = 0;
            for ( int j = 0; j < 3; ++j ) {
                int b = (block[offset + src++] & 0xFF);
                value |= (b << 8 * j);
            }

            // unpack 8 3-bit values from it
            for ( int j = 0; j < 8; ++j ) {
                int index = (value >> 3 * j) & 0x7;
                indices[dest++] = index;
            }
        }

        // write out the indexed codebook values
        for ( int i = 0; i < 16; ++i )
            rgba[4 * i + 3] = (byte)codes[indices[i]];
    }
}