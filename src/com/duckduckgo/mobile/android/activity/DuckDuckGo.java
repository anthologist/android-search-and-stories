package com.duckduckgo.mobile.android.activity;

import android.app.SearchManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.TypedValue;
import android.view.*;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnTouchListener;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListPopupWindow;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

import com.duckduckgo.mobile.android.DDGApplication;
import com.duckduckgo.mobile.android.R;
import com.duckduckgo.mobile.android.adapters.AutoCompleteResultsAdapter;
import com.duckduckgo.mobile.android.adapters.MultiHistoryAdapter;
import com.duckduckgo.mobile.android.bus.BusProvider;
import com.duckduckgo.mobile.android.container.DuckDuckGoContainer;
import com.duckduckgo.mobile.android.dialogs.NewSourcesDialogBuilder;
import com.duckduckgo.mobile.android.dialogs.menuDialogs.HistorySearchMenuDialog;
import com.duckduckgo.mobile.android.dialogs.menuDialogs.HistoryStoryMenuDialog;
import com.duckduckgo.mobile.android.dialogs.menuDialogs.MainFeedMenuDialog;
import com.duckduckgo.mobile.android.dialogs.menuDialogs.SavedSearchMenuDialog;
import com.duckduckgo.mobile.android.dialogs.menuDialogs.SavedStoryMenuDialog;
import com.duckduckgo.mobile.android.events.DismissBangPopupEvent;
import com.duckduckgo.mobile.android.events.DisplayScreenEvent;
import com.duckduckgo.mobile.android.events.HandleShareButtonClickEvent;
import com.duckduckgo.mobile.android.events.HistoryItemLongClickEvent;
import com.duckduckgo.mobile.android.events.HistoryItemSelectedEvent;
import com.duckduckgo.mobile.android.events.ReloadEvent;
import com.duckduckgo.mobile.android.events.RequestOpenWebPageEvent;
import com.duckduckgo.mobile.android.events.RequestSyncAdaptersEvent;
import com.duckduckgo.mobile.android.events.SetMainButtonHomeEvent;
import com.duckduckgo.mobile.android.events.SetMainButtonMenuEvent;
import com.duckduckgo.mobile.android.events.StopActionEvent;
import com.duckduckgo.mobile.android.events.SyncAdaptersEvent;
import com.duckduckgo.mobile.android.events.WebViewEvents.WebViewBackPressActionEvent;
import com.duckduckgo.mobile.android.events.WebViewEvents.WebViewClearBrowserStateEvent;
import com.duckduckgo.mobile.android.events.WebViewEvents.WebViewClearCacheAndCookiesEvent;
import com.duckduckgo.mobile.android.events.WebViewEvents.WebViewClearCacheEvent;
import com.duckduckgo.mobile.android.events.WebViewEvents.WebViewOpenMenuEvent;
import com.duckduckgo.mobile.android.events.WebViewEvents.WebViewReloadActionEvent;
import com.duckduckgo.mobile.android.events.WebViewEvents.WebViewSearchOrGoToUrlEvent;
import com.duckduckgo.mobile.android.events.WebViewEvents.WebViewSearchWebTermEvent;
import com.duckduckgo.mobile.android.events.WebViewEvents.WebViewShowHistoryObjectEvent;
import com.duckduckgo.mobile.android.events.deleteEvents.DeleteStoryInHistoryEvent;
import com.duckduckgo.mobile.android.events.deleteEvents.DeleteUrlInHistoryEvent;
import com.duckduckgo.mobile.android.events.externalEvents.SendToExternalBrowserEvent;
import com.duckduckgo.mobile.android.events.feedEvents.FeedCancelSourceFilterEvent;
import com.duckduckgo.mobile.android.events.feedEvents.FeedCleanImageTaskEvent;
import com.duckduckgo.mobile.android.events.feedEvents.MainFeedItemLongClickEvent;
import com.duckduckgo.mobile.android.events.feedEvents.SavedFeedItemLongClickEvent;
import com.duckduckgo.mobile.android.events.fontSizeEvents.FontSizeCancelScalingEvent;
import com.duckduckgo.mobile.android.events.fontSizeEvents.FontSizeOnProgressChangedEvent;
import com.duckduckgo.mobile.android.events.leftMenuEvents.LeftMenuChangeVisibilityEvent;
import com.duckduckgo.mobile.android.events.leftMenuEvents.LeftMenuClearSelectEvent;
import com.duckduckgo.mobile.android.events.leftMenuEvents.LeftMenuCloseEvent;
import com.duckduckgo.mobile.android.events.leftMenuEvents.LeftMenuHistoryDisabledEvent;
import com.duckduckgo.mobile.android.events.leftMenuEvents.LeftMenuHomeClickEvent;
import com.duckduckgo.mobile.android.events.leftMenuEvents.LeftMenuMarkSelectedEvent;
import com.duckduckgo.mobile.android.events.leftMenuEvents.LeftMenuSavedClickEvent;
import com.duckduckgo.mobile.android.events.leftMenuEvents.LeftMenuSetAdapterEvent;
import com.duckduckgo.mobile.android.events.leftMenuEvents.LeftMenuSetHomeSelectedEvent;
import com.duckduckgo.mobile.android.events.leftMenuEvents.LeftMenuSetRecentVisibleEvent;
import com.duckduckgo.mobile.android.events.leftMenuEvents.LeftMenuSetSavedSelectedEvent;
import com.duckduckgo.mobile.android.events.leftMenuEvents.LeftMenuSetStoriesSelectedEvent;
import com.duckduckgo.mobile.android.events.leftMenuEvents.LeftMenuSettingsClickEvent;
import com.duckduckgo.mobile.android.events.leftMenuEvents.LeftMenuStoriesClickEvent;
import com.duckduckgo.mobile.android.events.pasteEvents.RecentSearchPasteEvent;
import com.duckduckgo.mobile.android.events.pasteEvents.SavedSearchPasteEvent;
import com.duckduckgo.mobile.android.events.pasteEvents.SuggestionPasteEvent;
import com.duckduckgo.mobile.android.events.saveEvents.SaveSearchEvent;
import com.duckduckgo.mobile.android.events.saveEvents.SaveStoryEvent;
import com.duckduckgo.mobile.android.events.saveEvents.UnSaveSearchEvent;
import com.duckduckgo.mobile.android.events.saveEvents.UnSaveStoryEvent;
import com.duckduckgo.mobile.android.events.savedSearchEvents.SavedSearchItemLongClickEvent;
import com.duckduckgo.mobile.android.events.savedSearchEvents.SavedSearchItemSelectedEvent;
import com.duckduckgo.mobile.android.events.searchBarEvents.SearchBarAddClearTextDrawable;
import com.duckduckgo.mobile.android.events.searchBarEvents.SearchBarChangeEvent;
import com.duckduckgo.mobile.android.events.searchBarEvents.SearchBarClearEvent;
import com.duckduckgo.mobile.android.events.searchBarEvents.SearchBarSetProgressEvent;
import com.duckduckgo.mobile.android.events.searchBarEvents.SearchBarSetTextEvent;
import com.duckduckgo.mobile.android.events.shareEvents.ShareFeedEvent;
import com.duckduckgo.mobile.android.events.shareEvents.ShareSearchEvent;
import com.duckduckgo.mobile.android.events.shareEvents.ShareWebPageEvent;
import com.duckduckgo.mobile.android.fragment.AboutFragment;
import com.duckduckgo.mobile.android.fragment.DuckModeFragment;
import com.duckduckgo.mobile.android.fragment.FeedFragment;
import com.duckduckgo.mobile.android.fragment.HelpFeedbackFragment;
import com.duckduckgo.mobile.android.fragment.PreferencesFragment;
import com.duckduckgo.mobile.android.fragment.RecentSearchFragment;
import com.duckduckgo.mobile.android.fragment.RecentsFragment;
import com.duckduckgo.mobile.android.fragment.SavedFragment;
import com.duckduckgo.mobile.android.fragment.SearchFragment;
import com.duckduckgo.mobile.android.fragment.StoredItemsFragment;
import com.duckduckgo.mobile.android.fragment.TempFragment;
import com.duckduckgo.mobile.android.fragment.WebFragment;
import com.duckduckgo.mobile.android.objects.FeedObject;
import com.duckduckgo.mobile.android.objects.SuggestObject;
import com.duckduckgo.mobile.android.tasks.ScanAppsTask;
import com.duckduckgo.mobile.android.util.AppStateManager;
import com.duckduckgo.mobile.android.util.DDGConstants;
import com.duckduckgo.mobile.android.util.DDGControlVar;
import com.duckduckgo.mobile.android.util.DDGUtils;
import com.duckduckgo.mobile.android.util.DisplayStats;
import com.duckduckgo.mobile.android.util.PreferencesManager;
import com.duckduckgo.mobile.android.util.SCREEN;
import com.duckduckgo.mobile.android.util.SESSIONTYPE;
import com.duckduckgo.mobile.android.util.Sharer;
import com.duckduckgo.mobile.android.util.SuggestType;
import com.duckduckgo.mobile.android.util.TorIntegrationProvider;
import com.duckduckgo.mobile.android.views.DDGDrawerLayout;
import com.duckduckgo.mobile.android.views.DDGOverflowMenu;
import com.duckduckgo.mobile.android.views.SeekBarHint;
import com.duckduckgo.mobile.android.views.WelcomeScreenView;
import com.duckduckgo.mobile.android.views.autocomplete.BackButtonPressedEventListener;
import com.duckduckgo.mobile.android.views.autocomplete.DDGAutoCompleteTextView;
import com.duckduckgo.mobile.android.widgets.BangButtonExplanationPopup;
import com.squareup.otto.Subscribe;

