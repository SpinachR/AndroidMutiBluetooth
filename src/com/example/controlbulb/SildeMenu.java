package com.example.controlbulb;

import com.nineoldandroids.view.ViewHelper;
import android.content.Context;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;

public class SildeMenu extends HorizontalScrollView {

	private LinearLayout mWapper; // the whole container
	private ViewGroup mMenu;
	private ViewGroup mContent;

	private int mScreenWidth;
	private int mMenuWidth;
	private int mMenuRightPaddding = 80;// dp

	private boolean once; // only run once
	private boolean isOpen;// the menu is open or not

	/**
	 * without using self-attrs
	 * 
	 * @param context
	 * @param attrs
	 */
	public SildeMenu(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		WindowManager wm = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		DisplayMetrics outMetrics = new DisplayMetrics();
		wm.getDefaultDisplay().getMetrics(outMetrics);

		mScreenWidth = outMetrics.widthPixels;
		// dip (mMenuRightPaddding) change into px
		mMenuRightPaddding = (int) TypedValue.applyDimension(
				TypedValue.COMPLEX_UNIT_DIP, 80, context.getResources()
						.getDisplayMetrics());
	}

	// setting height and width of the View
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// TODO Auto-generated method stub
		if (!once) {
			mWapper = (LinearLayout) getChildAt(0);
			mMenu = (ViewGroup) mWapper.getChildAt(0);
			mContent = (ViewGroup) mWapper.getChildAt(1);

			mMenuWidth = mMenu.getLayoutParams().width = mScreenWidth
					- mMenuRightPaddding;

			mContent.getLayoutParams().width = mScreenWidth;
			once = true;
		}

		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}

	/**
	 * set offset to hide the menu set the layout of the whole element. menu is
	 * on the left, outside the window
	 */
	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		// TODO Auto-generated method stub
		super.onLayout(changed, l, t, r, b);
		if (changed) {
			this.scrollTo(mMenuWidth, 0);
		}
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		// TODO Auto-generated method stub
		int action = ev.getAction();
		switch(action){
		case MotionEvent.ACTION_UP:
			int scrollX = getScrollX();
			if(scrollX>=mMenuWidth/2){
				this.smoothScrollTo(mMenuWidth, 0);
			}else{
				this.smoothScrollTo(0, 0);
				isOpen=true;
			}
			return true;
		}
		
		return super.onTouchEvent(ev);
	}
	
	@Override
	protected void onScrollChanged(int l, int t, int oldl, int oldt) {
		// TODO Auto-generated method stub
		// l =getScrollX();
		//在滚动发生时，调用属性动画，设置transaction，
		super.onScrollChanged(l, t, oldl, oldt);
		
		ViewHelper.setTranslationX(mMenu, l);
	}
	
	/**
	 * toggle the menu
	 */
	
	public void toggle(){
		if(isOpen){
			this.smoothScrollTo(mMenuWidth, 0);
			isOpen = false;
		}else{
			this.smoothScrollTo(0, 0);
			isOpen = true;
		}
	}

}
