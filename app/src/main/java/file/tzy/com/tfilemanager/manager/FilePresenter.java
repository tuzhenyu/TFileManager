package file.tzy.com.tfilemanager.manager;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.AdapterView;


import java.io.File;
import java.io.Serializable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Stack;
import java.util.TreeSet;

/**
 * 文件访问的控制器，该类主要维护了访问路径的切换逻辑(前进和后退)和文件选择逻辑，
 * 并在发生上述事件（比如：路径切换，文件被选中等）时，通知界面做更新。
 */
public class FilePresenter implements AdapterView.OnItemClickListener,Serializable {

	private final Context mContext;
	private final FileManager fileManager;

	//the list used to feed info into the array adapter and when multi-select is on
	private List<File> mDataSource;
	private Set<File> mMultiSelectData;
	//private String TAG = "FilePresenter";

	@IntDef ({SELECT_MODE,DEFALUT_MODE})
	@Retention(RetentionPolicy.SOURCE)
	 @interface Mode {}

	 static final int DEFALUT_MODE = 0x00000000;
	 static final int SELECT_MODE = 0x00000004;

	private int mCurMode = DEFALUT_MODE;

	private static final int SELECTED_FILES_LIMIT_COUNT = 20;

	private int mLimitCountOfSelectedFiles = SELECTED_FILES_LIMIT_COUNT;

	public int getLimitCountOfSelectedFiles() {
		return mLimitCountOfSelectedFiles;
	}

	 interface HandlerOffer{
		FilePresenter getHandler();
	}


	 interface ModeChangedLisenter{
		 void onModeChange(@Mode int mode);
	}

	 interface ChangeDirLisenter{
		/**
		 * 前进
		 * @param path 进入的路径
		 * */
		 void onForward(FileInfo path);
		/**
		*后退
		 * @param path 后退到的路劲
		* */
		 void onBackword(FileInfo path);
		/**
		 * 刷新
		 * */
		 void onRefresh(FileInfo path);

		 void onPathUnReachAbleError(String path);
	}

	 interface FileSecletedLisenter {
		 void onFileSelected(File file, boolean isCkecked);

		 void onSelectedPatchFiles(Collection<File> files, boolean isChecked);
	     /**
	      * 选择文件数量超过上限
		  * @param limit 文件选择数的限制值
		  * */
		 void onCountLimitError(int limit);

		 void onSelectedAll(boolean isSelectedAll);

	}

	 interface FileChecker{
		 void onCheckResult(boolean isSuccess, String msg);
	}

	private FileChecker mFileChecker = null;

	private ChangeDirLisenter mChangeDirLisenter = null;

	private FileSecletedLisenter mFileSelectLisenter = null;

	private ModeChangedLisenter modeChangedLisenter = null;

	public ModeChangedLisenter getModeChangedLisenter() {
		return modeChangedLisenter;
	}

	public FileChecker getmFileChecker() {
		return mFileChecker;
	}

	public void setFileChecker(@NonNull FileChecker mFileChecker) {
		this.mFileChecker = mFileChecker;
	}

	public void setModeChangedLisenter(ModeChangedLisenter modeChangedLisenter) {
		this.modeChangedLisenter = modeChangedLisenter;
	}

	public ChangeDirLisenter getmChangeDirLisenter() {
		return mChangeDirLisenter;
	}

	public void setChangeDirLisenter(@NonNull ChangeDirLisenter mChangeDirLisenter) {
		this.mChangeDirLisenter = mChangeDirLisenter;
	}

	public FileSecletedLisenter getFileCheckLisenter() {
		return mFileSelectLisenter;
	}

	public void setFileSelectedLisenter(@NonNull FileSecletedLisenter fileSelectLisenter) {
		mFileSelectLisenter = fileSelectLisenter;
	}