import java.util.List;

//import net.hockeyapp.android.CrashManager;
//import net.hockeyapp.android.UpdateManager;

public class DuckDuckGo extends ActionBarActivity implements OnClickListener {
	protected final String TAG = "DuckDuckGo";
    private KeyboardService keyboardService;

	private DDGAutoCompleteTextView searchField = null;
    private FrameLayout searchFieldContainer = null;
	//private HistoryListView leftRecentView = null;//drawer fragment

	private DDGDrawerLayout drawer;
	private View contentView = null;
	
	//private HistoryListView recentSearchView = null;//recent search fragment

	private ImageButton mainButton = null;
	//private ImageButton bangButton = null;
	//private ImageButton shareButton = null;
    private ImageButton homebutton = null;
    private ImageButton bangButton = null;

	private View mainContentView;
	private FrameLayout fragmentContainer;

	private FragmentManager fragmentManager;
	private WebFragment webFragment;
	private DuckModeFragment duckModeFragment;
	private RecentSearchFragment recentSearchFragment;
	private SavedFragment savedFragment;
    //private StoredItemsFragment savedFragment;
    private RecentsFragment recentsFragment;
	private FeedFragment feedFragment;
    private SearchFragment searchFragment;

	public Toolbar toolbar;
	private ActionBar actionBar;
	
	// font scaling
	private LinearLayout fontSizeLayout = null;
	
	// welcome screen
	private WelcomeScreenView welcomeScreenLayout = null;
	OnClickListener welcomeCloseListener = null;
		
	private SharedPreferences sharedPreferences;
		
	public boolean savedState = false;
		
	private final int PREFERENCES_RESULT = 0;

	// keep prev progress in font seek bar, to make incremental changes available
	SeekBarHint fontSizeSeekBar;

    private View searchBar;
    private View dropShadowDivider;

	private boolean shouldShowBangButtonExplanation;

	private BangButtonExplanationPopup bangButtonExplanationPopup;

    private DDGOverflowMenu overflowMenu = null;
    
    /**
     * save feed by object or by the feed id
     * 
     * @param feedObject
     * @param pageFeedId
     */
    public void itemSaveFeed(FeedObject feedObject, String pageFeedId) {
    	if(feedObject != null) {
    		if(DDGApplication.getDB().existsAllFeedById(feedObject.getId())) {
    			DDGApplication.getDB().makeItemVisible(feedObject.getId());
    		}
    		else {
    			DDGApplication.getDB().insertVisible(feedObject);
    		}
    	}
    	else if(pageFeedId != null && pageFeedId.length() != 0){
    		DDGApplication.getDB().makeItemVisible(pageFeedId);
    	}
    }
    
    public void itemSaveSearch(String query) {
        //Log.e("aaa", "saving query: "+query);
    	DDGApplication.getDB().insertSavedSearch(query);
    }
    
    public void syncAdapters() {
    	DDGControlVar.mDuckDuckGoContainer.historyAdapter.sync();
    	BusProvider.getInstance().post(new SyncAdaptersEvent());
    }
    
    /**
     * Adds welcome screen on top of content view
     * Also disables dispatching of touch events from viewPager to children views
     */
    private void addWelcomeScreen() {
		drawer.lockDrawer();

    	if(!getResources().getBoolean(R.bool.welcomeScreen_allowLandscape)){
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		}
    	
    	// add welcome screen
        welcomeScreenLayout = new WelcomeScreenView(this);
        FrameLayout rootLayout = (FrameLayout)findViewById(android.R.id.content);
        rootLayout.addView(welcomeScreenLayout);
    	welcomeScreenLayout.setOnCloseListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				removeWelcomeScreen();
				setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
			}
		});
    }
    
    /**
     * Removes welcome screen from content view
     * Also enables dispatching of touch events from viewPager
     */
    private void removeWelcomeScreen() {
    	welcomeScreenLayout.setVisibility(View.GONE);

		drawer.unlockDrawer();
		PreferencesManager.setWelcomeShown();
    	// remove welcome screen
		FrameLayout rootLayout = (FrameLayout)findViewById(android.R.id.content);
		rootLayout.removeView(welcomeScreenLayout);
		welcomeScreenLayout = null;
    }
    
    private void showBangButton(boolean visible){
    	mainButton.setVisibility(visible ? View.GONE: View.VISIBLE);
		bangButton.setVisibility(visible ? View.VISIBLE: View.GONE);
		if(shouldShowBangButtonExplanation && visible && welcomeScreenLayout == null){
			bangButtonExplanationPopup = BangButtonExplanationPopup.showPopup(DuckDuckGo.this, bangButton);
			shouldShowBangButtonExplanation = false;
		}
		if(!visible){
			if(bangButtonExplanationPopup!=null){
				bangButtonExplanationPopup.dismiss();
			}
		}
    }

    @Override
    protected void onStart() {
        super.onStart();
		TorIntegrationProvider.getInstance(this).prepareTorSettings();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        keyboardService = new KeyboardService(this);
        supportRequestWindowFeature(Window.FEATURE_PROGRESS);

        showNewSourcesDialog();

        sharedPreferences = DDGApplication.getSharedPreferences();
        
        String themeName = PreferencesManager.getThemeName();
		int themeId = getResources().getIdentifier(themeName, "style", getPackageName());
		if(themeId != 0) {
			setTheme(themeId);
		}
		
		PreferencesManager.setFontDefaultsFromTheme(this);
        		        
        //setContentView(R.layout.drawer);
		setContentView(R.layout.main_temp);
        
        DDGUtils.displayStats = new DisplayStats(this);        
        
        if(savedInstanceState != null)
        	savedState = true;
        
        DDGControlVar.isAutocompleteActive = !PreferencesManager.getTurnOffAutocomplete();
        // always refresh on start
        DDGControlVar.hasUpdatedFeed = false;
        DDGControlVar.mDuckDuckGoContainer = (DuckDuckGoContainer) getLastCustomNonConfigurationInstance();
    	if(DDGControlVar.mDuckDuckGoContainer == null){
            initializeContainer();
    	}

		mainContentView = (View) findViewById(R.id.activityContainer);

		drawer = (DDGDrawerLayout) findViewById(R.id.mainDrawer);

		fragmentContainer = (FrameLayout) findViewById(R.id.fragmentContainer);

		fragmentManager = getSupportFragmentManager();
        initFragments();

		drawer.setLeftMenuView(fragmentManager.findFragmentById(R.id.drawerFragment));
		contentView = (View) findViewById(R.id.mainView);

        if(!PreferencesManager.isWelcomeShown()) {
            addWelcomeScreen();
            shouldShowBangButtonExplanation = true;
    	}

		//initSearchBar();
        initActionBar();

        overflowMenu = new DDGOverflowMenu(this);
/*aaa
        //mainButton = (ImageButton) contentView.findViewById(R.id.settingsButton);
		mainButton = (ImageButton) actionBar.getCustomView().findViewById(R.id.settingsButton);
        mainButton.setOnClickListener(this);
        //bangButton = (ImageButton)contentView.findViewById(R.id.bangButton);
		bangButton = (ImageButton) actionBar.getCustomView().findViewById(R.id.bangButton);
        bangButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				getSearchField().addBang();				
			}
		});
*/
        if(DDGControlVar.mDuckDuckGoContainer.webviewShowing) {
        	setMainButtonHome();
        }
        /*
        shareButton = (ImageButton) contentView.findViewById(R.id.shareButton);
        shareButton.setOnClickListener(this);
        
        // adjust visibility of share button after screen rotation
        if(DDGControlVar.mDuckDuckGoContainer.webviewShowing) {
        	shareButton.setVisibility(View.VISIBLE);
        }*/
        //initSearchBar();

		initFontSizeLayout();

		if(savedInstanceState==null) {
			displayHomeScreen();
		}

        checkForUpdates();
    }

	private void setMainButtonHome() {
		//mainButton.setImageResource(R.drawable.ic_home);
	}

	private void setMainButtonMenu() {
		//mainButton.setImageResource(R.drawable.ic_menu);
	}

    private void updateActionBar(String tag) {
        //Log.e("aaa", "update actionbar: "+tag);
        //Log.e("aaa", "current screen: "+DDGControlVar.mDuckDuckGoContainer.currentScreen);

        dropShadowDivider.setVisibility(View.VISIBLE);

        SCREEN screen = getScreenByTag(tag);

        switch(screen) {
            case SCR_STORIES:/*
                actionBar.setDisplayShowCustomEnabled(false);
                actionBar.setDisplayShowTitleEnabled(true);
                actionBar.setTitle("Ciao");*/
                setActionBarShadow(true);
                hasOverflowButtonVisible(true);
                setHomeButton(DDGControlVar.START_SCREEN!=screen);
                //setHomeButton(false);
                //setActionBarMarginStart(DDGControlVar.START_SCREEN==screen);
                //setActionBarMarginEnd(false);
                break;
            case SCR_RECENTS:
                setActionBarShadow(false);
                hasOverflowButtonVisible(false);
                setHomeButton(DDGControlVar.START_SCREEN!=screen);
                //setActionBarMarginStart(DDGControlVar.START_SCREEN==screen);
                //setActionBarMarginEnd(true);
                break;
            case SCR_SAVED:
                setActionBarShadow(false);
                hasOverflowButtonVisible(false);
                setHomeButton(DDGControlVar.START_SCREEN!=screen);
                //setActionBarMarginStart(DDGControlVar.START_SCREEN==screen);
                //setActionBarMarginEnd(true);
                break;
            case SCR_WEBVIEW:
                setActionBarShadow(true);
                hasOverflowButtonVisible(true);
                setHomeButton(true);
                //setHomeButton(false);

                //setOverflowButton();



                break;
            case SCR_SEARCH:
                setActionBarShadow(true);
                hasOverflowButtonVisible(false);
                setBangButton();
                //setActionBarMarginStart(true);
                //setActionBarMarginEnd(true);
                break;
            case SCR_ABOUT:
                setActionBarShadow(true);
                hasOverflowButtonVisible(false);
                setHomeButton(true);
                //setActionBarMarginStart(false);
                break;
            case SCR_HELP:
                setActionBarShadow(true);
                hasOverflowButtonVisible(false);
                setHomeButton(true);
                //  setActionBarMarginStart(false);
                break;
            case SCR_SETTINGS:
                setActionBarShadow(true);
                hasOverflowButtonVisible(false);
                setHomeButton(true);
                //setActionBarMarginStart(false);
                break;
            default:
                break;
        }



    }

    private SCREEN getScreenByTag(String tag) {
        if(tag.equals(RecentsFragment.TAG)) {
            return SCREEN.SCR_RECENTS;
        } else if(tag.equals(SavedFragment.TAG)) {
            return SCREEN.SCR_SAVED;
        } else if(tag.equals(WebFragment.TAG)) {
            return SCREEN.SCR_WEBVIEW;
        } else if(tag.equals(SearchFragment.TAG)) {
            return SCREEN.SCR_SEARCH;
        } else if(tag.equals(AboutFragment.TAG)) {
            return SCREEN.SCR_ABOUT;
        } else if(tag.equals(HelpFeedbackFragment.TAG)) {
            return SCREEN.SCR_HELP;
        } else if(tag.equals("preferencetag")) {
            // scr settings
        }
        return SCREEN.SCR_STORIES;
    }

    /*private void changeSearchBar(SCREEN screen) {
        dropShadowDivider.setVisibility(View.VISIBLE);
        toolbar.setContentInsetsRelative(0, (int)(48*getResources().getDisplayMetrics().density));
        switch(screen) {
            case SCR_STORIES:
                //actionBar.getCustomView().setPadding(6,6,6,6);
                //actionBar.setDisplayShowCustomEnabled(true);
                //actionBar.setDisplayShowTitleEnabled(false);
                //actionBar.getCustomView().setVisibility(View.VISIBLE);
                //actionBar.setTitle("FEEDS");
                //actionBar.setDisplayHomeAsUpEnabled(true);
                //actionBar.setHomeAsUpIndicator(R.drawable.ic_home);
                showSearchField();
                //setHomeButton(DDGControlVar.START_SCREEN!=screen);
                setHomeButton(false);
                break;
            case SCR_RECENTS:
                toolbar.setContentInsetsRelative(0,0);
                dropShadowDivider.setVisibility(View.GONE);
                //actionBar.setDisplayShowCustomEnabled(false);
                //actionBar.setDisplayShowTitleEnabled(true);
                //actionBar.setTitle("Recents");
                //setHomeButton(DDGControlVar.START_SCREEN!=screen);
                setHomeButton(true);
                break;
            case SCR_SAVED:
                toolbar.setContentInsetsRelative(0,0);
                dropShadowDivider.setVisibility(View.GONE);
                //actionBar.setDisplayShowCustomEnabled(false);
                //actionBar.setDisplayShowTitleEnabled(true);
                //actionBar.setTitle("Saved");
                setHomeButton(DDGControlVar.START_SCREEN != screen);
                break;
            case SCR_WEBVIEW:
                //actionBar.getCustomView().setPadding(6,6,6,0);
                //actionBar.setDisplayShowCustomEnabled(true);
                //actionBar.setDisplayShowTitleEnabled(false);
                showSearchField();
                //actionBar.getCustomView().setVisibility(View.VISIBLE);
                //actionBar.setTitle("Weview");
                setHomeButton(false);
                //setBangButton();
                break;
            case SCR_SEARCH:
                toolbar.setContentInsetsRelative(0,0);
                //actionBar.setDisplayShowCustomEnabled(false);
                actionBar.setDisplayShowTitleEnabled(true);
                //actionBar.setTitle("Search");
                setBangButton();
                break;
            case SCR_ABOUT:
                //actionBar.setDisplayShowCustomEnabled(false);
                //actionBar.getCustomView().setVisibility(View.GONE);
                //actionBar.setDisplayShowTitleEnabled(true);
                //actionBar.setTitle("About");
                showActionBarTitle("About");
                setHomeButton(true);
                break;
            case SCR_HELP:
                //actionBar.setDisplayShowCustomEnabled(false);
                //actionBar.getCustomView().setVisibility(View.GONE);
                //actionBar.setDisplayShowTitleEnabled(true);
                //actionBar.setTitle("Help & Feedback");
                //showActionBarTitle("Help & Feedback");
                setHomeButton(true);
                break;
            case SCR_SETTINGS:
                //actionBar.setDisplayShowCustomEnabled(false);
                //actionBar.getCustomView().setVisibility(View.GONE);
                //actionBar.setDisplayShowTitleEnabled(true);
                //actionBar.setTitle("Settings");
                //actionBar.setDisplayHomeAsUpEnabled(true);
                break;
            default:
                break;
        }

    }*/
