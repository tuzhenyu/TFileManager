package file.tzy.com.tfilemanager.utils;

import android.app.Activity;
import android.widget.ListView;

import java.util.List;

public class CheckUtils {

    public static boolean checkListIfValid(List<?> targetList) {

        if (targetList != null && targetList.size() > 0) {
            return true;
        } else {
            return false;
        }

    }

    public static boolean checkListIfValid(List<?> targetList, int pos ) {

        if (targetList != null && targetList.size() > 0 && pos >-1 && targetList.size()> pos) {
            return true;
        } else {
            return false;
        }

    }

    /**
     * 在List调用 get 方法前 建议加此判断
     * @param position
     * @param list
     * @return
     */
    public static boolean checkPositionForListIfValid(int position,List<?> list) {
        if(list == null) return false;
        if(position<0 || position>list.size()-1) {
            return false;
        } else return true;

    }

    public static boolean checkItemclickIfValid(int position,ListView mListView) {
        return position < mListView.getAdapter().getCount() + mListView.getHeaderViewsCount() && position >= mListView.getHeaderViewsCount();
    }

    public static boolean checkActivity(Activity activity) {
        if (activity != null && !activity.isFinishing()) {
            return true;
        }

        return false;
    }

    public static boolean checkTwoListEqual(List<?> thisList, List<?> otherList){
        if(thisList == null ){
            return otherList == null || otherList.isEmpty();
        }
        if(otherList == null){
            return thisList == null || thisList.isEmpty();
        }
        if(thisList.size() != otherList.size()){
            return false;
        }
        for(int i=0;i<thisList.size();i++){
            if(!thisList.get(i).equals(otherList.get(i))){
                return false;
            }
        }
        return true;
    }



}
