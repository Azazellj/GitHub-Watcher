package com.azazellj.githubwatcher.ui;

import android.databinding.ViewDataBinding;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.view.View;

import com.azazellj.githubwatcher.common.MockModelFabric;
import com.azazellj.githubwatcher.common.injection.rule.TestComponentRule;
import com.azazellj.githubwatcher.data.model.Repository;
import com.azazellj.githubwatcher.data.response.RepositoryResponse;
import com.azazellj.githubwatcher.ui.main.MainActivity;
import com.azazellj.githubwatcher.util.Constants;
import com.azimolabs.conditionwatcher.ConditionWatcher;
import com.azimolabs.conditionwatcher.Instruction;
import com.malinskiy.superrecyclerview.SuperRecyclerView;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.RuleChain;
import org.junit.rules.TestRule;
import org.junit.runner.RunWith;

import java.util.List;
import java.util.concurrent.TimeUnit;

import rx.Observable;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.pressImeActionButton;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static com.azazellj.githubwatcher.common.TestConstants.LOAD_TIME;
import static com.azazellj.githubwatcher.util.EspressoTestsMatcher.RECYCLER_MAIN_SEARCH;
import static com.azazellj.githubwatcher.util.EspressoTestsMatcher.noSuperRecyclerViewProgress;
import static com.azazellj.githubwatcher.util.EspressoTestsMatcher.withSuperRecyclerViewID;
import static org.mockito.Mockito.doReturn;

/**
 * Created by azazellj on 10/5/17.
 */
@RunWith(AndroidJUnit4.class)
public class MainActivityTest {

    private final TestComponentRule component =
            new TestComponentRule();
    private final IntentsTestRule<MainActivity> main =
            new IntentsTestRule<>(MainActivity.class, false, false);

    @Rule
    public TestRule chain = RuleChain.outerRule(component).around(main);

    @Before
    public void setup() {
    }


    public class RepositoriesLoadingInstruction extends Instruction {
        private MainActivity mActivity;

        public RepositoriesLoadingInstruction(final MainActivity activity) {
            mActivity = activity;
        }

        @Override
        public String getDescription() {
            return "Repositories are searching";
        }

        @Override
        public boolean checkCondition() {
            if (mActivity == null) {
                return false;
            }

            ViewDataBinding binding = mActivity.getView();

            SuperRecyclerView view = (SuperRecyclerView) binding.getRoot().findViewById(RECYCLER_MAIN_SEARCH);
            return view != null && view.getMoreProgressView().getVisibility() == View.GONE;
        }
    }

    @Test
    public void loadRepositories_AllDataIsValid() throws Exception {
        RepositoryResponse firstResponse = MockModelFabric.randomRepositories(10);
        RepositoryResponse secondResponse = MockModelFabric.randomRepositories(0);
        Integer firstPage = Constants.FIRST_PAGE;
        Integer secondPage = firstPage + 1;

        String searchQuery = MockModelFabric.randomString();

        Observable firstObservable = Observable.just(firstResponse)
                .delay(LOAD_TIME, TimeUnit.MILLISECONDS);
        Observable secondObservable = Observable.just(secondResponse)
                .delay(LOAD_TIME, TimeUnit.MILLISECONDS);

        stubRepositories(firstObservable, searchQuery, firstPage);
        stubRepositories(secondObservable, searchQuery, secondPage);

        main.launchActivity(null);

        onView(withId(android.support.design.R.id.search_button)).perform(click());
        onView(withId(android.support.design.R.id.search_src_text)).perform(typeText(searchQuery), pressImeActionButton());


        ConditionWatcher.waitForCondition(new RepositoriesLoadingInstruction(main.getActivity()));

        checkNoProgress(RECYCLER_MAIN_SEARCH);

        checkRepositories(RECYCLER_MAIN_SEARCH, 0, firstResponse.getItems());

        ConditionWatcher.waitForCondition(new RepositoriesLoadingInstruction(main.getActivity()));

        checkNoProgress(RECYCLER_MAIN_SEARCH);

        checkRepositories(RECYCLER_MAIN_SEARCH, firstResponse.getItems().size(), secondResponse.getItems());

        Thread.sleep(LOAD_TIME);
    }

    private void stubRepositories(Observable observable, String searchQuery, int page) {
        doReturn(observable)
                .when(component.getMockRestService())
                .findRepositories(searchQuery, page);
    }

    private void checkNoProgress(final int superRecyclerViewID) {
        onView(withId(superRecyclerViewID))
                .check(matches(noSuperRecyclerViewProgress()));
    }

    private void checkRepositories(final int recyclerID, final int startPosition, final List<Repository> items) {
        for (int position = 0; position < items.size(); position++) {
            Object item = items.get(position);

            int finalPosition = startPosition + position;

            onView(withSuperRecyclerViewID(recyclerID))
                    .perform(RecyclerViewActions.scrollToPosition(finalPosition));

            onView(withText(((Repository) item).getName()))
                    .check(matches(isDisplayed()));
        }
    }
}