	/**
	 * Creates an Handler object. This object is used to communicate
	 * most work from the Main activity to the FileManager class.
	 * 
	 * @param context	The context of the main activity e.g  Main
	 * @param manager	The FileManager object that was instantiated from Main
	 */
	 FilePresenter(Context context, final FileManager manager, @Mode int mode, int limitCountOfSelectedFiles) {
		mContext = context;
		fileManager = manager;
		mCurMode = mode;
		mDataSource = new ArrayList<>();
		if(mLimitCountOfSelectedFiles > 0){
			mLimitCountOfSelectedFiles = limitCountOfSelectedFiles;
		}
	}

	/**
	 * 最后提交时，检查和过滤所有文件
	 * */
	/*public void checkFiles(){

		if(getCountofSelcetedFile() <= 0){return;}
		Set<File> garbage = new HashSet<>();
		Set<File> files = getSelectedFiles();

		for(File file : files){
			if( !file.exists() || !file.canRead() ){
				garbage.add(file);
			}
		}

		files.removeAll(garbage);

		if(files.isEmpty()){
			mMultiSelectData = files;
			if(mFileChecker != null){
				mFileChecker.onCheckResult(false,mContext.getString(R.string.file_no_exist_wranning));
			}
			return;
		}

		garbage.clear();

		for(File file : files){
			if( file.length() <= 0){
				garbage.add(file);
			}
		}

		files.removeAll(garbage);

		if(files.isEmpty()){
			mMultiSelectData = files;
			if(mFileChecker != null){
				mFileChecker.onCheckResult(false,mContext.getString(R.string.file_size_zero_warnning));
			}
			return;
		}

	    mFileChecker.onCheckResult(true,"");

	}*/

	public boolean changeLimitCountOfSelectedFile(int limit){
		if(limit <= 0){return false;}

		int count = getCountofSelcetedFile();
		if(limit < count){return false;}

		mLimitCountOfSelectedFiles = limit;

		if(mDataSource == null ){
			mDataSource = new ArrayList<>();
		}
		return true;
	}

	/**
	 * Indicates whether the user wants to select 
	 * multiple files or folders at a time.
	 * <br><br>
	 * false by default
	 * 
	 * @return	true if the user has turned on multi selection
	 */
	 boolean isMultiSelected() {
		return mCurMode == SELECT_MODE;
	}
	
	/**
	 * Use this method to determine if the user has selected multiple files/folders
	 * 
	 * @return	returns true if the user is holding multiple objects (multi-select)
	 */
	public boolean hasMultiSelectData() {
		return (mMultiSelectData != null && mMultiSelectData.size() > 0);
	}


	 Stack<FileInfo> getPathStack(){
		if(fileManager == null){
			return null;
		}

		return fileManager.getPathStack();
	}

	/**
	 * 恢复路径栈的信息
	 * */
	 void reStorePathStack(Stack<FileInfo> nStack){
		if(nStack == null){return;}
		fileManager.setPathStack(nStack);
	}

	/**
	 * 恢复被选择的文件的数据
	 * */
	 void reStoreSelectedFiles(Collection<File> files){
		if(mMultiSelectData == null){
			mMultiSelectData = new HashSet<>(files);
		} else{
			mMultiSelectData.clear();
			mMultiSelectData.addAll(files);
		}
	}


	public Context getContext() {
		return mContext;
	}

	 Set<File> getSelectedFiles(){
		return mMultiSelectData;
	}

	public String getCurrentDir(){
		return fileManager.getCurrentDir();
	}

	/**
	 * 获得已选择的文件的数量
	 * */
	 int getCountofSelcetedFile(){
		if(mMultiSelectData == null || mMultiSelectData.size() <= 0){
			return 0;
		}else{
			return mMultiSelectData.size();
		}

	}

	/**
	 * will return the data in the ArrayList that holds the dir contents. 
	 * 
	 * @param position	the indext of the arraylist holding the dir content
	 * @return the data in the arraylist at position (position)
	 */
	public File getData(int position) {
		if(mDataSource == null){return null;}
		if(position > mDataSource.size() - 1 || position < 0)
			return null;
		
		return mDataSource.get(position);
	}

