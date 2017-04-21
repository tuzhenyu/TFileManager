package file.tzy.com.tfilemanager.utils;

import android.text.TextUtils;

import java.util.Locale;

import file.tzy.com.tfilemanager.R;

public class FileType {

    public enum FileFormat {
        OTHER,
        IMG,
        WORD,
        EXCEL,
        PPT,
        PDF,
        TXT,
        ZIP,
        AUDIO,
        VIDEO,
        UNKNOW_IMG,//其他图片格式 tif、webp
        VSD,
        XPC,//xml,pdg,caj
        DESIGN,
        WIN_EXE,
        APK
    }

    public static final String PREFIX_AMR = ".amr";
    public static final String PREFIX_JPEG = ".jpg";
    public static final String PREFIX_LINK = ".link";
    public static final String PREFIX_LOCATION = ".location";
    public static final String PREFIX_FILE = ".file";

    public static final int TYPE_UNKNOW = -1;
    public static final int TYPE_AMR = 1;
    public static final int TYPE_JPEG = 2;
    public static final int TYPE_LINK = 3;
    public static final int TYPE_LOCATION = 4;
    public static final int TYPE_FILE = 5;

    public static final String IAMGE_TYPE = "image/*";
    public static final String IAMGE_TYPE_PRE = "image/";
    public static final String mFileTypeImage[] = {"png", "gif", "jpg", "jpeg", "bmp"};

    public static final String mFileTypeUNKNOW_IMG[] = {"tif", "webp"};

    public static final String AUDIO_TYPE = "audio/*";
    public static final String AUDIO_TYPE_PRE = "audio/";
    public static final String mFileTypeAudio[] = {"mp3", "wav", "ogg", "midi","wma","ape","flac","aac","amr","m4a"};
    public static final String mFileTypeAudioCanPlay[] = {"mp3", "wav", "ogg","wma","ape","flac","aac","amr","m4a"};

    public static final String VIDEO_TYPE = "video/*";
    public static final String VIDEO_TYPE_PRE = "video/";
    public static final String mFileTypeVideo[] = {"rm","mp4", "rmvb", "avi", "flv","3gp","swf","wmv","mkv","mpv","m4v","mpg","mov","vob","asf"};
    public  static final String mFileTypeVideoCanPlay[] = {"rm","mp4", "rmvb", "avi", "flv","3gp","swf","wmv","mkv","m4v","mpg","mov","vob","asf"};

    private static final String HTML_TYPE = "text/html";
    private static final String mFileTypeWebText[] = {"htm", "html", "php", "jsp", "stm"};

    public static final String TXT_TYPE = "text/plain";
    private static final String mFileTypeText[] = {"txt", "xml"};

    private static final String DOC_TYPE = "application/msword";
    private static final String mFileTypeWord[] = {"doc", "docx", "wps","dot","rtf"};

    private static final String XLS_TYPE = "application/vnd.ms-excel";
    private static final String mFileTypeExcel[] = {"xls", "xlsx", "et","xla","xlc","xlt","xlw","csv"};

    private static final String mFileTypeVsd[] = {"vsd", "vsdx"};

    private static final String mFileTypeXPC[] = {"xml", "pdg","caj"};

    private static final String PPT_TYPE = "application/vnd.ms-powerpoint";
    private static final String mFileTypePPT[] = {"ppt", "pptx", "dps","pot"};

    public static final String PDF_TYPE = "application/pdf";
    private static final String mFileTypePdf[] = {"pdf"};
    public static final String wordFilesCanPreviewed[] = {"pdf","doc","docx","wps","rtf","xls","xlsx","et","csv","ppt","pptx","dps"};
    public static final String wordFilesText[] = {"txt","xml"};

    private static final String mFileTypeZip[] = {"zip", "rar","7z"};


    private static final String APK_TYPE = "application/vnd.android.package-archive";
    private static final String mFileTypeApk[] = {"jar", "zip", "rar", "gz", "apk", "img"};

    private static final String mFileTypeAPP[] = {"apk"};

    private static final String VCARD_TYPE = "text/x-vcard";
    private static final String mFileTypeVcard[] = {"vcf"};

    private static final String DEFAULT_TYPE = "";
    private static final String mFileTypePackage[] = {"jar", "zip", "rar", "gz"};

    private static final String mFileTypeDesign[] = {"dwg", "psd", "eps", "ndf","ai","cdr","enb"};

    private static final String mFileTypeExe[] = {"exe"};

