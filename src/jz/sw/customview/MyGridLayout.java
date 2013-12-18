package jz.sw.customview;

import android.content.Context;
import android.content.res.Resources;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.Scroller;

public class MyGridLayout extends GridLayout implements
		ViewGroup.OnHierarchyChangeListener {
	static final int PAGE_LEFT = 1, PAGE_TOP = 2, PAGE_RIGHT = 3,
			PAGE_BOTTOM = 4;
	protected int MIN_X_SWITCH_PAGE = 30, MIN_Y_SWITCH_PAGE = 30;
	protected int mScrollWidth , mScrollHeight ;
	protected float mDownMotionX, mDownMotionY, mLastMotionX, mLastMotionY;
	private int mCurrentPageX = 0, mCurrentPageY = 0;
	private final int mMaxPageY;// row count
	private int mMaxPageX = 5; // temp; column
//	private SwitchPageAnimat spa;
	private Scroller mScroller;
	
	public MyGridLayout(Context context) {
		this(context, null);
	}

	public MyGridLayout(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public MyGridLayout(Context context, AttributeSet attrs, int def) {
		super(context, attrs, def);
		mMaxPageY = this.getRowCount();
		Resources res = this.getResources();
		DisplayMetrics dm = res.getDisplayMetrics();
		int h = dm.heightPixels, w = dm.widthPixels;
		log("DisplayMetrics : width="+w+", height="+h);
		mScrollWidth=w;
		mScrollHeight=h;
		setOnHierarchyChangeListener(this);
//		spa=new Animations();
		mScroller = new Scroller(context);
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		// TODO Auto-generated method stub
		return super.onInterceptTouchEvent(ev);
	}
	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		final int action = ev.getAction();
		log( "on touch event, "+action);
		switch (action & MotionEvent.ACTION_MASK) {
		case MotionEvent.ACTION_DOWN:
			mLastMotionX = mDownMotionX = ev.getX();
			mLastMotionX = mDownMotionY = ev.getY();
			break;
		case MotionEvent.ACTION_MOVE: {
			// Scroll to follow the motion event
			final float x = ev.getX(), y = ev.getX();
			final float dx = mLastMotionX - x, dy = mLastMotionY - y;
			int cx=getScrollX(),cy=getScrollY();
			int xx = (int) (cx + dx), yy = (int) (cy + dy);
//			scrollTo(xx, 0);
			log("move.. x="+x+",mLastMotionX= "+mLastMotionX+",dx="+dx+",cx="+cx);
			mLastMotionX = x;
			mLastMotionY = y;
			break;
		}
		case MotionEvent.ACTION_UP:
		case MotionEvent.ACTION_CANCEL:
			float upX = ev.getX(),
			upY = ev.getY();
			int deltaX = (int) (mDownMotionX - upX),
			deltaY = (int) (mDownMotionY - upY);
			if (Math.abs(deltaX) > Math.abs(deltaY)) { // 左右滑, horizontal
				if (MIN_X_SWITCH_PAGE < Math.abs(deltaX)) {
					if (deltaX > 0)
						switchPage(PAGE_RIGHT);
					else
						switchPage(PAGE_LEFT);
				}else{
					smoothScrollToPage(mCurrentPageX,mCurrentPageY);
				}
			} else { // 上下滑 vertical
				if (MIN_Y_SWITCH_PAGE < Math.abs(deltaY)) {
					if (deltaY > 0)
						switchPage(PAGE_BOTTOM);
					else
						switchPage(PAGE_TOP);
				}else{
					smoothScrollToPage(mCurrentPageX,mCurrentPageY);
				}
			}
			
			mLastMotionX=0;
			break;
		case MotionEvent.ACTION_POINTER_UP:
			break;
		}
		log("super. "+super.onTouchEvent(ev));
		return true;
	}

	void switchPage(int dir) {

		View from=getCurrentDisplayView();
		switch (dir) {
		case PAGE_LEFT:
			if (mCurrentPageX > 0) {
				mCurrentPageX--;
				View to=getCurrentDisplayView();
//				spa.pageSwitch(from, to, dir);
				ScrollerDir(from, null, dir);
			}else{
//				spa.pageEdge(from, dir);
			}
			break;
		case PAGE_RIGHT:
			if (mCurrentPageX < mMaxPageX - 1) {
				mCurrentPageX++;
				View to=getCurrentDisplayView();
//				spa.pageSwitch(from, to, dir);
				ScrollerDir(from, null, dir);
			}else{
//				spa.pageEdge(from, dir);
			}
			break;
		case PAGE_TOP:
			if (mCurrentPageY > 0) {
				mCurrentPageY--;
				View to=getCurrentDisplayView();
//				spa.pageSwitch(from, to, dir);
				ScrollerDir(from, null, dir);
			}else{
//				spa.pageEdge(from, dir);
			}
			break;
		case PAGE_BOTTOM:
			if (mCurrentPageY < mMaxPageY - 1) {
				mCurrentPageY++;
				View to=getCurrentDisplayView();
//				spa.pageSwitch(from, to, dir);
				ScrollerDir(from, null, dir);
			}else{
//				spa.pageEdge(from, dir);
			}
			break;
		}
	}

	void ScrollerDir(View from,View to, int dir){
		
		int startX=getScrollX(),startY=getScrollY();
		int dx=0,dy=0;
		switch(dir){
		case PAGE_LEFT:
			dx = -mScrollWidth;
			break;
		case PAGE_RIGHT:
			dx = mScrollWidth;
			break;
		case PAGE_TOP:
			dy = -mScrollHeight;
			break;
		case PAGE_BOTTOM:
			dy = mScrollHeight;
			break;
		}
		mScroller.startScroll(startX, startY, dx, dy);
		log("start scroll: sx"+startX+",sy="+startY+"; dx="+dx+",dy="+dy);
		invalidate();
	}
	
	// 和 scroller 协作的关键，
	@Override
	public void computeScroll(){
		if(mScroller.computeScrollOffset() && !mScroller.isFinished()){
			int oldx=getScrollX(),oldy=getScrollY();
			int x=mScroller.getCurrX(),y=mScroller.getCurrY();
			if(oldx!=x || oldy!=y)
				scrollTo(x,y);
//			log("compute scroll: ox"+oldx+",oy="+oldy+"; x="+x+",y="+y);	
			ViewCompat.postInvalidateOnAnimation(this);
		}
	}
	
	void log(String s) {
		Log.i("sw2df", "[MyGridL..]" + s);
	}
	
	private void smoothScrollToPage(int pagex,int pagey){
		int x=pagex*mScrollWidth, y=pagey*mScrollHeight;
		smoothScrollTo(x,y);
	}
	
	private void smoothScrollTo(int x, int y){
		int sx=getScrollX(),sy=getScrollY();
		int dx=x-sx, dy=y-sy;
		if(dx ==0 && dy == 0){
			return ;
		}
		int duration=100;
		if(Math.abs(dy)>0){
			duration=Math.abs(dy)*10;
		}else{
			duration=Math.abs(dx)*10;
		}
		mScroller.startScroll(sx, sy, dx, dy, duration);
		ViewCompat.postInvalidateOnAnimation(this);
	}

	View getCurrentDisplayView(){
		return getChildAt(mCurrentPageX*mMaxPageY+mCurrentPageY);
	}
	
	void addView(int row, int column, View v) {
		if (row > 0 && row < mMaxPageY) {
			int index = row + column * mMaxPageY;
			removeViewAt(index);
			addView(v, index);
		} else if (row == 0) {
			addView(v);
			for (int i = 1; i < row; i++)
				addView(v);// :TODO should be null view
		}
	}

	@Override
	public void onChildViewAdded(View arg0, View arg1) {
		int rowSize = mMaxPageY;
		mMaxPageX = (getChildCount() + rowSize - 1) / rowSize;
	}

	@Override
	public void onChildViewRemoved(View arg0, View arg1) {
		int rowSize = mMaxPageY;
		mMaxPageX = (getChildCount() + rowSize - 1) / rowSize;
	}


}
