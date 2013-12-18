package jz.sw.customview;

import android.animation.ObjectAnimator;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.BounceInterpolator;
import android.view.animation.TranslateAnimation;

public class Animations implements SwitchPageAnimat{

	private Animation mRightToHere,mHereToLeft;// page to right
	private Animation mLeftToHere,mHereToRight;// page to left
	private Animation mBottomToHere,mHereToTop;// page to bottom
	private Animation mTopToHere,mHereToBottom;// page to top
	
	public Animations(){
		mRightToHere = new TranslateAnimation(
				Animation.RELATIVE_TO_SELF, 1f, Animation.RELATIVE_TO_SELF, 0f,
				Animation.RELATIVE_TO_SELF,	0f, Animation.RELATIVE_TO_SELF, 0f);
		mHereToLeft = new TranslateAnimation(
				Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF, -1f,
				Animation.RELATIVE_TO_SELF,	0f, Animation.RELATIVE_TO_SELF, 0f);
		mLeftToHere = new TranslateAnimation(
				Animation.RELATIVE_TO_SELF, -1f, Animation.RELATIVE_TO_SELF, 0f,
				Animation.RELATIVE_TO_SELF,	0f, Animation.RELATIVE_TO_SELF, 0f);
		mHereToRight= new TranslateAnimation(
				Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF, 1f,
				Animation.RELATIVE_TO_SELF,	0f, Animation.RELATIVE_TO_SELF, 0f);
		mBottomToHere= new TranslateAnimation(
				Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF, 0f,
				Animation.RELATIVE_TO_SELF,	1f, Animation.RELATIVE_TO_SELF, 0f);
		mHereToTop= new TranslateAnimation(
				Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF, 0f,
				Animation.RELATIVE_TO_SELF,	0f, Animation.RELATIVE_TO_SELF, -1f);
		mTopToHere= new TranslateAnimation(
				Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF, 0f,
				Animation.RELATIVE_TO_SELF,	-1f, Animation.RELATIVE_TO_SELF, 0f);
		mHereToBottom= new TranslateAnimation(
				Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF, 0f,
				Animation.RELATIVE_TO_SELF,	0f, Animation.RELATIVE_TO_SELF, 1f);
		mRightToHere.setDuration(mDuration);
		mHereToLeft.setDuration(mDuration);
		mLeftToHere.setDuration(mDuration);
		mHereToRight.setDuration(mDuration);
		mBottomToHere.setDuration(mDuration);
		mHereToTop.setDuration(mDuration);
		mTopToHere.setDuration(mDuration);
		mHereToBottom.setDuration(mDuration);
	}

	@Override
	public void pageSwitch(View from, View to, int direction) {
		switch(direction){
		case LEFT:
			to.startAnimation(mLeftToHere);
			to.setVisibility(View.VISIBLE);
			from.startAnimation(mHereToRight);
			from.setVisibility(View.GONE);
			break;
		case RIGHT:
			to.startAnimation(mRightToHere);
			to.setVisibility(View.VISIBLE);
			from.startAnimation(mHereToLeft);
			from.setVisibility(View.GONE);	
			break;
		case UP:
			to.startAnimation(mTopToHere);
			to.setVisibility(View.VISIBLE);
			from.startAnimation(mHereToBottom);
			from.setVisibility(View.GONE);
			break;
		case DOWN:
			to.startAnimation(mBottomToHere);
			to.setVisibility(View.VISIBLE);
			from.startAnimation(mHereToTop);
			from.setVisibility(View.GONE);
			break;
		}
	}

	@Override
	public void pageEdge(View from, int direction) {
		ObjectAnimator oa = null;
		switch(direction){
		case LEFT:
			oa=ObjectAnimator.ofFloat(from, "x", 0f,from.getWidth()*0.2f);
			break;
		case RIGHT:
			oa=ObjectAnimator.ofFloat(from, "x", 0f,from.getWidth()*(-0.2f));
			break;
		case UP:
			oa=ObjectAnimator.ofFloat(from, "y", 0f,from.getHeight()*0.2f);
			break;
		case DOWN:
			oa=ObjectAnimator.ofFloat(from, "y", 0f,from.getHeight()*(-0.2f));
			break;
		}
		if (oa != null) {
			oa.setInterpolator(new BounceInterpolator());
			oa.reverse();
			oa.setDuration(mDuration);
			oa.addListener(mAl);
			oa.start();
		}
	}
}