package com.example.rheophotos.views.search;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.example.rheophotos.R;
import com.example.rheophotos.base.BaseActivity;
import com.example.rheophotos.model.SearchResponse;
import com.example.rheophotos.utils.AppUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SearchActivity
        extends BaseActivity
        implements SearchMvpView,
        TextView.OnEditorActionListener,
        TextWatcher,
        GenericOnScrollListener.OnScrollListener {

    @BindView(R.id.search_edit_text)
    EditText searchEditText;

    @BindView(R.id.cross_icon)
    ImageView crossIcon;

    @BindView(R.id.search_progress)
    ProgressBar searchProgressBar;

    @BindView(R.id.message_layout)
    LinearLayout messageLayout;

    @BindView(R.id.message_image)
    ImageView messageImage;

    @BindView(R.id.message_text)
    TextView messageText;

    @BindView(R.id.search_recycler)
    RecyclerView searchRecycler;

    private SearchAdapter searchAdapter;
    private StaggeredGridLayoutManager layoutManager;
    private SearchMvpPresenter searchMvpPresenter;

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_SEARCH) {
            AppUtils.hideKeyboard(SearchActivity.this);
            searchEditText.clearFocus();

            String searchQuery = searchEditText.getText().toString().trim();
            searchMvpPresenter.setNewSearchString(searchQuery);

            if (searchQuery.isEmpty())
                return false;

            if (searchAdapter != null)
                searchAdapter.clearData();

            searchMvpPresenter.initiateNewSearch();

            return true;
        }
        return false;
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        searchMvpPresenter.onTextChanged(s);
    }

    @Override
    public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
        if (dy > 0 && layoutManager != null && searchAdapter != null) {

            int[] arr = layoutManager.findLastVisibleItemPositions(null);
            int lastVisibleItem = Math.max(arr[0], arr[1]);

            if (!searchMvpPresenter.isRequestRunning()
                    && searchAdapter.getItemCount() - lastVisibleItem <= 7
                    && searchMvpPresenter.getOffset() != searchMvpPresenter.getTotalEstimatedMatches()) {
                searchMvpPresenter.fetchResults();
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ButterKnife.bind(this);
        searchMvpPresenter = new SearchPresenter();
        searchMvpPresenter.onAttached(this);
        layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        AppUtils.changeStatusBarColor(this, R.color.status_bar_color);
        showKeyboard();
        addListeners();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        searchMvpPresenter.onDetached();
    }

    private void addListeners() {
        searchEditText.setOnEditorActionListener(this);
        searchEditText.addTextChangedListener(this);
        searchRecycler.addOnScrollListener(new GenericOnScrollListener(this));
    }

    @OnClick({R.id.search_container, R.id.search_icon, R.id.search_edit_text})
    public void onSearchBarClicked(View view) {
        showKeyboard();
    }

    @OnClick(R.id.cross_icon)
    public void onCrossIconClicked() {
        searchEditText.setText("");
        showKeyboard();
    }

    @Override
    public void displayResultsOnUI(List<SearchResponse.Results> resultsList) {
        showOnlyDataLayout();
        if (searchAdapter == null) {
            searchAdapter = new SearchAdapter(resultsList);
            searchRecycler.setLayoutAnimation(null);
            searchRecycler.setLayoutManager(layoutManager);
            searchRecycler.setAdapter(searchAdapter);
        } else {
            searchAdapter.addMoreData(resultsList);
        }
    }

    private void showOnlyMessageLayout(Drawable drawable, String message) {
        searchProgressBar.setVisibility(View.INVISIBLE);
        searchRecycler.setVisibility(View.INVISIBLE);
        messageLayout.setVisibility(View.VISIBLE);
        messageImage.setImageDrawable(drawable);
        messageText.setText(message);
    }

    private void showProgressBar() {
        searchProgressBar.setVisibility(View.VISIBLE);
        searchRecycler.setVisibility(View.VISIBLE);
        messageLayout.setVisibility(View.GONE);
    }

    private void hideProgressBar() {
        searchProgressBar.setVisibility(View.INVISIBLE);
    }

    private void showOnlyDataLayout() {
        searchProgressBar.setVisibility(View.INVISIBLE);
        searchRecycler.setVisibility(View.VISIBLE);
        messageLayout.setVisibility(View.GONE);
    }

    private void showKeyboard() {
        searchEditText.requestFocus();
        AppUtils.showKeyboard(this, searchEditText);
    }

    @Override
    public void showOrHideCrossIcon(int visibility) {
        crossIcon.setVisibility(visibility);
    }

    @Override
    public void showProgressForNetworkRequest() {
        showProgressBar();
    }

    @Override
    public void hideProgressBarForNetworkRequest() {
        hideProgressBar();
    }

    @Override
    public void showMessage(int drawableId, int stringId) {
        showOnlyMessageLayout(AppUtils.getAppDrawable(drawableId, this), AppUtils.getAppString(stringId, this));
    }

    @Override
    public void showToast(int stringId) {
        Toast.makeText(this, stringId, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void afterTextChanged(Editable s) {

    }


}