    public static int getFileType(String fileName) {
        if (TextUtils.isEmpty(fileName)) {
            return TYPE_UNKNOW;
        }
        if (fileName.toLowerCase(Locale.getDefault()).endsWith(PREFIX_AMR)) {
            return TYPE_AMR;
        } else if (fileName.toLowerCase(Locale.getDefault()).endsWith(PREFIX_JPEG)) {
            return TYPE_JPEG;
        } else if (fileName.toLowerCase(Locale.getDefault()).endsWith(PREFIX_LINK)) {
            return TYPE_LINK;
        } else if (fileName.toLowerCase(Locale.getDefault()).endsWith(PREFIX_LOCATION)) {
            return TYPE_LOCATION;
        } else if (fileName.toLowerCase(Locale.getDefault()).endsWith(PREFIX_FILE)) {
            return TYPE_FILE;
        } else {
            return TYPE_UNKNOW;
        }
    }

    /**
     * 获取文件的打开类型对应方式
     *
     * @param fileSuffix 文件后缀
     * @return
     */
    public static String getOpenType(String fileSuffix) {
        String openType; // 第三方软件打开类型
        if (checkFileTypeInStringArray(fileSuffix, mFileTypeImage)) {
           // openType = IAMGE_TYPE;
            if(checkFileTypeEndWithString(fileSuffix,"png")){
                openType=IAMGE_TYPE_PRE+"png";
            }else if(checkFileTypeEndWithString(fileSuffix,"gif")){
                openType=IAMGE_TYPE_PRE+"gif";
            }else if(checkFileTypeEndWithString(fileSuffix,"bmp")){
                openType=IAMGE_TYPE_PRE+"bmp";
            }else {
                openType=IAMGE_TYPE_PRE+"jpeg";
            }
        } else if (checkFileTypeInStringArray(fileSuffix, mFileTypeVideo)) {
            //openType = VIDEO_TYPE;
            //{"rm","mp4", "rmvb", "avi", "flv","3gp","swf","wmv","mkv","mpv","m4v","mpg","mov","vob","asf"};
            if(checkFileTypeEndWithString(fileSuffix,"rm")){
                //openType=AUDIO_TYPE_PRE+"x-pn-realaudio"; //rm   audio/x-pn-realaudio
                openType=VIDEO_TYPE;//为了上报后台所需的数据类型，强转
            }else if(checkFileTypeEndWithString(fileSuffix,"mp4")){
                openType=VIDEO_TYPE_PRE+"mp4";
            }else if(checkFileTypeEndWithString(fileSuffix,"rmvb")){
                openType=VIDEO_TYPE_PRE+"vnd.rn-realvideo";
            }else if(checkFileTypeEndWithString(fileSuffix,"avi")){
                openType=VIDEO_TYPE_PRE+"x-msvideo";
            }else if(checkFileTypeEndWithString(fileSuffix,"flv")){
                openType=VIDEO_TYPE_PRE+"x-flv";
            }else if(checkFileTypeEndWithString(fileSuffix,"3gp")){
                openType=VIDEO_TYPE_PRE+"3gpp";
            }else if(checkFileTypeEndWithString(fileSuffix,"swf")){
               // openType="application/x-shockwave-flash";//swf	application/x-shockwave-flash
                openType=VIDEO_TYPE;//为了上报后台所需的数据类型，强转
            }else if(checkFileTypeEndWithString(fileSuffix,"wmv")){
                openType=VIDEO_TYPE_PRE+"x-ms-wmv";
            }else if(checkFileTypeEndWithString(fileSuffix,"mkv")){
                openType=VIDEO_TYPE_PRE+"x-matroska";
            }else if(checkFileTypeEndWithString(fileSuffix,"mpv") || checkFileTypeEndWithString(fileSuffix,"mpg")){
                openType=VIDEO_TYPE_PRE+"mpeg";
            }else if(checkFileTypeEndWithString(fileSuffix,"m4v")){
                openType=VIDEO_TYPE_PRE+"m4v";
            }else if(checkFileTypeEndWithString(fileSuffix,"mov")){
                openType=VIDEO_TYPE_PRE+"quicktime";
            }else if(checkFileTypeEndWithString(fileSuffix,"asf")){
                openType=VIDEO_TYPE_PRE+"x-ms-asf";
            }
            else {//"vob"
                openType=VIDEO_TYPE;
            }

        }else if (checkFileTypeInStringArray(fileSuffix, mFileTypeAudio)) {
            //openType = AUDIO_TYPE;
            // {"mp3", "wav", "ogg", "midi", "mr","wma","ape","flac","aac"};
            if(checkFileTypeEndWithString(fileSuffix,"mp3")){
                openType=AUDIO_TYPE_PRE+"mpeg";
            }else if(checkFileTypeEndWithString(fileSuffix,"wav")){
                openType=AUDIO_TYPE_PRE+"x-wav";
            }else if(checkFileTypeEndWithString(fileSuffix,"ogg")){
                openType=AUDIO_TYPE_PRE+"ogg";
            }else if(checkFileTypeEndWithString(fileSuffix,"midi")){
                openType=AUDIO_TYPE_PRE+"midi";
            }else if(checkFileTypeEndWithString(fileSuffix,"wma")){
                openType=AUDIO_TYPE_PRE+"x-ms-wma";
            }else if(checkFileTypeEndWithString(fileSuffix,"ape")){
                openType=AUDIO_TYPE_PRE+"ape";
            }else if(checkFileTypeEndWithString(fileSuffix,"aac")){
                openType=AUDIO_TYPE_PRE+"aac";
            }else if(checkFileTypeEndWithString(fileSuffix,"amr")){
                openType=AUDIO_TYPE_PRE+"amr";
            }
            else {//"mr","flac"
                openType=AUDIO_TYPE;
            }
        } else if (checkFileTypeInStringArray(fileSuffix, mFileTypeWebText)) {
            openType = HTML_TYPE;
        } else if (checkFileTypeInStringArray(fileSuffix, mFileTypePdf)) {
            openType = PDF_TYPE;
        } else if (checkFileTypeInStringArray(fileSuffix, mFileTypePPT)) {
            openType = PPT_TYPE;
        } else if (checkFileTypeInStringArray(fileSuffix, mFileTypeExcel)) {
            openType = XLS_TYPE;
        } else if (checkFileTypeInStringArray(fileSuffix, mFileTypeWord)) {
            openType = DOC_TYPE;
        }  else if (checkFileTypeInStringArray(fileSuffix, mFileTypeText)) {
            openType = TXT_TYPE;
        } else if (checkFileTypeInStringArray(fileSuffix, mFileTypeApk)) {
            openType = APK_TYPE;
        } else if (checkFileTypeInStringArray(fileSuffix, mFileTypePackage)) {
            openType = DEFAULT_TYPE; // 需要确定压缩格式的文件类型
        } else if (checkFileTypeInStringArray(fileSuffix, mFileTypeVcard)) {
            openType = VCARD_TYPE;
        } else if (checkFileTypeEndWithString(fileSuffix,"eml")) {
            openType = "message/rfc822";
        }else if (checkFileTypeEndWithString(fileSuffix,"rtf")) {
            openType = "text/rtf";
        }else if (checkFileTypeEndWithString(fileSuffix,"psd")) {
            openType = "image/vnd.adobe.photoshop";
        }
        else {
            openType = DEFAULT_TYPE;
        }
        return openType;

    }

