package com.xysss.ramandemo;


import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class DBInfo {
    public static final String APP_UPDATE_SER_NAME="CR2000.apk";
    public static final String LocalMatDtoLibName = "MatDtoLib";
    public static final String UnknowName = "未知物质";
    public static final String UnknowCategoryName = "未知分类";
    public static final String NatureDesc = "";
    public static final double confidence = 1.0;
    public static final int alarmFlag = 0;  //0 不报警 1报警
    public static final String UnknowId = "-1";
    public static final String SelfCategoryName = "自建库";
    public static final String URL_FORMOL = "https://detect.htvision.com.cn/api"; // 正式环境
    public static final String URL_TEST = "http://106.38.116.138:9527/api";  //测试环境
    public static final String SystemLanguage = "zh";
    public static final String SO_PATH = "/data/data/com.htnova.cr2000";
    public static final String A = "/data/data/com.htnova.cr2000/Mylibrary";
    public static final String B = "/data/data/com.htnova.cr2000/Mylibrary/Peak";
    public static final String C = "/data/data/com.htnova.cr2000/Mylibrary/ORC";
    /*public static final String D = "/data/data/com.htnova.cr2000/SPEC_EN";
    public static final String E = "/data/data/com.htnova.cr2000/SPEC_EN/ORC";
    public static final String F = "/data/data/com.htnova.cr2000/SPEC_EN/Info";*/
    public static final String G = "/data/data/com.htnova.cr2000/Customs";  //海关库路径
    public static final String H = "/data/data/com.htnova.cr2000/Police";  //公安库路径
    public static final String I = "/data/data/com.htnova.cr2000/Emergency";  //应急库路径
    public static final String J = "/data/data/com.htnova.cr2000/Enhance";  //增强库路径
    public static final String K = "/data/data/com.htnova.cr2000/Chemical";  //化学战剂库路径
    public static Map<String, String> like = new HashMap<String, String>();  //数据库保存
    public static String likeResult = "";  //数据库拼接
    public static Set<String> libCodes = new HashSet<String>();
    public static final String DB_MATERIAL_TEST_ID = "testId";  //测试id
    public static final String DB_MATERIAL_UUID = "Uuid";  //客户端生成的uuid
    public static final String DB_MATERIAL_NAME = "MaterialName";  //物质名称
    public static final String DB_MATERIAL_ID = "MaterialId";  //物质id
    public static final String DB_MATERIAL_CATEGORY_IDS = "categoryIds";  //分类id
    public static final String DB_MATERIAL_CATEGORY_NAMES = "categoryNames";  //分类名称
    public static final String DB_MATERIAL_DETAILS = "MaterialDetails";  //物质详情
    public static final String DB_MATERIAL_TEST_DATE = "TestDate";  //测试时间
    public static final String DB_MATERIAL_TEST_TIMESTAMP = "TestTimestamp";  //测试时间戳
    public static final String DB_MATERIAL_LONGITUDE = "Longitude";  //经度
    public static final String DB_MATERIAL_LATITUDE = "Latitude";  //纬度
    public static final String DB_MATERIAL_CONFIDENCE = "Confidence";  //置信度
    public static final String DB_MATERIAL_SPECTRUM = "Spectrum";  //光谱
    public static final String DB_MATERIAL_RESULT_ARRXDATA = "ResultArrxData";  //光谱的x轴偏移
    public static final String DB_MATERIAL_AFTER_ARRAYDATAY = "afterArrayDataY";  //处理后的光谱y轴偏移
    public static final String DB_MATERIAL_BEFORE_ARRAYDATAY = "beforeArrayDataY";  //原始光谱y轴偏移
    public static final String DB_MATERIAL_ISUPLOADED = "IsUploaded";  //光谱是否上传到服务器
    public static final String DB_MATERIAL_ADDRESS = "Address";  //定位地址
    public static final String DB_MATERIAL_PIC = "PictureUrl";  //存图片地址
    public static final String DB_SELF_TEST_ID = "SelfTestId";  //自建库数据id
    public static final String DB_SELF_MATERIAL_ID = "SelfMaterialId";  //自建库物质id
    public static final String DB_SELF_MATERIAL_URL = "SelfMaterialUrl";  //自建库物质存放路径
    public static final String DB_SELF_MATERIAL_NAME = "SelfMaterialName";  //自建库物质名称
    public static final String SP_DEVICE_ID = "DeviceId";  //设备ID
    public static final String SP_ADDR_INFO = "addressInfo";  //地理信息
    public static final String SP_LATITUDE = "latitude";  //GPS纬度
    public static final String SP_LONGITUDE = "longitude";  //GPS经度
    public static final String SP_INTENT_TAG = "intent_which";  //上传图片信息后，将本地路径保存在SP存储中；
    public static final String SP_WAVELENGTH = "Wavelength";  //波长
    public static final String SP_AXIAL_CALIBRATION = "axialCalibration";  //校准参数
    public static final String SP_TOKEN = "token";  //token
    public static final String SP_LaserId = "laserId";  //激光编号
    public static final String SP_SECURITY_KEY = "security_key";  //登录key
    public static final String SP_APP_CODE = "app_code";  //注册返回的设备类型
    public static final String SP_DEVICE_UUID = "device_uuid";  //注册返回的设备uuid
    public static final String SP_LASER_GEAR_1 = "lasergear1";  //激光校准
    public static final String SP_LASER_GEAR_2 = "lasergear2";
    public static final String SP_LASER_GEAR_3 = "lasergear3";
    public static final String SP_CCD_PGA = "ccdpga";  //ccdpga
    public static final String SP_CCD_OFFSET = "ccdoffset";  //ccdoffset
    public static final String SP_CHECK_TIME = "checktime";  //积分时间
    public static final String SP_CHECK_MODE_FLAG = "checkmodeflag";  //记录选中MODE位置的；0 快检模式 1精检 2深色 3增强 4自定义
    public static final String SP_CHECK_LASER_FLAG = "checklaserflag";  //记录选中LASER位置的；
    public static final String SP_CHECK_TIME_FLAG = "checktimeflag";  //记录选中TIME位置的；
    public static final String SP_CHECK_VOICE_FLAG = "checkvoiceflag";  //记录选中的声音开关
    public static final String SP_CHECK_MIXTURE_FLAG = "checkmixtureflag";   //记录选中混合物开关
    public static final String SP_CHECK_SELFlIBRARY_FLAG = "checkselflibraryflag";   //记录选中自建库开关
    public static final String SP_CHECK_WAITTIME_FLAG = "checkwaittimeflag";   //记录延时时间
    public static final String SP_HRAD_VERSION = "hradversion";   //硬件版本及两种状态
    public static final String SP_LASER_STATE = "laserstate";  //激光状态
    public static final String SP_CCD_STATE = "ccdstate";  //ccd状态
    public static final String SP_HeartBeatInterval = "heartbeatinterval";  //服务器中返回的心跳时间
    public static final String SP_CHECK_MODE_Library_FLAG = "checkModeLibcode";  //设置检测库版本的类型
    public static final String SP_iSFentanyl = "isFentanyl";  //是否是芬太尼模式
    public static final String SP_ANNEX_IDENTIFY = "annex_identify";  //附件识别
    public static final String SP_URL = "url";   //域名设置
    public static final String SP_SELF_URL = "selfUrl";   //自定义域名
    public static int LIB_CODE=0;
}
