package com.example.fiveinrowparse;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;


/**
 * TODO: document your custom view class.
 */
public class MyBoard extends View {


    private int dCenter;
    private int centerX;
    private int centerY;
    private Paint paint;
    private Rect rect;
    private Display mDisplay;
    private int parentWidth;
    private int parentHeight;
    private Bitmap emptySquare;
    private Bitmap emtySquareScaled;
    private static float MIN_ZOOM = 1f;
    private static float MAX_ZOOM = 5f;
    private float scaleFactor = 1.f;
    private ScaleGestureDetector detector;
    private static int NONE = 0;
    private static int DRAG = 1;
    private static int ZOOM = 2;
    private int mode;
    private float startX = 0f;
    private float startY = 0f;
    private float translateX = 0f;
    private float translateY = 0f;
    private float previousTranslateX = 0f;
    private float previousTranslateY = 0f;
    private boolean dragged = true;
    private int[] mGameArray;
    private int PLAYER_ONE = 1;
    private int PLAYER_TWO = 2;
    private int PLAYER_ONE_WON = 3;
    private int PLAYER_TWO_WON = 4;
    private Boolean mSomeOneWon = false;
    private int PLAYER_TURN = PLAYER_ONE;
    private Bitmap crossSquare;
    private Bitmap circleSquare;
    private Bitmap crossSquareScaled;
    private float translateSinceStartX;
    private float translateSinceStartY;
    private float startTranslateX = 0f;
    private float startTranslateY = 0f;
    private Bitmap circleSquareScaled;
    private boolean mDidZoomOrDragged = false;
    private OnProgressChangeListener listener;
    private int mBoardColumns = 40;
    private int mBoardRows = 20;
    private Bitmap boardBackground;
    private String mTheme;
    private int mLeftMargin;
    private int mBoardPiceHeight;
    private int mBoardPiceWidth;
    private int mTopMargin;


    public MyBoard(Context context, int[] gameArray, String theme) {
        super(context);

        detector = new ScaleGestureDetector(getContext(), new ScaleListener());
        mTheme = theme;
        mGameArray = gameArray;

    }

    public MyBoard(Context context, int[] gameArray, String theme, AttributeSet attrs) {
        super(context, attrs);
        mTheme = theme;
        mGameArray = gameArray;
        detector = new ScaleGestureDetector(getContext(), new ScaleListener());
    }

    public MyBoard(Context context, int[] gameArray, String theme, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mTheme = theme;
        mGameArray = gameArray;
        detector = new ScaleGestureDetector(getContext(), new ScaleListener());
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        parentWidth = MeasureSpec.getSize(widthMeasureSpec);
        parentHeight = MeasureSpec.getSize(heightMeasureSpec);
        this.setMeasuredDimension(parentWidth, parentHeight);
        //super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        init(null, 0);
    }

    private void init(AttributeSet attrs, int defStyle) {
        paint = new Paint();
        paint.setColor(Color.BLUE);
        rect = new Rect(0, 0, parentWidth, parentHeight);
        if(mTheme.equals("dark")){
            emptySquare = BitmapFactory.decodeResource(getResources(), R.drawable.emtysquare);
            crossSquare = BitmapFactory.decodeResource(getResources(), R.drawable.cross);
            circleSquare = BitmapFactory.decodeResource(getResources(), R.drawable.circle);
        } else{
            emptySquare = BitmapFactory.decodeResource(getResources(), R.drawable.light_emtysquare);
            crossSquare = BitmapFactory.decodeResource(getResources(), R.drawable.light_cross);
            circleSquare = BitmapFactory.decodeResource(getResources(), R.drawable.light_circle);
        }

        calculateGamepicesWidthAndHeight();

        //boardBackground = BitmapFactory.decodeResource(getResources(), R.drawable.gameboardbackground);
        emtySquareScaled = Bitmap.createScaledBitmap(emptySquare, mBoardPiceWidth, mBoardPiceHeight, false);
        crossSquareScaled = Bitmap.createScaledBitmap(crossSquare, mBoardPiceWidth, mBoardPiceHeight, false);
        circleSquareScaled = Bitmap.createScaledBitmap(circleSquare, mBoardPiceWidth, mBoardPiceHeight, false);
        //boardBackground = Bitmap.createScaledBitmap(boardBackground, parentWidth, parentHeight, false);
    }

    //Räknar ut storlekar och marginaler
    public void calculateGamepicesWidthAndHeight(){

        if(parentWidth/2 >= parentHeight){
            mBoardPiceHeight = parentHeight/mBoardRows;
            mBoardPiceWidth = parentHeight/mBoardRows;
            mTopMargin = (parentHeight - (mBoardPiceHeight*mBoardRows))/2;
            mLeftMargin = (parentWidth - (mBoardPiceWidth*mBoardColumns))/2;

            Log.d("LeftMargin inside 1" , Integer.toString(mLeftMargin));


        } else if (parentHeight*2 >= parentWidth){
            mBoardPiceHeight = parentWidth/mBoardColumns;
            mBoardPiceWidth = parentWidth/mBoardColumns;
            mTopMargin = (parentHeight - (mBoardPiceHeight*mBoardRows))/2;
            mLeftMargin = (parentWidth - (mBoardPiceWidth*mBoardColumns))/2;

            Log.d("LeftMargin inside 2" , Integer.toString(mLeftMargin));
        }

    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);