    /**
     * 根据文件名获取Mime类型
     * */
    public static String getOpenTypeByFileName(String fileName) {
        String openType; // 第三方软件打开类型
        if (checkFileNameTypeInStringArray(fileName, mFileTypeImage)) {
            // openType = IAMGE_TYPE;
            if(checkFileTypeEndWithString(fileName,"png")){
                openType=IAMGE_TYPE_PRE+"png";
            }else if(checkFileTypeEndWithString(fileName,"gif")){
                openType=IAMGE_TYPE_PRE+"gif";
            }else if(checkFileTypeEndWithString(fileName,"bmp")){
                openType=IAMGE_TYPE_PRE+"bmp";
            }else {
                openType=IAMGE_TYPE_PRE+"jpeg";
            }
        } else if (checkFileNameTypeInStringArray(fileName, mFileTypeVideo)) {
            //openType = VIDEO_TYPE;
            //{"rm","mp4", "rmvb", "avi", "flv","3gp","swf","wmv","mkv","mpv","m4v","mpg","mov","vob","asf"};
            if(checkFileTypeEndWithString(fileName,"rm")){
                openType=AUDIO_TYPE_PRE+"x-pn-realaudio"; //rm   audio/x-pn-realaudio
            }else if(checkFileTypeEndWithString(fileName,"mp4")){
                openType=VIDEO_TYPE_PRE+"mp4";
            }else if(checkFileTypeEndWithString(fileName,"rmvb")){
                openType=VIDEO_TYPE_PRE+"vnd.rn-realvideo";
            }else if(checkFileTypeEndWithString(fileName,"avi")){
                openType=VIDEO_TYPE_PRE+"x-msvideo";
            }else if(checkFileTypeEndWithString(fileName,"flv")){
                openType=VIDEO_TYPE_PRE+"x-flv";
            }else if(checkFileTypeEndWithString(fileName,"3gp")){
                openType=VIDEO_TYPE_PRE+"3gpp";
            }else if(checkFileTypeEndWithString(fileName,"swf")){
                 openType="application/x-shockwave-flash";//swf	application/x-shockwave-flash
            }else if(checkFileTypeEndWithString(fileName,"wmv")){
                openType=VIDEO_TYPE_PRE+"x-ms-wmv";
            }else if(checkFileTypeEndWithString(fileName,"mkv")){
                openType=VIDEO_TYPE_PRE+"x-matroska";
            }else if(checkFileTypeEndWithString(fileName,"mpv") || checkFileTypeEndWithString(fileName,"mpg")){
                openType=VIDEO_TYPE_PRE+"mpeg";
            }else if(checkFileTypeEndWithString(fileName,"m4v")){
                openType=VIDEO_TYPE_PRE+"m4v";
            }else if(checkFileTypeEndWithString(fileName,"mov")){
                openType=VIDEO_TYPE_PRE+"quicktime";
            }else if(checkFileTypeEndWithString(fileName,"asf")){
                openType=VIDEO_TYPE_PRE+"x-ms-asf";
            }
            else {//"vob"
                openType=VIDEO_TYPE;
            }

        }else if (checkFileNameTypeInStringArray(fileName, mFileTypeAudio)) {
            //openType = AUDIO_TYPE;
            // {"mp3", "wav", "ogg", "midi", "mr","wma","ape","flac","aac"};
            if(checkFileTypeEndWithString(fileName,"mp3")){
                openType=AUDIO_TYPE_PRE+"mpeg";
            }else if(checkFileTypeEndWithString(fileName,"wav")){
                openType=AUDIO_TYPE_PRE+"x-wav";
            }else if(checkFileTypeEndWithString(fileName,"ogg")){
                openType=AUDIO_TYPE_PRE+"ogg";
            }else if(checkFileTypeEndWithString(fileName,"midi")){
                openType=AUDIO_TYPE_PRE+"midi";
            }else if(checkFileTypeEndWithString(fileName,"wma")){
                openType=AUDIO_TYPE_PRE+"x-ms-wma";
            }else if(checkFileTypeEndWithString(fileName,"ape")){
                openType=AUDIO_TYPE_PRE+"ape";
            }else if(checkFileTypeEndWithString(fileName,"aac")){
                openType=AUDIO_TYPE_PRE+"aac";
            }else if(checkFileTypeEndWithString(fileName,"amr")){
                openType=AUDIO_TYPE_PRE+"amr";
            }
            else {//"mr","flac"
                openType=AUDIO_TYPE;
            }
        } else if (checkFileNameTypeInStringArray(fileName, mFileTypeWebText)) {
            openType = HTML_TYPE;
        } else if (checkFileNameTypeInStringArray(fileName, mFileTypePdf)) {
            openType = PDF_TYPE;
        } else if (checkFileNameTypeInStringArray(fileName, mFileTypePPT)) {
            openType = PPT_TYPE;
        } else if (checkFileNameTypeInStringArray(fileName, mFileTypeExcel)) {
            openType = XLS_TYPE;
        } else if (checkFileNameTypeInStringArray(fileName, mFileTypeWord)) {
            openType = DOC_TYPE;
        }  else if (checkFileNameTypeInStringArray(fileName, mFileTypeText)) {
            openType = TXT_TYPE;
        } else if (checkFileNameTypeInStringArray(fileName, mFileTypeApk)) {
            openType = APK_TYPE;
        } else if (checkFileNameTypeInStringArray(fileName, mFileTypePackage)) {
            openType = DEFAULT_TYPE; // 需要确定压缩格式的文件类型
        } else if (checkFileNameTypeInStringArray(fileName, mFileTypeVcard)) {
            openType = VCARD_TYPE;
        } else if (checkFileTypeEndWithString(fileName,"eml")) {
            openType = "message/rfc822";
        }else if (checkFileTypeEndWithString(fileName,"rtf")) {
            openType = "text/rtf";
        }else if (checkFileTypeEndWithString(fileName,"psd")) {
            openType = "image/vnd.adobe.photoshop";
        }
        else {
            openType = DEFAULT_TYPE;
        }
        return openType;

    }

