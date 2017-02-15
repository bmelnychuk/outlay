package app.outlay.view.helper;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.graphics.Point;
import android.os.Build;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;

import app.outlay.utils.DeviceUtils;

/**
 * Created by bogdan.melnychuk on 09.12.2014.
 */
public final class AnimationUtils {
    public static void animateBounceIn(final View target) {
        AnimatorSet set = new AnimatorSet();
        set.playTogether(
                ObjectAnimator.ofFloat(target, "alpha", 0, 1, 1, 1),
                ObjectAnimator.ofFloat(target, "scaleX", 0.3f, 1.05f, 0.9f, 1),
                ObjectAnimator.ofFloat(target, "scaleY", 0.3f, 1.05f, 0.9f, 1)
        );
        set.setDuration(500);
        target.setVisibility(View.VISIBLE);
        set.start();
    }

    public static void animatePulse(final View target) {
        AnimatorSet set = new AnimatorSet();
        set.playTogether(
                ObjectAnimator.ofFloat(target, "scaleY", 1, 1.1f, 1),
                ObjectAnimator.ofFloat(target, "scaleX", 1, 1.1f, 1)
        );
        set.setDuration(1000);
        set.start();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public static void showWithReveal(View view) {
        int x = (view.getRight() - view.getLeft()) / 2;
        int y = view.getBottom();
        showWithReveal(view, new Point(x, y));
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public static void showWithReveal(View view, Point point) {
        if(DeviceUtils.supportV5()) {
            view.setVisibility(View.VISIBLE);

            // get the final radius for the clipping circle
            int endRadius = (int) Math.hypot(view.getWidth(), view.getHeight());

            Animator animator =
                    ViewAnimationUtils.createCircularReveal(view, point.x, point.y, 0, endRadius);
            animator.setInterpolator(new AccelerateDecelerateInterpolator());
            animator.setDuration(500);
            animator.start();
        } else {
            view.setVisibility(View.VISIBLE);
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public static void hideWithReveal(View view) {
        int cx = (view.getRight() - view.getLeft()) / 2;
        int cy = view.getBottom();
        hideWithReveal(view, new Point(cx, cy));

    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public static void hideWithReveal(View view, Point point) {
        if(DeviceUtils.supportV5()) {
            // get the initial radius for the clipping circle
            int initialRadius = (int) Math.hypot(view.getWidth(), view.getHeight());

            // create the animation (the final radius is zero)
            Animator animator = ViewAnimationUtils.createCircularReveal(view, point.x, point.y, initialRadius, 0);

            // make the view invisible when the animation is done
            animator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    view.setVisibility(View.INVISIBLE);
                }
            });

            animator.setInterpolator(new AccelerateDecelerateInterpolator());
            animator.setDuration(500);
            animator.start();
        } else {
            view.setVisibility(View.INVISIBLE);
        }
    }

    private static ValueAnimator slideAnimator(int start, int end, final View summary) {
        ValueAnimator animator = ValueAnimator.ofInt(start, end);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                //Update Height
                int value = (Integer) valueAnimator.getAnimatedValue();

                ViewGroup.LayoutParams layoutParams = summary.getLayoutParams();
                layoutParams.height = value;
                summary.setLayoutParams(layoutParams);
            }
        });
        return animator;
    }

    public static void expand(View summary) {
        //set Visible
        summary.setVisibility(View.VISIBLE);
        final int widthSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        summary.measure(widthSpec, 300);
        ValueAnimator mAnimator = slideAnimator(0, 300, summary);
        mAnimator.start();
    }

    public static void collapse(final View summary) {
        int finalHeight = summary.getHeight();
        ValueAnimator mAnimator = slideAnimator(finalHeight, 0, summary);
        mAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationEnd(Animator animator) {
                //Height=0, but it set visibility to GONE
                summary.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationStart(Animator animator) {
            }

            @Override
            public void onAnimationCancel(Animator animator) {
            }

            @Override
            public void onAnimationRepeat(Animator animator) {
            }
        });
        mAnimator.start();
    }
}
