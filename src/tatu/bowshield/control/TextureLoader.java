package tatu.bowshield.control;

import java.io.IOException;
import java.io.InputStream;

import org.andengine.opengl.texture.bitmap.BitmapTexture;
import org.andengine.ui.activity.SimpleBaseGameActivity;
import org.andengine.util.adt.io.in.IInputStreamOpener;

import android.content.res.AssetManager;

public class TextureLoader {

    private SimpleBaseGameActivity mReference;
    private AssetManager           mAssets;

    public TextureLoader(SimpleBaseGameActivity reference, AssetManager assets) {
        mReference = reference;
        mAssets = assets;
    }

    public BitmapTexture load(final String path) {
        try {
            return new BitmapTexture(mReference.getTextureManager(), new IInputStreamOpener() {
                @Override
                public InputStream open() throws IOException {
                    // TODO Auto-generated method stub
                    return mAssets.open(path);
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