        canvas.save();


        canvas.scale(scaleFactor, scaleFactor);

        if ((translateX * -1) < 0) {
            translateX = 0;
        }

        else if ((translateX * -1) > (scaleFactor - 1) * parentWidth) {
            translateX = (1 - scaleFactor) * parentWidth;
        }

        if (translateY * -1 < 0) {
            translateY = 0;
        }

        else if ((translateY * -1) > (scaleFactor - 1) * parentHeight) {
            translateY = (1 - scaleFactor) * parentHeight;
        }


        canvas.translate(translateX / scaleFactor, translateY / scaleFactor);
        translateSinceStartX = startTranslateX + translateX;
        translateSinceStartY = startTranslateY + translateY;



        //canvas.drawRect(rect, paint);
        //canvas.drawBitmap(boardBackground,0,0,null);


        int j = 0;
        int p = 0;

        for (j = 0; j < mBoardRows; j++) {
            int i = 0;
            for (i = 0; i < mBoardColumns; i++) {
                if (mGameArray[p] == PLAYER_ONE) {
                    canvas.drawBitmap(crossSquareScaled, i * crossSquareScaled.getWidth() + mLeftMargin, j * crossSquareScaled.getHeight() +mTopMargin, null);


                    p++;

                } else if (mGameArray[p] == PLAYER_TWO) {
                    canvas.drawBitmap(circleSquareScaled, i * circleSquareScaled.getWidth() + mLeftMargin, j * circleSquareScaled.getHeight() + mTopMargin, null);


                    p++;


                } else {
                    canvas.drawBitmap(emtySquareScaled, i * emtySquareScaled.getWidth() + mLeftMargin, j * emtySquareScaled.getHeight() + mTopMargin, null);
                    p++;

                }
            }
        }


        //mGameArray = new int[800];