	/**
	 * called to update the file contents as the user navigates there
	 * phones file system. 
	 * 
	 * @param content	an ArrayList of the file/folders in the current directory.
	 */
	 void updateDirectory(List<File> content) {
		mDataSource = new ArrayList<>(content.size());

		for(File data : content)
			mDataSource.add(data);

		//checkedAllFileSelected();
	}

	 void notifyItemSelectedLisenter(File file, boolean isSelected){
			mFileSelectLisenter.onFileSelected(file,isSelected);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		FileBrowserActivity.FileAdapter adapter = (FileBrowserActivity.FileAdapter) parent.getAdapter();
		File file = (File) adapter.getItem(position);
		if( !isPathReachable(file.getPath())){return;}//检查该路径是否可达

		if(file.isDirectory()){
			forword(file.toString());
			return;
		}

		if(file.isFile()){
			if(isMultiSelected()) {
				toggleMultiSelected(file);
		}
	}

	}



	/***
	 * 强制选择文件
	 * */
	public void chooseFileForced(File file){
		mMultiSelectData.add(file);
		notifyItemSelectedLisenter(file,true);
	}

	 void forword(String path){
		updateDirectory(fileManager.getNextDir(path, true));
		if(mChangeDirLisenter != null){
			mChangeDirLisenter.onForward(fileManager.getCurrentFileInfo());
		}

	}

	 void backword(){
		updateDirectory(fileManager.getPreviousDir());
		if(mChangeDirLisenter != null){
			mChangeDirLisenter.onBackword(fileManager.getCurrentFileInfo());
		}
	}

	 boolean isPathReachable(String path){
		File file = new File(path);
		if(!file.exists() || !file.canRead()){
			if(mChangeDirLisenter != null){
				mChangeDirLisenter.onPathUnReachAbleError(path);
			}
			return false;
		}
		return true;
	}

	/**
	 *刷新当前路径
	 *  */
	 void refreshCurrentDir(){
		isPathReachable(fileManager.getCurrentDir());
		updateDirectory(fileManager.refreshCurrentDir());
        //checkedAllFileSelected();
		if(mChangeDirLisenter != null){
			mChangeDirLisenter.onRefresh(fileManager.getCurrentFileInfo());
		}
	}

	private void toggleMultiSelected(File file) {
        if(file == null){return;}
		if(mMultiSelectData == null) {
			if(checkIsOutOfLimit(1)){return;}
			add_multiSelect_file(file);
		} else if(mMultiSelectData.contains(file)) {
			mMultiSelectData.remove(file);
				//FileInfo curFile = fileManager.getCurrentFileInfo();
				/*if(curFile.isAllFileSelect() == true){
					curFile.setAllFileSelect(false);
					mFileSelectLisenter.onSelectedAll(false);
				}*/
			notifyItemSelectedLisenter(file,false);
		} else {
			if(checkIsOutOfLimit(1)){return;}
			add_multiSelect_file(file);
		}
	}

	/**
	 * 当前路径下，全选或者不全选
	 * @param isSelected  是否全选
	 * */
	/*public void makeAllSelected(boolean isSelected){
		if(mCurMode == DEFALUT_MODE){return;}
		if(mDataSource == null || mDataSource.size() <= 0){return;}

		LinkedList<File> files = new LinkedList<>();
		for(File file : mDataSource){
			if(file.isFile()){files.add(file);}
		}

		if(isSelected){
			if(mMultiSelectData != null){
				files.removeAll(mMultiSelectData);//去重
			}else{
				mMultiSelectData = new TreeSet<>();
			}
			if(checkIsOutOfLimit(files.size())){return;}
			if(mMultiSelectData.addAll(files) ){
				*//*
					FileInfo curFile = fileManager.getCurrentFileInfo();
					curFile.setAllFileSelect(true);
					mFileSelectLisenter.onSelectedAll(true);
				*//*
				mFileSelectLisenter.onSelectedPatchFiles(files,true);
			}
		}else{
			List<File> removedFiles = new ArrayList();
			for(File file : files){
				if(mMultiSelectData.remove(file)){removedFiles.add(file);}
			}
			if(removedFiles.size() > 0){
					*//*
						FileInfo fileInfo  = fileManager.getCurrentFileInfo();
						fileInfo.setAllFileSelect(false);
						mFileSelectLisenter.onSelectedAll(false);
					*//*
					mFileSelectLisenter.onSelectedPatchFiles(removedFiles,false);
			}
		}
	}
*/
	/**
	 * 检查文件数量限制
	 * @param newCount 新增加的文件数
	 * @return 是否超过限制
	 * */
	 boolean checkIsOutOfLimit(int newCount){
	  boolean isOutOfLimit = (mLimitCountOfSelectedFiles < getCountofSelcetedFile() + newCount);
		if(isOutOfLimit){
				mFileSelectLisenter.onCountLimitError(mLimitCountOfSelectedFiles);
			return true;
		}
		return false;
	}

