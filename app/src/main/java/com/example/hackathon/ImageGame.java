package com.example.hackathon;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.media.effect.EffectFactory;
import android.net.Uri;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class ImageGame extends View {

    Bitmap bitmap;
    String name;
    Paint mDrawPaint = new Paint();
    Canvas mBitmapCanvas;
    boolean crop;
    boolean insertimage;
    boolean inserttext;
    boolean doodle;
    private Paint brush = new Paint();
    private Path path = new Path();
    Rect rect;
    BitmapDrawable mDrawable;
    boolean freeze;
    int centerx;
    int centery;
    int prevleft;
    int prevtop;
    int prevright;
    int prevbottom;
    Bitmap insertimg;
    Rect sourceRect;
    Paint paint;
    TextView textView;
    int pixel,r,g,b,color;
    String hex;
    Rect rect1;
    Paint paint1;
    int centerx1;
    int centery1;
    int prevleft1;
    int prevtop1;
    int prevright1;
    int prevbottom1;
    boolean filter;

    public boolean isFilter() {
        return filter;
    }

    public void setFilter(boolean filter) {
        this.filter = filter;
    }

    public boolean isCrop() {
        return crop;
    }

    public void setCrop(boolean crop) {
        this.crop = crop;
    }

    public boolean isInsertimage() {
        return insertimage;
    }

    public void setInsertimage(boolean insertimage) {
        this.insertimage = insertimage;
    }

    public boolean isInserttext() {
        return inserttext;
    }

    public void setInserttext(boolean inserttext) {
        this.inserttext = inserttext;
    }

    public boolean isDoodle() {
        return doodle;
    }

    public void setDoodle(boolean doodle) {
        this.doodle = doodle;
    }

    public boolean isColorpick() {
        return colorpick;
    }

    public void setColorpick(boolean colorpick) {
        this.colorpick = colorpick;
    }

    public boolean isColorreplace() {
        return colorreplace;
    }

    public void setColorreplace(boolean colorreplace) {
        this.colorreplace = colorreplace;
    }

    public boolean isBlur() {
        return blur;
    }

    public void setBlur(boolean blur) {
        this.blur = blur;
    }

    public boolean isRotate() {
        return rotate;
    }

    public void setRotate(boolean rotate) {
        this.rotate = rotate;
    }

    boolean colorpick;
    boolean colorreplace;
    boolean blur;
    boolean rotate;



    public ImageGame(Context context, String name) {
        super(context);
        this.name = name;
        File file = new File("storage/emulated/0/DCIM/Camera/" + name);
        bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());

    }


    public ImageGame(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }


    public ImageGame(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public ImageGame(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onDraw(Canvas canvas) {

        if (bitmap == null) {

            File file = new File("/document/primary:DCIM/Camera/" + name);
            bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
            mBitmapCanvas = new Canvas(bitmap);

        }
        if(paint==null){
            initialize();
        }

        if (doodle) {
            brush.setAntiAlias(true);
            brush.setColor(Color.BLACK);
            brush.setStyle(Paint.Style.STROKE);
            brush.setStrokeJoin(Paint.Join.ROUND);
            brush.setStrokeWidth(10f);
            mBitmapCanvas.drawPath(path, brush);
        }
        if (insertimage) {
            mBitmapCanvas.drawBitmap(insertimg, sourceRect, rect, null);
        }


        canvas.drawBitmap(bitmap, 0, 0, mDrawPaint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        float pointX = event.getX();
        float pointY = event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (doodle) {
                    path.moveTo(pointX, pointY);
                }

                if (insertimage) {
                    if (rect.left < pointX && rect.right > pointX && rect.top < pointY && rect.bottom > pointY) {
                        freeze = true;
                        Log.d("inside_touch", "yes");

                    } else {
                        Log.d("inside_touch", "no");

                        freeze = false;
                    }

                }
                if(colorpick){
                    pixel = bitmap.getPixel((int)event.getX(), (int) event.getY());

                    r = Color.red(pixel);
                    g = Color.green(pixel);
                    b = Color.blue(pixel);

                    color = Color.TRANSPARENT;
                    hex=String.format("#%06X", 0xFFFFFF & color);
                    mBitmapCanvas.drawText("HEX : "+String.format("#%06X", 0xFFFFFF & color),200,200,paint);
                }else{
                    mBitmapCanvas.drawText("",200,200,paint);

                }
                if(colorreplace){
                    for(int i=0;i<mBitmapCanvas.getWidth();i++){
                        for(int j=0;j<mBitmapCanvas.getHeight();j++){
                            int pixel = bitmap.getPixel((int)i ,(int) j);
                           int  r = Color.red(pixel);
                           int  g = Color.green(pixel);
                            int b = Color.blue(pixel);

                         //  int color = Color.TRANSPARENT;
                            String hex1= String.format("#%02X%02X%02X", r, g, b);
                            if(hex.equals(hex1)){
                                bitmap.setPixel(i,j,Color.parseColor(hex));
                            }
                            invalidate();


                        }
                    }
                }
                if(crop){
                    if (rect1.left < pointX && rect1.right > pointX && rect1.top < pointY && rect1.bottom > pointY) {
                        freeze = true;
                        Log.d("inside_touch", "yes");

                    } else {
                        Log.d("inside_touch", "no");

                        freeze = false;
                    }
                }
                if(blur){
                    //Point point=new Point((int)pointX,(int)pointY);
                    BlurMaskFilter blurFilter = new BlurMaskFilter(5, BlurMaskFilter.Blur.OUTER);

                    mDrawPaint.setMaskFilter(blurFilter);
                    invalidate();

                }else{
                    mDrawPaint.setMaskFilter(null);
                }

                return true;

            case MotionEvent.ACTION_MOVE:
                if (doodle) {
                    path.lineTo(pointX, pointY);
                }
                if (insertimage) {
                    if (freeze) {
                        centerx = rect.centerX();
                        centery = rect.centerY();
                        prevleft = rect.left;
                        prevtop = rect.top;
                        prevright = rect.right;
                        prevbottom = rect.bottom;


                        rect.left = (int) pointX - (Math.abs(centerx - prevleft));
                        rect.top = (int) pointY - (Math.abs(centery - prevtop));
                        rect.right = rect.left + 2 * (Math.abs(centerx - prevleft));
                        rect.bottom = rect.top + 2 * (Math.abs(centery - prevtop));
                    } else {
                        if (rect.left < pointX && rect.right > pointX && Math.abs(rect.top - pointY) <= 100) {
                            rect.top = (int) pointY;
                        } else if (rect.left < pointX && rect.right > pointX && Math.abs(rect.bottom - pointY) <= 100) {
                            rect.bottom = (int) pointY;
                        } else if (rect.top < pointY && rect.bottom > pointY && Math.abs(rect.left - pointX) <= 100) {
                            rect.left = (int) pointX;
                        } else if (rect.top < pointY && rect.bottom > pointY && Math.abs(rect.right - pointX) <= 100) {
                            rect.right = (int) pointX;
                        }
                    }
                }

                if (inserttext) {
                    mBitmapCanvas.drawText(textView.getText().toString(), pointX, pointY, paint);
                }

                if(colorpick){
                     pixel = bitmap.getPixel((int)event.getX(), (int) event.getY());

                     r = Color.red(pixel);
                     g = Color.green(pixel);
                     b = Color.blue(pixel);

                     color = Color.TRANSPARENT;
                     hex = String.format("#%02x%02x%02x", r, g, b);
                    mBitmapCanvas.drawText("HEX : "+hex,200,200,paint);
                }else{
                    mBitmapCanvas.drawText("",200,200,paint);

                }

                if(crop){
                    if (freeze) {
                        centerx1 = rect1.centerX();
                        centery1 = rect1.centerY();
                        prevleft1 = rect1.left;
                        prevtop1 = rect1.top;
                        prevright1 = rect1.right;
                        prevbottom1 = rect1.bottom;


                        rect1.left = (int) pointX - (Math.abs(centerx1 - prevleft1));
                        rect1.top = (int) pointY - (Math.abs(centery1 - prevtop1));
                        rect1.right = rect1.left + 2 * (Math.abs(centerx1 - prevleft1));
                        rect1.bottom = rect1.top + 2 * (Math.abs(centery1 - prevtop1));
                    } else {
                        if (rect1.left < pointX && rect1.right > pointX && Math.abs(rect1.top - pointY) <= 100) {
                            rect1.top = (int) pointY;
                        } else if (rect1.left < pointX && rect1.right > pointX && Math.abs(rect1.bottom - pointY) <= 100) {
                            rect1.bottom = (int) pointY;
                        } else if (rect1.top < pointY && rect1.bottom > pointY && Math.abs(rect1.left - pointX) <= 100) {
                            rect1.left = (int) pointX;
                        } else if (rect1.top < pointY && rect1.bottom > pointY && Math.abs(rect1.right - pointX) <= 100) {
                            rect1.right = (int) pointX;
                        }
                    }
                }

                break;

            default:
                return false;
        }

        postInvalidate();
        return false;


    }

    public void AskImageInsert(Bitmap bitmap) {

        // mBitmapCanvas.drawBitmap(bitmap,getWidth()/2-200, getHeight()/2-200,mDrawPaint);
        insertimg = bitmap;
        Bitmap resultBitmap = Bitmap.createBitmap(bitmap.getHeight(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        sourceRect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        rect = new Rect((resultBitmap.getWidth() - bitmap.getWidth()) / 2, 0, (resultBitmap.getWidth() + bitmap.getWidth()) / 2, bitmap.getHeight());
        mBitmapCanvas.drawBitmap(bitmap, sourceRect, rect, null);


        /*mDrawable = new BitmapDrawable(getResources(), bitmap);
        mDrawable.setTileModeXY(Shader.TileMode.REPEAT, Shader.TileMode.REPEAT);
        mDrawable.setBounds(rect);*/


    }

    public void initialize() {
        rect = new Rect();
        paint = new Paint();
        rect.left = getWidth() / 2 - 200;
        rect.top = getHeight() / 2 - 200;
        rect.right = rect.left + 300;
        rect.bottom = rect.top + 400;
        paint.setColor(Color.YELLOW);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(10f);
        paint.setStrokeJoin(Paint.Join.ROUND);

        rect1 = new Rect();
        paint1 = new Paint();
        rect1.left = getWidth() / 2 - 200;
        rect1.top = getHeight() / 2 - 200;
        rect1.right = rect1.left + 300;
        rect1.bottom = rect1.top + 400;
        paint1.setColor(Color.BLUE);
        paint1.setAlpha(30);


        Log.d("initialize", "yes");
    }

    public void DrawTextView() {
        if (textView == null) {
            LinearLayout layout = new LinearLayout(getContext());

            textView = new TextView(getContext());
            textView.setVisibility(View.VISIBLE);
            textView.setText("Hello world");
            layout.addView(textView);

            layout.measure(mBitmapCanvas.getWidth(), mBitmapCanvas.getHeight());
            layout.layout(100, 100, mBitmapCanvas.getWidth(), mBitmapCanvas.getHeight());

// To place the text view somewhere specific:
//canvas.translate(0, 0);

            layout.draw(mBitmapCanvas);
        }

/*
        textView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mBitmapCanvas.drawText(textView.getText().toString(), 100, 100, paint);
            }
        });
*/
    }

    public void rotate(){
        mBitmapCanvas.rotate(90);
        invalidate();
    }

    public void save() throws IOException {
        String path = "Last Shared File";
        String name = "/" + "Transact/"+this.name;
        File root = new File(getContext().getExternalFilesDir(path).getAbsolutePath() + name);

        FileOutputStream fOut = new FileOutputStream(root);
        bitmap.compress(Bitmap.CompressFormat.PNG, 85, fOut);
        fOut.flush();
        fOut.close();

        Intent sharingIntent = new Intent(Intent.ACTION_SEND);
        Uri screenshotUri = Uri.parse(getContext().getExternalFilesDir(path).getAbsolutePath() + name);
        sharingIntent.setType("*/*");
        sharingIntent.putExtra(Intent.EXTRA_STREAM, screenshotUri);
        getContext().startActivity(Intent.createChooser(sharingIntent, "Share image using"));
    }

    public void cropimage(){
        bitmap=Bitmap.createBitmap(bitmap, rect1.left,rect1.top,Math.abs(rect1.left - rect1.right), Math.abs(rect1.top - rect1.bottom));
        invalidate();

    }

    public void Filter(){
        bitmap= toGrayscale(bitmap);
        invalidate();
    }

    public Bitmap toGrayscale(Bitmap bmpOriginal)
    {
        int width, height;
        height = bmpOriginal.getHeight();
        width = bmpOriginal.getWidth();

        Bitmap bmpGrayscale = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(bmpGrayscale);
        Paint paint = new Paint();
        ColorMatrix cm = new ColorMatrix();
        cm.setSaturation(0);
        ColorMatrixColorFilter f = new ColorMatrixColorFilter(cm);
        paint.setColorFilter(f);
        c.drawBitmap(bmpOriginal, 0, 0, paint);
        return bmpGrayscale;
    }

    public void blur(){
        //BlurMaskFilter blurFilter = new BlurMaskFilter(5, BlurMaskFilter.Blur.OUTER);

    }

    public byte[] getBytes(InputStream inputStream) throws IOException {
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
        int bufferSize = 1024;
        byte[] buffer = new byte[bufferSize];

        int len = 0;
        while ((len = inputStream.read(buffer)) != -1) {
            byteBuffer.write(buffer, 0, len);
        }
        return byteBuffer.toByteArray();
    }

    public void saveFile(byte[] decodedString,String extension) {
        String path = "Last Shared File";
        String name = "/" + "Transact"+extension;
        File root = new File(getContext().getExternalFilesDir(path).getAbsolutePath() + name);
        try {
            OutputStream fileOutputStream = new FileOutputStream(root);
            fileOutputStream.write(decodedString);
            fileOutputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
       // insertImage= BitmapFactory.decodeFile(root.getAbsolutePath());
      /*  RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"),root);

        multipartBody = MultipartBody.Part.createFormData("file",file.getName(),requestFile);*/

    }
}
