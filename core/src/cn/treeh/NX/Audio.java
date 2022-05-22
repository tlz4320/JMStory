package cn.treeh.NX;

public class Audio {
    long pos;
    int len;
    NxFile f;
    public Audio(long pos, int len, NxFile f){
        this.pos = pos;
        this.len = len;
        this.f = f;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Audio audio = (Audio) o;

        return pos == audio.pos;
    }
    public byte[] data(int skip){
        if(pos < 0)
            return new byte[0];
        synchronized (f.fileReader) {
            f.seek(pos);
            f.skip(skip);
            byte[] music = new byte[len - skip];
            try {
                f.fileReader.readFully(music);

                return music;
            } catch (Exception e) {
                throw new RuntimeException();
            }
        }
    }
    public byte[] data(){
        return data(0);
    }
}