	private void  add_multiSelect_file(File file) {
		if(mMultiSelectData == null){
			mMultiSelectData = Collections.synchronizedSet(new TreeSet<File>());
		}
		mMultiSelectData.add(file);
		notifyItemSelectedLisenter(file,true);
		//checkedAllFileSelected();
	}

	 FileManager getFileManager() {
		return fileManager;
	}

	/*private void checkedAllFileSelected(){
		if(isAllFileSelected()){
				FileInfo fileInfo  = fileManager.getCurrentFileInfo();
				fileInfo.setAllFileSelect(true);
				mFileSelectLisenter.onSelectedAll(true);
		}
	}*/


	public boolean isAllFileSelected(){
		List<File> files = getListFiles();
		boolean isAllDirs = true;
		Set<File> fileSet  =getSelectedFiles();
		if(files == null || files.size() <= 0 || fileSet == null || fileSet.size() <= 0){
			return false;
		}
		for(File file: files){
			if(file.isFile()){
				isAllDirs = false;
				if( !fileSet.contains(file)){
					return false;
				}
			}
		}

		return !isAllDirs;
	}

	 void notifyModeChange(@Mode int mode){
		if(modeChangedLisenter != null){
			modeChangedLisenter.onModeChange(mode);
		}
	}

	/*
	 * This will turn off multi-select and hide the multi-select buttons at the
	 * bottom of the view.
	 *
	 * @param clearData if this is true any files/folders the user selected for multi-select
	 * 					will be cleared. If false, the data will be kept for later use. Note:
	 * 					multi-select copy and move will usually be the only one to pass false,
	 * 					so we can later paste it to another folder.
	 */
	/*public void killMultiSelect(boolean clearData) {

		mCurMode = DEFALUT_MODE;
		notifyModeChange(mCurMode);

		if(clearData)
			if(mMultiSelectData != null && !mMultiSelectData.isEmpty())
				mMultiSelectData.clear();
	}
*/
	 List<File> getListFiles(){
		return mDataSource;
	}

	/**
	 * 存储一个路径的信息
	 * 暂时只有两个参数
	 * */
	 static class FileInfo implements Parcelable {
		private String path = "";
		private boolean isAllFileSelect = false;

		 FileInfo(String path, boolean isAllFileSelect) {
			this.path = path;
			this.isAllFileSelect = isAllFileSelect;
		}

		 FileInfo(Parcel in) {
			path = in.readString();
			isAllFileSelect = in.readByte() != 0;
		}



		public static final Creator<FileInfo> CREATOR = new Creator<FileInfo>() {
			@Override
			public FileInfo createFromParcel(Parcel in) {
				return new FileInfo(in);
			}

			@Override
			public FileInfo[] newArray(int size) {
				return new FileInfo[size];
			}
		};

		 String getPath() {
			return path;
		}

		public boolean isAllFileSelect() {
			return isAllFileSelect;
		}

		protected void setPath(String path) {
			this.path = path;
		}

		protected void setAllFileSelect(boolean allFileSelect) {
			isAllFileSelect = allFileSelect;
		}

		@Override
		public int describeContents() {
			return 0;
		}

		@Override
		public void writeToParcel(Parcel dest, int flags) {
			dest.writeString(path);
			dest.writeByte((byte) (isAllFileSelect ? 1 : 0));
		}
	}




}
