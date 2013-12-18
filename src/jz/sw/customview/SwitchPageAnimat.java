package jz.sw.customview;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.Animator.AnimatorListener;
import android.view.View;

public interface SwitchPageAnimat{
	static final int LEFT = 1, UP = 2, RIGHT = 3, DOWN = 4;
	static final long mDuration=500;
	static final AnimatorListener mAl = new AnimatorListener() {
		@Override
		public void onAnimationCancel(Animator arg0) {
		}
		@Override
		public void onAnimationEnd(Animator a) {
			((ObjectAnimator)a).reverse();
		}
		@Override
		public void onAnimationRepeat(Animator arg0) {
		}
		@Override
		public void onAnimationStart(Animator arg0) {
		}
		
	};
	
	void pageSwitch(View from, View to,int direction);
	void pageEdge(View view, int direction);
}
