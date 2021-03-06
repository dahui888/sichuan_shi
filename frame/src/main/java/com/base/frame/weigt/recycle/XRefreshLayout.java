

package com.base.frame.weigt.recycle;

import android.animation.ValueAnimator;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import androidx.core.view.ViewCompat;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.AbsListView;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import java.lang.reflect.Field;

/**
 * 创建时间:15/5/21 22:35
 * 描述:下拉刷新、上拉加载更多、可添加自定义（固定、可滑动）头部控件（例如慕课网app顶部的广告位）
 */
public class XRefreshLayout extends LinearLayout {
    private static final String TAG = XRefreshLayout.class.getSimpleName();
    private XRefreshViewHolder mRefreshViewHolder;
    /**
     * 整个头部控件，下拉刷新控件mRefreshHeaderView和下拉刷新控件下方的自定义组件mCustomHeaderView的父控件
     */
    private LinearLayout mWholeHeaderView;
    /**
     * 下拉刷新控件
     */
    private View mRefreshHeaderView;
    /**
     * 下拉刷新控件下方的自定义控件
     */
    private View mCustomHeaderView;
    /**
     * 下拉刷新控件下方的自定义控件是否可滚动，默认不可滚动
     */
    private boolean mIsCustomHeaderViewScrollable = false;
    /**
     * 下拉刷新控件的高度
     */
    private int mRefreshHeaderViewHeight;
    /**
     * 当前刷新状态
     */
    private RefreshStatus mCurrentRefreshStatus = RefreshStatus.IDLE;
    /**
     * 上拉加载更多控件
     */
    private View mLoadMoreFooterView;
    /**
     * 上拉加载更多控件的高度
     */
    private int mLoadMoreFooterViewHeight;
    /**
     * 下拉刷新和上拉加载更多代理
     */
    private PullLoadMoreListener mDelegate;
    /**
     * 手指按下时，y轴方向的偏移量
     */
    private int mWholeHeaderDownY = -1;
    /**
     * 整个头部控件最小的paddingTop
     */
    private int mMinWholeHeaderViewPaddingTop;
    /**
     * 整个头部控件最大的paddingTop
     */
    private int mMaxWholeHeaderViewPaddingTop;

    /**
     * 是否处于正在加载更多状态
     */
    private boolean mIsLoadingMore = false;

    /**
     * 配置控件达到签到所有的控件都可以单独刷新操作
     */
    private AbsListView mAbsListView;
    private ScrollView mScrollView;
    private RecyclerView mRecyclerView;
    private NestedScrollView nestedScrollView;
    private View mNormalView;
    private WebView mWebView;
    //************配置添加控件结束**************
    private View mContentView;

    private float mInterceptTouchDownX = -1;
    private float mInterceptTouchDownY = -1;
    /**
     * 按下时整个头部控件的paddingTop
     */
    private int mWholeHeaderViewDownPaddingTop = 0;
    /**
     * 记录开始下拉刷新时的downY
     */
    private int mRefreshDownY = -1;

    /**
     * 是否已经设置内容控件滚动监听器
     */
    private boolean mIsInitedContentViewScrollListener = false;
    /**
     * 触发上拉加载更多时是否显示加载更多控件
     */
    private boolean mIsShowLoadingMoreView = true;

    private Handler mHandler;

    public XRefreshLayout(Context context) {
        this(context, null);
    }