    /**
     * 获取文件类型
     * @param fileName 文件名
     */
    public static FileFormat getFileFormat(String fileName) {
        FileFormat result = FileFormat.OTHER;
        if(TextUtils.isEmpty(fileName)){
            return result;
        }
        if (checkFileNameTypeInStringArray(fileName, mFileTypeImage)) {
            result = FileFormat.IMG;
        } else if (checkFileNameTypeInStringArray(fileName, mFileTypePdf)) {
            result = FileFormat.PDF;
        } else if (checkFileNameTypeInStringArray(fileName, mFileTypePPT)) {
            result = FileFormat.PPT;
        } else if (checkFileNameTypeInStringArray(fileName, mFileTypeWord)) {
            result = FileFormat.WORD;
        } else if (checkFileNameTypeInStringArray(fileName, mFileTypeExcel)) {
            result = FileFormat.EXCEL;
        } else if (checkFileNameTypeInStringArray(fileName, mFileTypeZip)) {
            result = FileFormat.ZIP;
        } else if(checkFileNameTypeInStringArray(fileName, mFileTypeAudio)){
            result = FileFormat.AUDIO;
        } else if (checkFileNameTypeInStringArray(fileName, mFileTypeVideo)) {
            result = FileFormat.VIDEO;
        } else if (checkFileNameTypeInStringArray(fileName, mFileTypeText)) {
            result = FileFormat.TXT;
        } else if (checkFileNameTypeInStringArray(fileName,mFileTypeUNKNOW_IMG)){
            result = FileFormat.UNKNOW_IMG;
        } else if (checkFileNameTypeInStringArray(fileName,mFileTypeVsd)){
            result = FileFormat.VSD;
        } else if (checkFileNameTypeInStringArray(fileName,mFileTypeXPC)){
            result = FileFormat.XPC;
        } else if (checkFileNameTypeInStringArray(fileName,mFileTypeDesign)){
            result = FileFormat.DESIGN;
        } else if (checkFileNameTypeInStringArray(fileName,mFileTypeAPP)){
            result = FileFormat.APK;
        } else if (checkFileNameTypeInStringArray(fileName,mFileTypeExe)){
            result = FileFormat.WIN_EXE;
        }

        return result;
    }

