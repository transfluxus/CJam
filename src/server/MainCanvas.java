package server;

import processing.core.PApplet;

public class MainCanvas extends PApplet {

    CJamBlob blobs[];

    @Override
    public void setup () {
        super.setup( );
        size( 1920, 1080 );
        blobs = loadBlobs( );
        for ( CJamBlob b : blobs ) {
            pushStyle( );
            b.setup( );
            b.style = PStyleCopy.copyStyle( g.getStyle( ) );
            popStyle( );
        }
    }


    @Override
    public void draw () {
        for ( CJamBlob b : blobs ) {
            pushStyle( );
            style( b.style );
            b.draw( );
            b.style = PStyleCopy.copyStyle( g.getStyle( ) );
            popStyle( );
        }
    }

    private CJamBlob[] loadBlobs () {
        System.out.println( getClass( ).getSuperclass( ).getName( ) );
        int n = getClass( ).getSuperclass( ).getDeclaredClasses( ).length;
        CJamBlob[] blobs = new CJamBlob[ n ];
        blobs[ 0 ] = new blob_192_168_2_124( );
        return blobs;
    }

    public class blob_192_168_2_124 extends CJamBlob {

        public void setup () {
            background( 0 );
        }

        public void draw () {
            fill( 255, 100, 0 );
            ellipse( sin( frameCount * 0.01f ) * width / 2, height / 2, 30, 30 );
            int i = 0;
        }

    }
}
