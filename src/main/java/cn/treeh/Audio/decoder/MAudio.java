/*
 * Audio.java -- 音频输出
 * Copyright (C) 2010
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 * If you would like to negotiate alternate licensing terms, you may do
 * so by contacting the author: <http://jmp123.sf.net/>
 */
package cn.treeh.Audio.decoder;

import javax.sound.sampled.AudioFormat;
import java.util.LinkedList;

/**
 * 将解码得到的PCM数据写入音频设备（播放）。
 *
 */
public class MAudio implements IAudio {
    //	private SourceDataLine dateline;
    public LinkedList<byte[]> dataline = new LinkedList<>();
    @Override
    public boolean open(int rate, int channels, int bufferSize) {
        if(rate == 0 || !(channels == 1 || channels == 2))
            return false;
        if(bufferSize <= 4608)
            bufferSize = 4608 * 8;
        AudioFormat af = new AudioFormat(rate, 16, channels, true, false);
//		try {
//			dateline = AudioSystem.getSourceDataLine(af);
//			dateline.open(af, bufferSize);
//			// dateline.open(af);
//		} catch (LineUnavailableException e) {
//			System.out.println("鍒濆鍖栭煶棰戣緭鍑哄け璐ャ₿);
//			return false;
//		}

//		dateline.start();
        return true;
    }

    @Override
    public int write(byte[] b, int off, int size) {
        byte[] res = new byte[size];
        System.arraycopy(b, off, res, 0, size);
        dataline.add(res);
        return size;
    }

    public void start(boolean started) {
//		if (dateline == null)
//			return;
//		if (started)
//			dateline.start();
//		else
//			dateline.stop();
    }

    @Override
    public void drain() {
//		if (dateline != null)
//			dateline.drain();
    }

    @Override
    public void close() {
//		if (dateline != null)
//			if (dateline.isOpen())
//				dateline.close();
    }

}