/*
    private void initActionBar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayShowCustomEnabled(true);

        View actionBarView = LayoutInflater.from(this).inflate(R.layout.actionbar_temp, null);
        ActionBar.LayoutParams params = new ActionBar.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        actionBar.setCustomView(actionBarView, params);
    }
*/

    private void showActionBarTitle(String newTitle) {
        TextView title = (TextView) actionBar.getCustomView().findViewById(R.id.actionbar_title);
        title.setText(newTitle);
        title.setVisibility(View.VISIBLE);
        actionBar.getCustomView().findViewById(R.id.search_container).setVisibility(View.GONE);
    }

    private void showSearchField() {
        actionBar.getCustomView().findViewById(R.id.search_container).setVisibility(View.VISIBLE);
        actionBar.getCustomView().findViewById(R.id.actionbar_title).setVisibility(View.GONE);
    }

	private void initActionBar() {

        //Toolbar toolbar2 = (Toolbar) findViewById(R.id.toolbar2);

        Toolbar toolbar2 = (Toolbar) findViewById(R.id.toolbar2);
        toolbar2.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                return false;
            }
        });
        toolbar2.inflateMenu(R.menu.feeds);


		toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);
		actionBar = getSupportActionBar();
		actionBar.setDisplayShowTitleEnabled(false);
		actionBar.setDisplayShowCustomEnabled(true);
        //actionBar.setDisplayShowCustomEnabled(false);

		View actionBarView = LayoutInflater.from(this).inflate(R.layout.actionbar_temp, null);
		ActionBar.LayoutParams params = new ActionBar.LayoutParams(
				ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        //params.setMarginEnd(100);
		actionBar.setCustomView(actionBarView, params);

		searchBar = actionBar.getCustomView().findViewById(R.id.searchBar);
		dropShadowDivider = findViewById(R.id.dropshadow_top);
        searchFieldContainer = (FrameLayout) actionBar.getCustomView().findViewById(R.id.search_container);
		searchField = (DDGAutoCompleteTextView) actionBar.getCustomView().findViewById(R.id.searchEditText);
		getSearchField().setAdapter(DDGControlVar.mDuckDuckGoContainer.acAdapter);
		getSearchField().setOnEditorActionListener(new OnEditorActionListener() {
			@Override
			public boolean onEditorAction(TextView textView, int actionId, KeyEvent event) {
				if(textView == getSearchField() && actionId != EditorInfo.IME_NULL) {
					keyboardService.hideKeyboard(getSearchField());
					getSearchField().dismissDropDown();
					searchOrGoToUrl(getSearchField().getTrimmedText());
				}
				return false;
			}
		});

		getSearchField().setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				drawer.close();
                //displayScreen(SCREEN.SCR_SEARCH, true);
//				showBangButton(true);
			}
		});
		getSearchField().setOnFocusChangeListener(new OnFocusChangeListener() {
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
//				showBangButton(hasFocus);
                if(hasFocus) {
                    //displayScreen(SCREEN.SCR_SEARCH, true);
                }
			}
		});

		getSearchField().setOnBackButtonPressedEventListener(new BackButtonPressedEventListener() {
			@Override
			public void onBackButtonPressed() {
				if(getSearchField().isPopupShowing()){
					getSearchField().dismissDropDown();
				}
//				showBangButton(false);
			}
		});

		getSearchField().setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				getSearchField().dismissDropDown();

				SuggestObject suggestObject = DDGControlVar.mDuckDuckGoContainer.acAdapter.getItem(position);
				if (suggestObject != null) {
					SuggestType suggestType = suggestObject.getType();
					if(suggestType == SuggestType.TEXT) {
						if(PreferencesManager.getDirectQuery()){
							String text = suggestObject.getPhrase().trim();
							if(suggestObject.hasOnlyBangQuery()){
								getSearchField().addTextWithTrailingSpace(suggestObject.getPhrase());
							}else{
								keyboardService.hideKeyboard(getSearchField());
								searchOrGoToUrl(text);
							}
						}
					}
					else if(suggestType == SuggestType.APP) {
						DDGUtils.launchApp(DuckDuckGo.this, suggestObject.getSnippet());
					}
				}
			}
		});

		// This makes a little (X) to clear the search bar.
		DDGControlVar.mDuckDuckGoContainer.stopDrawable.setBounds(0, 0, (int)Math.floor(DDGControlVar.mDuckDuckGoContainer.stopDrawable.getIntrinsicWidth()/1.5), (int)Math.floor(DDGControlVar.mDuckDuckGoContainer.stopDrawable.getIntrinsicHeight()/1.5));
		getSearchField().setCompoundDrawables(null, null, getSearchField().getText().toString().equals("") ? null : DDGControlVar.mDuckDuckGoContainer.stopDrawable, null);

		getSearchField().setOnTouchListener(new OnTouchListener() {
			public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_DOWN) {
					DDGControlVar.mCleanSearchBar = true;
					//getSearchField().setBackgroundDrawable(DDGControlVar.mDuckDuckGoContainer.searchFieldDrawable);//aaa
				}

				if (getSearchField().getCompoundDrawables()[2] == null) {
					return false;
				}
				if (event.getAction() != MotionEvent.ACTION_UP) {
					return false;
				}
				if (event.getX() > getSearchField().getWidth() - getSearchField().getPaddingRight() - DDGControlVar.mDuckDuckGoContainer.stopDrawable.getIntrinsicWidth()) {
					if(getSearchField().getCompoundDrawables()[2] == DDGControlVar.mDuckDuckGoContainer.stopDrawable) {
						stopAction();
					}
					else {
						reloadAction();
					}
				}
				return false;
			}

		});

		getSearchField().addTextChangedListener(new TextWatcher() {
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				getSearchField().setCompoundDrawables(null, null, getSearchField().getText().toString().equals("") ? null : DDGControlVar.mDuckDuckGoContainer.stopDrawable, null);
			}

			public void afterTextChanged(Editable arg0) {
			}

			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}
		});
	}

	private void initFontSizeLayout() {
		fontSizeLayout = (LinearLayout) findViewById(R.id.fontSeekLayout);

		fontSizeSeekBar = (SeekBarHint) findViewById(R.id.fontSizeSeekBar);

		DDGControlVar.mainTextSize = PreferencesManager.getMainFontSize();

		DDGControlVar.recentTextSize = PreferencesManager.getRecentFontSize();

		fontSizeSeekBar.setProgress(DDGControlVar.fontPrevProgress);
		fontSizeSeekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
			}

			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
										  boolean fromUser) {
				if(!fromUser) return;

				int diff = progress - DDGControlVar.fontPrevProgress;
				float diffPixel = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,
						(float) diff, getResources().getDisplayMetrics());
				// set thumb text
				if(diff == 0) {
					fontSizeSeekBar.setExtraText(getResources().getString(R.string.NoChange));
				}
				else if(progress == DDGConstants.FONT_SEEKBAR_MID) {
					fontSizeSeekBar.setExtraText(getResources().getString(R.string.Default));
				}
				else if(progress > DDGConstants.FONT_SEEKBAR_MID) {
					fontSizeSeekBar.setExtraText("+" + (progress-DDGConstants.FONT_SEEKBAR_MID));
				}
				else {
					fontSizeSeekBar.setExtraText(String.valueOf((progress-DDGConstants.FONT_SEEKBAR_MID)));
				}
				DDGControlVar.fontProgress = progress;
				DDGControlVar.mainTextSize = PreferencesManager.getMainFontSize() + diffPixel;

				DDGControlVar.recentTextSize = PreferencesManager.getRecentFontSize() + diffPixel;

				DDGControlVar.ptrHeaderSize = PreferencesManager.getPtrHeaderTextSize() + diff;
				DDGControlVar.ptrSubHeaderSize = PreferencesManager.getPtrHeaderSubTextSize() + diff;

				DDGControlVar.webViewTextSize = PreferencesManager.getWebviewFontSize() + diff;

				DDGControlVar.leftTitleTextSize = PreferencesManager.getLeftTitleTextSize() + diffPixel;

				BusProvider.getInstance().post(new FontSizeOnProgressChangedEvent());
			}
		});

		Button fontSizeApplyButton = (Button) findViewById(R.id.fontSizeApplyButton);
		fontSizeApplyButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				DDGControlVar.fontPrevProgress = DDGControlVar.fontProgress;
				fontSizeSeekBar.setExtraText(null);

				PreferencesManager.saveAdjustedTextSizes();

				closeFontSlider();
			}
		});

		Button fontSizeCancelButton = (Button) findViewById(R.id.fontSizeCancelButton);
		fontSizeCancelButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				cancelFontScaling();
			}
		});
	}

    private void initializeContainer() {
        DDGControlVar.mDuckDuckGoContainer = new DuckDuckGoContainer();

        DDGControlVar.mDuckDuckGoContainer.webviewShowing = false;

        DDGControlVar.mDuckDuckGoContainer.stopDrawable = DuckDuckGo.this.getResources().getDrawable(R.drawable.stop);
//    		DDGControlVar.mDuckDuckGoContainer.reloadDrawable = DuckDuckGo.this.getResources().getDrawable(R.drawable.reload);
        DDGControlVar.mDuckDuckGoContainer.progressDrawable = DuckDuckGo.this.getResources().getDrawable(R.drawable.page_progress);
        DDGControlVar.mDuckDuckGoContainer.searchFieldDrawable = DuckDuckGo.this.getResources().getDrawable(R.drawable.searchfield);
        DDGControlVar.mDuckDuckGoContainer.searchFieldDrawable.setAlpha(150);

        DDGControlVar.mDuckDuckGoContainer.historyAdapter = new MultiHistoryAdapter(this);
		BusProvider.getInstance().post(new LeftMenuSetAdapterEvent());

        DDGControlVar.mDuckDuckGoContainer.acAdapter = new AutoCompleteResultsAdapter(this);
    }

    // Assist action is better known as Google Now gesture
    private boolean isLaunchedWithAssistAction(){
        return getIntent() != null && getIntent().getAction() != null && getIntent().getAction().equals(Intent.ACTION_ASSIST);
    }

    private void showNewSourcesDialog() {
        if(PreferencesManager.shouldShowNewSourcesDialog()){
            new NewSourcesDialogBuilder(this).show();
            PreferencesManager.newSourcesDialogWasShown();
        }
    }
	
	public void clearSearchBar() {
		getSearchField().setText("");
    	getSearchField().setCompoundDrawables(null, null, null, null);
		//getSearchField().setBackgroundDrawable(DDGControlVar.mDuckDuckGoContainer.searchFieldDrawable);//aaa
	}
	
	public void setSearchBarText(String text) {
        Log.e("aaa", "set search bar text: "+text);
        if(text.startsWith("https://")) {
            text = text.replace("https://", "");
        } else if(text.startsWith("http://")) {
            text = text.replace("http://", "");
        }
        if(text.startsWith("www.")) {
            text = text.replace("www.", "");
        }
		getSearchField().setFocusable(false);
		getSearchField().setFocusableInTouchMode(false);
		getSearchField().setText(text);
		getSearchField().setFocusable(true);
		getSearchField().setFocusableInTouchMode(true);
	}
	
	private void resetScreenState() {
		clearSearchBar();
		DDGControlVar.currentFeedObject = null;
		DDGControlVar.mDuckDuckGoContainer.sessionType = SESSIONTYPE.SESSION_BROWSE;
        resetSearchBar();
	}

    private void resetSearchBar() {
        //searchBar.setBackgroundResource(R.color.topbar_background);
        dropShadowDivider.setVisibility(View.VISIBLE);
    }

    private void closeFontSlider() {
		fontSizeLayout.setVisibility(View.GONE);
		fontSizeSeekBar.setProgress(DDGControlVar.fontPrevProgress);
		PreferencesManager.setFontSliderVisibility(false);
	}
	
	private void cancelFontScaling() {
		fontSizeSeekBar.setExtraText(null);
		DDGControlVar.mainTextSize = PreferencesManager.getMainFontSize();
		DDGControlVar.recentTextSize = PreferencesManager.getRecentFontSize();
		DDGControlVar.webViewTextSize = PreferencesManager.getWebviewFontSize();
		DDGControlVar.leftTitleTextSize = PreferencesManager.getLeftTitleTextSize();
		closeFontSlider();

		BusProvider.getInstance().post(new FontSizeCancelScalingEvent());
	}
	
	/**
	 * Displays given screen (stories, saved, settings etc.)
	 * 
	 * @param screenToDisplay Screen to display
	 * @param clean Whether screen state (searchbar, browser etc.) states will get cleaned
	 */
	public void displayScreen(SCREEN screenToDisplay, boolean clean) {
        //Log.e("aaa", "inside s")
        if(clean) {
			resetScreenState();
		}
		
	    // control which screen is shown & configure related views
			
		if(PreferencesManager.isFontSliderVisible()) {
			fontSizeLayout.setVisibility(View.VISIBLE);
		}
			
		switch(screenToDisplay) {
			case SCR_STORIES:
				displayNewsFeed();
				break;
			case SCR_RECENTS:
				displayRecents();
				break;/*aaa
			case SCR_DUCKMODE:
				displayDuckMode();
				break;*/
            case SCR_WEBVIEW:
                displayWebView();
                break;
            case SCR_SEARCH:
                displaySearch();
                break;
			case SCR_SAVED:
				displaySaved();
				break;
            case SCR_ABOUT:
                displayAbout();
                break;
            case SCR_HELP:
                displayHelp();
                break;
            case SCR_SETTINGS:
                //aaa todo
                break;
            default:
				break;
		}
		/*
		if(DDGControlVar.START_SCREEN == SCREEN.SCR_RECENT_SEARCH &&
				!screenToDisplay.equals(SCREEN.SCR_RECENT_SEARCH)){
			BusProvider.getInstance().post(new LeftMenuSetRecentVisibleEvent());
		}*/
        if(DDGControlVar.START_SCREEN == SCREEN.SCR_RECENTS &&
                !screenToDisplay.equals(SCREEN.SCR_RECENTS)){
            BusProvider.getInstance().post(new LeftMenuSetRecentVisibleEvent());
        }
        //Log.e("aaa", "inside display screen, webview is showing: "+DDGControlVar.mDuckDuckGoContainer.webviewShowing);
	    if(!DDGControlVar.mDuckDuckGoContainer.webviewShowing) {
			DDGControlVar.mDuckDuckGoContainer.prevScreen = DDGControlVar.mDuckDuckGoContainer.currentScreen;
			DDGControlVar.mDuckDuckGoContainer.currentScreen = screenToDisplay;
            //Log.e("aaa", "in - current screen: "+DDGControlVar.mDuckDuckGoContainer.currentScreen+" - prev screen: "+DDGControlVar.mDuckDuckGoContainer.prevScreen);
		}
        //Log.e("aaa", "out - current screen: "+DDGControlVar.mDuckDuckGoContainer.currentScreen+" - prev screen: "+DDGControlVar.mDuckDuckGoContainer.prevScreen);
	}
	
	private void displayHomeScreen() {
		displayScreen(DDGControlVar.START_SCREEN, true);
        /*aaa
		if(DDGControlVar.mDuckDuckGoContainer.sessionType == SESSIONTYPE.SESSION_SEARCH
				|| DDGControlVar.START_SCREEN == SCREEN.SCR_RECENT_SEARCH
                || DDGControlVar.START_SCREEN == SCREEN.SCR_SAVED_FEED
                || DDGControlVar.START_SCREEN == SCREEN.SCR_DUCKMODE) {
            keyboardService.showKeyboard(getSearchField());
		}*/
        if(DDGControlVar.mDuckDuckGoContainer.sessionType == SESSIONTYPE.SESSION_SEARCH
                || DDGControlVar.START_SCREEN == SCREEN.SCR_RECENTS
                || DDGControlVar.START_SCREEN == SCREEN.SCR_SAVED) {
            keyboardService.showKeyboard(getSearchField());
        }
        DDGControlVar.mDuckDuckGoContainer.sessionType = SESSIONTYPE.SESSION_BROWSE;
        //displayAbout();
        //displayHelp();
        //fragmentManager.beginTransaction().replace(fragmentContainer.getId(), new TempFragment(), "ciao").commit();
	}

	@Override
	public void onResume() {
		super.onResume();
		BusProvider.getInstance().register(this);
		
        DDGUtils.displayStats.refreshStats(this);

		// update feeds
		// https://app.asana.com/0/2891531242889/2858723303746
		DDGControlVar.hasUpdatedFeed = false;
		
		// check autocomplete 
		if(!DDGControlVar.isAutocompleteActive) {
			getSearchField().setAdapter(null);
		}
		else {
	        getSearchField().setAdapter(DDGControlVar.mDuckDuckGoContainer.acAdapter);
		}
		
		if(DDGControlVar.includeAppsInSearch && !DDGControlVar.hasAppsIndexed) {
			// index installed apps
			new ScanAppsTask(getApplicationContext()).execute();
			DDGControlVar.hasAppsIndexed = true;
		}

		// global search intent
        Intent intent = getIntent(); 
        
		if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
			intent.setAction(Intent.ACTION_MAIN);
			String query = intent.getStringExtra(SearchManager.QUERY);
			setSearchBarText(query);
			BusProvider.getInstance().post(new WebViewSearchWebTermEvent(query));
		}
		else if(intent.getBooleanExtra("widget", false)) {
            if(!getSearchField().getText().toString().equals("")) {
                clearSearchBar();
            }
			displayScreen(DDGControlVar.START_SCREEN, true);
            keyboardService.showKeyboard(getSearchField());
		}
		else if(DDGControlVar.mDuckDuckGoContainer.webviewShowing){
            keyboardService.hideKeyboard(getSearchField());
			//shareButton.setVisibility(View.VISIBLE);
			displayScreen(SCREEN.SCR_WEBVIEW, false);
		}
        else if(isLaunchedWithAssistAction()){
            keyboardService.showKeyboard(getSearchField());
        }

        checkForCrashes();
	}

	@Override
	public void onPause() {
		super.onPause();
		
		BusProvider.getInstance().unregister(this);

        if(DDGControlVar.mustClearCacheAndCookies) {
			BusProvider.getInstance().post(new WebViewClearCacheAndCookiesEvent());
            DDGControlVar.mustClearCacheAndCookies = false;
        }

		PreferencesManager.saveReadArticles();
		
		// XXX keep these for low memory conditions
		AppStateManager.saveAppState(sharedPreferences, DDGControlVar.mDuckDuckGoContainer, DDGControlVar.currentFeedObject);
	}
	
	@Override
	protected void onStop() {
		PreferencesManager.saveReadArticles();
		super.onStop();
	}
	
	@Override
	protected void onDestroy() {
		DDGApplication.getImageCache().purge();
		super.onDestroy();
	}
	
	@Override
	public void onBackPressed() {
		if(drawer.isOpen()) {
			drawer.close();
		}
		else if(fontSizeLayout.getVisibility() != View.GONE) {
			cancelFontScaling();
		}
		//else if (DDGControlVar.mDuckDuckGoContainer.webviewShowing) {
		else if(DDGControlVar.mDuckDuckGoContainer.currentScreen == SCREEN.SCR_WEBVIEW
				|| DDGControlVar.mDuckDuckGoContainer.webviewShowing
				|| webFragment.isVisible()) {
			BusProvider.getInstance().post(new WebViewBackPressActionEvent());
		}
		// main feed showing & source filter is active
		else if(DDGControlVar.targetSource != null){
			BusProvider.getInstance().post(new FeedCancelSourceFilterEvent());
		}
		else {
			DDGControlVar.hasUpdatedFeed = false;
			super.onBackPressed();
		}
	}
