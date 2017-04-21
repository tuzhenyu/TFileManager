package file.tzy.com.tfilemanager.manager;

import android.os.Environment;
import android.text.TextUtils;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * 该类维护了当前的访问路径，可通过该类获取当前访问的目录和整个访问的路径
 */
public class FileManager {
	//private static final int BUFFER = 		2048;
	//private static final int SORT_NONE = 	0;
	private static final int SORT_ALPHA = 	1;
	//private static final int SORT_TYPE = 	2;
	//private static final int SORT_SIZE = 	3;

	//private int mSortType = SORT_ALPHA;
	//private long mDirSize = 0;
	private Stack<FilePresenter.FileInfo> mPathStack;
	private ArrayList<File> mDirContent;
	//private FileFilter fileFilter;
	/**
	 * Constructs an object of the class
	 * <br>
	 * this class uses a stack to handle the navigation of directories.
	 */
	 FileManager() {
		mDirContent = new ArrayList<>();
		mPathStack = new Stack<>();

		File sdDir = Environment.getExternalStorageDirectory();//获取SD卡根目录
		homeDir = sdDir.toString();
		mPathStack.push(new FilePresenter.FileInfo(homeDir,false));

		//this.fileFilter = fileFilter;
	}

	 boolean isInSdCardRootDir(){
		return isSdCardRootDir(getCurrentDir());
	}

	 static boolean isSdCardRootDir(String dir){
		if(TextUtils.isEmpty(dir)){return false;}
		File sdDir = Environment.getExternalStorageDirectory();//获取跟目录
		return dir.equals(sdDir.toString());
	}
	
	/**
	 * This will return a string of the current directory path
	 * @return the current directory
	 */
	 String getCurrentDir() {
		if(mPathStack == null || mPathStack.isEmpty()){
			return "";
		}
		return mPathStack.peek().getPath();
	}
	
	/**
	 * This will return a string of the current home path.
	 * @return	the home directory
	 *//*
	public List<File> setHomeDir() {
		//This will eventually be placed as a settings item
		mPathStack.clear();
		mPathStack.push(getHomeDir());
		return populate_list();
	}
*/

	 Stack<FilePresenter.FileInfo> getPathStack() {
		return mPathStack;
	}

	 void setPathStack(Stack<FilePresenter.FileInfo> mPathStack) {
		this.mPathStack = mPathStack;
	}

	/**
	 * 
	 * @param type
	 */
	/*public void setSortType(int type) {
		mSortType = type;
	}*/
	
	/**
	 * This will return a string that represents the path of the previous path
	 * @return	returns the previous path
	 */
	 List<File> getPreviousDir() {
		int size = mPathStack.size();
		
		if (size >= 1)
			mPathStack.pop();
		
		else if(size == 0)
			mPathStack.push(new FilePresenter.FileInfo(getHomeDir(),false));
		
		return populate_list();
	}

	private String homeDir = null;
	 String getHomeDir(){
		if(homeDir != null){return homeDir;}
		boolean sdCardExist = Environment.getExternalStorageState()
				.equals(android.os.Environment.MEDIA_MOUNTED);//判断sd卡是否存在
		if(sdCardExist) {
			File sdDir = Environment.getExternalStorageDirectory();//获取跟目录
			return homeDir = sdDir.toString();
		}else{
			return homeDir = "";
		}
	}
	

	 List<File> getNextDir(String path, boolean isFullPath) {
		int size = mPathStack.size();
		
		if(!path.equals(mPathStack.peek()) && !isFullPath) {
			if(size == 1)
				mPathStack.push(new FilePresenter.FileInfo("/" + path,false));
			else
				mPathStack.push(new FilePresenter.FileInfo(mPathStack.peek() + "/" + path,false));
		}
		
		else if(!path.equals(mPathStack.peek()) && isFullPath) {
			mPathStack.push(new FilePresenter.FileInfo(path,false));
		}
		
		return populate_list();
	}


	public boolean isDirectory(String name) {
		return new File(mPathStack.peek() + "/" + name).isDirectory();
	}