        canvas.restore();
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getAction() & MotionEvent.ACTION_MASK) {

            case MotionEvent.ACTION_DOWN:
                mode = DRAG;


                startX = event.getX() - previousTranslateX;
                startY = event.getY() - previousTranslateY;

                break;

            case MotionEvent.ACTION_MOVE:
                translateX = event.getX() - startX;
                translateY = event.getY() - startY;

                double distance = Math.sqrt(Math.pow(event.getX() - (startX + previousTranslateX), 2) +
                                Math.pow(event.getY() - (startY + previousTranslateY), 2)
                );

                if (distance > 5) {

                    dragged = true;

                }

                break;

            case MotionEvent.ACTION_POINTER_DOWN:
                mode = ZOOM;

                break;

            case MotionEvent.ACTION_UP:
                if (mode == DRAG && !dragged) {
                    itemWasClicked(event);
                }
                mode = NONE;
                dragged = false;

                previousTranslateX = translateX;
                previousTranslateY = translateY;


                break;

            case MotionEvent.ACTION_POINTER_UP:
                mode = DRAG;

                previousTranslateX = translateX;
                previousTranslateY = translateY;
                break;
        }

        detector.onTouchEvent(event);


        if ((mode == DRAG && scaleFactor != 1f && dragged) || mode == ZOOM) {

            invalidate();
            mDidZoomOrDragged = false;
        }


        return true;
    }

    private void itemWasClicked(MotionEvent event) {
        if (!mSomeOneWon) {


            Boolean playerWon = false;
            int winningPlayer = 0;
            float valueX = event.getRawX() + (-translateSinceStartX-(mLeftMargin*2));
            float valueY = event.getRawY() + (-translateSinceStartY-(mTopMargin*2));

            float x = (valueX / (emtySquareScaled.getWidth() * scaleFactor));
            float y = (valueY / (emtySquareScaled.getHeight() * scaleFactor));
            Double arrayPlace = (Math.ceil(x) + (Math.ceil(y - 1) * mBoardColumns) - 1);
            Log.d("TRANSLATE", "TranslateX: " + translateSinceStartX + " TranslateY: " + translateSinceStartY);

            Log.d("values", "Value X: " + Math.ceil(x) + " Value y: " + Math.ceil(y) + " arrayPlace: " + arrayPlace.intValue());


            if (PLAYER_TURN == PLAYER_ONE && mGameArray[arrayPlace.intValue()] != PLAYER_ONE && mGameArray[arrayPlace.intValue()] != PLAYER_TWO) {
                mGameArray[arrayPlace.intValue()] = PLAYER_ONE;
                playerWon = checkPlayerWon();
                winningPlayer = PLAYER_ONE_WON;
                PLAYER_TURN = PLAYER_TWO;
            } else if (PLAYER_TURN == PLAYER_TWO && mGameArray[arrayPlace.intValue()] != PLAYER_ONE && mGameArray[arrayPlace.intValue()] != PLAYER_TWO) {
                mGameArray[arrayPlace.intValue()] = PLAYER_TWO;
                playerWon = checkPlayerWon();
                winningPlayer = PLAYER_TWO_WON;
                PLAYER_TURN = PLAYER_ONE;
            }

            if (!playerWon) {
                listener.onNextPlayer(this, PLAYER_TURN);
            } else if (playerWon) {
                listener.onSomeoneWon(this, winningPlayer);
                mSomeOneWon = true;
            }

            invalidate();
        }
    }

    public void resetTheGame() {
        mGameArray = new int[800];
        PLAYER_TURN = PLAYER_ONE;
        mSomeOneWon = false;
        listener.onNextPlayer(this, PLAYER_TURN);
        invalidate();
    }

    public void surrenderTheGame() {
        mSomeOneWon = true;
        if (PLAYER_TURN == PLAYER_ONE) {
            listener.onSomeoneWon(this, PLAYER_TWO_WON);
        } else if (PLAYER_TURN == PLAYER_TWO) {
            listener.onSomeoneWon(this, PLAYER_ONE_WON);
        }
        invalidate();

    }

    private boolean checkPlayerWon() {

        for (int i = 0; i < mGameArray.length; i++) {
            int consecutiveCells = 0;
            if (mGameArray[i] == PLAYER_TURN) {
                consecutiveCells++;


                // räknar diagonalt till höger
                int lastRowRight = i / mBoardColumns;
                for (int j = 1; j < 5; j++) {
                    if (i + (mBoardColumns * j) + j < mGameArray.length) {
                        int currentRowRight = (i + (mBoardColumns * j) + j) / mBoardColumns;
                        if (mGameArray[i + (mBoardColumns * j) + j] == PLAYER_TURN && lastRowRight + 1 == currentRowRight) {
                            lastRowRight = currentRowRight;
                            consecutiveCells++;
                        } else {
                            consecutiveCells = 0;
                        }

                        if (consecutiveCells >= 5) {
                            return true;
                        }
                    }
                }


                //Räknar diagonalt till vänster
                consecutiveCells = 1;
                int lastRowLeft = i / mBoardColumns;
                for (int j = 1; j < 5; j++) {

                    if (i + (mBoardColumns * j) - j < mGameArray.length) {
                        int justForDebugg = i + (mBoardColumns * j) - j;
                        int currentRowLeft = (i + (mBoardColumns * j) - j) / mBoardColumns;
                        if (mGameArray[i + (mBoardColumns * j) - j] == PLAYER_TURN && lastRowLeft != currentRowLeft) {
                            lastRowLeft = (i + (mBoardColumns * j) - j) / mBoardColumns;
                            consecutiveCells++;
                        } else {
                            consecutiveCells = 0;
                        }

                        if (consecutiveCells >= 5) {
                            return true;

                        }
                    }
                }

                //Räknar vågrätt
                consecutiveCells = 1;
                int lastRowAcross = i/mBoardColumns;
                for (int j = 1; j < 5; j++) {

                    if (i + j < mGameArray.length) {
                        int currentRowAcross = (i + j) / mBoardColumns;
                        if (mGameArray[i + j] == PLAYER_TURN && lastRowAcross == currentRowAcross) {
                            lastRowAcross = (i + j) / mBoardColumns;
                            consecutiveCells++;
                        } else {
                            consecutiveCells = 0;
                        }

                        if (consecutiveCells >= 5) {
                            return true;

                        }
                    }
                }

                //Räknar Lodrätt
                consecutiveCells = 1;
                for (int j = 1; j < 5; j++) {

                    if (i + (mBoardColumns*j) < mGameArray.length) {
                        if (mGameArray[i + (mBoardColumns*j)] == PLAYER_TURN) {
                            consecutiveCells++;
                        } else {
                            consecutiveCells = 0;
                        }

                        if (consecutiveCells >= 5) {
                            return true;

                        }
                    }
                }


            }

        }

        return false;
    }


    public interface OnProgressChangeListener {
        void onNextPlayer(View v, int currentPlayer);

        void onSomeoneWon(View v, int winningPlayer);

    }

    public void setOnProgressChangeListener(OnProgressChangeListener l) {
        listener = l;
    }

    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {

        @Override

        public boolean onScale(ScaleGestureDetector detector) {

            scaleFactor *= detector.getScaleFactor();

            scaleFactor = Math.max(MIN_ZOOM, Math.min(scaleFactor, MAX_ZOOM));

            return true;

        }

    }

}