/*
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.webfragment, menu);
		return super.onCreateOptionsMenu(menu);
	}
*/



    @Override
    public boolean onMenuOpened(int featureId, Menu menu) {
        Log.e("aaa", "on menu opened");

        android.support.v7.widget.ListPopupWindow popup = new android.support.v7.widget.ListPopupWindow(this);
        if(openingMenu!=null) {

            //popup.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, android.R.id.text1, new String[] {"ciao", "culo", "suca"}));
            //popup.setAnchorView(toolbar);

            //Log.e("aaa", "overflow menu check: "+overflowMenu.checkMenu());
        }
        if(DDGControlVar.mDuckDuckGoContainer.webviewShowing) {
            if(openingMenu!=null) {
                //onPrepareOptionsMenu(openingMenu);

                //overflowMenu.setMenu(openingMenu);
                //overflowMenu.setAnchorView(toolbar);

                    openingMenu.close();
                BusProvider.getInstance().post(new WebViewOpenMenuEvent(toolbar));
                //BusProvider.getInstance().post(new WebViewOpenMenuEvent(findViewById(android.R.id.content)));
                //popup.show();
                //overflowMenu.show();
                //Log.e("aaa", "safe to close");
            } else {
                //Log.e("aaa", "error, not safe to close");
            }
            if(menu!=null) {
                //menu.close();
            }
            return false;
        }
        return super.onMenuOpened(featureId, menu);
    }

    private Menu openingMenu = null;

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        Log.e("aaa", "on prepare options menu ACTIVITY");
        this.openingMenu = menu;
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case android.R.id.home:
                actionHome();
                return true;
            case R.id.action_favourites:
                actionFavourites();
                return true;
            case R.id.action_history:
                //actionHistory();
                Toast.makeText(this, "TO LNK", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.action_settings:
                //Toast.makeText(this, "TO ")
                actionSettings();
                return true;
            case R.id.action_help_feedback:
                actionHelpFeedback();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void actionHome() {
        if(DDGControlVar.mDuckDuckGoContainer.currentScreen==SCREEN.SCR_SEARCH) {
            //aaa todo add bang
        } else {
            displayHomeScreen();
        }
    }

    private void setHomeButton(boolean visible) {
        ImageButton homeButton = (ImageButton) actionBar.getCustomView().findViewById(R.id.home);
        actionBar.getCustomView().findViewById(R.id.bang).setVisibility(View.GONE);
        actionBar.getCustomView().findViewById(R.id.overflow).setVisibility(View.GONE);
        if(visible) {
            homeButton.setVisibility(View.VISIBLE);
            homeButton.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    displayHomeScreen();
                }
            });
        } else {
            homeButton.setVisibility(View.GONE);
        }
        setActionBarMarginStart(!visible);
    }

    private void setBangButton() {
        actionBar.getCustomView().findViewById(R.id.home).setVisibility(View.GONE);
        actionBar.getCustomView().findViewById(R.id.overflow).setVisibility(View.GONE);

        ImageButton bang = (ImageButton) findViewById(R.id.bang);
        bang.setVisibility(View.VISIBLE);
        bang.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                //todo
                Toast.makeText(DuckDuckGo.this, "TO DO", Toast.LENGTH_SHORT).show();
            }
        });
        setActionBarMarginStart(false);
    }

    private void setOverflowButton() {/*
        ImageView overflow = (ImageView) actionBar.getCustomView().findViewById(R.id.overflow);
        overflow.setVisibility(View.VISIBLE);
        overflow.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                //todo
                Toast.makeText(DuckDuckGo.this, "Open menu diocan", Toast.LENGTH_SHORT).show();
            }
        });
        setActionBarMarginEnd(false);*/
    }

    private void hasOverflowButtonVisible(boolean visible) {
        int endMargin = 0;
        if(visible) {
            endMargin = (int)getResources().getDimension(R.dimen.actionbar_overflow_margin);
        }
        toolbar.setContentInsetsRelative(0, endMargin);
        setActionBarMarginEnd(!visible);
    }

    private void setActionBarMarginStart(boolean visible) {
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) searchFieldContainer.getLayoutParams();
        int margin = 0;
        if(visible) {
            margin = (int) getResources().getDimension(R.dimen.actionbar_margin);
        }
        params.leftMargin = margin;
    }

    private void setActionBarMarginEnd(boolean visible) {
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) searchFieldContainer.getLayoutParams();
        int margin = 0;
        if(visible) {
            margin = (int) getResources().getDimension(R.dimen.actionbar_margin);
        }
        params.rightMargin = margin;
    }

    private void setActionBarShadow(boolean visible) {
        if(visible) {
            dropShadowDivider.setVisibility(View.VISIBLE);
        } else {
            dropShadowDivider.setVisibility(View.GONE);
        }
    }

    private void actionFavourites() {
        //displaySaved();
        displayScreen(SCREEN.SCR_SAVED, true);
        Log.e("aaa", "action favourites");
    }

    private void actionHistory() {
        displayScreen(SCREEN.SCR_RECENTS, true);
        Log.e("aaa", "action history");
    }

    private void actionHelpFeedback(){
        displayScreen(SCREEN.SCR_HELP, false);
        Log.e("aaa", "action help");
    }

    private void actionSettings() {
        displayScreen(SCREEN.SCR_SETTINGS, false);
        Log.e("aaa", "action settings");
    }

	public void reloadAction() {
		DDGControlVar.mCleanSearchBar = false;
        DDGControlVar.mDuckDuckGoContainer.stopDrawable.setBounds(0, 0, (int) Math.floor(DDGControlVar.mDuckDuckGoContainer.stopDrawable.getIntrinsicWidth() / 1.5), (int) Math.floor(DDGControlVar.mDuckDuckGoContainer.stopDrawable.getIntrinsicHeight() / 1.5));
		getSearchField().setCompoundDrawables(null, null, getSearchField().getText().toString().equals("") ? null : DDGControlVar.mDuckDuckGoContainer.stopDrawable, null);

		BusProvider.getInstance().post(new WebViewReloadActionEvent());
	}

	private void stopAction() {
		DDGControlVar.mCleanSearchBar = true;
    	getSearchField().setText("");

    	// This makes a little (X) to clear the search bar.
    	getSearchField().setCompoundDrawables(null, null, null, null);
    	//getSearchField().setBackgroundDrawable(DDGControlVar.mDuckDuckGoContainer.searchFieldDrawable);//aaa
	}

	public void searchOrGoToUrl(String text, SESSIONTYPE sessionType) {
		//displayWebView();//aaa
        displayScreen(SCREEN.SCR_WEBVIEW, false);
		BusProvider.getInstance().post(new WebViewSearchOrGoToUrlEvent(text, sessionType));
	}

	public void searchOrGoToUrl(String text) {
		searchOrGoToUrl(text, SESSIONTYPE.SESSION_BROWSE);
	}

	public void clearRecentSearch() {
		DDGControlVar.mDuckDuckGoContainer.historyAdapter.sync();
	}

	private void clearLeftSelect() {
		BusProvider.getInstance().post(new LeftMenuClearSelectEvent());
	}
	
	/**
	 * main method that triggers display of Preferences screen or fragment
	 */
	private void displaySettings() {
		BusProvider.getInstance().post(new FeedCleanImageTaskEvent());
        if(true || Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
            Intent intent = new Intent(getBaseContext(), Preferences.class);
            startActivityForResult(intent, PREFERENCES_RESULT);
        } else {
            //List<Fragment> fragments = getSupportFragmentManager().getFragments();
            //for(Fragment fragment : fragments) {
                //getSupportFragmentManager().beginTransaction().remove(fragment);
            //}
            //getFragmentManager().beginTransaction().replace(fragmentContainer.getId(), new PreferencesFragment(), "preferences").commit();
        }
        //fragmentManager.beginTransaction().replace(fragmentContainer.getId(), new HelpFeedbackFragment(), "about").commit();
	}

	/**
	 * Method that switches visibility of views for Home or Saved feed
	 */
	private void displayFeedCore() {		
    	// main view visibility changes and keep feed updated
		//shareButton.setVisibility(View.GONE);
    	DDGControlVar.mDuckDuckGoContainer.webviewShowing = false;
	}
	
	public void displayNewsFeed(){
		resetScreenState();
		stopAction();
		
		// left side menu visibility changes
		//BusProvider.getInstance().post(new LeftMenuChangeVisibilityEvent());
    	
    	// adjust "not recording" indicator
		//BusProvider.getInstance().post(new LeftMenuHistoryDisabledEvent());
    	
    	// ensures feed refresh every time user switches to Stories screen
    	DDGControlVar.hasUpdatedFeed = false;
		
		displayFeedCore();
		clearLeftSelect();

		if(feedFragment==null) {
			feedFragment = new FeedFragment();
		}
		if(!feedFragment.isVisible()) {
			changeFragment(feedFragment, FeedFragment.TAG);
		}

    	if(DDGControlVar.START_SCREEN == SCREEN.SCR_STORIES){
    		DDGControlVar.homeScreenShowing = true;
    		//setMainButtonMenu();//aaaa
			//BusProvider.getInstance().post(new LeftMenuSetHomeSelectedEvent(true));//aaa

    	}
    	else {
    		DDGControlVar.homeScreenShowing = false;
			//BusProvider.getInstance().post(new LeftMenuSetStoriesSelectedEvent(true));//aaa
            //setHomeButton();//aaa
        }
	}
	
	public void displaySaved(){
		resetScreenState();
		
		// left side menu visibility changes
		//BusProvider.getInstance().post(new LeftMenuChangeVisibilityEvent());//aaa
    	
		//shareButton.setVisibility(View.GONE);
    	DDGControlVar.mDuckDuckGoContainer.webviewShowing = false;
		clearLeftSelect();

		//savedFragment = new SavedFragment();

		if(!savedFragment.isVisible()) {
			changeFragment(savedFragment, SavedFragment.TAG);
            //changeFragment(savedFragment, StoredItemsFragment.SAVED_TAG);
		}
    	//aaa
    	//if(DDGControlVar.START_SCREEN == SCREEN.SCR_SAVED_FEED){
        if(DDGControlVar.START_SCREEN == SCREEN.SCR_SAVED){
    		DDGControlVar.homeScreenShowing = true;
    		//setMainButtonMenu();//aaa
			//BusProvider.getInstance().post(new LeftMenuSetHomeSelectedEvent(true));//aaa
    	}
    	else {
    		DDGControlVar.homeScreenShowing = false;
			//BusProvider.getInstance().post(new LeftMenuSetSavedSelectedEvent(true));//aaa
            //setHomeButton();//aaa
    	}
	}
	
	public void displayRecents(){
		resetScreenState(); 
		
		// left side menu visibility changes
		BusProvider.getInstance().post(new LeftMenuChangeVisibilityEvent());
		
    	// main view visibility changes
		//shareButton.setVisibility(View.GONE);
    	DDGControlVar.mDuckDuckGoContainer.webviewShowing = false;
		
		clearLeftSelect();

		if(recentSearchFragment==null) {
			recentSearchFragment = new RecentSearchFragment();
		}

        if(recentsFragment==null) {
            recentsFragment = new RecentsFragment();
        }

		if(!recentSearchFragment.isVisible()) {
			//changeFragment(recentSearchFragment, RecentSearchFragment.TAG);
		}

        if(!recentsFragment.isVisible()) {
            changeFragment(recentsFragment, RecentsFragment.TAG);
        }
    	//aaa
    	//if(DDGControlVar.START_SCREEN == SCREEN.SCR_RECENT_SEARCH){
        if(DDGControlVar.START_SCREEN == SCREEN.SCR_RECENTS){
    		DDGControlVar.homeScreenShowing = true;
    		//setMainButtonMenu();//aaa
			//BusProvider.getInstance().post(new LeftMenuSetHomeSelectedEvent(true));//aaa
    	}
    	else {
    		DDGControlVar.homeScreenShowing = false;
            //setHomeButton();//aaa
    	}
	}

    public void displaySearch() {
        resetScreenState();
        //DDGControlVar.mDuckDuckGoContainer.webviewShowing = false;

        changeFragment(searchFragment, SearchFragment.TAG);
    }

    public void displayAbout() {
        resetScreenState();//aaa to check
        DDGControlVar.mDuckDuckGoContainer.webviewShowing = false;

        actionBar.setDisplayShowCustomEnabled(false);
        actionBar.setDisplayHomeAsUpEnabled(true);
        //actionBar.setHomeAsUpIndicator(R.drawable.ic_home);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(R.string.about);

        changeFragment(new AboutFragment(), AboutFragment.TAG);
    }

    public void displayHelp() {
        resetScreenState();//aaa to check
        DDGControlVar.mDuckDuckGoContainer.webviewShowing = false;

        //actionBar.setDisplayShowCustomEnabled(false);
        //actionBar.setDisplayHomeAsUpEnabled(true);
        //actionBar.setHomeAsUpIndicator(R.drawable.ic_home);
        //actionBar.setDisplayShowTitleEnabled(true);
        //actionBar.setTitle(R.string.help_feedback);

        changeFragment(new HelpFeedbackFragment(), HelpFeedbackFragment.TAG);
    }