	public List<String> searchInDirectory(String dir, String pathName) {
		List<String> names = new ArrayList<String>();
		search_file(dir, pathName, names);

		return names;
	}

	 List<File> refreshCurrentDir(){
		 return populate_list();
	}


/*	private static final Comparator alph = new Comparator<String>() {
		@Override
		public int compare(String arg0, String arg1) {
			return arg0.toLowerCase().compareTo(arg1.toLowerCase());
		}
	};*/
	
	/*private final Comparator size = new Comparator<String>() {
		@Override
		public int compare(String arg0, String arg1) {
			String dir = mPathStack.peek().getPath();
			Long first = new File(dir + "/" + arg0).length();
			Long second = new File(dir + "/" + arg1).length();
			
			return first.compareTo(second);
		}
	};*/
	
	/*private final Comparator type = new Comparator<String>() {
		@Override
		public int compare(String arg0, String arg1) {
			String ext = null;
			String ext2 = null;
			int ret;
			
			try {
				ext = arg0.substring(arg0.lastIndexOf(".") + 1, arg0.length()).toLowerCase();
				ext2 = arg1.substring(arg1.lastIndexOf(".") + 1, arg1.length()).toLowerCase();
				
			} catch (IndexOutOfBoundsException e) {
				return 0;
			}
			ret = ext.compareTo(ext2);
			
			if (ret == 0)
					return arg0.toLowerCase().compareTo(arg1.toLowerCase());
			
			return ret;
		}
	};*/

	/**
	 * 获取文件列表,滤掉文件夹和隐藏文件
	 * */
	  static File[] getFileList(File file){
		return file.listFiles(new FileFilter() {
			@Override
			public boolean accept(File pathname) {
				return  pathname.isFile() &&  !pathname.isHidden() ;
			}
		});
	}

	/**
	 * 获取当前路径下的文件列表,滤掉文件夹和隐藏文件
	 * */
	public File[] getCurrentFileList(File file){
		return getFileList(file);
	}

	/* (non-Javadoc)
         * this function will take the string from the top of the directory stack
         * and list all files/folders that are in it and return that list so
         * it can be displayed. Since this function is called every time we need
         * to update the the list of files to be shown to the user, this is where
         * we do our sorting (by type, alphabetical, etc).
         *
         * @return
         */
	private List<File> populate_list() {
		
		if(!mDirContent.isEmpty())
			mDirContent.clear();

		String path = "";
		if(mPathStack != null && !mPathStack.isEmpty()){
			path = mPathStack.peek().getPath();
		}
		File file = new File(path);
		
		if(file.exists() && file.canRead()) {
			/* add files/folder to List depending on hidden status */
			File[] list = file.listFiles();
			for(File fileItem : list){
				mDirContent.add(fileItem);
			}
		}
		
		return mDirContent;
	}

	 FilePresenter.FileInfo getCurrentFileInfo(){
		if(mPathStack == null || mPathStack.isEmpty()){return  null;}
		return mPathStack.peek();
	}
	
	/*
	 * (non-JavaDoc)
	 * I dont like this method, it needs to be rewritten. Its hacky in that
	 * if you are searching in the root dir (/) then it is not going to be treated
	 * as a recursive method so the user dosen't have to sit forever and wait.
	 * 
	 * I will rewrite this ugly method.
	 * 
	 * @param dir		directory to search in
	 * @param fileName	filename that is being searched for
	 * @param n			List to populate results
	 */
	private void search_file(String dir, String fileName, List<String> n) {
		File root_dir = new File(dir);
		String[] list = root_dir.list();
		
		if(list != null && root_dir.canRead()) {

			for (String path : list) {
				File check = new File(dir + "/" + path);
				String name = check.getName();
					
				if(check.isFile() && name.toLowerCase().
										contains(fileName.toLowerCase())) {
					n.add(check.getPath());
				}
				else if(check.isDirectory()) {
					if(name.toLowerCase().contains(fileName.toLowerCase()))
						n.add(check.getPath());
					
					else if(check.canRead() && !dir.equals("/"))
						search_file(check.getAbsolutePath(), fileName, n);
				}
			}
		}
	}
	
	
}