    public static int getDrawableByFileType(FileType.FileFormat fileType) {
        if(fileType == null){
            return R.drawable.icon_unknow_file;
        }
        switch(fileType) {
            case WORD:
                return  R.drawable.icon_word_file;
            case EXCEL:
                return R.drawable.icon_excel_file;
            case IMG:
                return 0;
            case PDF:
                return R.drawable.icon_pdf_file;
            case PPT:
                return R.drawable.icon_ppt_file;
            case TXT:
                return R.drawable.icon_txt_file;
            case ZIP:
                return R.drawable.icon_zip;
            case VIDEO:
                return R.drawable.icon_avi_file;
            case AUDIO:
                return R.drawable.icon_audio_file;
            case UNKNOW_IMG:
                return R.drawable.icon_images_file;
            case VSD:
                return R.drawable.icon_vsd_file;
            case DESIGN:
                return R.drawable.icon_design_file;
            case WIN_EXE:
                return R.drawable.icon_exe_file;
            case XPC:
                return R.drawable.icon_xpc_file;
            case APK:
                return R.drawable.icon_apk_file;
            case OTHER:
            default:
                return R.drawable.icon_unknow_file;
        }
    }

    /**
     * 根据文件后缀匹配文件类型
     *
     * @param fileSuffix 文件后缀
     * @param array   文件类型集合Id
     * @return boolean
     */
    public static boolean checkFileTypeInStringArray(String fileSuffix, String array[]) {
        int dotIndex = fileSuffix.lastIndexOf('.');
        String extType ;
        if(dotIndex >= 0){
            extType = fileSuffix.substring(dotIndex + 1);
        }else{
            extType = fileSuffix;
        }

        for (String str : array) {
            if (extType.equalsIgnoreCase(str))
                return true;
        }
        return false;
    }

    /**
     * 根据文件名称匹配文件类型
     *
     * @param fileName 文件名
     * @param array   文件类型集合Id
     * @return boolean
     */
    public static boolean checkFileNameTypeInStringArray(String fileName, String array[]) {
        int dotIndex = fileName.lastIndexOf('.');
        String extType ;
        if(dotIndex >= 0){
            extType = fileName.substring(dotIndex + 1);
        }else{
            return false;
        }

        for (String str : array) {
            if (extType.equalsIgnoreCase(str))
                return true;
        }
        return false;
    }

    public static boolean checkFileTypeEndWithString(String fileName, String end) {
        return !(fileName == null || end == null) && fileName.toLowerCase(Locale.getDefault()).endsWith(end) ;
    }

}
