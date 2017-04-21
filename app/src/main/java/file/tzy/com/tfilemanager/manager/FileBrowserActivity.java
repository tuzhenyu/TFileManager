package file.tzy.com.tfilemanager.manager;

import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.ListFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Stack;

import file.tzy.com.tfilemanager.R;
import file.tzy.com.tfilemanager.interfaces.FileCheckedView;
import file.tzy.com.tfilemanager.utils.CheckUtils;
import file.tzy.com.tfilemanager.utils.FileType;

/**
 * 类描述：
 * created by tzy on 2016-7-29
 */
public final class FileBrowserActivity extends AppCompatActivity implements FilePresenter.FileChecker,
        FilePresenter.FileSecletedLisenter,
        FilePresenter.ChangeDirLisenter,
        FilePresenter.HandlerOffer,
        FileCheckedView,
        View.OnClickListener {

    private static final int SELECTED_COUNT = 20;
    private FilePresenter mHandler;
   // public static final int FILE_SELECTED_REQUEST_CODE = 0X02;
   // public static final int RESULT_CODE = 0X002;

    // views...
    Toolbar toolbar;
    TextView txHint;
    /**
     * 文件处理接口，如果使用者传入接口的实例，
     * 则点击“完成”按钮后，不会返回所选文件列表，而是调用该接口的方法去处理文件；
     * 如果未传入该接口的实例，则会返回文件列表。
     */
    /*public interface FileChooseAction<Param, Result> {

        void preAction(Activity activity);

        Result onAction(Activity activity, List<File> files, Param... params);

        void postAction(Activity activity, Result t);

        Param[] getParams();

    }*/

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_choose);
        onPreBindViews();

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        txHint = (TextView) findViewById(R.id.tx_empty_hint);

        setSupportActionBar(toolbar);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        ArrayList<FilePresenter.FileInfo> pathList = new ArrayList<>(mHandler.getPathStack());
        outState.putParcelableArrayList("DIR_STACK", pathList);

        Set<File> selectedFiles = mHandler.getSelectedFiles();
        if (selectedFiles == null || selectedFiles.size() <= 0) return;
        ArrayList<String> selectedPathsStates = new ArrayList<>(mHandler.getCountofSelcetedFile());
        for (File file : selectedFiles) {
            selectedPathsStates.add(file.getPath());
        }
        outState.putStringArrayList("STATE_SELECTED_FILES", selectedPathsStates);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (savedInstanceState != null) {

            ArrayList<FilePresenter.FileInfo> fileinfos = savedInstanceState.getParcelableArrayList("DIR_STACK");
            if (fileinfos == null) {
                fileinfos = new ArrayList<>(0);
            }
            Stack<FilePresenter.FileInfo> pathStack = new Stack<>();
            pathStack.addAll(fileinfos);
            mHandler.reStorePathStack(pathStack);

            ArrayList<String> selectedFiles = savedInstanceState.getStringArrayList("STATE_SELECTED_FILES");
            if (selectedFiles == null) return;
            Set<File> fileSet = new HashSet<>(selectedFiles.size());
            for (String path : selectedFiles) {
                fileSet.add(new File(path));
            }
            mHandler.reStoreSelectedFiles(fileSet);

        }
    }


    protected void onPreBindViews() {
        getHandler();
    }


    @Override
    public FilePresenter getHandler() {
        if (mHandler == null) {

            FileManager mFileMag = new FileManager();

            //获得文件选择个数的配置信息
            int selectedCountLimit = SELECTED_COUNT;
            mHandler = new FilePresenter(FileBrowserActivity.this, mFileMag, FilePresenter.DEFALUT_MODE, selectedCountLimit);
            mHandler.setFileSelectedLisenter(this);
            mHandler.setChangeDirLisenter(this);
            mHandler.setFileChecker(this);
        }
        return mHandler;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            default:
        }
    }

   /* @Override
    protected void onRightClick() {
        super.onRightClick();
        mHandler.checkFiles();
    }*/

  /*  @Override
    protected void onLeftClick() {
        super.onLeftClick();
        AndroidUtils.exit(this);
    }*/

    @Override
    public boolean onKeyDown(int keycode, KeyEvent event) {

        if (keycode == KeyEvent.KEYCODE_BACK ) {
            boolean isInSdcardRoot = mHandler.getFileManager().isInSdCardRootDir();
            if(isInSdcardRoot){
                finish();
                return false;
            }else{
                mHandler.backword();//后退
                return true;
            }
        }
        return false;
    }

    @Override
    protected void onResume() {
        super.onResume();
        mHandler.refreshCurrentDir();
    }

   /* @Override
    protected String getEmptyHint() {
        return getString(R.string.local_file_dir_is_empty);
    }*/

    //********************Activity中会被控制器回调的方法，这些方法是数据与界面交互的接口**********************/
    @Override
    public void onFileSelected(File file, boolean isCkecked) {
      //  updateFilesCheckedStates(mHandler.getCountofSelcetedFile());
    }

    @Override
    public void onSelectedPatchFiles(Collection<File> files, boolean isChecked) {
     //   int count = mHandler.getCountofSelcetedFile();
      //  updateFilesCheckedStates(count);
    }

    @Override
    public void onCountLimitError(int limit) {
        Toast.makeText(this, getString(R.string.file_selected_count_limit_waring, limit), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onSelectedAll(boolean isSelectedAll) {

    }

    @Override
    public void onInvalidFileTypeSelectedError(File file) {

    }


    @Override
    public void onFileSizeLimitWarning(final File file, int SizeLimit) {

    }

    @Override
    public void onNoWifiWarning(final File file, int SizeLimitInWifi) {
     /*   dialog = new MoaAlertDialog.Builder(this)
                .setConfirmBtnText(getString(R.string.daialog_continue))
                .setCancelBtnText(getString(R.string.cancel))
                .setMessage(getString(R.string.file_size_out_of_limit_no_wifi,filterPresenter.getFileSizeLimitNoWifi()))
                .setCancelButtonListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                })
                .setConfirmClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        mHandler.chooseFileForced(file);
                    }
                }).create();
        dialog.show();*/
    }

    @Override
    public void onSelectNullFileError(File file) {
        Toast.makeText(this, getString(R.string.file_size_zero_warnning), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onFileNoExist(File file) {

    }

    @Override
    public String getFCViewTag() {
        return "FileBrowserActivity";
    }

    @Override
    public void onForward(FilePresenter.FileInfo fileInfo) {
        String path = fileInfo.getPath();
        checkoutDirEmpty(path);
        toolbar.setTitle(path.substring(path.lastIndexOf("/")+1,path.length()));
        forwordFragment(path);
    }

    @Override
    public void onBackword(FilePresenter.FileInfo fileInfo) {
        String path = fileInfo.getPath();
        changeTitle(path);
        backFragment();
        checkoutDirEmpty(path);
    }

    @Override
    public void onRefresh(FilePresenter.FileInfo fileInfo) {
        String path = fileInfo.getPath();
        changeTitle(path);
       // updateFilesCheckedStates(mHandler.getCountofSelcetedFile());
        checkoutDirEmpty(path);
        refreshFragment(path);
    }

    protected  void changeTitle(String path){
        if (FileManager.isSdCardRootDir(path)) {
            toolbar.setTitle(R.string.root_path);
        } else {
            toolbar.setTitle(path.substring(path.lastIndexOf("/")+1,path.length()));
        }
    }

    @Override
    public void onPathUnReachAbleError(String path) {
        if (!Environment.getExternalStorageState()
                .equals(android.os.Environment.MEDIA_MOUNTED)) {
            Toast.makeText(this, getString(R.string.sdcard_nomounted_warning), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, getString(R.string.path_unreachable), Toast.LENGTH_SHORT).show();
        }

    }

   // public static final String EXTRA_PARA_FILES = "PARA_FILES";

    /**
     * 检查完成会回调这个函数
     */
    @Override
    public void onCheckResult(boolean isSuccess, String msg) {
        /*if (isSuccess) {

            FileChooseAction action = getIntent().getParcelableExtra(FilePresenter.EXTRA_FILEACTION);
            if (action != null) {// 如果有Action，则不返回文件列表，，而是由Action处理
                FilesActionTask task = new FilesActionTask(this, action);
                List<File> files = new ArrayList<>(mHandler.getSelectedFiles());
                File[] fArrays = new File[files.size()];
                for(int i = 0;i < files.size(); i++){
                    fArrays[i] = files.get(i);
                }
                task.execute(fArrays);
                return;
            }

            Bundle bundle = new Bundle();
            Set<File> files = mHandler.getSelectedFiles();
            ArrayList<String> filePaths = new ArrayList<>(files.size());
            for (File file : files) {
                filePaths.add(file.toString());
            }
            bundle.putStringArrayList(EXTRA_PARA_FILES, filePaths);
            Intent intent = new Intent();
            intent.putExtras(bundle);
            setResult(Activity.RESULT_OK, intent);
            finish();
        } else {
            Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
            mHandler.refreshCurrentDir();//要上传的文件不存在，需要刷新界面
        }*/

    }

    /*******************************************
     * Activity调用的方法
     ********************************/

    //public static String PARA_DATA = "DATA";
    //全选和不全选的相关处理函数，暂时不需要
   /* public void makeRightBnState(boolean isSelectedAll){
        if(isSelectedAll){
            title.newChangeRight(0,R.string.file_un_select_all);
            rightClick = new Runnable() {
                @Override
                public void run() {
                    mHandler.makeAllSelected(false);
                }
            };
        }else{
            title.newChangeRight(0,R.string.file_select_all);
            rightClick = new Runnable() {
                @Override
                public void run() {
                    mHandler.makeAllSelected(true);
                }
            };
        }
    }*/

    public void refreshFragment(String path) {
        FileListFragment fr = (FileListFragment) getSupportFragmentManager().findFragmentByTag(path);
        if (fr == null) {
            fr = new FileListFragment();
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_frame, fr, path);
            ft.addToBackStack(path);
            ft.commit();
        } else {
            fr.refresh();
        }


    }

    public void forwordFragment(String path) {

        FileListFragment curFg = new FileListFragment();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.setCustomAnimations(
                R.anim.slide_right_in, R.anim.slide_left_out, R.anim.slide_left_in, R.anim.slide_right_out);
        ft.replace(R.id.content_frame, curFg, path);
        ft.addToBackStack(path);
        ft.commit();
    }

    public void backFragment() {
        this.getSupportFragmentManager().popBackStack();
    }

    public void checkoutDirEmpty(String path) {
        if (path == null) {
            showEmptyHint(true);
        }
        List<File> fileList = mHandler.getListFiles();
        if (fileList == null || fileList.size() <= 0) {
            showEmptyHint(true);
        } else {
            showEmptyHint(false);
        }
    }

/*    private void updateFilesCheckedStates(int countOfChecked) {

        if (countOfChecked <= 0) {
           *//* title.newDisableRight(0);
            tv_top_tip.setText(R.string.cloud_file_please_choose_file);
            tv_top_tip.setBackgroundColor(Color.parseColor("#322a33"));*//*
        } else {
          *//*  title.newEnableRight(0);
            tv_top_tip.setTextColor(getResources().getColor(R.color.white));
            tv_top_tip.setText(getString(R.string.cloud_local_file_had_select_file, countOfChecked));
            tv_top_tip.setBackgroundColor(Color.parseColor("#ef7510"));*//*

        }

        FileListFragment fr = (FileListFragment) getSupportFragmentManager().findFragmentByTag(mHandler.getCurrentDir());
        if (fr != null) {
            fr.refresh();
        }
    }*/


    protected void showEmptyHint(boolean show){
        if(show){
            txHint.setVisibility(View.VISIBLE);
        }else{
            txHint.setVisibility(View.GONE);

        }
    }
    //****************************************内部类**********************************

    /*public static class FilesActionTask extends AsyncTask<File, Integer, Object> {

        Activity activity;

        FileChooseAction action;

        private FilesActionTask(Activity activity, FileChooseAction action) {
            this.activity = activity;
            this.action = action;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (action == null) {
                return;
            }
            action.preAction(activity);
        }

        @Override
        protected Object doInBackground(File... params) {
            if (action == null) {
                return null;
            }
            return action.onAction(activity,Arrays.asList(params), action.getParams());
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            if (action == null) {
                return;
            }
            action.postAction(activity, o);
        }
    }
*/
    public static class FileListFragment extends ListFragment {

        FileAdapter fileAdapter;

        //BaseThumbLoader.Controller loadController ;

       /* public BaseThumbLoader getThumbLoader(Activity activity) {
            FilePresenter.HandlerOffer offer = (FilePresenter.HandlerOffer) activity;
            return offer.getThumbLoader();
        }*/

        public FileListFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            return inflater.inflate(R.layout.list_fragment_layout, container, false);
        }

        @Override
        public void onViewCreated(View view, Bundle savedInstanceState) {
            super.onViewCreated(view, savedInstanceState);

            FilePresenter.HandlerOffer activity = (FilePresenter.HandlerOffer) getActivity();
            FilePresenter mFilePresenter = activity.getHandler();

            if (fileAdapter == null) {
                fileAdapter = new FileAdapter(getContext(), mFilePresenter);
            }

            fileAdapter.setFileList(mFilePresenter);
            setListAdapter(fileAdapter);

        }

       /* public BaseThumbLoader.Controller getLoadController(){
            if(loadController == null){
                loadController = new BaseThumbLoader.Controller() {
                    @Override
                    public boolean onBackground() {
                        if(isRemoving() || isHidden() || isDetached() || !isVisible() ){
                            return true;
                        }
                        return false;
                    }
                };
            }
            return loadController;
        }*/

        @Override
        public void onDestroy() {
            super.onDestroy();
        }

        @Override
        public void onListItemClick(ListView l, View v, int position, long id) {
            super.onListItemClick(l, v, position, id);
            FilePresenter.HandlerOffer activity = (FilePresenter.HandlerOffer) getActivity();

            FilePresenter mFilePresenter = activity.getHandler();
            mFilePresenter.onItemClick(l, v, position, id);
        }


        public void refresh() {
            FileAdapter fileAdapter = (FileAdapter) getListAdapter();
            FilePresenter filePresenter = ((FilePresenter.HandlerOffer) getActivity()).getHandler();
            if (fileAdapter != null) {
                fileAdapter.setFileList(filePresenter);
            } else {
                fileAdapter = new FileAdapter(getActivity(), filePresenter);
                fileAdapter.setFileList(filePresenter);
                setListAdapter(fileAdapter);
            }
        }
    }

    public static class FileAdapter extends BaseAdapter {

        Context mContext;
        List<File> fileList;
        FilePresenter filePresenter;

        //  BaseThumbLoader.Controller mController;
        //  BaseThumbLoader mImgLoader;
        //  MOA_JNI moaJni = new MOA_JNI();
        //LinkedHashMap<String,BetterThumbInfo> thumbInfoCache;
        private FileAdapter(Context context, FilePresenter handler) {
            filePresenter = handler;
            this.mContext = context;
        }


        private void setFileList(FilePresenter filePresenter) {
            this.filePresenter = filePresenter;
            fileList = filePresenter.getListFiles();
            this.notifyDataSetChanged();
        }


      /*  public String getFilePermissions(File file) {
            String per = "-";

            if (file.isDirectory())
                per += "d";
            if (file.canRead())
                per += "r";
            if (file.canWrite())
                per += "w";

            return per;
        }*/

        @Override
        public int getCount() {
            if (!CheckUtils.checkListIfValid(fileList)) {
                return 0;
            }
            return fileList.size();
        }

        @Override
        public Object getItem(int position) {
            return fileList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final ViewHolder mViewHolder;

            if (convertView == null) {
                convertView = LayoutInflater.from(mContext).inflate(R.layout.item_file_layout, parent,false);
                mViewHolder = new ViewHolder();
                mViewHolder.checkChoose = (CheckBox) convertView.findViewById(R.id.check_choose);
                mViewHolder.fileTypeIv = (ImageView) convertView.findViewById(R.id.img_file_type);
                mViewHolder.fileNameTv = (TextView) convertView.findViewById(R.id.txt_file_name);
                mViewHolder.noMarginIv = (ImageView) convertView.findViewById(R.id.img_line_not_margin);
                mViewHolder.lineMargin = (ImageView) convertView.findViewById(R.id.img_line);
                convertView.setTag(mViewHolder);
            } else {
                mViewHolder = (ViewHolder) convertView.getTag();
            }

            mViewHolder.file = fileList.get(position);
            //ChcekBox显示逻辑
            //文件信息显示逻辑
            if (mViewHolder.file != null) {
                mViewHolder.fileNameTv.setText(mViewHolder.file.getName());

                if (mViewHolder.file.isFile()) {
                    mViewHolder.noMarginIv.setVisibility(View.GONE);
                    mViewHolder.lineMargin.setVisibility(View.VISIBLE);
                    if (filePresenter.isMultiSelected()) {
                        mViewHolder.checkChoose.setVisibility(View.VISIBLE);
                        if (filePresenter.getSelectedFiles() != null &&
                                filePresenter.getSelectedFiles().contains(mViewHolder.file)) {
                            mViewHolder.checkChoose.setChecked(true);
                        } else {
                            mViewHolder.checkChoose.setChecked(false);
                        }
                    } else {
                        mViewHolder.checkChoose.setVisibility(View.GONE);
                    }
                    //根据文件后缀决定显示的缩略图
                     FileType.FileFormat fileFormat = FileType.getFileFormat(mViewHolder.file.getName());
                    mViewHolder.fileTypeIv.setImageResource(FileType.getDrawableByFileType(fileFormat));

                } else if (mViewHolder.file.isDirectory()) {
                    mViewHolder.lineMargin.setVisibility(View.GONE);
                    mViewHolder.noMarginIv.setVisibility(View.VISIBLE);
                    mViewHolder.checkChoose.setVisibility(View.GONE);
                    mViewHolder.fileTypeIv.setImageResource(R.drawable.icon_dir);
                }
            }

            return convertView;
        }

        private static class ViewHolder {
            CheckBox checkChoose;//check_choose
            ImageView fileTypeIv;//img_file_type
            TextView fileNameTv;//txt_file_name
           // TextView permitTv;//txt_permit
            //ImageView arrowIv;//img_arrow
            ImageView lineMargin;
            ImageView noMarginIv;//img_line_not_margin
            File file;
        }


    }
}
