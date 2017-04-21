package file.tzy.com.tfilemanager.interfaces;

import java.io.File;

/**对文件检查结果感兴趣的视图*/
    public interface FileCheckedView{
        /**选择了非法类型的文件*/
        void onInvalidFileTypeSelectedError(File file);
        /**提示用户文件超过了限制，要开通云盘增值服务*/
        void onFileSizeLimitWarning(File file , int SizeLimit);
        /**提示用户正在非wifif网络下上传超过20M的文件*/
        void onNoWifiWarning(File file ,int SizeLimitInWifi);
        /**提示用户选择了大小为0的文件*/
        void onSelectNullFileError(File file);
        /**文件不存在时被回调**/
        void onFileNoExist(File file);
        /**视图标签，可以记录哪个视图在使用该类*/
        String getFCViewTag();
    }