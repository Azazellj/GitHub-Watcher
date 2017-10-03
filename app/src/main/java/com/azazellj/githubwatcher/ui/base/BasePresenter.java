package com.azazellj.githubwatcher.ui.base;

/**
 * Created by azazellj on 10/3/17.
 *
 * @param <T> view
 */

public abstract class BasePresenter<T extends MvpView>
        implements Presenter<T> {

    /**
     * Main view.
     */
    private T mMvpView;

    /**
     * Create event.
     */
    @Override
    public void attachView(T mvpView) {
        mMvpView = mvpView;
    }

    /**
     * Destroy event.
     */
    @Override
    public void detachView() {
        mMvpView = null;
    }

    /**
     * Check if view is attached.
     *
     * @return true, if view is attached
     */
    private boolean isViewAttached() {
        return mMvpView != null;
    }

    /**
     * Get main view.
     *
     * @return main view
     */
    public T getMvpView() {
        checkViewAttached();

        return mMvpView;
    }

    /**
     * Check if view is attached.
     */
    public void checkViewAttached() {
        if (!isViewAttached()) {
            throw new MvpViewNotAttachedException(getClass().getSimpleName());
        }
    }

    public static class MvpViewNotAttachedException extends RuntimeException {
        /**
         * Default constructor.
         */
        private MvpViewNotAttachedException(final String className) {
            super("Please call Presenter.attachView(MvpView) before"
                    + " requesting data to the " + className);
        }
    }
}