/*
    public void displaySettings() {
        resetScreenState();
        DDGControlVar.mDuckDuckGoContainer.webviewShowing = false;

        actionBar.setDisplayShowCustomEnabled(false);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_home);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(R.string.settings);

        //changeFragment(new PreferencesFragment(), "preferences");

    }
*/

	/*
	public void displayDuckMode(){  
		resetScreenState(); 
		
		// left side menu visibility changes
		BusProvider.getInstance().post(new LeftMenuChangeVisibilityEvent());
		
    	// main view visibility changes
		//shareButton.setVisibility(View.GONE);
    	DDGControlVar.mDuckDuckGoContainer.webviewShowing = false;
		
		clearLeftSelect();

		if(duckModeFragment==null) {
			duckModeFragment = new DuckModeFragment();
		}

		if(!duckModeFragment.isVisible()) {
			changeFragment(duckModeFragment, DuckModeFragment.TAG);
		}
    	    	/*
    	if(DDGControlVar.START_SCREEN == SCREEN.SCR_DUCKMODE){
    		DDGControlVar.homeScreenShowing = true;
    		setMainButtonMenu();
			BusProvider.getInstance().post(new LeftMenuSetHomeSelectedEvent(true));
            hideSearchBarBackground();
    	}
    	else {
    		DDGControlVar.homeScreenShowing = false;
    	}*//*
	}*/

    private void hideSearchBarBackground() {
        TypedArray styledAttributes = getTheme().obtainStyledAttributes(R.style.DDGTheme, new int[]{R.attr.searchBarBackground});
        searchBar.setBackgroundResource(styledAttributes.getResourceId(0,0));
        dropShadowDivider.setVisibility(View.GONE);
    }

    public void displayWebView() {
		// loading something in the browser - set home icon
		DDGControlVar.homeScreenShowing = false;
		setMainButtonHome();
        resetSearchBar();
		if (!DDGControlVar.mDuckDuckGoContainer.webviewShowing) {
			//shareButton.setVisibility(View.VISIBLE);

			webFragment = new WebFragment();

			if(!webFragment.isVisible()) {
				changeFragment(webFragment, WebFragment.TAG);

				DDGControlVar.mDuckDuckGoContainer.prevScreen = DDGControlVar.mDuckDuckGoContainer.currentScreen;
				DDGControlVar.mDuckDuckGoContainer.currentScreen = SCREEN.SCR_WEBVIEW;
			}

			DDGControlVar.mDuckDuckGoContainer.webviewShowing = true;
		}
	}

	private void changeFragment(Fragment newFragment, String newTag) {
        //Log.e("aaa", "change fragment");
		FragmentTransaction transaction = fragmentManager.beginTransaction();
		Fragment currentFragment = fragmentManager.findFragmentByTag(DDGControlVar.mDuckDuckGoContainer.currentFragmentTag);

		if(currentFragment==null) {
			transaction.replace(fragmentContainer.getId(), newFragment, newTag);
		} else if(currentFragment==feedFragment) {
			transaction.hide(currentFragment);
			transaction.add(fragmentContainer.getId(), newFragment, newTag);
		} else if(newFragment==feedFragment) {
			if(feedFragment.isAdded() ||
					(fragmentManager.findFragmentByTag(FeedFragment.TAG)!=null
							&& fragmentManager.findFragmentByTag(FeedFragment.TAG).isAdded())) {//reference is when actvity get destroyed and recreated
				transaction.remove(currentFragment);
				transaction.show(newFragment);
			} else {
				transaction.remove(currentFragment);
				transaction.add(fragmentContainer.getId(), newFragment, newTag);
			}
		} else {
			transaction.remove(currentFragment);
			transaction.add(fragmentContainer.getId(), newFragment, newTag);
		}

		transaction.commit();
		fragmentManager.executePendingTransactions();
		DDGControlVar.mDuckDuckGoContainer.currentFragmentTag = newTag;
        updateActionBar(newTag);

	}

    public void onClick(View view) {
		if (view.equals(mainButton)) {
			handleHomeSettingsButtonClick();
		}/*
		else if (view.equals(shareButton)) {
			BusProvider.getInstance().post(new HandleShareButtonClickEvent());
		}*/
	}

	private void handleLeftHomeTextViewClick() {
		drawer.close();
					
		if (DDGControlVar.mDuckDuckGoContainer.webviewShowing) {

			//We are going home!
			//mainWebView.clearHistory();
			//mainWebView.clearView();
			clearSearchBar();
			DDGControlVar.mDuckDuckGoContainer.webviewShowing = false;
		}
		
		displayHomeScreen();
	}

	private void handleHomeSettingsButtonClick() {
        keyboardService.hideKeyboard(getSearchField());
		
		if(DDGControlVar.homeScreenShowing){
			drawer.open();
		}
		else {
			// going home
			displayHomeScreen();
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (requestCode == PREFERENCES_RESULT){
			if (resultCode == RESULT_OK) {
                boolean clearWebCache = data.getBooleanExtra("mustClearWebCache", false);
                if(clearWebCache){
					BusProvider.getInstance().post(new WebViewClearCacheEvent());
                }
				boolean clearedHistory = data.getBooleanExtra("hasClearedHistory",false);
				if(clearedHistory){
					clearRecentSearch();
				}
                boolean startOrbotCheck = data.getBooleanExtra("startOrbotCheck",false);
                if(startOrbotCheck){
                    searchOrGoToUrl(getString(R.string.OrbotCheckSite));
                }
                boolean switchTheme = data.getBooleanExtra("switchTheme", false);
                if(switchTheme){
                    Intent intent = new Intent(getApplicationContext(), DuckDuckGo.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }
                
                if(PreferencesManager.isFontSliderVisible()) {
    				fontSizeLayout.setVisibility(View.VISIBLE);
    			}
                
                if(DDGControlVar.homeScreenShowing) {
                	displayHomeScreen();
                }
			}
		}
	}
	
	@Override
	public Object onRetainCustomNonConfigurationInstance() {
	       // return page container, holding all non-view data
	       return DDGControlVar.mDuckDuckGoContainer;
	}
    
	@Override
	protected void onSaveInstanceState(Bundle outState)	{
		AppStateManager.saveAppState(outState, DDGControlVar.mDuckDuckGoContainer, DDGControlVar.currentFeedObject);
		super.onSaveInstanceState(outState);
	}
	
	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState){
		super.onRestoreInstanceState(savedInstanceState);
		
		AppStateManager.recoverAppState(savedInstanceState, DDGControlVar.mDuckDuckGoContainer, DDGControlVar.currentFeedObject);
		String feedId = AppStateManager.getCurrentFeedObjectId(savedInstanceState);
		
		clearLeftSelect();

		BusProvider.getInstance().post(new LeftMenuMarkSelectedEvent(DDGControlVar.mDuckDuckGoContainer.currentScreen));
		
		if(feedId != null && feedId.length() != 0) {
			FeedObject feedObject = DDGApplication.getDB().selectFeedById(feedId);
			if(feedObject != null) {
				DDGControlVar.currentFeedObject = feedObject;
			}
		}			

		if(DDGControlVar.mDuckDuckGoContainer.webviewShowing) {
			return;
		}
		
		displayScreen(DDGControlVar.mDuckDuckGoContainer.currentScreen, true);
	}


	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		DDGUtils.displayStats.refreshStats(this);
		
		if(welcomeScreenLayout != null) {
			removeWelcomeScreen();
			addWelcomeScreen();
		}
		super.onConfigurationChanged(newConfig);
	}
	
	public boolean onKeyDown(int keyCode, KeyEvent event) {
	    if ( keyCode == KeyEvent.KEYCODE_MENU ) {
			drawer.close();
	        return true;
	    }
	    return super.onKeyDown(keyCode, event);
	}

	private void initFragments() {
		duckModeFragment = new DuckModeFragment();
		webFragment = new WebFragment();
		recentSearchFragment = new RecentSearchFragment();
		savedFragment = new SavedFragment();
        //savedFragment = StoredItemsFragment.newSavedInstance();
        recentsFragment = new RecentsFragment();
        searchFragment = new SearchFragment();
        Fragment fragment = null;
        fragment = fragmentManager.findFragmentByTag(FeedFragment.TAG);
        if(fragment==null) {
            feedFragment = new FeedFragment();
        } else {
            feedFragment = (FeedFragment) fragment;
        }

	}

	public DDGAutoCompleteTextView getSearchField() {
		return searchField;
	}

    private void checkForCrashes() {
        if(DDGApplication.isIsReleaseBuild())
            return;
        //CrashManager.register(this, DDGConstants.HOCKEY_APP_ID);
    }

    private void checkForUpdates() {
        if(DDGApplication.isIsReleaseBuild())
            return;
        //UpdateManager.register(this, DDGConstants.HOCKEY_APP_ID);
    }

	@Subscribe
	public void onDeleteStoryInHistoryEvent(DeleteStoryInHistoryEvent event) {//left menu
		final long delResult = DDGApplication.getDB().deleteHistoryByFeedId(event.feedObjectId);
		if(delResult != 0) {							
			syncAdapters();
		}
		Toast.makeText(this, R.string.ToastDeleteStoryInHistory, Toast.LENGTH_SHORT).show();
	}
	
	@Subscribe
	public void onDeleteUrlInHistoryEvent(DeleteUrlInHistoryEvent event) {//left menu
		final long delHistory = DDGApplication.getDB().deleteHistoryByDataUrl(event.pageData, event.pageUrl);				
		if(delHistory != 0) {							
			syncAdapters();
		}	
		Toast.makeText(this, R.string.ToastDeleteUrlInHistory, Toast.LENGTH_SHORT).show();
	}
	
	@Subscribe
	public void onReloadEvent(ReloadEvent event) {
		reloadAction();
	}
	
	@Subscribe
	public void onSaveSearchEvent(SaveSearchEvent event) {
		itemSaveSearch(event.pageData);
		syncAdapters();
		Toast.makeText(this, R.string.ToastSaveSearch, Toast.LENGTH_SHORT).show();
	}
	
	@Subscribe
	public void onSaveStoryEvent(SaveStoryEvent event) {
		itemSaveFeed(event.feedObject, null);
		syncAdapters();
		Toast.makeText(this, R.string.ToastSaveStory, Toast.LENGTH_SHORT).show();
	}
	
	@Subscribe
	public void onSendToExternalBrowserEvent(SendToExternalBrowserEvent event) {
		Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(event.url));
		DDGUtils.execIntentIfSafe(this, browserIntent);
	}

	@Subscribe
	public void onShareFeedEvent(ShareFeedEvent event) {
		Sharer.shareStory(this, event.title, event.url);
	}
	
	@Subscribe
	public void onShareSearchEvent(ShareSearchEvent event) {
		Sharer.shareSearch(this, event.query);
	}

	@Subscribe
	public void onShareWebPageEvent(ShareWebPageEvent event) {//web fragment
		Sharer.shareWebPage(this, event.url, event.url);
	}
	
	@Subscribe
	public void onUnSaveSearchEvent(UnSaveSearchEvent event) {
		final long delHistory = DDGApplication.getDB().deleteSavedSearch(event.query);
		if(delHistory != 0) {							
			syncAdapters();
		}	
		Toast.makeText(this, R.string.ToastUnSaveSearch, Toast.LENGTH_SHORT).show();
	}
	
	@Subscribe
	public void onUnSaveStoryEvent(UnSaveStoryEvent event) {
		final long delResult = DDGApplication.getDB().makeItemHidden(event.feedObjectId);
		if(delResult != 0) {							
			syncAdapters();
		}
		Toast.makeText(this, R.string.ToastUnSaveStory, Toast.LENGTH_SHORT).show();
	}

	/**
	 * Handling both MainFeedItemSelectedEvent and SavedFeedItemSelectedEvent.
	 * (modify to handle independently when necessary)
	 * @param event
	 *//* feed fragment
	@Subscribe
	public void onFeedItemSelected(FeedItemSelectedEvent event) {//
		// close left nav if it's open
		/*if(drawer.isDrawerOpen(leftMenuView)) {
			drawer.closeDrawer(leftMenuView);
		}*//*
		drawer.close();
		displayWebView();
		//feedFragment.feedItemSelected(event.feedObject);
	}*/

	@Subscribe
	public void onMainFeedItemLongClick(MainFeedItemLongClickEvent event) {
		new MainFeedMenuDialog(this, event.feedObject).show();
	}
	
	@Subscribe
	public void onSavedFeedItemLongClick(SavedFeedItemLongClickEvent event) {
        new SavedStoryMenuDialog(this, event.feedObject).show();
    }
	
	@Subscribe
	public void onHistoryItemSelected(HistoryItemSelectedEvent event) {
		drawer.close();
        keyboardService.hideKeyboard(getSearchField());
		if(!webFragment.isVisible()) {
			//displayWebView();//aaa
            displayScreen(SCREEN.SCR_WEBVIEW, false);
			//displayScreen(SCREEN.SCR_WEBVIEW, false);
		}
		BusProvider.getInstance().post(new WebViewShowHistoryObjectEvent(event.historyObject));
	}
	
	@Subscribe
	public void onHistoryItemLongClick(HistoryItemLongClickEvent event) {//to both recent search fragment AND left recent
        if(event.historyObject.isFeedObject()) {
            new HistoryStoryMenuDialog(this, event.historyObject).show();
        }
        else{
            new HistorySearchMenuDialog(this, event.historyObject).show();
        }
	}

    @Subscribe
    public void onSavedSearchItemSelected(SavedSearchItemSelectedEvent event) {
        searchOrGoToUrl(event.query);
        syncAdapters();
    }

	@Subscribe
	public void onSavedSearchItemLongClick(SavedSearchItemLongClickEvent event) {
		new SavedSearchMenuDialog(this, event.query).show();
	}

	@Subscribe
	public void onRecentSearchPaste(RecentSearchPasteEvent event) {
		drawer.close();
        getSearchField().pasteQuery(event.query);
        keyboardService.showKeyboard(getSearchField());
	}

    @Subscribe
	public void onSuggestionPaste(SuggestionPasteEvent event) {
		drawer.close();
        getSearchField().pasteQuery(event.query);
	}
	
	@Subscribe
	public void onSavedSearchPaste(SavedSearchPasteEvent event) {
		drawer.close();
        getSearchField().pasteQuery(event.query);
        keyboardService.showKeyboard(getSearchField());
	}

	@Subscribe
	public void onDisplayScreenEvent(DisplayScreenEvent event) {
		displayScreen(event.screenToDisplay, event.clean);
	}

	@Subscribe
	public void onSearchBarClearEvent(SearchBarClearEvent event) {
		clearSearchBar();
	}

	@Subscribe
	public void onSearchBarSetTextEvent(SearchBarSetTextEvent event) {
		setSearchBarText(event.text);
	}

	@Subscribe
	public void onSearchBarAddClearTextDrawable(SearchBarAddClearTextDrawable event) {
		//getSearchField().setBackgroundDrawable(DDGControlVar.mDuckDuckGoContainer.searchFieldDrawable);//aaa
	}

	@Subscribe
	public void onSearchBarSetProgressEvent(SearchBarSetProgressEvent event) {
		DDGControlVar.mDuckDuckGoContainer.progressDrawable.setLevel(event.newProgress);
		getSearchField().setBackgroundDrawable(DDGControlVar.mDuckDuckGoContainer.progressDrawable);
	}

	@Subscribe
	public void onRequestOpenWebPageEvent(RequestOpenWebPageEvent event) {
		searchOrGoToUrl(event.url, event.sessionType);
	}

	@Subscribe
	public void onLeftMenuHomeClickEvent(LeftMenuHomeClickEvent event) {
		handleLeftHomeTextViewClick();
	}

	@Subscribe
	public void onLeftMenuStoriesClickEvent(LeftMenuStoriesClickEvent event) {
		drawer.close();
		displayScreen(SCREEN.SCR_STORIES, false);
	}

	@Subscribe
	public void onLeftMenuSavedClickEvent(LeftMenuSavedClickEvent event) {
		drawer.close();
		//displayScreen(SCREEN.SCR_SAVED_FEED, false);//aaaa
        displayScreen(SCREEN.SCR_SAVED, false);
	}

	@Subscribe
	public void onLeftMenuSettingsClickEvent(LeftMenuSettingsClickEvent event) {
		drawer.close();
		displaySettings();
	}

	@Subscribe
	public void onLeftMenuCloseEvent(LeftMenuCloseEvent event) {
		drawer.close();
	}

	@Subscribe
	public void onDismissBangPopupEvent(DismissBangPopupEvent event) {
//		if(bangButtonExplanationPopup!=null){
//			bangButtonExplanationPopup.dismiss();
//		}
	}

	@Subscribe
	public void onSetMainButtonHomeEvent(SetMainButtonHomeEvent event) {
		setMainButtonHome();
	}

	@Subscribe
	public void onSetMainButtonMenuEvent(SetMainButtonMenuEvent event) {
		setMainButtonMenu();
	}

	@Subscribe
	public void onStopActionEvent(StopActionEvent event) {
		stopAction();
	}

	@Subscribe
	public void onRequestSyncAdaptersEvent(RequestSyncAdaptersEvent event) {
		syncAdapters();
	}

    @Subscribe
    public void onSearchBarChangeEvent(SearchBarChangeEvent event) {
        //changeSearchBar(event.screen);
    }
}