    public XRefreshLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        setOrientation(LinearLayout.VERTICAL);
        mHandler = new Handler(Looper.getMainLooper());
        initWholeHeaderView();
    }

    /**
     * 初始化整个头部控件
     */
    private void initWholeHeaderView() {
        mWholeHeaderView = new LinearLayout(getContext());
        mWholeHeaderView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
        mWholeHeaderView.setOrientation(LinearLayout.VERTICAL);
        addView(mWholeHeaderView);
    }

    @Override
    public void onFinishInflate() {
        super.onFinishInflate();

        if (getChildCount() != 2) {
            throw new RuntimeException(XRefreshLayout.class.getSimpleName() + "必须有且只有一个子控件");
        }

        mContentView = getChildAt(1);
        if (mContentView instanceof AbsListView) {
            mAbsListView = (AbsListView) mContentView;
        } else if (mContentView instanceof RecyclerView) {
            mRecyclerView = (RecyclerView) mContentView;
        } else if (mContentView instanceof ScrollView) {
            mScrollView = (ScrollView) mContentView;
        } else if (mContentView instanceof WebView) {
            mWebView = (WebView) mContentView;
        } else if (mContentView instanceof NestedScrollView) {
            nestedScrollView = (NestedScrollView) mContentView;
        } else {
            mNormalView = mContentView;
            // 设置为可点击，否则在空白区域无法拖动
            mNormalView.setClickable(true);
        }
    }

    public void setRefreshViewHolder(XRefreshViewHolder refreshViewHolder) {
        mRefreshViewHolder = refreshViewHolder;
        mRefreshViewHolder.setRefreshLayout(this);
        initRefreshHeaderView();
        initLoadMoreFooterView();
    }

    public void startChangeWholeHeaderViewPaddingTop(int distance) {
        ValueAnimator animator = ValueAnimator.ofInt(mWholeHeaderView.getPaddingTop(), mWholeHeaderView.getPaddingTop() - distance);
        animator.setDuration(mRefreshViewHolder.getTopAnimDuration());
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int paddingTop = (int) animation.getAnimatedValue();
                mWholeHeaderView.setPadding(0, paddingTop, 0, 0);
            }
        });
        animator.start();
    }

    /**
     * 初始化下拉刷新控件
     *
     * @return
     */
    private void initRefreshHeaderView() {
        mRefreshHeaderView = mRefreshViewHolder.getRefreshHeaderView();
        if (mRefreshHeaderView != null) {
            mRefreshHeaderView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));

            mRefreshHeaderViewHeight = mRefreshViewHolder.getRefreshHeaderViewHeight();
            mMinWholeHeaderViewPaddingTop = -mRefreshHeaderViewHeight;
            mMaxWholeHeaderViewPaddingTop = (int) (mRefreshHeaderViewHeight * mRefreshViewHolder.getSpringDistanceScale());

            mWholeHeaderView.setPadding(0, mMinWholeHeaderViewPaddingTop, 0, 0);
            mWholeHeaderView.addView(mRefreshHeaderView, 0);
        }
    }

    /**
     * 设置下拉刷新控件下方的自定义控件
     *
     * @param customHeaderView 下拉刷新控件下方的自定义控件
     * @param scrollable       是否可以滚动
     */
    public void setCustomHeaderView(View customHeaderView, boolean scrollable) {
        if (mCustomHeaderView != null && mCustomHeaderView.getParent() != null) {
            ViewGroup parent = (ViewGroup) mCustomHeaderView.getParent();
            parent.removeView(mCustomHeaderView);
        }
        mCustomHeaderView = customHeaderView;
        if (mCustomHeaderView != null) {
            mCustomHeaderView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
            mWholeHeaderView.addView(mCustomHeaderView);
            mIsCustomHeaderViewScrollable = scrollable;
        }
    }

    /**
     * 初始化上拉加载更多控件
     *
     * @return
     */
    private void initLoadMoreFooterView() {
        mLoadMoreFooterView = mRefreshViewHolder.getLoadMoreFooterView();
        if (mLoadMoreFooterView != null) {
            // 测量上拉加载更多控件的高度
            mLoadMoreFooterView.measure(0, 0);
            mLoadMoreFooterViewHeight = mLoadMoreFooterView.getMeasuredHeight();
            mLoadMoreFooterView.setVisibility(GONE);
        }
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();

        // 被添加到窗口后再设置监听器，这样开发者就不必烦恼先初始化RefreshLayout还是先设置自定义滚动监听器
        if (!mIsInitedContentViewScrollListener && mLoadMoreFooterView != null) {
            setRecyclerViewOnScrollListener();
            setAbsListViewOnScrollListener();

            addView(mLoadMoreFooterView, getChildCount());

            mIsInitedContentViewScrollListener = true;
        }
    }

    private void setRecyclerViewOnScrollListener() {
        if (mRecyclerView != null) {
            mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                    if ((newState == RecyclerView.SCROLL_STATE_IDLE || newState == RecyclerView.SCROLL_STATE_SETTLING) && shouldHandleRecyclerViewLoadingMore()) {
                        beginLoadingMore();
                    }
                }
            });
        }
    }

    private void setAbsListViewOnScrollListener() {
        if (mAbsListView != null) {
            try {
                // 通过反射获取开发者自定义的滚动监听器，并将其替换成自己的滚动监听器，触发滚动时也要通知开发者自定义的滚动监听器（非侵入式，不让开发者继承特定的控件）
                // mAbsListView.getClass().getDeclaredField("mOnScrollListener")获取不到mOnScrollListener，必须通过AbsListView.class.getDeclaredField("mOnScrollListener")获取
                Field field = AbsListView.class.getDeclaredField("mOnScrollListener");
                field.setAccessible(true);
                // 开发者自定义的滚动监听器
                final AbsListView.OnScrollListener onScrollListener = (AbsListView.OnScrollListener) field.get(mAbsListView);
                mAbsListView.setOnScrollListener(new AbsListView.OnScrollListener() {
                    @Override
                    public void onScrollStateChanged(AbsListView absListView, int scrollState) {
                        if ((scrollState == SCROLL_STATE_IDLE || scrollState == SCROLL_STATE_FLING) && shouldHandleAbsListViewLoadingMore()) {
                            Log.i(TAG, "开始加载更多");
                            beginLoadingMore();
                        }

                        if (onScrollListener != null) {
                            onScrollListener.onScrollStateChanged(absListView, scrollState);
                        }
                    }

                    @Override
                    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                        if (onScrollListener != null) {
                            onScrollListener.onScroll(view, firstVisibleItem, visibleItemCount, totalItemCount);
                        }
                    }
                });
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    private boolean shouldHandleAbsListViewLoadingMore() {
        if (mIsLoadingMore || mCurrentRefreshStatus == RefreshStatus.REFRESHING || mLoadMoreFooterView == null || mDelegate == null || mAbsListView.getAdapter() == null || mAbsListView.getAdapter().getCount() == 0) {
            return false;
        }

        int lastChildBottom = 0;
        if (mAbsListView.getChildCount() > 0) {
            // 如果AdapterView的子控件数量不为0，获取最后一个子控件的bottom
            lastChildBottom = mAbsListView.getChildAt(mAbsListView.getChildCount() - 1).getBottom();
        }
        return mAbsListView.getLastVisiblePosition() == mAbsListView.getAdapter().getCount() - 1 && lastChildBottom <= mAbsListView.getMeasuredHeight();
    }

    private boolean shouldHandleRecyclerViewLoadingMore() {
        if (mIsLoadingMore || mCurrentRefreshStatus == RefreshStatus.REFRESHING || mLoadMoreFooterView == null || mDelegate == null || mRecyclerView.getAdapter() == null || mRecyclerView.getAdapter().getItemCount() == 0) {
            return false;
        }
        if (mRecyclerView != null) {
            RecyclerView.LayoutManager manager = mRecyclerView.getLayoutManager();
            if (manager == null || manager.getItemCount() == 0) {
                return false;
            }
            if (manager instanceof LinearLayoutManager) {
                // 处理item高度超过一屏幕时的情况
//                LinearLayoutManager layoutManager = (LinearLayoutManager) manager;
//                Log.i(TAG, "shouldHandleRecyclerViewLoadingMore: " + layoutManager.findLastCompletelyVisibleItemPosition());
//                Log.i(TAG, "shouldHandleRecyclerViewLoadingMore1: " + (mRecyclerView.getAdapter().getItemCount() - 1));
//                Log.i(TAG, "shouldHandleRecyclerViewLoadingMore2: " + layoutManager.findLastVisibleItemPosition());
//                if (/*layoutManager.findLastCompletelyVisibleItemPosition()*/ layoutManager.findLastVisibleItemPosition() == mRecyclerView.getAdapter().getItemCount() - 1) {
//                    return true;
//                }
                View lastVisibleChild = mRecyclerView.getChildAt(mRecyclerView.getChildCount() - 1);
                if (lastVisibleChild != null && lastVisibleChild.getMeasuredHeight() >= mRecyclerView.getMeasuredHeight()) {
                    if (android.os.Build.VERSION.SDK_INT < 14) {
                        return !(ViewCompat.canScrollVertically(mRecyclerView, 1) || mRecyclerView.getScrollY() < 0);
                    } else {
                        return !ViewCompat.canScrollVertically(mRecyclerView, 1);
                    }
                }
                LinearLayoutManager layoutManager = (LinearLayoutManager) manager;
                if (layoutManager.findLastCompletelyVisibleItemPosition() == layoutManager.getItemCount() - 1) {
                    return true;
                }
            } else if (manager instanceof StaggeredGridLayoutManager) {
                StaggeredGridLayoutManager layoutManager = (StaggeredGridLayoutManager) manager;

                int[] out = layoutManager.findLastCompletelyVisibleItemPositions(null);
                int lastPosition = layoutManager.getItemCount() - 1;
                for (int position : out) {
                    if (position == lastPosition) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * 是否满足处理刷新的条件
     *
     * @return
     */
    private boolean shouldHandleLoadingMore() {
        if (mIsLoadingMore || mCurrentRefreshStatus == RefreshStatus.REFRESHING || mLoadMoreFooterView == null || mDelegate == null) {
            return false;
        }
        // 内容是普通控件，满足
        if (mNormalView != null) {
            return true;
        }
        if (mWebView != null) {
            if (mWebView.getContentHeight() * mWebView.getScale() == (mWebView.getScrollY() + mWebView.getMeasuredHeight())) {
                return true;
            }
        }
        if (mScrollView != null) {
            int scrollContentHeight = mScrollView.getScrollY() + mScrollView.getMeasuredHeight() - mScrollView.getPaddingTop() - mScrollView.getPaddingBottom();
            int realContentHeight = mScrollView.getChildAt(0).getMeasuredHeight();
            if (scrollContentHeight == realContentHeight) {
                return true;
            }
        }
        if (nestedScrollView != null) {
            int scrollContentHeight = nestedScrollView.getScrollY() + nestedScrollView.getMeasuredHeight() - nestedScrollView.getPaddingTop() - nestedScrollView.getPaddingBottom();
            int realContentHeight = nestedScrollView.getChildAt(0).getMeasuredHeight();
            if (scrollContentHeight == realContentHeight) {
                return true;
            }
        }
        if (mAbsListView != null) {
            return shouldHandleAbsListViewLoadingMore();
        }
        if (mRecyclerView != null) {
            return shouldHandleRecyclerViewLoadingMore();
        }
        return false;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mInterceptTouchDownX = event.getRawX();
                mInterceptTouchDownY = event.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:
                if (!mIsLoadingMore && (mCurrentRefreshStatus != RefreshStatus.REFRESHING)) {
                    if (mInterceptTouchDownX == -1) {
                        mInterceptTouchDownX = (int) event.getRawX();
                    }
                    if (mInterceptTouchDownY == -1) {
                        mInterceptTouchDownY = (int) event.getRawY();
                    }

                    int interceptTouchMoveDistanceY = (int) (event.getRawY() - mInterceptTouchDownY);
                    // 可以没有上拉加载更多，但是必须有下拉刷新，否则就不拦截事件
                    if (Math.abs(event.getRawX() - mInterceptTouchDownX) < Math.abs(interceptTouchMoveDistanceY) && mRefreshHeaderView != null) {
                        if ((interceptTouchMoveDistanceY > 0 && shouldHandleRefresh()) || (interceptTouchMoveDistanceY < 0 && shouldHandleLoadingMore()) || interceptTouchMoveDistanceY < 0 && !isWholeHeaderViewCompleteInvisible()) {

                            // ACTION_DOWN时没有消耗掉事件，子控件会处于按下状态，这里设置ACTION_CANCEL，使子控件取消按下状态
                            event.setAction(MotionEvent.ACTION_CANCEL);
                            super.onInterceptTouchEvent(event);
                            return true;
                        }
                    }
                }
                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                // 重置
                mInterceptTouchDownX = -1;
                mInterceptTouchDownY = -1;
                break;
        }

        return super.onInterceptTouchEvent(event);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (mIsCustomHeaderViewScrollable && !isWholeHeaderViewCompleteInvisible()) {
            super.dispatchTouchEvent(ev);
            return true;
        }
        return super.dispatchTouchEvent(ev);
    }

    /**
     * 是否满足处理刷新的条件
     *
     * @return
     */
    private boolean shouldHandleRefresh() {
        if (mIsLoadingMore || mCurrentRefreshStatus == RefreshStatus.REFRESHING || mRefreshHeaderView == null || mDelegate == null) {
            return false;
        }

        // 内容是普通控件，满足
        if (mNormalView != null) {
            return true;
        }

        if (mWebView != null && mWebView.getScrollY() == 0) {
            return true;
        }

        // 内容是ScrollView，并且其scrollY为0时满足
        if (mScrollView != null && mScrollView.getScrollY() == 0) {
            return true;
        }
        if (nestedScrollView != null && nestedScrollView.getScrollY() == 0) {
            return true;
        }

        if (mAbsListView != null) {
            int firstChildTop = 0;
            if (mAbsListView.getChildCount() > 0) {
                // 如果AdapterView的子控件数量不为0，获取第一个子控件的top
                firstChildTop = mAbsListView.getChildAt(0).getTop() - mAbsListView.getPaddingTop();
            }
            if (mAbsListView.getFirstVisiblePosition() == 0 && firstChildTop == 0) {
                return true;
            }
        }

        if (mRecyclerView != null) {
            int firstChildTop = 0;
            if (mRecyclerView.getChildCount() > 0) {
                // 如果RecyclerView的子控件数量不为0，获取第一个子控件的top
                // 解决item的topMargin不为0时不能触发下拉刷新
                MarginLayoutParams layoutParams = (MarginLayoutParams) mRecyclerView.getChildAt(0).getLayoutParams();
                firstChildTop = mRecyclerView.getChildAt(0).getTop() - layoutParams.topMargin - mRecyclerView.getPaddingTop();
            }
            RecyclerView.LayoutManager manager = mRecyclerView.getLayoutManager();
            if (manager == null) {
                return true;
            }
            if (manager.getItemCount() == 0) {
                return true;
            }
            if (manager instanceof LinearLayoutManager) {
                LinearLayoutManager layoutManager = (LinearLayoutManager) manager;
                //解决第一条item显示示不完全时，不能触发下拉刷新
                if (layoutManager.findFirstCompletelyVisibleItemPosition() == 0 && firstChildTop == 0 || layoutManager.findLastVisibleItemPosition() == 0 && firstChildTop == 0) {
                    return true;
                }
            } else if (manager instanceof StaggeredGridLayoutManager) {
                StaggeredGridLayoutManager layoutManager = (StaggeredGridLayoutManager) manager;
                int[] out = layoutManager.findFirstCompletelyVisibleItemPositions(null);
                if (out[0] == 0) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (null != mRefreshHeaderView) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    mWholeHeaderDownY = (int) event.getY();
                    Log.i(TAG, "ACTION_DOWN mWholeHeaderDownY = " + mWholeHeaderDownY);
                    if (mCustomHeaderView != null) {
                        mWholeHeaderViewDownPaddingTop = mWholeHeaderView.getPaddingTop();
                    }
                    if (mCustomHeaderView == null || !mIsCustomHeaderViewScrollable) {
                        mRefreshDownY = (int) event.getY();
                    }
                    if (isWholeHeaderViewCompleteInvisible()) {
                        mRefreshDownY = (int) event.getY();
                        return true;
                    }
                    break;
                case MotionEvent.ACTION_MOVE:
                    if (handleActionMove(event)) {
                        return true;
                    }
                    break;
                case MotionEvent.ACTION_CANCEL:
                case MotionEvent.ACTION_UP:
                    if (handleActionUpOrCancel(event)) {
                        return true;
                    }
                    break;
                default:
                    break;
            }
        }
        return super.onTouchEvent(event);
    }

    /**
     * 自定义头部控件是否已经完全显示
     *
     * @return true表示已经完全显示，false表示没有完全显示
     */
    private boolean isCustomHeaderViewCompleteVisible() {
        if (mCustomHeaderView != null) {
            // 0表示x，1表示y
            int[] location = new int[2];
            getLocationOnScreen(location);
            int mOnScreenY = location[1];

            mCustomHeaderView.getLocationOnScreen(location);
            int customHeaderViewOnScreenY = location[1];
            if (mOnScreenY <= customHeaderViewOnScreenY) {
                return true;
            } else {
                return false;
            }

        }
        return true;
    }

    /**
     * 整个头部控件是否已经完全隐藏
     *
     * @return true表示完全隐藏，false表示没有完全隐藏
     */
    private boolean isWholeHeaderViewCompleteInvisible() {
        if (mCustomHeaderView != null && mIsCustomHeaderViewScrollable) {
            // 0表示x，1表示y
            int[] location = new int[2];
            getLocationOnScreen(location);
            int mOnScreenY = location[1];

            mWholeHeaderView.getLocationOnScreen(location);
            int wholeHeaderViewOnScreenY = location[1];
            if (wholeHeaderViewOnScreenY + mWholeHeaderView.getMeasuredHeight() <= mOnScreenY) {
                return true;
            } else {
                return false;
            }
        }
        return true;
    }

    /**
     * 处理手指滑动事件
     *
     * @param event
     * @return true表示自己消耗掉该事件，false表示不消耗该事件
     */
    private boolean handleActionMove(MotionEvent event) {
        if (mCurrentRefreshStatus == RefreshStatus.REFRESHING || mIsLoadingMore) {
            return false;
        }

        if ((mCustomHeaderView == null || !mIsCustomHeaderViewScrollable) && mRefreshDownY == -1) {
            mRefreshDownY = (int) event.getY();
        }
        if (mCustomHeaderView != null && mIsCustomHeaderViewScrollable && isCustomHeaderViewCompleteVisible() && mRefreshDownY == -1) {
            mRefreshDownY = (int) event.getY();
        }

        int refreshDiffY = (int) event.getY() - mRefreshDownY;
        refreshDiffY = (int) (refreshDiffY / mRefreshViewHolder.getPaddingTopScale());

        // 如果是向下拉，并且当前可见的第一个条目的索引等于0，才处理整个头部控件的padding
        if (refreshDiffY > 0 && shouldHandleRefresh() && isCustomHeaderViewCompleteVisible()) {
            int paddingTop = mMinWholeHeaderViewPaddingTop + refreshDiffY;
            if (paddingTop > 0 && mCurrentRefreshStatus != RefreshStatus.RELEASE_REFRESH) {
                // 下拉刷新控件完全显示，并且当前状态没有处于释放开始刷新状态
                mCurrentRefreshStatus = RefreshStatus.RELEASE_REFRESH;
                handleRefreshStatusChanged();

                mRefreshViewHolder.handleScale(1.0f, refreshDiffY);
            } else if (paddingTop < 0) {
                // 下拉刷新控件没有完全显示，并且当前状态没有处于下拉刷新状态
                if (mCurrentRefreshStatus != RefreshStatus.PULL_DOWN) {
                    boolean isPreRefreshStatusNotIdle = mCurrentRefreshStatus != RefreshStatus.IDLE;
                    mCurrentRefreshStatus = RefreshStatus.PULL_DOWN;
                    if (isPreRefreshStatusNotIdle) {
                        handleRefreshStatusChanged();
                    }
                }
                float scale = 1 - paddingTop * 1.0f / mMinWholeHeaderViewPaddingTop;
                /**
                 * 往下滑
                 * paddingTop    mMinWholeHeaderViewPaddingTop 到 0
                 * scale         0 到 1
                 * 往上滑
                 * paddingTop    0 到 mMinWholeHeaderViewPaddingTop
                 * scale         1 到 0
                 */
                mRefreshViewHolder.handleScale(scale, refreshDiffY);
            }
            paddingTop = Math.min(paddingTop, mMaxWholeHeaderViewPaddingTop);
            mWholeHeaderView.setPadding(0, paddingTop, 0, 0);

            if (mRefreshViewHolder.canChangeToRefreshingStatus()) {
                mWholeHeaderDownY = -1;
                mRefreshDownY = -1;

                beginRefreshing();
            }

            return true;
        }


        if (mCustomHeaderView != null && mIsCustomHeaderViewScrollable) {
            if (mWholeHeaderDownY == -1) {
                mWholeHeaderDownY = (int) event.getY();

                Log.i(TAG, "ACTION_MOVE mWholeHeaderDownY = " + mWholeHeaderDownY);
                if (mCustomHeaderView != null) {
                    mWholeHeaderViewDownPaddingTop = mWholeHeaderView.getPaddingTop();
                }
            }

            int wholeHeaderDiffY = (int) event.getY() - mWholeHeaderDownY;
            if (!isWholeHeaderViewCompleteInvisible() || (wholeHeaderDiffY > 0 && shouldHandleRefresh() && !isCustomHeaderViewCompleteVisible())) {

                int paddingTop = mWholeHeaderViewDownPaddingTop + wholeHeaderDiffY;
                if (paddingTop < mMinWholeHeaderViewPaddingTop - mCustomHeaderView.getMeasuredHeight()) {
                    paddingTop = mMinWholeHeaderViewPaddingTop - mCustomHeaderView.getMeasuredHeight();
                }
                mWholeHeaderView.setPadding(0, paddingTop, 0, 0);

                return true;
            }
        }

        return false;
    }

    /**
     * 处理手指抬起事件
     *
     * @return true表示自己消耗掉该事件，false表示不消耗该事件
     */
    private boolean handleActionUpOrCancel(MotionEvent event) {
        boolean isReturnTrue = false;
        // 如果当前头部刷新控件没有完全隐藏，则需要返回true，自己消耗ACTION_UP事件
        if ((mCustomHeaderView == null || (mCustomHeaderView != null && !mIsCustomHeaderViewScrollable)) && mWholeHeaderView.getPaddingTop() != mMinWholeHeaderViewPaddingTop) {
            isReturnTrue = true;
        }

        if (mCurrentRefreshStatus == RefreshStatus.PULL_DOWN || mCurrentRefreshStatus == RefreshStatus.IDLE) {
            // 处于下拉刷新状态，松手时隐藏下拉刷新控件
            if (mCustomHeaderView == null || (mCustomHeaderView != null && mWholeHeaderView.getPaddingTop() < 0 && mWholeHeaderView.getPaddingTop() > mMinWholeHeaderViewPaddingTop)) {
                hiddenRefreshHeaderView();
            }
            mCurrentRefreshStatus = RefreshStatus.IDLE;
            handleRefreshStatusChanged();
        } else if (mCurrentRefreshStatus == RefreshStatus.RELEASE_REFRESH) {
            // 处于松开进入刷新状态，松手时完全显示下拉刷新控件，进入正在刷新状态
            beginRefreshing();
        }

        if (mRefreshDownY == -1) {
            mRefreshDownY = (int) event.getY();
        }
        int diffY = (int) event.getY() - mRefreshDownY;
        if (shouldHandleLoadingMore() && diffY <= 0) {
            // 处理上拉加载更多，需要返回true，自己消耗ACTION_UP事件
            isReturnTrue = true;
            beginLoadingMore();
        }

        mWholeHeaderDownY = -1;
        mRefreshDownY = -1;
        return isReturnTrue;
    }

    /**
     * 处理下拉刷新控件状态变化
     */
    private void handleRefreshStatusChanged() {
        switch (mCurrentRefreshStatus) {
            case IDLE:
                mRefreshViewHolder.changeToIdle();
                break;
            case PULL_DOWN:
                mRefreshViewHolder.changeToPullDown();
                break;
            case RELEASE_REFRESH:
                mRefreshViewHolder.changeToReleaseRefresh();
                break;
            case REFRESHING:
                mRefreshViewHolder.changeToRefreshing();
                break;
            default:
                break;
        }
    }

    /**
     * 切换到正在刷新状态，会调用delegate的onBGARefreshLayoutBeginRefreshing方法
     */
    public void beginRefreshing() {
        if (mCurrentRefreshStatus != RefreshStatus.REFRESHING && mDelegate != null) {
            mCurrentRefreshStatus = RefreshStatus.REFRESHING;
            changeRefreshHeaderViewToZero();
            handleRefreshStatusChanged();
            mDelegate.onRefresh();
        }
    }

    /**
     * 结束下拉刷新
     */
    public void endRefreshing() {
        if (mCurrentRefreshStatus == RefreshStatus.REFRESHING) {
            mCurrentRefreshStatus = RefreshStatus.IDLE;
            hiddenRefreshHeaderView();
            handleRefreshStatusChanged();
            mRefreshViewHolder.onEndRefreshing();
        }
    }

    /**
     * 隐藏下拉刷新控件，带动画
     */
    private void hiddenRefreshHeaderView() {
        ValueAnimator animator = ValueAnimator.ofInt(mWholeHeaderView.getPaddingTop(), mMinWholeHeaderViewPaddingTop);
        animator.setDuration(mRefreshViewHolder.getTopAnimDuration());
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int paddingTop = (int) animation.getAnimatedValue();
                mWholeHeaderView.setPadding(0, paddingTop, 0, 0);
            }
        });
        animator.start();
    }

    /**
     * 设置下拉刷新控件的paddingTop到0，带动画
     */
    private void changeRefreshHeaderViewToZero() {
        ValueAnimator animator = ValueAnimator.ofInt(mWholeHeaderView.getPaddingTop(), 0);
        animator.setDuration(mRefreshViewHolder.getTopAnimDuration());
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int paddingTop = (int) animation.getAnimatedValue();
                mWholeHeaderView.setPadding(0, paddingTop, 0, 0);
            }
        });
        animator.start();
    }

    /**
     * 开始上拉加载更多，会触发delegate的onBGARefreshLayoutBeginRefreshing方法
     */
    public void beginLoadingMore() {
        if (!mIsLoadingMore && mLoadMoreFooterView != null && mDelegate != null && mDelegate.onLoadMore()) {
            mIsLoadingMore = true;
            if (mIsShowLoadingMoreView) {
                showLoadingMoreView();
            }
        }
    }

    /**
     * 显示上拉加载更多控件
     */
    private void showLoadingMoreView() {
        mRefreshViewHolder.changeToLoadingMore();
        mLoadMoreFooterView.setVisibility(VISIBLE);

        if (mScrollView != null) {
            new Handler().post(new Runnable() {
                @Override
                public void run() {
                    mScrollView.fullScroll(ScrollView.FOCUS_DOWN);
                }
            });
        }
        if (nestedScrollView != null) {
            new Handler().post(new Runnable() {
                @Override
                public void run() {
                    nestedScrollView.fullScroll(ScrollView.FOCUS_DOWN);
                }
            });
        }
        if (mRecyclerView != null) {
            RecyclerView.LayoutManager layoutManager = mRecyclerView.getLayoutManager();
            if (mRecyclerView.getAdapter() != null && mRecyclerView.getAdapter().getItemCount() > 0) {
                layoutManager.scrollToPosition(mRecyclerView.getAdapter().getItemCount() - 1);
            }
        }
        if (mAbsListView != null) {
            if (mAbsListView.getAdapter() != null && mAbsListView.getAdapter().getCount() > 0) {
                mAbsListView.scrollBy(0, mLoadMoreFooterViewHeight);
            }
        }
    }

    /**
     * 结束上拉加载更多
     */
    public void endLoadingMore() {
        if (mIsLoadingMore) {
            if (mIsShowLoadingMoreView) {
                // 避免WiFi环境下请求数据太快，加载更多控件一闪而过
                mHandler.postDelayed(mDelayHiddenLoadingMoreViewTask, 300);
            } else {
                mIsLoadingMore = false;
            }
        }
    }

    private Runnable mDelayHiddenLoadingMoreViewTask = new Runnable() {
        @Override
        public void run() {
            mIsLoadingMore = false;
            mRefreshViewHolder.onEndLoadingMore();
            mLoadMoreFooterView.setVisibility(GONE);

            if (mAbsListView != null) {
                if (mAbsListView.getAdapter() != null && mAbsListView.getAdapter().getCount() > 0) {
                    mAbsListView.scrollBy(0, -mLoadMoreFooterViewHeight);
                }
            }
        }
    };

    /**
     * 上拉加载更多时是否显示加载更多控件
     *
     * @param isShowLoadingMoreView
     */
    public void setIsShowLoadingMoreView(boolean isShowLoadingMoreView) {
        mIsShowLoadingMoreView = isShowLoadingMoreView;
    }

    public void setOnPullLoadMoreListener(PullLoadMoreListener delegate) {
        mDelegate = delegate;
    }

    public interface PullLoadMoreListener {
        /**
         * 开始刷新
         */
        void onRefresh();

        /**
         * 开始加载更多。由于监听了ScrollView、RecyclerView、AbsListView滚动到底部的事件，所以这里采用返回boolean来处理是否加载更多。否则使用endLoadingMore方法会失效
         *
         * @return 如果要开始加载更多则返回true，否则返回false。（返回false的场景：没有网络、一共只有x页数据并且已经加载了x页数据了）
         */
        boolean onLoadMore();
    }


    public enum RefreshStatus {
        IDLE, PULL_DOWN, RELEASE_REFRESH, REFRESHING
    }

    public static int dp2px(Context context, int dpValue) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpValue, context.getResources().getDisplayMetrics());
    }
}